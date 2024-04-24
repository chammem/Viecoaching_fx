package entities;

public class Categorie {

    private  int id ;
    private String nom_categorie ;
    private  String description ;

    private  String image ;
    private Ressources ressource_id;

    public Categorie(String nom_categorie, String description, String image, Ressources ressources) {
        this.nom_categorie = nom_categorie;
        this.description = description;
        this.image = image;
        this.ressource_id = ressources;
    }

    public Ressources getRessources() {
        return ressource_id;
    }

    public void setRessources(Ressources ressources) {
        this.ressource_id = ressources;
    }

    public Categorie(int id, String nom_categorie, String description, String image, Ressources ressources) {
        this.id = id;
        this.nom_categorie = nom_categorie;
        this.description = description;
        this.image = image;
        this.ressource_id = ressources;
    }

    public Categorie(String nom_categorie, String description, String image) {
        this.nom_categorie = nom_categorie;
        this.description = description;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", nom_categorie='" + nom_categorie + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom_categorie() {
        return nom_categorie;
    }

    public void setNom_categorie(String nom_categorie) {
        this.nom_categorie = nom_categorie;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public Ressources getRessource_id() {
        return ressource_id;
    }

    public void setRessource_id(Ressources ressource_id) {
        this.ressource_id = ressource_id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Categorie(int id, String nom_categorie, String description, String image) {
        this.id = id;
        this.nom_categorie = nom_categorie;
        this.description = description;
        this.image = image;
    }

}
