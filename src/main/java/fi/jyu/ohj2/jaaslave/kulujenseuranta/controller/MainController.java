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

    FilteredList<Tapahtuma> tapahtumatFiltteroityna;
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
    private boolean naytetaankoVainPakollisetTapahtumat;

    private boolean suodatetaankoTapahtumia = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Alustetaan seuranta ja tiedostojen tallennus.
        seuranta = new Seuranta(new JsonSeurantaRepository(Path.of("kategoriat.json"), Path.of("tapahtumat.json")));

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

        seuranta.getTapahtumat().addListener((ListChangeListener<Tapahtuma>) _ -> paivitaNakyma());

        seuranta.getKategoriat().addListener((ListChangeListener<Kategoria>) _ -> paivitaNakyma());


    }

    /**
     * Alustaa käyttöliittymän kun sovellus ensi kerran käynistetään.
     */
    private void alustaKayttoLiittyma() {
        alkuPvmKentta.setValue(LocalDate.of(2026, 1, 1));
        loppuPvmKentta.setValue(LocalDate.now());

        seuranta.lataaDataTiedostosta();

        Kategoria esimerkkiKategoria;

        if (seuranta.getKategoriat().isEmpty()) {
            Kategoria esimerkkiKategoria2 = new Kategoria("Asuminen", true);
            esimerkkiKategoria = new Kategoria("Yleinen", false);
            seuranta.lisaaKategoria(esimerkkiKategoria);
            seuranta.lisaaKategoria(esimerkkiKategoria2);
        }

        if (seuranta.getTapahtumat().isEmpty()) {
            if (seuranta.getKategoriat().isEmpty()) { // Mikäli tapahtumat ja kategoriat on molemmat tyhjiä
                Kategoria esimerkkiKategoria2 = new Kategoria("Asuminen", true);
                esimerkkiKategoria = new Kategoria("Yleinen", false);
                seuranta.lisaaKategoria(esimerkkiKategoria);
                seuranta.lisaaKategoria(esimerkkiKategoria2);
            } else {
                esimerkkiKategoria = seuranta.getKategoriat().getFirst();
            }
            Tapahtuma esimerkkiTapahtuma = new Tapahtuma();
            esimerkkiTapahtuma.setNimi("Esimerkkitapahtuma");
            esimerkkiTapahtuma.setSumma(100.0);
            esimerkkiTapahtuma.setKategoria(esimerkkiKategoria);
            esimerkkiTapahtuma.setPaivamaara(LocalDate.now());
            seuranta.lisaaTapahtuma(esimerkkiTapahtuma);
        }

        tapahtumaListaus.setItems(seuranta.getTapahtumat());
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

        paivitaNakyma();
    }

    private void avaaTapahtumaNakyma(Tapahtuma tapahtuma) {

        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("tapahtuma-nakyma.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            TapahtumaController controller = loader.getController();
            controller.setTapahtuma(tapahtuma);
            controller.setKategoriat(seuranta.getKategoriat());
            controller.setTapahtumat(seuranta.getTapahtumat());

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
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("tapahtuma-nakyma.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            TapahtumaController controller = loader.getController();
            controller.setKategoriat(seuranta.getKategoriat());
            controller.setTapahtumat(seuranta.getTapahtumat());

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
            controller.setKategoriat(seuranta.getKategoriat());

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

    private void paivitaNakyma() {

        if (seuranta.getKategoriat() != null) {
            kategoriaValitsin.setItems(seuranta.getKategoriat());
        }

        if (suodatetaankoTapahtumia) {
            // Suodatetaan tapahtumat valittuna olevan kategorian sekä valittujen päivämäärien mukaan.
            tapahtumatFiltteroityna = new FilteredList<>(seuranta.getTapahtumat(), t ->
                    t.getKategoria() != null &&
                    (kategoriaValitsin.getValue() == null || t.getKategoria().getNimi().equals(kategoriaValitsin.getValue().getNimi())) &&
                            (t.getPaivamaara().isAfter(alkuPvmKentta.getValue()) || t.getPaivamaara().equals(alkuPvmKentta.getValue())) &&
                            (t.getPaivamaara().isBefore(loppuPvmKentta.getValue()) || t.getPaivamaara().equals(loppuPvmKentta.getValue()))

            );
            tapahtumaListaus.setItems(tapahtumatFiltteroityna);
        } else if (naytetaankoVainPakollisetTapahtumat) {
            tapahtumatFiltteroityna = new FilteredList<>(seuranta.getTapahtumat(), t -> (t.getKategoria() != null && t.getKategoria().getPakollinen()));
            tapahtumaListaus.setItems(tapahtumatFiltteroityna);
        } else {
            tapahtumaListaus.setItems(seuranta.getTapahtumat());
        }
        paivitaTapahtumatYhteensa();
        tapahtumaListaus.refresh();

    }

    private void paivitaTapahtumatYhteensa() {
        if (seuranta.getTapahtumat() != null) {
            double tulot;
            double menot;
            if (!suodatetaankoTapahtumia && !naytetaankoVainPakollisetTapahtumat) {
                tulot = seuranta.getTapahtumat().stream().filter(t -> t.getSumma() > 0).mapToDouble(Tapahtuma::getSumma).sum();
                menot = seuranta.getTapahtumat().stream().filter(t -> t.getSumma() < 0).mapToDouble(Tapahtuma::getSumma).sum();
            } else {
                tulot = tapahtumatFiltteroityna.stream().filter(t -> t.getSumma() > 0).mapToDouble(Tapahtuma::getSumma).sum();
                menot = tapahtumatFiltteroityna.stream().filter(t -> t.getSumma() < 0).mapToDouble(Tapahtuma::getSumma).sum();
            }
            tulotYhteensaTeksti.setText(Double.toString(tulot));
            menotYhteensaTeksti.setText(Double.toString(menot));

        }
    }
}
