package ma.youcode.covoiturage.vehicule.entity;

import jakarta.persistence.*;
import lombok.*;
import ma.youcode.covoiturage.conducteur.entity.Conducteur;

@Entity
@Table(name = "vehicules")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String marque;

    @Column(nullable = false)
    private String modele;

    @Column(nullable = false, unique = true)
    private String immatriculation;

    @Column(nullable = false)
    private String couleur;

    @Column(nullable = false)
    private Integer capacite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conducteur_id", nullable = false)
    private Conducteur conducteur;
}
