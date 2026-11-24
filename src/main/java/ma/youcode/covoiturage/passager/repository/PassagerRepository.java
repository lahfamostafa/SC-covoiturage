package ma.youcode.covoiturage.passager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ma.youcode.covoiturage.passager.entity.Passager;

public interface PassagerRepository extends JpaRepository<Passager, Long> {

    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);

    @Query("""
        SELECT p FROM Passager p
        WHERE LOWER(p.nom) LIKE LOWER(CONCAT('%', :terme, '%'))
           OR LOWER(p.prenom) LIKE LOWER(CONCAT('%', :terme, '%'))
           OR LOWER(p.email) LIKE LOWER(CONCAT('%', :terme, '%'))
    """)
    Page<Passager> findByNameOrEmail(@Param("terme") String terme, Pageable pageable);
}
