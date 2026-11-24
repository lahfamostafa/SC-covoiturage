package ma.youcode.covoiturage.vehicule.service;

import java.util.List;

import ma.youcode.covoiturage.vehicule.dto.VehiculeRequestDTO;
import ma.youcode.covoiturage.vehicule.dto.VehiculeResponseDTO;

public interface VehiculeService {

    VehiculeResponseDTO creerVehicule(VehiculeRequestDTO request);

    List<VehiculeResponseDTO> getVehicules();

    VehiculeResponseDTO getVehiculeById(Long id);

    VehiculeResponseDTO modifierVehicule(Long id, VehiculeRequestDTO request);

    List<VehiculeResponseDTO> getVehiculesByConducteurId(Long conducteurId);

    void supprimerVehicule(Long id);
}
