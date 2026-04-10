package fi.jyu.ohj2.jaaslave.kulujenseuranta.persistence;

import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Kategoria;
import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Tapahtuma;

import java.util.List;

public interface SeurantaRepository {

    List<Kategoria> lataaKategoriat() throws RepositoryException;

    List<Tapahtuma> lataaTapahtumat() throws RepositoryException;

    void tallennaKategoriat(List<Kategoria> kategoriat) throws RepositoryException;

    void tallennaTapahtumat(List<Tapahtuma> tapahtumat) throws RepositoryException;
}
