package ch.epfl.javass.jass;
import ch.epfl.javass.bits.Bits64;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @brief Non-instantiable
 *        Contains the methods used to manipulate a CardSet.
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

    public  final static long EMPTY = 0;
    public  final static long ALL_CARDS = 0b00000001_11111111_00000001_11111111_00000001_11111111_00000001_11111111L;
    private final static long SPADE_CARDS   = 0b00000000_00000000_00000000_00000000_00000000_00000000_00000001_11111111L;
    private final static long HEART_CARDS   = 0b00000000_00000000_00000000_00000000_00000001_11111111_00000000_00000000L;
    private final static long DIAMOND_CARDS = 0b00000000_00000000_00000001_11111111_00000000_00000000_00000000_00000000L;
    private final static long CLUB_CARDS    = 0b00000001_11111111_00000000_00000000_00000000_00000000_00000000_00000000L;

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

    //private so this class is not instantiated
    private PackedCardSet() {
        //empty
    }

    
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
    
    /**
     * @brief Indicates whether the given set of packed cards is packed correctly
     *        It is iff the bits b_i of "pkCardSet" such that
     *        <em>9 <= b_i % 16 <= 15</em> are zeros [0] [where b_i represents the i-th bit]
     *
     * @param pkCardSet (long) - the set of packed cards we are interested in.
     * @return (boolean) - true if "pkCardSet" encodes a valid set of PackedCards.
     */
    public static boolean isValid(long pkCardSet) {
        long mask = Bits64.mask(SPADE_COLOR_START + UNUSED_BITS_START, UNUSED_BITS_SIZE) |
                    Bits64.mask(HEART_COLOR_START + UNUSED_BITS_START, UNUSED_BITS_SIZE) |
                    Bits64.mask(DIAMOND_COLOR_START + UNUSED_BITS_START, UNUSED_BITS_SIZE) |
                    Bits64.mask(CLUB_COLOR_START + UNUSED_BITS_START, UNUSED_BITS_SIZE);

        return (mask & pkCardSet) == 0L;
    }

    /**
     * @brief The [packed] set corresponding to all the cards better than
     *        the trump card "pkCard"
     *
     * @param pkCard (int) - a [packed] trump card.
     * @return (long) the [packed] set of all the cards better than "pkCard"
     */
    public static long trumpAbove (int pkCard) {
        return pkCardsForTrump.get(pkCard);
    }

    /**
     * @brief the set of cards composed of only the card "pkCard"
     *
     * @param pkCard (int) - the [packed] card to turn into a [packed] set of cards.
     * @return (long) the set of cards composed of only the card "pkCard"
     */
    public static long singleton (int pkCard) {
        return 1L << index(pkCard);
    }

    /**
     * @brief Returns true iff the (long) "pkCardSet" is empty.
     *
     * @param pkCardSet (long) - the given [packed] set of cards.
     * @return (boolean) true iff the [packed] set of cards is empty [i.e. pkCardSet == 00...000]
     */
    public static boolean isEmpty (long pkCardSet) {
        return pkCardSet == EMPTY;
    }

    /**
     * @brief Indicates how many cards are in the set "pkCardSet"
     *
     * @param pkCardSet (long) - the pkCardSet whose size we want to determine
     * @return (int) the number of cards in the set
     */
    public static int size(long pkCardSet) {
        return Long.bitCount(pkCardSet);
    }

    /**
     * @brief returns the index-th packed card from the given set of packed cards.
     *        if [index == 0], then returns the card given by the least significant 1-bit
     *        of the set of cards.
     *
     * @param pkCardSet (long) - a packed set of card
     * @param index (int) - an int between 0 [included] and size(pkCardSet) [excluded]
     *              which determines which card of the set we want to return
     * @return (int) the index-th <em>packed card</em> from the given set of packed cards.
     */
    public static int get(long pkCardSet, int index) {
        assert (index >= 0  &&  index < Long.bitCount(pkCardSet));
        for (int i = 0 ; i < index ; ++i)
            pkCardSet ^= Long.lowestOneBit(pkCardSet);

        return Long.numberOfTrailingZeros(pkCardSet);
    }

    /**
     * @brief If the packed card "pkCard" is not already in the set "pkCardSet",
     *        this method puts it in [the corresponding bit is shifted from 0 to 1].
     *        Otherwise, does nothing.
     *
     * @param pkCardSet (long) - the set we want to put the packed card in
     * @param pkCard (int) - the packed card we want to put in the set
     * @return (long) The previous set, except the bit corresponding to the packed card
     *         'pkCard" is at 1.
     */
    public static long add(long pkCardSet, int pkCard) {
        return pkCardSet | (1L << index(pkCard));
    }

    /**
     * @brief If the packed card "pkCard" is already in the set "pkCardSet",
     *        this method removes it[the corresponding bit is shifted from 1 to 0].
     *        Otherwise, does nothing.
     *
     * @param pkCardSet (long) - the set we want to remove the packed card from
     * @param pkCard (int) - the packed card we want to remove from the set
     * @return (long) The previous set, except the bit corresponding to the packed card
     *         'pkCard" must be at 0. [i.e. where we have removed "pkCard"]
     */
    public static long remove(long pkCardSet, int pkCard) {
        return pkCardSet & ~(1L << index(pkCard));
    }

    /**
     * @brief Indicates whether the given set of card "pkCardSet" contains the
     *        given card "pkCard".
     *
     * @param pkCardSet (long) - a [packed] set of cards
     * @param pkCard (int) - a [packed] card
     * @return (boolean) true if "pkCardSet" contains "pkCard".
     */
    public static boolean contains(long pkCardSet, int pkCard) {
        long mask = 1L << index(pkCard);
        return (mask & pkCardSet) == mask;
    }


    /**
     * @brief Given a packed card, return its index in the (long) encoding any
     *        set of cards
     *
     * @param pkCard (int) - the packed card whose index we're looking for
     * @return (int) the index of this packed card in the (long) encoding any
     *         set of cards.
     */
    private static int index(int pkCard) {
        return pkCardToIndex.get(pkCard);
    }

    /**
     * @brief The [packed] set of cards made of all the cards not in the given [packed]
     *        set of cards : "pkCardSet"
     *
     * @param pkCardSet (long) - a set of packed cards.
     * @return (long) the <em>valid</em> complement of "pkCardSet".
     */
    
    public static long complement(long pkCardSet) {
        return (~pkCardSet) & ALL_CARDS;
    }

    /**
     * @brief The [packed] set of cards made of all the cards either in
     *        "pkCardSet1" or in "pkCardSet2" [or both].
     *
     * @param pkCardSet1 (long) - the first set of packed cards
     * @param pkCardSet2 (long) - the second set of packed cards.
     * @return the union of the two given sets of packed cards.
     */
    public static long union(long pkCardSet1, long pkCardSet2) {
        return pkCardSet1 | pkCardSet2;
    }

    /**
     * @brief The [packed] set mad of all the cards which are both in
     *        "pkCardSet1" and "pkCardSet2".
     *
     * @param pkCardSet1 (long) - the first set of packed cards
     * @param pkCardSet2 (long) - the second set of packed cards.
     * @return the intersection of the two given sets of packed cards.
     */
    public static long intersection(long pkCardSet1, long pkCardSet2) {
        return pkCardSet1 & pkCardSet2;
    }

    /**
     * @brief the [packed] set of cards containing all the cards which are in
     *        "pkCardSet1" but not in "pkCardSet2".
     *
     * <p>
     *     If we interpret a set of packed cards "pkCS" the following way :
     *     pkCS = <em>{</em>b_63 * (2**63), ..., b_0 * (2 ** 0)<em>]</em>, where b_i represents
     *     the i-th bit of pkCS.
     *     Then this method simply returns <em>pkCardSet1 \ pkCardSet2</em>
     *</p>
     *
     * @param pkCardSet1 (long) - the first set of packed cards
     * @param pkCardSet2 (long) - the second set of packed cards.
     * @return (long) the set of packed cards formed by all the cards in the first
     *         set, but not in the second.
     */
    public static long difference(long pkCardSet1, long pkCardSet2) {
        return pkCardSet1 & ~pkCardSet2;
    }

    /**
     * @brief Given the [packed] set of cards "pkCardSet" and the Color "color",
     *        returns the [packed] set of cards made only of the cards of "pkCardSet"
     *        of Color "color".
     *
     * @param pkCardSet (long) - a [packed] set of cards
     * @param color (Color) - /
     * @return (long) the set with only the cards from a chosen color which were already in "pkCardSet"
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
            throw new Error();
        }
    }

    /**
     * @brief A [packed] set of cards corresponds to a number of Cards [c1, ..., cn].
     *        This method returns the textual representation of the specified "pkCardSet".
     *        It takes the form: "{c1, ..., cn}".
     * 
     * @param pkCardSet (long) - a set of [packed] cards
     * @return (String) the textual representation of "pkCardSet"
     */
    public static String toString(long pkCardSet) {
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
    // themselves used to reduce the length/complexity of some other methods.

    /**
     * @author Michel Schinz (+31252)
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

    /**
     * @brief Creates a map, where the keys are all the possible [packed] cards
     *        and the value associated to a key "pkCard" is its index in a [packed]
     *        set of cards.
     *
     * @return (Map) key : pkCard,
     *               value : its index in a [packed] set of cards
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


    /**
     * @brief Creates a Map, where the keys are all the possible [packed] cards
     *        and the value associated to a key "pkCard" is the [packed] set of all the cards better
     *        than "pkCard" <em>when the trump is the color of pkCard</em>
     *
     * @return (Map) key : pkCard
     *               value : the [packed] set of cards of the better trumps
     */
    private static Map<Integer, Long> pkCardsToTrumpAbove() {
        HashMap<Integer, Long> hash = new HashMap<>(ranks.length * COLOR_SIZE);
        Card.Rank[] trumpRanks = getAllRanksInTrumpCase();

        for (Card.Color c : colors) {
            for (Card.Rank r : ranks) {
                int pkCard = PackedCard.pack(c, r);
                long pkCardSet = 0L;
                for (int k = r.trumpOrdinal() + 1 ; k < trumpRanks.length ; ++k) {
                    pkCardSet = add(pkCardSet, PackedCard.pack(c, trumpRanks[k]));
                }

                hash.put(pkCard, pkCardSet);
            }
        }

        return Collections.unmodifiableMap(hash);
    }
}





