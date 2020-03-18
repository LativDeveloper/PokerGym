package poker;

import java.util.ArrayList;
import java.util.Arrays;

public class Game {
    private final double bb;
    private final double bankroll;
    private ArrayList<Player> gamePlayers;
    private ArrayList<Player> roundPlayers;
    private Pot pot;
    private Deck deck;
    private ArrayList<Card> board;
    private Player dealerPlayer;
    private Player movePlayer;
    private double minBet;
    private double[] bets;
    private int round;
    private int movesInRound;

    public Game(Player[] players, double bb, double bankroll) {
        this.bb = bb;
        this.bankroll = bankroll;
        gamePlayers = new ArrayList<>(Arrays.asList(players));
        roundPlayers = new ArrayList<>();
        pot = new Pot();
        deck = new Deck();
        board = new ArrayList<>();
        System.out.println(String.format("Создана игра (%s bb/%s) на %s игроков", bb, bankroll, players.length));
    }

    public void run() {
        System.out.println("Игра запущена");
        dealerPlayer = gamePlayers.get((int) (Math.random() * gamePlayers.size()));
        while (gamePlayers.size() > 1) {
            deck.fillCards();
            board.clear();
            roundPlayers.addAll(gamePlayers);
            dealerPlayer = nextDealer();
            minBet = 0;
            round = 0;
            movesInRound = 0;
            bets = new double[roundPlayers.size()];

            doBlinds();
            preflop();

            while (roundPlayers.size() > 1) {
                String move = movePlayer.move(roundPlayers, dealerPlayer, bets, board);
                move(movePlayer, move);
                movePlayer = nextMovePlayer();
                if (isEndRound()) nextRound();
            }




            clearGamePlayers();
        }
        System.out.println("Игра сыграна. Победитель: " + gamePlayers.get(0));
    }

    private Player nextDealer() {
        int i = roundPlayers.indexOf(dealerPlayer);
        if (i == roundPlayers.size() - 1) return roundPlayers.get(0);
        return roundPlayers.get(i + 1);
    }

    private Player nextMovePlayer() {
        int movePos = roundPlayers.indexOf(movePlayer);
        Player nextMovePlayer = null;
        for (int i = (movePos + 1) % roundPlayers.size(); i != movePos; i = (i + 1) % roundPlayers.size()) {
            nextMovePlayer = roundPlayers.get(i);
            if (nextMovePlayer.bankroll > 0) return nextMovePlayer;
        }
        if (movePlayer == nextMovePlayer) return null;
        return nextMovePlayer;
    }

    private void clearGamePlayers() {
        for (int i = 0; i < gamePlayers.size(); i++) {
            Player player = gamePlayers.get(i);
            if (player.bankroll <= 0) {
                gamePlayers.remove(i);
                i--;
                if (player == dealerPlayer) {
                    if (i < 0) dealerPlayer = gamePlayers.get(gamePlayers.size() - 1);
                    else dealerPlayer = gamePlayers.get(i);
                }
                System.out.println(player + " выбыл из игры");
            }
        }
    }

    private void doBlinds() {
        int dealerPos = roundPlayers.indexOf(dealerPlayer);
        Player sbPlayer = roundPlayers.get((dealerPos + 1) % roundPlayers.size());
        Player bbPlayer = roundPlayers.get((dealerPos + 2) % roundPlayers.size());

        move(sbPlayer, "sb");
        move(bbPlayer, "bb");

        movePlayer = roundPlayers.get((dealerPos + 3) % roundPlayers.size());
    }

    private void preflop() {
        for (Player player : roundPlayers) {
            Hand hand = new Hand(deck.takeCard(), deck.takeCard());
            player.setHand(hand);
        }
        System.out.println("Preflop: карты выданы");
    }

    private void bet(Player player, double count) {
        if (count > player.bankroll) count = player.bankroll;
        if (count > minBet) minBet = count;
        pot.add(player, count);
        bets[roundPlayers.indexOf(player)] += count;
//        System.out.println(String.format("%s: bet " + count, player));
    }

    private void move(Player player, String move) {
        int pos = roundPlayers.indexOf(player);
        movesInRound++;
        switch (move) {
            case "sb":
                bet(player, bb / 2);
                movesInRound--;
                System.out.println(String.format("%s: %s (%s)", player, move, bb / 2));
                break;
            case "bb":
                bet(player, bb);
                System.out.println(String.format("%s: %s (%s)", player, move, bb));
                movesInRound--;
                break;
            case "call":
                if (bets[pos] == minBet) {
                    System.err.println(String.format("%s ходит неправильно (call, но его ставка уже уравнена)", player));
                    return;
                }
                double count = minBet - bets[pos];
                bet(player, count);
                System.out.println(String.format("%s: %s (%s)", player, move, count));
                break;
            case "check":
                System.out.println(String.format("%s: %s", player, move));
                break;
            case "fold":
                System.out.println(String.format("%s: %s", player, move));
                break;
            case "bet small":
                count = bb * 3;
                if (count > player.bankroll) {
                    System.err.println(String.format("%s ходит неправильно (bet small, но фишек столько не имеет)", player));
                    return;
                }
                bet(player, count);
                System.out.println(String.format("%s: %s (%s)", player, move, count));
                break;
            case "bet med":
                count = bb * 6;
                if (count > player.bankroll) {
                    System.err.println(String.format("%s ходит неправильно (bet med, но фишек столько не имеет)", player));
                    return;
                }
                bet(player, count);
                System.out.println(String.format("%s: %s (%s)", player, move, count));
                break;
            case "bet big":
                count = bb * 10;
                if (count > player.bankroll) {
                    System.err.println(String.format("%s ходит неправильно (bet big, но фишек столько не имеет)", player));
                    return;
                }
                bet(player, count);
                System.out.println(String.format("%s: %s (%s)", player, move, count));
                break;
            case "all-in":
                if (player.bankroll == 0) {
                    System.err.println(String.format("%s ходит неправильно (all-in, но фишек не имеет", player));
                    return;
                }
                System.out.println(String.format("%s: %s (%s)", player, move, player.bankroll));
                bet(player, player.bankroll);
                break;
        }
    }

    private boolean isEndRound() {
        int playersWithBankroll = 0;
        for (Player player : roundPlayers)
            if (player.bankroll > 0) playersWithBankroll++;
        if (playersWithBankroll > movesInRound) return false;
        for (int i = 0; i < bets.length; i++) {
            if (bets[i] < minBet && roundPlayers.get(i).bankroll > 0) return false;
        }
        return true;
    }

    private void nextRound() {
        switch (round) {
            case 0:
                board.add(deck.takeCard());
                board.add(deck.takeCard());
                board.add(deck.takeCard());
                movePlayer = roundPlayers.get((roundPlayers.indexOf(dealerPlayer) + 1) % roundPlayers.size());
                System.out.println("Flop: " + board.toString());
                break;
            case 1:
                board.add(deck.takeCard());
                movePlayer = roundPlayers.get((roundPlayers.indexOf(dealerPlayer) + 1) % roundPlayers.size());
                System.out.println("Turn: " + board.toString());
                break;
            case 2:
                board.add(deck.takeCard());
                movePlayer = roundPlayers.get((roundPlayers.indexOf(dealerPlayer) + 1) % roundPlayers.size());
                System.out.println("River: " + board.toString());
                break;
            case 3:
                for (Player player : roundPlayers)
                    player.calcCombination(board);
                pot.pay(roundPlayers);
                for (int i = 0; i < roundPlayers.size(); i++) {
                    Player player = roundPlayers.get(i);
                    if (player.bankroll <= 0) {
                        if (player == dealerPlayer) {
                            dealerPlayer = roundPlayers.get((i + roundPlayers.size() - 1) % roundPlayers.size());
                        }
                        gamePlayers.remove(player);
                    }
                    player.clearCombination();
                }
                roundPlayers.clear();
                System.out.println("Раздача сыграна");
                break;
        }
        for (int i = 0; i < bets.length; i++)
            bets[i] = 0;
        round++;
        movesInRound = 0;
        minBet = 0;
    }
}
