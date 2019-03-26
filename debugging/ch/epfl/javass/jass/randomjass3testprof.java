package ch.epfl.javass.jass;
import java.util.HashMap;
import java.util.Map;
//this is for a test, thus no need of javadoc
public class randomjass3testprof {
        public static void main(String[] args) {
            Map<PlayerId, Player> players = new HashMap<>();
            Map<PlayerId, String> playerNames = new HashMap<>();
          for (PlayerId pId: PlayerId.ALL) {
              Player player = new PacedPlayer(new RandomPlayer(2019), 1);
             if (pId == PlayerId.PLAYER_4) {
                 player = new MctsPlayer(PlayerId.PLAYER_4, 2019, 100_000);
              }
             if (pId == PlayerId.PLAYER_2) {
                 player = new MctsPlayer(PlayerId.PLAYER_2, 2019, 100_000);
               }
           if (pId == PlayerId.PLAYER_1) {
           player = new PrintingPlayer(player);
       }
              players.put(pId, player);
              playerNames.put(pId, pId.name());
            }
            JassGame g = new JassGame(2019, players, playerNames);
            TurnState t = TurnState.
            while (!g.isGameOver()) {
                g.advanceToEndOfNextTrick();
                System.out.println("-------------------------------------------------------------");
            }
        }
    }

