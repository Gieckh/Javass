package ch.epfl.javass.jass;

import java.util.EnumMap;
import java.util.Map;

import ch.epfl.javass.gui.GraphicalPlayerAdapter;
import javafx.application.Application;
import javafx.stage.Stage;
import  ch.epfl.javass.jass.PlayerId;

public final class TestMain extends Application {
  public static void main(String[] args) { launch(args); }

  @Override
  public void start(Stage primaryStage) throws Exception {
    Map<PlayerId, Player> ps = new EnumMap<>(PlayerId.class);
    ps.put(PlayerId.PLAYER_1, new GraphicalPlayerAdapter());
    ps.put(PlayerId.PLAYER_2, new PacedPlayer(new MctsPlayer(PlayerId.PLAYER_2, 789, 10_000),0));
    ps.put(PlayerId.PLAYER_3, new PacedPlayer(new MctsPlayer(PlayerId.PLAYER_3, 456, 10_000),0));
    ps.put(PlayerId.PLAYER_4, new PacedPlayer(new MctsPlayer(PlayerId.PLAYER_4, 789, 10_000),0));

    Map<PlayerId, String> ns = new EnumMap<>(PlayerId.class);
    PlayerId.ALL.forEach(i -> ns.put(i, i.name()));

    new Thread(() -> {
    JassGame g = new JassGame(0, ps, ns);
    while (! g.isGameOver()) {
      g.advanceToEndOfNextTrick();
      try { Thread.sleep(0000); } catch (Exception e) {}
    }
    }).start();
  }
}