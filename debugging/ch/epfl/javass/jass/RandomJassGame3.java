
package ch.epfl.javass.jass;

import java.util.HashMap;
import java.util.Map;

import bonus.mctsPlayerSmart;
//this is for a test, thus no need of javadoc
public final class RandomJassGame3 {
    public static void main(String[] args) {

        //I expect the team 1 to lose ^^
        // after test, team 2 have almost 200 more points , with 10 000 iterations ;
        //which is not  great at all , but maybe a proof mcts does something
        Map<PlayerId, Player> players = new HashMap<>();
        Map<PlayerId, String> playerNames = new HashMap<>();

        int k = 100_000;
        for (PlayerId pId: PlayerId.ALL) {
          Player player = new  mctsPlayerSmart(pId.equals(PlayerId.PLAYER_2)?  PlayerId.PLAYER_2 : PlayerId.PLAYER_4, 2019, k);
          if (pId.team() == TeamId.TEAM_1) {
             player=  new mctsPlayerSmart(pId.equals(PlayerId.PLAYER_1)?  PlayerId.PLAYER_1 : PlayerId.PLAYER_3, 2019, k);
           }
         
          players.put(pId, player);
          playerNames.put(pId, pId.name());
        }
            JassGame g = new JassGame(1000, players, playerNames);
            while (!g.isGameOver()) {
                g.advanceToEndOfNextTrick();

            }
           
    }
}