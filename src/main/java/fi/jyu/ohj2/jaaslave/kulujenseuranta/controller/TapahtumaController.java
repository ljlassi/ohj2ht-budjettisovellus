package fi.jyu.ohj2.jaaslave.kulujenseuranta.controller;

import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Kategoria;
import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Tapahtuma;
import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.TarkistusVirhe;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class TapahtumaController implements Initializable {

    @FXML
    TextField tapahtumanAiheKentta;

    @FXML
    TextField tapahtumanSummaKentta;

    @FXML
    ComboBox<Kategoria> tapahtumanKategoriaValitsin;

    @FXML
    DatePicker paivamaaraValitsin;

    @FXML
    Button poistaTapahtumaPainike;

    @FXML
    Button lisaaTapahtumaPainike;

    @FXML
    Button peruutaLisaysPainike;

    private Tapahtuma tapahtuma;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        poistaTapahtumaPainike.setOnAction(_ -> poistaTapahtuma());
        lisaaTapahtumaPainike.setOnAction(_ -> lisaaTapahtuma());
        peruutaLisaysPainike.setOnAction(_ -> sulje());

    }

    public void setTapahtuma(Tapahtuma tapahtuma) {
        this.tapahtuma = tapahtuma;
        if(!tapahtuma.getNimi().isBlank()) {
            tapahtumanAiheKentta.setText(tapahtuma.getNimi());
        }
        if(tapahtuma.getSumma() != 0.0) {
            tapahtumanSummaKentta.setText(Double.toString(tapahtuma.getSumma()));
        }
        if(tapahtuma.getPaivamaara().isBefore(LocalDate.now()) || tapahtuma.getPaivamaara().equals(LocalDate.now())) { // Tällainen validointiratkaisu tähän alustavasti...
            paivamaaraValitsin.setValue(tapahtuma.getPaivamaara());
        }
    }

    public void setKategoriat(ObservableList<Kategoria> kategoriat) {
        tapahtumanKategoriaValitsin.setItems(kategoriat);
    }

    private void lisaaTapahtuma() {
        TarkistusVirhe kenttienValidointiVirhe = validoiKentat();
        if(kenttienValidointiVirhe != null) {
            naytaValidointiVirheIlmoitus(kenttienValidointiVirhe);
            return;
        }
        if(this.tapahtuma == null) {
            this.tapahtuma = new Tapahtuma();
        }
        this.tapahtuma.setNimi(tapahtumanAiheKentta.getText());
        this.tapahtuma.setSumma(Double.parseDouble(tapahtumanSummaKentta.getText()));
        Kategoria kategoria = tapahtumanKategoriaValitsin.getValue();
        this.tapahtuma.setKategoria(kategoria);
        this.tapahtuma.setPaivamaara(paivamaaraValitsin.getValue());
        if(tapahtuma.tarkistaVirheet() != null) {
            naytaValidointiVirheIlmoitus(tapahtuma.tarkistaVirheet());
            return;
        }

        sulje();
    }

    private void sulje() {
        Scene scene = tapahtumanAiheKentta.getScene();
        Stage ikkuna = (Stage) scene.getWindow();
        ikkuna.close();
    }

    private void poistaTapahtuma() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Varmistusdialogi");
        alert.setHeaderText("Haluatko varmasti poistaa tapahtuman?");
        alert.setContentText("Tapahtuma " + tapahtuma.getNimi() + " poistetaan.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() != ButtonType.OK){
            return;
        }
        this.tapahtuma.setNimi("");
        this.tapahtuma = null;
        this.sulje();
    }

    private void naytaValidointiVirheIlmoitus(TarkistusVirhe virhe) {
        String virheIlmoitus = "";
        switch(virhe) {
            case TarkistusVirhe.NIMI_TYHJA -> virheIlmoitus = "Nimikenttä ei saa olla tyhjä.";
            case TarkistusVirhe.NIMI_EPAVALIDI -> virheIlmoitus = "Nimi on epävalidi, tarkista nimen muotoilu ja pituus.";
            case TarkistusVirhe.SUMMA_TYHJA -> virheIlmoitus = "Summaa ei ole annettu.";
            case TarkistusVirhe.SUMMA_EPAVALIDI -> virheIlmoitus = "Annettu summa on epävalidi, tarkista että annoit numeerisen arvon";
            case TarkistusVirhe.PAIVAMAARA_TYHJA -> virheIlmoitus = "Päivämäärää ei ole asetettu, päivämäärä vaaditaan.";
            case TarkistusVirhe.KATEGORIA_TYHJA -> virheIlmoitus = "Kategoriaa ei ole valittu, tapahtumalla täytyy olla kategoria.";
        }

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validointivirhe");
        alert.setHeaderText("Virhe tapahtuman tiedoissa!");
        alert.setContentText(virheIlmoitus);
        alert.show();

    }

    /**
     * Validoi UI:n kenttien arvot, käytetään ennen kuin laitetaan mitään dataa
     * sieltä tietomallin sisään.
     *
     */
    private TarkistusVirhe validoiKentat() {
        if(tapahtumanAiheKentta.getText().isBlank()) {
            return TarkistusVirhe.NIMI_TYHJA;
        }
        if(tapahtumanAiheKentta.getText().length() > 255) {
            return TarkistusVirhe.NIMI_EPAVALIDI;
        }
        if(tapahtumanSummaKentta.getText().isBlank()) {
            return TarkistusVirhe.SUMMA_TYHJA;
        }
        if(!onNumeerinen(tapahtumanSummaKentta.getText())) {
            return TarkistusVirhe.SUMMA_EPAVALIDI;
        }
        if(paivamaaraValitsin.getValue() == null) {
            return TarkistusVirhe.PAIVAMAARA_TYHJA;
        }
        if(tapahtumanKategoriaValitsin.getValue() == null) {
            return TarkistusVirhe.KATEGORIA_TYHJA;
        }
        return null;
    }

    /**
     * Katsotaan että saadanko merkkijonosta parsittua validi double.
     * Ei kovin suorituskykyinen ratkaisu validointimielessä mutta tässä
     * kontekstissa käynee
     */
    public boolean onNumeerinen(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

}
