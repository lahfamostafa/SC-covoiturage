package ma.youcode.covoiturage.conducteur.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ma.youcode.covoiturage.commun.dto.PageResponseDTO;
import ma.youcode.covoiturage.conducteur.dto.ConducteurRequestDTO;
import ma.youcode.covoiturage.conducteur.dto.ConducteurResponseDTO;
import ma.youcode.covoiturage.conducteur.service.ConducteurService;

@RestController
@RequestMapping("/api/conducteurs")
public class ConducteurController {

    private final ConducteurService conducteurService;

    public ConducteurController(ConducteurService conducteurService) {
        this.conducteurService = conducteurService;
    }

    @GetMapping
    public ResponseEntity<List<ConducteurResponseDTO>> getConducteurs() {
        return ResponseEntity.ok(conducteurService.getConducteurs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConducteurResponseDTO> getConducteur(@PathVariable Long id) {
        return ResponseEntity.ok(conducteurService.getConducteur(id));
    }

    @PostMapping
    public ResponseEntity<ConducteurResponseDTO> creerConducteur(@Valid @RequestBody ConducteurRequestDTO request) {

        ConducteurResponseDTO response = conducteurService.creerConducteur(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConducteurResponseDTO> updateConducteur(@PathVariable Long id, @Valid @RequestBody ConducteurRequestDTO request) {
        return ResponseEntity.ok(conducteurService.updateConducteur(id, request));
    }

    @GetMapping("/recherche")
    public PageResponseDTO<ConducteurResponseDTO> rechercher(@RequestParam String terme,@PageableDefault(size = 10) Pageable pageable) {
        return conducteurService.rechercherParNomOuEmail(terme, pageable);
    }
}