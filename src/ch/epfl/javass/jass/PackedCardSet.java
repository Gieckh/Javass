package ch.epfl.javass.jass;
import static ch.epfl.javass.bits.Bits64.extract;

import java.beans.Transient;
import java.util.StringJoiner;

import java.util.Hashtable;
import ch.epfl.javass.bits.Bits32;
import ch.epfl.javass.jass.Card.Color;
import ch.epfl.javass.jass.Card.Rank;

//TODO signatureCheck

/**
 *  //TODO : this com. sucks
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
        
    
    public static long singleton (int pkCard) {
        assert isValid(pkCard);

        return 1L << index(pkCard);
    }

    //WORKS
    public static boolean isEmpty (long pkCardSet) {
        return pkCardSet == 0;
    }
    
    public static int size(long pkCardSet) {
        return Long.bitCount(pkCardSet);
    }

    //TODO check which one is the best. Probably not the "2"
    public static int get2(long pkCardSet, int index) {
        assert (size(pkCardSet) >= index  &&  index >= 0);

        int i = (int) Long.numberOfTrailingZeros(pkCardSet);
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

    /**
     * @brief returns the index-th packed card from the given set of packed cards.
     *        if [index == 0], then the card given by the least significant 1-bit
     *        of the set of cards is returned
     *
     * @param pkCardSet (long) a packed set of card
     * @param index (int) //TODO
     * @return (int) the index-th <em>packed card</em> from the given set of packed cards.
     *
     * @author - Marin Nguyen (288260)
     * @author - Antoine Scardigli (299905)
     */
    public static int get(long pkCardSet, int index) { //TODO: more tests
        assert (index >= 0  &&  index < Long.bitCount(pkCardSet));
        //int i = Long.numberOfTrailingZeros(pkCardSet);
        int i = 0;
        for (int ind = 0 ; ind <= index ; ++ind) {
            int nbOfTrailingZerosBis = Long.numberOfTrailingZeros(pkCardSet) + 1;
            i += nbOfTrailingZerosBis;
            pkCardSet >>= nbOfTrailingZerosBis;
        }

        return indexToPkCard[i - 1];
    }

    
    public static long add(long pkCardSet, int pkCard) {
        return pkCardSet |(1L << index(pkCard));
    }
    
    public static long remove(long pkCardSet, int pkCard) {
        return pkCardSet & ~(1L << index(pkCard));
    }

    /**
     * @brief Indicates whether the given set of card [pkCardSet] contains the
     *        given card [pkCard].
     *
     * @param pkCardSet (long)
     * @param pkCard (int)
     * @return (boolean) true if "pkCardSet" contains "pkCard".
     */
    public static boolean contains(long pkCardSet, int pkCard) {
        long mask = 1L << index(pkCard);
        return (mask & pkCardSet) == mask;
    }

    //WORKS

    /**
     * @brief Given a packed card, return its index in the (long) encoding any
     *        set of cards
     *
     * @param pkCard (int) the packed card whose index we're looking for
     * @return (int) the index of this packed card in the (long) encoding any
     *         set of cards.
     */
    private static int index(int pkCard) {
        return pkCardToIndex.get(pkCard);
    }

    /**
     * @brief The complement of the given set of packed cards.
     *
     * @param pkCardSet (long) a set of packed cards.
     * @return (long) the complement of "pkCardSet".
     */
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



    // Methods to initialize variables, used to reduce the number of operations.
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





