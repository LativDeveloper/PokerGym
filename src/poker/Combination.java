package poker;

public class Combination {
    private static String[] handTypes = new String[] {
            "invalid hand",
            "high card",
            "one pair",
            "two pairs",
            "three of a kind",
            "straight",
            "flush",
            "full house",
            "four of a kind",
            "straight flush"
    };

    public final int handType;
    public final int handRank;
    public final int value;
    public final String handName;

    public Combination(int handType, int handRank, int value) {
        this.handType = handType;
        this.handRank = handRank;
        this.value = value;
        this.handName = handTypes[handType];
    }

    @Override
    public String toString() {
        return handName;
    }
}
