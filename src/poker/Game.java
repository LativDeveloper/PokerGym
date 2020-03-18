package poker;

import poker.evaluator.HandEvaluator;

import java.util.*;

public class Game {
    private static final double smallBet = 1.3;
    private static final double medBet = 1.7;
    private static final double bigBet = 2.3;
    private final double bankroll;
    private final double blind;
    private Deck deck;
    private Random random;
    private double minBet;
    private double pot;
    private double prePot;
    private int dealer;
    private int round;
    private int movesInRound;
    private int movePos;
    private ArrayList<Player> roundPlayers;
    private Player[] players;
    private double[] bets;
    private ArrayList<Card> board;

    public Game(Player[] players, double bankroll, double blind) {
        this.bankroll = bankroll;
        this.blind = blind;
        this.deck = new Deck();
        this.random = new Random(0);
        this.players = players;
        this.board = new ArrayList<>();
    }

    public void run() {
        ArrayList<Player> gamePlayers = new ArrayList<>(Arrays.asList(players));
        dealer = random.nextInt(gamePlayers.size());

        while (gamePlayers.size() > 1) {
            this.minBet = 0;
            this.pot = 0;
            this.prePot = 0;
            this.round = 0;
            this.movesInRound = 0;
            this.board.clear();
            this.deck.fillCards();
            this.roundPlayers = new ArrayList<>(gamePlayers);
            this.dealer = (dealer + 1) % roundPlayers.size();
            this.bets = new double[roundPlayers.size()];

            // blinds
            int sbPos = (dealer + 1) % roundPlayers.size();
            int bbPos = (dealer + 2) % roundPlayers.size();
            movePos = (dealer + 3) % roundPlayers.size();
            Player smallBlind = roundPlayers.get(sbPos);
            Player bigBlind = roundPlayers.get(bbPos);

            bet(smallBlind, sbPos, blind / 2);
            bet(bigBlind, bbPos, blind);

            preflop(roundPlayers);

            Player movePlayer = roundPlayers.get(movePos);
            while (roundPlayers.size() > 1) {
                String move = movePlayer.move(roundPlayers, movePos, dealer, bets, board);
                move(movePlayer, movePos, move);
                if (round == 0) {
                    if (isRoundEnd()) {
                        flop();
//                        movePos = (dealer + 1) % roundPlayers.size();
//                        movePlayer = roundPlayers.get(movePos);
//                        continue;
                    }
                } else {
                    if (isRoundEnd()) {
                        if (round == 1) turn();
                        else if (round == 2) river();
                        else {
                            System.out.println("Вскрываемся!");
                            ArrayList<Player> winners = getWinners(roundPlayers, board);
                            for (Player winner : winners)
                                winner.bankroll += pot / winners.size();

                            System.out.println(winners.toString() + " won pot " + pot);
                            for (int i = 0; i < roundPlayers.size(); i++) {
                                Player player = roundPlayers.get(i);
                                if (player.bankroll == 0) {
                                    if (i == dealer) {
                                        dealer--;
                                        if (dealer < 0) dealer = roundPlayers.size();
                                    }
                                    gamePlayers.remove(player);
                                }
                            }
                            break;
                        }
                    }
                }

                movePos = nextMovePos(movePos);
                movePlayer = roundPlayers.get(movePos);
            }
        }

        System.out.println("Игра окончена!");
        System.out.println("Побетилель: " + gamePlayers.get(0));
        System.out.println(players.toString());
    }

    private void bet(Player player, int pos, double count) {
        if (count > player.bankroll) count = player.bankroll;
        if (count > minBet) minBet = count;
        prePot += count;
        player.bankroll -= count;
        bets[pos] += count;
        System.out.println(String.format("%s: bet " + count, player));
    }

    private void move(Player player, int pos, String move) {
        System.out.println(String.format("%s: %s", player, move));
        switch (move) {
            case "call":
                if (bets[pos] == minBet) {
                    System.err.println(String.format("%s ходит неправильно (call, но его ставка уже уравнена)", player));
                    return;
                }
                bet(player, pos, minBet - bets[pos]);
                break;
            case "check":
                break;
            case "bet small":
                double count = minBet * smallBet;
                if (count > player.bankroll) {
                    System.err.println(String.format("%s ходит неправильно (bet small, но фишек столько не имеет)", player));
                    return;
                }
                bet(player, pos, count);
                break;
            case "bet med":
                count = minBet * medBet;
                if (count > player.bankroll) {
                    System.err.println(String.format("%s ходит неправильно (bet med, но фишек столько не имеет)", player));
                    return;
                }
                bet(player, pos, count);
                break;
            case "bet big":
                count = minBet * bigBet;
                if (count > player.bankroll) {
                    System.err.println(String.format("%s ходит неправильно (bet big, но фишек столько не имеет)", player));
                    return;
                }
                bet(player, pos, count);
                break;
            case "all-in":
                if (player.bankroll == 0) {
                    System.err.println(String.format("%s ходит неправильно (all-in, но фишек не имеет", player));
                    return;
                }
                bet(player, pos, player.bankroll);
                break;
        }
        movesInRound++;
    }

    private void preflop(ArrayList<Player> players) {
        for (int i = 0; i < players.size(); i++) {
            Hand hand = new Hand(deck.takeCard(), deck.takeCard());
            players.get(i).setHand(hand);
        }
        System.out.println("Preflop: карты выданы");
    }

    private void flop() {
        round++;
        movesInRound = 0;
        movePos = (dealer + 1) % roundPlayers.size();
        pot += prePot;
        prePot = 0;
        minBet = 0;
        for (int i = 0; i < bets.length; i++)
            bets[i] = 0;

        board.add(deck.takeCard());
        board.add(deck.takeCard());
        board.add(deck.takeCard());
        System.out.println("Flop: " + board.toString());
    }

    private void turn() {
        round++;
        movesInRound = 0;
        movePos = (dealer + 1) % roundPlayers.size();
        pot += prePot;
        prePot = 0;
        minBet = 0;
        for (int i = 0; i < bets.length; i++)
            bets[i] = 0;

        board.add(deck.takeCard());
        System.out.println("Turn: " + board.toString());
    }

    private void river() {
        round++;
        pot += prePot;
        movesInRound = 0;
        movePos = (dealer + 1) % roundPlayers.size();
        prePot = 0;
        minBet = 0;
        for (int i = 0; i < bets.length; i++)
            bets[i] = 0;

        board.add(deck.takeCard());
        System.out.println("River: " + board.toString());
    }

    private ArrayList<Player> getWinners(ArrayList<Player> players, ArrayList<Card> board) {
        Combination[] combinations = new Combination[players.size()];
        for (int i = 0; i < players.size(); i++) {
            combinations[i] = HandEvaluator.evalHand(players.get(i).getHand(), board);
        }

        Combination maxComb = combinations[0];
        for (Combination comb : combinations) {
            if (comb.value > maxComb.value) maxComb = comb;
        }

        ArrayList winners = new ArrayList();
        for (int i = 0; i < players.size(); i++) {
            if (combinations[i].value == maxComb.value) winners.add(players.get(i));
        }
        return winners;
    }

    private int nextMovePos(int movePos) {
        if (movePos >= roundPlayers.size()) movePos = roundPlayers.size() - 1;
        for (int i = (movePos + 1) % roundPlayers.size(); i != movePos; i++)
            if (roundPlayers.get(i).bankroll > 0) return i;
        return -1;
    }

    private boolean isRoundEnd() {
        int playersWithBankroll = 0;
        for (Player player : roundPlayers)
            if (player.bankroll > 0) playersWithBankroll++;
        if (playersWithBankroll > movesInRound) return false;
        for (int i = 0; i < bets.length; i++) {
            if (bets[i] < minBet && roundPlayers.get(i).bankroll > 0) return false;
        }
        return true;
    }
}
