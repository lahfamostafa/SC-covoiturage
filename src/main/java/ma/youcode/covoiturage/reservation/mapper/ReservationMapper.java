package ma.youcode.covoiturage.reservation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ma.youcode.covoiturage.reservation.dto.ReservationResponseDTO;
import ma.youcode.covoiturage.reservation.entity.Reservation;

@Mapper (componentModel = "spring")
public interface ReservationMapper {

    @Mapping(source = "trajet.id", target = "trajetId")
    @Mapping(source = "passager.id", target = "passagerId")
    @Mapping(source = "pointMontee.id", target = "pointMonteeId")
    @Mapping(source = "pointDescente.id", target = "pointDescenteId")
    ReservationResponseDTO toResponseDTO(Reservation reservation);
}