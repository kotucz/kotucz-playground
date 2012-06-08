package kotucz.village.tiles;

import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import java.util.Iterator;
import java.util.Random;
import kotucz.village.game.MyGame;

/**
 *
 * @author Kotuc
 */
public class TileGrid {

    MeshTileGrid meshGrid;
//    Multitexture mtex = new Multitexture(16*4, 16*4);
    Multitexture1 mtex = new Multitexture1(new LinearGrid(4, 4));
    Material mat;
    MyGame myGame;
    LinearGrid lingrid;
    final Geometry geometry;

    public TileGrid(LinearGrid lingrid, Material mat, MyGame myGame) {

        this.mat = mat;
        this.myGame = myGame;
        this.lingrid = lingrid;

        this.meshGrid = new MeshTileGrid(lingrid);



//                    PathNetwork pnet = new PathNetwork(16, 16);
//            pnet.randomlySelect(80);
//
        geometry = new Geometry("roadgrid", meshGrid);
        geometry.setMaterial(mat);
        geometry.setShadowMode(ShadowMode.Receive);
        geometry.setQueueBucket(Bucket.Transparent);



    }
    
    public void genRandom() {
        Random r = new Random();

        for (int i = 0; i < lingrid.getTotalNum(); i++) {
//            setTexture(i, mtex.getTex(i)createSubtexture(16*(i%3), 16*(i%3), 16*(i%3)+16, 16*(i%3)+16));                
            meshGrid.setTexture(i, mtex.getTex(r.nextInt(16)));
        }
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setTexture(int x, int y, int tex) {
        meshGrid.setTexture(x, y, mtex.getTex(tex));
    }

    public void updateTexture() {
        meshGrid.updateTexture();
    }

   
}
