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
        if(turnstate != null) {
            return  (PackedScore.totalPoints(turnstate.packedScore(), TeamId.TEAM_1)>=Jass.WINNING_POINTS) ||
                    (PackedScore.totalPoints(turnstate.packedScore(), TeamId.TEAM_2)>=Jass.WINNING_POINTS);
            }
        return false;
    }
// ne parche pas
    private List<Card> deckShuffled(Random shuffleRng){
        List<Card> deck = new ArrayList<>();
        for(int i = 0 ; i < 0b111111; ++i) {

        }
        return null;
    }
// ne marche pas
    private List<Color> trumpRandom(Random shuffleRng){
        List<Color> trump = new ArrayList<>();
        for(int i = 0 ; i < Color.ALL.size(); ++i) {
            trump.add(Color.ALL.get(i));
        }
        Collections.shuffle(trump, shuffleRng);
        return trump;

    }

    public void advanceToEndOfNextTrick() {
        //cas ou on est au 1er tour
        if(turnstate == null) {
            PlayerId firstPlayer ;
            turnstate = TurnState.initial( trumpRandom(shuffleRng).get(0), Score.INITIAL, firstPlayer);
        }

    }
}
