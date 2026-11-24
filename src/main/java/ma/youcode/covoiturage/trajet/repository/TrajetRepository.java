package ma.youcode.covoiturage.trajet.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import ma.youcode.covoiturage.enums.StatutReservation;
import ma.youcode.covoiturage.enums.StatutTrajet;
import ma.youcode.covoiturage.trajet.dto.TrajetRechercheDTO;
import ma.youcode.covoiturage.trajet.entity.Trajet;

public interface TrajetRepository extends JpaRepository<Trajet, Long> {
    boolean existsByVehicule_Id(Long vehiculeId);

    List<Trajet> findByConducteur_Id(Long conducteurId);

    boolean existsByConducteur_IdAndDateDepartAndStatutNot(Long conducteurId, LocalDateTime dateDepart, StatutTrajet status);

    boolean existsByConducteur_IdAndDateDepartAndStatutNotAndIdNot(Long conducteurId, LocalDateTime dateDepart, StatutTrajet statut, Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Trajet t WHERE t.id = :id")
    Optional<Trajet> findByIdForUpdate(@Param("id") Long id);

    @EntityGraph(attributePaths = {"pointsArret"})
    @Query("SELECT t FROM Trajet t WHERE t.id = :id")
    Optional<Trajet> findWithPointsArretById(@Param("id") Long id);
    
    // @Query("""
    //         SELECT DISTINCT t FROM Trajet t
    //         JOIN t.pointsArret depart
    //         JOIN t.pointsArret arrivee
    //         WHERE LOWER(depart.ville) = LOWER(:villeDepart)
    //         AND LOWER(arrivee.ville) = LOWER(:villeArrivee)
    //         AND depart.ordre < arrivee.ordre
    //         AND t.dateDepart >= :debutJour
    //         AND t.dateDepart < :finJour
    //         AND t.statut = :statut
    //         """)
    // Page<Trajet> findTrajets(
    //         @Param("villeDepart") String villeDepart,
    //         @Param("villeArrivee") String villeArrivee,
    //         @Param("debutJour") LocalDateTime debutJour,
    //         @Param("finJour") LocalDateTime finJour,
    //         @Param("statut") StatutTrajet statut,
    //         Pageable pageable);



    @Query(value = """
            SELECT new ma.youcode.covoiturage.trajet.dto.TrajetRechercheDTO(
                t.id,
                t.dateDepart,
                depart.ville,
                arrivee.ville,
                (arrivee.prixDepuisDepart - depart.prixDepuisDepart),
                (
                    t.placesProposees - COALESCE(
                        (
                            SELECT SUM(r.nombrePlaces)
                            FROM Reservation r
                            WHERE r.trajet = t
                              AND r.statut = :statutReservation
                        ),
                        0L
                    )
                ),
                t.placesProposees,
                t.conducteur.id,
                t.vehicule.id
            )
            FROM Trajet t
            JOIN t.pointsArret depart
            JOIN t.pointsArret arrivee
            WHERE t.statut = :statutTrajet
              AND LOWER(TRIM(depart.ville)) = LOWER(TRIM(:villeDepart))
              AND LOWER(TRIM(arrivee.ville)) = LOWER(TRIM(:villeArrivee))
              AND depart.ordre < arrivee.ordre

              AND (
                CAST(:debutJour AS LocalDateTime) IS NULL
                OR t.dateDepart >= :debutJour
            )
            AND (
                CAST(:finJour AS LocalDateTime) IS NULL
                OR t.dateDepart < :finJour
            )

              AND (
                  t.placesProposees - COALESCE(
                      (
                          SELECT SUM(r.nombrePlaces)
                          FROM Reservation r
                          WHERE r.trajet = t
                            AND r.statut = :statutReservation
                      ),
                      0L
                  )
              ) >= :nombrePlaces

              AND (
                  :prixMax IS NULL
                  OR (arrivee.prixDepuisDepart - depart.prixDepuisDepart) <= :prixMax
              )
            """, countQuery = """
            SELECT COUNT(DISTINCT t.id)
            FROM Trajet t
            JOIN t.pointsArret depart
            JOIN t.pointsArret arrivee
            WHERE t.statut = :statutTrajet
              AND LOWER(TRIM(depart.ville)) = LOWER(TRIM(:villeDepart))
              AND LOWER(TRIM(arrivee.ville)) = LOWER(TRIM(:villeArrivee))
              AND depart.ordre < arrivee.ordre

              AND (
                  :debutJour IS NULL
                  OR t.dateDepart >= :debutJour
              )
              AND (
                  :finJour IS NULL
                  OR t.dateDepart < :finJour
              )

              AND (
                  t.placesProposees - COALESCE(
                      (
                          SELECT SUM(r.nombrePlaces)
                          FROM Reservation r
                          WHERE r.trajet = t
                            AND r.statut = :statutReservation
                      ),
                      0L
                  )
              ) >= :nombrePlaces

              AND (
                  :prixMax IS NULL
                  OR (arrivee.prixDepuisDepart - depart.prixDepuisDepart) <= :prixMax
              )
            """)
    Page<TrajetRechercheDTO> rechercherTrajets(
            @Param("villeDepart") String villeDepart,
            @Param("villeArrivee") String villeArrivee,
            @Param("debutJour") LocalDateTime debutJour,
            @Param("finJour") LocalDateTime finJour,
            @Param("nombrePlaces") Integer nombrePlaces,
            @Param("prixMax") BigDecimal prixMax,
            @Param("statutTrajet") StatutTrajet statutTrajet,
            @Param("statutReservation") StatutReservation statutReservation,
            Pageable pageable);
}