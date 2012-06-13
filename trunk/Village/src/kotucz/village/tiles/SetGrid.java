package kotucz.village.tiles;

import com.jme3.math.Vector3f;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Kotuc
 */
public class SetGrid extends AbstractSetGrid {

    public final Set<Pos> set = new HashSet<Pos>();

    public SetGrid(TileGrid tilegrid, int def) {
        super(tilegrid, def);
    }

    @Override
    public boolean contains(Pos pos) {
        return set.contains(pos);
    }

    public boolean add(int x, int y) {
        return set.add(new Pos(x, y));
    }


    public boolean add(Pos p) {
        return set.add(p);
    }

}
