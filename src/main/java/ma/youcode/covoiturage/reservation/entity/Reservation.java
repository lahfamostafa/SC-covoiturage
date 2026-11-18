package ma.youcode.covoiturage.reservation.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import lombok.*;

import ma.youcode.covoiturage.enums.StatutReservation;
import ma.youcode.covoiturage.passager.entity.Passager;
import ma.youcode.covoiturage.trajet.entity.PointArret;
import ma.youcode.covoiturage.trajet.entity.Trajet;

@Entity
@Table(name = "reservations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trajet_id", nullable = false)
    private Trajet trajet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passager_id", nullable = false)
    private Passager passager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_montee_id", nullable = false)
    private PointArret pointMontee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_descente_id", nullable = false)
    private PointArret pointDescente;

    @Column(name = "nombre_places", nullable = false)
    private Integer nombrePlaces;

    @Column(name = "montant_total", nullable = false,
            precision = 10, scale = 2)
    private BigDecimal montantTotal;

    @Column(name = "date_reservation", nullable = false,
            updatable = false)
    private LocalDateTime dateReservation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutReservation statut;

    @PrePersist
    protected void onCreate() {
        if (dateReservation == null) {
            dateReservation = LocalDateTime.now();
        }
    }

    public void assignPassager(Passager passager) {
        this.passager = passager;
    }

    public void removePassager() {
        this.passager = null;
    }
}
