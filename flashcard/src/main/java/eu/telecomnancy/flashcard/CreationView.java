package eu.telecomnancy.flashcard;

import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.File;
import java.util.ArrayList;

public class CreationView implements Observateurs{
    private ListView listCard;
    private TextField questionTexte;
    private TextField questionImage;
    private TextField reponse;
    private Pile pile;
    private Text nomPile;
    private ControleurCreationCarte controleurCreationCarte;

    public CreationView(ListView listCard, TextField questionTexte,TextField questionImage, TextField reponse, Pile pile, Text nomPile, ControleurCreationCarte controleurCreationCarte) {
        this.listCard = listCard;
        this.questionTexte = questionTexte;
        this.questionImage = questionImage;

        this.reponse = reponse;
        this.pile = pile;
        this.nomPile = nomPile;
        this.controleurCreationCarte = controleurCreationCarte;
    }

    /**
     * Cette méthode permet d'afficher les cartes de la pile ainsi que son identifiant, sa question ou sa réponse selon le sens de la carte
     */
    @Override
    public void update() {
        listCard.getItems().clear();
        int currentCard = 1;
        ArrayList element = new ArrayList<>();
        if (pile.getCards() != null){
            for(Carte c : pile.getCards()){
                HBox hBox = new HBox();
                /* add a child to the hbox */
                hBox.getChildren().add(new Text("Carte "+currentCard));
                /* add margin in the hBox */
                hBox.setSpacing(50);
                Button button = new Button("Supprimer");
                /* set the action button to delete the card at the current index */
                /*set a name to the button*/
                button.setId((currentCard-1)+"");
                button.setOnAction(actionEvent -> {
                    /* get the id of the button */
                    String id = ((Button)actionEvent.getSource()).getId();
                    /* convert the id to int */
                    int index = Integer.parseInt(id);
                    try{
                        File file = new File(pile.getCard(index).getQuestionImage());
                        file.delete();
                    }
                    catch (Exception e){
                        System.out.println("No image");
                    }


                    pile.getCards().remove(index);

                    this.update();
                });
                hBox.getChildren().add(button);
                listCard.getItems().add(hBox);
                currentCard++;
                listCard.getItems().add("Question texte : ");
                TextField questionTexte = new TextField(c.getQuestionTexte());
                listCard.getItems().add(questionTexte);
                element.add(questionTexte);

                listCard.getItems().add("Question image : ");

                String nomImage = "";
                if (c.getQuestionImage() != null){
                     nomImage = c.getQuestionImage().substring(c.getQuestionImage().lastIndexOf("/")+1);
                }
                else{  nomImage = "";
                }
                TextField questionImage = new TextField(nomImage);
                listCard.getItems().add(questionImage);
                element.add(questionImage);

                TextField reponse = new TextField(c.getAnswer());
                listCard.getItems().add("Réponse : ");
                listCard.getItems().add(reponse);
                element.add(reponse);
                listCard.getItems().add(" ");
            }
        }
        this.nomPile.setText(pile.getName());
        controleurCreationCarte.setElement(element);
        questionTexte.setText("");
        questionImage.setText("");
        reponse.setText("");
    }
}
