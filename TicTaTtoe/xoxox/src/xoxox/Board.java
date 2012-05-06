package xoxox;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Stack;

/**
 *
 * @author xkotula
 */
public class Board {

    static final int W = 20;
    static final int H = 20;
    //  x y
    private Sign[][] signs = new Sign[W][H];
    private Sign player;
    static final int[] int_0_19 = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
        10, 11, 12, 13, 14, 15, 16, 17, 18, 19};

    private final Stack<Move> moves = new Stack<Move>();

    public Sign getCurrentPlayer() {
        return player;
    }

    public int[] cols() {
        return int_0_19;
    }

    public int[] rows() {
        return int_0_19;
    }

    void load(BufferedReader reader) throws IOException {

        player = Sign.parse(reader.readLine().charAt(0));

        for (int y = 0; y < H; y++) {
            String readLine = reader.readLine();
            for (int x = 0; x < W; x++) {
                signs[x][y] = Sign.parse(readLine.charAt(x));
            }
        }

    }

    boolean isLegal(Move mv) {
        return isLegal(mv.x, mv.y);
    }

    boolean isLegal(int x, int y) {
        if (!contains(x, y)) {
            throw new ArrayIndexOutOfBoundsException("" + x + " " + y);
        }
        return Sign.FREE.equals(signs[x][y]);
    }

    boolean contains(Move move) {
        return contains(move.x, move.y);

    }

    boolean contains(int x, int y) {
        return !(x < 0 || W <= x || y < 0 || H <= y);
    }

    void apply(Move move) {
        signs[move.x][move.y] = player;
        moves.push(move);
        player = player.opp();
        
//        signs[move.x][move.y] = move.s;
    }

    Move undoMove() {
        Move last = moves.pop();
        signs[last.x][last.y] = Sign.FREE;
        player = player.opp();
        return last;
    }
    
    public int getW() {
        return W;
    }

    public int getH() {
        return H;
    }

    void print() throws IOException {

//        System.out.println(player.opp());
        System.out.println(player);

        for (int y : rows()) {
            for (int x : cols()) {
                System.out.print(signs[x][y]);
            }
            System.out.println("");
        }

    }
    final int gridsize = 32;
    final int stroke = 4;

    public Sign get(int x, int y) {
        return signs[x][y];
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        for (int y : rows()) {
            for (int x : cols()) {
                switch (get(x, y)) {
                    case X:
                        g2.setStroke(new BasicStroke(2));
                        g.setColor(Color.blue);
                        g.drawLine(x * gridsize + stroke, y * gridsize + stroke, (x + 1) * gridsize - stroke, (y + 1) * gridsize - stroke);
                        g.drawLine(x * gridsize + stroke, (y + 1) * gridsize - stroke, (x + 1) * gridsize - stroke, y * gridsize + stroke);
                        break;
                    case O:
                        g2.setStroke(new BasicStroke(2));
                        g.setColor(Color.red);
                        g.drawOval(x * gridsize + stroke, y * gridsize + stroke, gridsize - 2 * stroke, gridsize - 2 * stroke);
                        break;
                    case FREE:
                        g2.setStroke(new BasicStroke(1));
                        g.setColor(Color.black);
                        g.drawRect(x * gridsize, y * gridsize, gridsize, gridsize);
                        break;
                }
            }
        }
    }

    public Sign get(Move move) {
        return get(move.x, move.y);
    }
}
