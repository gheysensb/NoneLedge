package eu.telecomnancy.flashcard;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Pile {
    private List<Carte> cartes;
    private String name;
    private String description;
    private int currentCard;
    private int id;
    private static int ID = 0;

    public Pile(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = ID;
        ID++;
        this.cartes = new ArrayList<Carte>();
        this.currentCard = 0;
    }

    /**
     * Cette méthode permet de retourner le nombre de cartes connues dans la pile
     * @return nombre de cartes connues
     */
    public int compterConnu(){
        int compteur = 0;
        for(Carte c :this.cartes){
            if (c.isConnu()) compteur++;
        }
        return compteur;
    }

    /**
     * Cette méthode permet de retourner le nombre de cartes inconnues (à réviser) dans la pile
     * @return nombre de cartes inconnues
     */
    public int compterInconnu(){
        int i=0;
        for(Carte c : this.cartes){
            if (!c.isConnu()) i++;
        }
        return i;
    }

    public void addCard(Carte carte) {
        this.cartes.add(carte);
    }

    /**
     * Cette méthode permet de réupérer une carte de la pile en fonction de son identifiant
     * @param id identifiant de la carte
     * @return carte dont l'identifiant a été passé en paramètre
     */
    public Carte getCard(int id) {
        System.out.println("id : "+id);
        return this.cartes.get(id);
    }

    /**
     * Cette méthode retourne la carte courante dans la pile
     * @return carte courante
     */
    public Carte getCard() {
        return this.cartes.get(this.currentCard);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCardCount() {
        return this.cartes.size();
    }

    public List<Carte> getCards() {
        return this.cartes;
    }
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int size() {
        return this.cartes.size();
    }


    /**
     * Cette méthode permet de mélanger les cartes lorsque l'utilisateur choisit le mode aléatoire
     */
    public void melanger(){
        List<Carte> cartesMelangees = new ArrayList<Carte>();
        int i = 0;
        while (this.cartes.size() > 0){
            int random = (int) (Math.random() * this.cartes.size());
            cartesMelangees.add(this.cartes.get(random));
            this.cartes.remove(random);
        }
        this.cartes = cartesMelangees;
    }

    public void exporterPile() throws IOException {
        Gson export_pile = new Gson();
        String json = export_pile.toJson(this);
        try {
            FileWriter writer = new FileWriter("src/main/resources/eu/telecomnancy/flashcard/upload/"+this.getName()+".json");
            writer.write(json);
            writer.close();
        }catch(IOException e){
            try {
                FileWriter writer = new FileWriter(getClass().getResource("json/"+this.getName()+".json").toString());
                writer.write(json);
                writer.close();
            }catch(IOException a){
                a.printStackTrace();
            }
        }
    }
}
