package kotucz.village.tiles;

import java.util.List;

/**
 *
 * @author Kotuc
 */
public class GenericGrid<T> {

    private final LinearGrid lingrid;
    private final T[] tiles;

    public GenericGrid(LinearGrid lingrid) {
        this.lingrid = lingrid;
        tiles = (T[]) new Object[lingrid.getTotalNum()];
    }

    public T get(int index) {
        return tiles[index];
    }

    public T get(int x, int y) {
        if (lingrid.isOutOfBounds(x, y)) {
            return null;
        }
        return tiles[lingrid.index(x, y)];
    }

    public void set(int index, T obj) {
        tiles[index] = obj;
    }

    public void set(int x, int y, T obj) {
        tiles[lingrid.index(x, y)] = obj;
    }

    public T get(Pos pos) {

        return get(pos.x, pos.y);
    }

    public void set(Pos p, T obj) {
        set(p.x, p.y, obj);
    }


}
