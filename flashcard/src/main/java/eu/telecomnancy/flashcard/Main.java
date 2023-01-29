package eu.telecomnancy.flashcard;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.MalformedJsonException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FileReader reader = new FileReader("src/main/resources/eu/telecomnancy/flashcard/json/piles.json");
        } catch (FileNotFoundException e) {
            try {


                Path homePath = Paths.get(System.getProperty("user.home"));
                Path newdirpath = homePath.resolve("NoneLedge");
                if (!Files.exists(newdirpath)) {
                    Files.createDirectory(newdirpath);
                }
                Path pathpiles = newdirpath.resolve("piles");
                if (!Files.exists(pathpiles)) {
                    Files.createDirectory(pathpiles);
                }
                Path pathimages = newdirpath.resolve("images");
                if (!Files.exists(pathimages)) {
                    Files.createDirectory(pathimages);
                }
                Path pathupload = newdirpath.resolve("upload");
                if (!Files.exists(pathupload)) {
                    Files.createDirectory(pathupload);
                }

                String homepath = System.getProperty("user.home");
                File pop = new File(homepath + "/NoneLedge/upload/pop.json");
                pop.createNewFile();
                File f = new File(homepath + "/NoneLedge/piles.json");
                f.createNewFile();
            }
            catch (Exception ev){
                System.out.println("Erreur");
            }
        }

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/accueil.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            stage.setTitle("NoneLedge");
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
            ControleurAccueil.primaryStage = stage;
        } catch(LoadException exception){
            //System.out.println("Le fichier piles.json contient des erreurs, merci de le corriger avant de relancer l'application.");
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}