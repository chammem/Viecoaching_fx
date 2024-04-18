package entities;

import java.util.ArrayList;
import java.util.List;

public class Ressources {
    private int id;
    private String type_r ;
    private  String titre_r ;
    private  String url ;
    private  String description ;
    private List<Categorie> categorieList = new ArrayList<>();

    public Ressources(String type_r, String titre_r, String url, String description, List<Categorie> categorieList) {
        this.type_r = type_r;
        this.titre_r = titre_r;
        this.url = url;
        this.description = description;
        this.categorieList = categorieList;
    }

    public Ressources() {

    }

    public Ressources(int id, String type_r, String titre_r, String url, String description, List<Categorie> categorieList) {
        this.id = id;
        this.type_r = type_r;
        this.titre_r = titre_r;
        this.url = url;
        this.description = description;
        this.categorieList = categorieList;
    }

    public List<Categorie> getCategorieList() {
        return categorieList;
    }

    public void setCategorieList(List<Categorie> categorieList) {
        this.categorieList = categorieList;
    }

    public Ressources(int id, String type_r, String titre_r, String url, String description) {
        this.id = id;
        this.type_r = type_r;
        this.titre_r = titre_r;
        this.url = url;
        this.description = description;
    }
    public Ressources(String type_r, String titre_r, String url, String description) {
        this.type_r = type_r;
        this.titre_r = titre_r;
        this.url = url;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType_r() {
        return type_r;
    }

    public void setType_r(String type_r) {
        this.type_r = type_r;
    }

    public String getTitre_r() {
        return titre_r;
    }

    public void setTitre_r(String titre_r) {
        this.titre_r = titre_r;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Ressources{" +
                "id=" + id +
                ", type_r='" + type_r + '\'' +
                ", titre_r='" + titre_r + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
