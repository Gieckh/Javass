package ch.epfl.javass.jass;

import ch.epfl.javass.bits.Bits32; //TODO c'est assez bizarre ces import
import static ch.epfl.javass.bits.Bits32.extract;

//TODO: handle access rights
//TODO: check where we assert (tests must not be done here)
//TODO: use class preconditions

/**
 * @brief Contains the methods used to manipulate the cards
 *
 * @author - Marin Nguyen (288260)
 * @author - Antoine Scardigli (299905)
 */
public final class PackedCard {
    /** =============================================== **/
    /** ===============    ATTRIBUTES    ============== **/
    /** =============================================== **/
    public final static int INVALID = 0b111111;
    //TODO: public or private.
    private final static int MAX_RANK = 8;
    private final static int CODED_RANK_START = 0;
    private final static int CODED_RANK_SIZE = 4;
    private final static int RANK_SHIFT = 6;

    private final static int CODED_COLOR_SIZE = 2;
    private final static int CODED_COLOR_START = CODED_RANK_SIZE;
    private final static int COLOR_SHIFT = 1;

    private final static int EMPTY_BITS_START = 6;
    private final static int EMPTY_BITS_SIZE = 26;

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/
    private PackedCard() {
        // cuz' we don't want this class to be instantiated.
    }

    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    /**
     * @brief returns true if the pkCard is correctly packed.
     *
     * @param pkCard
     * @return true if the pkCard is correctly packed
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static boolean isValid(int pkCard) {
        return (extract(pkCard, CODED_RANK_START, CODED_RANK_SIZE) <= MAX_RANK &&  //The rank is valid (we don't check color since it can only be valid)
                extract(pkCard, EMPTY_BITS_START, EMPTY_BITS_SIZE) == 0);
    }

    /**
     * @brief packs a card, given its color and rank
     * @param c (Color), the color of the card
     * @param r (Rank), the rank of the card
     * @return (int) the corresponding packed card.
     */
    public static int pack(Card.Color c, Card.Rank r) {
        return Bits32.pack(r.type - RANK_SHIFT, CODED_RANK_SIZE, c.ordinal(), CODED_COLOR_SIZE); //TODO: test
    }

    
    /** returns the color of the card packed, we assert the int is valid
     * @param pkCard
     * @return the color of this card
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static Card.Color color(int pkCard) {
        assert (isValid(pkCard));

        return Card.Color.ALL.get(extract(pkCard, CODED_COLOR_START, CODED_COLOR_SIZE)); //TODO: test again
    }

    /**
     * @brief Gives the rank of a packed card
     * @param pkCard (int), a card coded by an int (i.e. a packed card)
     * @return (Rank) the rank of this card
     */
    public static Card.Rank rank(int pkCard) { //TODO: erase unnecessary comments
        assert isValid(pkCard);

        return Card.Rank.toType(extract(pkCard, CODED_RANK_START, CODED_RANK_SIZE) + RANK_SHIFT);
    }

    /**
     * @brief returns true if the packed card "pkCardL" is stronger than the
     *        packed card "pkCardR", false otherwise (weaker or not comparable).
     *
     * @param trump (Color) the color of the trump. Needed to evaluate the strength of a card
     * @param pkCardL (int) the int encoding the first card
     * @param pkCardR (int) the int encoding the second card
     * @return (boolean) true when pkCardL is better than pkCardR.
     *
     * @author - Marin Nguyen (288260)
     * @author - Antoine Scardigli (299905)
     */
    public static boolean isBetter(Card.Color trump, int pkCardL, int pkCardR) {
        assert (isValid(pkCardL)  &&  isValid(pkCardR));

        Card.Color colorOfL = color(pkCardL);
        Card.Color colorOfR = color(pkCardR);

        Card.Rank rankOfL = rank(pkCardL);
        Card.Rank rankOfR = rank(pkCardR);

        if (colorOfL == trump) { //TODO: "==" works too i think ? and is safer / less greedy
            if (colorOfR == trump) {
                return rankOfL.trumpOrdinal() > rankOfR.trumpOrdinal();
            }

            return true;
        }

        if (colorOfL == colorOfR) {
            return rankOfL.ordinal() > rankOfR.ordinal();
        }

        return false;
    }

    /**
     * @brief return the value (nb of points a card is worth) of the (encoded)
     *        card "pkCard", given the trump "trump".
     *
     * @param trump (Color) the color of the trump
     * @param pkCard (int) the int representing a card
     *
     * @return the value of the (encoded) card "pkCard"
     *
     * @author - Antoine Scardigli (299905)
     * @author - Marin Nguyen (288260)
     */
    public static int points(Card.Color trump, int pkCard) {
        Card.Color color = color(pkCard); 
        Card.Rank rank = rank(pkCard);

        switch(rank) {
            case SIX:
            case SEVEN:
            case EIGHT:
                return 0;
            case NINE:
                return color.equals(trump) ? 14 : 0;
            case TEN:
                return 10;
            case JACK:
                return color.equals(trump) ? 20 : 2;
            case QUEEN:
                return 3;
            case KING:
                return 4;
            case ACE:
                return 11;
            default: throw new IllegalArgumentException();
        }
    }


    /**
     * @brief the (String) representation of the card :
     *        "[its suit]" then "[its rank]", without spaces in between.
     *
     * @param pkCard (int) the int encoding a card
     * @return the (String) representation of the card.
     *
     * @author - Antoine Scardigli (299905)
     * @author - Marin Nguyen (288260)
     */
    public static String toString(int pkCard) {
        return color(pkCard).toString() + rank(pkCard).toString();
    }
}

