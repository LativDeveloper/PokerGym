package poker.bots;

import poker.Card;
import poker.Player;

import java.util.ArrayList;


/**
 * Бот, который делает малую ставку, либо коллирует любую ставку.
 */
public class BetBot extends Player {

    public BetBot(String name, double bankroll) {
        super(name, bankroll);
    }

    @Override
    public void move(ArrayList<Player> players, Player dealer, ArrayList<Card> board) {
        double minBet = getMinBet(players);
        if (minBet > bet) {
            if (10 * 3 >= bankroll) move = "all-in";
            else move = "call";
        } else {
            if (10 * 3 >= bankroll) move = "all-in";
            else move = "bet small";
        }
    }
}