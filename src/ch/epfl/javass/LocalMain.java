package ch.epfl.javass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;

public class LocalMain extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        List<String> args = getParameters().getRaw();
        List<List<String>> informations = new ArrayList<List<String>>(4);
        long generatingSeed ;
        int size = args.size();
        if(size!=4 || size !=5) {
            System.err.println("Utilisation: java ch.epfl.javass.LocalMain <j1>…<j4> [<graine>]\n" + 
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
            System.exit(1);
        }
        
        for ( int i = 0 ; i < size; ++i) {
            if(i == 5) {
                try {
                generatingSeed = Long.parseLong(args.get(i));
                }
                catch (NumberFormatException e){
                    System.err.println("Utilisation: java ch.epfl.javass.LocalMain <j1>…<j4> [<graine>]\n" + 
                    "où :\n" + 
                    "<graine> devrait etre de type long:\n");
                    System.exit(1);
                }
            }
            else {
                List<String> list = new ArrayList<String>();
                list.add(args.get(i).split(":")[0]);
                if(list.get(0).equals("h")){
                    
                }
                else if(list.get(0).equals("s")){
                    
                }
                else if(list.get(0).equals("r")){
                    
                }
                else{
                    System.err.println("Utilisation: java ch.epfl.javass.LocalMain <j1>…<j4> [<graine>]\n" + 
                            "où :\n" + 
                            "<jn> à la structure suivante :");
                    System.exit(1);
                }
                
                
                informations.get(i).addAll(Arrays.asList(args.get(i).split(":")));
            }
            
        }
        
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}
