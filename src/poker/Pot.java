package poker;

import java.util.ArrayList;

public class Pot {
    private ArrayList<Player> players;

    public void init(ArrayList<Player> players) {
        this.players = (ArrayList<Player>) players.clone();
    }

    public void distribute(ArrayList<Player> livePlayers) {
        double pot = 0;
//        for (Player player : players) {
//            player.result = -player.invested;
//        }

        while (players.size() > 1) {
            double minStack = players.get(0).invested;
            for (Player player : players) {
                if (player.invested < minStack) minStack = player.invested;
            }
            pot += minStack * players.size();
            for (Player player : players) {
                player.invested -= minStack;
            }

            Combination maxComb = livePlayers.get(0).getCombination();
            for (Player player : livePlayers) {
//                if (!livePlayers.contains(player)) continue;
                Combination comb = player.getCombination();
                if (comb.value > maxComb.value) maxComb = comb;
            }

            ArrayList<Player> winners = new ArrayList<>();
            for (Player player : livePlayers) {
//                if (!livePlayers.contains(player)) continue;
                if (player.getCombination().value == maxComb.value) winners.add(player);
            }
            for (Player player : winners) {
                player.result += pot / winners.size();
            }
            players.removeIf(player -> player.invested <= 0);
            pot = 0;
        }

        if (players.size() == 1) {
            Player player = players.get(0);
            player.result += player.invested;
            player.invested = 0;
        }
    }
}
