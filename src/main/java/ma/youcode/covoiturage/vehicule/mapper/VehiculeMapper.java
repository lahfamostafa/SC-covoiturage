package ma.youcode.covoiturage.vehicule.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ma.youcode.covoiturage.vehicule.dto.VehiculeResponseDTO;
import ma.youcode.covoiturage.vehicule.entity.Vehicule;

@Mapper(componentModel = "spring")
public interface VehiculeMapper {

    @Mapping(target = "conducteurId", source = "conducteur.id")
    VehiculeResponseDTO toResponseDTO(Vehicule vehicule);
}
