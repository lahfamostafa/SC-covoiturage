
package ma.youcode.covoiturage.vehicule.dto;

import jakarta.validation.constraints.*;

public record VehiculeRequestDTO(

        @NotBlank(message = "La marque est obligatoire")
        @Size(max = 100, message = "La marque ne doit pas dépasser 100 caractères")
        String marque,

        @NotBlank(message = "Le modèle est obligatoire")
        @Size(max = 100, message = "Le modèle ne doit pas dépasser 100 caractères")
        String modele,

        @NotBlank(message = "L'immatriculation est obligatoire")
        @Size(max = 50, message = "L'immatriculation ne doit pas dépasser 50 caractères")
        String immatriculation,

        @NotBlank(message = "La couleur est obligatoire")
        @Size(max = 50, message = "La couleur ne doit pas dépasser 50 caractères")
        String couleur,

        @NotNull(message = "La capacité est obligatoire")
        @Min(value = 1, message = "La capacité minimale est de 1 place")
        @Max(value = 8, message = "La capacité maximale est de 8 places")
        Integer capacite,

        @NotNull(message = "L'identifiant du conducteur est obligatoire")
        @Positive(message = "L'identifiant du conducteur doit être positif")
        Long conducteurId

) {}
