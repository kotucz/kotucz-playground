package kotucz.village.tiles;

import kotucz.village.common.Dir4;

/**
 * @author Kotuc
 */
public abstract class AbstractSetGrid implements SubtextureSelector {
    protected final TileGrid tilegrid;
    int def;

    public AbstractSetGrid(int def, TileGrid tilegrid) {
        this.def = def;
        this.tilegrid = tilegrid;
    }

    public void updateGrid() {
        tilegrid.updateGrid(this);
//        for (Tile t : tilegrid.lingrid) {
////            tilegrid.setTexture(t.x, t.y, getRoadTileHash(t.x, t.y));
//            tilegrid.setTexture(t.x, t.y, getSubtexture(t.pos));
//        }
//        tilegrid.updateTexture();
    }

    @Override
    public Subtexture getSubtexture(Pos pos) {
        if (!this.contains(pos)) {
            return tilegrid.mtex.getTex(def);
        }

        return tilegrid.mtex.getTex(getRoadTileHash(pos));
    }

    public int getRoadTileHash(Pos pos) {
//        if ((x < 0) || (y < 0) || (tilesx <= x) || (tilesy <= y)) {
//            return;
//        }

        int hash = ((contains(pos.inDir(Dir4.E))) ? 1 : 0)
                + ((contains(pos.inDir(Dir4.N))) ? 2 : 0)
                + ((contains(pos.inDir(Dir4.W))) ? 4 : 0)
                + ((contains(pos.inDir(Dir4.S))) ? 8 : 0);
        return hash;
    }

    public abstract boolean contains(Pos pos);
}
