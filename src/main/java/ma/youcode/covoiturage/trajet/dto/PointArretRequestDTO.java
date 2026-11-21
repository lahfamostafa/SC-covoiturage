package ma.youcode.covoiturage.trajet.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.*;

public record PointArretRequestDTO(

        @NotBlank(message = "La ville est obligatoire")
        @Size(max = 150, message = "La ville ne doit pas dépasser 150 caractères")
        String ville,

        @NotNull(message = "L'ordre est obligatoire")
        @Min(value = 0, message = "L'ordre doit être supérieur ou égal à 0")
        Integer ordre,

        @NotNull(message = "L'heure de passage est obligatoire")
        LocalDateTime heurePassage,

        @NotNull(message = "Le prix depuis le départ est obligatoire")
        @DecimalMin(value = "0.00", message = "Le prix ne peut pas être négatif")
        @Digits(integer = 8, fraction = 2, message = "Format de prix invalide")
        BigDecimal prixDepuisDepart

) {}