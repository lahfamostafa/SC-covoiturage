package ma.youcode.covoiturage.passager.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ma.youcode.covoiturage.commun.dto.PageResponseDTO;
import ma.youcode.covoiturage.passager.dto.PassagerRequestDTO;
import ma.youcode.covoiturage.passager.dto.PassagerResponseDTO;
import ma.youcode.covoiturage.passager.service.PassagerService;

@RestController
@RequestMapping("/api/passagers")
public class PassagerController {

    private final PassagerService passagerService;

    public PassagerController(PassagerService passagerService) {
        this.passagerService = passagerService;
    }

    @GetMapping
    public ResponseEntity<List<PassagerResponseDTO>> getPassagers() {
        return ResponseEntity.ok(passagerService.getPassagers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassagerResponseDTO> getPassager(@PathVariable Long id) {
        return ResponseEntity.ok(passagerService.getPassager(id));
    }

    @PostMapping
    public ResponseEntity<PassagerResponseDTO> creerPassager(@Valid @RequestBody PassagerRequestDTO request) {
        PassagerResponseDTO response = passagerService.creerPassager(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PassagerResponseDTO> updatePassager(@PathVariable Long id,@Valid @RequestBody PassagerRequestDTO request) {
        return ResponseEntity.ok(passagerService.updatePassager(id, request));
    }

    @GetMapping("/recherche")
    public PageResponseDTO<PassagerResponseDTO> rechercher(@RequestParam String terme,@PageableDefault(size = 10) Pageable pageable) {
        return passagerService.rechercherParNomOuEmail(terme, pageable);
    }
}
