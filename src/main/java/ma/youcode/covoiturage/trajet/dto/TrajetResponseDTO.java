package ma.youcode.covoiturage.trajet.dto;

import java.time.LocalDateTime;
import java.util.List;

import ma.youcode.covoiturage.enums.StatutTrajet;


public record TrajetResponseDTO(
        Long id,
        LocalDateTime dateDepart,
        Integer placesProposees,
        StatutTrajet statut,
        LocalDateTime createdAt,
        Long conducteurId,
        Long vehiculeId,
        List<PointArretResponseDTO> pointsArret
) {}