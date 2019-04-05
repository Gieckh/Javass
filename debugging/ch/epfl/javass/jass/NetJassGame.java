package ch.epfl.javass.jass;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.javass.net.RemotePlayerClient;
import ch.epfl.javass.net.RemotePlayerServer;

public class NetJassGame {
   
    public static void main(String[] args)  {
        Map<PlayerId, Player> players = new HashMap<>();
        Map<PlayerId, String> playerNames = new HashMap<>();
        //marche pour n'importe quelle seed
        Player player;
        for (PlayerId pId: PlayerId.ALL) {
            if (pId.team() == TeamId.TEAM_1)
                player = new MctsPlayer(pId, 2019, 10_000);
            else {
                player = new RandomPlayer(2019);
                if(pId ==PlayerId.PLAYER_2) {
                    RemotePlayerServer gali =  new RemotePlayerServer(player);
                    gali.run();
                    System.out.println("42");
                }
            }
                players.put(pId, player);
                playerNames.put(pId, pId.name());
        }

            JassGame g = new JassGame(2019, players, playerNames);
            while (!g.isGameOver()) {
                g.advanceToEndOfNextTrick();
    System.out.println(
            "-------------------------------------------------------------");
            }
        }
    }


