package ch.epfl.javass.jass;

import java.util.Random;

import ch.epfl.javass.Preconditions;

public final class MctsPlayer implements Player {

    
    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    
    int iterations;
    Random rng ;
    PlayerId ownId;
    
    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    
    public MctsPlayer(PlayerId ownId, long rngSeed, int iterations){
        this.rng = new Random(rngSeed);
        this.ownId = ownId;
        Preconditions.checkArgument(iterations>=9);
        this.iterations = iterations;
    }
    
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    

    @Override
    public Card cardToPlay(TurnState state, CardSet hand) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
