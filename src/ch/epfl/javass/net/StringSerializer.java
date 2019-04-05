package ch.epfl.javass.net;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.StringJoiner;


public final class StringSerializer {

    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    
    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/   
    
    // cannot be instantiated
    private StringSerializer() {};
    
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
     
    /**
     * @Brief returns the string representation of the int in base 16.
     *
     * @param i an int in base 10
     * @return the string representation of the int in base 16
    */
    public  static String serializeInt(int i) {
        return Integer.toUnsignedString(i, 16);
    }

    /**
     * @Brief returns an int in base 10 from the string representation of a base 16 integer.
     *
     * @param s a string of a base 16 integer
     * @return an int in base 10 from the string representation of a base 16 integer
    */
    public static int deserializeInt(String s) {
        return Integer.parseUnsignedInt(s, 16);
    }
    
    /**
     * @Brief returns the string representation of the long in base 16.
     *
     * @param l an long in base 10
     * @return the string representation of the long in base 16
    */
    public static String serializeLong(long l ) {
        return Long.toUnsignedString(l, 16);
    }

    /**
     * @Brief returns a long in base 10 from the string representation of a base 16 long.
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
     * @Brief  Returns a new String composed of copies of the
     * list of strings joined together with the specified char.
     *
     * @param c the char
     * @param s the list of strings
     * @return a new String that is composed of the list of strings
     *         separated by the char
    */
    public static String combine(char  c, String ... s ) {
        return String.join(",", s);
    }
    
    /**
     * @Brief Splits this string around matches of the given.
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
