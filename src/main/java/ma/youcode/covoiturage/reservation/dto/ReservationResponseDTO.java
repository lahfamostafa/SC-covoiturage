package ma.youcode.covoiturage.reservation.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import ma.youcode.covoiturage.enums.StatutReservation;

public record ReservationResponseDTO(

        Long id,
        Long trajetId,
        Long passagerId,
        Long pointMonteeId,
        Long pointDescenteId,
        Integer nombrePlaces,
        BigDecimal montantTotal,
        LocalDateTime dateReservation,
        StatutReservation statut

) {
}