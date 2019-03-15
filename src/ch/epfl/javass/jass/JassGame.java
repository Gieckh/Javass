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

    public void advanceToEndOfNextTrick() {
        //We do nothing if the game is over.
        if (isGameOver()) {
            return;
        }

        //if(PackedScore.) { }

//        //turnstate = TurnState.initial( trumpShuffled(shuffleRng).get(0), score, firstPlayer);
//        //cas ou on est au 1er tour
//        if(turnstate == null) {
//            PlayerId firstPlayer ;
//            turnstate = TurnState.initial( trumpRandom(shuffleRng).get(0), Score.INITIAL, firstPlayer);
//        }

    }




    //The empty deck is passed as argument
    //It is then filled and shuffled
    private void handleDeck(List<Card> deck) {
        assert (deck.isEmpty());
        for (Card.Color c : getAllColors()) {
            for (Card.Rank r : getAllRanks()) {
                deck.add(Card.of(c, r));
            }
        }

        Collections.shuffle(deck, shuffleRng);
    }

    private static Card.Color[] getAllColors() {
        return new Card.Color[] {
                Card.Color.SPADE,
                Card.Color.HEART,
                Card.Color.DIAMOND,
                Card.Color.CLUB
        };
    }

    private static Card.Rank[] getAllRanks() {
        return new Card.Rank[] {
                Card.Rank.SIX,
                Card.Rank.SEVEN,
                Card.Rank.EIGHT,
                Card.Rank.NINE,
                Card.Rank.TEN,
                Card.Rank.JACK,
                Card.Rank.QUEEN,
                Card.Rank.KING,
                Card.Rank.ACE,
        };
    }
}
