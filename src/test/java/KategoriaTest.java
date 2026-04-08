import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Kategoria;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KategoriaTest {

    @Test
    void nimiTesti() {
        String kategorianNimi = "TestiKategoria";
        Kategoria kategoria = new Kategoria(kategorianNimi);
        assertEquals(kategorianNimi, kategoria.getNimi(), "Annettun nimen tulisi täsmätä.");
        Kategoria toinenTestiKategoria = new Kategoria();
        toinenTestiKategoria.setNimi(kategorianNimi);
        assertEquals(kategorianNimi, toinenTestiKategoria.getNimi(), "Annetun nimen tulisi täsmätä.");
    }

}
