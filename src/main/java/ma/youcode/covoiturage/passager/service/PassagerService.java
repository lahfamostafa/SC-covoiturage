package ma.youcode.covoiturage.passager.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import ma.youcode.covoiturage.commun.dto.PageResponseDTO;
import ma.youcode.covoiturage.passager.dto.PassagerRequestDTO;
import ma.youcode.covoiturage.passager.dto.PassagerResponseDTO;

public interface PassagerService {

    List<PassagerResponseDTO> getPassagers();

    PassagerResponseDTO creerPassager(PassagerRequestDTO request);

    PassagerResponseDTO getPassager(Long id);

    PassagerResponseDTO updatePassager(Long id, PassagerRequestDTO requestDTO);

    PageResponseDTO<PassagerResponseDTO> rechercherParNomOuEmail(String terme, Pageable pageable);
}
