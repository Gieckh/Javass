////package ch.epfl.javass.jass;
////
////import java.util.HashMap;
////import java.util.Map;
//////this is for a test, thus no need of javadoc
////public final class RandomJassGame {
////    public static void main(String[] args) {
////          Map<PlayerId, Player> players = new HashMap<>();
////          Map<PlayerId, String> playerNames = new HashMap<>();
////          //marche pour n'importe quelle seed
////          for (PlayerId pId: PlayerId.ALL) {
////              Player player = new MctsPlayer(pId, 2019, 100);
////              if (pId == PlayerId.PLAYER_1) {
////                  player = new PacedPlayer(new PrintingPlayer(player), 100);
////              }
////
////              players.put(pId, player);
////              playerNames.put(pId, pId.name());
////          }
////
////    //      JassGame g = new JassGame(2039, players, playerNames);
////          JassGame g = new JassGame(2019, players, playerNames);
////          while (!g.isGameOver()) {
////              g.advanceToEndOfNextTrick();
////              System.out.println("----");
////          }
////    }
////}
//package ch.epfl.javass.jass;
//
//import java.util.HashMap;
//import java.util.Map;
////this is for a test, thus no need of javadoc
//public final class RandomJassGame {
//    public static void main(String[] args) {
//        Map<PlayerId, Player> players = new HashMap<>();
//        Map<PlayerId, String> playerNames = new HashMap<>();
//        //marche pour n'importe quelle seed
////        for (PlayerId pId: PlayerId.ALL) {
////            Player player = new MctsPlayer(pId, 2019, 100);
////            if (pId == PlayerId.PLAYER_1) {
////                player = new PrintingPlayer(player);
////            }
////
////            players.put(pId, player);
////            playerNames.put(pId, pId.name());
////        }
//
//        //I expect the team 1 to lose ^^
//        // after test, team 2 have almost 200 more points , with 10 000 iterations ;
//        //which is not  great at all , but maybe a proof mcts does something
//        for (PlayerId pId: PlayerId.ALL) {
//          Player player = new RandomPlayer(2019);
//         if (pId == PlayerId.PLAYER_4 || pId == PlayerId.PLAYER_2) {
//             player = new MctsPlayer(pId, 2019, 100);
//          }
//       if (pId == PlayerId.PLAYER_1) {
//       player = new PrintingPlayer(player);
//   }
//          players.put(pId, player);
//          playerNames.put(pId, pId.name());
//        }
//
//        JassGame g = new JassGame(2019, players, playerNames);
//        while (!g.isGameOver()) {
//            g.advanceToEndOfNextTrick();
//            System.out.println("-------------------------------------------------------------");
//        }
//    }
//}