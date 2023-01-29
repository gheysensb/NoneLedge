package eu.telecomnancy.flashcard;

import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

/* import media of javafx */
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class ControleurCarte implements Initializable {
    public static ListePile listepiles;
    public static Stage primaryStage;
    public static Pile pile;
    private  int nbconnus = 0;
    public float temps;
    private Pile revisions;
    public static int nombretotal;
    private Timer timer;
    private int indexencours ;
    private TimerTask task;
    public static int tempsPourRepondre;
    public static String ordre;
    @FXML
    private Label nompile;
    @FXML
    private ProgressBar barretemps;
    @FXML
    private ProgressIndicator pourcentagemotconnus;
    @FXML
    private Button changement;
    @FXML
    private Button connu;
    @FXML
    private Button arevoir;
    @FXML
    private Text question;
    @FXML
    private Text descriptioncontent;
    @FXML
    private Pane carte;
    @FXML
    private ImageView image;

    public ControleurCarte(){
        revisions = new Pile(this.pile.getName(), this.pile.getDescription());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        descriptioncontent.setText(pile.getDescription());
        nompile.setText(pile.getName());
        indexencours = 0;
        temps = 0;
        if (ordre.equals("Aléatoire")){
            pile.melanger();
        }

        /** Initialisation du timer */
        task = new TimerTask() {
            @Override
            public void run() {
                temps++;
                barretemps.setProgress(temps/tempsPourRepondre);
                if (temps >= tempsPourRepondre){
                    Platform.runLater(() -> {
                        barretemps.setStyle("-fx-accent: red;");
                        changerAffichage();
                    });
                    timer.cancel();
                }
            }
        };
        timer = new Timer();
        timer.schedule(task, 0, 10);

        /** Affichage de la question */
        if (pile != null){
            affichageQuestion();
            changement.setText("Voir réponse");
        }
        pourcentagemotconnus.setProgress((float)(nbconnus + (nombretotal - this.pile.size()))/(float)this.nombretotal);
    }


    /**
     * Cette méthode permet de changer l'affichage quand l'utilisateur veut voir la réposne/question.
     * Elle permet également d'ajouter une animation à ce changement d'affichage.
     */
    @FXML
    public void changerAffichage(){
        RotateTransition rt = new RotateTransition(Duration.millis(500),carte);
        rt.setByAngle(90);
        changement.setDisable(true);
        rt.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                /* Lorsuq'on veut voir la réponse */
                if (changement.getText().equals("Voir réponse")){
                    question.setText(pile.getCard(indexencours).getAnswer());
                    image.setVisible(false);
                    timer.cancel();
                    barretemps.setStyle("-fx-accent: green;");
                    changement.setText("Voir question");

                }
                /* Lorsqu'on veut voir la question */
                else if(changement.getText().equals("Voir question")){
                    image.setVisible(true);
                    question.setText(pile.getCard(indexencours).getQuestionTexte());
                    changement.setText("Voir réponse");
                }
                RotateTransition rot = new RotateTransition(Duration.millis(500),carte);
                rot.setByAngle(-90);
                rot.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        changement.setDisable(false);
                    }
                });
                rot.play();
            }
        });
        rt.play();
    }

    /**
     * Cette méthode permet de lancer le timer quand la question apparaît.
     */
    private void LaunchTimer(){
        temps = 0;
        barretemps.setProgress(0);
        barretemps.setStyle("-fx-accent: green;");
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                temps++;
                barretemps.setProgress(temps/tempsPourRepondre);
                if (temps >= tempsPourRepondre){
                    Platform.runLater(() -> {
                        barretemps.setStyle("-fx-accent: red;");
                        changerAffichage();
                    });
                    timer.cancel();
                }
            }
        };
        timer.schedule(task, 0, 10);
    }

    /**
     * Cette méthode permet d'afficher la question de la carte courant de la pile.
     */
    private void affichageQuestion(){
        try{
            question.setText(pile.getCard(this.indexencours).getQuestionTexte());
            if(pile.getCard(this.indexencours).getQuestionImage() != null && !pile.getCard(this.indexencours).getQuestionImage().equals("")) {
                InputStream in = Files.newInputStream(Paths.get("src/main/resources/eu/telecomnancy/flashcard/images/"+pile.getCard(this.indexencours).getQuestionImage()));
                Image imageCarte = new Image(in);
                this.image.setImage(imageCarte);
                this.image.setVisible(true);
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    private void affichageButtonQuestion(){
        changement.setText("Voir réponse");
    }

    /**
     * Cette méthode permet de changer de page et de se rendre sur la page d'accueil
     * @param e évènement
     * @throws IOException
     */
    @FXML
    public void accederAccueil(ActionEvent e) throws IOException{
        timer.cancel();
        Parent root = FXMLLoader.load(getClass().getResource("fxml/accueil.fxml"));
        primaryStage.setTitle("NoneLedge");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        ControleurCreationPile.primaryStage=primaryStage;
    }

    /**
     * Cette méthode sert exécuter les actions lorsque la carte est connue
     * @param e évènement
     */
    @FXML
    public void connu(ActionEvent e){
        this.pile.getCard(indexencours).setConnu(true);
        listepiles.enregistrer();
        nbconnus++;
        indexencours++;
        temps = 0;
        timer.cancel();
        this.LaunchTimer();
        pourcentagemotconnus.setProgress((float)(nbconnus + (nombretotal - this.pile.size()))/(float)this.nombretotal);
        if (indexencours >= this.pile.size()){
            if(revisions.size() == 0){
                connu.setDisable(true);
                arevoir.setDisable(true);
                changement.setDisable(true);
                timer.cancel();
                return;
            }
            else{
                verification();
            }
        }
        else{
            affichageQuestion();
            affichageButtonQuestion();
        }
    }

    /**
     * Cette méthode permet d'ajouter une carte à une liste des cartes à réviser
     * @param e évènement
     */
    @FXML
    public void arevoir(ActionEvent e){
        this.pile.getCard().setConnu(false);
        this.revisions.addCard(this.pile.getCard(indexencours));
        timer.cancel();
        indexencours++;
        if (this.indexencours == this.pile.getCards().size()){
            verification();
        }
        else{
            this.LaunchTimer();
            affichageButtonQuestion();
            affichageQuestion();
        }
    }

    /**
     * Cette méthode permet de vérifier que nous ne sommes pas à la fin de la liste en cours.
     * Si c'est le cas, on relance l'apprentissage avec la liste des révisions.
     */
    private void verification(){
        if (this.indexencours == this.pile.getCards().size()){
            try{
                ControleurCarte.pile= revisions;
                ControleurCarte.primaryStage=this.primaryStage;
                /* get controller and load carte.fxml */
                FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/carte.fxml"));
                ControleurCarte controleur = loader.getController();
                Parent root = loader.load();
                primaryStage.setTitle("Apprentissage");
                primaryStage.setScene(new Scene(root, 800, 600));
                primaryStage.show();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
