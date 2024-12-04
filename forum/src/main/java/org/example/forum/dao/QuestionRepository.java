package org.example.forum.dao;

import org.example.forum.modele.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface QuestionRepository extends JpaRepository<Question, String> {

    Collection<Question> findAllByIdUtilisateur(long idUtilisateur);
    Collection<Question> findAllByReponseIsNull();
    Collection<Question> findAllByIdUtilisateurAndReponseIsNotNull(long idUtilisateur);
    Collection<Question> findAllByIdUtilisateurAndReponseIsNull(long idUtilisateur);
    Question findByIdUtilisateurAndIdQuestion(long idUtilisateur, String idQuestion);
}
