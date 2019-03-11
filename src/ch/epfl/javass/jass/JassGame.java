package ch.epfl.javass.jass;

import java.util.Map;
import java.util.Random;

public final class JassGame {
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    private Random shuffleRng;
    private Random trumpRng;

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
        return false;
    }

    public void advanceToEndOfNextTrick() {

    }
}
