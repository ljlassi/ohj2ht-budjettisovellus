package fi.jyu.ohj2.jaaslave.kulujenseuranta.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
        lisaaUusiKategoriaPainike.setOnAction(event -> { IO.println("Lisätään uusi kategoria lisäyksen nimikentän arvon pohjalta..."); });
        poistaKategoriaPainike.setOnAction(event -> { IO.println("Poistetaan valittu kategoria..."); });
        tallennaMuutoksetKategoriaanPainike.setOnAction(event -> { IO.println("Tallennetaan muutokset kategoriaan..."); });
        poistuKategoriaNakymastaPainike.setOnAction(event -> { sulje(); });
    }

    private void sulje() {
        Scene scene = uusiKategoriaKentta.getScene();
        Stage ikkuna = (Stage) scene.getWindow();
        ikkuna.close();
    }
}
