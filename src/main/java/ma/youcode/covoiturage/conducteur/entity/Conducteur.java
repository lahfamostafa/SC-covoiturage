
package ma.youcode.covoiturage.conducteur.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import ma.youcode.covoiturage.trajet.entity.Trajet;
import ma.youcode.covoiturage.vehicule.entity.Vehicule;

@Entity
@Table(name = "conducteurs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Conducteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String telephone;

    @Column(name = "numero_permis", nullable = false, unique = true)
    private String numeroPermis;

    @OneToMany(mappedBy = "conducteur")
    @Getter(AccessLevel.NONE)
    private List<Vehicule> vehicules = new ArrayList<>();

    @OneToMany(mappedBy = "conducteur")
    @Getter(AccessLevel.NONE)
    private List<Trajet> trajets = new ArrayList<>();

    public Conducteur(String nom, String prenom, String email, String telephone, String numeroPermis) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.numeroPermis = numeroPermis;
    }

    public List<Vehicule> getVehicules() {
        return Collections.unmodifiableList(vehicules);
    }

    public List<Trajet> getTrajets() {
        return Collections.unmodifiableList(trajets);
    }

    public void modifierInformations(String nom, String prenom, String email, String telephone, String numeroPermis) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.numeroPermis = numeroPermis;
    }

    public void enregistrerVehicule(Vehicule vehicule) {

        if (vehicule.getConducteur() != this) {
            throw new IllegalArgumentException("Ce véhicule n'appartient pas à ce conducteur");
        }

        if (!vehicules.contains(vehicule)) {
            vehicules.add(vehicule);
        }
    }
}
