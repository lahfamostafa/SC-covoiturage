package ma.youcode.covoiturage.trajet.service;

import java.util.List;

import ma.youcode.covoiturage.trajet.dto.TrajetRequestDTO;
import ma.youcode.covoiturage.trajet.dto.TrajetResponseDTO;

public interface TrajetService {

    TrajetResponseDTO creerTrajet(TrajetRequestDTO request);

    List<TrajetResponseDTO> getTrajets();

    TrajetResponseDTO getTrajetById(Long id);

    List<TrajetResponseDTO> getTrajetsByConducteurId(Long conducteurId);

    TrajetResponseDTO modifierTrajet(Long id, TrajetRequestDTO request);

    void supprimerTrajet(Long id);
}