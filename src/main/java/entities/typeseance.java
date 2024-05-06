package entities;

public class typeseance {
    int typeSeanceId;
    String nomtype;

    public typeseance() {
    }

    public typeseance(int typeSeanceId, String nomtype) {
        this.typeSeanceId = typeSeanceId;
        this.nomtype = nomtype;
    }

    public typeseance(String nomtype) {
        this.nomtype = nomtype;
    }

    public typeseance(int typeSeanceId) {
        this.typeSeanceId = typeSeanceId;
    }

    public int getTypeSeanceId() {
        return typeSeanceId;
    }

    public void setTypeSeanceId(int typeSeanceId) {
        this.typeSeanceId = typeSeanceId;
    }

    public String getNomtype() {
        return nomtype;
    }

    public void setNomtype(String nomtype) {
        this.nomtype = nomtype;
    }

    @Override
    public String toString() {
        return "typeseance{" +
                "typeSeanceId=" + typeSeanceId +
                ", nomtype='" + nomtype + '\'' +
                '}';
    }
}
