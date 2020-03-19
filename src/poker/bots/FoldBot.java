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
    public void move(ArrayList<Player> players, Player dealer, ArrayList<Card> board) {
        double minBet = getMinBet(players);
        if (minBet == 0 || bet == minBet) move = "check";
        else move = "fold";
    }
}
