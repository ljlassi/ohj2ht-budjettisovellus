package fi.jyu.ohj2.jaaslave.kulujenseuranta.model;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Seuranta {

    private final ObservableList<Kategoria> kategoriat = FXCollections.observableArrayList(
            kategoria -> new Observable[] {
                    kategoria.nimiProperty()
            }
    );
    private final ObservableList<Tapahtuma> tapahtumat = FXCollections.observableArrayList(
            tapahtuma -> new Observable[] {
                    tapahtuma.nimiProperty(),
                    tapahtuma.summaProperty(),
                    tapahtuma.paivamaaraProperty(),
                    tapahtuma.kategoriaProperty()
            }
    );

    public Seuranta() {

    }

    public ObservableList<Tapahtuma> getTapahtumat() {
        return this.tapahtumat;
    }

    public void lisaaTapahtuma(Tapahtuma tapahtuma) {
        this.tapahtumat.add(tapahtuma);
    }

    public ObservableList<Kategoria> getKategoriat() {
        return this.kategoriat;
    }

    public void lisaaKategoria(Kategoria kategoria) {
        this.kategoriat.add(kategoria);
    }

    public List<String> haeKategorioidenNimet() {
        List<String> nimet = new ArrayList<>();
        for (Kategoria k : kategoriat) {
            nimet.add(k.getNimi());
        }
        return nimet;
    }

}
