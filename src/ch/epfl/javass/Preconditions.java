package ch.epfl.javass;

public final class Preconditions {
    private Preconditions() {}
    // ...méthodes

    public static void checkArgument(boolean b) {
        if (!b) {
            throw new IllegalArgumentException();
        }
        // ne fait rien sinon
    }

    public static int checkIndex(int index, int size) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        // S'il n'y a pas d'erreur
        return index;
    }
}
