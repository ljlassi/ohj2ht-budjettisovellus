package fi.jyu.ohj2.jaaslave.kulujenseuranta.controller;

import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Kategoria;
import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Tapahtuma;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
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

    private ObservableList<Kategoria> kategoriat;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        paivamaaraValitsin.setOnAction(event -> { IO.println("Päivämäräävalitsimen arvoa on muokattu..."); });
        poistaTapahtumaPainike.setOnAction(event -> { poistaTapahtuma(); });
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

    private void poistaTapahtuma() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Varmistusdialogi");
        alert.setHeaderText("Haluatko varmasti poistaa tapahtuman?");
        alert.setContentText("Tapahtuma " + tapahtuma.getNimi() + " poistetaan.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK){
            return;
        }
        this.tapahtuma.setNimi("");
        this.tapahtuma = null;
        this.sulje();
    }

}
