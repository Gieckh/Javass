package ch.epfl.javass;

import ch.epfl.javass.gui.GraphicalPlayer;
import ch.epfl.javass.gui.GraphicalPlayerAdapter;
import ch.epfl.javass.jass.JassGame;
import ch.epfl.javass.jass.MctsPlayer;
import ch.epfl.javass.jass.Player;
import ch.epfl.javass.jass.PlayerId;
import ch.epfl.javass.net.RemotePlayerServer;
import javafx.application.Application;
import javafx.stage.Stage;

public class RemoteMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Thread gameThread = new Thread(() -> {
            Player player = new GraphicalPlayerAdapter();
            RemotePlayerServer gali =  new RemotePlayerServer(player);
            gali.run();       
          });
        System.out.println("La partie commencera à la connexion du client...");
        gameThread.setDaemon(true);
        gameThread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
