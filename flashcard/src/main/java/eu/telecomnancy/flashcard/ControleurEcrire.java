package eu.telecomnancy.flashcard;

import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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

public class ControleurEcrire implements Initializable {
    public static Stage primaryStage;
    public static Pile pile;
    private int nbconnus = 0;
    public float temps;
    private Pile revisions;
    public static int nombretotal;
    private Timer timer;
    private int indexencours;
    private TimerTask task;
    public static int tempsPourRepondre;
    public static String ordre;
    public static ListePile listepiles;
    private boolean isReponse;

    @FXML
    private Text msgvictoire;
    @FXML
    private Button checkButton;
    @FXML
    private Button defaite;
    @FXML
    private Button victoire;
    @FXML
    private TextField reponsetext;
    @FXML
    private Label nompile;
    @FXML
    private Text question;
    @FXML
    private ProgressBar barretemps;
    @FXML
    private ProgressIndicator pourcentagemotconnus;
    @FXML
    private Button changement;

    @FXML
    private Pane carte;
    @FXML
    private MediaView sonjoueur;
    @FXML
    private Text descriptioncontent;
    @FXML
    private ImageView image;

    public ControleurEcrire() {
        revisions = new Pile(this.pile.getName(), this.pile.getDescription());
    }

    /**
     * Lancement de l'apprentissage en mode Ecrire en fonction des param??tres choisis
     *
     * @param url            The location used to resolve relative paths for the root object, or
     *                       {@code null} if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or {@code null} if
     *                       the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        descriptioncontent.setText(pile.getDescription());
        msgvictoire.setText("");
        isReponse = false;
        victoire.setVisible(false);
        defaite.setVisible(false);
        changement.setVisible(false);

        nompile.setText(pile.getName());
        indexencours = 0;
        temps = 0;
        if (ordre.equals("Al??atoire")) {
            pile.melanger();
        }
        if (pile != null)
            affichageQuestion();
        task = new TimerTask() {
            @Override
            public void run() {
                temps++;
                barretemps.setProgress(temps / tempsPourRepondre);
                if (temps >= tempsPourRepondre) {
                    Platform.runLater(() -> {
                        barretemps.setStyle("-fx-accent: red;");
                        check();
                    });
                    timer.cancel();
                }
            }
        };
        timer = new Timer();
        timer.schedule(task, 0, 10);
        if (pile != null) {
            affichageQuestion();
            changement.setText("Voir r??ponse");
        }
        System.out.println("nbconnus = " + (nombretotal - this.pile.size()));
        pourcentagemotconnus.setProgress((float) (nbconnus + (nombretotal - this.pile.size())) / (float) this.nombretotal);
    }


    /**
     * Cette m??thode permet de changer l'affichage lorsqu'on veut voir la r??ponse/question d'une carte et ajoure l'animation
     */
    @FXML
    public void changerAffichage() {
        MediaPlayer mediaPlayer = new MediaPlayer(new Media(getClass().getResource("Swipe.mp3").toString()));
        mediaPlayer.setVolume(0.5);
        sonjoueur.setMediaPlayer(mediaPlayer);
        mediaPlayer.play();
        System.out.println("sound played");
        RotateTransition rt = new RotateTransition(Duration.millis(500), carte);
        rt.setByAngle(90);
        changement.setDisable(true);
        rt.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (changement.getText().equals("Voir r??ponse")) {
                    question.setText(pile.getCard(indexencours).getAnswer());
                    image.setVisible(false);
                    timer.cancel();
                    barretemps.setStyle("-fx-accent: green;");
                    changement.setText("Voir question");

                } else if (changement.getText().equals("Voir question")) {
                    image.setVisible(true);
                    question.setText(pile.getCard(indexencours).getQuestionTexte());
                    changement.setText("Voir r??ponse");
                }
                RotateTransition rot = new RotateTransition(Duration.millis(500), carte);
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
     * Cette m??thode lance le timer lorsuq'une question appara??t
     */
    private void LaunchTimer() {
        temps = 0;
        barretemps.setProgress(0);
        barretemps.setStyle("-fx-accent: green;");
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                temps++;
                barretemps.setProgress(temps / tempsPourRepondre);
                if (temps >= tempsPourRepondre) {
                    Platform.runLater(() -> {
                        check();
                        barretemps.setStyle("-fx-accent: red;");
                    });
                    timer.cancel();
                }
            }
        };
        timer.schedule(task, 0, 10);
    }

    /**
     * Cette m??thode permet d'afficher la question de la carte courante de la pile
     */
    private void affichageQuestion() {

            question.setText(pile.getCard(this.indexencours).getQuestionTexte());
                try{
                    InputStream in = Files.newInputStream(Paths.get("src/main/resources/eu/telecomnancy/flashcard/images/"+pile.getCard(this.indexencours).getQuestionImage()));
                    Image imageCarte = new Image(in);
                    this.image.setImage(imageCarte);
                    this.image.setVisible(true);
                }catch (IOException e){
                    System.out.println("Erreur lors de l'affichage de l'image");
                }



    }

    private void affichageButtonQuestion() {
        changement.setText("Voir r??ponse");
    }

    /**
     * Passer ?? la carte suivante en cas de carte connue
     * @param e ??v??nement
     */
    @FXML
    public void victoryNext(ActionEvent e) {
        this.msgvictoire.setText("");
        checkButton.setVisible(true);
        victoire.setVisible(false);
        changement.setVisible(false);
        this.connu(e);
    }

    /**
     * Passer ?? la carte suivante dans le cas o?? la carte n'est pas connue
     * @param e ??v??nement
     */
    @FXML
    public void looseNext(ActionEvent e) {
        this.msgvictoire.setText("");
        checkButton.setVisible(true);
        defaite.setVisible(false);
        changement.setVisible(false);
        this.arevoir(e);
    }

    /**
     * Cette m??thode sert ex??cuter les actions lorsque la carte est connue
     * @param e ??v??nement
     */
    @FXML
    public void connu(ActionEvent e) {
        this.pile.getCard(indexencours).setConnu(true);
        listepiles.enregistrer();
        nbconnus++;
        indexencours++;
        temps = 0;

        this.LaunchTimer();
        pourcentagemotconnus.setProgress((float) (nbconnus + (nombretotal - this.pile.size())) / (float) this.nombretotal);
        if (indexencours >= this.pile.size()) {
            if (revisions.size() == 0) {
                timer.cancel();
            } else {
                verification();
            }
        } else {
            affichageQuestion();
            affichageButtonQuestion();
        }
    }

    /**
     * Cette m??thode permet de changer de page et de retourner sur la page d'accueil.
     * @param e ??v??nement
     */
    @FXML
    public void accueil(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("fxml/accueil.fxml"));
            primaryStage.setTitle("NoneLedge");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Cette m??thode permet d'ajouter des cartes non connues ?? une liste de r??visions
     * @param e ??v??nement
     */
    @FXML
    public void arevoir(ActionEvent e) {
        this.pile.getCard().setConnu(false);
        this.revisions.addCard(this.pile.getCard(indexencours));
        indexencours++;
        if (this.indexencours == this.pile.getCards().size()) {
            verification();
        } else {
            this.LaunchTimer();
            affichageQuestion();
        }

    }

    /**
     * Cette m??thode permet de savoir si on est ?? la fin d'une liste ou pas.
     * Si c'est le cas, on repart sur un processus d'apprentissage sur les cartes appartenant ?? la liste r??visions
     */
    private void verification() {
        if (this.indexencours == this.pile.getCards().size()) {
            try {
                ControleurEcrire.pile = revisions;
                ControleurEcrire.primaryStage = this.primaryStage;
                /* get controller and load carte.fxml */
                FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/ecrire.fxml"));
                ControleurEcrire controleur = loader.getController();
                Parent root = loader.load();
                primaryStage.setTitle("NoneLedge");
                primaryStage.setScene(new Scene(root, 800, 600));
                primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Cette m??thode permet de v??rifier que la r??ponse saisie est la bonne
     * @parame ??v??nement
     */
    @FXML
    public void check() {
        if (this.reponsetext.getText().equals(this.pile.getCard(indexencours).getAnswer())) {
            msgvictoire.setText("Bonne r??ponse !");
            isReponse = true;
            reponsetext.setText("");
            checkButton.setVisible(false);
            changement.setVisible(true);
            victoire.setVisible(true);
            timer.cancel();
        } else {
            msgvictoire.setText("Mauvaise r??ponse !");
            reponsetext.setText("");
            checkButton.setVisible(false);
            isReponse = false;
            timer.cancel();
            defaite.setVisible(true);
            changement.setVisible(true);
        }
    }

    /**
     * Cette m??thode permet de changer de page et de retourner ?? la page d'accuueil
     * @param e ??v??nement
     * @throws IOException
     */
    @FXML
    public void accederAccueil(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/accueil.fxml"));
        primaryStage.setTitle("NoneLedge");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        ControleurCreationPile.primaryStage = primaryStage;
    }
}
