package poker;

import java.util.TreeSet;

public class Hand {
    public static final int HIGH_CARD       = 0;
    public static final int ONE_PAIR        = 1;
    public static final int TWO_PAIR        = 2;
    public static final int THREE_OF_A_KIND = 3;
    public static final int STRAIGHT        = 4;
    public static final int FLUSH           = 5;
    public static final int FULL_HOUSE      = 6;
    public static final int FOUR_OF_A_KIND  = 7;
    public static final int STRAIGHT_FLUSH  = 8;
    public static final int ROYAL_FLUSH     = 9;

    private TreeSet<Card> cards;

    public Hand(Card ... cards) {
        this.cards = (TreeSet<Card>) new TreeSet<Card>().descendingSet();

        for (int i = 0; i < cards.length; i++)
            this.cards.add(cards[i]);
    }

    public Hand(String cards) {
        String[] array = cards.split(" ");
        this.cards = (TreeSet<Card>) new TreeSet<Card>().descendingSet();
        for (int i = 0; i < array.length; i++)
            this.cards.add(new Card(array[i]));
    }

    @Override
    public String toString() {
        String str = "";
        for (Card card : cards) {
            str += card + " ";
        }
        return str.substring(0, str.length() - 1);
    }
}
