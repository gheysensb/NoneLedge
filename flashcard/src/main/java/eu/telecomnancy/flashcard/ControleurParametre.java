package eu.telecomnancy.flashcard;

import com.google.gson.stream.MalformedJsonException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControleurParametre implements Initializable {

    public static Stage primaryStage;
    private ListePile listepile;
    @FXML
    ListView vuepiles;
    @FXML
    TextField recherche;
    @FXML
    ToggleGroup mode;
    @FXML
    ToggleGroup temps;
    @FXML
    ToggleGroup ordre;
    @FXML
    Button boutonCommencer;
    @FXML
    Button precedent;
    @FXML
    private Button validation;
    @FXML
    private Label dialog;
    @FXML
    private DialogPane dialog1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            this.listepile = new ListePile();
            this.listepile.charger();
            dialog1.setVisible(false);
            dialog.setVisible(false);
            validation.setVisible(false);
        }
        catch(MalformedJsonException e){
            dialog1.setVisible(true);
            dialog.setVisible(true);
            validation.setVisible(true);
        }

        if (this.listepile.getPiles()!=null){
            for (Pile pile : this.listepile.getPiles()) {
                if (pile.size()>0){
                    vuepiles.getItems().add(pile.getName());
                }
            }
        }
        ObservableList<String> list = FXCollections.observableArrayList();
        list.addAll(vuepiles.getItems());
        recherche.textProperty().addListener((observable, oldValue, newValue) -> {
            FilteredList<String> filteredItems = new FilteredList<>(list, s -> true);
            filteredItems.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (item.toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
            vuepiles.setItems(filteredItems);
        });
    }

    @FXML
    public void commencer(ActionEvent e) throws IOException {
        String pileselectionne = (String) vuepiles.getSelectionModel().getSelectedItem();
        if (pileselectionne != null){
            for (Pile pile : this.listepile.getPiles()) {
                if (pile.getName().equals(pileselectionne)){
                    ControleurCarte.pile = pile;
                    ControleurCarte.nombretotal = pile.size();
                    ControleurEcrire.pile = pile;
                    ControleurEcrire.nombretotal = pile.size();
                }
            }
            RadioButton mode = (RadioButton) this.mode.getSelectedToggle();
            RadioButton ordre = (RadioButton) this.ordre.getSelectedToggle();
            RadioButton temps = (RadioButton) this.temps.getSelectedToggle();
            String modeString = mode.getText();
            String ordreString = ordre.getText();
            String tempsString = temps.getText();
            int tempsInt = getIntTemps(tempsString);
            if (modeString.equals("Lire")){
                ControleurCarte.tempsPourRepondre = tempsInt;
                ControleurCarte.ordre = ordreString;
                ControleurCarte.listepiles = this.listepile;
                Parent root = FXMLLoader.load(getClass().getResource("fxml/carte.fxml"));
                primaryStage.setTitle("NoneLedge");
                primaryStage.setScene(new Scene(root, 800, 600));
                ControleurCarte.primaryStage = primaryStage;
                primaryStage.show();

            }
            if (modeString.equals("Ecrire")){
                ControleurEcrire.tempsPourRepondre = tempsInt;
                ControleurEcrire.ordre = ordreString;
                ControleurEcrire.listepiles = this.listepile;
                Parent root = FXMLLoader.load(getClass().getResource("fxml/ecrire.fxml"));
                primaryStage.setTitle("NoneLedge");
                primaryStage.setScene(new Scene(root, 800, 600));
                primaryStage.show();
                ControleurEcrire.primaryStage = primaryStage;

            }
        }
    }

    @FXML
    public void ACCEUIL(ActionEvent e){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("fxml/accueil.fxml"));
            primaryStage.setTitle("NoneLedge");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public int getIntTemps(String temps){
        if (temps.equals("10 sec"))
            return 10*100;
        if (temps.equals("30 sec"))
            return 30*100;
        if (temps.equals("1 min"))
            return 60*100;
        if (temps.equals("2 min"))
            return 120*100;
        if (temps.equals("5 min"))
            return 300*100;
        return 0;
    }



}