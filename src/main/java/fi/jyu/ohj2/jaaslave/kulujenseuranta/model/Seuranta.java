package fi.jyu.ohj2.jaaslave.kulujenseuranta.model;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        this.kategoriat.addListener((ListChangeListener<Kategoria>) change -> {
            while(change.next()) {
                if(change.wasRemoved()) {
                        this.tapahtumat.forEach(t -> {
                            if(t.getKategoria().getNimi().equals(change.getRemoved().getFirst().getNimi())) {
                                t.setKategoria(null);
                            }
                        });
                }
            }
        } );

        this.tapahtumat.addListener(new ListChangeListener<Tapahtuma>() {
            @Override
            public void onChanged(Change<? extends Tapahtuma> c) {
                while (c.next()) {
                    if (c.wasUpdated()) {
                        for (int i = c.getFrom(); i < c.getTo(); ++i) {
                            if(tapahtumat.get(i).getNimi().isBlank()) { // Tämä on vähän hassusti tehty ja ehkä paranellaan myöhemmin, toimii sinällään.
                                tapahtumat.remove(tapahtumat.get(i));   // Ongelmana siis ylläolevaan liittyen että poisto ei tahdo välittyä aina, mutta nimen tyhjennys kyllä välittyy.
                            }
                        }
                    }
                }
            }
        });

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
