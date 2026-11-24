package ma.youcode.covoiturage.reservation.controller;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ma.youcode.covoiturage.reservation.dto.ReservationRequestDTO;
import ma.youcode.covoiturage.reservation.dto.ReservationResponseDTO;
import ma.youcode.covoiturage.reservation.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponseDTO> reserver(@RequestParam Long passagerId,@Valid @RequestBody ReservationRequestDTO request) {

        ReservationResponseDTO reservation =reservationService.reserver(passagerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    @GetMapping("/mes-reservations/{passagerId}")
    public ResponseEntity<List<ReservationResponseDTO>> consulterMesReservations(@PathVariable Long passagerId) {
        return ResponseEntity.ok(reservationService.consulterMesReservations(passagerId));
    }

    @PatchMapping("/{reservationId}/annuler")
    public ResponseEntity<Void> annulerReservation(@PathVariable Long reservationId,@RequestParam Long passagerId) {

        reservationService.annulerReservation(reservationId, passagerId);
        return ResponseEntity.noContent().build();
    }
}