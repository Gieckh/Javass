package ch.epfl.javass.jass;
import static ch.epfl.javass.bits.Bits64.extract;

import java.util.StringJoiner;

import java.util.Hashtable;
import ch.epfl.javass.bits.Bits32;
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

    private final static Card.Color[] colors = getAllColors();
    private final static Card.Rank[] ranks = getAllRanks();

    private final static Hashtable<Integer, Integer> pkCardToIndex = pkCardToIndex();
    private final static Hashtable<Integer, Long> pkCardsForTrump = pkCardsForTrump();

    private final static int[] indexToPkCard = indexToPkCard();

    public static final long EMPTY = 0;
    static final long ALL_CARDS =  0b0000000111111111000000011111111100000001111111110000000111111111L;
    // TODO trouver a quelle couleur correspond 1St, 2nd , etc
    private final static int SPADE_COLOR_START = 0;
    private final static int HEART_COLOR_START = 16;
    private final static int DIAMOND_COLOR_START = 32;
    private final static int CLUB_COLOR_START = 48;

    private final static int UNUSED_BITS_START = 9;
    private final static int UNUSED_BITS_SIZE = 7;

    private final static int COLOR_SIZE = 16;
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

    // WORKS
    public static boolean isValid(long pkCardSet) {
       return(
              //We want only 0 from the 9th + 16*N to 15 ++ 16*N (N goes from 0 to 3)
              extract(pkCardSet, SPADE_COLOR_START +UNUSED_BITS_START, UNUSED_BITS_SIZE) == 0 &&
              extract(pkCardSet, HEART_COLOR_START +UNUSED_BITS_START, UNUSED_BITS_SIZE) == 0 &&
              extract(pkCardSet, DIAMOND_COLOR_START +UNUSED_BITS_START, UNUSED_BITS_SIZE) == 0 &&
              extract(pkCardSet, CLUB_COLOR_START +UNUSED_BITS_START, UNUSED_BITS_SIZE) == 0
       );
    }
    
    public static long trumpAbove (int pkCard) {
        assert isValid(pkCard);
        return pkCardsForTrump.get(pkCard);
    }
        
    
    public static long singleton ( int pkCard) {
        assert isValid(pkCard);
        int shift = index (pkCard);
        return 1L << shift;
    }

    //WORKS
    public static boolean isEmpty (long pkCardSet) {
        return pkCardSet == 0;
    }
    
    public static int size(long pkCardSet) {
        return Long.bitCount(pkCardSet);
    }
    
    public static int get2(long pkCardSet, int index) {
        int i = (int) Long.lowestOneBit(pkCardSet);
        while (true) {
            for (int j = i; j < 64; ++j) {
                long mask = 1 << j;
                if((mask&pkCardSet) == mask) {
                    i+=1;
                }
                if(((mask&pkCardSet) == mask) && (i==index)){
                    return findPkCard(j);
                }
            }
        }
    }

    public static int get(long pkCardSet, int index) {
        int i = (int)Long.numberOfTrailingZeros(pkCardSet);
        System.out.println("LowestOneBitPos = " + i);
        long mask = 1L << i;

        int nbOfValuesPassed = 0;

        while (nbOfValuesPassed != index) {
            mask <<= 1;
            ++i;
            if ((mask & pkCardSet) == mask) {
                ++nbOfValuesPassed;
            }
        }

        return indexToPkCard[i];
    }


    
    private static int findPkCard(int index) {
        int rank = index % 4 ;
        int color = index % 16;
        return Bits32.pack(color, 2, rank, 4); //TODO: demander au prof quel pack utiliser.
    }
    
    public static long add(long pkCardSet, int pkCard) {
        return pkCardSet |(1L << index(pkCard));
    }
    
    public static long remove(long pkCardSet, int pkCard) {
        return pkCardSet & ~(1L << index(pkCard));
   
    }
    
    public static boolean contains(long pkCardSet, int pkCard) {
        int mask = 1<<index(pkCard);
        return (mask&pkCardSet)==mask;
    }

    //WORKS
    private static int index(int pkCard) {
        return pkCardToIndex.get(pkCard);
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

    public static long difference(long pkCardSet1, long pkCardSet2) {
        return pkCardSet1 & (~pkCardSet1&pkCardSet2);
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
    private static Card.Rank[] getAllRanksInTrumpCase() {
        return new Card.Rank[] {
                Card.Rank.SIX,
                Card.Rank.SEVEN,
                Card.Rank.EIGHT,
                Card.Rank.TEN,
                Card.Rank.QUEEN,
                Card.Rank.KING,
                Card.Rank.ACE,
                Card.Rank.NINE,
                Card.Rank.JACK,

        };
    }

    private static int[] indexToPkCard() {
        int[] tmp = new int[ranks.length * COLOR_SIZE];

        for (int j = 0 ; j < colors.length ; ++j) {
            for (int i = 0 ; i < ranks.length ; ++i) {
                int pkCard = PackedCard.pack(colors[j], ranks[i]);
                tmp[COLOR_SIZE * j + i] = pkCard;
            }
        }

        return tmp;
    }

    private static Hashtable<Integer, Integer> pkCardToIndex() {
        Hashtable<Integer, Integer> hash = new Hashtable<Integer, Integer>();
        for (int j = 0 ; j < colors.length ; ++j) {
            for (int i = 0 ; i < ranks.length ; ++i) {
                
                int pkCard = PackedCard.pack(colors[j], ranks[i]);
                hash.put(pkCard, 16 * j + i);
            }
        }
        return hash;
    }
    
    private static Hashtable<Integer, Long> pkCardsForTrump() {
        Hashtable<Integer, Long> hash = new Hashtable<Integer, Long>();
        Card.Rank[] trumpRanks = getAllRanksInTrumpCase();
        long pkSet = 0L;
        for (int j = 0 ; j < colors.length ; ++j) {
            for (int k = 0 ; k < trumpRanks.length ; ++k) {
                pkSet = 0L;
                for (int i = k+1 ; i < trumpRanks.length ; ++i) {
                    pkSet = add(pkSet,k);
                }
            int pkCardSet = PackedCard.pack(colors[j], ranks[k]);
            hash.put(pkCardSet, pkSet);
            }
        }
    return hash;
    }
}





