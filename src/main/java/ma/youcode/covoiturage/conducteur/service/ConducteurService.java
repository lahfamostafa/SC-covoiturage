package ma.youcode.covoiturage.conducteur.service;

import java.util.List;

import ma.youcode.covoiturage.conducteur.dto.ConducteurRequestDTO;
import ma.youcode.covoiturage.conducteur.dto.ConducteurResponseDTO;

public interface ConducteurService {
    List<ConducteurResponseDTO> getConducteurs();
    ConducteurResponseDTO creerConducteur(ConducteurRequestDTO request);
    ConducteurResponseDTO getConducteur(Long id);
    ConducteurResponseDTO updateConducteur(Long id, ConducteurRequestDTO requestDTO);
}
