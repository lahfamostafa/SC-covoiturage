package ma.youcode.covoiturage.conducteur.mapper;

import org.mapstruct.Mapper;

import ma.youcode.covoiturage.conducteur.dto.ConducteurRequestDTO;
import ma.youcode.covoiturage.conducteur.dto.ConducteurResponseDTO;
import ma.youcode.covoiturage.conducteur.entity.Conducteur;

@Mapper(componentModel = "spring")
public interface ConducteurMapper {
    
    Conducteur toEntity(ConducteurRequestDTO request);

    ConducteurResponseDTO toResponseDTO(Conducteur conducteur);
}
