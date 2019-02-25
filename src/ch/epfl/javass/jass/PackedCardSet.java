package ch.epfl.javass.jass;

import static ch.epfl.javass.bits.Bits64.extract;

/**
 * manipulates sets of cards of a jass game.
 * 
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 *
 */
public final class PackedCardSet {
   
    static final long EMPTY = 0;
    static final long ALL_CARDS =  0b0000000111111111000000011111111100000001111111110000000111111111L;
    // TODO trouver a quelle couleur correspond 1St, 2nd , etc
    private final static int SPADE_COLOR_START = 0;
    private final static int HEART_COLOR_START = 16;
    private final static int DIAMOND_COLOR_START = 32;
    private final static int CLUB_COLOR_START = 48;
    private final static int UNUSED_BITS_START = 9;
    private final static int UNUSED_BITS_SIZE = 7;
    //so the class is not instantiable
    private PackedCardSet() {};

    /**
     * returns true if pkCardSet is packed correctly.
     *
     * @param  pkCardSet (long) the long encoding a set of cards
     * @return true if pkCardSet is packed correctly
     *
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    
    //TODO : l'ennoncé ne demande pas à ce que ca soit static ... etrange
    public boolean isValid(long pkCardSet) {

       return(
               //We want only 0 from the 9th + 16*N to 15 ++16*N (N goes from 0 to 3)
              extract(pkCardSet, SPADE_COLOR_START +UNUSED_BITS_START, UNUSED_BITS_SIZE) == 0 &&
              extract(pkCardSet, HEART_COLOR_START +UNUSED_BITS_START, UNUSED_BITS_SIZE) == 0 &&
              extract(pkCardSet, DIAMOND_COLOR_START +UNUSED_BITS_START, UNUSED_BITS_SIZE) == 0 &&
              extract(pkCardSet, CLUB_COLOR_START +UNUSED_BITS_START, UNUSED_BITS_SIZE) == 0);
    }
    
    public long trumpAbove (int pkCard) {
        return ;
    }
    
    public long singleton ( int pkCard) {
        return ;
    }
    
    public boolean isEmpty (long pkCardSet) {
        return pkCardSet == 0;
    }
    
    public int size(long pkCardSet) {
        return Long.bitCount(pkCardSet);
    }
    
    public int get(long pkCardSet, int index) {
        return ;
    }
    
    public long add(long pkCardSet, int pkCard) {
        
    }
    
    public long remove(long pkCardSet, int pkCard) {
        
    }
    
    public boolean contains(long pkCardSet, int pkCard) {
        
    }
    
    public long complement(long pkCardSet) {
        
    }
    
    public long union(long pkCardSet1, long pkCardSet2) {
        
    }
    
    public long intersection(long pkCardSet1, long pkCardSet2) {
        
    }
    
    public long difference(long pkCardSet1, long pkCardSet2) {
        
    }
    public long subsetOfColor(long pkCardSet, Card.Color color) {
        
    }
    
    public String toString(long pkCardSet) {
        
    }
    
    
    




}





