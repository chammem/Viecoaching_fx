package controllers.Rubrique;

import javafx.beans.property.SimpleStringProperty;

public class AppModel {
    private SimpleStringProperty id;
    private SimpleStringProperty auteur;
    private SimpleStringProperty title;
    private SimpleStringProperty datePublication;
    private SimpleStringProperty contenu;
    
    public AppModel(String id, String auteur, String title, String datePublication, String contenu) {
        this.id= new SimpleStringProperty(id);
        this.auteur = new SimpleStringProperty(auteur);
        this.title = new SimpleStringProperty(title);
        this.datePublication = new SimpleStringProperty(datePublication);
        this.contenu = new SimpleStringProperty(contenu);
    }

    public String getAuteur() {
        return auteur.get();
    }
    public String getId() {return id.get();}
    public String getTitle() {
        return title.get();
    }


    public String getDate_publication() {
        return datePublication.get();
    }
    public String getContenu() {
        return contenu.get();
    }

}
