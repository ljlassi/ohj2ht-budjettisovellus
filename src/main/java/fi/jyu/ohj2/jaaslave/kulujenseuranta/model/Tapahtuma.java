package fi.jyu.ohj2.jaaslave.kulujenseuranta.model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Tapahtuma {

    private final StringProperty nimi = new SimpleStringProperty("");
    private final DoubleProperty summa = new SimpleDoubleProperty(0.0);
    private final ObjectProperty<LocalDate> paivamaara = new SimpleObjectProperty<LocalDate>(LocalDate.now());

    private final ObjectProperty<Kategoria> kategoria = new SimpleObjectProperty<Kategoria>(new Kategoria("Yleinen", false));

    public Tapahtuma() {
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

    public void setSumma(double summa) {
        this.summa.set(summa);
    }

    public double getSumma() {
        return this.summa.get();
    }

    public DoubleProperty summaProperty() {
        return this.summa;
    }

    public ObjectProperty<LocalDate> paivamaaraProperty() {
        return this.paivamaara;
    }

    public LocalDate getPaivamaara() {
        return this.paivamaara.get();
    }

    public void setPaivamaara(LocalDate paivamaara) {
        this.paivamaara.set(paivamaara);
    }

    public ObjectProperty<Kategoria> kategoriaProperty() {
        return this.kategoria;
    }

    public Kategoria getKategoria() {
        return this.kategoria.get();
    }

    public void setKategoria(Kategoria kategoria) {
        this.kategoria.set(kategoria);
    }


    public TarkistusVirhe tarkistaVirheet() {
        if(getNimi().isBlank()) {
            return TarkistusVirhe.NIMI_TYHJA;
        }
        if(getNimi().length() > 255) {
            return TarkistusVirhe.NIMI_EPAVALIDI;
        }
        if(getPaivamaara() == null) {
            return TarkistusVirhe.PAIVAMAARA_TYHJA;
        }
        if(getPaivamaara().isAfter(LocalDate.now())) {
            return TarkistusVirhe.PAIVAMAARA_EPAVALIDI;
        }
        if(getKategoria() == null) {
            return TarkistusVirhe.KATEGORIA_TYHJA;
        }
        return null;
    }

}
