package org.example.forum.dao;

import org.example.forum.modele.Question;
import org.example.forum.modele.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {

    Collection<Question> findAllByReponseIsNull();
    Question findByUtilisateurAndIdQuestion(Utilisateur utilisateur, String idQuestion);
    Collection<Question> findByUtilisateurAndReponseIsNull(Utilisateur utilisateur);
    Collection<Question> findByUtilisateurAndReponseIsNotNull(Utilisateur utilisateur);
    Collection<Question> findByUtilisateur(Utilisateur utilisateur);
}
