package kotucz.village.cubes;

import kotucz.village.common.Dir;
import kotucz.village.common.Dir3D;

/**
 * @author Kotuc
 *         Immutable
 *         Value class
 */
public final class Pos3D {
    public final int x;
    public final int y;
    public final int z;

    public Pos3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pos3D pos3D = (Pos3D) o;

        if (x != pos3D.x) return false;
        if (y != pos3D.y) return false;
        if (z != pos3D.z) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }

    @Override
    public String toString() {
        return "Pos3D(" + x + ", " + y + ", " + z + ")";
    }

    public Pos3D inDir(Dir dir) {
        return new Pos3D(this.x + dir.dx(), this.y + dir.dy(), this.z);
    }

    public Pos3D inDir(Dir3D dir) {
        return new Pos3D(this.x + dir.dx(), this.y + dir.dy(), this.z + dir.dz());
    }

}
