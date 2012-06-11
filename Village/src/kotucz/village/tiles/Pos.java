package kotucz.village.tiles;

/**
 *
 * @author Kotuc
 */
public final class Pos {
    public final int x;
    public final int y;

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


    @Override
    public String toString() {
        return "Pos("+x+","+y+")";
    }
}
