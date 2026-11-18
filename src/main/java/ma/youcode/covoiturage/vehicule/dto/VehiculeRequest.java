package ma.youcode.covoiturage.vehicule.dto;

import jakarta.validation.constraints.*;

public record VehiculeRequest(

    @NotBlank (message = "La marque est obligatoire")
    String marque,

    @NotBlank(message = "Le modèle est obligatoire")
    String modele,

    @NotBlank(message = "L'immatriculation est obligatoire")
    String immatriculation,

    @NotBlank(message = "La couleur est obligatoire")
    String couleur,

    @NotNull(message = "La capacité est obligatoire")
    @Min(value = 1, message = "La capacité minimale est de 1")
    @Max(value = 8, message = "La capacité maximale est de 8")
    Integer capacite,

    @NotNull(message = "L'identifiant du conducteur est obligatoire")
    Long conducteurId

) {}
