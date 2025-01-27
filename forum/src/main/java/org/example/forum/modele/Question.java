package org.example.forum.modele;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

@Entity
public class Question {

    @Id
    private String idQuestion;
//    private long idUtilisateur;
    private String libelleQuestion;
    private String reponse;

    @ManyToOne(fetch = FetchType.LAZY)
    private Utilisateur utilisateur;

    public Question(Utilisateur utilisateur, String question) {
//        this.idUtilisateur = idUtilisateur;
        this.libelleQuestion = question;
        this.utilisateur = utilisateur;
        this.idQuestion = UUID.randomUUID().toString();
    }

    public Question() {}

    public String getIdQuestion() {
        return idQuestion;
    }

    public String getLibelleQuestion() {
        return libelleQuestion;
    }

    public String getReponse() {
        return reponse;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    //    public long getIdUtilisateur() {
//        return idUtilisateur;
//    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }



}
