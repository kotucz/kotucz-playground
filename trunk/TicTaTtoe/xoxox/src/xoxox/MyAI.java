package xoxox;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Kotuc
 */
public class MyAI {

    private Board board;
    private List<Diag> alldiags;
    private int[][] vals;
    private HistHeur[][] heurs;

    public MyAI(Board board) {
        this.board = board;
        vals = new int[board.getW()][board.getH()];
        heurs = new HistHeur[board.getW()][board.getH()];
    }

    void move() {
        boolean found = false;

//        for (int x = 0; x < W; x++) {
//            for (int y = 0; y < H; y++) {
//                if (!isLegal(x, y)) {
//                    found = false;
//                }
////                if (isLegal(x, y)) {
////                    apply(new Move(x, y));
////                    return;
////                }
//            }
//
//        }
//        Move mv = new Move(0, 0);
//        for (List<Move> list : alldiags) {
//            for (Move move : list) {
////                if (found) {
////                    mv = move;
////                    found = false;
////                }
////                if (!isLegal(move)) {
////                   signs[move.x][move.y] = Sign._;
////                    found = true;
////                }
//            }
//        }

        vals[10][11]++;
        vals[11][11]++;

        for (Diag diag : alldiags) {
            diag.convolve(board.getCurrentPlayer(), vals);
        }

        Move mv = new Move(0, 0);
        int max = 0;
        for (int x : board.cols()) {
            for (int y : board.cols()) {
                if (board.isLegal(x, y)) {
                    if (max < vals[x][y]) {
                        max = vals[x][y];
                        mv = new Move(x, y);
                    }
                }
            }
        }
        board.apply(mv);
        return;
    }

    void diags() {
        this.alldiags = new ArrayList<Diag>();
        for (int i = -19; i <= 19; i++) {
            int x = Math.max(0, i);
            int y = Math.abs(Math.max(0, -i));
//            System.out.println(i+" "+x+" "+y);
            Diag diag = new Diag(board);
            while (x < 20 && y < 20) {
                diag.add(new Move(x, y));
//                if (isLegal(x, y)) {
//                    apply(new Move(x, y));
//                    return;
//                }
                x++;
                y++;
            }
            alldiags.add(diag);
        }

        for (int i = 0; i < 40; i++) {

            int y = Math.min(i, 19);
            int x = 0 + Math.max(0, i - y);
//            System.out.println(i+" "+x+" "+y);
            Diag diag = new Diag(board);
            while (x < 20 && y >= 0) {
//                if (isLegal(x, y)) {
//                    apply(new Move(x, y, player));
//                    return;
//                }
                diag.add(new Move(x, y));
                x++;
                y--;
            }
            alldiags.add(diag);
        }


        for (int x : board.cols()) {
            Diag vdiag = new Diag(board);
            Diag hdiag = new Diag(board);
            for (int y : board.rows()) {
                vdiag.add(new Move(y, x));
                hdiag.add(new Move(x, y));
            }
            alldiags.add(vdiag);
            alldiags.add(hdiag);
        }
    }

    void play() {
//        {
//        diags();
//        move();}
        move2();
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        for (int y : board.rows()) {
            for (int x : board.cols()) {
//                g.drawString("" + vals[x][y], x * board.gridsize + 10, y * board.gridsize + 20);
                HistHeur hh = heurs[x][y];
                if (hh != null) {
                    g.drawString("X" + hh.xstats[1] + "," + hh.xstats[2] + hh.xstats[3], x * board.gridsize + 10, y * board.gridsize + 20);
                    g.drawString("O" + hh.ostats[1] + "," + hh.ostats[2] + hh.ostats[3], x * board.gridsize + 10, y * board.gridsize + 30);
                }
            }
        }
    }

    public HistHeur getPents(Move start) {
        HistHeur heur = new HistHeur();
        for (Dir dir : Dir.values()) {
            for (int off = 0; off < 5; off++) {
                checkPent(start.add(dir, -off), dir, heur);
            }
        }
        return heur;
    }

    public HistHeur getTotalHeur() {
        HistHeur heur = new HistHeur();
        for (int y : board.rows()) {
            for (int x : board.cols()) {
                for (Dir dir : Dir.values()) {
                    checkPent(new Move(x, y), dir, heur);
                }
            }
        }
        return heur;
    }

    private void checkPent(Move start, Dir dir, HistHeur heur) {
        int order = 0; // count of players
        Sign owner = null; // owning player
        for (int i = 0; i < 5; i++) {
            Move p = start.add(dir, i);
            if (!board.contains(p)) {
                return;
            }
            Sign s = board.get(p);
            if (!Sign.FREE.equals(s)) {
                if (owner == null) {
                    owner = s;
                }
                if (s.equals(owner.opp())) {
                    return;
                } else if (s.equals(owner)) {
                    order++;
                }
            }
        }
        // add pentlet of order
        if (owner != null) {
            heur.get(owner)[order]++;
        }
    }

    List<Move> availableMoves() {
        List<Move> avails = new LinkedList<Move>();
        for (int y : board.rows()) {
            for (int x : board.cols()) {
                Move move = new Move(x, y);
                if (board.isLegal(move)) {
                    avails.add(move);
                }
            }
        }
        return avails;
    }

    void move2() {

        HistHeur bestheur = new HistHeur();
        Move bestmv = new Move(9, 11);

        for (Move move : availableMoves()) {
            HistHeur pents = getPents(move);
            if (pents.betterThanFor(bestheur, board.getCurrentPlayer())) {
                bestheur = pents;
                bestmv = move;
            }
            heurs[move.x][move.y] = pents;
        }

        board.apply(bestmv);
        return;
    }
}
