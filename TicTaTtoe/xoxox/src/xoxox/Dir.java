package xoxox;

/**
 *
 * @author Kotuc
 */
public enum Dir {

    HORIZONTAL(1, 0), MAIN_DIAG(1, 1), VERTICAL(0, 1), OTHER_DIAG(1, -1);
    final int dx;
    final int dy;

    private Dir(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
}
