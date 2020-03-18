package poker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Pot {
    private HashMap<Player, Double> chips = new HashMap<>();

    public void add(Player player, double count) {
        if (count > player.bankroll) count = player.bankroll;
        if (chips.containsKey(player)) chips.put(player, chips.get(player) + count);
        else chips.put(player, count);
        player.bankroll -= count;
    }

    public void pay(ArrayList<Player> players) {
        HashMap<Double, ArrayList<Player>> chipsList = new HashMap<>();
        for (double min = getMinChips(); min != 0; min = getMinChips()) {
            ArrayList<Player> list = new ArrayList<>();
            Iterator<Map.Entry<Player, Double>> iterator = chips.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Player, Double> entry = iterator.next();
                if (players.contains(entry.getKey())) list.add(entry.getKey());
                entry.setValue(entry.getValue() - min);
                if (entry.getValue() == 0) iterator.remove();
            }
            chipsList.put(min * list.size(), list);
        }
        Iterator<Map.Entry<Double, ArrayList<Player>>> iterator = chipsList.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Double, ArrayList<Player>> entry = iterator.next();
            ArrayList<Player> winners = getWinners(entry.getValue());
            for (Player winner : winners)
                winner.bankroll += entry.getKey() / winners.size();

            System.out.println(winners.toString() + " won " + entry.getKey() / winners.size());
        }
    }

    private double getMinChips() {
        if (chips.size() == 0) return 0;
        double min = 1.7976931348623157E308;
        for (Map.Entry<Player, Double> entry : chips.entrySet()) {
            if (min > entry.getValue()) min = entry.getValue();
        }
        return min;
    }

    private ArrayList<Player> getWinners(ArrayList<Player> players) {
        Combination maxComb = players.get(0).getCombination();
        for (Player player : players) {
            Combination comb = player.getCombination();
            if (comb.value > maxComb.value) maxComb = comb;
        }

        ArrayList<Player> winners = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getCombination().value == maxComb.value) winners.add(players.get(i));
        }
        return winners;
    }
}
