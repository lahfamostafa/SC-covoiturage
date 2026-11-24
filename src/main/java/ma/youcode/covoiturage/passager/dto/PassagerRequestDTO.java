package ma.youcode.covoiturage.passager.dto;

import jakarta.validation.constraints.*;

public record PassagerRequestDTO(

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100)
    String nom,

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 100)
    String prenom,

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'adresse email est invalide")
    String email,

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^[0-9]{10}$",message = "Le téléphone doit contenir exactement 10 chiffres")
    String telephone

) {}
