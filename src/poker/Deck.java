package poker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {
    private List<Card> cards;
    private List<Card> takenCards;
    private Random random;

    public Deck() {
        cards = new ArrayList<>();
        takenCards = new ArrayList<>();
        random = new Random();
        for (String str : Card.list.keySet())
            cards.add(new Card(str));
    }

    public void fillCards() {
        for (Card card : takenCards)
                cards.add(card);
        takenCards.clear();
    }

    public Card takeCard() {
        if (cards.size() == 0) {
            System.err.println("Нельзя взять карту! (колода пустая)");
            return null;
        }
        int rand = random.nextInt(cards.size());
        Card card = cards.remove(rand);
        takenCards.add(card);
        return card;
    }
}