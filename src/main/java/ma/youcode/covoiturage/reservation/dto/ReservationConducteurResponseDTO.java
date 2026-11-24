package ma.youcode.covoiturage.reservation.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import ma.youcode.covoiturage.enums.StatutReservation;

public record ReservationConducteurResponseDTO(
        Long id,
        Long passagerId,
        String nomPassager,
        String prenomPassager,
        String emailPassager,
        String telephonePassager,
        Long pointMonteeId,
        String villeMontee,
        Long pointDescenteId,
        String villeDescente,
        Integer nombrePlaces,
        BigDecimal montantTotal,
        LocalDateTime dateReservation,
        StatutReservation statut
) {}
