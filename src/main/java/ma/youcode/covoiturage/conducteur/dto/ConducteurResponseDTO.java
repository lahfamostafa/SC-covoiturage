package ma.youcode.covoiturage.conducteur.dto;

public record ConducteurResponseDTO(
    Long id,
    String nom,
    String prenom,
    String email,
    String telephone,
    String numeroPermis
) {}
