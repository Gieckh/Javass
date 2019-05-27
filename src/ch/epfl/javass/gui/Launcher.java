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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

@SuppressWarnings("Duplicates")
public class Launcher extends Application {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/

    private static final List<String> DEFAULT_NAMES = Collections.unmodifiableList(
            Arrays.asList("Aline", "Bastien", "Colette", "David")
    );

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

        Map<PlayerId, PlayerType> playerTypes = new HashMap<>();
        List<List<Node>> playersGUI = new ArrayList<>();
        for (PlayerId pId: PlayerId.ALL) {
            playersGUI.add(createPlayerField(pId, playerTypes));
        }

        TextField seed = new TextField();
        seed.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                seed.setText(oldValue);
        }));


        Button launch = new Button("Démarrer");
        launch.setOnAction(e -> {
            Map <PlayerId, String> playerNames = new HashMap<>(PlayerId.COUNT);
            Map <PlayerId, Player> players = new HashMap<>(PlayerId.COUNT);
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


        GridPane localLauncher = new GridPane();
        for (int i = 0; i < PlayerId.COUNT; ++i) {
            localLauncher.addColumn(
                    i,
                    new StackPane(
                            playersGUI.get(i).get(0),
                            playersGUI.get(i).get(1),
                            playersGUI.get(i).get(2)),
                    playersGUI.get(i).get(3),
                    playersGUI.get(i).get(4)
                    );
        }
        return null;
    }

    private List<Node> createPlayerField(PlayerId pId, Map<PlayerId, PlayerType> playerTypes) {
        TextField nameTextField = new TextField(DEFAULT_NAMES.get(pId.ordinal()));
        Button human = new Button("humain");
        Button remote = new Button("distant");
        Button simulated = new Button("simulated");

        TextField name = new TextField(DEFAULT_NAMES.get(pId.ordinal()));

        Label iterationsLabel = new Label("Itérations :");
        TextField iterationsField = new TextField("10000");
        iterationsField.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*"))
                iterationsField.setText(oldValue);
        }));
        HBox iterationsBox = new HBox(iterationsLabel, iterationsField);
        iterationsBox.setVisible(false);

        Label ipAddressLabel = new Label("Adresse IP :");
        TextField ipAddressField = new TextField("localhost");
        HBox ipAddressBox = new HBox(ipAddressLabel, ipAddressField);
        ipAddressBox.setVisible(false);

        human.setOnAction(e -> {
            human.setVisible(false);
            remote.setVisible(false);
            simulated.setVisible(false);

            playerTypes.put(pId, PlayerType.HUMAN);
        });

        remote.setOnAction(e -> {
            human.setVisible(false);
            remote.setVisible(false);
            simulated.setVisible(false);
            iterationsBox.setVisible(true);

            playerTypes.put(pId, PlayerType.REMOTE);
        });

        simulated.setOnAction(e -> {
            human.setVisible(false);
            remote.setVisible(false);
            simulated.setVisible(false);
            ipAddressBox.setVisible(true);

            playerTypes.put(pId, PlayerType.SIMULATED);
        });

        return Collections.unmodifiableList(Arrays.asList(human, remote, simulated, name, iterationsBox, ipAddressBox));
    }

    private void createsPlayerAndPutsInMaps(Map<PlayerId, String> playerNames,
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
