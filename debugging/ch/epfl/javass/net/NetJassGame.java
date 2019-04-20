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
        Player player = null;
        for (PlayerId pId: PlayerId.ALL) {
            
            if(pId ==PlayerId.PLAYER_1) {
           
                    //128.179.142.70
                    try {
                        player = new RemotePlayerClient("localhost", 5108);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                 
                 

            }
            else if(pId ==PlayerId.PLAYER_2) {
                try {
                    //128.179.142.70
                    player = new RemotePlayerClient("128.179.137.194", RemotePlayerClient.PORT_NUMBER);
                 
                 
                                      
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
            else if(pId ==PlayerId.PLAYER_3) {
                try {
                    //128.179.142.70
                    player = new RemotePlayerClient("128.179.190.46", RemotePlayerClient.PORT_NUMBER);
                 
                 
                                      
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
            else {
                try {
                    //128.179.142.70
                    player = new RemotePlayerClient("128.179.144.133", RemotePlayerClient.PORT_NUMBER);
                 
                 
                                      
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
            players.put(pId, player);
            playerNames.put(pId, pId.name());
        }
int winTeam1 =0;
        for(int i =0; i<100 ; ++i) {
            JassGame g = new JassGame(2019, players, playerNames);

        while (!g.isGameOver()) {
            g.advanceToEndOfNextTrick();
            System.out.println("new Trick");
            if(g.isGameOver()) {
                
            }
        }
       
        }
        System.out.println("finish");
    }
}


