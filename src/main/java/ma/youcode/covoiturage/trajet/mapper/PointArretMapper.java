package ma.youcode.covoiturage.trajet.mapper;

import org.mapstruct.Mapper;

import ma.youcode.covoiturage.trajet.dto.PointArretResponseDTO;
import ma.youcode.covoiturage.trajet.entity.PointArret;

@Mapper(componentModel = "spring")
public interface PointArretMapper {

    PointArretResponseDTO toResponseDTO(PointArret pointArret);
}