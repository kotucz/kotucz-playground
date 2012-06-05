/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kotucz.village.tiles;

/**
 *
 * @author Kotuc
 */
public class RoadTileGrid extends TileGrid  {
    
    private PathNetwork pnet;

    public RoadTileGrid(PathNetwork pnet) {
        super();
        this.pnet = pnet;
        
        for (int x = 0; x < sizex; x++) {
            for (int y = 0; y < sizey; y++) {
                setTexture(x, y, pnet.getRoadTileHash(x, y));
            }            
        }
        
//        setTexture(, 0, 5);
        
        updateGeometry();
        
    }
    
    
    
}
