package org.example.authentification.modele;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Utilisateur {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String email;

    private String nom;

    private String prenom;

    private String password;

    @ElementCollection
    List<Role> roles;


    public Utilisateur() {}

    public Utilisateur(String email, String password, String nom, String prenom) {
        this.email = email;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
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

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public boolean verifierPassword(String motDePasse) {
        return this.password.equals(motDePasse);
    }

    public List<Role> getRoles() {
        return roles;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}
