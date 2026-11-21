package ma.youcode.covoiturage.trajet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ma.youcode.covoiturage.trajet.dto.TrajetResponseDTO;
import ma.youcode.covoiturage.trajet.entity.Trajet;

@Mapper(componentModel = "spring")
public interface TrajetMapper {

    @Mapping(target = "conducteurId", source = "conducteur.id")
    @Mapping(target = "vehiculeId", source = "vehicule.id")
    TrajetResponseDTO toResponseDTO(Trajet trajet);
}