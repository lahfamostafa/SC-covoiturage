package ma.youcode.covoiturage.conducteur.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ma.youcode.covoiturage.commun.dto.PageResponseDTO;
import ma.youcode.covoiturage.commun.exception.AlreadyExistsException;
import ma.youcode.covoiturage.commun.exception.ResourceNotFoundException;
import ma.youcode.covoiturage.conducteur.dto.ConducteurRequestDTO;
import ma.youcode.covoiturage.conducteur.dto.ConducteurResponseDTO;
import ma.youcode.covoiturage.conducteur.entity.Conducteur;
import ma.youcode.covoiturage.conducteur.mapper.ConducteurMapper;
import ma.youcode.covoiturage.conducteur.repository.ConducteurRepository;
import ma.youcode.covoiturage.conducteur.service.ConducteurService;

@Service
@Transactional
public class ConducteurServiceImpl implements ConducteurService {

    private final ConducteurRepository conducteurRepository;
    private final ConducteurMapper conducteurMapper;

    public ConducteurServiceImpl(ConducteurMapper conducteurMapper,ConducteurRepository conducteurRepository) {

        this.conducteurRepository = conducteurRepository;
        this.conducteurMapper = conducteurMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConducteurResponseDTO> getConducteurs() {

        return conducteurRepository.findAll()
                .stream().map(conducteurMapper::toResponseDTO).toList();
    }

    @Override
    public ConducteurResponseDTO creerConducteur(ConducteurRequestDTO request) {

        if (conducteurRepository.existsByEmail(request.email())) {
            throw new AlreadyExistsException("Cette adresse email est déjà utilisée.");
        }

        if (conducteurRepository.existsByNumeroPermis(request.numeroPermis())) {
            throw new AlreadyExistsException("Ce numéro de permis est déjà utilisé.");
        }

        Conducteur conducteur = conducteurMapper.toEntity(request);

        Conducteur conducteurEnregistre = conducteurRepository.save(conducteur);

        return conducteurMapper.toResponseDTO(conducteurEnregistre);
    }

    @Override
    @Transactional(readOnly = true)
    public ConducteurResponseDTO getConducteur(Long id) {

        Conducteur conducteur = conducteurRepository.findById(id)
            .orElseThrow(() ->new ResourceNotFoundException("Conducteur introuvable avec l'identifiant : " + id));

        return conducteurMapper.toResponseDTO(conducteur);
    }

    @Override
    public ConducteurResponseDTO updateConducteur(Long id, ConducteurRequestDTO requestDTO) {

        Conducteur conducteur = conducteurRepository.findById(id)
            .orElseThrow(() ->new ResourceNotFoundException("Conducteur introuvable avec l'identifiant : " + id));

        if (conducteurRepository.existsByEmailAndIdNot(requestDTO.email(), id)) {
            throw new AlreadyExistsException("Cette adresse email est déjà utilisée.");
        }

        if (conducteurRepository.existsByNumeroPermisAndIdNot(requestDTO.numeroPermis(), id)) {
            throw new ResourceNotFoundException("Ce numéro de permis est déjà utilisé.");
        }

        conducteur.modifierInformations(
                requestDTO.nom(),
                requestDTO.prenom(),
                requestDTO.email(),
                requestDTO.telephone(),
                requestDTO.numeroPermis()
        );

        return conducteurMapper.toResponseDTO(conducteur);
    }

    @Override
    public PageResponseDTO<ConducteurResponseDTO> rechercherParNomOuEmail(String terme, Pageable pageable) {
        Page<ConducteurResponseDTO> resultats = conducteurRepository.findByNameOrEmail(terme, pageable)
                .map(conducteurMapper::toResponseDTO);
        return PageResponseDTO.from(resultats);
    }
}