package poker.bots;

import poker.Card;
import poker.Player;

import java.util.ArrayList;

/**
 * Спокойный бот, который не делает ставки, а только коллирует их.
 */
public class CallBot extends Player {

    public CallBot(String name, double bankroll) {
        super(name, bankroll);
    }

    @Override
    public String move(ArrayList<Player> players, int myPos, int dealerPos, double[] bets, ArrayList<Card> board) {
        double minBet = getMinBet(bets);
        if (minBet == 0 || bets[myPos] == minBet) return "check";
        return "call";
    }
}