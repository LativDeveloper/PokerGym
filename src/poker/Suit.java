package poker;

public class Suit {
    public static final char CLUBS       = 'c';
    public static final char DIAMONDS    = 'd';
    public static final char HEARTS      = 'h';
    public static final char SPADES      = 's';

    private char symbol;
    private int value;


    public Suit(char symbol) {
        symbol = Character.toLowerCase(symbol);
        switch (symbol) {
            case CLUBS:
                value = 0;
                break;
            case DIAMONDS:
                value = 1;
                break;
            case HEARTS:
                value = 2;
                break;
            case SPADES:
                value = 3;
                break;
            default:
                value = -1;
        }
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return Character.toString(symbol);
    }
}
