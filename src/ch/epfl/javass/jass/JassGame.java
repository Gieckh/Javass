package ch.epfl.javass.jass;

import java.util.*;

import ch.epfl.javass.jass.Card.Color;

/**
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 *
 */
public final class JassGame {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    private Random shuffleRng;
    private Random trumpRng;
    private TurnState turnstate = null ;
    private Map<PlayerId, Player> players;
    private Map<PlayerId, String> playerNames;
    private Map<PlayerId, Long> playerHands;
    private Card.Color trump;
    private PlayerId firstPlayerId;


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


    /**
     * @Brief returns true iff one team has 1000 points or more.
     * 
     * @return true iff one team has 1000 points or more
     *
    */
    public boolean isGameOver() {
        return (turnstate != null) &&
               (
                (PackedScore.totalPoints(turnstate.packedScore(), TeamId.TEAM_1)>=Jass.WINNING_POINTS) ||
                (PackedScore.totalPoints(turnstate.packedScore(), TeamId.TEAM_2)>=Jass.WINNING_POINTS)
               );
    }

    /**
     * @Brief advance the state of the game until the end of the next trick.
     *
     *
    */
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




    //After the first turn, this method is used to update the first player of the turn
    private void updatePlayer() {
        firstPlayerId = PlayerId.ALL.get((firstPlayerId.ordinal() + 1) % PlayerId.COUNT);
    }

    private void setTrump() {
        trump = Color.ALL.get(trumpRng.nextInt(Color.COUNT));
    }

    private void distributeHands() {
        List<Card> deck = new ArrayList<>();
        fillAndShuffleDeck(deck);
        distributeHands(deck);
    }

    //The empty deck is passed as argument
    //It is then filled and shuffled
    private void fillAndShuffleDeck(List<Card> deck) {
        assert (deck.isEmpty());
        for (Card.Color c : getAllColors()) {
            for (Card.Rank r : getAllRanks()) {
                deck.add(Card.of(c, r));
            }
        }

        Collections.shuffle(deck, shuffleRng);
    }

    private void setDistribution(PlayerId id, List<Card> deck,Map<PlayerId, Long> tmp) {
        //TODO: check refs
        CardSet hand = CardSet.EMPTY;
        int start = id.ordinal() * Jass.HAND_SIZE;
        //TODO: iterator ? //tho not necessary cuz it doesnt happen often
        for (int i = start; i < start + Jass.HAND_SIZE; ++i) {
            hand.add(deck.get(i));
        }

        tmp.put(id, hand.packed());
    }
    //TODO: Iterator
    private void distributeHands(List<Card> deck) {
        assert (deck.size() == 36);

        Map<PlayerId, Long> tmp = new HashMap<>();
        setDistribution(PlayerId.PLAYER_1, deck, tmp);
        setDistribution(PlayerId.PLAYER_2, deck, tmp);
        setDistribution(PlayerId.PLAYER_3, deck, tmp);
        setDistribution(PlayerId.PLAYER_4, deck, tmp);

        playerHands = tmp;
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
