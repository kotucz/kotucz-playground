/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kotucz.village.common;

/**
 *
 * @author Kotuc
 */
enum Dir8 implements Dir {

        E(1, 0),
        NE(1, 1),
        N(0, 1),
        NW(-1, 1),
        W(-1, 0),
        SW(-1, -1),
        S(0, -1),
        SE(1, -1);
        private final int dx, dy;

        @Override
        public int dx() {
            return dx;
        }

        @Override
        public int dy() {
            return dy;
        }



        private Dir8(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }