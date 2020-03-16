package poker;

import java.util.ArrayList;
import java.util.Arrays;

public class Hand {
    private ArrayList<Card> cards;

    public Hand(Card ... cards) {
        this.cards = new ArrayList<>();

        this.cards.addAll(Arrays.asList(cards));
    }

    public Hand(String ... cards) {
        this.cards = new ArrayList<>();
        for (int i = 0; i < cards.length; i++)
            this.cards.add(new Card(cards[i]));
    }

    public Hand(String cards) {
        this(cards.split(" "));
    }

    public ArrayList<Card> getCards() {
        return cards;
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
