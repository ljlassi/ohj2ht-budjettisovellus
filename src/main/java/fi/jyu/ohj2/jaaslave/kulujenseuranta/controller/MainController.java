package fi.jyu.ohj2.jaaslave.kulujenseuranta.controller;

import fi.jyu.ohj2.jaaslave.kulujenseuranta.App;
import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Kategoria;
import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Seuranta;
import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Tapahtuma;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private DatePicker alkuPvmKentta;

    @FXML
    private DatePicker loppuPvmKentta;

    @FXML
    private ComboBox<String> kategoriaValitsin;

    @FXML
    private Hyperlink muokkaaKategorioitaLinkki;

    @FXML
    private Button lisaaTuloPainike;

    @FXML
    private Button lisaaMenoPainike;

    @FXML
    private TableView<Tapahtuma> tapahtumaListaus;

    @FXML
    private AnchorPane menotYhteensaNakyma;

    @FXML
    private Text menotYhteensaTeksti;

    @FXML
    private AnchorPane tulotYhteensaNakyma;

    @FXML
    private Text tulotYhteensaTeksti;

    @FXML
    private Seuranta seuranta;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        seuranta = new Seuranta();

        alustaKayttoLiittyma();

        alkuPvmKentta.setOnAction(event -> { IO.println("Alkupäivämääräkenttän arvoa muokattu..."); });
        loppuPvmKentta.setOnAction(event -> { IO.println("Loppupäivämääräkentän arvoa muokattu..."); });
        lisaaTuloPainike.setOnAction(event -> {
            avaaTapahtumaNakyma();
        });
        lisaaMenoPainike.setOnAction(event -> { avaaTapahtumaNakyma(); });
        muokkaaKategorioitaLinkki.setOnAction(event -> {
            avaaKategoriaNakyma();
        });

        this.seuranta.getTapahtumat().addListener((ListChangeListener<Tapahtuma>) change -> {
            paivitaNakyma();
        });

        this.seuranta.getKategoriat().addListener((ListChangeListener<Kategoria>) change -> {
            paivitaNakyma();
        });


    }

    /**
     * Käytetään tässä kohtaa siihen että saadaan jotain dataa sovellukseen,
     * siirrytään pois tästä kun päästään siihen vaiheeseen että tieto tallentuu
     * tiedostoon.
     */
    private void alustaKayttoLiittyma() {
        this.alkuPvmKentta.setValue(LocalDate.of(2026, 1, 1)); // TODO: Filtteröinnit kuntoon
        this.loppuPvmKentta.setValue(LocalDate.now());

        if(this.seuranta.getTapahtumat().isEmpty()) {
            Kategoria esimerkkiKategoria = new Kategoria("Yleinen");
            this.seuranta.lisaaKategoria(esimerkkiKategoria);
            Tapahtuma esimerkkiTapahtuma = new Tapahtuma();
            esimerkkiTapahtuma.setNimi("Esimerkkitapahtuma");
            esimerkkiTapahtuma.setSumma(100.0);
            esimerkkiTapahtuma.setKategoria(esimerkkiKategoria);
            esimerkkiTapahtuma.setPaivamaara(LocalDate.now());
            this.seuranta.lisaaTapahtuma(esimerkkiTapahtuma);
        }

        tapahtumaListaus.setItems(this.seuranta.getTapahtumat());
        tapahtumaListaus.setEditable(true);

        seuranta.getTapahtumat().forEach( t -> {

                    TableColumn<Tapahtuma, String> nimiSarake = new TableColumn<>("Nimi");
                    nimiSarake.setCellValueFactory(cd -> cd.getValue().nimiProperty());
                    tapahtumaListaus.getColumns().add(nimiSarake);

                    TableColumn<Tapahtuma, String> pvmSarake = new TableColumn<>("Päivämäärä");
                    pvmSarake.setCellValueFactory(cd -> cd.getValue().paivamaaraProperty().asString());
                    tapahtumaListaus.getColumns().add(pvmSarake);

                    TableColumn<Tapahtuma, String> summaSarake = new TableColumn<>("Summa");
                    summaSarake.setCellValueFactory(cd -> cd.getValue().summaProperty().asString());
                    tapahtumaListaus.getColumns().add(summaSarake);

                    TableColumn<Tapahtuma, String> kategoriaSarake = new TableColumn<>("Kategoria");
                    kategoriaSarake.setCellValueFactory(cd -> cd.getValue().getKategoria().nimiProperty());
                    tapahtumaListaus.getColumns().add(kategoriaSarake);

                    tapahtumaListaus.setRowFactory(tv -> {

                        TableRow<Tapahtuma> row = new TableRow<>();

                        row.setOnMouseClicked(event -> {

                            if (event.getButton().equals(MouseButton.PRIMARY) &&
                                    event.getClickCount() == 2 && !row.isEmpty()) {

                                Tapahtuma tapahtuma = row.getItem();
                                avaaTapahtumaNakyma(tapahtuma);
                            }
                        });

                        return row;
                    });
                }

        );

        this.paivitaNakyma();
    }

    private void avaaTapahtumaNakyma(Tapahtuma tapahtuma) {

        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("tapahtuma-nakyma.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            TapahtumaController controller = loader.getController();
            controller.setTapahtuma(tapahtuma);
            controller.setKategoriat(this.seuranta.getKategoriat());

            Stage dialogi = new Stage();
            dialogi.setScene(scene);

            dialogi.setTitle("Yksittäisen tapahtuman muokkausnäkymä");
            dialogi.setMinWidth(500);
            dialogi.setMinHeight(300);
            dialogi.initModality(Modality.APPLICATION_MODAL);

            dialogi.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private void avaaTapahtumaNakyma() {

        Tapahtuma tapahtuma = new Tapahtuma();
        this.seuranta.lisaaTapahtuma(tapahtuma);
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("tapahtuma-nakyma.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            TapahtumaController controller = loader.getController();
            controller.setTapahtuma(tapahtuma);
            controller.setKategoriat(this.seuranta.getKategoriat());

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
            controller.setKategoriat(this.seuranta.getKategoriat());

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

    public void paivitaNakyma() {

        if(this.seuranta.getKategoriat() != null) {
            ObservableList<String> lista = FXCollections.observableList(this.seuranta.haeKategorioidenNimet());
            kategoriaValitsin.setItems(lista);
        }

        if(this.seuranta.getTapahtumat() != null) {
            double tulot = this.seuranta.getTapahtumat().stream().filter(t -> t.getSumma() > 0).mapToDouble(Tapahtuma::getSumma).sum();
            double menot = this.seuranta.getTapahtumat().stream().filter(t -> t.getSumma() < 0).mapToDouble(Tapahtuma::getSumma).sum();
            this.tulotYhteensaTeksti.setText(Double.toString(tulot));
            this.menotYhteensaTeksti.setText(Double.toString(menot));
        }

        tapahtumaListaus.refresh();

    }
}
