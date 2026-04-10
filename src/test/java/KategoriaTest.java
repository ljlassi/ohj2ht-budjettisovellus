import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Kategoria;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KategoriaTest {

    @Test
    void nimiTesti() {
        String kategorianNimi = "TestiKategoria";
        boolean pakollinen = false;
        Kategoria kategoria = new Kategoria(kategorianNimi, pakollinen);
        assertEquals(kategorianNimi, kategoria.getNimi(), "Annettun nimen tulisi täsmätä.");
        Kategoria toinenTestiKategoria = new Kategoria();
        toinenTestiKategoria.setNimi(kategorianNimi);
        assertEquals(kategorianNimi, toinenTestiKategoria.getNimi(), "Annetun nimen tulisi täsmätä.");
    }

    @Test
    void pakollinenTesti() {
        String kategorianNimi = "Toinen Testi Kategoria";
        boolean pakollinen = true;
        Kategoria kategoria = new Kategoria(kategorianNimi, pakollinen);
        assertEquals(pakollinen, kategoria.getPakollinen(), "Kategorian pakollinen-propertyn tulisi olla tosi.");
    }

}
