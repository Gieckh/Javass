package ch.epfl.javass.jass;

public class CardSet {

    private CardSet() {};
    
    public long packed() {
        return 0L;
    }
    
    public boolean isEmpty () {
        return false;
    }
    
    public int size() {
        return 0;
    }
    
    public int get( int index) {
        return 0;
    }
    
    public long add(Card card) {
        return 0L;
    }
    
    public long remove(Card card) {
        return 0L;
    }
    
    public boolean contains(Card card) {
        return false;
    }
    
    public long complement() {
        return 0L;
    }
    
    public long union(CardSet that) {
        return 0L;
    }
    
    public long intersection(CardSet that) {
        return 0L;
    }
    
    public long difference(CardSet that) {
        return 0L;
    }
    public long subsetOfColor(CardSet that) {
        return 0L;
    }
    
    public boolean equals(Object thatO) {
        return false;
    }
    
    public int hashCode() {
        return 0;
    }
    
    public String toString() {
        return "";
    }
}
