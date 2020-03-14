package poker;

import java.util.HashMap;

public class Card implements Comparable<Card> {

    private static HashMap<String, Integer> list = new HashMap<String, Integer>();
    static {
        list.put("2c", 1);
        list.put("2d", 2);
        list.put("2h", 3);
        list.put("2s", 4);
        list.put("3c", 5);
        list.put("3d", 6);
        list.put("3h", 7);
        list.put("3s", 8);
        list.put("4c", 9);
        list.put("4d", 10);
        list.put("4h", 11);
        list.put("4s", 12);
        list.put("5c", 13);
        list.put("5d", 14);
        list.put("5h", 15);
        list.put("5s", 16);
        list.put("6c", 17);
        list.put("6d", 18);
        list.put("6h", 19);
        list.put("6s", 20);
        list.put("7c", 21);
        list.put("7d", 22);
        list.put("7h", 23);
        list.put("7s", 24);
        list.put("8c", 25);
        list.put("8d", 26);
        list.put("8h", 27);
        list.put("8s", 28);
        list.put("9c", 29);
        list.put("9d", 30);
        list.put("9h", 31);
        list.put("9s", 32);
        list.put("tc", 33);
        list.put("td", 34);
        list.put("th", 35);
        list.put("ts", 36);
        list.put("jc", 37);
        list.put("jd", 38);
        list.put("jh", 39);
        list.put("js", 40);
        list.put("qc", 41);
        list.put("qd", 42);
        list.put("qh", 43);
        list.put("qs", 44);
        list.put("kc", 45);
        list.put("kd", 46);
        list.put("kh", 47);
        list.put("ks", 48);
        list.put("ac", 49);
        list.put("ad", 50);
        list.put("ah", 51);
        list.put("as", 52);
    }

    private String name;
    private int value;

    public Card(String cardStr) {
        try {
            cardStr = cardStr.trim();

            value = list.get(cardStr.toLowerCase());
            name = cardStr;
        } catch (NullPointerException e) {
            System.err.println(String.format("Неверная карты колоды! (%s)", cardStr));
        }
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Card card) {
        if (value > card.value) return 1;
        if (value < card.value) return -1;

        return 0;
    }
}