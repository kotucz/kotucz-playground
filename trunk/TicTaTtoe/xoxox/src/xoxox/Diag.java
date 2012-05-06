package xoxox;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kotuc
 */
public class Diag {

    private Board board;
    final List<Move> fields = new ArrayList<Move>();

    public Diag(Board board) {
        this.board = board;
    }

    boolean contains(int i) {
        return (0 <= i && i < fields.size());
    }

    Sign get(int i) {
        return board.get(fields.get(i));
    }

    void convolve(Sign player, int[][] vals) {
        for (int i = 0; contains(i); i++) {
            int sum = 0;
//            int[] ker = int[]{1 1 };
            for (int j = -4; j < 5; j++) {
                int x = i + j;
                if (contains(x)) {
                    if (get(x).equals(player)) {
                        sum++;
                    }
//                    if (get(x).equals(player.opp())) {
//                        sum++;
//                    }
//                    case player.opp():
//                            break;
//                    }
                }
            }
            Move get = fields.get(i);
            vals[get.x][get.y] += sum;
        }

//        for (Move move : diag) {
////                if (found) {
////                    mv = move;
////                    found = false;
////                }
////                if (!isLegal(move)) {
////                   signs[move.x][move.y] = Sign._;
////                    found = true;
////                }
//        }
    }



    boolean add(Move move) {
        return fields.add(move);
    }
}
