package xoxox;

/**
 *
 * @author Kotuc
 */
public class HistHeur/* implements Comparable<HistHeur>*/ {

    private static final int MAX = 6;
    int[] xstats = new int[MAX];
    int[] ostats = new int[MAX];

    int[] get(Sign player) {
        switch (player) {
            case X:
                return xstats;
            case O:
                return ostats;
            default:
                throw new IllegalArgumentException("player: " + player);
        }

    }

    public boolean betterThanFor(HistHeur o, Sign player) {

        for (int i = MAX - 1; i >= 0; i--) {
            {
                int sub = this.get(player)[i] - o.get(player)[i];
                if (sub > 0) {
                    return true;
                } else if (sub < 0) {
                    return false;
                }
            }
            {
                int sub2 = this.get(player.opp())[i] - o.get(player.opp())[i];
                if (sub2 > 0) {
                    return true;
                } else if (sub2 < 0) {
                    return false;
                }
            }
        }
        return true;

    }
//    public int compareTo(HistHeur o) {
//        for (int i = MAX - 1; i >= 0; i--) {
//            if ()
//        }
//    }
}
