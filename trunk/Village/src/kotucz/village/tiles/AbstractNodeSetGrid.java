package kotucz.village.tiles;

import kotucz.village.common.Dir8;

/**
 * @author Kotuc
 */
public abstract class AbstractNodeSetGrid implements SubtextureSelector {
    protected final TileGrid tilegrid;


    public AbstractNodeSetGrid(TileGrid tilegrid) {

        this.tilegrid = tilegrid;
    }

    public void updateGrid() {
        tilegrid.updateGrid(this);
    }

    @Override
    public Subtexture getSubtexture(Pos pos) {
        return tilegrid.mtex.getTex(getNodesHash(pos));
    }

    public int getNodesHash(Pos pos) {
//        if ((x < 0) || (y < 0) || (tilesx <= x) || (tilesy <= y)) {
//            return;
//        }
        int hash = ((contains(pos)) ? 1 : 0)
                + ((contains(pos.inDir(Dir8.E))) ? 2 : 0)
                + ((contains(pos.inDir(Dir8.NE))) ? 4 : 0)
                + ((contains(pos.inDir(Dir8.N))) ? 8 : 0);
//        layer.selectTexture(x, y, selects[hash]);
        return hash;
    }

    public abstract boolean contains(Pos pos);
}
