package fi.jyu.ohj2.jaaslave.kulujenseuranta.controller;

import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Kategoria;
import javafx.collections.ObservableList;
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
    ComboBox<String> muokattavanKategorianValitsin;

    @FXML
    TextField kategorianNimenMuokkain;

    @FXML
    Button poistaKategoriaPainike;

    @FXML
    Button tallennaMuutoksetKategoriaanPainike;

    @FXML
    Button poistuKategoriaNakymastaPainike;

    private ObservableList<Kategoria> kategoriat;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Write initialization code here
        lisaaUusiKategoriaPainike.setOnAction(event -> { lisaaUusiKategoria(); });
        poistaKategoriaPainike.setOnAction(event -> { IO.println("Poistetaan valittu kategoria..."); });
        tallennaMuutoksetKategoriaanPainike.setOnAction(event -> { IO.println("Tallennetaan muutokset kategoriaan..."); });
        poistuKategoriaNakymastaPainike.setOnAction(event -> { sulje(); });

        paivitaNakyma();

    }

    private void sulje() {
        Scene scene = uusiKategoriaKentta.getScene();
        Stage ikkuna = (Stage) scene.getWindow();
        ikkuna.close();
    }

    public void setKategoriat(ObservableList<Kategoria> kategoriat) {
        this.kategoriat = kategoriat;
    }

    private void lisaaUusiKategoria() {
        String nimi = uusiKategoriaKentta.getText();
        Kategoria uusiKategoria = new Kategoria(nimi);
        this.kategoriat.add(uusiKategoria);
        paivitaNakyma();
    }

    private void paivitaNakyma() {
        if(this.kategoriat != null) {
            this.kategoriat.forEach(k -> {
                muokattavanKategorianValitsin.getItems().add(k.getNimi());
            });
        }
    }

}
