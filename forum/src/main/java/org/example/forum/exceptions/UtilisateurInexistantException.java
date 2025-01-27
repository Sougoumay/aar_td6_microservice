package org.example.forum.exceptions;

public class UtilisateurInexistantException extends Exception {
    public UtilisateurInexistantException(long id) {

        super("L'utilisateur " + id + " n'existe pas");
    }
}
