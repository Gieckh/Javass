package ch.epfl.javass.jass;
import static ch.epfl.javass.bits.Bits64.extract;

import java.util.StringJoiner;

import ch.epfl.javass.jass.Card.Color;
import ch.epfl.javass.jass.Card.Rank;

//TODO faire le signcheck

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
    private final static int COLOR_SIZE= 16;
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
    public static boolean isValid(long pkCardSet) {

       return(
               //We want only 0 from the 9th + 16*N to 15 ++16*N (N goes from 0 to 3)
              extract(pkCardSet, SPADE_COLOR_START +UNUSED_BITS_START, UNUSED_BITS_SIZE) == 0 &&
              extract(pkCardSet, HEART_COLOR_START +UNUSED_BITS_START, UNUSED_BITS_SIZE) == 0 &&
              extract(pkCardSet, DIAMOND_COLOR_START +UNUSED_BITS_START, UNUSED_BITS_SIZE) == 0 &&
              extract(pkCardSet, CLUB_COLOR_START +UNUSED_BITS_START, UNUSED_BITS_SIZE) == 0);
    }
    
    public static long trumpAbove (int pkCard) {
        assert isValid(pkCard);
        Color color = PackedCard.color(pkCard);
        Rank rank = PackedCard.rank(pkCard);
        return 0L;
    }
    
    public static long singleton ( int pkCard) {
        assert isValid(pkCard);
        int shift = index (pkCard);
        return 1<<shift;
    }
    
    public static boolean isEmpty (long pkCardSet) {
        return pkCardSet == 0;
    }
    
    public static int size(long pkCardSet) {
        return Long.bitCount(pkCardSet);
    }
    
    public static int get(long pkCardSet, int index) {
        int lowestBit = (int) Long.lowestOneBit(pkCardSet);
        for(int i = lowestBit ; i < index ; ++i ) {
            long mask = 1<<i;
            if((mask&pkCardSet) == mask) {
                return 0;
            }
            
        }
        return 0;
    }
    
    public static long add(long pkCardSet, int pkCard) {
        return pkCardSet|(1<<index(pkCard));
    }
    
    public static long remove(long pkCardSet, int pkCard) {
      if(contains(pkCardSet, pkCard)){
        return pkCardSet - 1<<index(pkCard);
      }
      else {
          return pkCardSet;
      }
    }
    
    public static boolean contains(long pkCardSet, int pkCard) {
        int mask = 1<<index(pkCard);
        return (mask&pkCardSet)==mask;
    }
    
    private static int index(int pkCard) {
        Color color = PackedCard.color(pkCard);
        Rank rank = PackedCard.rank(pkCard);
        return 16*color.ordinal() + rank.ordinal();
    }
    
    public static long complement(long pkCardSet) {
        return ~pkCardSet;
        
    }
    
    public static long union(long pkCardSet1, long pkCardSet2) {
        return (pkCardSet1 | pkCardSet2);
    }
    
    public static long intersection(long pkCardSet1, long pkCardSet2) {
        return (pkCardSet1 & pkCardSet2);
    }
    //moins ou exlusif je suis vraiement pas sur
    public static long difference(long pkCardSet1, long pkCardSet2) {
        return (pkCardSet1 - (pkCardSet1&pkCardSet2));
    }
    public static long subsetOfColor(long pkCardSet, Card.Color color) {
       return extract(pkCardSet, COLOR_SIZE*color.ordinal(),COLOR_SIZE);
        
    }
    
    public static String toString(long pkCardSet) {
        StringJoiner j = new StringJoiner(",", "{", "}");
        for (int i=0; i<64; ++i) {
            if(1 == extract(pkCardSet, i, 1)) {
                j.add(PackedCard.toString(get(pkCardSet,i)));
            }
        }
        return j.toString();
    }

}





