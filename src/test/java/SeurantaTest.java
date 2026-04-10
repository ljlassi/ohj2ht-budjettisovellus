import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Kategoria;
import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Seuranta;
import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Tapahtuma;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeurantaTest {

    @Test
    public void lisaaKategoriaTest_NimiOikein() {
        MockSeurantaRepository mock = new MockSeurantaRepository();
        Seuranta seuranta = new Seuranta(mock);

        String testiNimi = "TestiKategoria";
        boolean pakollinen = true;
        Kategoria kategoria = new Kategoria(testiNimi, pakollinen);

        seuranta.lisaaKategoria(kategoria);

        assertEquals(1, seuranta.getKategoriat().size(), "Kategorioita kuuluisi olla yksi.");
        assertEquals(testiNimi, seuranta.getKategoriat().getFirst().getNimi(), "Kategorian nimen kuuluisi täsmätä.");
        assertEquals(testiNimi, mock.getTallennetutKategoriat().getFirst().getNimi(), "Kategorian nimen kuuluisi täsmätä myös tietovarastossa.");
    }

    @Test
    public void lisaaKategoriaTest_TyhjaNimi() {
        MockSeurantaRepository mock = new MockSeurantaRepository();
        Seuranta seuranta = new Seuranta(mock);

        String testiNimi = "     ";
        Kategoria kategoria = new Kategoria(testiNimi, false);

        seuranta.lisaaKategoria(kategoria);
        assertEquals(0, seuranta.getKategoriat().size(), "Ilman nimeä tai tyhjällä sellaisella olevat kategoriat eivät saisi tallentua listaan.");
    }

    @Test
    public void lisaaTapahtumaTest() {
        MockSeurantaRepository mock = new MockSeurantaRepository();
        Seuranta seuranta = new Seuranta(mock);

        String testiKategorianNimi = "Yleiskategoria";
        boolean pakollinen = true;
        Kategoria testiKategoria = new Kategoria(testiKategorianNimi, pakollinen);

        String testiNimi = "Testi Tapahtuma";
        double testiSumma = -199.95;
        LocalDate paivamaara = LocalDate.now();

        Tapahtuma testiTapahtuma = new Tapahtuma();
        testiTapahtuma.setNimi(testiNimi);
        testiTapahtuma.setKategoria(testiKategoria);
        testiTapahtuma.setSumma(testiSumma);
        testiTapahtuma.setPaivamaara(paivamaara);

        seuranta.lisaaKategoria(testiKategoria);
        seuranta.lisaaTapahtuma(testiTapahtuma);

        assertEquals(1, seuranta.getKategoriat().size(), "Kategorioita kuuluisi olla tässä kohtaa seurannassa tasan yksi.");
        assertEquals(1, seuranta.getTapahtumat().size(), "Tapahtumia kuuluisi tässä kohtaa olla seurannassa tasan yksi.");
        assertEquals(paivamaara, seuranta.getTapahtumat().getFirst().getPaivamaara(), "Päivämäärien kuuluisi olla tässä testissä identtisiä.");
    }

}
