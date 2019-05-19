package ch.epfl.javass.net;

import ch.epfl.javass.PlayerType;
import com.sun.org.apache.regexp.internal.RE;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LocalMain2 extends Application {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    private static final List<String> DEFAULT_NAMES = Collections.unmodifiableList(
            Arrays.asList("Aline", "Bastien", "Colette", "David")
    );
    private static final int DEFAULT_MCTS_ITERATION = 10_000;
    private static final String DEFAULT_IP_ADDRESS = "localhost";

    private static final int SIMULATED_PLAYER_WAIT_TIME_MS = 2; //in seconds
    private static final int NEW_CARD_WAIT_TIME_MS = 1000;
    private static final int ARBITRARY_MIN_MCTS_ITERATIONS = 10;
    private static final int SIZE_WITHOUT_SEED = 4;
    private static final int SIZE_WITH_SEED = 5;

    private static final String HUMAN = "h";
    private static final String REMOTE = "r";
    private static final String SIMULATED = "s";


    /** ============================================== **/
    /** ==============   PRIVATE METHODS  ============ **/
    /** ============================================== **/

    /**
     * @brief This method is used to display an error, and then end the program
     *
     * @see System#exit
     * @param s ({@code String}) the error message to display.
     */
    private void displayError(String s) {
        System.err.println(s);
        System.exit(1);
    }

    /**
     * @brief Checks that the program is run with the correct number of parameters. (4 or 5)
     *
     * @param args ({@code List<String>}) the arguments of the main.
     */
    private void checkTotalNoOfParametersisValid (List<String> args) {
        int size = args.size();
        if( !(size == SIZE_WITHOUT_SEED || size == SIZE_WITH_SEED) ) {
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
                    " où la graine est optionnelle et est forcément en 5ème position.\n ");
        }
    }

    /**
     * @brief Given one of the arguments to the program [except the seed], checks
     *        that its first argument refers to a valid {@code PlayerType}, i.e. is
     *        a "h", a "r", or a "s".
     *
     * @param arg ({@code List<String>}) an argument of the main program
     *            [not the seed, i.e the 5th argument]
     */
    private void checkPlayerTypeIsValid (List<String> arg) {
        String fstParam = arg.get(0);
        if (! (fstParam.equals(HUMAN)  ||
               fstParam.equals(REMOTE) ||
               fstParam.equals(SIMULATED))
        )
            displayError("Erreur : spécification de joueur invalide : " + fstParam +"\n" +
                         "Le premier caractère de chaque argument du programme devrait être 'h', 'r' ou 's'."
            );
    }

    /**
     * @brief Checks that each argument [represented by a {@code List<String>}]
     *        of the main contains the correct number of parameters.
     *
     * @param arg ({@code List<String>}) an argument of the main program.
     * @param playerType
     */
    private void
    checkNoOfEachArgumentParametersIsValid (List<String> arg, PlayerType playerType) {
        switch (playerType) {
            case HUMAN:
                if (arg.size() > 2)
                    displayError(
                            "Erreur : nombre de paramètres excessif : " + arg.size() + "dans l'entrée \"" + String.join(":", arg) + "\".\n" +
                            "Un joueur de type humain admet 2 paramètres au maximum\n" +
                            "Par exemple : \"h:Aline\" définit un joueur humain nommé Aline."
                    );
                break;

            case REMOTE:
                if (arg.size() > 3)
                    displayError(
                            "Erreur : nombre de paramètres excessif : " + arg.size() + "dans l'entrée \"" + String.join(":", arg) + "\".\n" +
                            "Un joueur de type remote (distant) admet 3 paramètres au maximum\n" +
                            "Par exemple : \"r:Bastien:128.178.243.14\" définit un joueur distant nommé Bastien dont l'adresse IP est 128.178.243.14."
                    );
                break;

            case SIMULATED:
                if (arg.size() > 3)
                    displayError(
                            "Erreur : nombre de paramètres excessif " + arg.size() + "dans l'entrée \"" + String.join(":", arg) + "\".\n" +
                            "Un joueur de type simulé admet 3 paramètres au maximum\n" +
                            "Par exemple : \"s:Bastien:10000\" définit un joueur simulé nommé Bastien, utilisant l'algorithme MCTS avec 10_000 itérations."
                    );
                break;

            default:
                throw new Error();
        }
    }

    /**
     * @brief Given a simulated player, checks that its given number of iterations
     *        is valid [if there is one], and returns it. If there is none, returns
     *        the default number of iterations.
     * @param arg ({@code List<String>}) an argument of the main program [a simulated player].
     *
     * @return ({@code int}) a simulated player's number of iterations.
     */
    private int checkNumberOfMctsIterationsIsValidAndReturnsIt (List<String> arg) {
        if (arg.size() < 3)
            return DEFAULT_MCTS_ITERATION;

        assert (arg.size() == 3);
        int iterations;
        try {
            iterations = Integer.parseInt(arg.get(2));
        }
        catch (NumberFormatException e) {
            iterations = DEFAULT_MCTS_ITERATION; //ignore
            displayError("Erreur : spécification du nombre d'itérations invalide : " + arg.get(3) + "\n" +
                         "Le nombre d'itérations devrait être une valeur de type int.");
        }

        if (iterations < ARBITRARY_MIN_MCTS_ITERATIONS)
            displayError("Erreur : spécification du nombre d'itérations invalide : " + arg.get(3) + "\n" +
                         "Le nombre d'itérations devrait être supérieur ou égal à " + ARBITRARY_MIN_MCTS_ITERATIONS);

        return iterations;
    }

    /**
     * @brief Here we assume we have either 4 or 5 arguments to the program.
     *        We then generate a new Random, depending on this number of arguments.
     *
     * @param args ({@code List<Strin>}) the argument of the main program.
     * @return a new {@code Random}
     */
    private Random generateRandom(List<String> args) {
        if (args.size() == SIZE_WITHOUT_SEED)
            return new Random();

        assert (args.size() == SIZE_WITH_SEED);
        try {
            return new Random(Long.parseLong(args.get(4)));
        }
        catch (NumberFormatException e) {
            displayError("Erreur : spécification de la graine invalide : " + args.get(4) + "\n" +
                         "La graine devrait être une valeur de type long.");
        }

        //Unreachable statement
        throw new Error("How in God's name did you get there?");
    }


    /** ============================================== **/
    /** =============    MAIN METHODS    ============= **/
    /** ============================================== **/

    /**
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        List<String> args = getParameters().getRaw();

    }

    /**
     * @brief the main of the whole project. It runs "launch" that will create
     *        a new thread for a game customized by the args array.
     *
     * @param args an Array of {@code String} containing information about the players
     */
    public static void main(String[] args) {
        launch(args);
    }
}
