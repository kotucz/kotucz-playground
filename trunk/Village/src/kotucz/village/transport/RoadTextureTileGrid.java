package kotucz.village.transport;

import com.jme3.material.Material;
import kotucz.village.game.MyGame;
import kotucz.village.tiles.AbstractSetGrid;
import kotucz.village.tiles.LinearGrid;
import kotucz.village.tiles.Pos;
import kotucz.village.tiles.TileGrid;

/**
 * @author Kotuc
 */
public class RoadTextureTileGrid extends TileGrid {

    public RoadTextureTileGrid(AbstractGridPathNetwork pnet, Material mat, MyGame myGame) {
        super(pnet.lingrid, mat, myGame);
        this.pnet = pnet;
    }

    final AbstractGridPathNetwork pnet;

    @Override
    public void updateTexture() {
//        for (Tile t : lingrid) {
//            tilegrid.setTexture(t.x, t.y, getRoadTileHash(t.x, t.y));
//        }
//        tilegrid.updateTexture();

        this.updateGrid(new AbstractSetGrid(this, 0) {
            @Override
            public boolean contains(Pos pos) {
                return pnet.getPoint(pos) != null;
            }
        });

//        super.updateTexture();
    }

}
