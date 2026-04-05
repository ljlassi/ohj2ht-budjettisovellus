package fi.jyu.ohj2.jaaslave.kulujenseuranta.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

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
        muokkaaKategorioitaLinkki.setOnAction(event -> { IO.println("Kategoroiden muokkausnäkymä avataan..."); });
        lisaaTuloPainike.setOnAction(event -> { IO.println("Tapahtuman lisäysnäkymä avataan, alustaen niin että kyseessä tulo..."); });
        lisaaMenoPainike.setOnAction(event -> { IO.println("Tapahtuman lisäysnäkymä avataan, alustaen niin että kyseessä meno..."); });


    }
}
