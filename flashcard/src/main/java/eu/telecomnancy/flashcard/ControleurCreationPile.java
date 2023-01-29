package eu.telecomnancy.flashcard;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControleurCreationPile implements Initializable {
    public static Stage primaryStage;
    @FXML
    Button creer;
    @FXML
    TextField nompile;
    @FXML
    TextArea descriptionpile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    /**
     * Cette méthode permet de créer une pile, de l'enregistrer dans la liste des piles et dans le fichier JSON.
     * Ensuite, l'utilisateur est redirigé sur la page de création des cartes.
     * @param e évènement
     * @throws IOException
     */
    @FXML
    public void creerPile(ActionEvent e) throws IOException {
        if (nompile.getText() != null && descriptionpile.getText()!= null){
            ListePile piles = new ListePile();
            piles.charger();
            Pile p = new Pile(nompile.getText(), descriptionpile.getText());
            piles.addPile(p);
            piles.enregistrer();

            ControleurCreationCarte.primaryStage=primaryStage;
            ControleurCreationCarte.pile = p;
            ControleurCreationCarte.listepiles = piles;
            Parent root = FXMLLoader.load(getClass().getResource("fxml/creationCarte.fxml"));
            primaryStage.setTitle("NoneLedge");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
        }
    }

    /**
     * Cette méthode permet de changer de page et de retourner sur la page d'accueil
     * @param e évènement
     * @throws IOException
     */
    @FXML
    public void accederAccueil(ActionEvent e) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("fxml/accueil.fxml"));
        primaryStage.setTitle("NoneLedge");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        ControleurCreationPile.primaryStage=primaryStage;
    }
}
