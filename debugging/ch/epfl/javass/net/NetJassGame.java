package ch.epfl.javass.net;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.javass.jass.*;
import ch.epfl.javass.net.RemotePlayerClient;
import ch.epfl.javass.net.RemotePlayerServer;

/**
 * test thus non Jdoc
 *
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
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
                    try {
                        player = new RemotePlayerClient("128.179.156.75", RemotePlayerClient.PORT_NUMBER);
                     
                     
                                          
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }
            }
            players.put(pId, player);
            playerNames.put(pId, pId.name());
        }

        JassGame g = new JassGame(2019, players, playerNames);
        while (!g.isGameOver()) {
            g.advanceToEndOfNextTrick();
            //hint  : pour le moment on arrive jamais ici
        }
    }
}


