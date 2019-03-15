package ch.epfl.javass.jass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
//this is for a test, thus no need of javadoc
public final class PrintingPlayer implements Player {
    private final Player underlyingPlayer;

    public PrintingPlayer(Player underlyingPlayer) {
      this.underlyingPlayer = underlyingPlayer;
    }

    @Override
    public Card cardToPlay(TurnState state, CardSet hand) {
      System.out.print("C'est à moi de jouer... Je joue : ");
      Card c = underlyingPlayer.cardToPlay(state, hand);
      System.out.println(c);
      return c;
    }
    @Override
    public void setPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        underlyingPlayer.setPlayers(ownId, playerNames);
        System.out.println("Les joueurs sont :  ");
        List<String> a  = (List<String>) (playerNames.values());
        String ownV = playerNames.get(ownId);
        for(int i = 0 ; i < a.size() ; ++i) {
            System.out.println();
            System.out.print(a.get(i));
            if(a.get(i)==ownV) {
                System.out.print("  (moi)");
            }
        }
       
    }
    
    @Override
    public void updateHand(CardSet newHand) {
        underlyingPlayer.updateHand(newHand);
        System.out.println("Ma nouvelle main : " + newHand.toString());
    }
    @Override
    public void setTrump(Card.Color trump) {
        underlyingPlayer.setTrump(trump);
        System.out.println("Atout :" + trump.toString() );
    }
    @Override
    public void updateTrick(Trick newTrick) {
        underlyingPlayer.updateTrick(newTrick);
        System.out.println(newTrick.toString());
   }
    @Override
    public void updateScore(Score score) {
        underlyingPlayer.updateScore(score);
        System.out.println("Scores :" + score.toString());
    }

    @Override
    public void setWinningTeam(TeamId winningTeam) {
        underlyingPlayer.setWinningTeam(winningTeam);
        System.out.println( "L'équipe gagnante est "+ winningTeam.toString());
    }
  }
  
