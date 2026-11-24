package ma.youcode.covoiturage.reservation.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReservationRequestDTO(

    @NotNull(message = "Le trajet est obligatoire")
    Long trajetId,

    @NotNull(message = "Le point de montée est obligatoire")
    Long pointMonteeId,

    @NotNull(message = "Le point de descente est obligatoire")
    Long pointDescenteId,

    @NotNull(message = "Le nombre de places est obligatoire")
    @Positive(message = "Le nombre de places doit être supérieur à zéro")
    Integer nombrePlaces

) {
}