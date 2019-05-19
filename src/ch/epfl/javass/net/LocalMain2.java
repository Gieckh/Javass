package ch.epfl.javass.net;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LocalMain2 {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    private static final List<String> DEFAULT_NAMES = Collections.unmodifiableList(
            Arrays.asList("Aline", "Bastien", "Colette", "David")
    );
    private static final int DEFAULT_MCTS_ITERATION = 10_000;
    private static final String DEFAULT_IP_ADDRESS = "localhost";
    private static final int SIMULATED_PLAYER_WAIT_TIME_MS = 2000; //in milliseconds
    private static final int NEW_CARD_WAIT_TIME_MS = 1000;

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    private void displayError(String s) {
        System.err.println(s);
        System.exit(1);
    }

    private void checkSize(int size) {
        if( !(size == 4 || size == 5) ) {
            displayError("Mauvais nombre d'arguments : " + size + " n'est pas dans l'intervalle [|4, 5|]\n" +
                    "Utilisation : java ch.epfl.javass.LocalMain <j1>…<j4> [<graine>]\n" +
                    "où :\n" +
                    "<jn> spécifie le joueur n, ainsi :\n" +
                    "  h:<nom>  un joueur humain nommé <nom> \n"+
                    "  s:<nom>:<itérations>  un joueur simulé nommé <nom> avec <itérations> itérations \n" +
                    "  r:<nom>:<IP-Host>  un joueur humain à distance nommée <nom> et d'adresse IP <IP-Host> \n"+
                    "  <graine> la graine génératrice des autres graines utilisées partout dans le jeu \n"+
                    " où les noms <nom> sont optionnels (par défaut : Aline, Bastien, Colette et David dans l'ordre)\n" +
                    " où les itérations sont optionnelles (par défaut : 10 000 )\n" +
                    " où les IP-Host sont optionnels (par défaut : localhost )\n" +
                    " où la graine est optionnelle et est forcément en 5ème position \n ");
        }
    }

}
