package poker;

public class Card implements Comparable<Card> {
    public static enum Rank {
        TWO('2', 2),
        ACE('A', 14),
        EIGHT('8', 8),
        FIVE('5', 5),
        FOUR('4', 4),
        JACK('J', 11),
        KING('K', 13),
        NINE('9', 9),
        QUEEN('Q', 12),
        SEVEN('7', 7),
        SIX('6', 6),
        TEN('T', 10),
        THREE('3', 3);

        public final char symbol;
        public final int value;
        Rank(char symbol, int value) {
            this.symbol = symbol;
            this.value = value;
        }

    }
    public static enum Suit {
        HEARTS      ('♥', 1),
        CLUBS       ('♣', 2),
        SPADES      ('♠', 3),
        DIAMONDS    ('♦', 4);

        public final char symbol;
        public final int value;
        Suit(char symbol, int value) {
            this.symbol = symbol;
            this.value = value;
        }
    }

    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Card(String cardStr) {
        cardStr = cardStr.trim();
        Suit resultSuit = null;
        Rank resultRank = null;

        char rankSymbol = cardStr.charAt(0);
        for (Rank rank : Rank.values()) {
            if (rank.symbol == rankSymbol){
                resultRank = rank;
                break;
            }
        }

        char suitSymbol = cardStr.charAt(1);
        for (Suit suit : Suit.values()) {
            if (suit.symbol == suitSymbol) {
                resultSuit = suit;
                break;
            }
        }

        this.rank = resultRank;
        this.suit = resultSuit;
        if (resultRank == null || resultSuit == null) {
            System.err.println(String.format("Ошибка создания Card (%s)", cardStr));
        }
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return String.format("%s%s", rank.symbol, suit.symbol);
    }

    @Override
    public int compareTo(Card card) {
        if (rank.value > card.rank.value) return 1;
        if (rank.value < card.rank.value) return -1;

        if (suit.value > card.suit.value) return 1;
        if (suit.value < card.suit.value) return -1;

        return 0;
    }
}
