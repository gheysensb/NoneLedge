package eu.telecomnancy.flashcard;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ControleurCreationCarte implements Initializable {
    public static Stage primaryStage;
    public static Pile pile;
    public static ListePile listepiles;
    private ArrayList<TextField> element;
    @FXML
    private Text nomPile;
    @FXML
    private Text contentdescription;
    @FXML
    private TextField questionImage;
    @FXML
    private TextField questionTexte;
    @FXML
    private TextField reponse;
    @FXML
    private ListView listCard;
    @FXML
    private Button validation;
    @FXML
    private DialogPane dialog1;
    @FXML
    private Label dialog2;
    @FXML
    private Button pasvalidation;
    private CreationView creationView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /* afficher les cartes de la pile avec l'id de la carte , la question et la réponse */
        contentdescription.setText(pile.getDescription());
        validation.setVisible(false);
        dialog1.setVisible(false);
        dialog2.setVisible(false);
        pasvalidation.setVisible(false);

        this.creationView = new CreationView(listCard,questionTexte,questionImage,reponse,pile,nomPile,this);
        this.creationView.update();
    }

    /**
     * Cette méthode permet d'ajouter une carte dans la pile en cours.
     */
    @FXML
    public void ajouterCarte() {
        refresh();
        Carte ajout;
        if (!questionImage.getText().equals("")){
            String image ="src/main/resources/eu/telecomnancy/flashcard/images/"+questionImage.getText();
            ajout = new Carte(questionTexte.getText(),image,reponse.getText());
        }
        else ajout = new Carte(questionTexte.getText(),null,reponse.getText());
        pile.addCard(ajout);

        creationView.update();
    }

    /**
     * Cette méthode permet de rafraîchir la liste lorsqu'on ajoute ou modifie une carte
     */
    @FXML
    public void refresh(){
        for(int i=0;i<pile.getCardCount();i++){
            pile.getCard(i).setQuestion(element.get(i*3).getText());
            pile.getCard(i).setImage(element.get(i*3+1).getText());
            pile.getCard(i).setAnswer(element.get(i*3+2).getText());
        }
        listepiles.enregistrer();
    }

    /**
     * Cette méthode permet de supprimer une pile de cartes
     * @throws IOException
     */
    @FXML
    public void deletePile() throws IOException {
        listepiles.removePile(pile);
        listepiles.enregistrer();
        Parent root = FXMLLoader.load(getClass().getResource("fxml/accueil.fxml"));
        primaryStage.setTitle("NoneLedge");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        ControleurAccueil.primaryStage = primaryStage;
    }

    /**
     * Cette méthode permet de fermer le pop-up de confirmation de la suppression
     * @param e évènement
     */
    @FXML
    public void deletePopUp(ActionEvent e){
        pasvalidation.setVisible(false);
        validation.setVisible(false);
        dialog1.setVisible(false);
        dialog2.setVisible(false);
    }

    /**
     * Cette méthode s'exécute lorsque l'utilisateur veut quitter la page.
     * Elle sauvegarde la page en cours et amène l'utilisateur sur la page d'accueil.
     * @throws IOException
     */
    @FXML
    public void terminer() throws IOException {
        /* SAUVEGARDER LA PILE JSON */
        refresh();
        /* RETOUR A LA PAGE D'ACCUEIL */
        Parent root = FXMLLoader.load(getClass().getResource("fxml/accueil.fxml"));
        primaryStage.setTitle("NoneLedge");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        ControleurCreationPile.primaryStage=primaryStage;
    }

    /**
     * Cette méthode permet d'afficher le pop-up de confirmation de la suppression d'une pile.
     * @param e évènement
     */
    @FXML
    public void confirmationSuppression(ActionEvent e){
        pasvalidation.setVisible(true);
        validation.setVisible(true);
        dialog1.setVisible(true);
        dialog2.setVisible(true);
    }

    /**
     * Cette méthode permet de changer de page et de retourner sur la page d'accueil
     * @param e évènementg
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

    @FXML
    public void chargerImage(ActionEvent e) throws IOException{
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir votre image");
        File selectedFile = fileChooser.showOpenDialog(null);
        String imagePath = "src/main/resources/eu/telecomnancy/flashcard/images/";
        String nomImage = imagePath +selectedFile.getName();
        File target = new File(nomImage);
     
        try {
            Files.copy(selectedFile.toPath(), target.toPath());
            String image = nomImage.substring(nomImage.lastIndexOf("/")+1);
            questionImage.setText(image);
            questionImage.setEditable(false);

        } catch (IOException a) {
            a.printStackTrace();
        }
    }

    public void setElement(ArrayList<TextField> element) {
        this.element = element;
    }
}
