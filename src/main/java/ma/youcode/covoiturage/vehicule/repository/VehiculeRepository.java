package ma.youcode.covoiturage.vehicule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.youcode.covoiturage.vehicule.entity.Vehicule;

public interface VehiculeRepository extends JpaRepository<Vehicule,Long>{
    boolean existsByImmatriculation(String immatriculation);
    boolean existsByImmatriculationAndIdNot(String immatriculation, Long id);
    List<Vehicule> findByConducteur_Id(Long conducteurId);
}
