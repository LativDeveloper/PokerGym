package poker.evaluator;

public class SpeedTest {

    public static void main(String[] args) {

        int u0, u1, u2, u3, u4, u5;
        int c0, c1, c2, c3, c4, c5, c6;
        int[] handTypeSum = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        int count = 0;

        System.out.println("Перечисление и оценка всех 133,784,560 возможных 7-карточных покерных рук...");
        HandEvaluator.evalCard(0);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long startTime = System.currentTimeMillis();

        for (c0 = 1; c0 < 47; c0++) {
            u0 = HandEvaluator.evalCard(53+c0);
            for (c1 = c0+1; c1 < 48; c1++) {
                u1 = HandEvaluator.evalCard(u0+c1);
                for (c2 = c1+1; c2 < 49; c2++) {
                    u2 = HandEvaluator.evalCard(u1+c2);
                    for (c3 = c2+1; c3 < 50; c3++) {
                        u3 = HandEvaluator.evalCard(u2+c3);
                        for (c4 = c3+1; c4 < 51; c4++) {
                            u4 = HandEvaluator.evalCard(u3+c4);
                            for (c5 = c4+1; c5 < 52; c5++) {
                                u5 = HandEvaluator.evalCard(u4+c5);
                                for (c6 = c5+1; c6 < 53; c6++) {

                                    handTypeSum[HandEvaluator.evalCard(u5+c6) >> 12]++;
                                    count++;
                                }
                            }
                        }
                    }
                }
            }
        }

        long endTime = System.currentTimeMillis();

        System.out.println("BAD:              " + handTypeSum[0]);
        System.out.println("High Card:        " + handTypeSum[1]);
        System.out.println("One Pair:         " + handTypeSum[2]);
        System.out.println("Two Pair:         " + handTypeSum[3]);
        System.out.println("Trips:            " + handTypeSum[4]);
        System.out.println("Straight:         " + handTypeSum[5]);
        System.out.println("Flush:            " + handTypeSum[6]);
        System.out.println("Full House:       " + handTypeSum[7]);
        System.out.println("Quads:            " + handTypeSum[8]);
        System.out.println("Straight Flush:   " + handTypeSum[9]);

        // проверяем производительность и результаты теста
        int testCount = 0;
        for (int index = 0; index < 10; index++)
            testCount += handTypeSum[index];

        assert testCount == count;
        assert count == 133784560;
        assert handTypeSum[0] == 0;

        System.out.println("Перечислены " + count + " рук за " + (endTime - startTime) + " мс!");
    }

}
