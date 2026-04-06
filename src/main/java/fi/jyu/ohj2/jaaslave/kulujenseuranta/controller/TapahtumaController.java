package fi.jyu.ohj2.jaaslave.kulujenseuranta.controller;

import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Kategoria;
import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Tapahtuma;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
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

    private ObservableList<Kategoria> kategoriat;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        paivamaaraValitsin.setOnAction(event -> { IO.println("Päivämäräävalitsimen arvoa on muokattu..."); });
        poistaTapahtumaPainike.setOnAction(event -> { IO.println("Poistetaan tämä muokattavana ollut tapahtuma kokonaan..."); });
        lisaaTapahtumaPainike.setOnAction(event -> { lisaaTapahtuma(); });
        peruutaLisaysPainike.setOnAction(event -> { sulje(); });

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
        this.kategoriat = kategoriat;
        tapahtumanKategoriaValitsin.setItems(this.kategoriat);
    }

    private void lisaaTapahtuma() {
        if(this.tapahtuma == null) {
            this.tapahtuma = new Tapahtuma();
        }
        this.tapahtuma.setNimi(tapahtumanAiheKentta.getText());
        this.tapahtuma.setSumma(Double.parseDouble(tapahtumanSummaKentta.getText()));
        Kategoria kategoria = tapahtumanKategoriaValitsin.getValue();
        this.tapahtuma.setKategoria(kategoria);
        this.tapahtuma.setPaivamaara(paivamaaraValitsin.getValue());

    }

    private void sulje() {
        Scene scene = tapahtumanAiheKentta.getScene();
        Stage ikkuna = (Stage) scene.getWindow();
        ikkuna.close();
    }

}
