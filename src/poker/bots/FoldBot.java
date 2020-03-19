package poker.bots;

import poker.Card;
import poker.Player;

import java.util.ArrayList;

/**
 * Бот, который всегда сбрасывает свои карты.
 */
public class FoldBot extends Player {

    public FoldBot(String name, double bankroll) {
        super(name, bankroll);
    }

    @Override
    public String move(ArrayList<Player> players, Player dealer, double[] bets, ArrayList<Card> board) {
        double minBet = getMinBet(bets);
        int myPos = players.indexOf(this);
        if (minBet == 0 || bets[myPos] == minBet) return "check";
        return "fold";
    }
}
