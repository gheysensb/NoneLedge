package eu.telecomnancy.flashcard;

import com.google.gson.stream.MalformedJsonException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControleurStatistiques implements Initializable {

    public static Stage primaryStage;
    private ListePile listepile;
    @FXML
    private Button precedent;
    @FXML
    private Button global;
    @FXML
    private MenuButton listeIndiv;
    @FXML
    private StackedBarChart<String,Integer> graphGlobal;
    @FXML
    private PieChart graphIndiv;

    @FXML
    private Button validation;
    @FXML
    private Label dialog;
    @FXML
    private DialogPane dialog1;

    public ControleurStatistiques() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphGlobal.setVisible(false);
        graphIndiv.setVisible(false);

        try{
            listepile = new ListePile();
            listepile.charger();
            dialog.setVisible(false);
            dialog1.setVisible(false);
            validation.setVisible(false);
        } catch(MalformedJsonException exception){
            dialog.setVisible(true);
            dialog1.setVisible(true);
            validation.setVisible(true);
        }

        for (Pile pile : listepile.getPiles()) {
            if (pile.size() != 0) {
                MenuItem item = new MenuItem(pile.getName());
                item.setOnAction(e -> afficherStatIndiv(pile));
                listeIndiv.getItems().add(item);
            }
        }
    }


    /**
     * Cette méthode permet d'afficher le diagramme sur les statistiques globales des piles.
     * C'est un diagramme en barres superposées.
     * @param e évènement
     */
    @FXML
    public void afficherStatGlobal(ActionEvent e){
        graphGlobal.getData().clear();
        try{
            listepile = new ListePile();
            listepile.charger();
            dialog.setVisible(false);
            dialog1.setVisible(false);
            validation.setVisible(false);
        } catch(MalformedJsonException exception){
            dialog.setVisible(true);
            dialog1.setVisible(true);
            validation.setVisible(true);
        }

        graphGlobal.setVisible(true);
        graphIndiv.setVisible(false);
        global.setDisable(true);
        listeIndiv.setText("Statistiques individuelles");
        XYChart.Series bonneRep = new XYChart.Series();
        bonneRep.setName("Réponse connue");

        /* Ajout des bonnes réponses au diagramme */
        for (Pile pile : listepile.getPiles()) {
            bonneRep.getData().add(new XYChart.Data(pile.getName(), pile.compterConnu()));
        }
        XYChart.Series mauvaiseRep = new XYChart.Series();

        /* Ajout des mauvaises réponses au diagramme */
        mauvaiseRep.setName("Réponse inconnue");
        for (Pile pile : listepile.getPiles()) {
            mauvaiseRep.getData().add(new XYChart.Data(pile.getName(), pile.compterInconnu()));
        }
        if (mauvaiseRep.getNode() != null) mauvaiseRep.getNode().setStyle("-fx-bar-fill : red");
        if (bonneRep.getNode() != null) bonneRep.getNode().setStyle("-fx-bar-fill : green");

        graphGlobal.setCategoryGap(30);
        graphGlobal.getData().addAll(mauvaiseRep,bonneRep);
    }


    /**
     * Cette méthode permet d'afficher le diagramme des statistiques d'une pile passée en paramètres.
     * C'est un diagramme circulaire.
     * @param pile pile dont on doit afficher les statistiques
     */
    public void afficherStatIndiv(Pile pile){
        if (pile.size()==0) System.out.println("pas de cartes");
        else{
            try{
                listepile = new ListePile();
                listepile.charger();
                dialog.setVisible(false);
                dialog1.setVisible(false);
                validation.setVisible(false);
            } catch(MalformedJsonException exception){
                dialog.setVisible(true);
                dialog1.setVisible(true);
                validation.setVisible(true);
            }

            graphIndiv.setVisible(true);
            graphGlobal.setVisible(false);
            listeIndiv.setText(pile.getName());
            /* Ajout des différentes parties */
            ObservableList<PieChart.Data> pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data("Cartes connues ("+pile.compterConnu()+")",(double)pile.compterConnu()/pile.size()),
                            new PieChart.Data("Cartes à revoir ("+pile.compterInconnu()+")",(double)pile.compterInconnu()/pile.size())
                    );

            graphIndiv.setData(pieChartData);
            graphIndiv.setLabelsVisible(false);
        }
        global.setDisable(false);
    }

    /**
     * Cette méthode permet de changer de page et de retourner à la page d'accueil
     * @param e évènement
     * @throws IOException
     */
    @FXML
    public void accederAccueil(ActionEvent e) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("fxml/accueil.fxml"));
        primaryStage.setTitle("NoneLedge");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        ControleurAccueil.primaryStage=primaryStage;
    }
}
