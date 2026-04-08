package fi.jyu.ohj2.jaaslave.kulujenseuranta.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Kategoria {

    private final StringProperty nimi = new SimpleStringProperty("");

    public Kategoria() {}

    public Kategoria(String nimi) {
        this.nimi.set(nimi);
    }

    public void setNimi(String nimi) {
        this.nimi.set(nimi);
    }

    public String getNimi() {
        return this.nimi.get();
    }

    public StringProperty nimiProperty() {
        return this.nimi;
    }

    public TarkistusVirhe tarkistaVirheet() {
        if(getNimi().isBlank()) {
            return TarkistusVirhe.NIMI_TYHJA;
        }
        if(getNimi().length() > 60) {
            return TarkistusVirhe.NIMI_EPAVALIDI;
        }
        return null;
    }

    @Override
    public String toString() {
        return this.getNimi();
    }


}
