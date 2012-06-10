package kotucz.village.common;

/**
 * @author Kotuc
 */
enum Dir4 implements Dir {

    E(1, 0),
    N(0, 1),
    W(-1, 0),
    S(0, -1);
    private final int dx, dy;

    @Override
    public int dx() {
        return dx;
    }

    @Override
    public int dy() {
        return dy;
    }

    private Dir4(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
}