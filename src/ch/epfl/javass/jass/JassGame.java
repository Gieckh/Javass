package ch.epfl.javass.jass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ch.epfl.javass.jass.Card.Color;

public final class JassGame {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    private Random shuffleRng;
    private Random trumpRng;
    private TurnState turnstate = null ;
    

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    public JassGame(long rngSeed, Map<PlayerId, Player> players, Map<PlayerId, String> playerNames) {
        Random rng = new Random(rngSeed);
        this.shuffleRng = new Random(rng.nextLong());
        this.trumpRng = new Random(rng.nextLong());
        //TODO
    }


    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    public boolean isGameOver() {
        return  (PackedScore.totalPoints(turnstate.packedScore(), TeamId.TEAM_1)>=1000) ||
                (PackedScore.totalPoints(turnstate.packedScore(), TeamId.TEAM_2)>=1000);
    }

    private List<Card> deckShuffled(Random shuffleRng){
        List<Card> deck = new ArrayList<>();
        for(int i = 0 ; i<0b111111; ++i) {
            
        }
    }
    
    private List<Color> trumpShuffled(Random shuffleRng){
        List<Color> trump = new ArrayList<>();
        for(int i = 0 ; i < Color.ALL.size(); ++i) {
            trump.add(Color.ALL.get(i));
        }
        Collections.shuffle(trump, shuffleRng);
        return trump;
        
    }
    
    public void advanceToEndOfNextTrick() {
        if(turnstate == null) {
            
            turnstate = TurnState.initial( trumpShuffled(shuffleRng).get(0), score, firstPlayer);
                    
                    
           
            
        }

    }
}
