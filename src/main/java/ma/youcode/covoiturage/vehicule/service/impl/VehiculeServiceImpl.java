
package ma.youcode.covoiturage.vehicule.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import ma.youcode.covoiturage.conducteur.entity.Conducteur;
import ma.youcode.covoiturage.conducteur.repository.ConducteurRepository;
import ma.youcode.covoiturage.exception.ResourceNotFoundException;
import ma.youcode.covoiturage.exception.VehiculeAssocieATrajetException;
import ma.youcode.covoiturage.exception.AlreadyExistsException;
import ma.youcode.covoiturage.exception.VehiculeConducteurMismatchException;
import ma.youcode.covoiturage.trajet.repository.TrajetRepository;
import ma.youcode.covoiturage.vehicule.dto.VehiculeRequestDTO;
import ma.youcode.covoiturage.vehicule.dto.VehiculeResponseDTO;
import ma.youcode.covoiturage.vehicule.entity.Vehicule;
import ma.youcode.covoiturage.vehicule.mapper.VehiculeMapper;
import ma.youcode.covoiturage.vehicule.repository.VehiculeRepository;
import ma.youcode.covoiturage.vehicule.service.VehiculeService;

@Service
@RequiredArgsConstructor
@Transactional
public class VehiculeServiceImpl implements VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final ConducteurRepository conducteurRepository;
    private final VehiculeMapper vehiculeMapper;
    private final TrajetRepository trajetRepository;

    @Override
    public VehiculeResponseDTO creerVehicule(VehiculeRequestDTO request) {

        if (vehiculeRepository.existsByImmatriculation(request.immatriculation())) {
            throw new AlreadyExistsException("Cette immatriculation existe déjà");
        }

        Conducteur conducteur = conducteurRepository.findById(request.conducteurId())
                .orElseThrow(() -> new ResourceNotFoundException("Conducteur introuvable avec l'id : " + request.conducteurId()));

        Vehicule vehicule = new Vehicule(request.marque(), request.modele(), request.immatriculation(), request.couleur(), request.capacite(), conducteur);

        conducteur.enregistrerVehicule(vehicule);

        Vehicule savedVehicule = vehiculeRepository.save(vehicule);

        return vehiculeMapper.toResponseDTO(savedVehicule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculeResponseDTO> getVehicules() {

        return vehiculeRepository.findAll().stream().map(vehiculeMapper::toResponseDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VehiculeResponseDTO getVehiculeById(Long id) {

        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable avec l'id : " + id));

        return vehiculeMapper.toResponseDTO(vehicule);
    }

    @Override
    public VehiculeResponseDTO modifierVehicule(Long id, VehiculeRequestDTO request) {

        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable avec l'id : " + id));

        if (!vehicule.getConducteur().getId().equals(request.conducteurId())) {
            throw new VehiculeConducteurMismatchException("Le conducteur d'un véhicule ne peut pas être modifié");
        }

        if (vehiculeRepository.existsByImmatriculationAndIdNot(request.immatriculation(), id)) {
            throw new AlreadyExistsException("Cette immatriculation est déjà utilisée");
        }

        vehicule.modifierInformations(
                request.marque(),
                request.modele(),
                request.immatriculation(),
                request.couleur(),
                request.capacite()
        );

        return vehiculeMapper.toResponseDTO(vehicule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehiculeResponseDTO> getVehiculesByConducteurId(Long conducteurId) {

        conducteurRepository.findById(conducteurId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Conducteur introuvable avec l'id : " + conducteurId
                ));

        return vehiculeRepository.findByConducteur_Id(conducteurId)
                .stream()
                .map(vehiculeMapper::toResponseDTO)
                .toList();
    }

     @Override
    public void supprimerVehicule(Long id) {

        Vehicule vehicule = vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Véhicule introuvable avec l'id : " + id
                ));

        boolean estAssocieATrajet = trajetRepository.existsByVehicule_Id(id);

        if (estAssocieATrajet) {
            throw new VehiculeAssocieATrajetException(
                    "Impossible de supprimer ce véhicule : il est associé à un ou plusieurs trajets"
            );
        }

        vehiculeRepository.delete(vehicule);
    }
}
