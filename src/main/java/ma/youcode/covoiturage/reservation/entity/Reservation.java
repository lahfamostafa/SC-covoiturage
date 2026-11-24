package ma.youcode.covoiturage.reservation.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "montant_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal montantTotal;

    @Column(name = "date_reservation", nullable = false, updatable = false)
    private LocalDateTime dateReservation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutReservation statut;

    public Reservation(
            Trajet trajet,
            Passager passager,
            PointArret pointMontee,
            PointArret pointDescente,
            Integer nombrePlaces,
            BigDecimal montantTotal) {

        this.trajet = Objects.requireNonNull(trajet, "Le trajet est obligatoire");
        this.pointMontee = Objects.requireNonNull(pointMontee, "Le point de montée est obligatoire");
        this.pointDescente = Objects.requireNonNull(pointDescente, "Le point de descente est obligatoire");

        if (nombrePlaces == null || nombrePlaces <= 0) {
            throw new IllegalArgumentException("Le nombre de places doit être supérieur à zéro");
        }

        if (montantTotal == null || montantTotal.signum() < 0) {
            throw new IllegalArgumentException("Le montant total doit être positif ou nul");
        }

        if (pointMontee.getOrdre() >= pointDescente.getOrdre()) {
            throw new IllegalArgumentException("Le point de montée doit précéder le point de descente");
        }

        this.nombrePlaces = nombrePlaces;
        this.montantTotal = montantTotal;
        this.dateReservation = LocalDateTime.now();
        this.statut = StatutReservation.CONFIRMEE;

        assignPassager(passager);
    }

    public void assignPassager(Passager passager) {

        Objects.requireNonNull(passager, "Le passager ne peut pas être null");

        if (this.passager != null && this.passager != passager) {
            throw new IllegalStateException(
                    "Cette réservation possède déjà un passager");
        }

        if (this.passager == passager) {
            return;
        }

        this.passager = passager;
        passager.addReservationReference(this);
    }

    public void annuler() {

        if (this.statut != StatutReservation.CONFIRMEE) {
            throw new IllegalStateException("Seules les réservations confirmées peuvent être annulées");
        }

        this.statut = StatutReservation.ANNULEE;
    }
}