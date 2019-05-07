package ch.epfl.javass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ch.epfl.javass.gui.GraphicalPlayerAdapter;
import ch.epfl.javass.jass.JassGame;
import ch.epfl.javass.jass.MctsPlayer;
import ch.epfl.javass.jass.PacedPlayer;
import ch.epfl.javass.jass.Player;
import ch.epfl.javass.jass.PlayerId;
import ch.epfl.javass.net.Net;
import ch.epfl.javass.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

public class LocalMain extends Application {
    private static String defaultNames[] = {"Aline","Bastien","Colette","David"};
    private List<String> typeOfPlayer = new ArrayList<String>(4);
    private List<String> names = new ArrayList<String>(4);
    private List<String> hosts = new ArrayList<String>(4);
    private List<Long> randomForPlayer = new ArrayList<>(4);
    private List<Integer> iterations = new ArrayList<>(4); 
    
    private void checkSize(int size) {
        if(size!=4 || size !=5) {
            displayError("Utilisation: java ch.epfl.javass.LocalMain <j1>…<j4> [<graine>]\n" + 
                    "où :\n" + 
                    "<jn> spécifie le joueur n, ainsi:\n" + 
                    "  h:<nom>  un joueur humain nommé <nom> \n"+
                    "  s:<nom>:<itérations>  un joueur simulé nommé <nom> avec <itérations> itérations \n" +
                    "  r:<nom>:<IP-Host>  un joueur humain à distance nommé <nom> et avec l'adresse IP <IP-Host> \n"+
                    "  <graine> la graine génératrice des autres graines utilisées partout dans le jeu \n"+
                    " où les noms sont optionnels (par defaut : Aline, Bastien, Colette et David dans l'ordre ) \n " +
                    " où les itérations sont optionnelles (par defaut : 10 000 )\n " +
                    " où les IP-Host sont optionnels (par defaut : localhost )\n "+
                    " où graine est optionnelle et est forcément en 5ème position \n ");
        }
    }
    
    private void checkParameters(List list, String s, int i) {
        switch (s) {
        
        case "h": if(list.size()>2) {
            displayError("Utilisation: java ch.epfl.javass.LocalMain <j1>…<j4> [<graine>]\n" + 
                    "où :\n" + 
                    "<j"+i+"> est un joueur humain (h) "+
                    "qui n'admet qu'un paramètre maximum :"+
                    "le nom du joueur "+
                    "ex : <j"+i+">:<"+list.size()+"> .");
        }
            break;
            
        case "r":if(list.size()>3) {
            displayError("Utilisation: java ch.epfl.javass.LocalMain <j1>…<j4> [<graine>]\n" + 
                    "où :\n" + 
                    "<j"+i+"> est un joueur humain (h) "+
                    "qui n'admet que deux paramètres maximums :"+
                    "le nom du joueur et l'IP du joueur distant"+
                    "ex : <j"+i+">:<"+list.get(1)+">:<"+list.get(2)+">  .");
        }
            break;
            
        case "s": if(list.size()>3) {
            displayError("Utilisation: java ch.epfl.javass.LocalMain <j1>…<j4> [<graine>]\n" + 
                    "où :\n" + 
                    "<j"+i+"> est un joueur simulé (s) "+
                    "qui n'admet que deux paramètres maximums :"+
                    "le nom du joueur et le nombre d'itérations "+
                    "ex : <j"+i+">:<"+list.get(1)+">:<"+list.get(2)+">  .");
        }
            break;
            
        default: displayError("wrong input on switch");            
        }
    }
    
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        List<String> args = getParameters().getRaw();
        Map<PlayerId, Player> ps = new EnumMap<>(PlayerId.class);
        Map<PlayerId, String> ns = new EnumMap<>(PlayerId.class);
        boolean hasFiveArgs = false; 
        long generatingSeed =0;
        int size = args.size();
        checkSize(size);
        for ( int i = 0 ; i < size; ++i) {
            if(i == 5) {
                try {
                generatingSeed = Long.parseLong(args.get(i));
                hasFiveArgs = true;
                }
                catch (NumberFormatException e){
                    displayError("Utilisation: java ch.epfl.javass.LocalMain <j1>…<j4> [<graine>]\n" + 
                    "où :\n" + 
                    "<graine> devrait etre de type long:\n");
                }
            }
            else {
                List<String> list = Arrays.asList(args.get(i).split(":"));
                names.set(i, ((list.get(1).isEmpty()) ? LocalMain.defaultNames[i] : list.get(1)));
                if(list.get(0).equals("h")){
                    checkParameters(list, "h", i);
                    typeOfPlayer.set(i, "h");
                }
                if(list.get(0).equals("s")){
                    checkParameters(list, "s", i);
                    typeOfPlayer.set(i,"s");
                    try {
                        if(!list.get(2).isEmpty()|| Integer.parseInt(list.get(2))<10) {
                            displayError("Utilisation: java ch.epfl.javass.LocalMain <j1>…<j4> [<graine>]\n" + 
                                    "où :\n" + 
                                    "<j"+i+"> est un joueur simulé (s) "+
                                    "et doit avoir comme deuxieme paramètre "+
                                    "un nombre d'itérations strictement superieur a 9");
                        }
                    }
                    catch (NumberFormatException e){
                        displayError("Utilisation: java ch.epfl.javass.LocalMain <j1>…<j4> [<graine>]\n" + 
                                "où :\n" + 
                                "<j"+i+"> est un joueur simulé (s) "+
                                "qui n'admet doit avoir comme deuxième paramètre "+
                                "le nombre d'itérations "+
                                "forcément un entier"
                                );
                    }
                    iterations.set(i, (list.get(2).isEmpty()) ? 10000 : Integer.parseInt(list.get(2)));

                }
                if(list.get(0).equals("r")){
                    checkParameters(list, "r", i);
                    typeOfPlayer.set(i, "r");
                    hosts.set(i, list.set(2, ((list.get(2).isEmpty()) ? "localhost" : list.get(2))));

                }
                if(!(list.get(0).equals("r")||list.get(0).equals("h")||list.get(0).equals("s"))){
                    displayError("Utilisation: java ch.epfl.javass.LocalMain <j1>…<j4> [<graine>]\n" + 
                            "où :\n" + 
                            "<j"+i +"> à la structure suivante : "+
                            "<a>:<b>:<c>"+
                            "où <a> doit être <r>, <s>, ou <h> ");
                }
                
                
             }
        }
                
        Random random = hasFiveArgs ?  new Random(generatingSeed) : new Random();
        long randomForJassGame = random.nextLong();
                        
        for(int j = 0 ; j<4 ; ++j) {
            randomForPlayer.set(j, random.nextLong());
            putCustomedPlayer(j,ps);
            ns.put(PlayerId.ALL.get(j), names.get(j));
        }
        
        new Thread(() -> {
            JassGame g = new JassGame(randomForJassGame, ps, ns);
            while (! g.isGameOver()) {
              g.advanceToEndOfNextTrick();
              try { Thread.sleep(1000); } catch (Exception e) {}
            }
            }).start();

        

    
        
    }
        
    
    
    private void putCustomedPlayer(int i, Map<PlayerId,Player> ps) {
        switch (typeOfPlayer.get(i)) {
        
        case "h": ps.put(PlayerId.ALL.get(i), new GraphicalPlayerAdapter());
            break;
            
        case "s" : ps.put(PlayerId.ALL.get(i), new PacedPlayer(new MctsPlayer(PlayerId.ALL.get(i), randomForPlayer.get(i), iterations.get(i)),2));
            break;
            
        case "r": try {
                ps.put(PlayerId.ALL.get(i), new RemotePlayerClient(hosts.get(i)));
            } catch (IOException e) {
                displayError("Utilisation: java ch.epfl.javass.LocalMain .\n" + 
                        "où une erreur de connexion au serveur à eu lieu pour le joueur distant "+i+" :\n" + 
                        " il devrait avoir la structure suivante : <r>:<name>:<IpAdress> \n" + 
                        " l'IpAdress est peut etre mauvaise , ou le serveur n'est peut etre pas lancé.\"");
                e.printStackTrace();
            }
            break;
            
        default: displayError("wrong input on switch");            
        }
    }
    
    

    public static void main(String[] args) {
        launch(args);
    }
    
    private void displayError(String s) {
        System.err.println(s);
        System.exit(1);
    }
    
}


