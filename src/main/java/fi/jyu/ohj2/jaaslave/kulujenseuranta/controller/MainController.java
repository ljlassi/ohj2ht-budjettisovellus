package fi.jyu.ohj2.jaaslave.kulujenseuranta.controller;

import fi.jyu.ohj2.jaaslave.kulujenseuranta.App;
import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Kategoria;
import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Seuranta;
import fi.jyu.ohj2.jaaslave.kulujenseuranta.model.Tapahtuma;
import fi.jyu.ohj2.jaaslave.kulujenseuranta.persistence.JsonSeurantaRepository;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private DatePicker alkuPvmKentta;

    @FXML
    private DatePicker loppuPvmKentta;

    @FXML
    private ComboBox<Kategoria> kategoriaValitsin;

    @FXML
    private Hyperlink muokkaaKategorioitaLinkki;

    @FXML
    private Button lisaaTapahtumaPainike;

    @FXML
    private TableView<Tapahtuma> tapahtumaListaus;

    @FXML
    private Text menotYhteensaTeksti;

    @FXML
    private Text tulotYhteensaTeksti;

    @FXML
    private Seuranta seuranta;

    @FXML
    private ToggleButton vainPakollisetNappain;

    @FXML
    private ToggleButton suodataPainike;

    FilteredList<Tapahtuma> tapahtumatFiltteroityna;

    private boolean naytetaankoVainPakollisetTapahtumat;

    private boolean suodatetaankoTapahtumia = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Alustetaan seuranta ja tiedostojen tallennus.
        this.seuranta = new Seuranta(new JsonSeurantaRepository(Path.of("kategoriat.json"), Path.of("tapahtumat.json")));

        alustaKayttoLiittyma();

        alkuPvmKentta.setOnAction(_ -> paivitaNakyma());
        loppuPvmKentta.setOnAction(_ -> paivitaNakyma());
        lisaaTapahtumaPainike.setOnAction(_ -> avaaTapahtumaNakyma());
        muokkaaKategorioitaLinkki.setOnAction(_ -> avaaKategoriaNakyma());
        vainPakollisetNappain.setOnAction(_ -> {
            naytetaankoVainPakollisetTapahtumat = !naytetaankoVainPakollisetTapahtumat;
            paivitaNakyma();
        });
        suodataPainike.setOnAction(_ -> {
            suodatetaankoTapahtumia = !suodatetaankoTapahtumia;
            paivitaNakyma();
        });

        this.seuranta.getTapahtumat().addListener((ListChangeListener<Tapahtuma>) _ -> paivitaNakyma());

        this.seuranta.getKategoriat().addListener((ListChangeListener<Kategoria>) _ -> paivitaNakyma());


    }

    /**
     * Käytetään tässä kohtaa siihen että saadaan jotain dataa sovellukseen,
     * siirrytään pois tästä kun päästään siihen vaiheeseen että tieto tallentuu
     * tiedostoon.
     */
    private void alustaKayttoLiittyma() {
        this.alkuPvmKentta.setValue(LocalDate.of(2026, 1, 1)); // TODO: Filtteröinnit kuntoon
        this.loppuPvmKentta.setValue(LocalDate.now());

        this.seuranta.lataaKategoriat();
        this.seuranta.lataaTapahtumat();

        Kategoria esimerkkiKategoria;

        if(this.seuranta.getKategoriat().isEmpty()) {
            Kategoria esimerkkiKategoria2 = new Kategoria("Asuminen", true);
            esimerkkiKategoria = new Kategoria("Yleinen", false);
            this.seuranta.lisaaKategoria(esimerkkiKategoria);
            this.seuranta.lisaaKategoria(esimerkkiKategoria2);
        }

        if(this.seuranta.getTapahtumat().isEmpty()) {
            if(this.seuranta.getKategoriat().isEmpty()) { // Mikäli tapahtumat ja kategoriat on molemmat tyhjiä
                Kategoria esimerkkiKategoria2 = new Kategoria("Asuminen", true);
                esimerkkiKategoria = new Kategoria("Yleinen", false);
                this.seuranta.lisaaKategoria(esimerkkiKategoria);
                this.seuranta.lisaaKategoria(esimerkkiKategoria2);
            } else {
                esimerkkiKategoria = this.seuranta.getKategoriat().getFirst();
            }
            Tapahtuma esimerkkiTapahtuma = new Tapahtuma();
            esimerkkiTapahtuma.setNimi("Esimerkkitapahtuma");
            esimerkkiTapahtuma.setSumma(100.0);
            esimerkkiTapahtuma.setKategoria(esimerkkiKategoria);
            esimerkkiTapahtuma.setPaivamaara(LocalDate.now());
            this.seuranta.lisaaTapahtuma(esimerkkiTapahtuma);
        }

        tapahtumaListaus.setItems(this.seuranta.getTapahtumat());
        tapahtumaListaus.setEditable(true);

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
        kategoriaSarake.setCellValueFactory(cd -> (cd.getValue().getKategoria() != null ? cd.getValue().getKategoria().nimiProperty() : null));
        tapahtumaListaus.getColumns().add(kategoriaSarake);

        TableColumn<Tapahtuma, Boolean> pakollinenMenoSarake = new TableColumn<>("Pakollinen menoerä");
        pakollinenMenoSarake.setCellValueFactory(cd -> (cd.getValue().getKategoria() != null ? cd.getValue().getKategoria().pakollinenProperty() : null));
        pakollinenMenoSarake.setCellFactory(CheckBoxTableCell.forTableColumn(pakollinenMenoSarake));
        pakollinenMenoSarake.setEditable(false);
        tapahtumaListaus.getColumns().add(pakollinenMenoSarake);

        tapahtumaListaus.setRowFactory(_ -> {

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
            kategoriaValitsin.setItems(this.seuranta.getKategoriat());
        }

        if(this.seuranta.getTapahtumat() != null) {
            double tulot = this.seuranta.getTapahtumat().stream().filter(t -> t.getSumma() > 0).mapToDouble(Tapahtuma::getSumma).sum();
            double menot = this.seuranta.getTapahtumat().stream().filter(t -> t.getSumma() < 0).mapToDouble(Tapahtuma::getSumma).sum();
            this.tulotYhteensaTeksti.setText(Double.toString(tulot));
            this.menotYhteensaTeksti.setText(Double.toString(menot));
        }

        if(this.suodatetaankoTapahtumia) {
            // Suodatetaan tapahtumat valittuna olevan kategorian sekä valittujen päivämäärien mukaan.
            this.tapahtumatFiltteroityna = new FilteredList<>(this.seuranta.getTapahtumat(), t ->
                    (this.kategoriaValitsin.getValue() == null || t.getKategoria().getNimi().equals(this.kategoriaValitsin.getValue().getNimi())) &&
                            (t.getPaivamaara().isAfter(this.alkuPvmKentta.getValue()) || t.getPaivamaara().equals(this.alkuPvmKentta.getValue())) &&
                            (t.getPaivamaara().isBefore(this.loppuPvmKentta.getValue()) || t.getPaivamaara().equals(this.loppuPvmKentta.getValue()))

                    );
            this.tapahtumaListaus.setItems(this.tapahtumatFiltteroityna);
        } else if(naytetaankoVainPakollisetTapahtumat) {
            this.tapahtumatFiltteroityna = new FilteredList<>(this.seuranta.getTapahtumat(), t -> t.getKategoria().getPakollinen());
            tapahtumaListaus.setItems(this.tapahtumatFiltteroityna);
        } else {
            this.tapahtumaListaus.setItems(this.seuranta.getTapahtumat());
        }

        tapahtumaListaus.refresh();

    }
}
