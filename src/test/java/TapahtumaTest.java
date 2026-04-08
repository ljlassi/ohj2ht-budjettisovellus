import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Kategoria;
import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Tapahtuma;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TapahtumaTest {

    @Test
    void nimiTesti() {
        String nimi = "Testi Tapahtuma";
        Tapahtuma tapahtuma = new Tapahtuma();
        tapahtuma.setNimi(nimi);
        assertEquals(nimi, tapahtuma.getNimi(), "Tapahtuman nimen tulisi täsmätä.");
    }

    @Test
    void summaTesti() {
        double summa = 125.65;
        Tapahtuma tapahtuma = new Tapahtuma();
        tapahtuma.setSumma(summa);
        assertEquals(summa, tapahtuma.getSumma(), "Summan tulisi täsmätä.");
        double negativiinenSumma = -999.96;
        Tapahtuma toinenTapahtuma = new Tapahtuma();
        toinenTapahtuma.setSumma(negativiinenSumma);
        assertEquals(negativiinenSumma, toinenTapahtuma.getSumma(), "Summan tulisi täsmätä.");
    }

    @Test
    void paivamaaraTesti() {
        LocalDate paivamaara = LocalDate.now();
        Tapahtuma tapahtuma = new Tapahtuma();
        tapahtuma.setPaivamaara(paivamaara);
        assertEquals(paivamaara, tapahtuma.getPaivamaara(), "Päivämäärän tulisi täsmätä.");
    }

    @Test
    void kategoriaTesti() {
        String kategorianNimi = "Testikategoria";
        boolean pakollinen = true;
        Kategoria kategoria = new Kategoria(kategorianNimi, pakollinen);
        Tapahtuma tapahtuma = new Tapahtuma();
        tapahtuma.setKategoria(kategoria);
        assertEquals(kategoria, tapahtuma.getKategoria(), "Tapahtuman kategoria-olioiden kuuluisi täsmätä.");
    }

}
