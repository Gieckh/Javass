package ch.epfl.javass.jass;

import static ch.epfl.javass.Preconditions.checkArgument;
import static ch.epfl.javass.jass.PackedCard.pack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The class used to represent a card.
 * 
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 */
public final class Card {
    private final int packedCard;

    /**
     * @brief PRIVATE constructor of the class card. Called by method
     *        "of(Color c, Rank r)" and "ofPacked(int packed)".
     *
     * @param packed (int) an encoded card
     */
    private Card(int packed) {
        packedCard = packed;
    }

    /**
     *  @brief A Card is represented by two things: its color and its rank.
     *         This enumeration is used to represent a Card's Color.
     */
    public enum Color {
        SPADE, //pique
        HEART, //coeur
        DIAMOND, //carreau
        CLUB; //trèfle

        public final static int COUNT = 4;
        public final static List<Color> ALL =
          Collections.unmodifiableList(Arrays.asList(values()));

        /**
         * @brief the character corresponding to the rank of the card
         *
         * @return (String) - the String representation of a Color.
         */
        @Override public String toString() {
            //We decided to use a switch instead of a second attribute because
            //this method should not be called too often
            switch (this) {
                case SPADE:
                    return "\u2660";
                case HEART:
                    //\u2665 if you want the heart to be full
                    return "\u2661";
                case DIAMOND:
                    //\u2666 if you want the diamond to be full
                    return "\u2662";
                case CLUB:
                    return "\u2663";
                default:
                    //unreachable statement
                    throw new Error("not a color");
            }
        }

    }

    /**
     * @brief A Card is represented by two things: its color and its rank.
     *    n   This enumeration is used to represent a Card's Rank.
     */
    public enum Rank {
        SIX   (0),
        SEVEN (1),
        EIGHT (2),
        NINE  (7),
        TEN   (3),
        JACK  (8),
        QUEEN (4),
        KING  (5),
        ACE   (6);

        private final int trumpOrdinal;
        public final static int COUNT = 9;
        public final static List<Rank> ALL =
          Collections.unmodifiableList(Arrays.asList(values()));

        /** 
         * @brief PRIVATE constructor : Creates a new variable of type Rank.
         *
         * @param trumpOrdinal (int) associates to the card rank its strength, assuming
         *                     it is a trump. for example, the nine of trump color
         *                     beats the ten. Therefore, it has a bigger trumpOrdinal
         *                     (7 > 3).
         */
        private Rank(int trumpOrdinal) {
            this.trumpOrdinal = trumpOrdinal;
        }

        /**
         * @brief Indicates the strength of a trump card. The higher the better. (ranges from 0 to 8)
         *
         * @return (int) - the position of the trump card which has the rank "this"
         *         in the ordered list of trump cards, based on their strength
         */
        public int trumpOrdinal() {
            return trumpOrdinal;
        }

        /**
         * @brief the character corresponding to the rank of the card
         *
         * @return (String) - the character corresponding to the rank of the card
         */
        @Override
        public String toString() {
            //We decided to use a switch instead of a third attribute because
            //this method should not be called too often
            switch (this) {
            case SIX:
                return "6";
            case SEVEN:
                return "7";
            case EIGHT:
                return "8";
            case NINE:
                return "9";
            case TEN:
                return "10";
            case JACK:
                return "J";
            case QUEEN:
                return "Q";
            case KING:
                return "K";
            case ACE:
                return "A";
            default:
                throw new IllegalArgumentException(
                        "this thing does not correspond to a rank");
            }
        }
    }


    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/

    /**
     * @brief Constructs a new card with the chosen Color and Rank.
     *
     * @param c (Color) - The given Color
     * @param r (Rank) - The given Rank
     * 
     * @return (Card) - a new Card corresponding to the given Rank and Color.
    */
    public static Card of(Color c, Rank r) {
        return new Card(pack(c, r));
    }

    /**
     * @brief constructs a new card from the specified int "packed".
     * 
     * @param packed (int) - the [packed] card we want to turn into a Card
     * @return a new Card with the chosen int "packed"
     * @throws IllegalArgumentException if the [packed] card given as argument is
     *         not valid.
     */
    public static Card ofPacked(int packed) {
        checkArgument(PackedCard.isValid(packed));
        return new Card(packed);
    }


    /**
     * @brief returns the packedCard corresponding to this card.
     * 
     * @return (int) the packedCard of this card
     * 
     */
    public int packed() {
        return packedCard;
    }

    /**
     * @brief this Card's color
     * 
     * @return (Color) the color of this card
     * 
     */
    public Color color() {
        return PackedCard.color(packedCard);
    }
    
    /**
     * @brief This Card's rank.
     *
     * @return (Rank) the rank of this card
     */
    public Rank rank() {
        return PackedCard.rank(packedCard);
    }
    
    /**
     * @brief returns true if the "this" Card is stronger than the
     *        Card "that", false otherwise (weaker or not comparable).
     *
     * @param trump (Color) - the color of the trump. Needed to evaluate the strength of a card
     * @param that (Card) - the specified Card, to be compared with "this"
     * @return (boolean) - true when "this" is stronger than "that".
     */
    public boolean isBetter(Color trump, Card that) {
        return PackedCard.isBetter(trump, packedCard, that.packed());
    }

    /**
     * @brief The value (nb of points a card is worth) of this
     *        Card , given the trump Color "trump".
     *
     * @param trump (Color) the color of the trump
     *
     * @return (int) the value of the Card "this", given the trump
     *
     */
    public int points(Color trump) {
        return PackedCard.points(trump, packedCard);
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object thatO) {
        if (!(thatO instanceof Card)) { //the call to instanceof handles the case (thatO == null)
            return false;
        }

        Card thatOCard = (Card) thatO;
        return (thatOCard.color() == this.color()  &&  thatOCard.rank() == this.rank());
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return packedCard;
    }
    
    /**
     * @brief the (String) representation of this card :
     *        "[its suit]" then "[its rank]", without spaces in between.
     *
     * @return the (String) representation of the card.
     */
    @Override
    public String toString() {
        return PackedCard.toString(packedCard);
    }
}
