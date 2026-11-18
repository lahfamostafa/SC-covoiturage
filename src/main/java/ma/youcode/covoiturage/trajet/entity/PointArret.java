package ma.youcode.covoiturage.trajet.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "points_arret")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointArret {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ville", nullable = false, length = 150)
    private String ville;

    @Column(nullable = false)
    private Integer ordre;

    @Column(name = "heure_passage", nullable = false)
    private OffsetDateTime heurePassage;

    @Column(name = "prix_depuis_depart", nullable = false, precision = 10, scale = 2)
    private BigDecimal prixDepuisDepart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trajet_id", nullable = false)
    private Trajet trajet;

    void assignTrajet(Trajet trajet) {
        this.trajet = trajet;
    }

    void removeTrajet() {
        this.trajet = null;
    }
}