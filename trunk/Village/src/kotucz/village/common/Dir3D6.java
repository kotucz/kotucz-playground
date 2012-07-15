package kotucz.village.common;

import com.jme3.math.Vector3f;

/**
 * @author Kotuc
 */
public enum Dir3D6 implements Dir3D {

    E(1, 0, 0),
    N(0, 1, 0),
    W(-1, 0, 0),
    S(0, -1, 0),
    UP(0, 0, 1),
    DOWN(0, 0, -1);
    private final int dx, dy, dz;

    @Override
    public int dx() {
        return dx;
    }

    @Override
    public int dy() {
        return dy;
    }

    @Override
    public int dz() {
        return dz;
    }


    private Dir3D6(int dx, int dy, int dz) {
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    public static Dir3D valueOfVector(Vector3f vector3f) {
        if (vector3f.x==1) {
            return E;
        } else
        if (vector3f.x==-1) {
            return W;
        } else
        if (vector3f.y==1) {
            return N;
        } else
        if (vector3f.y==-1) {
            return S;
        } else
        if (vector3f.z==1) {
            return UP;
        } else
        if (vector3f.z==-1) {
            return DOWN;
        } else {
            throw new IllegalArgumentException("Cant recognize "+vector3f);
        }
    }


}