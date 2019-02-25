package ch.epfl.javass.jass;


import ch.epfl.javass.jass.TeamId;

public abstract class test {
    public static void main(String[] args) {
        long packed = PackedScore.pack(5, 77, 1023,
                                       4, 80, 12);
        System.out.println(PackedScore.toString(packed));
        packed = PackedScore.nextTurn(packed);
        System.out.println(PackedScore.toString(packed));
    }
}
