package fi.jyu.ohj2.jaaslave.kulujenseuranta.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Kategoria {

    private final StringProperty nimi = new SimpleStringProperty("");
    private final BooleanProperty pakollinen = new SimpleBooleanProperty(false);

    public Kategoria() {}

    public Kategoria(String nimi, boolean pakollinen) {
        this.nimi.set(nimi);
        this.pakollinen.set(pakollinen);
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

    public void setPakollinen(boolean pakollinen) {
        this.pakollinen.set(pakollinen);
    }

    public boolean getPakollinen() {
        return this.pakollinen.get();
    }

    public BooleanProperty pakollinenProperty() {
        return this.pakollinen;
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
