package ch.epfl.javass.jass;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import ch.epfl.javass.bits.Bits64;

//TODO: access rights

/**
 *  //TODO : this com. sucks
 * manipulates sets of cards of a jass game.
 * 
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 *
 */
public final class PackedCardSet {

    /** =============================================== **/
    /** ===============    ATTRIBUTES    ============== **/
    /** =============================================== **/
    private final static Card.Color[] colors = getAllColors();
    private final static Card.Rank[] ranks = getAllRanks();

    private final static Map<Integer, Integer> pkCardToIndex = pkCardToIndex();
    private final static Map<Integer, Long> pkCardsForTrump = pkCardsToTrumpAbove();

    //TODO: modify. indexToPkCard
    private final static int[] indexToPkCard = indexToPkCard();

    public static final long EMPTY = 0;
    public static final long ALL_CARDS = 0b00000001_11111111_00000001_11111111_00000001_11111111_00000001_11111111L;
    private static final long SPADE_CARDS   = 0b00000000_00000000_00000000_00000000_00000000_00000000_00000001_11111111L;
    private static final long HEART_CARDS   = 0b00000000_00000000_00000000_00000000_00000001_11111111_00000000_00000000L;
    private static final long DIAMOND_CARDS = 0b00000000_00000000_00000001_11111111_00000000_00000000_00000000_00000000L;
    private static final long CLUB_CARDS    = 0b00000001_11111111_00000000_00000000_00000000_00000000_00000000_00000000L;

    private final static int SPADE_COLOR_START = 0;
    private final static int HEART_COLOR_START = 16;
    private final static int DIAMOND_COLOR_START = 32;
    private final static int CLUB_COLOR_START = 48;

    private final static int UNUSED_BITS_START = 9;
    private final static int UNUSED_BITS_SIZE = 7;

    private final static int COLOR_SIZE = 16;
    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    
    //so the class is not instantiable
    private PackedCardSet() {};

    
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    
    /**
     * @brief Indicates whether the given set of packed cards is packed correctly
     *        It is iff the bits b_i of "pkCardSet" such that
     *        <em>9 <= b_i % 16 <= 15</em>cc are zeros [0].
     *
     * @param pkCardSet (long) the set of packed cards we are interested in.
     * @return (boolean) true if "pkCardSet" encodes a valid set of packedCards.
     *
     * @author - Antoine Scardigli (299905)
     */
    public static boolean isValid(long pkCardSet) {
        long mask = Bits64.mask(SPADE_COLOR_START + UNUSED_BITS_START, UNUSED_BITS_SIZE) |
                    Bits64.mask(HEART_COLOR_START + UNUSED_BITS_START, UNUSED_BITS_SIZE) |
                    Bits64.mask(DIAMOND_COLOR_START + UNUSED_BITS_START, UNUSED_BITS_SIZE) |
                    Bits64.mask(CLUB_COLOR_START + UNUSED_BITS_START, UNUSED_BITS_SIZE);

        return (mask & pkCardSet) == 0L;

//       return( todo suppr
//              extract(pkCardSet, SPADE_COLOR_START +UNUSED_BITS_START, UNUSED_BITS_SIZE) == 0 &&
//              extract(pkCardSet, HEART_COLOR_START +UNUSED_BITS_START, UNUSED_BITS_SIZE) == 0 &&
//              extract(pkCardSet, DIAMOND_COLOR_START +UNUSED_BITS_START, UNUSED_BITS_SIZE) == 0 &&
//              extract(pkCardSet, CLUB_COLOR_START +UNUSED_BITS_START, UNUSED_BITS_SIZE) == 0
//       );
    }

    /**
     * @brief returns the set of cards better than the trump card represented by pkCard. 
     *
     * @param pkCard (int)
     * @return (long) the set of cards better than the trump card in the parameters
     *
     * @author - Antoine Scardigli (299905)
     */
    public static long trumpAbove (int pkCard) { //TODO: test
        assert isValid(pkCard);
        return pkCardsForTrump.get(pkCard);
    }

    /**
     * @brief returns the set with the only card represented by pkCard.
     *
     * @param pkCard (int)
     * @return (long) returns the set with the only card in the parameters
     *
     * @author - Marin Nguyen (288260)
     * @author - Antoine Scardigli (299905)
     */
    public static long singleton (int pkCard) {
        assert isValid(pkCard);

        return 1L << index(pkCard);
    }

    /**
     * @brief returns true if and only if the set pkCardSet is empty
     *
     * @param pkCardSet (long)
     * @return (boolean) true if the set of packed cards is empty [i.e. pkCardSet == 00...000]
     *
     * @author - Marin Nguyen (288260)
     * @author - Antoine Scardigli (299905)
     */
    public static boolean isEmpty (long pkCardSet) {
        return pkCardSet == EMPTY;
    }

    /**
     * @brief Indicates how many cards are in the set "pkCardSet"
     *
     * @param pkCardSet (long) the pkCardSet whose size we want to determine
     * @return (int) the number of cards in the set
     *
     * @author - Antoine Scardigli (299905)
     */
    public static int size(long pkCardSet) {
        return Long.bitCount(pkCardSet);
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
    private static int get2(long pkCardSet, int index) { //TODO: more tests
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

    //TODO : benchmark which one is the best.
    public static int get(long pkCardSet, int index) {
        assert (index >= 0  &&  index < Long.bitCount(pkCardSet));
        for (int i = 0 ; i < index ; ++i)
            pkCardSet ^= Long.lowestOneBit(pkCardSet);

        return Long.numberOfTrailingZeros(pkCardSet);
    }

    private static int get3(long pkCardSet, int index) {
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
     * @brief If the packed card "pkCard" is not already in the set "pkCardSet",
     *        this method puts it in [the corresponding bit is shifted from 0 to 1]. //TODO: "shifted" ?
     *        Otherwise, does nothing.
     *
     * @param pkCardSet (long) the set we want to put the packed card in
     * @param pkCard (int) the packed card we want to put in the set
     * @return (long) The previous set, where the bit corresponding to the packed card
     *         'pkCard" is at 1.
     *
     * @author - Marin Nguyen (288260)
     * @author - Antoine Scardigli (299905)
     */
    public static long add(long pkCardSet, int pkCard) {
        return pkCardSet | (1L << index(pkCard));
    }

    /**
     * @brief If the packed card "pkCard" is already in the set "pkCardSet",
     *        this method remove it[the corresponding bit is shifted from 1 to 0]. //TODO: "shifted" ?
     *        Otherwise, does nothing.
     *
     * @param pkCardSet (long) the set we want to remove the packed card from
     * @param pkCard (int) the packed card we want to remove from the set
     * @return (long) The previous set, where the bit corresponding to the packed card
     *         'pkCard" is at 0. [i.e. where we have "removed" "pkCard"]
     *
     * @author - Marin Nguyen (288260)
     * @author - Antoine Scardigli (299905)
     */
    public static long remove(long pkCardSet, int pkCard) {
        return pkCardSet & ~(1L << index(pkCard));
        //We could use the method "difference", but let's not call it unnecessarily
    }

    /**
     * @brief Indicates whether the given set of card [pkCardSet] contains the
     *        given card [pkCard].
     *
     * @param pkCardSet (long)
     * @param pkCard (int)
     * @return (boolean) true if "pkCardSet" contains "pkCard".
     *
     * @author - Marin Nguyen (288260)
     * @author - Antoine Scardigli (299905)
     */
    public static boolean contains(long pkCardSet, int pkCard) {
        long mask = 1L << index(pkCard);
        return (mask & pkCardSet) == mask;
    }

    //WORKS

    //TODO : return change so that newIndex = 1L << oldIndex.
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
     * 
     * @author - Antoine Scardigli (299905)
     */
    
    public static long complement(long pkCardSet) {
        return (~pkCardSet) & ALL_CARDS;
        // return pkCardSet ^ ALL_CARDS;
    }

    /**
     * @brief the union of the two given sets of packed cards.
     *
     * @param pkCardSet1 (long) the first set of packed cards
     * @param pkCardSet2 (long) the second set of packed cards.
     * @return the union of the two given sets of packed cards.
     *
     * @author - Antoine Scardigli (299905)
     */
    public static long union(long pkCardSet1, long pkCardSet2) {
        return pkCardSet1 | pkCardSet2;
    }

    /**
     * @brief the intersection of the two given sets of packed cards.
     *
     * @param pkCardSet1 (long) the first set of packed cards
     * @param pkCardSet2 (long) the second set of packed cards.
     * @return the intersection of the two given sets of packed cards.
     *
     * @author - Antoine Scardigli (299905)
     */
    public static long intersection(long pkCardSet1, long pkCardSet2) {
        return pkCardSet1 & pkCardSet2;
    }

    /**
     * @brief If we interpret a set of packed cards "pkCS" the following way :
     *        pkCS = <em>{</em>b_63 * (2**63), ..., b_0 * (2 ** 0)<em>]</em>, where b_i represents
     *        the i-th bit of pkCS.
     *        Then this method simply return <em>pkCardSet1 \ pkCardSet2</em>
     *
     * @param pkCardSet1 (long) the first set of packed cards
     * @param pkCardSet2 (long) the second set of packed cards.
     * @return (long) the set of packed cards formed by all the cards in the first
     *         set, but not in the second.
     *
     * @author - Marin Nguyen (288260)
     * @author - Antoine Scardigli (299905)
     */
    public static long difference(long pkCardSet1, long pkCardSet2) {
        return pkCardSet1 & ~pkCardSet2;
    }

    /**
     * @brief returns the set with only the remaining cards of the chosen color from a certain set pkCardSet.
     *
     * @param pkCardSet (long)
     * @param color (Color)
     * @return (long) the set with only the cards from a chosen color and that were already in pkCardSet
     *
     * @author - Marin Nguyen (288260)
     * @author - Antoine Scardigli (299905)
     */
    public static long subsetOfColor(long pkCardSet, Card.Color color) {
        switch (color) {
        case SPADE:
            return pkCardSet & SPADE_CARDS;
        case HEART:
            return pkCardSet & HEART_CARDS;
        case DIAMOND:
            return pkCardSet & DIAMOND_CARDS;
        case CLUB:
            return pkCardSet & CLUB_CARDS;
        default: //unreachable statement
            throw new IllegalArgumentException();
        }
    }

    /**
     * @brief returns the string with the cards included in the set pkCardSet.
     * 
     * @param pkCardSet (long)
     * @return a string with the cards in pkCardSet
     *
     * @author - Marin Nguyen (288260)
     * @author - Antoine Scardigli (299905)
     */
    public static String toString(long pkCardSet) { //TODO: technique tout ça
        StringJoiner j = new StringJoiner(",", "{", "}");
        for (int i = 0; i < 64; ++i) {
            long mask = 1L << i;
            if((mask & pkCardSet) == mask) {
                j.add(PackedCard.toString(pkCardToIndex.get(i)));
            }
        }
        return j.toString();
    }



    // -------------------------------------------------------------------------
    // Methods to initialize variables,
    // themselves used to reduce the number of some other methods.

    /**
     *
     * @return
     *
     * @author Michel Schinz
     */
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

    /** TODO TESTESTESTESTEST
     * @brief
     *
     * @return
     *
     * @author - Marin Nguyen (288260)
     */
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

    /** TODO
     * @brief
     *
     * @return
     *
     * @author - Marin Nguyen (288260)
     */
    private static Map<Integer, Integer> pkCardToIndex() {
        HashMap<Integer, Integer> hash = new HashMap<>(ranks.length * COLOR_SIZE);
        for (int j = 0 ; j < colors.length ; ++j) {
            for (int i = 0 ; i < ranks.length ; ++i) {
                
                int pkCard = PackedCard.pack(colors[j], ranks[i]);
                hash.put(pkCard, 16 * j + i);
            }
        }
        return Collections.unmodifiableMap(hash);
    }


    /** TODO
     * @brief
     *
     * @return
     *
     * @author - Marin Nguyen (288260)
     * @author - Antoine Scardigli (299905)
     */
    private static Map<Integer, Long> pkCardsToTrumpAbove() {
        HashMap<Integer, Long> hash = new HashMap<>(ranks.length * COLOR_SIZE);
        Card.Rank[] trumpRanks = getAllRanksInTrumpCase();

        for (Card.Color c : colors) {
            for (Card.Rank r : ranks) {
                int pkCard = PackedCard.pack(c, r);
                long pkCardSet = 0L;
                for (int k = r.trumpOrdinal + 1 ; k < trumpRanks.length ; ++k) {
                    pkCardSet = add(pkCardSet, PackedCard.pack(c, trumpRanks[k]));
                }

                hash.put(pkCard, pkCardSet);
            }
        }

        return Collections.unmodifiableMap(hash);
    }
}





