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
    public void move(ArrayList<Player> players, Player dealer, ArrayList<Card> board) {
        double minBet = getMinBet(players);
        if (10 * 3 >= bankroll) move = "all-in";
        else move = "bet small";
    }
}