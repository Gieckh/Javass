package ch.epfl.javass.net;
import java.io.IOException;
import ch.epfl.javass.jass.MctsPlayer;
import ch.epfl.javass.jass.Player;
import ch.epfl.javass.jass.PlayerId;

/**
 * test thus non Jdoc 
 * 
 * IMPORTANT : LANCER D'ABORD servernetgame PUIS netjassgame
 *
 *
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
public class serverNetGame {
    public static void main(String[] args) throws IOException {
        Player player =new MctsPlayer(PlayerId.PLAYER_2, 2019, 100_000);
        RemotePlayerServer gali =  new RemotePlayerServer(player,5108);
        gali.run();
        System.out.println("on devrait pas arriver la");
    }
}
