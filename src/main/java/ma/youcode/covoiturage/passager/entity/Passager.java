package ma.youcode.covoiturage.passager.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import ma.youcode.covoiturage.reservation.entity.Reservation;

@Entity
@Table(name = "passagers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Passager {

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

    @OneToMany(mappedBy = "passager")
    private List<Reservation> reservations = new ArrayList<>();

    public Passager(String nom, String prenom, String email, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
    }

    public void modifierInformations(String nom, String prenom, String email, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
    }

    public void addReservation(Reservation reservation) {
        if (reservation == null) {
            throw new IllegalArgumentException("La réservation ne peut pas être null");
        }

        reservation.assignPassager(this);
    }

    public void addReservationReference(Reservation reservation) {
        if (!reservations.contains(reservation)) {
            reservations.add(reservation);
        }
    }

    public List<Reservation> getReservations() {
        return List.copyOf(reservations);
    }
}
