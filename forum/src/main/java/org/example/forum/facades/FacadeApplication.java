package org.example.forum.facades;

import jakarta.transaction.Transactional;
import org.example.forum.dao.QuestionRepository;
import org.example.forum.facades.exceptions.QuestionInexistanteException;
import org.example.forum.modele.Question;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FacadeApplication {

    private final QuestionRepository questionRepository;

    public FacadeApplication(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    /**
     * Permet à un utilisateur de poser une question.
     */
    public Question ajouterUneQuestion(long idUtilisateur, String libelleQuestion) {
        Question question = new Question(idUtilisateur, libelleQuestion);
        return questionRepository.save(question);
    }

    /**
     * Ajoute/remplace une réponse à une question.
     */
    @Transactional
    public void repondreAUneQuestion(String idQuestion, String reponse) throws QuestionInexistanteException {
        Optional<Question> question = questionRepository.findById(idQuestion);
        if (question.isPresent()) {
            Question q = question.get();
            q.setReponse(reponse);
            questionRepository.save(q);
        } else {
            throw new QuestionInexistanteException();
        }
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
    public Collection<Question> getQuestionsAvecReponsesByUser(long idUtilisateur) {
        return questionRepository.findAllByIdUtilisateurAndReponseIsNotNull(idUtilisateur);
    }

    /**
     * Retourne toutes les questions posées par un utilisateur et pour lesquelles personne n'a répondu.
     */
    public Collection<Question> getQuestionsSansReponsesByUser(long idUtilisateur) {
        return questionRepository.findAllByIdUtilisateurAndReponseIsNull(idUtilisateur);
    }

    /**
     * Retourne toutes les questions posées par un utilisateur.
     */
    public Collection<Question> getToutesLesQuestionsByUser(long idUtilisateur) {
        return questionRepository.findAllByIdUtilisateur(idUtilisateur);
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
    public Question getQuestionByIdPourUnUtilisateur(long idUtilisateur, String idQuestion) {
        return questionRepository.findByIdUtilisateurAndIdQuestion(idUtilisateur, idQuestion);
    }
}
