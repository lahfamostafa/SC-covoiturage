package ma.youcode.covoiturage.passager.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ma.youcode.covoiturage.commun.dto.PageResponseDTO;
import ma.youcode.covoiturage.commun.exception.AlreadyExistsException;
import ma.youcode.covoiturage.commun.exception.ResourceNotFoundException;
import ma.youcode.covoiturage.passager.dto.PassagerRequestDTO;
import ma.youcode.covoiturage.passager.dto.PassagerResponseDTO;
import ma.youcode.covoiturage.passager.entity.Passager;
import ma.youcode.covoiturage.passager.mapper.PassagerMapper;
import ma.youcode.covoiturage.passager.repository.PassagerRepository;
import ma.youcode.covoiturage.passager.service.PassagerService;

@Service
@Transactional
public class PassagerServiceImpl implements PassagerService {

    private final PassagerRepository passagerRepository;
    private final PassagerMapper passagerMapper;

    public PassagerServiceImpl(PassagerRepository passagerRepository,PassagerMapper passagerMapper) {
        this.passagerRepository = passagerRepository;
        this.passagerMapper = passagerMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PassagerResponseDTO> getPassagers() {
        return passagerRepository.findAll().stream().map(passagerMapper::toResponseDTO).toList();
    }

    @Override
    public PassagerResponseDTO creerPassager(PassagerRequestDTO request) {
        if (passagerRepository.existsByEmail(request.email())) {
            throw new AlreadyExistsException("Cette adresse email est déjà utilisée.");
        }

        Passager passager = passagerMapper.toEntity(request);

        Passager passagerEnregistre = passagerRepository.save(passager);

        return passagerMapper.toResponseDTO(passagerEnregistre);
    }

    @Override
    @Transactional(readOnly = true)
    public PassagerResponseDTO getPassager(Long id) {
        Passager passager = passagerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Passager introuvable avec l'identifiant : " + id));

        return passagerMapper.toResponseDTO(passager);
    }

    @Override
    public PassagerResponseDTO updatePassager(Long id,PassagerRequestDTO requestDTO) {
        Passager passager = passagerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Passager introuvable avec l'identifiant : " + id));

        if (passagerRepository.existsByEmailAndIdNot(requestDTO.email(), id)) {
            throw new AlreadyExistsException("Cette adresse email est déjà utilisée.");}

        passager.modifierInformations(requestDTO.nom(),requestDTO.prenom(),requestDTO.email(),requestDTO.telephone());

        return passagerMapper.toResponseDTO(passager);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<PassagerResponseDTO> rechercherParNomOuEmail(String terme,Pageable pageable) {

        Page<PassagerResponseDTO> resultats = passagerRepository.findByNameOrEmail(terme, pageable).map(passagerMapper::toResponseDTO);

        return PageResponseDTO.from(resultats);
    }
}
