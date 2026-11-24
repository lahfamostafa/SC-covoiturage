package ma.youcode.covoiturage.reservation.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ma.youcode.covoiturage.reservation.dto.ReservationConducteurResponseDTO;
import ma.youcode.covoiturage.reservation.service.ReservationService;

@RestController
@RequestMapping("/api/trajets")
@RequiredArgsConstructor
public class TrajetReservationController {

    private final ReservationService reservationService;

    @GetMapping("/{trajetId}/reservations")
    public ResponseEntity<List<ReservationConducteurResponseDTO>>consulterReservationsDuTrajet(@PathVariable Long trajetId,@RequestParam Long conducteurId) {
        return ResponseEntity.ok(reservationService.consulterReservationsDuTrajet(trajetId,conducteurId));
    }
}
