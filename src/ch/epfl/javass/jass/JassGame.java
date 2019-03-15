package ch.epfl.javass.jass;

import java.util.*;

import ch.epfl.javass.jass.Card.Color;

public final class JassGame {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    private Random shuffleRng;
    private Random trumpRng;
    private TurnState turnstate = null ;
    private Map<PlayerId, Player> players;
    private Map<PlayerId, String> playerNames;


    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    public JassGame(long rngSeed, Map<PlayerId, Player> players, Map<PlayerId, String> playerNames) {
        Random rng = new Random(rngSeed);
        this.shuffleRng = new Random(rng.nextLong());
        this.trumpRng = new Random(rng.nextLong());

        this.players = Collections.unmodifiableMap(new EnumMap<>(players));
        this.playerNames = Collections.unmodifiableMap(new EnumMap<>(playerNames));
        //TODO
    }


    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/


    public boolean isGameOver() {
        return (turnstate != null) &&
               (
                (PackedScore.totalPoints(turnstate.packedScore(), TeamId.TEAM_1)>=Jass.WINNING_POINTS) ||
                (PackedScore.totalPoints(turnstate.packedScore(), TeamId.TEAM_2)>=Jass.WINNING_POINTS)
               );
    }

    private List<Card> deckShuffled(Random shuffleRng){
        List<Card> deck = new ArrayList<>();
        for(int i = 0 ; i < 0b111111; ++i) {

        }
        return null;
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
        //We do nothing if the game is over.
        if (isGameOver()) {
            return;
        }

        //if(PackedScore.) { }

        //turnstate = TurnState.initial( trumpShuffled(shuffleRng).get(0), score, firstPlayer);
    }
}
