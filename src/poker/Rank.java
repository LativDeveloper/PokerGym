package poker;

public class Rank {
    public static final char ACE    = 'A';
    public static final char TWO    = '2';
    public static final char THREE  = '3';
    public static final char FOUR   = '4';
    public static final char FIVE   = '5';
    public static final char SIX    = '6';
    public static final char SEVEN  = '7';
    public static final char EIGHT  = '8';
    public static final char NINE   = '9';
    public static final char TEN    = 'T';
    public static final char JACK   = 'J';
    public static final char QUEEN  = 'Q';
    public static final char KING   ='K';

    private int value;


    public Rank(char symbol) {
        switch (symbol) {
            case TWO:
                value = 0;
                break;
            case THREE:
                value = 1;
                break;
            case FOUR:
                value = 2;
                break;
            case FIVE:
                value = 3;
                break;
            case SIX:
                value = 4;
                break;
            case SEVEN:
                value = 5;
                break;
            case EIGHT:
                value = 6;
                break;
            case NINE:
                value = 7;
                break;
            case 'T':
            case 't':
                value = 8;
                break;
            case 'J':
            case 'j':
                value = 9;
                break;
            case 'Q':
            case 'q':
                value = 10;
                break;
            case 'K':
            case 'k':
                value = 11;
                break;
            case 'A':
            case 'a':
                value = 12;
                break;
            default:
                value = -1;
        }
    }
}