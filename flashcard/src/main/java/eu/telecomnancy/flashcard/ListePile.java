package eu.telecomnancy.flashcard;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.MalformedJsonException;

public class ListePile {

    private List<Pile> piles;
    private int currentPile;

    public ListePile() {
        this.piles = new ArrayList<Pile>();
        this.currentPile = 0;
    }

    /**
     * Cette méthode permet de charger le fichier contenant les piles de notre application
     * @throws MalformedJsonException
     */
    public void charger() throws MalformedJsonException{
        Gson gson = new Gson();
        try {
            /* Ce bloc s'exécute lorsqu'on est dans IntelliJ */
            String emplacement = "src/main/resources/eu/telecomnancy/flashcard/json/piles.json" ;
            Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(emplacement),"UTF-8"));
            gson.htmlSafe();
            JsonObject root = gson.fromJson(reader, JsonObject.class);
            if (root != null) this.piles = gson.fromJson(root.get("piles"), new TypeToken<List<Pile>>() {}.getType());
        } catch (IOException e) {
            try {
                /* Ce bloc s'exécute lorsqu'on est dans le jar */
                String homepath = System.getProperty("user.home");
                String emplacement = homepath+"/NoneLedge/piles.json" ;
                Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(emplacement),"UTF-8"));

                gson.htmlSafe();
                JsonObject root = gson.fromJson(reader, JsonObject.class);
                if (root != null) this.piles = gson.fromJson(root.get("piles"), new TypeToken<List<Pile>>() {}.getType());
            } catch (IOException a) {
                a.printStackTrace();
            }
        }
    }

    /**
     * Cette méthode permet de sauvegarder les piles de notre application dans un fichier JSON
     */
    public void enregistrer(){
        Gson new_pile = new Gson();
        String json = new_pile.toJson(this);
        try {
            /* Ce bloc s'exécute lorsqu'on est dans IntelliJ */
            String emplacement = "src/main/resources/eu/telecomnancy/flashcard/json/piles.json" ;
            Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(emplacement),"UTF-8"));
            w.write(json);
            w.close();
        }catch(IOException e){
            try {
                /* Ce bloc est exécuté lorsqu'on se situe dans le jar */
                String homepath = System.getProperty("user.home");
                String emplacement = homepath+"/NoneLedge/piles.json" ;
                Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(emplacement),"UTF-8"));
                w.write(json);
                w.close();
            }catch(IOException a){
                a.printStackTrace();
            }
        }
    }

    public void addPile(Pile pile) {
        this.piles.add(pile);
    }

    public void removePile(Pile pile) {
        this.piles.remove(pile);
    }

    public List<Pile> getPiles(){
        return this.piles;
    }
}
