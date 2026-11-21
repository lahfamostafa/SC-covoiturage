package ma.youcode.covoiturage.trajet.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "points_arret")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointArret {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String ville;

    @Column(nullable = false)
    private Integer ordre;

    @Column(name = "heure_passage", nullable = false)
    private LocalDateTime heurePassage;

    @Column(name = "prix_depuis_depart", nullable = false, precision = 10, scale = 2)
    private BigDecimal prixDepuisDepart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trajet_id", nullable = false)
    @Getter(AccessLevel.NONE)
    private Trajet trajet;

    public PointArret(String ville, Integer ordre, LocalDateTime heurePassage, BigDecimal prixDepuisDepart) {
        this.ville = ville;
        this.ordre = ordre;
        this.heurePassage = heurePassage;
        this.prixDepuisDepart = prixDepuisDepart;
    }

    void associerAuTrajet(Trajet trajet) {
        this.trajet = trajet;
    }

    void retirerDuTrajet() {
        this.trajet = null;
    }

    Trajet getTrajet() {
        return trajet;
    }
}