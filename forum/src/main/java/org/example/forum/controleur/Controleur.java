package org.example.forum.controleur;

import org.example.forum.facades.FacadeApplication;
import org.example.forum.facades.exceptions.AccessIllegalAUneQuestionException;
import org.example.forum.facades.exceptions.QuestionInexistanteException;
import org.example.forum.modele.Question;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class Controleur {

    private final FacadeApplication facadeApplication;

    public Controleur(FacadeApplication facadeApplication) {
        this.facadeApplication = facadeApplication;
    }

    record LibelleQuestion(String libelleQuestion) {}

    @PreAuthorize("hasRole('ETUDIANT')")
    @PostMapping("/questions")
    public ResponseEntity<Question> ajouterQuestion(@RequestBody LibelleQuestion libelleQuestion,
                                                    Authentication authentication,
                                                    UriComponentsBuilder base)
    {

        long id = Long.parseLong(authentication.getName());

        Question question = facadeApplication.ajouterUneQuestion(id, libelleQuestion.libelleQuestion);
        URI location = base.path("questions/{idQuestion}")
                .buildAndExpand(question.getIdQuestion())
                .toUri();
        return ResponseEntity.created(location).body(question);
    }

    @PreAuthorize("hasRole('ENSEIGNANT')")
    @PatchMapping("/questions/{idQuestion}")
    public ResponseEntity<String> repondreQuestion(@PathVariable String idQuestion,
                                                   @RequestBody String reponse)
            throws QuestionInexistanteException
    {
        this.facadeApplication.repondreAUneQuestion(idQuestion, reponse);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/questions")
    public ResponseEntity<Collection<Question>> getSomeQuestionsByUtilisateur(@RequestParam Optional<String> filtre,
                                                                              Authentication authentication)
    {
        long id = Long.parseLong(authentication.getName());
        String f = filtre.orElse("sansFiltre");

        return switch (f) {
            case "sansReponse" ->
                    ResponseEntity.ok(facadeApplication.getQuestionsSansReponsesByUser(id));
            case "avecReponse" ->
                    ResponseEntity.ok(facadeApplication.getQuestionsAvecReponsesByUser(id));
            default -> ResponseEntity.ok(facadeApplication.getToutesLesQuestionsByUser(id));
        };



    }

    @GetMapping("/questions/{idQuestion}")
    public ResponseEntity<Question> getQuestionByUtilisateur(@PathVariable String idQuestion,
                                                             Authentication authentication)
    {
        long id = Long.parseLong(authentication.getName());
        Question question = facadeApplication.ajouterUneQuestion(id, idQuestion);
        boolean isEnseignant = authentication.getAuthorities().stream().map(Object::toString).collect(Collectors.joining(",")).contains("ROLE_ENSEIGNANT");
        if (question.getIdUtilisateur() == id || isEnseignant) {
            return ResponseEntity.ok(question);
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}