package ch.epfl.javass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ch.epfl.javass.gui.GraphicalPlayerAdapter;
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
        List<List<String>> informations = new ArrayList<List<String>>(4);
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
                if(list.get(0).equals("h")){
                    list.set(1, ((list.get(1).isEmpty()) ? LocalMain.defaultNames[i] : list.get(1)));
                    checkParameters(list, "h", i);
                    informations.set(i,list);

                }
                if(list.get(0).equals("s")){
                    list.set(1, ((list.get(1).isEmpty()) ? LocalMain.defaultNames[i] : list.get(1)));
                    try {
                        if(!list.get(2).isEmpty()&& Integer.parseInt(list.get(2))<10) {
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
                    list.set(2, ((list.get(2).isEmpty()) ? "10000" : list.get(2)));
                    checkParameters(list, "s", i);
                    informations.set(i,list);

                }
                if(list.get(0).equals("r")){
                    list.set(1, ((list.get(1).isEmpty()) ? LocalMain.defaultNames[i] : list.get(1)));
                    list.set(2, ((list.get(2).isEmpty()) ? "localhost" : list.get(2)));
                    checkParameters(list, "r", i);
                    informations.set(i,list);

                }
                if(!(list.get(0).equals("r")||list.get(0).equals("h")||list.get(0).equals("s"))){
                    displayError("Utilisation: java ch.epfl.javass.LocalMain <j1>…<j4> [<graine>]\n" + 
                            "où :\n" + 
                            "<j"+i +"> à la structure suivante : "+
                            "<a>:<b>:<c>"+
                            "où <a> doit être <r>, <s>, ou <h> ");
                }
                
                Random random = hasFiveArgs ?  new Random(generatingSeed) : new Random();
                long randomForJassGame = random.nextLong();
                List<Long> randomForPlayer = new ArrayList<>();
                randomForPlayer.add(0, random.nextLong());
                randomForPlayer.add(1, random.nextLong());
                randomForPlayer.add(2, random.nextLong());
                randomForPlayer.add(3, random.nextLong());
                                
                Map<PlayerId, Player> ps = new EnumMap<>(PlayerId.class);
                for(int j = 0 ; j<4 ; ++j) {
                    putCustomedPlayer(j, informations, ps, randomForPlayer);
                }
                Map<PlayerId, String> ns = new EnumMap<>(PlayerId.class);
                PlayerId.ALL.forEach(k -> ns.put(k, informations.get(k.ordinal()).get(1)));
                
                
                ps.put(PlayerId.PLAYER_1, new GraphicalPlayerAdapter());
                
                ps.put(PlayerId.PLAYER_2, new PacedPlayer(new MctsPlayer(PlayerId.PLAYER_2, 789, 10_000),1));
                ps.put(PlayerId.PLAYER_3, new PacedPlayer(new MctsPlayer(PlayerId.PLAYER_3, 456, 10_000),1));
                ps.put(PlayerId.PLAYER_4, new PacedPlayer(new MctsPlayer(PlayerId.PLAYER_4, 789, 10_000),1));
               
                
            }
            
        }
        
    }
    
    private void putCustomedPlayer(int i,List<List<String>>  list,Map<PlayerId,Player> ps , List<Long> seeds) {
        switch (list.get(i).get(0)) {
        
        case "h": ps.put(PlayerId.ALL.get(i), new GraphicalPlayerAdapter());
            break;
            
        case "s" : ps.put(PlayerId.ALL.get(i), new PacedPlayer(new MctsPlayer(PlayerId.PLAYER_2, seeds.get(i), list.get(i).get(2)),2));

            break;
            
        case "r": ps.put(PlayerId.ALL.get(i), new RemotePlayerClient("localhost", Net.PORT_NUMBER);)
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


