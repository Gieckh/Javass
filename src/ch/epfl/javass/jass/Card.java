package ch.epfl.javass.jass;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ch.epfl.javass.jass.PackedCard.*;
import static ch.epfl.javass.Preconditions.*;

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
     * @author - Marin Nguyen (288260)
     * @author - Antoine Scardigli (299905)
     */
    private Card(int packed) {
        packedCard = packed; //int so no need to copy ? //TODO: suppr
    }



    //TODO : J-DOC
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
         * returns the color.
         *
         * @param number
         * @return color
         * @author Antoine Scardigli - (299905)
         * @author Marin Nguyen - (288260)
         */
        public static Color toType(int number) {
            // for all the switches which returns something for any value, it is not necessary to use break
            switch (number) {
            case 1:
                return SPADE;
            case 2:
                return HEART;
            case 3:
                return DIAMOND;
            case 4:
                return CLUB;
            default:
                throw new IllegalArgumentException("this input (" + number + ") doesn't correspond to a color");
            }
        }

        /**
         * returns the character corresponding to the rank of the card
         *
         * @throws IllegalArgumentException
         */
        @Override public String toString() throws IllegalArgumentException {
            switch (this) {
            case SPADE:
                return "\u2660";
            case HEART:
                //2661
                //2665
                return "\u2661";
            case DIAMOND:
                //2662
                //2666
                return "\u2662";
            case CLUB:
                return "\u2663";
            default:
                throw new IllegalArgumentException(
                        "this thing does not correspond to a color");
            }
        }

    }

    /**
     * manages the ranks of the cards.
     *
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
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

        /** todo
         * @brief Creates a new variable of type Rank
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

        /** todo
         * @brief gives the rank corresponding to the number
         *
         * @param number
         * @return Rank
         * @author Antoine Scardigli - (299905)
         * @author Marin Nguyen - (288260)
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
                //TODO: what to put there?
                throw new IllegalArgumentException("this input (" + number + ") doesn't correspond to a rank");
            }
        }

        /**
         * @return (int) the position of the trump card which has the rank "this"
         *         in the list of trump cards, based on their strength //TODO: better
         *
         * @author Antoine Scardigli - (299905)
         * @author Marin Nguyen - (288260)
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
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
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
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public static Card ofPacked(int packed) throws IllegalArgumentException {
        checkArgument(isValid(packed));
        return new Card(packed);
    }


    /**
     * @brief returns the packedCard corresponding to this card.
     * 
     * @return (int) the packedCard of this card
     * 
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
    */
    public int packed() {
        return packedCard;
    }

    /** returns the color of the card .
     * 
     * @return the color of this card
     * 
     * @author Antoine Scardigli - (299905)
     * @author Marin Nguyen - (288260)
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
     * @author - Marin Nguyen (288260)
     * @author - Antoine Scardigli (299905)
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
     * @author - Antoine Scardigli (299905)
     * @author - Marin Nguyen (288260)
     */
    public int points(Color trump) {
        return PackedCard.points(trump, packedCard);
    }


    //TODO
    @Override
    public boolean equals(Object thatO) {
        if (thatO == null  ||  !(thatO instanceof Card)) { //todo: test
            return false;
        }

        Card thatOCard = (Card) thatO; // Or do 2 "conversions, idk"
        return (thatOCard.color() == this.color()  &&  thatOCard.rank() == this.rank());
    }

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
     * @author - Antoine Scardigli (299905)
     * @author - Marin Nguyen (288260)
     */
    @Override
    public String toString() {
        return PackedCard.toString(packedCard);
    }
}
