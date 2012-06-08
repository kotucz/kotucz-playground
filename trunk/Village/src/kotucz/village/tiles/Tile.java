package kotucz.village.tiles;

/**
 *
 * @author Kotuc
 */
public class Tile {

    public final int x;
    public final int y;
    public final int i;

    public Tile(int x, int y, int i) {
        this.x = x;
        this.y = y;
        this.i = i;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tile other = (Tile) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.i != other.i) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this.x;
        hash = 37 * hash + this.y;
        hash = 37 * hash + this.i;
        return hash;
    }

    
    
    
}
