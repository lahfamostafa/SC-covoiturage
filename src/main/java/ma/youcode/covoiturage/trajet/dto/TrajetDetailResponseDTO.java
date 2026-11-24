package ma.youcode.covoiturage.trajet.dto;

public record TrajetDetailResponseDTO(
        TrajetResponseDTO trajet,
        Integer placesRestantes
) {}
