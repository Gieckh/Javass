package ch.epfl.javass.jass;

import java.util.Map;

//TODO: see of the implementation is right
public final class PacedPlayer implements Player{
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    private Player underLyingPlayer;
    private double minTime;

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    /**
     * @brief the PacedPlayer acts the same as its underlying player, except he
     *        takes at least "minTime" seconds before deciding which card to play
     * @param underlyingPlayer (Player) - the underlying player, determines the behaviour
     *                         of the PacedPlayer.
     * @param minTime (double) the minimum time (in milliseconds) this PacedPlayer needs
     *                to wait before deciding which card to play.
     */
    public PacedPlayer(Player underlyingPlayer, double minTime) {
        this.underLyingPlayer = underlyingPlayer;
        this.minTime = minTime * 1000;
    }


    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    /**
     * @brief The same method as its parent's, except it takes at least a minimum time before
     *        returning the Card that should be played
     *
     * @see ch.epfl.javass.jass.Player#cardToPlay(ch.epfl.javass.jass.TurnState, ch.epfl.javass.jass.CardSet)
     */
    @Override
    public Card cardToPlay(TurnState state, CardSet hand) {
        long tIni = System.currentTimeMillis();
        Card cardToPlay = underLyingPlayer.cardToPlay(state, hand);
        long timeElapsed = (System.currentTimeMillis() - tIni);

        if (timeElapsed < minTime) {
            try {
                Thread.sleep((long) minTime - timeElapsed);
            } catch (InterruptedException e) { /* ignore */ }
        }
        
        return cardToPlay;
    }



    /* 
     * @see ch.epfl.javass.jass.Player#setPlayers(ch.epfl.javass.jass.PlayerId, java.util.Map)
     */
    @Override
    public void setPlayers(PlayerId ownId,
            Map<PlayerId, String> playerNames) {
        underLyingPlayer.setPlayers(ownId, playerNames);
    }

    /*
     * @see ch.epfl.javass.jass.Player#updateHand(ch.epfl.javass.jass.CardSet)
     */
    @Override
    public void updateHand(CardSet newHand) {
        underLyingPlayer.updateHand(newHand);
    }

    /* 
     * @see ch.epfl.javass.jass.Player#setTrump(ch.epfl.javass.jass.Card.Color)
     */
    @Override
    public void setTrump(Card.Color trump) {
        underLyingPlayer.setTrump(trump);
    }

    /*
     * @see ch.epfl.javass.jass.Player#updateTrick(ch.epfl.javass.jass.Trick)
     */
    @Override
    public void updateTrick(Trick newTrick) {
        underLyingPlayer.updateTrick(newTrick);
    }

    /* 
     * @see ch.epfl.javass.jass.Player#updateScore(ch.epfl.javass.jass.Score)
     */
    @Override
    public void updateScore(Score score) {
        underLyingPlayer.updateScore(score);
    }

    /* 
     * @see ch.epfl.javass.jass.Player#setWinningTeam(ch.epfl.javass.jass.TeamId)
     */
    @Override
    public void setWinningTeam(TeamId winningTeam) {
        underLyingPlayer.setWinningTeam(winningTeam);
    }
}
