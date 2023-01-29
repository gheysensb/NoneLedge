package eu.telecomnancy.flashcard;

import com.google.gson.Gson;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ControleurAccueil implements Initializable {
    public static Stage primaryStage;

    @FXML
    ListView vuepiles;

    @FXML
    Label labelrecherche;

    @FXML
    TextField recherche;
    @FXML
    private Button apprentissage;
    @FXML
    private Button editer;
    @FXML
    private Button creer;

    @FXML
    private Button validation;

    @FXML
    private Button importer;
    @FXML
    private Button exporter;
    @FXML
    private Button valide;

    @FXML
    private DialogPane dialog1;
    @FXML
    private Label dialog;
    @FXML
    private Button internetbutton;
    private ListePile listepile;
    private ListePile listepileapprentissage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /** Initialisation de la vue (enlever les boutons non nécessaires) */
        internetbutton.setVisible(false);
        editer.setVisible(false);
        creer.setVisible(false);
        importer.setVisible(false);
        exporter.setVisible(false);
        valide.setVisible(false);
        dialog1.setVisible(false);
        dialog.setVisible(false);
        validation.setVisible(false);
        vuepiles.setVisible(false);
        recherche.setVisible(false);
        labelrecherche.setVisible(false);


        /** Chargement des données des listes */
        try{
            listepile = new ListePile();
            listepile.charger();
            listepileapprentissage = new ListePile();
            listepileapprentissage.charger();

            /** Mise à jour des menus déroulants */
            //actionEdition();
            this.listepile = new ListePile();
            this.listepile.charger();
        }catch (MalformedJsonException exception){
            dialog1.setVisible(true);
            validation.setVisible(true);
            dialog.setVisible(true);
            dialog.setText("Le fichier piles.json est mal formé.");
        }


        if (this.listepile.getPiles()!=null){
            for (Pile pile : this.listepile.getPiles()) {
                vuepiles.getItems().add(pile.getName());
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

    /**
     * Cette méthode permet de remettre les boutons dans un état normal (juste le bouton Editer et Apprendre utilisable)
     * @param e évènement
     * @throws IOException
     */
    @FXML
    public void clicCreation(ActionEvent e){
        internetbutton.setVisible(true);
        internetbutton.setDisable(false);
        editer.setVisible(true);
        creer.setVisible(true);
        apprentissage.setVisible(true);
        apprentissage.setDisable(false);
        importer.setVisible(true);
        exporter.setVisible(true);
        editer.setDisable(true);
        creer.setDisable(false);
        importer.setDisable(false);
        exporter.setDisable(true);
        valide.setVisible(false);
        dialog1.setVisible(false);
        dialog.setVisible(false);
        validation.setVisible(false);
        recherche.setVisible(true);
        labelrecherche.setVisible(true);
        vuepiles.setVisible(true);


    }

    @FXML
    public void clicPileCreation(MouseEvent e) {
        editer.setDisable(false);
        creer.setDisable(true);
        importer.setDisable(true);
        exporter.setDisable(false);
    }

    @FXML
    public void Editer(ActionEvent e) throws IOException {
        String pileselectionne = (String) vuepiles.getSelectionModel().getSelectedItem();
        if (pileselectionne != null) {
            for (Pile pile : this.listepile.getPiles()) {
                if (pile.getName().equals(pileselectionne)) {
                    ControleurCreationCarte.pile = pile;
                    Parent root = FXMLLoader.load(getClass().getResource("fxml/creationCarte.fxml"));
                    primaryStage.setTitle("NoneLedge");
                    primaryStage.setScene(new Scene(root, 800, 600));
                    primaryStage.show();
                    ControleurCreationCarte.primaryStage = primaryStage;
                    ControleurCreationCarte.listepiles = this.listepile;

                }
            }
        }
    }

    @FXML
    public void Exporter(ActionEvent e) {
        String pileselectionne = (String) vuepiles.getSelectionModel().getSelectedItem();
        if (pileselectionne != null) {
            for (Pile pile : this.listepile.getPiles()) {
                if (pile.getName().equals(pileselectionne)) {
                    try {
                        exporterPile(pile);
                        dialog.setText("Exportation réalisée avec succès.");
                        dialog.setVisible(true);
                        validation.setVisible(true);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
    }

    /**
     * Cette méthode fait apparaître le champ et le bouton liés à l'importation de fichier en cas de clic sur "Importer"
     * @param e évènement
     * @throws IOException
     */
    @FXML
    public void clicImporter(ActionEvent e) throws IOException {
        exporter.setDisable(true);
        internetbutton.setDisable(true);
        editer.setDisable(true);
        creer.setDisable(true);
        valide.setVisible(true);
        labelrecherche.setVisible(false);
        recherche.setVisible(false);
        vuepiles.setVisible(false);
    }

    /**
     * Cette méthode permet de changer de page et d'aller sur la page de création de la pile
     * @param e évènement
     * @throws IOException
     */
    @FXML
    public void accederCreationPile(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/creationPile.fxml"));
        primaryStage.setTitle("Création pile");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        ControleurCreationPile.primaryStage=primaryStage;
    }

    /**
     * @throws IOException
     */
    @FXML
    public void accederParametre(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/paramApprentissage.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Apprentissage");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        ControleurParametre.primaryStage=primaryStage;
    }

    /**
     * Cette méthode permet de changer de pas et de se rendre sur la page des statistiques
     * @param e évènement
     * @throws IOException
     */
    @FXML
    public void accederStatistiques(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/statistiques.fxml"));
        primaryStage.setTitle("Statistiques");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        ControleurStatistiques.primaryStage=primaryStage;
    }

    /**
     * Cette méthode permet d'exporter une pile passée en paramètre dans  un fichier JSON
     * @param pile pile à exporter
     * @throws IOException
     */
    @FXML
    public void exporterPile(Pile pile) throws IOException{
        Gson export_pile = new Gson();
        String json = export_pile.toJson(pile);
        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/main/resources/eu/telecomnancy/flashcard/json/"+pile.getName()+".json"),"UTF-8"));
            writer.write(json);
            writer.close();
        }catch(IOException e){
            try {
                String homepath = System.getProperty("user.home");
                Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(homepath+"/NoneLedge/piles/"+pile.getName()+".json"),"UTF-8"));
                writer.write(json);
                writer.close();
            }catch(IOException a){
                a.printStackTrace();
            }
        }
    }

    /**
     * Cette méthode permet d'importer une pile dans notre application à partir d'un fichier JSON donné dans l'appli
     * @param nomFichier nom du fichier contenant la pile à importer au format JSON
     * @throws IOException
     */
    @FXML
    public void importerPile() throws IOException {
        FileChooser filechooser = new FileChooser();
        filechooser.setTitle("Choisissez la pile à importer (au format json)");
        File selectedFile = filechooser.showOpenDialog(null);

        String filePath = selectedFile.getPath();

        try {
            Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"UTF-8"));
            Gson gson = new Gson();
            gson.htmlSafe();
            Pile pile= gson.fromJson(reader, Pile.class);
            System.out.println(pile.getName());
            BufferedReader br = new BufferedReader(reader);
            if (pile == null) {
                dialog.setText("Le fichier demandé est vide.");
                dialog.setVisible(true);
                dialog1.setVisible(true);
                validation.setVisible(true);
            } else {
                dialog.setText("Importation réalisée avec succès.");
                dialog.setVisible(true);
                dialog1.setVisible(true);
                validation.setVisible(true);
                ListePile lp = new ListePile();
                lp.charger();
                lp.addPile(pile);
                lp.enregistrer();

                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/accueil.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 800, 600);
                primaryStage.setTitle("NoneLedge");
                primaryStage.setScene(scene);
                primaryStage.show();
                primaryStage.setResizable(false);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void internetPage(ActionEvent e) throws IOException {
        /* load the internet.fxml file */
        Parent root = FXMLLoader.load(getClass().getResource("fxml/login.fxml"));
        primaryStage.setTitle("Internet");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
        ControleurLogin.primaryStage=primaryStage;
    }
}


