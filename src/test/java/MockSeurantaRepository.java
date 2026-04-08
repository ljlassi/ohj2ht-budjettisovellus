import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Kategoria;
import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Tapahtuma;
import fi.jyu.ohj2.jaaslave.kulujenseuranta.persistence.SeurantaRepository;

import java.util.ArrayList;
import java.util.List;

public class MockSeurantaRepository implements SeurantaRepository {

    private final List<Kategoria> tallennetutKategoriat = new ArrayList<>();
    private final List<Tapahtuma> tallennetutTapahtumat = new ArrayList<>();

    @Override
    public List<Kategoria> lataaKategoriat() {
        return tallennetutKategoriat;
    }

    @Override
    public List<Tapahtuma> lataaTapahtumat() {
        return tallennetutTapahtumat;
    }

    public void tallennaKategoriat(List<Kategoria> kategoriat) {
        tallennetutKategoriat.clear();
        for (Kategoria kategoria : kategoriat) {
            Kategoria kopio = new Kategoria();
            kopio.setNimi(kategoria.getNimi());
            kopio.setPakollinen(kategoria.getPakollinen());
            tallennetutKategoriat.add(kopio);
        }

    }

    public void tallennaTapahtumat(List<Tapahtuma> tapahtumat) {
        tallennetutTapahtumat.clear();
        for (Tapahtuma tapahtuma : tapahtumat) {
            Tapahtuma kopio = new Tapahtuma();
            kopio.setNimi(tapahtuma.getNimi());
            kopio.setPaivamaara(tapahtuma.getPaivamaara());
            kopio.setSumma(tapahtuma.getSumma());
            kopio.setKategoria(tapahtuma.getKategoria());
            tallennetutTapahtumat.add(kopio);
        }
    }

    public List<Kategoria> getTallennetutKategoriat() {
        return this.tallennetutKategoriat;
    }

    public List<Tapahtuma> getTallennetutTapahtumat() {
        return this.tallennetutTapahtumat;
    }

}
