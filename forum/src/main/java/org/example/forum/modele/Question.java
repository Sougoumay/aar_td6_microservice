package org.example.forum.modele;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;

@Entity

public class Question {

    @Id
    private String idQuestion;
    private long idUtilisateur;
    private String libelleQuestion;
    private String reponse;



    public Question(long idUtilisateur, String question) {
        this.idUtilisateur = idUtilisateur;
        this.libelleQuestion = question;
        this.idQuestion = UUID.randomUUID().toString();
    }

    public Question() {}

    public String getIdQuestion() {
        return idQuestion;
    }

    public long getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }
}
