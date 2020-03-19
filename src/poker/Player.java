package poker;

import poker.evaluator.HandEvaluator;

import java.util.ArrayList;

public abstract class Player {
    public double bankroll;
    public double bet;
    public String move;
    private String name;
    private Hand hand;
    private Combination combination;

    public Player(String name, double bankroll) {
        this.name = name;
        this.bankroll = bankroll;
        this.bet = 0;
        this.move = null;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public Combination getCombination() {
        return combination;
    }

    public void calcCombination(ArrayList<Card> board) {
        combination = HandEvaluator.evalHand(hand, board);
    }

    public void clearCombination() {
        combination = null;
    }

    public abstract void move(ArrayList<Player> players, Player dealer, ArrayList<Card> board);

    @Override
    public String toString() {
        String str = name + "(" + bankroll + ") ";
        if (combination != null) str += combination;
        return str;
    }

    protected double getMinBet(ArrayList<Player> players) {
        double minBet = 0;
        for (Player player : players)
            if (player.bet > minBet) minBet = player.bet;

        return minBet;
    }
}