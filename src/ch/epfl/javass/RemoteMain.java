package ch.epfl.javass;

import ch.epfl.javass.gui.GraphicalPlayerAdapter;
import ch.epfl.javass.jass.Player;
import ch.epfl.javass.net.RemotePlayerServer;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *  RemoteMain is the main class to run to launch the remotePlayerServer that will create a graphical player
 *  on this computer playing remotely a jassGame thrown by a LocalMain.
 *
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
@SuppressWarnings("Duplicates")
public class RemoteMain extends Application {
    /**
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Thread gameThread = new Thread(() -> {
            Player player = new GraphicalPlayerAdapter();
            RemotePlayerServer rps =  new RemotePlayerServer(player);
            rps.run();
        });
        System.out.println("La partie commencera à la connexion du client...");
        gameThread.setDaemon(true);
        gameThread.start();
    }

    
    /**
     * @brief executes launch that will make the server waiting for a connexion.
     *
     * @param args
    */
    public static void main(String[] args) {
        launch(args);
    }
}
