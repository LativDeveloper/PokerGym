package poker;

public class Card implements Comparable<Card> {
    private Rank rank;
    private Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Card(String card) {
        card = card.trim();
        this.rank = new Rank(card.charAt(0));
        this.suit = new Suit(card.charAt(1));
    }

    @Override
    public String toString() {
        return rank.toString() + suit.toString();
    }

    @Override
    public int compareTo(Card card) {
        int compareByRank = rank.compareTo(card.rank);
        if (compareByRank == 0) return suit.compareTo(card.suit);
        return compareByRank;
    }
}
