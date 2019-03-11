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
    public PacedPlayer(Player underlyingPlayer, double minTime) {
        this.underLyingPlayer = underlyingPlayer;
        this.minTime = minTime;
    }


    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
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

    @Override
    public void setPlayers(PlayerId ownId,
            Map<PlayerId, String> playerNames) {
        underLyingPlayer.setPlayers(ownId, playerNames);
    }

    @Override
    public void updateHand(CardSet newHand) {
        underLyingPlayer.updateHand(newHand);
    }

    @Override
    public void setTrump(Card.Color trump) {
        underLyingPlayer.setTrump(trump);
    }

    @Override
    public void updateTrick(Trick newTrick) {
        underLyingPlayer.updateTrick(newTrick);
    }

    @Override
    public void updateScore(Score score) {
        underLyingPlayer.updateScore(score);
    }

    @Override
    public void setWinningTeam(TeamId winningTeam) {
        underLyingPlayer.setWinningTeam(winningTeam);
    }
}
