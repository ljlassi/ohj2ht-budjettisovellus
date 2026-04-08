package fi.jyu.ohj2.jaaslave.kulujenseuranta.controller;

import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Kategoria;
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

    private ObservableList<Kategoria> kategoriat;

    private Kategoria valittuKategoria;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        lisaaUusiKategoriaPainike.setOnAction(event -> { lisaaUusiKategoria(); });
        poistaKategoriaPainike.setOnAction(event -> { poistaKategoria(); });
        tallennaMuutoksetKategoriaanPainike.setOnAction(event -> { this.tallennaMuutoksetKategoriaan(); });
        poistuKategoriaNakymastaPainike.setOnAction(event -> { sulje(); });
        muokattavanKategorianValitsin.setOnAction(event -> {
            naytaValitunKategorianTiedot();
        });

        paivitaNakyma();

    }

    private void sulje() {
        Scene scene = uusiKategoriaKentta.getScene();
        Stage ikkuna = (Stage) scene.getWindow();
        ikkuna.close();
    }

    public void setKategoriat(ObservableList<Kategoria> kategoriat) {
        this.kategoriat = kategoriat;
        muokattavanKategorianValitsin.setItems(this.kategoriat);
    }

    private void naytaValitunKategorianTiedot() {
        this.valittuKategoria = muokattavanKategorianValitsin.getValue();
        this.kategorianNimenMuokkain.setText(this.valittuKategoria.getNimi());
    }

    private void tallennaMuutoksetKategoriaan() {
        if(this.valittuKategoria == null) {
            return;
        }
        String uusiNimi = this.kategorianNimenMuokkain.getText();
        this.kategoriat.forEach(k -> {
            if(k == this.valittuKategoria) {
                k.setNimi(uusiNimi);
            }
        });
    }

    private void lisaaUusiKategoria() {
        String nimi = uusiKategoriaKentta.getText();
        Kategoria uusiKategoria = new Kategoria(nimi);
        this.kategoriat.add(uusiKategoria);
        paivitaNakyma();
    }

    private void paivitaNakyma() {
        muokattavanKategorianValitsin.setItems(this.kategoriat);
    }

    private void poistaKategoria() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Varmistusdialogi");
        alert.setHeaderText("Haluatko varmasti poistaa kategorian?");
        alert.setContentText("Kateogoria " + muokattavanKategorianValitsin.getValue().getNimi() + " poistetaan.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK){
            return;
        }
        Kategoria poistettavaKategoria = muokattavanKategorianValitsin.getValue();
        this.kategoriat.remove(poistettavaKategoria);
    }

}
