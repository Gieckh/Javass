package ch.epfl.javass.net;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class StringSerializer {

    /** ============================================== **/
    /** ==============    ATTRIBUTES    ============== **/
    /** ============================================== **/
    
    private static int RADIX = 16;
    
    /** ============================================== **/
    /** ==============   CONSTRUCTORS   ============== **/
    /** ============================================== **/   
    
    // cannot be instantiated
    private StringSerializer() {}
    
    /** ============================================== **/
    /** ===============    METHODS    ================ **/
    /** ============================================== **/
     
    /**
     * @brief returns the string representation, in base RADIX, of the given int.
     *
     * @param i (int) - the {@code int} to convert into a {@code String}
     * @return (String) - the {@code String} representation of the {@code int},
     *         in base RADIX
     */
    public  static String serializeInt(int i) {
        return Integer.toUnsignedString(i, RADIX);
    }

    /**
     * @brief Given an the string representation "<em>s</em>" in base RADIX
     *        of an {@code int}, returns the latter.
     *
     * @param s (String) - represents an {@code int} in base RADIX
     * @return (int) - the corresponding {@code int}
     */
    public static int deserializeInt(String s) {
        return Integer.parseUnsignedInt(s, RADIX);
    }
    
    /**
     * @brief returns the string representation, in base RADIX, of the given long.
     *
     * @param l (long) - the int to convert into a {@code String}
     * @return (String) - the {@code String} representation of the {@code long},
     *      *         in base RADIX
     */
    public static String serializeLong(long l) {
        return Long.toUnsignedString(l, RADIX);
    }

    /**
     * @brief Given an the string representation "<em>s</em>" in base RADIX
     *        of an {@code long}, returns the latter.
     *
     * @param s (String) - represents an {@code long} in base RADIX
     * @return (long) - the corresponding {@code long}
     */
    public static long deserializeLong(String s) {
        return Long.parseUnsignedLong(s, RADIX);
    }

    /**
     * @brief Given the {@code String} "s", serializes its UTF_8 encoding in Base64
     *        and returns this serialization.
     * @see Base64#getEncoder()
     * @see java.util.Base64.Encoder#encodeToString(byte[])
     *
     * @param s ({@code String}) - the {@code String} to serialize
     * @return ({@code String}) - the serialized version of "s"
     */
    public static String serializeString(String s) {
        return Base64.getEncoder().encodeToString((s.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * @brief Given the serialized {@code String} "s", return its deserialization.
     * @see Base64#getDecoder()
     * @see java.util.Base64.Decoder#decode(String)
     *
     * @param s ({@code String}) - a serialized {@code String}.
     * @return ({@code}) - the deserialized version of "s".
     */
    public static String deserializeString(String s) {
        return new String(Base64.getDecoder().decode(s), StandardCharsets.UTF_8);
    }
    
    
    
    /**
     * @brief  Returns a new String composed of copies of the
     *         list of {@code String} joined together with the specified {@code char}.
     * @see String#join(CharSequence, CharSequence...)
     *
     * @param c (char) - the delimiter that separates each {@code String}
     * @param s (String[]) - the Strings to join
     * @return (String) - a new {@code String} that is composed of the Strings
     *                    given by "s" separated by the {@code char} "c"
     */
    public static String combine(char c, String... s ) {
        return String.join(Character.toString(c), s);
    }
    
    /**
     * @brief Splits this string around matches of the given {@code char}.
     * @see String#split(String)
     *
     * @param s (String) - the given [potentially] concatenation of {@code String}
     * @param c (char) - the splitting {@code char}
     *
     * @return (String[]) - the array of {@code String} computed by splitting this string
     *          around matches of the given {@code char}
    */
    public static String[] split(String s, char c) {
        return s.split(Character.toString(c));
    }
    
}
