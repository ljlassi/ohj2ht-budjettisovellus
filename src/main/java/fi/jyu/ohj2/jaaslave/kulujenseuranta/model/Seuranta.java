package fi.jyu.ohj2.jaaslave.kulujenseuranta.model;

import fi.jyu.ohj2.jaaslave.kulujenseuranta.persistence.RepositoryException;
import fi.jyu.ohj2.jaaslave.kulujenseuranta.persistence.SeurantaRepository;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.List;

public class Seuranta {

    private final ObservableList<Kategoria> kategoriat = FXCollections.observableArrayList(
            kategoria -> new Observable[]{
                    kategoria.nimiProperty(),
                    kategoria.pakollinenProperty()
            }
    );
    private final ObservableList<Tapahtuma> tapahtumat = FXCollections.observableArrayList(
            tapahtuma -> new Observable[]{
                    tapahtuma.nimiProperty(),
                    tapahtuma.summaProperty(),
                    tapahtuma.paivamaaraProperty(),
                    tapahtuma.kategoriaProperty()
            }
    );

    private final SeurantaRepository repository;

    public Seuranta(SeurantaRepository repository) {

        this.repository = repository;

        kategoriat.addListener((ListChangeListener<Kategoria>) change -> {
            while (change.next()) {
                if (change.wasRemoved()) {
                    tapahtumat.forEach(t -> {
                        if (t.getKategoria().getNimi().equals(change.getRemoved().getFirst().getNimi())) {
                            t.setKategoria(null);
                        }
                    });
                }
                if (change.wasUpdated() || change.wasReplaced()) {
                    for (int i = change.getFrom(); i < change.getTo(); i++) {
                        Kategoria k = change.getList().get(i);
                        tapahtumat.forEach(t -> {
                            if(t.getKategoria() == null) {
                                t.setKategoria(null);
                            } else if (t.getKategoria().equals(k))
                                t.setKategoria(k);
                            else if (t.getKategoria().nimetSamoja(k)) {
                                t.setKategoria(k);
                            }
                        });
                            
                    }
                }
            }
            this.tallennaKategoriat();
            if (!getTapahtumat().isEmpty()) {
                tallennaTapahtumat(); // Tallennetaan myös tapahtumat (mikäli niitä on), siltä varala että kategoriamuutokset heijastuu myös niihin.
            }
        });

        this.tapahtumat.addListener((ListChangeListener<Tapahtuma>) _ -> tallennaTapahtumat());

    }

    public ObservableList<Tapahtuma> getTapahtumat() {
        return tapahtumat;
    }

    public void lisaaTapahtuma(Tapahtuma tapahtuma) {
        tapahtumat.add(tapahtuma);
    }

    public ObservableList<Kategoria> getKategoriat() {
        return kategoriat;
    }

    public void lisaaKategoria(Kategoria kategoria) {
        if (kategoria == null) {
            return;
        }
        if (kategoria.getNimi().isBlank()) {
            return;
        }
        kategoriat.add(kategoria);
    }

    public void lataaTapahtumat() {
        try {
            List<Tapahtuma> kaikkiTapahtumat = repository.lataaTapahtumat();
            tapahtumat.addAll(kaikkiTapahtumat);
        } catch (RepositoryException e) {
            IO.println(e.getMessage());
        }
    }

    public void tallennaTapahtumat() {
        try {
            repository.tallennaTapahtumat(getTapahtumat());
        } catch (RepositoryException e) {
            IO.println(e.getMessage());
        }
    }

    public void lataaKategoriat() {
        try {
            List<Kategoria> kaikkiKategoriat = repository.lataaKategoriat();
            kategoriat.addAll(kaikkiKategoriat);
        } catch (RepositoryException e) {
            IO.println(e.getMessage());
        }
    }

    public void tallennaKategoriat() {
        try {
            repository.tallennaKategoriat(getKategoriat());
        } catch (RepositoryException e) {
            IO.println(e.getMessage());
        }
    }


}
