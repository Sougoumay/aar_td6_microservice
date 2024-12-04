package org.example.authentification.modele;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Utilisateur {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String password;

    @ElementCollection
    List<Role> roles;


    public Utilisateur() {}

    public Utilisateur(String email, String password) {
        this.email = email;
        this.password = password;
        roles = List.of(checkRole(email));
    }


    private Role checkRole(String email) {
        String domain = email.split("@")[1];
        return switch (domain) {
            case "etu.univ-orleans.fr" -> Role.ETUDIANT;
            case "univ-orleans.fr" -> Role.ENSEIGNANT;
            default -> null;
        };
    }

    public boolean verifierPassword(String motDePasse) {
        return this.password.equals(motDePasse);
    }

}
