package org.example.authentification.facade;

import org.apache.catalina.User;
import org.example.authentification.dao.UtilisateurRepository;
import org.example.authentification.facade.exceptions.LoginDejaUtiliseException;
import org.example.authentification.facade.exceptions.UtilisateurInexistantException;
import org.example.authentification.modele.Utilisateur;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class FacadeUtilisateurs {

    private final UtilisateurRepository utilisateurRepository;

    private final Map<String, Utilisateur> utilisateursMap;

    public FacadeUtilisateurs(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
        utilisateursMap = new HashMap<>();
    }

    public long getIdUtilisateurByEmail(String email) throws UtilisateurInexistantException {
        Optional<Utilisateur> user = utilisateurRepository.findByEmail(email);

        if (user.isPresent()) {
            return user.get().getId();
        }

        throw new UtilisateurInexistantException();
    }

    public Utilisateur getUtilisateurByEmail(String email) throws UtilisateurInexistantException {
        Optional<Utilisateur> user = utilisateurRepository.findByEmail(email);

        if (user.isPresent()) {
            return user.get();
        }

        throw new UtilisateurInexistantException();

    }

    public Utilisateur inscrireUtilisateur(String email, String password) throws LoginDejaUtiliseException {

        Optional<Utilisateur> user = utilisateurRepository.findByEmail(email);

        if (user.isPresent()) {
            throw new LoginDejaUtiliseException();
        }
        else {
            Utilisateur utilisateur = new Utilisateur(email, password);
            return utilisateurRepository.save(utilisateur);
        }
    }

    public Collection<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

}
