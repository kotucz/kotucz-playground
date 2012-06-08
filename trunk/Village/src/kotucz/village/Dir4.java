package kotucz.village;

/**
 *
 * @author Kotuc
 */
public enum Dir4 {

        E(1, 0),
        N(0, 1),
        W(-1, 0),
        S(0, -1);
        private final int dx, dy;

        public int dx() {
            return dx;
        }

        public int dy() {
            return dy;
        }

        private Dir4(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }
    }