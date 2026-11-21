package ma.youcode.covoiturage.trajet.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PointArretResponseDTO(
        Long id,
        String ville,
        Integer ordre,
        LocalDateTime heurePassage,
        BigDecimal prixDepuisDepart
) {}