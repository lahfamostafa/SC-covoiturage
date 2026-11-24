package ma.youcode.covoiturage.trajet.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.youcode.covoiturage.commun.dto.PageResponseDTO;
import ma.youcode.covoiturage.trajet.dto.TrajetDetailResponseDTO;
import ma.youcode.covoiturage.trajet.dto.TrajetRechercheDTO;
import ma.youcode.covoiturage.trajet.dto.TrajetRequestDTO;
import ma.youcode.covoiturage.trajet.dto.TrajetResponseDTO;
import ma.youcode.covoiturage.trajet.service.TrajetService;

@RestController
@RequestMapping("/api/trajets")
@RequiredArgsConstructor
public class TrajetController {

    private final TrajetService trajetService;

    @PostMapping
    public ResponseEntity<TrajetResponseDTO> creerTrajet(@Valid @RequestBody TrajetRequestDTO request) {

        TrajetResponseDTO response = trajetService.creerTrajet(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TrajetResponseDTO>> getTrajets() {
        return ResponseEntity.ok(trajetService.getTrajets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrajetResponseDTO> getTrajetById(@PathVariable Long id) {
        return ResponseEntity.ok(trajetService.getTrajetById(id));
    }

    @GetMapping("/conducteur/{conducteurId}")
    public ResponseEntity<List<TrajetResponseDTO>> getTrajetsByConducteurId(@PathVariable Long conducteurId) {
        return ResponseEntity.ok(trajetService.getTrajetsByConducteurId(conducteurId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrajetResponseDTO> modifierTrajet(@PathVariable Long id,@Valid @RequestBody TrajetRequestDTO request) {
        return ResponseEntity.ok(trajetService.modifierTrajet(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerTrajet(@PathVariable Long id) {
        trajetService.supprimerTrajet(id);
        return ResponseEntity.noContent().build();
    }

    // @GetMapping("/recherche")
    // public PageResponseDTO<TrajetResponseDTO> rechercherTrajets(@RequestParam String villeDepart, @RequestParam String villeArrivee, @RequestParam LocalDate date, @PageableDefault(size = 10) Pageable pageable) {
    //     return trajetService.rechercherTrajets(villeDepart, villeArrivee, date, pageable);
    // }

    @GetMapping("/recherche")
    public PageResponseDTO<TrajetRechercheDTO> rechercherTrajetsAvances(
            @RequestParam String villeDepart,
            @RequestParam String villeArrivee,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(defaultValue = "1") Integer nombrePlaces,
            @RequestParam(required = false) BigDecimal prixMax,
            @PageableDefault(size = 10) Pageable pageable) {

        return trajetService.rechercherTrajetsAvances(villeDepart, villeArrivee, date, nombrePlaces, prixMax, pageable);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<TrajetDetailResponseDTO> getTrajetDetails(@PathVariable Long id) {
        return ResponseEntity.ok(trajetService.getTrajetDetails(id));
    }
}