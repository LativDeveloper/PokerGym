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
    public void move(ArrayList<Player> players, Player dealer, ArrayList<Card> board) {
        double minBet = getMinBet(players);
        if (minBet == 0 || bet == minBet) move = "check";
        else move = "call";
    }
}