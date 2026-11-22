package ma.youcode.covoiturage.conducteur.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ma.youcode.covoiturage.conducteur.entity.Conducteur;

public interface ConducteurRepository extends JpaRepository<Conducteur,Long>{
    boolean existsByEmail(String email);
    boolean existsByNumeroPermis(String numeroPermis);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByNumeroPermisAndIdNot(String numeroPermis, Long id);

    @Query("""
        Select c from Conducteur c
        where Lower(c.nom) Like Lower(Concat('%',:terme,'%'))
           or Lower(c.prenom) Like Lower(Concat('%',:terme,'%'))
           or Lower(c.email) Like Lower(Concat('%',:terme,'%'))
    """)
    Page<Conducteur> findByNameOrEmail(@Param("terme") String terme, Pageable pageable);
}
