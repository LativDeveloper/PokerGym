package poker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {
    private List<Card> cards;
    private Random random;

    public Deck() {
        cards = new ArrayList<>();
        random = new Random();
        fillCards();
    }

    public void fillCards() {
        cards.clear();
        for (String str : Card.list.keySet())
                cards.add(new Card(str));
    }

    public Card takeCard() {
        if (cards.size() == 0) {
            System.err.println("Нельзя взять карту! (колода пустая)");
            return null;
        }
        return cards.remove(random.nextInt(cards.size()));
    }
}