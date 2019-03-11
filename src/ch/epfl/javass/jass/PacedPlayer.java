package ch.epfl.javass.jass;

//TODO: see of the implementation is right
public final class PacedPlayer implements Player{
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    private Player player;
    private double minTime;

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    public PacedPlayer(Player underlyingPlayer, double minTime) {
        this.player = underlyingPlayer;
        this.minTime = minTime;
    }


    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    @Override
    public Card cardToPlay(TurnState state, CardSet hand) {
        long tIni = System.currentTimeMillis();
        Card cardToPlay = player.cardToPlay(state, hand);
        long timeElapsed = (System.currentTimeMillis() - tIni);
        if (timeElapsed < minTime) {
            try {
                Thread.sleep((long) minTime - timeElapsed);
            } catch (InterruptedException e) { /* ignore */ }
        }
        
        return cardToPlay;
    }
}
