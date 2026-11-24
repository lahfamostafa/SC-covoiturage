package ma.youcode.covoiturage.passager.dto;

public record PassagerResponseDTO(
    Long id,
    String nom,
    String prenom,
    String email,
    String telephone
) {}
