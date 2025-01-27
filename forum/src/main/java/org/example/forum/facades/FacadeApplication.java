package org.example.forum.facades;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.forum.dao.QuestionRepository;
import org.example.forum.dao.UtilisateurRepository;
import org.example.forum.exceptions.UtilisateurInexistantException;
import org.example.forum.exceptions.QuestionInexistanteException;
import org.example.forum.modele.Question;
import org.example.forum.modele.Utilisateur;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FacadeApplication {

    private final QuestionRepository questionRepository;
    private final UtilisateurRepository utilisateurRepository;

    @PersistenceContext
    private EntityManager em;

    public FacadeApplication(QuestionRepository questionRepository, UtilisateurRepository utilisateurRepository) {
        this.questionRepository = questionRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    public Utilisateur addUser(long id, String email) {
        Utilisateur u = new Utilisateur(id, email);
        return utilisateurRepository.save(u);
    }

    /**
     * Permet à un utilisateur de poser une question.
     */
    @Transactional
    public Question ajouterUneQuestion(long idUtilisateur, String libelleQuestion) throws UtilisateurInexistantException {

        Utilisateur utilisateur = utilisateurRepository.findById(idUtilisateur).orElseThrow(() -> new UtilisateurInexistantException(idUtilisateur));

        Question question = new Question(utilisateur, libelleQuestion);
        return questionRepository.save(question);
    }

    /**
     * Ajoute/remplace une réponse à une question.
     */
    @Transactional
    public void repondreAUneQuestion(String idQuestion, String reponse) throws QuestionInexistanteException {
        Question question = questionRepository.findById(idQuestion).orElseThrow(QuestionInexistanteException::new);
        question.setReponse(reponse);
        questionRepository.save(question);
    }

    /**
     * Retourne toutes les questions en attente de réponse.
     */
    public Collection<Question> getQuestionsSansReponses() {
        return questionRepository.findAllByReponseIsNull();
    }

    /**
     * Retourne toutes les questions posées par un utilisateur et pour lesquelles quelqu'un a répondu.
     */
    @Transactional
    public Collection<Question> getQuestionsAvecReponsesByUser(long idUtilisateur) throws UtilisateurInexistantException {

//        Collection<Question> questions = em.createQuery("Select q from Utilisateur u join u.questions q where u.id =: idUtilisateur and q.reponse is NULL", Question.class)
//                .setParameter("idUtilisateur", idUtilisateur).getResultList();
//        return questions;
//
//        Collection<Question> questions1 = em.createQuery("select q from Question q where q.utilisateur.id =: idUtilisateur and q.reponse is null ", Question.class)
//                .setParameter("idUtilisateur", idUtilisateur).getResultList();
//        return questions1;

        Utilisateur utilisateur = utilisateurRepository.findById(idUtilisateur).orElseThrow(() -> new UtilisateurInexistantException(idUtilisateur));
        return questionRepository.findByUtilisateurAndReponseIsNotNull(utilisateur);
    }

    /**
     * Retourne toutes les questions posées par un utilisateur et pour lesquelles personne n'a répondu.
     */
    public Collection<Question> getQuestionsSansReponsesByUser(long idUtilisateur) throws UtilisateurInexistantException {
        Utilisateur utilisateur = utilisateurRepository.findById(idUtilisateur).orElseThrow(() -> new UtilisateurInexistantException(idUtilisateur));
        return questionRepository.findByUtilisateurAndReponseIsNull(utilisateur);

    }

    /**
     * Retourne toutes les questions posées par un utilisateur.
     */
    public Collection<Question> getToutesLesQuestionsByUser(long idUtilisateur) throws UtilisateurInexistantException {
        Utilisateur utilisateur = utilisateurRepository.findById(idUtilisateur).orElseThrow(() -> new UtilisateurInexistantException(idUtilisateur));
        return questionRepository.findByUtilisateur(utilisateur);

    }

    /**
     * Retourne l'ensemble des questions posées.
     */
    public Collection<Question> getToutesLesQuestions() {
        return questionRepository.findAll();
    }

    /**
     * Retourne une question d'un utilisateur, si cette question a bien été posée par cet utilisateur.
     */
    public Question getQuestionByIdForUser(long idUtilisateur, String idQuestion) throws UtilisateurInexistantException {
        Utilisateur utilisateur = utilisateurRepository.findById(idUtilisateur).orElseThrow(() -> new UtilisateurInexistantException(idUtilisateur));
        return questionRepository.findByUtilisateurAndIdQuestion(utilisateur, idQuestion);
    }
}
