package ma.youcode.covoiturage.trajet.entity;

import java.time.LocalDateTime;
import java.util.*;

import jakarta.persistence.*;

import lombok.*;
import ma.youcode.covoiturage.conducteur.entity.Conducteur;
import ma.youcode.covoiturage.enums.StatutTrajet;
import ma.youcode.covoiturage.reservation.entity.Reservation;
import ma.youcode.covoiturage.vehicule.entity.Vehicule;

@Entity
@Table(name = "trajets")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Trajet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conducteur_id", nullable = false)
    private Conducteur conducteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicule_id", nullable = false)
    private Vehicule vehicule;

    @Column(name = "date_depart", nullable = false)
    private LocalDateTime dateDepart;

    @Column(name = "places_proposees", nullable = false)
    private Integer placesProposees;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutTrajet statut = StatutTrajet.PUBLIE;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "trajet", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordre ASC")
    private List<PointArret> pointsArret = new ArrayList<>();

    @OneToMany(mappedBy = "trajet")
    private List<Reservation> reservations = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public void addPointArret(PointArret pointArret) {
        if (pointArret == null) {
            throw new IllegalArgumentException("Le point d'arrêt ne peut pas être null");
        }

        if (!pointsArret.contains(pointArret)) {
            pointsArret.add(pointArret);
            pointArret.assignTrajet(this);
        }
    }

    public void removePointArret(PointArret pointArret) {
        if (pointsArret.remove(pointArret)) {
            pointArret.removeTrajet();
        }
    }
}
