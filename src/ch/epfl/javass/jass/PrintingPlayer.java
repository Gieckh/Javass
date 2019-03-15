package ch.epfl.javass.jass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    
    public void setPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        System.out.println("Les joueurs sont :  ");
        List<String> a  = playerNames.values();
        for(int i = 0 ; i < a.size() ; ++i) {
            if(a.)
        }
        String name = playerNames.get(ownId);
        System.out.println(name);
    }

    public void updateHand(CardSet newHand) {
        System.out.println("Ma nouvelle main : " + newHand.toString());
    }

    public void setTrump(Card.Color trump) {
        System.out.println("Atout :" + trump.toString() );
    }

    public void updateTrick(Trick newTrick) {
        System.out.println(newTrick.toString());
   }

    public void updateScore(Score score) {
        System.out.println("Scores :" + score.toString());
    }

    public void setWinningTeam(TeamId winningTeam) {
        PlayerId.valueOf(enumType, name)
        TeamId.
    }
  }
