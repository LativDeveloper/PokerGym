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
        for (Card.Rank rank : Card.Rank.values())
            for (Card.Suit suit : Card.Suit.values())
                cards.add(new Card(rank, suit));
    }

    public Card takeCard() {
        if (cards.size() == 0) {
            System.err.println("Нельзя взять карту! (колода пустая)");
            return null;
        }
        return cards.remove(random.nextInt(cards.size()));
    }
}