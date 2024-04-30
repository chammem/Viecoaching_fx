package services;
import java.sql.SQLException;
import java.util.List;

public interface IService <T>{
    public void ajouter(T t) throws SQLException;
    public void supprimer(int id) throws SQLException;
    public List<T> afficher() throws SQLException;
<<<<<<< HEAD
    public void modifier(T t) throws SQLException;


=======
    T trouverParId(int id) throws SQLException;
>>>>>>> 400a8ba329c716088c97e277c00b2f54dbc8e483
}