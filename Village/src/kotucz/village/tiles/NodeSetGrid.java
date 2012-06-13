package kotucz.village.tiles;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Kotuc
 */
public class NodeSetGrid extends AbstractNodeSetGrid {

    public final Set<Pos> set = new HashSet<Pos>();

    public NodeSetGrid( TileGrid tilegrid) {
        super( tilegrid);
    }

    @Override
    public boolean contains(Pos pos) {
        return set.contains(pos);
    }
}
