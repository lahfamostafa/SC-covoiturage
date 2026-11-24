package ma.youcode.covoiturage.trajet.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TrajetRechercheDTO(
    Long trajetId,
    LocalDateTime dateDepart,
    String villeDepart,
    String villeArrivee,
    BigDecimal prixSegment,
    Long placesRestantes,
    Integer placesProposees,
    Long conducteurId,
    Long vehiculeId
) {}