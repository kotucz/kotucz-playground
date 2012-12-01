/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interlos2012;

/**
 * sudoku wit non regular areas
 *
 * @author tkotula
 */
public class P3 {

    static int numSols = 0;

    final static int N = 6;

    static int[] area = new int[]{
            0, 0, 0, 1, 1, 1,
            2, 2, 0, 0, 0, 1,
            3, 2, 4, 4, 4, 1,
            3, 2, 4, 4, 5, 1,
            3, 2, 2, 4, 5, 5,
            3, 3, 3, 5, 5, 5
    };


    // solution 0 = empty
    static int[] s = new int[N * N];

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        s[5] = 5;
        s[6 + 4] = 4;
        s[4 * 6 + 1] = 5;
        s[5 * 6 + 0] = 2;


        // backtracking with early cutting
        solve(0);

    }

    // p - which position solving
    static void solve(int p) {
        if (p==N*N) {
            // celebrate
            numSols++;
            System.out.println(""+numSols+":");
            printSolution();
            return;
        }
        if (s[p] != 0) {
            // prefilled
            solve(p + 1);
            return;
        }
        for (int j = 0; j < N; j++) {
            s[p] = j + 1;
//            printSolution();
            try {
                checkState();
                solve(p + 1);
            } catch (IllegalStateException ex) {
//                System.out.println("ILLEGAL " + ex.getMessage());
            }
        }
        s[p] = 0;
        // backtrack
    }

    // return true if solution is still valid
    private static void checkState() {
        boolean[][] hor = new boolean[N][N + 1];
        boolean[][] ver = new boolean[N][N + 1];
        boolean[][] ar = new boolean[N][N + 1];

        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                final int p = y * N + x;

                checkArea(ver, x, s[p], "col");
                checkArea(hor, y, s[p], "row");
                checkArea(ar, area[p], s[p], "area");

            }
        }


    }

    // areaid - row, col, areacode, num - new numSols
    static void checkArea(boolean[][] set, int areaid, int num, String name) {
        if (num == 0) return;
        if (set[areaid][num]) {
            // already contains
            throw new IllegalStateException(name+" " + areaid + " dupl "+num);
        }
        set[areaid][num] = true;
    }

    static void printSolution() {
        System.out.println();
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                final int p = y * N + x;
                System.out.print(s[p] + "  ");

            }
            System.out.println();
        }
    }

}
