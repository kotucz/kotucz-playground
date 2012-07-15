package kotucz.village.cubes;

import kotucz.village.tiles.LinearGrid;
import kotucz.village.tiles.Pos;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Kotuc
 */
public class Map3D<T> {

    private final Map<Pos3D, T> cubes;

    public Map3D() {
        cubes = new HashMap<Pos3D, T>();
    }

    public T get(Pos3D pos) {
        return cubes.get(pos);
    }

    public void set(Pos3D pos, T obj) {
        cubes.put(pos, obj);
    }

}
