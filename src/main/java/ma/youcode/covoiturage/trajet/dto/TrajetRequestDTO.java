package ma.youcode.covoiturage.trajet.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record TrajetRequestDTO(

        @NotNull(message = "La date de départ est obligatoire")
        @Future(message = "La date de départ doit être dans le futur")
        LocalDateTime dateDepart,

        @NotNull(message = "Le nombre de places est obligatoire")
        @Min(value = 1, message = "Il faut proposer au minimum une place")
        @Max(value = 8, message = "Le nombre maximum de places est 8")
        Integer placesProposees,

        @NotNull(message = "L'identifiant du conducteur est obligatoire")
        @Positive(message = "L'identifiant du conducteur doit être positif")
        Long conducteurId,

        @NotNull(message = "L'identifiant du véhicule est obligatoire")
        @Positive(message = "L'identifiant du véhicule doit être positif")
        Long vehiculeId,

        @NotNull(message = "Les points d'arrêt sont obligatoires")
        @Size(min = 2, message = "Un trajet doit avoir au minimum deux points d'arrêt")
        List<@Valid PointArretRequestDTO> pointsArret

) {}