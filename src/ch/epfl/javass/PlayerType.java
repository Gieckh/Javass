package ch.epfl.javass;

public enum PlayerType {
    HUMAN("h"),
    REMOTE("r"),
    SIMULATED("s");

    private String stringRep;

    private PlayerType(String s) {
        this.stringRep = s;
    }

    public static PlayerType toType(String s) {
        switch(s) {
            case "h": return HUMAN;
            case "r": return REMOTE;
            case "s": return SIMULATED;
            default: throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString() {
        return stringRep;
    }
}
