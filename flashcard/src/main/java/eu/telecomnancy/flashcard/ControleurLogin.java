package eu.telecomnancy.flashcard;

import com.dropbox.core.DbxException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControleurLogin implements Initializable {
    public static Stage primaryStage;
    @FXML
    private TextField codetoken;
    @FXML
    private TextField urlaffich;
    private LinkDropBox linkDropBox;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            linkDropBox = new LinkDropBox();
            String urle = linkDropBox.getUrl();
            urlaffich.setText(urle);
        } catch (DbxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void Connect(){
        /* load internet.fxml */
        try {
            ControleurInternet.token = codetoken.getText();
            ControleurInternet.primaryStage = primaryStage;
            linkDropBox.connect(codetoken.getText());
            ControleurInternet.linkDropBox = linkDropBox;
            Parent root = FXMLLoader.load(getClass().getResource("fxml/internet.fxml"));
            primaryStage.setTitle("NoneLedge");
            primaryStage.setScene(new Scene(root, 800, 600));
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (DbxException e) {
            e.printStackTrace();
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
