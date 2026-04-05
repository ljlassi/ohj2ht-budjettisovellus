package fi.jyu.ohj2.jaaslave.kulujenseuranta.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class KategoriaController implements Initializable {

    @FXML
    TextField uusiKategoriaKentta;

    @FXML
    Button lisaaUusiKategoriaPainike;

    @FXML
    ComboBox muokattavanKategorianValitsin;

    @FXML
    TextField kategorianNimenMuokkain;

    @FXML
    Button poistaKategoriaPainike;

    @FXML
    Button tallennaMuutoksetKategoriaanPainike;

    @FXML
    Button poistuKategoriaNakymastaPainike;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Write initialization code here
        lisaaUusiKategoriaPainike.setOnAction(event -> {});
        poistaKategoriaPainike.setOnAction(event -> {});
        tallennaMuutoksetKategoriaanPainike.setOnAction(event -> {});
        poistuKategoriaNakymastaPainike.setOnAction(event -> {});
    }
}
