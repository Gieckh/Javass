package ch.epfl.javass.jass;

import java.util.HashMap;
import java.util.Map;
//this is for a test, thus no need of javadoc
@SuppressWarnings("Duplicates")
public final class RandomJassGame2 {
    public static void main(String[] args) {
        Map<PlayerId, Player> players = new HashMap<>();
        Map<PlayerId, String> playerNames = new HashMap<>();
        int iterations = 1_000;
        //marche pour n'importe quelle seed
        for (PlayerId pId: PlayerId.ALL) {
            Player player = new MctsPlayerThomabenmato(pId, 2019, iterations);
            if (pId.team() == TeamId.TEAM_2) {
                 player = new MctsPlayer(pId, 2019, iterations);

            }


            players.put(pId, player);
            playerNames.put(pId, pId.name());
        }

        for (int k = 4000 ; k < 4250 ; ++k){
            System.out.println("itération : " + k);
            JassGame g = new JassGame(k, players, playerNames);
            while (!g.isGameOver()) {
                g.advanceToEndOfNextTrick();
//                System.out.println(
//                        "-------------------------------------------------------------");
            }
        }
    }
}