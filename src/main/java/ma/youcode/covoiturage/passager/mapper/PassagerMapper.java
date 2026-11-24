
package ma.youcode.covoiturage.passager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ma.youcode.covoiturage.passager.dto.PassagerRequestDTO;
import ma.youcode.covoiturage.passager.dto.PassagerResponseDTO;
import ma.youcode.covoiturage.passager.entity.Passager;

@Mapper(componentModel = "spring")
public interface PassagerMapper {

    @Mapping(target = "reservations", ignore = true)
    Passager toEntity(PassagerRequestDTO request);

    PassagerResponseDTO toResponseDTO(Passager passager);
}
