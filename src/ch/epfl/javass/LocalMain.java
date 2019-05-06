package ch.epfl.javass;

import java.util.List;

import javafx.application.Application;
import javafx.stage.Stage;

public class LocalMain extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        List<String> args2 = getParameters().getRaw();

        if(args2.size()!=4 || args2.size() !=5) {
            System.out.println("Utilisation: java ch.epfl.javass.LocalMain <j1>…<j4> [<graine>]\n" + 
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
        
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}
