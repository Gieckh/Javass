package ch.epfl.javass.net;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class StringSerializer {

    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    
    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/   
    
    // cannot be instantiated
    private StringSerializer() {}
    
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
     
    /**
     * @brief returns the string representation of the integer in base 16.
     *
     * @param i an integer in base 10
     * @return the string representation of the integer in base 16
     */
    public  static String serializeInt(int i) {
        return Integer.toUnsignedString(i, 16);
    }

    /**
     * @brief returns an integer in base 10 from the string representation of a base 16 integer.
     *
     * @param s a string of a base 16 integer
     * @return an integer in base 10 from the string representation of a base 16 integer
     */
    public static int deserializeInt(String s) {
        return Integer.parseUnsignedInt(s, 16);
    }
    
    /**
     * @brief returns the string representation of the long in base 16.
     *
     * @param l an long in base 10
     * @return the string representation of the long in base 16
     */
    public static String serializeLong(long l ) {
        return Long.toUnsignedString(l, 16);
    }

    /**
     * @brief returns a long in base 10 from the string representation of a base 16 long.
     *
     * @param s a string of a base 16 long
     * @return a long in base 10 from the string representation of a base 16 long
     */
    public static long deserializeLong(String s) {
        return Long.parseUnsignedLong(s, 16);
    }
    
    public static String serializeString(String s) {
        return Base64.getEncoder().encodeToString((s.getBytes(StandardCharsets.UTF_8)));
    }
    
    public static String deserializeString(String s) {
        return new String(Base64.getDecoder().decode(s), StandardCharsets.UTF_8);
    }
    
    
    
    /**
     * @brief  Returns a new String composed of copies of the
     * list of strings joined together with the specified char.
     *
     * @param c the char
     * @param s the list of strings
     * @return a new String that is composed of the list of strings
     *         separated by the char
     */
    public static String combine(char c, String... s ) { //TODO: ici la virgule.
        return String.join(Character.toString(c), s);
    }
    
    /**
     * @brief Splits this string around matches of the given.
     *
     * @param s a String
     * @param c a Char
     * @return the array of strings computed by splitting this string
     *          around matches of the given char
    */
    public static String[] split(String s, char c) {
        return s.split(Character.toString(c));
    }
    
}
