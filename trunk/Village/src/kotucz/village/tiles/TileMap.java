package kotucz.village.tiles;



import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.vecmath.Point3d;


/**
 *
 * @author Kotuc
 */
public class TileMap {

    private final Tile[][] roadpoints;
    private int widthx;
    private int widthy;
//    private Land3D land;
//    private TileLayer layer;
    
    
//    public PathNetwork(Land3D land) {
//        this.land = land;
//        this.widthx = land.getTilesx() + 1;
//        this.widthy = land.getTilesy() + 1;
//        this.roadpoints = new RoadPoint[widthx][widthy];
////        this.layer = layer;
////        roadTextures();
//    }

    public TileMap(int sx, int sy) {
//        this.land = land;
        this.widthx = sx;
        this.widthy = sy;
        this.roadpoints = new Tile[widthx][widthy];
//        this.layer = layer;
//        roadTextures();
        
        
        final Random random = new Random();
        
        for (int i = 0; i < 100; i++) {

            roadpoints[random.nextInt(widthx)][random.nextInt(widthy)] = new Tile();
            
//            TextureSelect textureSelect = selects[i];
            
        }
        
        
    }
    
    


    public boolean isAreaFree(double cx, double cy, double wx, double wy) {
        for (int i = (int) Math.round(cx - 0.5 * wy); i <= (int) Math.round(cx + 0.5 * wx); i++) {
            for (int j = (int) Math.round(cy - 0.5 * wy); j <= (int) Math.round(cy + 0.5 * wy); j++) {
                if (getTile(i, j) != null) {
                    return false;
                }
            }
        }
        return true;
    }


    public Tile getTile(int x, int y) {
        if ((x < 0) || (y < 0) || (widthx <= x) || (widthy <= y)) {
            return null;
        }
        return roadpoints[x][y];
    }

    
    
    class Tile {
        
    }
    
}
