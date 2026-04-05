package fi.jyu.ohj2.jaaslave.kulujenseuranta.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class TapahtumaController implements Initializable {

    @FXML
    TextField tapahtumanAiheKentta;

    @FXML
    TextField tapahtumanSummaKentta;

    @FXML
    ComboBox tapahtumanKategoriaValitsin;

    @FXML
    DatePicker paivamaaraValitsin;

    @FXML
    Button poistaTapahtumaPainike;

    @FXML
    Button lisaaTapahtumaPainike;

    @FXML
    Button peruutaLisaysPainike;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Write initialization code here
        paivamaaraValitsin.setOnAction(event -> {});
        poistaTapahtumaPainike.setOnAction(event -> {});
        lisaaTapahtumaPainike.setOnAction(event -> {});
        peruutaLisaysPainike.setOnAction(event -> {});
    }

}
