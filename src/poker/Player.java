package poker;

import poker.evaluator.HandEvaluator;

import java.util.ArrayList;

public abstract class Player {
    public double bankroll;
    private String name;
    private Hand hand;
    private Combination combination;

    public Player(String name, double bankroll) {
        this.name = name;
        this.bankroll = bankroll;
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

    public abstract String move(ArrayList<Player> players, int myPos, int dealerPos, double[] bets, ArrayList<Card> board);

    @Override
    public String toString() {
        return name + "(" + bankroll + ")";
    }

    protected double getMinBet(double[] bets) {
        double minBet = 0;
        for (double bet : bets)
            if (bet > minBet) minBet = bet;

        return minBet;
    }
}