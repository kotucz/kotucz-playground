package kotucz.village.tiles;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Kotuc
 */
public class SelectGrid {

    private final TileGrid tilegrid;
    final Set<Pos> set = new HashSet<Pos>();

    public SelectGrid(TileGrid tilegrid) {
        this.tilegrid = tilegrid;
    }

    public void updateGrid() {
        for (Tile t : tilegrid.lingrid) {
            tilegrid.setTexture(t.x, t.y, getRoadTileHash(t.x, t.y));
        }
        tilegrid.updateTexture();
    }

    public int getRoadTileHash(int x, int y) {
//        if ((x < 0) || (y < 0) || (tilesx <= x) || (tilesy <= y)) {
//            return;
//        }


        if (contains(x, y)) {
            return 0;
        }

        int hash = ((contains(x + 1, y)) ? 1 : 0)
                + ((contains(x, y + 1)) ? 2 : 0)
                + ((contains(x - 1, y)) ? 4 : 0)
                + ((contains(x, y - 1)) ? 8 : 0);
        return hash;
    }

    public boolean contains(int x, int y) {
        return set.contains(new Pos(x, y));
    }

    public boolean add(int x, int y) {
        return set.add(new Pos(x, y));
    }

    
    
    public final class Pos {

        final int x;
        final int y;

        public Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Pos other = (Pos) obj;
            if (this.x != other.x) {
                return false;
            }
            if (this.y != other.y) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 59 * hash + this.x;
            hash = 59 * hash + this.y;
            return hash;
        }
    }
}
