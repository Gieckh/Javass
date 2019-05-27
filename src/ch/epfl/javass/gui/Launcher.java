package ch.epfl.javass.gui;

import ch.epfl.javass.LocalMain;
import ch.epfl.javass.PlayerType;
import ch.epfl.javass.jass.*;
import ch.epfl.javass.net.RemotePlayerClient;
import ch.epfl.javass.net.RemotePlayerServer;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

@SuppressWarnings("Duplicates")
public class Launcher extends Application {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/

    private static final int ARBITRARY_MIN_MCTS_ITERATIONS = 10;
    private static final int SIMULATED_PLAYER_WAIT_TIME_MS = 1; //in seconds
    private static final int NEW_CARD_WAIT_TIME_MS = 1000;      //in milliseconds.
    private static final String DEFAULT_IP_ADDRESS = "localhost";

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

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

    @SuppressWarnings("Duplicates")
    private GridPane createMainLauncher() {
        String[] args1 = {"s", "h", "s", "s"};
        String[] args2 = {};

        Text instructions = new Text("Choisissez votre type de partie : ");
        Button local = new Button("Partie locale");
        local.setOnAction(e -> LocalMain.main(args1));

        Button remote = new Button("Partie distante");
        remote.setOnAction(e -> {
            Thread gameThread = new Thread(() -> {
                Player player = new GraphicalPlayerAdapter();
                RemotePlayerServer rps =  new RemotePlayerServer(player);
                rps.run();
            });
            System.out.println("La partie commencera à la connexion du client...");
            gameThread.setDaemon(true);
            gameThread.start();
        });

        GridPane launcher = new GridPane();
        launcher.addRow(0, instructions);
        launcher.addRow(1, local);
        launcher.addRow(2, remote);

        GridPane.setHalignment(instructions, HPos.CENTER);
        GridPane.setHalignment(local       , HPos.CENTER);
        GridPane.setHalignment(remote      , HPos.CENTER);

        return launcher;
    }

    private GridPane createLocalLaucher() {
        //TODO

        Node[][] playersParameters;
        TextField seedTextField = new TextField();

        seedTextField.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                seedTextField.setText(oldValue);
        }));

        Button launch = new Button("Démarrer");
        launch.setOnAction(e -> {
            Map <PlayerId, String> playerNames;
            Map <PlayerId, Player> players;
            Random random = randomOrDefault(...);

            Thread gameThread = new Thread(() -> {
                JassGame g = new JassGame(random, players, playerNames);
                while (! g.isGameOver()) {
                    g.advanceToEndOfNextTrick();
                    try {
                        Thread.sleep(NEW_CARD_WAIT_TIME_MS);
                    } catch (Exception exception) {
                        throw new Error(exception);
                    }
                }
            });
        });
        return null;
    }


    private void createPlayerAndPutInMaps(Map<PlayerId, String> playerNames,
            Map<PlayerId, Player> players, List<String> args, PlayerId pId,
            Random random)
    {
        playerNames.put(pId, args.get(1));

        switch(PlayerType.toType(args.get(0))) {
        case HUMAN:
            players.put(pId, new GraphicalPlayerAdapter());
            break;

        case REMOTE:
            try {
                players.put(pId, new RemotePlayerClient(getHost(args.get(2))));
            }
            catch (IOException e) {
                displayError("Erreur de connexion au serveur du joueur distant n°" + pId.ordinal() + " à l'adresse " + getHost(args.get(2)));
            }
            break;

        case SIMULATED:
            players.put(
                    pId,
                    new PacedPlayer(
                            new MctsPlayer(
                                    pId,
                                    random.nextLong(),
                                    iterationsOrDefault(args.get(2))
                            ),
                            SIMULATED_PLAYER_WAIT_TIME_MS
                    )
            );
            break;

        default:
                throw new Error("Impossible!!!");
        }
    }

    private String getHost(String host) {
        return host.isEmpty() ? DEFAULT_IP_ADDRESS: host;
    }

    private int iterationsOrDefault(String iterationsString) {
        int iterations = (iterationsString.isEmpty()) ? 0: Integer.parseInt(iterationsString);
        if (iterations < ARBITRARY_MIN_MCTS_ITERATIONS)
            displayError("Erreur : spécification du nombre d'itérations invalide : " + iterations + "\n" +
                    "Le nombre d'itérations devrait être supérieur ou égal à " + ARBITRARY_MIN_MCTS_ITERATIONS);
        return iterations;
    }

    private Random randomOrDefault(String seed) {
        return seed.isEmpty() ? new Random(): new Random(Integer.parseInt(seed));
    }

    /** ============================================== **/
    /** =============    MAIN METHODS    ============= **/
    /** ============================================== **/

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane launcher = createMainLauncher();
        StackPane mainPane = new StackPane(launcher);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("ok");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
