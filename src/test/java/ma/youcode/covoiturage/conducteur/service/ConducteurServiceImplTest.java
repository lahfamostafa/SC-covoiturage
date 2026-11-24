
package ma.youcode.covoiturage.conducteur.service;

import java.util.List;
import java.util.Optional;

import ma.youcode.covoiturage.commun.exception.AlreadyExistsException;
import ma.youcode.covoiturage.commun.exception.ResourceNotFoundException;
import ma.youcode.covoiturage.conducteur.dto.ConducteurRequestDTO;
import ma.youcode.covoiturage.conducteur.dto.ConducteurResponseDTO;
import ma.youcode.covoiturage.conducteur.entity.Conducteur;
import ma.youcode.covoiturage.conducteur.mapper.ConducteurMapper;
import ma.youcode.covoiturage.conducteur.repository.ConducteurRepository;
import ma.youcode.covoiturage.conducteur.service.impl.ConducteurServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConducteurServiceImplTest {

    @Mock
    private ConducteurRepository conducteurRepository;

    @Mock
    private ConducteurMapper conducteurMapper;

    @InjectMocks
    private ConducteurServiceImpl conducteurService;

    private final ConducteurRequestDTO request = new ConducteurRequestDTO(
            "Lahfa",
            "Mostafa",
            "mostafa@example.com",
            "0612345678",
            "PERMIS123"
    );

    private final Conducteur conducteur = new Conducteur(
            "Lahfa",
            "Mostafa",
            "mostafa@example.com",
            "0612345678",
            "PERMIS123"
    );

    private final ConducteurResponseDTO response =
            new ConducteurResponseDTO(
                    1L,
                    "Lahfa",
                    "Mostafa",
                    "mostafa@example.com",
                    "0612345678",
                    "PERMIS123"
            );

    // CREATE : création réussie
    @Test
    void shouldCreateConducteur() {

        // Arrange
        when(conducteurRepository.existsByEmail(request.email()))
                .thenReturn(false);

        when(conducteurRepository.existsByNumeroPermis(request.numeroPermis()))
                .thenReturn(false);

        when(conducteurMapper.toEntity(request)).thenReturn(conducteur);

        when(conducteurRepository.save(conducteur)).thenReturn(conducteur);

        when(conducteurMapper.toResponseDTO(conducteur)).thenReturn(response);

        // Act
        ConducteurResponseDTO result = conducteurService.creerConducteur(request);

        // Assert
        assertEquals(response, result);

        verify(conducteurRepository).save(conducteur);
    }

    // CREATE : email déjà utilisé
    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        when(conducteurRepository.existsByEmail(request.email()))
                .thenReturn(true);

        assertThrows(
                AlreadyExistsException.class,
                () -> conducteurService.creerConducteur(request)
        );

        verify(conducteurRepository, never()).save(any());
        verifyNoInteractions(conducteurMapper);
    }

    // CREATE : numéro de permis déjà utilisé
    @Test
    void shouldThrowExceptionWhenNumeroPermisAlreadyExists() {

        when(conducteurRepository.existsByEmail(request.email()))
                .thenReturn(false);

        when(conducteurRepository.existsByNumeroPermis(request.numeroPermis()))
                .thenReturn(true);

        assertThrows(
                AlreadyExistsException.class,
                () -> conducteurService.creerConducteur(request)
        );

        verify(conducteurRepository, never()).save(any());
        verifyNoInteractions(conducteurMapper);
    }

    // READ : récupérer tous les conducteurs
    @Test
    void shouldReturnAllConducteurs() {

        when(conducteurRepository.findAll())
                .thenReturn(List.of(conducteur));

        when(conducteurMapper.toResponseDTO(conducteur))
                .thenReturn(response);

        List<ConducteurResponseDTO> result = conducteurService.getConducteurs();

        assertEquals(1, result.size());
        assertEquals(response, result.get(0));
    }

    // READ : conducteur inexistant
    @Test
    void shouldThrowExceptionWhenConducteurNotFound() {

        when(conducteurRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> conducteurService.getConducteur(99L)
        );
    }

    // UPDATE : modification réussie
    @Test
    void shouldUpdateConducteur() {

        when(conducteurRepository.findById(1L))
                .thenReturn(Optional.of(conducteur));

        when(conducteurRepository.existsByEmailAndIdNot(
                request.email(), 1L))
                .thenReturn(false);

        when(conducteurRepository.existsByNumeroPermisAndIdNot(
                request.numeroPermis(), 1L))
                .thenReturn(false);

        when(conducteurMapper.toResponseDTO(conducteur))
                .thenReturn(response);

        ConducteurResponseDTO result =
                conducteurService.updateConducteur(1L, request);

        assertEquals(response, result);

        // L'entité est managed : aucun save nécessaire.
        verify(conducteurRepository, never()).save(any());

        assertEquals("Lahfa", conducteur.getNom());
    }
}
