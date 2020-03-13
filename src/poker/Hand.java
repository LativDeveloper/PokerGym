package poker;

import java.util.Arrays;
import java.util.TreeSet;

public class Hand {
    private TreeSet<Card> cards;

    public Hand(Card ... cards) {
        this.cards = (TreeSet<Card>) new TreeSet<Card>().descendingSet();

        this.cards.addAll(Arrays.asList(cards));
    }

    public Hand(String cards) {
        String[] array = cards.split(" ");
        this.cards = (TreeSet<Card>) new TreeSet<Card>().descendingSet();
        for (int i = 0; i < array.length; i++)
            this.cards.add(new Card(array[i]));
    }

    public TreeSet<Card> getCards() {
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
