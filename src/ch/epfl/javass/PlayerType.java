package ch.epfl.javass;

public enum PlayerType {
    HUMAN("h"),
    REMOTE("r"),
    SIMULATED_VERY_GOOD("s0"),
    SIMULATED_GOOD("s1"),
    SIMULATED_BAD("s2");

    private String stringRep;

    private PlayerType(String s) {
        this.stringRep = s;
    }

    public static PlayerType toType(String s) {
        switch(s) {
            case "h": return HUMAN;
            case "r": return REMOTE;
            case "s0": return SIMULATED_VERY_GOOD;
            case "s1": return SIMULATED_GOOD;
            case "s2": return SIMULATED_BAD;
            default: throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString() {
        return stringRep;
    }
}
