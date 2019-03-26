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
            Player player = new MctsPlayer2(pId, 2019, 100_000);
            if (pId.team() == TeamId.TEAM_2) {
                player = new RandomPlayer(2019);
            }


            players.put(pId, player);
            playerNames.put(pId, pId.name());
        }

        for (int k = 1750 ; k < 2250 ; ++k){
            System.out.println(k);
            JassGame g = new JassGame(k, players, playerNames);
            while (!g.isGameOver()) {
                g.advanceToEndOfNextTrick();
//                System.out.println(
//                        "-------------------------------------------------------------");
            }
        }
    }
}