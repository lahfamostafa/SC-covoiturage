package ma.youcode.covoiturage.vehicule.controller;

import java.util.List;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ma.youcode.covoiturage.vehicule.dto.VehiculeRequestDTO;
import ma.youcode.covoiturage.vehicule.dto.VehiculeResponseDTO;
import ma.youcode.covoiturage.vehicule.service.VehiculeService;

@RestController
@RequestMapping("/api/vehicules")
@RequiredArgsConstructor
public class VehiculeController {

    private final VehiculeService vehiculeService;

    @PostMapping
    public ResponseEntity<VehiculeResponseDTO> creerVehicule(@Valid @RequestBody VehiculeRequestDTO request) {
        VehiculeResponseDTO response = vehiculeService.creerVehicule(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<VehiculeResponseDTO>> getVehicules() {
        return ResponseEntity.ok(vehiculeService.getVehicules());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehiculeResponseDTO> getVehiculeById(@PathVariable Long id) {
        return ResponseEntity.ok(vehiculeService.getVehiculeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehiculeResponseDTO> modifierVehicule(@PathVariable Long id,@Valid @RequestBody VehiculeRequestDTO request) {
        return ResponseEntity.ok(vehiculeService.modifierVehicule(id, request));
    }

    @GetMapping("/conducteur/{conducteurId}")
    public ResponseEntity<List<VehiculeResponseDTO>> getVehiculesByConducteurId(@PathVariable Long conducteurId) {
        return ResponseEntity.ok(vehiculeService.getVehiculesByConducteurId(conducteurId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerVehicule(@PathVariable Long id) {

        vehiculeService.supprimerVehicule(id);
        return ResponseEntity.noContent().build();
    }
}
