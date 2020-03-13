package poker;

import java.util.*;

public class HandEvaluator {
    private static enum Combination {
        HIGH_CARD,
        PAIR,
        TWO_PAIRS,
        THREE_OF_A_KIND,
        STRAIGHT,
        FLUSH,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        STRAIGHT_FLUSH,
        ROYAL_FLUSH;
    }

    private Hand hand;
    private TreeSet<Card> cards;
    private Map<Card.Rank, Integer> ranksCount;
    private Map<Card.Suit, Integer> suitsCount;

    public HandEvaluator setHand(Hand hand) {
        this.hand = hand;
        this.cards = hand.getCards();
        initRanksMap();
        initSuitsMap();
        return this;
    }

    public Combination getCombination() {
        if (isFlush()) {
            if (isStraight()) {
                if (cards.first().getRank() == Card.Rank.ACE) return Combination.ROYAL_FLUSH;
                return Combination.STRAIGHT_FLUSH;
            }
            if (isFourOfAKind()) return Combination.FOUR_OF_A_KIND;
            if (isFullHouse()) return Combination.FULL_HOUSE;
            return Combination.FLUSH;
        }
        if (isFourOfAKind()) return Combination.FOUR_OF_A_KIND;
        if (isFullHouse()) return Combination.FULL_HOUSE;
        if (isStraight()) return Combination.STRAIGHT;
        if (isThreeOfAKind()) return Combination.THREE_OF_A_KIND;
        if (isTwoPairs()) return Combination.TWO_PAIRS;
        if (isPair()) return Combination.PAIR;
        return Combination.HIGH_CARD;
    }

    private void initRanksMap() {
        ranksCount = new LinkedHashMap<>();
        for (Card card : hand.getCards()) {
            if (ranksCount.keySet().contains(card.getRank()))
                ranksCount.put(card.getRank(), ranksCount.get(card.getRank()) + 1);
            else
                ranksCount.put(card.getRank(), 1);
        }
    }

    private void initSuitsMap() {
        suitsCount = new LinkedHashMap<>();
        for (Card card : hand.getCards()) {
            if (suitsCount.keySet().contains(card.getSuit()))
                suitsCount.put(card.getSuit(), suitsCount.get(card.getSuit()) + 1);
            else
                suitsCount.put(card.getSuit(), 1);
        }
    }

    private boolean isPair() {
        return ranksCount.values().size() == cards.size() - 1;
    }

    private boolean isTwoPairs() {
        if (cards.size() < 4) return false;
        int countPairs = 0;
        for (Integer count : ranksCount.values()) {
            if (count == 2) countPairs++;
        }
        return countPairs > 1;
    }

    private boolean isThreeOfAKind() {
        if (cards.size() < 3) return false;
        return ranksCount.containsValue(3);
    }

    private boolean isStraight() {
        if (cards.size() < 5) return false;
        Card.Rank[] ranks = new Card.Rank[ranksCount.size()];
        int i = 0;
        for (Map.Entry<Card.Rank, Integer> entry : ranksCount.entrySet()) {
            ranks[i] = entry.getKey();
            i++;
        }

        if (ranks.length < 5) return false;
        for (i = 0; i < ranks.length - 4; i++) {
            if (ranks[i].value - ranks[i + 1].value != 1) continue;
            if (ranks[i + 1].value - ranks[i + 2].value != 1) continue;
            if (ranks[i + 2].value - ranks[i + 3].value != 1) continue;
            if (ranks[i + 3].value - ranks[i + 4].value != 1) {
                if (ranks[i + 3].value == Card.Rank.TWO.value) return true;
                continue;
            }
            return true;
        }
        return false;
    }

    private boolean isFlush() {
        if (cards.size() < 5) return false;
        for (Integer count : suitsCount.values()) {
            if (count >= 5) return true;
        }
        return false;
    }

    private boolean isFullHouse() {
        if (cards.size() < 5) return false;
        int value = 0;
        for (Integer count : ranksCount.values()) {
            if (count == 2) value += 2;
            else if (count == 3) value += 10;
        }
        return value > 10;
    }

    private boolean isFourOfAKind() {
        if (cards.size() < 4) return false;
        return ranksCount.containsValue(4);
    }


}
