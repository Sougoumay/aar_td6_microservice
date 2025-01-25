package org.example.authentification.controleur;

import org.example.authentification.controleur.dtos.LoginDTO;
import org.example.authentification.controleur.dtos.UtilisateurDTO;
import org.example.authentification.facade.FacadeUtilisateurs;
import org.example.authentification.facade.exceptions.LoginDejaUtiliseException;
import org.example.authentification.facade.exceptions.UtilisateurInexistantException;
import org.example.authentification.modele.Utilisateur;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.function.Function;

@RestController
@RequestMapping("/api")
public class Controleur {


    private final FacadeUtilisateurs facadeUtilisateurs;

    private final PasswordEncoder passwordEncoder;

    private final Function<Utilisateur, String> genereToken;

    public Controleur(FacadeUtilisateurs facadeUtilisateurs, PasswordEncoder passwordEncoder, Function<Utilisateur, String> genereToken) {
        this.facadeUtilisateurs = facadeUtilisateurs;
        this.passwordEncoder = passwordEncoder;
        this.genereToken = genereToken;
    }

    @PostMapping("/utilisateurs")
    public ResponseEntity<Utilisateur> inscrire(@RequestBody UtilisateurDTO dto,
                                                UriComponentsBuilder base) {
        Utilisateur utilisateur;
        try {
            utilisateur = facadeUtilisateurs.inscrireUtilisateur(dto);
        } catch (LoginDejaUtiliseException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        URI location = base.path("/api/utilisateurs/{idUtilisateur}")
                .buildAndExpand(utilisateur.getId())
                .toUri();
        return ResponseEntity.created(location).header("Authorization", "Bearer " + genereToken.apply(utilisateur)).body(utilisateur);
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        try {
            Utilisateur utilisateur = facadeUtilisateurs.getUtilisateurByEmail(loginDTO.email());
            if (passwordEncoder.matches(loginDTO.password(), utilisateur.getPassword())) {
                return ResponseEntity.status(HttpStatus.OK).header("Authorization", "Bearer " + genereToken.apply(utilisateur)).build();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (UtilisateurInexistantException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/utilisateurs/{idUtilisateur}")
    @PreAuthorize("T(java.lang.String).valueOf(#idUtilisateur) == authentication.name || hasRole('ENSEIGNANT')")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable int idUtilisateur,
                                                          Authentication authentication) throws UtilisateurInexistantException {
        long id = Long.parseLong(authentication.getName());
        System.out.println("L'id est : " + id);
        if (idUtilisateur == id) {
            Utilisateur utilisateur = facadeUtilisateurs.getUtilisateurById(id);
            return ResponseEntity.ok(utilisateur);
        }

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    @DeleteMapping("/utilisateurs/{idUtilisateur}")
    @PreAuthorize("T(java.lang.String).valueOf(#idUtilisateur) == authentication.name && hasRole('ETUDIANT')")
    public ResponseEntity<String> desinscription(@PathVariable int idUtilisateur,
                                            Authentication authentication) throws UtilisateurInexistantException {
        long id = Long.parseLong(authentication.getName());
        if (idUtilisateur == id) {
            Utilisateur utilisateur = facadeUtilisateurs.desinscription(id);
            System.out.println(utilisateur);
            return ResponseEntity.ok("L'utilisateur est désinscrit avec succès");
        }

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

}