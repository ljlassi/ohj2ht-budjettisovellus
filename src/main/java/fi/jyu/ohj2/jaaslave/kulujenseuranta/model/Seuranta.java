package fi.jyu.ohj2.jaaslave.kulujenseuranta.model;

import fi.jyu.ohj2.jaaslave.kulujenseuranta.persistence.RepositoryException;
import fi.jyu.ohj2.jaaslave.kulujenseuranta.persistence.SeurantaRepository;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Seuranta {

    private final ObservableList<Kategoria> kategoriat = FXCollections.observableArrayList(
            kategoria -> new Observable[] {
                    kategoria.nimiProperty(),
                    kategoria.pakollinenProperty()
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

    private final SeurantaRepository repository;

    public Seuranta(SeurantaRepository repository) {

        this.repository = repository;

        this.kategoriat.addListener((ListChangeListener<Kategoria>) change -> {
            while(change.next()) {
                if(change.wasRemoved()) {
                        this.tapahtumat.forEach(t -> {
                            if(t.getKategoria().getNimi().equals(change.getRemoved().getFirst().getNimi())) {
                                t.setKategoria(null);
                            }
                        });
                } else if(change.wasUpdated()) {
                    for (int i = change.getFrom(); i < change.getTo(); i++) {
                        Kategoria k = change.getList().get(i);
                        this.tapahtumat.forEach(t -> {
                            if(t.getKategoria() == k)
                                t.setKategoria(k);
                        });
                    }
                }
            }
            this.tallennaKategoriat();
            if(!this.getTapahtumat().isEmpty()) {
                this.tallennaTapahtumat(); // Tallennetaan myös tapahtumat (mikäli niitä on), siltä varala että kategoriamuutokset heijastuu myös niihin.
            }
        } );

        this.tapahtumat.addListener((ListChangeListener<Tapahtuma>) _ -> this.tallennaTapahtumat() );

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
        if(kategoria == null) {
            return;
        }
        if (kategoria.getNimi().isBlank()) {
            return;
        }
        this.kategoriat.add(kategoria);
    }

    public void lataaTapahtumat() {
        try {
            List<Tapahtuma> kaikkiTapahtumat = repository.lataaTapahtumat();
            tapahtumat.addAll(kaikkiTapahtumat);
        } catch(RepositoryException e) {
            IO.println(e.getMessage());
        }
    }

    public void tallennaTapahtumat() {
        try {
            repository.tallennaTapahtumat(this.getTapahtumat());
        } catch(RepositoryException e) {
            IO.println(e.getMessage());
        }
    }

    public void lataaKategoriat() {
        try {
            List<Kategoria> kaikkiKategoriat = repository.lataaKategoriat();
            kategoriat.addAll(kaikkiKategoriat);
        } catch(RepositoryException e) {
            IO.println(e.getMessage());
        }
    }

    public void tallennaKategoriat() {
        try {
            repository.tallennaKategoriat(this.getKategoriat());
        } catch(RepositoryException e) {
            IO.println(e.getMessage());
        }
    }



}
