package poker;

public class Suit {
    public static final int CLUBS       = 0;
    public static final int DIAMONDS    = 1;
    public static final int HEARTS      = 2;
    public static final int SPADES      = 3;

    private int value = 0;


    public Suit(char symbol) {
        switch (symbol) {
            case 'c':
            case 'C':
                value = CLUBS;
                return;
            case 'd':
            case 'D':
                value = DIAMONDS;
                return;
            case 'h':
            case 'H':
                value = HEARTS;
                return;
            case 's':
            case 'S':
                value = SPADES;
                return;
            default:
                value = -1;
        }
    }
}
