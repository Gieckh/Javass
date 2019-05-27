
package ch.epfl.javass.jass;

import java.util.HashMap;
import java.util.Map;

import bonus.mctsMemory;
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
        int i = 4000;
        int m = 0;
        int win = 0;
        for (PlayerId pId: PlayerId.ALL) {
          Player player = new  MctsPlayer(pId.equals(PlayerId.PLAYER_2)?  PlayerId.PLAYER_2 : PlayerId.PLAYER_4, i, k);
          if (pId.team() == TeamId.TEAM_1) {
             player=  new mctsPlayerSmart(pId.equals(PlayerId.PLAYER_1)?  PlayerId.PLAYER_1 : PlayerId.PLAYER_3, i, k);
           }
     
          players.put(pId, player);
          playerNames.put(pId, pId.name());
        }
        for(int l = 10 ; l < 100; ++l) {
            ++i;
            JassGame g = new JassGame(i, players, playerNames);
            while (!g.isGameOver()) {
                g.advanceToEndOfNextTrick();
                }
            m=m+ (g.turnState.score().totalPoints(TeamId.TEAM_2 )-g.turnState.score().totalPoints(TeamId.TEAM_1 ));
            System.out.println(m);
            System.out.println(g.turnState.score().totalPoints(TeamId.TEAM_2 )>=1000? ++win : win);
            System.out.println();
        
        }
            
           
    }
}