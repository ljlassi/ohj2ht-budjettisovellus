package fi.jyu.ohj2.jaaslave.kulujenseuranta.controller;

import fi.jyu.ohj2.jaaslave.kulujenseuranta.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private DatePicker alkuPvmKentta;

    @FXML
    private DatePicker loppuPvmKentta;

    @FXML
    private ComboBox kategoriaValitsin;

    @FXML
    private Hyperlink muokkaaKategorioitaLinkki;

    @FXML
    private Button lisaaTuloPainike;

    @FXML
    private Button lisaaMenoPainike;

    @FXML
    private TableView tapahtumaListaus;

    private AnchorPane menotYhteensaNakyma;

    private AnchorPane tulotYhteensaNakyma;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Write initialization code here

        alkuPvmKentta.setOnAction(event -> { IO.println("Alkupäivämääräkenttän arvoa muokattu..."); });
        loppuPvmKentta.setOnAction(event -> { IO.println("Loppupäivämääräkentän arvoa muokattu..."); });
        muokkaaKategorioitaLinkki.setOnAction(event -> { avaaKategoriaNakyma(); });
        lisaaTuloPainike.setOnAction(event -> { avaaTapahtumaNakyma(); });
        lisaaMenoPainike.setOnAction(event -> { avaaTapahtumaNakyma(); });


    }

    private void avaaTapahtumaNakyma() {

        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("tapahtuma-nakyma.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            TapahtumaController controller = loader.getController();

            Stage dialogi = new Stage();
            dialogi.setScene(scene);

            dialogi.setTitle("Yksittäisen tapahtuman näkymä");
            dialogi.setMinWidth(500);
            dialogi.setMinHeight(300);
            dialogi.initModality(Modality.APPLICATION_MODAL);

            dialogi.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private void avaaKategoriaNakyma() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("kategoria-nakyma.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            KategoriaController controller = loader.getController();

            Stage dialogi = new Stage();
            dialogi.setScene(scene);

            dialogi.setTitle("Kategorioiden muokkausnäkymä");
            dialogi.setMinWidth(400);
            dialogi.setMinHeight(300);
            dialogi.initModality(Modality.APPLICATION_MODAL);

            dialogi.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
