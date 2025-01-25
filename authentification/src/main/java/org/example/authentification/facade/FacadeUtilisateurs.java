package org.example.authentification.facade;

import org.apache.catalina.User;
import org.example.authentification.controleur.dtos.LoginDTO;
import org.example.authentification.controleur.dtos.UtilisateurDTO;
import org.example.authentification.dao.UtilisateurRepository;
import org.example.authentification.facade.exceptions.LoginDejaUtiliseException;
import org.example.authentification.facade.exceptions.UtilisateurInexistantException;
import org.example.authentification.modele.Utilisateur;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class FacadeUtilisateurs {

    private final UtilisateurRepository utilisateurRepository;

    private final Map<String, Utilisateur> utilisateursMap;

    private final PasswordEncoder passwordEncoder;

    public FacadeUtilisateurs(UtilisateurRepository utilisateurRepository, PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        utilisateursMap = new HashMap<>();
    }

    public Utilisateur getUtilisateurByEmail(String email) throws UtilisateurInexistantException {
        Optional<Utilisateur> user = utilisateurRepository.findByEmail(email);

        if (user.isPresent()) {
            return user.get();
        }

        throw new UtilisateurInexistantException();

    }

    public Utilisateur getUtilisateurById(long id) throws UtilisateurInexistantException {
        Optional<Utilisateur> user = utilisateurRepository.findById(id);

        if (user.isPresent()) {
            return user.get();
        }

        throw new UtilisateurInexistantException();

    }

    @Transactional
    public Utilisateur inscrireUtilisateur(UtilisateurDTO dto) throws LoginDejaUtiliseException {

        Optional<Utilisateur> user = utilisateurRepository.findByEmail(dto.email());

        if (user.isPresent()) {
            throw new LoginDejaUtiliseException();
        }
        else {
            Utilisateur utilisateur = new Utilisateur(dto.email(), passwordEncoder.encode(dto.password()), dto.nom(), dto.prenom());
            return utilisateurRepository.save(utilisateur);
        }
    }

    public Collection<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    @Transactional
    public Utilisateur desinscription(long id) throws UtilisateurInexistantException {
        Utilisateur user = utilisateurRepository.findById(id).orElseThrow(UtilisateurInexistantException::new);

        user.getRoles().size();


        utilisateurRepository.delete(user);
        return user;
    }

}
