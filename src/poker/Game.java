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
    private int round;

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
            System.out.println("Раздача запущена");
            deck.fillCards();
            board.clear();
            roundPlayers.addAll(gamePlayers);
            dealerPlayer = nextDealer();
            minBet = 0;
            round = 0;
            pot.init(roundPlayers);

            doBlinds();
            preflop();

            while (roundPlayers.size() > 1) {
                movePlayer.move(roundPlayers, dealerPlayer, board);
                move(movePlayer, movePlayer.move);
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
        player.bet += count;
        player.invested += count;
        player.bankroll -= count;
//        System.out.println(String.format("%s: bet " + count, player));
    }

    private void move(Player player, String move) {
        switch (move) {
            case "sb":
                bet(player, bb / 2);
                System.out.println(String.format("%s: %s (%s)", player, move, bb / 2));
                break;
            case "bb":
                bet(player, bb);
                System.out.println(String.format("%s: %s (%s)", player, move, bb));
                break;
            case "call":
                if (player.bet == minBet) {
                    System.err.println(String.format("%s ходит неправильно (call, но его ставка уже уравнена)", player));
                    return;
                }
                double count = minBet - player.bet;
                bet(player, count);
                System.out.println(String.format("%s: %s (%s)", player, move, count));
                break;
            case "check":
                System.out.println(String.format("%s: %s", player, move));
                break;
            case "fold":
                roundPlayers.remove(player);
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
        for (Player player : roundPlayers) {
            if (player.bankroll > 0) {
                if (player.bet < minBet || player.move == null) return false;
            }
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
                pot.distribute(roundPlayers);

                for (Player player : roundPlayers) {
                    if (player.result > 0) player.bankroll += player.result;
                    System.out.println(player + " won " + player.result);
                    player.result = 0;
//                    player.invested = 0;
                    player.clearCombination();
                }

                for (int i = 0; i < roundPlayers.size(); i++) {
                    Player player = roundPlayers.get(i);
                    if (player.bankroll <= 0) {
                        if (player == dealerPlayer) {
                            dealerPlayer = roundPlayers.get((i + roundPlayers.size() - 1) % roundPlayers.size());
                        }
                        gamePlayers.remove(player);
                    }
                }
                roundPlayers.clear();
                System.out.println("Раздача сыграна");
                break;
        }
        for (Player player : gamePlayers) {
            player.bet = 0;
            player.move = null;
        }
        round++;
        minBet = 0;
    }
}
