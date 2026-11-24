package ma.youcode.covoiturage.reservation.service;

import java.util.List;

import ma.youcode.covoiturage.reservation.dto.ReservationConducteurResponseDTO;
import ma.youcode.covoiturage.reservation.dto.ReservationRequestDTO;
import ma.youcode.covoiturage.reservation.dto.ReservationResponseDTO;

public interface ReservationService {

    ReservationResponseDTO reserver(Long passagerId, ReservationRequestDTO request);

    List<ReservationResponseDTO> consulterMesReservations(Long passagerId);

    void annulerReservation(Long reservationId, Long passagerId);

    List<ReservationConducteurResponseDTO> consulterReservationsDuTrajet(Long trajetId, Long conducteurId);
}
