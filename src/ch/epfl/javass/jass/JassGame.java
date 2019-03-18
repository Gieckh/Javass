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
    private TurnState turnState = null;
    private Map<PlayerId, Player> players;
    private Map<PlayerId, String> playerNames;
    private Map<PlayerId, CardSet> playerHands;
    private Card.Color trump;
    private PlayerId gameFirstPlayer;
    private PlayerId turnFirstPlayer;
    private PlayerId trickFirstPlayer;
    private int turnNumber = 1; //starts at turn 1


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
        if (turnState == null) {
            return false;
        }

        if (PackedScore.totalPoints(turnState.packedScore(), TeamId.TEAM_1) >= Jass.WINNING_POINTS) {
            setPlayersWinningTeam(TeamId.TEAM_1);
            return true;
        }

        if (PackedScore.totalPoints(turnState.packedScore(), TeamId.TEAM_2) >= Jass.WINNING_POINTS) {
            setPlayersWinningTeam(TeamId.TEAM_2);
            return true;
        }

        return false;
    }

    /**
     * @brief advance the state of the game until the end of the next trick.
     *
     *
     */
    //TODO: update players.
    public void advanceToEndOfNextTrick() {
        if (isTrickFirstOfTheGame()) {
            setPlayers();
            setTurn();
            setGameFirstPlayer();
            turnState = TurnState.initial(trump, Score.INITIAL, gameFirstPlayer);
        }

        else {
            collect();
            //We do nothing if the game is over.
            if (isGameOver()) {
                //TODO: ?
                return; //nothing
            }

            if (isTrickFirstOfTheTurn()) {
                turnNumber++;
                setTurn(); //trump and player hands
                updatePlayer();
                turnState = TurnState.ofPackedComponents(
                        turnState.packedScore(),
                        turnState.packedUnplayedCards(),
                        PackedTrick.firstEmpty(trump, turnFirstPlayer)
                );
            }
        }

        setTrickFirstPlayer(turnState.packedTrick());
        updatePlayersTricks(turnState.trick());

        //The 4 players play until the end
        for (int i = 0; i < PlayerId.COUNT ; ++i) {
            PlayerId tmpId = PlayerId.ALL.get(((trickFirstPlayer.ordinal() + 1 ) % PlayerId.COUNT));
            Player tmpPlayer = players.get(tmpId);
            CardSet oldHand = playerHands.get(tmpId);
            Card cardToPlay = players.get(tmpId).cardToPlay(turnState, oldHand);
            CardSet newHand = oldHand.remove(cardToPlay);
            tmpPlayer.updateHand(newHand);

            playerHands.put(tmpId, newHand);
            turnState = turnState.withNewCardPlayed(cardToPlay);
            tmpPlayer.updateTrick(turnState.trick());
        }
    }


    private void collect() {
        turnState = turnState.withTrickCollected();
        updatePlayersScores(turnState.score());
    }

    private void setPlayers() {
        for (Map.Entry<PlayerId, Player> entry : players.entrySet()) {
            entry.getValue().setPlayers(entry.getKey(), playerNames);
        }
    }
    private void setPlayersTrumps(Color trump) {
        for (PlayerId p : PlayerId.ALL) {
            players.get(p).setTrump(trump);
        }
    }
    private void updatePlayersTricks(Trick newTrick) {
        for (PlayerId p : PlayerId.ALL) {
            players.get(p).updateTrick(newTrick);
        }
    }
    private void updatePlayersScores(Score newScore) {
        for (PlayerId p : PlayerId.ALL) {
            players.get(p).updateScore(newScore);
        }
    }
    private void setPlayersWinningTeam(TeamId winningTeam) {
        for (PlayerId p : PlayerId.ALL) {
            players.get(p).setWinningTeam(winningTeam);
        }
    }

    private void setTrickFirstPlayer(int pkTrick) {
        trickFirstPlayer = PackedTrick.player(pkTrick, 0);
    }

    //The cards need to have been distributed
    //This method also set the turnFirstPlayer
    private void setGameFirstPlayer() {
        Card card = Card.of(Color.DIAMOND, Card.Rank.SEVEN);
        for (PlayerId pId : PlayerId.values()) {
            if (playerHands.get(pId).contains(card)) {
                gameFirstPlayer = pId;
                turnFirstPlayer = gameFirstPlayer;
                System.out.println("game first player set : " + gameFirstPlayer.toString());
                return;
            }
        }

        System.out.println("game first player not set");
        //theoretically unreachable statement
        assert(false);
    }

    private boolean isTrickFirstOfTheGame() {
        return turnState == null;
    }

    private boolean isTrickFirstOfTheTurn() {
        return turnState.isTerminal();
    }

    //After the first turn, this method is used to update the first player of the turn
    private void updatePlayer() {
        turnFirstPlayer = PlayerId.ALL.get((turnFirstPlayer.ordinal() + 1) % PlayerId.COUNT);
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
    private void setDistribution(PlayerId id, List<Card> deck, Map<PlayerId, CardSet> tmp) {
        //TODO: check refs
        CardSet hand = CardSet.EMPTY;
        int start = id.ordinal() * Jass.HAND_SIZE;
        //TODO: iterator ? //tho not necessary cuz it doesnt happen often
        for (int i = start; i < start + Jass.HAND_SIZE; ++i) {
            hand = hand.add(deck.get(i));
        }

        tmp.put(id, hand);
    }
    //TODO: Iterator
    private void distributeHands(List<Card> deck) {
        assert (deck.size() == 36);

        Map<PlayerId, CardSet> tmp = new HashMap<>();
        setDistribution(PlayerId.PLAYER_1, deck, tmp);
        setDistribution(PlayerId.PLAYER_2, deck, tmp);
        setDistribution(PlayerId.PLAYER_3, deck, tmp);
        setDistribution(PlayerId.PLAYER_4, deck, tmp);

        playerHands = tmp;
    }

    private void setTurn() {
        setTrump();
        setPlayersTrumps(trump);

        distributeHands();
        for (PlayerId pId: PlayerId.ALL) {
            players.get(pId).updateHand(playerHands.get(pId));
        }
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
