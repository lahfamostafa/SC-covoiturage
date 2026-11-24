package ma.youcode.covoiturage.reservation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ma.youcode.covoiturage.enums.StatutReservation;
import ma.youcode.covoiturage.reservation.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
        SELECT COUNT(r)
        FROM Reservation r
        WHERE r.trajet.id = :trajetId
        AND r.statut = :statut
    """)
    long countByTrajetAndStatut(@Param("trajetId") Long trajetId,@Param("statut") StatutReservation statut);

    boolean existsByPassager_IdAndTrajet_IdAndStatut(Long passagerId,Long trajetId,StatutReservation statut);

    List<Reservation> findByPassager_IdOrderByDateReservationDesc(Long passagerId);

    List<Reservation> findByTrajet_Id(Long trajetId);

    Optional<Reservation> findByIdAndPassager_Id(Long id, Long passagerId);

    @Query("""
        SELECT COALESCE(SUM(r.nombrePlaces), 0)
        FROM Reservation r
        WHERE r.trajet.id = :trajetId
        AND r.statut = :statut
    """)
    Long sumPlacesReservees(@Param("trajetId") Long trajetId, @Param("statut") StatutReservation statut);

    @Query("""
        SELECT r
        FROM Reservation r
        JOIN FETCH r.passager
        JOIN FETCH r.pointMontee
        JOIN FETCH r.pointDescente
        WHERE r.trajet.id = :trajetId
        ORDER BY r.dateReservation DESC
    """)
    List<Reservation> findReservationsByTrajetId(@Param("trajetId") Long trajetId);
}