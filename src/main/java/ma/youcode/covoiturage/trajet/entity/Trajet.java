package ma.youcode.covoiturage.trajet.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import ma.youcode.covoiturage.conducteur.entity.Conducteur;
import ma.youcode.covoiturage.enums.StatutTrajet;
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
    @Getter(AccessLevel.NONE)
    private List<PointArret> pointsArret = new ArrayList<>();

    public Trajet(Conducteur conducteur, Vehicule vehicule, LocalDateTime dateDepart, Integer placesProposees) {
        this.conducteur = conducteur;
        this.vehicule = vehicule;
        this.dateDepart = dateDepart;
        this.placesProposees = placesProposees;
        this.statut = StatutTrajet.PUBLIE;
        this.createdAt = LocalDateTime.now();
    }

    public List<PointArret> getPointsArret() {
        return Collections.unmodifiableList(pointsArret);
    }

    public void ajouterPointArret(PointArret pointArret) {
        if (pointArret.getTrajet() != null && pointArret.getTrajet() != this) {
            throw new IllegalArgumentException("Ce point d'arrêt appartient déjà à un autre trajet");
        }

        if (!pointsArret.contains(pointArret)) {
            pointArret.associerAuTrajet(this);
            pointsArret.add(pointArret);
        }
    }

    public void retirerPointArret(PointArret pointArret) {
        if (pointsArret.remove(pointArret)) {
            pointArret.retirerDuTrajet();
        }
    }

    public void remplacerPointsArret(List<PointArret> nouveauxPoints) {
        for (PointArret point : new ArrayList<>(pointsArret)) {
            retirerPointArret(point);
        }

        for (PointArret point : nouveauxPoints) {
            ajouterPointArret(point);
        }
    }

    public void modifierInformations(LocalDateTime dateDepart, Integer placesProposees) {
        this.dateDepart = dateDepart;
        this.placesProposees = placesProposees;
    }

    public void changerStatut(StatutTrajet statut) {
        this.statut = statut;
    }
}