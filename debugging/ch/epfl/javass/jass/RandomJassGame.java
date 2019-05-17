//package ch.epfl.javass.jass;
//
//import java.util.HashMap;
//import java.util.Map;
////this is for a test, thus no need of javadoc
//public final class RandomJassGame {
//    public static void main(String[] args) {
//          Map<PlayerId, Player> players = new HashMap<>();
//          Map<PlayerId, String> playerNames = new HashMap<>();
//          //marche pour n'importe quelle seed
//          for (PlayerId pId: PlayerId.ALL) {
//              Player player = new MctsPlayer(pId, 2019, 100);
//              if (pId == PlayerId.PLAYER_1) {
//                  player = new PacedPlayer(new PrintingPlayer(player), 100);
//              }
//
//              players.put(pId, player);
//              playerNames.put(pId, pId.name());
//          }
//
//    //      JassGame g = new JassGame(2039, players, playerNames);
//          JassGame g = new JassGame(2019, players, playerNames);
//          while (!g.isGameOver()) {
//              g.advanceToEndOfNextTrick();
//              System.out.println("----");
//          }
//    }
//}
package ch.epfl.javass.jass;

import java.util.HashMap;
import java.util.Map;

import bonus.mctsPlayerSmart;
//this is for a test, thus no need of javadoc
public final class RandomJassGame {
    public static void main(String[] args) {
//        Map<PlayerId, Player> players = new HashMap<>();
//        Map<PlayerId, String> playerNames = new HashMap<>();
        //marche pour n'importe quelle seed
//        for (PlayerId pId: PlayerId.ALL) {
//            Player player = new MctsPlayer(pId, 2019, 100);
//            if (pId == PlayerId.PLAYER_1) {
//                player = new PrintingPlayer(player);
//            }
//
//            players.put(pId, player);
//            playerNames.put(pId, pId.name());
//        }
        
        //I expect the team 1 to lose ^^
        // after test, team 2 have almost 200 more points , with 10 000 iterations ;
        //which is not  great at all , but maybe a proof mcts does something
        Map<PlayerId, Player> players = new HashMap<>();
        Map<PlayerId, String> playerNames = new HashMap<>();
        //marche pour n'importe quelle seed
//        for (PlayerId pId: PlayerId.ALL) {
//            Player player = new MctsPlayer(pId, 2019, 100);
//            if (pId == PlayerId.PLAYER_1) {
//                player = new PrintingPlayer(player);
//            }
//
//            players.put(pId, player);
//            playerNames.put(pId, pId.name());
//        }
        
        //I expect the team 1 to lose ^^
        // after test, team 2 have almost 200 more points , with 10 000 iterations ;
        //which is not  great at all , but maybe a proof mcts does something
        for (PlayerId pId: PlayerId.ALL) {
          Player player = new PacedPlayer(new RandomPlayer(2019), 1);
          if (pId == PlayerId.PLAYER_3) {
              player = new PrintingPlayer(new mctsPlayerSmart(PlayerId.PLAYER_3, 2019, 100_000));
           }
         if (pId == PlayerId.PLAYER_4) {
             player = new MctsPlayer(PlayerId.PLAYER_4, 2019, 100_000);
          }
         if (pId == PlayerId.PLAYER_2) {
             player = new MctsPlayer(PlayerId.PLAYER_2, 2019, 100_000);
           }
       if (pId == PlayerId.PLAYER_1) {
           player = new mctsPlayerSmart(PlayerId.PLAYER_1, 2019, 100_000);
   }
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