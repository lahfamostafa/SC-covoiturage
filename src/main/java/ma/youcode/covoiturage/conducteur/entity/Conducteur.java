package ma.youcode.covoiturage.conducteur.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ma.youcode.covoiturage.trajet.entity.Trajet;
import ma.youcode.covoiturage.vehicule.entity.Vehicule;

@Entity
@Table(name = "conducteurs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conducteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Column(nullable = false)
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Column(nullable = false)
    private String telephone;

    @NotBlank(message = "Le numéro de permis est obligatoire")
    @Column(name = "numero_permis", nullable = false, unique = true)
    private String numeroPermis;

    @OneToMany(mappedBy = "conducteur")
    @Builder.Default
    private java.util.List<Vehicule> vehicules = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "conducteur")
    @Builder.Default
    private java.util.List<Trajet> trajets = new java.util.ArrayList<>();
}
