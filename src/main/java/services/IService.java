package services;
import entities.Utilisateur;

import java.sql.SQLException;
import java.util.List;

public interface IService <T>{
    public void ajouter(T t) throws SQLException;
    public void supprimer(int id) throws SQLException;
    public List<T> afficher() throws SQLException;
    public void modifier(T t) throws SQLException;
    public void ajouterAvecUtilisateurs(T t, List<Utilisateur> utilisateursSelectionnes) throws SQLException;




}