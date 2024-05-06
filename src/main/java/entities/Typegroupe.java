package entities;

public class Typegroupe {
    private int id;
    private String nomtype;

    public Typegroupe(int id, String nomtype) {
        this.id = id;
        this.nomtype = nomtype;
    }

    public Typegroupe() {

    }

    public Typegroupe(String nomtype) {
        this.nomtype = nomtype;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomtype() {
        return nomtype;
    }

    public void setNomtype(String nomtype) {
        this.nomtype = nomtype;
    }

    @Override
    public String toString() {
        return "Typegroupe{" +
                "id=" + id +
                ", nomtype='" + nomtype + '\'' +
                '}';
    }
}
