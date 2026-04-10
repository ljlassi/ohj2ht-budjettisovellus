package fi.jyu.ohj2.jaaslave.kulujenseuranta.controller;

import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Kategoria;
import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.TarkistusVirhe;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class KategoriaController implements Initializable {

    @FXML
    TextField uusiKategoriaKentta;

    @FXML
    Button lisaaUusiKategoriaPainike;

    @FXML
    ComboBox<Kategoria> muokattavanKategorianValitsin;

    @FXML
    TextField kategorianNimenMuokkain;

    @FXML
    Button poistaKategoriaPainike;

    @FXML
    Button tallennaMuutoksetKategoriaanPainike;

    @FXML
    Button poistuKategoriaNakymastaPainike;

    @FXML
    CheckBox kategoriaPakollinenCheckBox;

    private ObservableList<Kategoria> kategoriat;

    private Kategoria valittuKategoria;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        lisaaUusiKategoriaPainike.setOnAction(_ -> lisaaUusiKategoria());
        poistaKategoriaPainike.setOnAction(_ -> poistaKategoria());
        tallennaMuutoksetKategoriaanPainike.setOnAction(_ -> tallennaMuutoksetKategoriaan());
        poistuKategoriaNakymastaPainike.setOnAction(_ -> sulje());
        muokattavanKategorianValitsin.setOnAction(_ -> naytaValitunKategorianTiedot());

        paivitaNakyma();

    }

    private void sulje() {
        Scene scene = uusiKategoriaKentta.getScene();
        Stage ikkuna = (Stage) scene.getWindow();
        ikkuna.close();
    }

    /**
     * Tuodaan kategorialitaus tätä kautta tähän controlleriin.
     *
     * @param kategoriat Kategorialista
     */
    public void setKategoriat(ObservableList<Kategoria> kategoriat) {
        this.kategoriat = kategoriat;
        muokattavanKategorianValitsin.setItems(this.kategoriat);
    }

    private void naytaValitunKategorianTiedot() {
        valittuKategoria = muokattavanKategorianValitsin.getValue();
        kategorianNimenMuokkain.setText(this.valittuKategoria.getNimi());
        kategoriaPakollinenCheckBox.setSelected(this.valittuKategoria.getPakollinen());
    }

    /**
     * Kutsuu validointimetodia ja tallentaa sen jälkeen muutokset listaan.
     */
    private void tallennaMuutoksetKategoriaan() {
        TarkistusVirhe validointi = validoiMuokkaus();
        if (validointi != null) {
            naytaValidointiVirheIlmoitus(validointi);
            return;
        }
        String uusiNimi = kategorianNimenMuokkain.getText();
        boolean onkoPakollinen = kategoriaPakollinenCheckBox.isSelected();
        kategoriat.forEach(k -> {
            if (k == valittuKategoria) {
                k.setNimi(uusiNimi);
                k.setPakollinen(onkoPakollinen);
            }
        });
    }

    private void lisaaUusiKategoria() {
        TarkistusVirhe validointi = validoiLisays();
        if (validointi != null) {
            naytaValidointiVirheIlmoitus(validointi);
            return;
        }
        String nimi = uusiKategoriaKentta.getText();
        Kategoria uusiKategoria = new Kategoria(nimi, false);
        TarkistusVirhe sisainenValidointi = uusiKategoria.tarkistaVirheet();
        if (sisainenValidointi != null) {
            naytaValidointiVirheIlmoitus(sisainenValidointi);
            return;
        }
        kategoriat.add(uusiKategoria);
        paivitaNakyma();
    }

    private void paivitaNakyma() {
        muokattavanKategorianValitsin.setItems(this.kategoriat);
    }

    private void poistaKategoria() {
        if (valittuKategoria == null) {
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Varmistusdialogi");
        alert.setHeaderText("Haluatko varmasti poistaa kategorian?");
        alert.setContentText("Kateogoria " + muokattavanKategorianValitsin.getValue().getNimi() + " poistetaan.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() != ButtonType.OK) {
            return;
        }
        Kategoria poistettavaKategoria = muokattavanKategorianValitsin.getValue();
        kategoriat.remove(poistettavaKategoria);
    }

    private TarkistusVirhe validoiLisays() {
        if (uusiKategoriaKentta.getText().isBlank()) {
            return TarkistusVirhe.NIMI_TYHJA;
        }
        if (uusiKategoriaKentta.getText().length() > 60) {
            return TarkistusVirhe.NIMI_EPAVALIDI;
        }
        return null;
    }

    private TarkistusVirhe validoiMuokkaus() {
        if (valittuKategoria == null) {
            return TarkistusVirhe.KATEGORIA_TYHJA;
        }
        if (kategorianNimenMuokkain.getText().isBlank()) {
            return TarkistusVirhe.NIMI_TYHJA;
        }
        if (kategorianNimenMuokkain.getText().length() > 60) {
            return TarkistusVirhe.NIMI_EPAVALIDI;
        }
        return null;
    }

    private void naytaValidointiVirheIlmoitus(TarkistusVirhe virhe) {
        String virheIlmoitus = "";
        switch (virhe) {
            case TarkistusVirhe.NIMI_TYHJA -> virheIlmoitus = "Nimikenttä ei saa olla tyhjä.";
            case TarkistusVirhe.NIMI_EPAVALIDI ->
                    virheIlmoitus = "Kategorialle annettu nimi on epävalidi tai liian pitkä.";
            case TarkistusVirhe.KATEGORIA_TYHJA -> virheIlmoitus = "Valitse muokattava kategoria.";
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validointivirhe");
        alert.setHeaderText("Virhe kategorian tiedoissa!");
        alert.setContentText(virheIlmoitus);
        alert.show();
    }
}
