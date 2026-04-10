package fi.jyu.ohj2.jaaslave.kulujenseuranta.model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Tapahtuma {

    private final StringProperty nimi = new SimpleStringProperty("");
    private final DoubleProperty summa = new SimpleDoubleProperty(0.0);
    private final ObjectProperty<LocalDate> paivamaara = new SimpleObjectProperty<>(LocalDate.now());

    private final ObjectProperty<Kategoria> kategoria = new SimpleObjectProperty<>(new Kategoria("Yleinen", false));

    public Tapahtuma() {
    }

    public String getNimi() {
        return nimi.get();
    }

    public void setNimi(String nimi) {
        this.nimi.set(nimi);
    }

    public StringProperty nimiProperty() {
        return nimi;
    }

    public double getSumma() {
        return summa.get();
    }

    public void setSumma(double summa) {
        this.summa.set(summa);
    }

    public DoubleProperty summaProperty() {
        return summa;
    }

    public ObjectProperty<LocalDate> paivamaaraProperty() {
        return paivamaara;
    }

    public LocalDate getPaivamaara() {
        return paivamaara.get();
    }

    public void setPaivamaara(LocalDate paivamaara) {
        this.paivamaara.set(paivamaara);
    }

    public ObjectProperty<Kategoria> kategoriaProperty() {
        return kategoria;
    }

    public Kategoria getKategoria() {
        return kategoria.get();
    }

    public void setKategoria(Kategoria kategoria) {
        this.kategoria.set(kategoria);
    }


    public TarkistusVirhe tarkistaVirheet() {
        if (getNimi().isBlank()) {
            return TarkistusVirhe.NIMI_TYHJA;
        }
        if (getNimi().length() > 255) {
            return TarkistusVirhe.NIMI_EPAVALIDI;
        }
        if (getPaivamaara() == null) {
            return TarkistusVirhe.PAIVAMAARA_TYHJA;
        }
        if (getPaivamaara().isAfter(LocalDate.now())) {
            return TarkistusVirhe.PAIVAMAARA_EPAVALIDI;
        }
        if (getKategoria() == null) {
            return TarkistusVirhe.KATEGORIA_TYHJA;
        }
        return null;
    }

}
