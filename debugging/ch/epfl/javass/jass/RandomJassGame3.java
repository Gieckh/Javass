package ch.epfl.javass.jass;

import java.util.HashMap;
import java.util.Map;
//this is for a test, thus no need of javadoc
@SuppressWarnings("Duplicates")
public final class RandomJassGame3 {
    public static void main(String[] args) {
        Map<PlayerId, Player> players = new HashMap<>();
        Map<PlayerId, String> playerNames = new HashMap<>();
        //marche pour n'importe quelle seed
        Player player;
        for (PlayerId pId: PlayerId.ALL) {
            if (pId.team() == TeamId.TEAM_1)
                player = new MctsPlayer(pId, 2019, 1_000);
            else
                player = new RandomPlayer(2019);

            players.put(pId, player);
            playerNames.put(pId, pId.name());
        }

        for (int k = 2000 ; k < 3000 ; ++k){
            System.out.println("itération : " + k);
            JassGame g = new JassGame(k, players, playerNames);
            while (!g.isGameOver()) {
                g.advanceToEndOfNextTrick();
                //                System.out.println(
                //                        "-------------------------------------------------------------");
            }
        }

//        JassGame g = new JassGame(2000, players, playerNames);
//        while(!g.isGameOver()) {
//            g.advanceToEndOfNextTrick();
////            System.out.println("-------------------------------------------------------------");
//        }
//        System.out.println("Over");
    }
}