package eu.telecomnancy.flashcard;

import com.dropbox.core.DbxException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ControleurInternet implements Initializable {
    public static Stage primaryStage;
    private ListePile listepile;
    public static LinkDropBox linkDropBox;
    public static String token;

    @FXML
    public ListView listlocaleView;

    @FXML
    private ListView listinternetView;

    @FXML
    private Button validation;
    @FXML
    private Label dialog;
    @FXML
    private DialogPane dialog1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /* initiliaze the list of pile en local */
        try{
            this.listepile = new ListePile();
            this.listepile.charger();
            dialog.setVisible(false);
            dialog1.setVisible(false);
            validation.setVisible(false);

        } catch(MalformedJsonException exception){
            dialog.setVisible(true);
            dialog1.setVisible(true);
            validation.setVisible(true);
        }
        for (Pile pile : this.listepile.getPiles()) {
            if (pile.size() > 0) {
                listlocaleView.getItems().add(pile.getName());
            }
        }
        /* initialize the list of pile in the dropbox */
        this.linkDropBox.send("pop.json");
        try {
            for (String pile : this.linkDropBox.Viewer()) {
                listinternetView.getItems().add(pile);
            }
        } catch (DbxException e) {
            e.printStackTrace();
        }
    }



    @FXML
    public void importer() throws IOException {
        if(listinternetView.getSelectionModel().getSelectedItem() != null){
            /* import the selected pile from the dropbox */
            String piletext = (String) listinternetView.getSelectionModel().getSelectedItem();
            this.linkDropBox.download(piletext);

            try {
                FileReader reader = new FileReader("src/main/resources/eu/telecomnancy/flashcard/upload/" + piletext);
                Gson gson = new Gson();
                gson.htmlSafe();
                Pile pile= gson.fromJson(reader, Pile.class);
                ListePile lp = new ListePile();
                lp.charger();
                lp.addPile(pile);
                lp.enregistrer();
            } catch (Exception a) {
                if (a instanceof FileNotFoundException){
                    a.printStackTrace();
                }
                if (a instanceof IOException) {
                    a.printStackTrace();
                }
                if(a instanceof JsonSyntaxException || a instanceof EOFException){
                    a.printStackTrace();
                }
            };
            /* reload the page */
            Parent root = FXMLLoader.load(getClass().getResource("fxml/internet.fxml"));
            primaryStage.setTitle("FlashCards");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
        }
    }

    @FXML
    public void exporter() throws IOException {
        if(listlocaleView.getSelectionModel().getSelectedItem() != null){
            /* export the selected pile to the dropbox */
            String pile = (String) listlocaleView.getSelectionModel().getSelectedItem();
            /* find the pile in the list of pile */
            for (Pile p : this.listepile.getPiles()) {
                if (p.getName().equals(pile)){
                    p.exporterPile();
                    /* upload the file in the upload folder to the dropbox */
                    this.linkDropBox.send(p.getName()+".json");
                    /* delete the file in the resources folder */
                }
            }
            /* reload the page */
            Parent root = FXMLLoader.load(getClass().getResource("fxml/internet.fxml"));
            primaryStage.setTitle("NoneLedge");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
        }
    }
    @FXML
    public void goAccueil(){
        /* load the accueil page */
        try {
            Parent root = FXMLLoader.load(getClass().getResource("fxml/accueil.fxml"));
            primaryStage.setTitle("NoneLedge");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
