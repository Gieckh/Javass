package ch.epfl.javass.jass;

import java.util.HashMap;
import java.util.Map;
//this is for a test, thus no need of javadoc
public final class RandomJassGame2 {
    public static void main(String[] args) {
        Map<PlayerId, Player> players = new HashMap<>();
        Map<PlayerId, String> playerNames = new HashMap<>();
        //marche pour n'importe quelle seed
        for (PlayerId pId: PlayerId.ALL) {
            Player player = new MctsPlayer2(pId, 2019, 100);
//            if (pId == PlayerId.PLAYER_1) {
                player = new PrintingPlayer(player);
//            }

            players.put(pId, player);
            playerNames.put(pId, pId.name());
        }

        JassGame g = new JassGame(2019, players, playerNames);
        while (!g.isGameOver()) {
            g.advanceToEndOfNextTrick();
            System.out.println("-------------------------------------------------------------");
        }
    }
}