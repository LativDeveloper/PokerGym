package poker.evaluator;

import poker.Card;
import poker.Combination;
import poker.Hand;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class HandEvaluator {
    private static char[] handValues = new char[] { '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k', 'a' };
    private static byte[] handRanks;
    static {
        try {
            String path = "/home/carter/IdeaProjects/PokerGym/resources/HandRanks.dat";
            FileInputStream inputStream = new FileInputStream(path);
            handRanks = new byte[inputStream.available()];
            inputStream.read(handRanks);
        } catch (Exception e) {
            System.err.println("Ошибка загрузки HandRanks.dat!");
            e.printStackTrace();
        }
    }

    public static Combination evalHand(Hand hand, Card[] board) {
        ArrayList cards = (ArrayList) hand.getCards().clone();
        if (board != null) {
            for (Card card : board)
                cards.add(card);
        }
        if (cards.size() != 3 && cards.size() != 5 && cards.size() != 7) {
            System.err.println("Рука должна состоять из 3, 5 или 7 карт!");
            return null;
        }

        // если рука содержит 3 карты, то дополняем до 5
        if (cards.size() == 3) fillCards3To5(cards);


        return eval(cards);
    }

    public static Combination evalHand(Hand hand) {
        if (handRanks == null) {
            System.err.println("HandRanks.dat не загружен!");
            return null;
        }

        return evalHand(hand, null);
    }

    private static Combination eval(ArrayList<Card> cards) {
        int p = 53;
        for (Card card : cards) {
            p = evalCard(p + card.getValue());
        }

        if (cards.size() == 5) {
            p = evalCard(p);
        }
        return new Combination(p >> 12, p & 0x00000fff, p);
    }

    static int evalCard(int value) {
        return readUInt32(value * 4);
    }

    private static void fillCards3To5(ArrayList<Card> cards) {

        byte[] cardUsed = new byte[] { 0,0,0,0,0,0,0,0,0,0,0,0,0 };

        // convert each card to values 0-12, strip suit
        for (Card card : cards) {
            int i = (int) Math.ceil((card.getValue() - 1) /4);
            cardUsed[i] = 1;
        }


        int toFill = 2; // need to fill 2 cards
        int maxFillIndex = 0; // index in cardsUsed of highest filled card

        // fill in <toFill> cards to complete 5 card hand
        for (int i = 0; i < 13; i++) {
            if (toFill == 0) break; // done filling
            if (cardUsed[i] == 0) {
                cardUsed[i] = 2;
                maxFillIndex = i;
                toFill--;
            }
        }

        // check if there is straight
        int continuousCards = 0;
        boolean hasStraight = false;
        int straightEndIndex = 0;

        for (int i = 0; i < 13; i++) {
            if (cardUsed[i] == 0) {
                continuousCards = 0;
            } else {
                continuousCards++;
                if (continuousCards == 5) {
                    hasStraight = true;
                    straightEndIndex = i;
                }
            }
        }

        // if there is straight, fix it by shifting highest filled card to one past the straight
        if (hasStraight) {
            cardUsed[maxFillIndex] = 0;
            cardUsed[straightEndIndex + 1] = 2;
        }

        // fill dummy cards for lowest possible hand
        // TODO: 3/14/20 есть проблемы с добавлением карт, т.к. нет учета стрита от туза
        char[] suits = new char[] { 's', 'd' };
        int j = 0;
        for (int i = 0; i < 13; i++) {
            if (cardUsed[i] == 2) {
                String str = String.format("%s%s", handValues[i], suits[0]);
                j++;
                cards.add(new Card(str));
            }
        }
    }

    private static int readUInt32(int offset) {
        ByteBuffer buffer = ByteBuffer.wrap(handRanks, offset, handRanks.length - offset).order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getInt();
    }


}
