package ch.epfl.javass.jass;

import static ch.epfl.javass.Preconditions.checkArgument;
import static ch.epfl.javass.jass.PackedCard.pack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//TODO: check access rights
//TODO: use class preconditions
/**
 * The class used to represent a card.
 * 
 * @author Antoine Scardigli - (299905)
 * @author Marin Nguyen - (288260)
 *
 */
public final class Card {
    /** =============================================== **/
    /** ===============    ATTRIBUTES    ============== **/
    /** =============================================== **/
    private final int packedCard;

    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/

    /**
     * @brief PRIVATE constructor of the class card. Invoked by method //TODO: "invoked" ?
     *        "of(Color c, Rank r)" and "ofPacked(int packed)".
     *
     * @param packed (int) an encoded card
     *
     */
    private Card(int packed) {
        packedCard = packed; //int so no need to copy ? //TODO: suppr
    }



    

    /**
     *  @Brief manages the colors of the cards.
     *
     */
    public enum Color {
        SPADE  (1), //pique
        HEART  (2), //coeur
        DIAMOND(3), //carreau
        CLUB   (4); //trèfle

        public final int type;
        public final static int COUNT = 4;
        public final static List<Color> ALL =
          Collections.unmodifiableList(Arrays.asList(values()));

        Color(int type) {
            this.type = type;
        }


        /**
         * @Brief returns the character corresponding to the rank of the card
         *
         * @throws IllegalArgumentException
         */
        @Override public String toString() throws IllegalArgumentException {
            switch (this) {
            case SPADE:
                return "\u2660";
            case HEART:
                //2661 or 2665 if you want the heart to be full or not
                return "\u2661";
            case DIAMOND:
                //2662 or 2666 if you want the diamond to be full or not
                return "\u2662";
            case CLUB:
                return "\u2663";
            default:
                //should never happen
                throw new IllegalArgumentException(
                        "this thing does not correspond to a color");
            }
        }

    }

    /**
     * @Brief manages the ranks of the cards.
     *
     */
    public enum Rank {
        /** =============================================== **/
        /** ===============    ATTRIBUTES    ============== **/
        /** =============================================== **/

        SIX   (6, 0),
        SEVEN (7, 1),
        EIGHT (8, 2),
        NINE  (9, 7),
        TEN   (10, 3),
        JACK  (11, 8),
        QUEEN (12, 4),
        KING  (13, 5),
        ACE   (14, 6);

        public final int type;
        public final int trumpOrdinal;
        public final static int COUNT = 9;
        public final static List<Rank> ALL =
          Collections.unmodifiableList(Arrays.asList(values()));


        /** ============================================== **/
        /** ==============   CONSTRUCTORS   ============== **/
        /** ============================================== **/

        /** 
         * @brief PRIVATE constructor : Creates a new variable of type Rank.
         *
         * @param type (int) associates the card rank to an int
         * @param trumpOrdinal (int) associates to the card rank its strength, assuming
         *                     it is a trump. for example, the nine of trump color
         *                     beats the ten. Therefore, it has a bigger trumpOrdinal
         *                     (7 > 3).
         */
        private Rank(int type, int trumpOrdinal) {
            this.type = type;
            this.trumpOrdinal = trumpOrdinal;
        }


        /** ============================================== **/
        /** ===============    METHODS    ================ **/
        /** ============================================== **/

        /** 
         * @brief gives the rank corresponding to the int : number.
         *
         * @param number an Int
         * @return Rank
         */
        static Rank toType(int number) {
            switch (number) {
            case 6:
                return SIX;
            case 7:
                return SEVEN;
            case 8:
                return EIGHT;
            case 9:
                return NINE;
            case 10:
                return TEN;
            case 11:
                return JACK;
            case 12:
                return QUEEN;
            case 13:
                return KING;
            case 14:
                return ACE;
            default:
                //should never happen
                throw new IllegalArgumentException("this input (" + number + ") doesn't correspond to a rank");
            }
        }

        /**
         * @return (int) the position of the trump card which has the rank "this"
         *         in the list of trump cards, based on their strength //TODO: better
         *
         */
        public int trumpOrdinal() {
            return trumpOrdinal;
        }

        /**
         * returns the character corresponding to the rank of the card
         */
        @Override public String toString() {
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
     * @brief constructs a new card with the chosen color and rank
     * 
     * @param "c" a color
     * @param "r" a rank
     * 
     * @return a new Card with the chosen color and rank 
    */
    public static Card of(Color c, Rank r) {
        return new Card(pack(c, r));
    }

    /**
     * @brief constructs a new card from the chosen int "packed".
     * 
     * @param packed
     * @return a new Card with the chosen int "packed"
     * @throws IllegalArgumentException
     * 
    */
    public static Card ofPacked(int packed) throws IllegalArgumentException {
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

    /** returns the color of the card .
     * 
     * @return the color of this card
     * 
     */
    public Color color() {
        return PackedCard.color(packedCard);
    }
    
    /**
     * @brief Gives the rank of a card
     * @return (Rank) the rank of this card
     */
    public Rank rank() {
        return PackedCard.rank(packedCard);
    }
    
    /**
     * @brief returns true if the this card is stronger than the
     *        packed card "that", false otherwise (weaker or not comparable).
     *
     * @param trump (Color) the color of the trump. Needed to evaluate the strength of a card
     * @param that (Card) the Card we use to compare
     * @return (boolean) true when this card is better than "that".
     *
     */
    public boolean isBetter(Color trump, Card that) {
        return PackedCard.isBetter(trump, packedCard, that.packed());
    }
    /**
     * @brief return the value (nb of points a card is worth) of this
     *        card , given the trump Color  "trump".
     *
     * @param trump (Color) the color of the trump
     *
     * @return the value of this card 
     *
     */
    public int points(Color trump) {
        return PackedCard.points(trump, packedCard);
    }


    /* 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object thatO) {
        if (thatO == null  ||  !(thatO instanceof Card)) { //todo: test
            return false;
        }

        Card thatOCard = (Card) thatO; // Or do 2 "conversions, idk"
        return (thatOCard.color() == this.color()  &&  thatOCard.rank() == this.rank());
    }

    /* 
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
     *
     */
    @Override
    public String toString() {
        return PackedCard.toString(packedCard);
    }
}
