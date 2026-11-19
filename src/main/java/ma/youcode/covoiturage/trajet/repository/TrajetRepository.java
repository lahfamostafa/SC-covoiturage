package ma.youcode.covoiturage.trajet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.youcode.covoiturage.trajet.entity.Trajet;

public interface TrajetRepository extends JpaRepository<Trajet, Long> {
    boolean existsByVehicule_Id(Long vehiculeId);
}