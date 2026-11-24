package ma.youcode.covoiturage.trajet.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ma.youcode.covoiturage.commun.dto.PageResponseDTO;
import ma.youcode.covoiturage.commun.exception.AlreadyExistsException;
import ma.youcode.covoiturage.commun.exception.ResourceNotFoundException;
import ma.youcode.covoiturage.commun.exception.VehiculeConducteurMismatchException;
import ma.youcode.covoiturage.conducteur.entity.Conducteur;
import ma.youcode.covoiturage.conducteur.repository.ConducteurRepository;
import ma.youcode.covoiturage.enums.StatutReservation;
import ma.youcode.covoiturage.enums.StatutTrajet;
import ma.youcode.covoiturage.reservation.repository.ReservationRepository;
import ma.youcode.covoiturage.trajet.dto.PointArretRequestDTO;
import ma.youcode.covoiturage.trajet.dto.TrajetDetailResponseDTO;
import ma.youcode.covoiturage.trajet.dto.TrajetRechercheDTO;
import ma.youcode.covoiturage.trajet.dto.TrajetRequestDTO;
import ma.youcode.covoiturage.trajet.dto.TrajetResponseDTO;
import ma.youcode.covoiturage.trajet.entity.PointArret;
import ma.youcode.covoiturage.trajet.entity.Trajet;
import ma.youcode.covoiturage.trajet.mapper.TrajetMapper;
import ma.youcode.covoiturage.trajet.repository.TrajetRepository;
import ma.youcode.covoiturage.trajet.service.TrajetService;
import ma.youcode.covoiturage.vehicule.entity.Vehicule;
import ma.youcode.covoiturage.vehicule.repository.VehiculeRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class TrajetServiceImpl implements TrajetService {

    private final TrajetRepository trajetRepository;
    private final ConducteurRepository conducteurRepository;
    private final VehiculeRepository vehiculeRepository;
    private final TrajetMapper trajetMapper;
    private final ReservationRepository reservationRepository;

    @Override
    public TrajetResponseDTO creerTrajet(TrajetRequestDTO request) {

        Conducteur conducteur = conducteurRepository.findById(request.conducteurId())
                .orElseThrow(() -> new ResourceNotFoundException("Conducteur introuvable avec l'id : " + request.conducteurId()));

        Vehicule vehicule = vehiculeRepository.findById(request.vehiculeId())
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable avec l'id : " + request.vehiculeId()));

        verifierVehiculeConducteur(vehicule, conducteur);
        verifierCapaciteVehicule(vehicule, request.placesProposees());
        verifierPointsArret(request.pointsArret(), request.dateDepart());
        verifierDoublon(request.conducteurId(), request.dateDepart());

        Trajet trajet = new Trajet(conducteur, vehicule, request.dateDepart(), request.placesProposees());

        for (PointArretRequestDTO pointDTO : request.pointsArret()) {
            PointArret point = new PointArret(
                    pointDTO.ville(),
                    pointDTO.ordre(),
                    pointDTO.heurePassage(),
                    pointDTO.prixDepuisDepart());

            trajet.ajouterPointArret(point);
        }

        Trajet trajetEnregistre = trajetRepository.save(trajet);

        return trajetMapper.toResponseDTO(trajetEnregistre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrajetResponseDTO> getTrajets() {
        return trajetRepository.findAll().stream().map(trajetMapper::toResponseDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TrajetResponseDTO getTrajetById(Long id) {

        Trajet trajet = trajetRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Trajet introuvable avec l'id : " + id));
        return trajetMapper.toResponseDTO(trajet);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrajetResponseDTO> getTrajetsByConducteurId(Long conducteurId) {

        conducteurRepository.findById(conducteurId).orElseThrow(() -> new ResourceNotFoundException("Conducteur introuvable avec l'id : " + conducteurId));
        return trajetRepository.findByConducteur_Id(conducteurId).stream().map(trajetMapper::toResponseDTO).toList();
    }

    @Override
    public TrajetResponseDTO modifierTrajet(Long id, TrajetRequestDTO request) {

        Trajet trajet = trajetRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Trajet introuvable avec l'id : " + id));

        // Le conducteur et le véhicule restent ceux définis à la création.
        if (!trajet.getConducteur().getId().equals(request.conducteurId())
                || !trajet.getVehicule().getId().equals(request.vehiculeId())) {
            throw new VehiculeConducteurMismatchException("Le conducteur et le véhicule d'un trajet ne peuvent pas être modifiés");
        }

        verifierCapaciteVehicule(trajet.getVehicule(), request.placesProposees());

        verifierPointsArret(request.pointsArret(), request.dateDepart());

        trajet.modifierInformations(request.dateDepart(), request.placesProposees());

        List<PointArret> nouveauxPoints = request.pointsArret()
                .stream()
                .map(pointDTO -> new PointArret(pointDTO.ville(), pointDTO.ordre(), pointDTO.heurePassage(), pointDTO.prixDepuisDepart()))
                .toList();

        trajet.remplacerPointsArret(nouveauxPoints);

        return trajetMapper.toResponseDTO(trajet);
    }

    @Override
    public void supprimerTrajet(Long id) {

        Trajet trajet = trajetRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Trajet introuvable avec l'id : " + id));
        trajetRepository.delete(trajet);
    }

    private void verifierVehiculeConducteur(Vehicule vehicule, Conducteur conducteur) {
        if (!vehicule.getConducteur().getId().equals(conducteur.getId())) {
            throw new VehiculeConducteurMismatchException("Le véhicule sélectionné n'appartient pas à ce conducteur");
        }
    }

    private void verifierCapaciteVehicule(Vehicule vehicule, Integer placesProposees) {
        if (placesProposees > vehicule.getCapacite()) {
            throw new IllegalArgumentException("Le nombre de places proposées dépasse la capacité du véhicule");
        }
    }

    private void verifierPointsArret(List<PointArretRequestDTO> points, LocalDateTime dateDepart) {
        if (points == null || points.size() < 2) {
            throw new IllegalArgumentException("Un trajet doit avoir au minimum deux points d'arrêt");
        }

        Set<String> villes = new HashSet<>();

        for (PointArretRequestDTO point : points) {
            String villeNormalisee = point.ville().trim().toLowerCase(Locale.ROOT);

            if (!villes.add(villeNormalisee)) {
                throw new AlreadyExistsException("La ville " + point.ville()+ " existe déjà dans cet itinéraire.");
            }
        }

        if (points.get(0).ordre() != 0) {
            throw new IllegalArgumentException("Le premier point d'arrêt doit avoir l'ordre 0");
        }

        if (points.get(0).prixDepuisDepart().compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalArgumentException("Le prix du premier point doit être égal à 0");
        }

        for (int i = 0; i < points.size(); i++) {

            PointArretRequestDTO actuel = points.get(i);

            if (actuel.ordre() != i) {
                throw new IllegalArgumentException("Les ordres des points d'arrêt doivent être consécutifs et commencer à 0");
            }

            if (actuel.heurePassage().isBefore(dateDepart)) {
                throw new IllegalArgumentException("L'heure de passage ne peut pas être antérieure au départ");
            }

            if (i > 0) {
                PointArretRequestDTO precedent = points.get(i - 1);

                if (!actuel.heurePassage().isAfter(precedent.heurePassage())) {
                    throw new IllegalArgumentException("Les heures de passage doivent être strictement croissantes");
                }

                if (actuel.prixDepuisDepart().compareTo(precedent.prixDepuisDepart()) <= 0) {
                    throw new IllegalArgumentException("Les prix depuis le départ doivent être strictement croissants");
                }
            }
        }
    }

    private void verifierDoublon(Long conducteurId,LocalDateTime dateDepart) {
        boolean existe = trajetRepository
            .existsByConducteur_IdAndDateDepartAndStatutNot(conducteurId, dateDepart, StatutTrajet.ANNULE);

        if (existe) {
            throw new AlreadyExistsException("Un trajet existe déjà pour ce conducteur à cette date.");
        }
    }

    // @Override
    // public PageResponseDTO<TrajetResponseDTO> rechercherTrajets(String villeDepart, String villeArrivee, LocalDate date, Pageable pageable) {
    //     LocalDateTime debutJour = date.atStartOfDay();
    //     LocalDateTime finJour = date.plusDays(1).atStartOfDay();

    //     Page<TrajetResponseDTO> resultats = trajetRepository.findTrajets(villeDepart.trim(), villeArrivee.trim(), debutJour, finJour, StatutTrajet.PUBLIE, pageable)
    //         .map(trajetMapper::toResponseDTO);

    //     return PageResponseDTO.from(resultats);
    // }

    @Override
    public PageResponseDTO<TrajetRechercheDTO> rechercherTrajetsAvances(String villeDepart, String villeArrivee, LocalDate date, Integer nombrePlaces, BigDecimal prixMax, Pageable pageable) {

        LocalDateTime debutJour = date != null ? date.atStartOfDay() : null;

        LocalDateTime finJour = date != null ? date.plusDays(1).atStartOfDay() : null;

        Pageable pagination = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(),Sort.by(Sort.Direction.ASC, "dateDepart"));

        Page<TrajetRechercheDTO> resultats =
            trajetRepository.rechercherTrajets(
                villeDepart.trim(),
                villeArrivee.trim(),
                debutJour,
                finJour,
                nombrePlaces,
                prixMax,
                StatutTrajet.PUBLIE,
                StatutReservation.CONFIRMEE,
                pagination
            );

        return PageResponseDTO.from(resultats);
    }

    @Override
    @Transactional(readOnly = true)
    public TrajetDetailResponseDTO getTrajetDetails(Long id) {

        Trajet trajet = trajetRepository.findWithPointsArretById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trajet introuvable avec l'id : " + id));

        Long placesReservees = reservationRepository.sumPlacesReservees(id,StatutReservation.CONFIRMEE);

        Integer placesRestantes = trajet.getPlacesProposees() - placesReservees.intValue();

        TrajetResponseDTO trajetDTO = trajetMapper.toResponseDTO(trajet);

        return new TrajetDetailResponseDTO(trajetDTO, placesRestantes);
    }
}