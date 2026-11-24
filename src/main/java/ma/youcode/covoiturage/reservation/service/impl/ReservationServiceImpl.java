package ma.youcode.covoiturage.reservation.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import ma.youcode.covoiturage.commun.exception.AlreadyExistsException;
import ma.youcode.covoiturage.commun.exception.ResourceNotFoundException;
import ma.youcode.covoiturage.enums.StatutReservation;
import ma.youcode.covoiturage.enums.StatutTrajet;
import ma.youcode.covoiturage.passager.entity.Passager;
import ma.youcode.covoiturage.passager.repository.PassagerRepository;
import ma.youcode.covoiturage.reservation.dto.ReservationConducteurResponseDTO;
import ma.youcode.covoiturage.reservation.dto.ReservationRequestDTO;
import ma.youcode.covoiturage.reservation.dto.ReservationResponseDTO;
import ma.youcode.covoiturage.reservation.entity.Reservation;
import ma.youcode.covoiturage.reservation.mapper.ReservationMapper;
import ma.youcode.covoiturage.reservation.repository.ReservationRepository;
import ma.youcode.covoiturage.reservation.service.ReservationService;
import ma.youcode.covoiturage.trajet.entity.PointArret;
import ma.youcode.covoiturage.trajet.entity.Trajet;
import ma.youcode.covoiturage.trajet.repository.TrajetRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final TrajetRepository trajetRepository;
    private final PassagerRepository passagerRepository;
    private final ReservationMapper reservationMapper;

    @Override
    public ReservationResponseDTO reserver(Long passagerId,ReservationRequestDTO request) {

        if (request == null) {
            throw new IllegalArgumentException("Les informations de réservation sont obligatoires");
        }

        // 1. Récupérer le passager
        Passager passager = passagerRepository.findById(passagerId)
                .orElseThrow(() -> new ResourceNotFoundException("Passager introuvable : " + passagerId));

        // 2. Récupérer et verrouiller le trajet
        Trajet trajet = trajetRepository.findByIdForUpdate(request.trajetId())
                .orElseThrow(() -> new ResourceNotFoundException("Trajet introuvable : " + request.trajetId()));

        // 3. Vérifier la disponibilité
        if (trajet.getStatut() != StatutTrajet.PUBLIE) {
            throw new IllegalStateException("Ce trajet n'est pas disponible à la réservation");
        }

        // 4. Vérifier les points d'arrêt
        PointArret pointMontee = trouverPointArret(trajet, request.pointMonteeId());

        PointArret pointDescente = trouverPointArret(trajet, request.pointDescenteId());

        // 5. Vérifier l'ordre des points
        if (pointMontee.getOrdre() >= pointDescente.getOrdre()) {
            throw new IllegalArgumentException("Le point de montée doit précéder le point de descente");
        }

        // 6. Vérifier la réservation existante
        boolean reservationExistante = reservationRepository.existsByPassager_IdAndTrajet_IdAndStatut(passagerId, trajet.getId(), StatutReservation.CONFIRMEE);

        if (reservationExistante) {
            throw new AlreadyExistsException("Vous avez déjà une réservation confirmée pour ce trajet");
        }

        // 7. Calculer toutes les places réservées sur le trajet
        Long placesReservees = reservationRepository.sumPlacesReservees(trajet.getId(), StatutReservation.CONFIRMEE);

        // 8. Calculer les places disponibles
        long placesDisponibles = trajet.getPlacesProposees() - placesReservees;

        if (request.nombrePlaces() > placesDisponibles) {
            throw new IllegalStateException("Nombre de places insuffisant. Places disponibles : " + placesDisponibles);
        }

        // 9. Calculer le montant
        BigDecimal prixUnitaire = pointDescente.getPrixDepuisDepart()
                .subtract(pointMontee.getPrixDepuisDepart());

        if (prixUnitaire.signum() < 0) {
            throw new IllegalStateException("Le prix du point de descente est invalide");
        }

        BigDecimal montantTotal = prixUnitaire.multiply(BigDecimal.valueOf(request.nombrePlaces()));

        // 10. Créer la réservation
        Reservation reservation = new Reservation(trajet, passager, pointMontee, pointDescente, request.nombrePlaces(), montantTotal);

        // 11. Enregistrer
        Reservation reservationEnregistree = reservationRepository.save(reservation);

        // 12. Vérifier la saturation globale du trajet
        Long totalPlacesReservees = reservationRepository.sumPlacesReservees(trajet.getId(),StatutReservation.CONFIRMEE);

        if (totalPlacesReservees >= trajet.getPlacesProposees()) {
            trajet.changerStatut(StatutTrajet.COMPLET);
        }

        return reservationMapper.toResponseDTO(reservationEnregistree);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> consulterMesReservations(Long passagerId) {

        if (!passagerRepository.existsById(passagerId)) {
            throw new ResourceNotFoundException("Passager introuvable : " + passagerId);
        }

        return reservationRepository
            .findByPassager_IdOrderByDateReservationDesc(passagerId)
            .stream()
            .map(reservationMapper::toResponseDTO)
            .toList();
    }

    @Override
    public void annulerReservation(Long reservationId, Long passagerId) {

        // 1. Récupérer la réservation
        Reservation reservation = reservationRepository
                .findByIdAndPassager_Id(reservationId, passagerId)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation introuvable : " + reservationId));

        // 2. Récupérer et verrouiller le trajet
        Trajet trajet = trajetRepository.findByIdForUpdate(
                reservation.getTrajet().getId()).orElseThrow( () -> new ResourceNotFoundException("Trajet introuvable"));

        // 3. Annuler la réservation
        reservation.annuler();

        // 4. Vérifier le statut du trajet
        if (trajet.getStatut() == StatutTrajet.COMPLET) {

            // 5. Recalculer les places confirmées
            Long totalPlacesReservees = reservationRepository.sumPlacesReservees(trajet.getId(),StatutReservation.CONFIRMEE);

            if (totalPlacesReservees < trajet.getPlacesProposees()) {

                // 6. Réactiver le trajet
                trajet.changerStatut(StatutTrajet.PUBLIE);
            }
        }
    }

    private PointArret trouverPointArret(Trajet trajet, Long pointArretId) {

        return trajet.getPointsArret()
                .stream()
                .filter(point -> point.getId().equals(pointArretId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Point d'arrêt introuvable dans ce trajet : "+ pointArretId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationConducteurResponseDTO> consulterReservationsDuTrajet(Long trajetId,Long conducteurId) {

        // 1. Vérifier que le trajet existe
        Trajet trajet = trajetRepository.findById(trajetId)
                .orElseThrow(() -> new ResourceNotFoundException("Trajet introuvable : " + trajetId));

        // 2. Vérifier que le trajet appartient au conducteur
        if (!trajet.getConducteur().getId().equals(conducteurId)) {
            throw new ResourceNotFoundException("Trajet introuvable : " + trajetId);
        }

        // 3. Récupérer les réservations avec leurs relations
        List<Reservation> reservations = reservationRepository.findReservationsByTrajetId(trajetId);

        // 4. Convertir en DTO
        return reservations.stream()
            .map(reservation -> new ReservationConducteurResponseDTO(
                reservation.getId(),
                reservation.getPassager().getId(),
                reservation.getPassager().getNom(),
                reservation.getPassager().getPrenom(),
                reservation.getPassager().getEmail(),
                reservation.getPassager().getTelephone(),
                reservation.getPointMontee().getId(),
                reservation.getPointMontee().getVille(),
                reservation.getPointDescente().getId(),
                reservation.getPointDescente().getVille(),
                reservation.getNombrePlaces(),
                reservation.getMontantTotal(),
                reservation.getDateReservation(),
                reservation.getStatut()
            )).toList();
    }
}