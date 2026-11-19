package ma.youcode.covoiturage.vehicule.dto;

public record VehiculeResponseDTO(
        Long id,
        String marque,
        String modele,
        String immatriculation,
        String couleur,
        Integer capacite,
        Long conducteurId
) {}
