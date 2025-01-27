package org.example.forum.modele;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;
import java.util.Set;

@Entity
public class Utilisateur {

    @Id
    private long id;

    private String email;

    @OneToMany(mappedBy = "utilisateur")
    private Set<Question> questions;

    public Utilisateur() {}

    public Utilisateur(long id,String email) {
        this.id = id;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}
