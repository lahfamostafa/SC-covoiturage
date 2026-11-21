package ma.youcode.covoiturage.trajet.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.youcode.covoiturage.enums.StatutTrajet;
import ma.youcode.covoiturage.trajet.entity.Trajet;

public interface TrajetRepository extends JpaRepository<Trajet, Long> {
    boolean existsByVehicule_Id(Long vehiculeId);
    List<Trajet> findByConducteur_Id(Long conducteurId);
    boolean existsByConducteur_IdAndDateDepartAndStatutNot(Long conducteurId, LocalDateTime dateDepart, StatutTrajet status);
    boolean existsByConducteur_IdAndDateDepartAndStatutNotAndIdNot(Long conducteurId, LocalDateTime dateDepart, StatutTrajet statut, Long id);
}