package ma.youcode.covoiturage.trajet.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;

import ma.youcode.covoiturage.commun.dto.PageResponseDTO;
import ma.youcode.covoiturage.trajet.dto.TrajetDetailResponseDTO;
import ma.youcode.covoiturage.trajet.dto.TrajetRechercheDTO;
import ma.youcode.covoiturage.trajet.dto.TrajetRequestDTO;
import ma.youcode.covoiturage.trajet.dto.TrajetResponseDTO;

public interface TrajetService {

    TrajetResponseDTO creerTrajet(TrajetRequestDTO request);

    List<TrajetResponseDTO> getTrajets();

    TrajetResponseDTO getTrajetById(Long id);

    List<TrajetResponseDTO> getTrajetsByConducteurId(Long conducteurId);

    TrajetResponseDTO modifierTrajet(Long id, TrajetRequestDTO request);

    void supprimerTrajet(Long id);

    TrajetDetailResponseDTO getTrajetDetails(Long id);

    PageResponseDTO<TrajetRechercheDTO> rechercherTrajetsAvances(String villeDepart, String villeArrivee, LocalDate date, Integer nombrePlaces, BigDecimal prixMax, Pageable pageable);
}