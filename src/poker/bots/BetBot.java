package poker.bots;

import poker.Card;
import poker.Player;

import java.util.ArrayList;


/**
 * Бот, который всегда делает малую ставку.
 */
public class BetBot extends Player {

    public BetBot(String name, double bankroll) {
        super(name, bankroll);
    }

    @Override
    public String move(ArrayList<Player> players, int myPos, int dealerPos, double[] bets, ArrayList<Card> board) {
        double minBet = getMinBet(bets);
        if (minBet * 1.3 >= bankroll) return "all-in";
        return "bet small";
    }
}