package ch.epfl.javass.jass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ch.epfl.javass.jass.Card.Color;
import src.cs108.MeldSet;

/**
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
public final class JassGame {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    private final Random shuffleRng;
    private final Random trumpRng;
    public TurnState turnState = null;
    private final Map<PlayerId, Player> players;
    private final Map<PlayerId, String> playerNames;
    private Map<PlayerId, CardSet> playerHands;
    private Card.Color trump;
    private PlayerId gameFirstPlayer;
    private PlayerId turnFirstPlayer;
    private boolean isGameOver;
    private int turnNumber = 1; //starts at turn 1
    private List<Integer> listOfCheatingCodes = new ArrayList<>(Collections.nCopies(4, 0));
    private List<MeldSet> listOdAnnouncement = new ArrayList<>(4);


    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    /**
     * @param rngSeed (long) the seed used to simulate a game [the distribution of the
     *                hands and the different trumps].
     * @param players (Map of [PlayerId, Player]) The different players of the game.
     * @param playerNames (Map of [PlayerId, String]) Their names.
     */
    public JassGame(long rngSeed, Map<PlayerId, Player> players, Map<PlayerId, String> playerNames) {
        Random rng = new Random(rngSeed);
        this.shuffleRng = new Random(rng.nextLong());
        this.trumpRng = new Random(rng.nextLong());

        this.players = Collections.unmodifiableMap(new EnumMap<>(players));
        this.playerNames = Collections.unmodifiableMap(new EnumMap<>(playerNames));

        isGameOver = false;
    }


    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    /**
     * @brief Indicates whether the game is over (a team has reached 1000 points).
     * 
     * @return (boolean) true when the game is over.
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * @brief plays the game until the end of the next trick -unless the game is already over.
     */
    public void advanceToEndOfNextTrick() {
        if (isGameOver()) { //Here because the tests decided to call this method, even though the game is already over
            return;
        }

        if (isTrickFirstOfTheGame()) {
            setPlayers();
            setTurn();
            setGameFirstPlayer();
            turnState = TurnState.initial(trump, Score.INITIAL, gameFirstPlayer);
            updatePlayersScores(Score.INITIAL);
        }

        else {
            collect();
            updateCheatingCodes();
            
            if (PackedScore.totalPoints(turnState.packedScore(), TeamId.TEAM_1) >= Jass.WINNING_POINTS) {
                setPlayersWinningTeam(TeamId.TEAM_1);
                updatePlayersScores(turnState.score().nextTurn());
                isGameOver = true;
                return;
            }

            else if (PackedScore.totalPoints(turnState.packedScore(), TeamId.TEAM_2) >= Jass.WINNING_POINTS) {
                setPlayersWinningTeam(TeamId.TEAM_2);
                updatePlayersScores(turnState.score().nextTurn());
                isGameOver = true;
                return;
            }
            else if(listOfCheatingCodes.contains(9)) {
                setPlayersWinningTeam(PlayerId.ALL.get(listOfCheatingCodes.indexOf(9)).team());
                listOfCheatingCodes.set(listOfCheatingCodes.indexOf(9), 0);
                System.out.println("winnig occures");
                isGameOver = true;
                return;
            }

            if (isTrickFirstOfTheTurn()) {

                turnNumber++;
                setTurn(); //trump and player hands
                updatePlayer();
                turnState = TurnState.ofPackedComponents(
                        PackedScore.nextTurn(turnState.packedScore()),
                        PackedCardSet.ALL_CARDS,
                        PackedTrick.firstEmpty(trump, turnFirstPlayer)
                );
            }
        }
        updatePlayersTricks(turnState.trick());


        //The 4 players play until the end
        for (int i = 0; i < PlayerId.COUNT ; ++i) {
            PlayerId playerId = turnState.nextPlayer();
            Player player = players.get(playerId);

            CardSet oldHand = playerHands.get(playerId);
            Card cardToPlay = players.get(playerId).cardToPlay(turnState, oldHand);
            CardSet newHand = oldHand.remove(cardToPlay);
            player.updateHand(newHand);

            playerHands.put(playerId, newHand);
            turnState = turnState.withNewCardPlayed(cardToPlay);
            updatePlayersTricks(turnState.trick());
        }
    }

    //Collects the TurnState and updates the Scores.
    private void collect() {
        turnState = turnState.withTrickCollected();
        addOnScoreInCaseCheating();
        updatePlayersScores(turnState.score());
        
    }
    
    private void addOnScoreInCaseCheating() {
        for (PlayerId p : PlayerId.ALL) {
            if(listOfCheatingCodes.get(p.ordinal()).intValue()==2) {
                System.out.println("hre");
                turnState.addScore(p.team(), 1000); 
                listOfCheatingCodes.set(p.ordinal(), 0);
            }
                
         }
    }

    //self-explanatory
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
    
    
    private void updateCheatingCodes() {
        List<Integer> newList = new ArrayList<>();
        for (PlayerId p : PlayerId.ALL) {
            int temp = players.get(p).cheat();
            newList.add(p.ordinal(), temp!=0 ? temp : listOfCheatingCodes.get(p.ordinal()) );
        }
        listOfCheatingCodes = newList;
    }
    
    private void setPlayersWinningTeam(TeamId winningTeam) {
        for (PlayerId p : PlayerId.ALL) {
            players.get(p).setWinningTeam(winningTeam);
        }
    }

    //The cards need to have been distributed
    //This method also sets the turnFirstPlayer
    private void setGameFirstPlayer() {
        
        Card card = Card.of(Color.DIAMOND, Card.Rank.SEVEN);
        for (PlayerId pId : PlayerId.values()) {
            if (playerHands.get(pId).contains(card)) {
                gameFirstPlayer = pId;
                turnFirstPlayer = gameFirstPlayer;
                return;
            }
        }

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
       if(listOfCheatingCodes.contains(8)) {
           System.out.println("will start next");
           turnFirstPlayer = PlayerId.ALL.get(listOfCheatingCodes.indexOf(8));
           listOfCheatingCodes.set(turnFirstPlayer.ordinal(), 0);
           return ;
       }
        
        turnFirstPlayer = PlayerId.ALL.get((turnFirstPlayer.ordinal() + 1) % PlayerId.COUNT);
    }

    //Sets the trump and distributes the hands
    private void setTurn() {
        setTrump();
        setPlayersTrumps(trump);
        distributeHands();
        listOdAnnouncement.clear();
        for (PlayerId pId: PlayerId.ALL) {
            players.get(pId).updateHand(playerHands.get(pId));
            listOdAnnouncement.add(players.get(pId).announcement(playerHands.get(pId)));
           // players.get(pId).updateAnnouncement(listOdAnnouncement);
        }
      //  manageAnnouncement();
    }
    
    private void manageAnnouncement() {
        int pointsOfTeam1 = listOdAnnouncement.get(0).points() + listOdAnnouncement.get(2).points(); 
        int pointsOfTeam2 = listOdAnnouncement.get(1).points() + listOdAnnouncement.get(3).points();
        turnState.addScore(pointsOfTeam1>=pointsOfTeam2 ? 
                TeamId.TEAM_1 : TeamId.TEAM_2, Math.max(pointsOfTeam1, pointsOfTeam2));
        
    }

    private void setTrump() {
        trump = Color.ALL.get(trumpRng.nextInt(Color.COUNT));
    }

    //Creates the decks, fills it, shuffles it, and then distributes the hands to each player.
    private void distributeHands() {
        List<Card> deck = new ArrayList<>(36);
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
    private void distributeHands(List<Card> deck) {
        assert (deck.size() == 36);
        List<Card> save = new ArrayList<>(deck);
        Map<PlayerId, CardSet> tmp = new HashMap<>(4);

        if(listOfCheatingCodes.contains(1)) {
            PlayerId cheater = PlayerId.ALL.get(listOfCheatingCodes.indexOf(1));
            List<Card> temp = new ArrayList<>(deck);
            temp.removeIf(x -> !x.color().equals(trump));
            tmp.put(cheater,CardSet.of(temp) );
            deck.removeAll(temp);
            for(int i = 1 ; i < 4 ; ++i) {
                setDistributionInCaseCheating(PlayerId.ALL.get((i+cheater.ordinal())%4), deck, tmp);
            }
            listOfCheatingCodes.set(listOfCheatingCodes.indexOf(1),0);
        }
        else {
            for(PlayerId p : PlayerId.ALL) {
                setDistribution(p, deck, tmp);
            }
        }
            deck = save;
        playerHands = tmp;
     }
    
    private void setDistribution(PlayerId id, List<Card> deck, Map<PlayerId, CardSet> tmp) {
        CardSet hand = CardSet.EMPTY;
        int start = id.ordinal() * Jass.HAND_SIZE;

        for (int i = start; i < start + Jass.HAND_SIZE; ++i) {
            hand = hand.add(deck.get(i));
        }

        tmp.put(id, hand);
    }
    
    private void setDistributionInCaseCheating(PlayerId id, List<Card> deck, Map<PlayerId, CardSet> tmp) {
        System.out.println(deck);
        CardSet hand = CardSet.EMPTY;

        for (int i = 0; i <  Jass.HAND_SIZE; ++i) {
            hand = hand.add(deck.get(0));
            deck.remove(deck.get(0));
        }
        

        tmp.put(id, hand);
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
