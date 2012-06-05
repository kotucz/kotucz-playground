package kotucz.village.tiles;

/**
 *
 * @author Kotuc
 */
public class NodedefTileGrid extends TileGrid  {
    
    private PathNetwork pnet;

    public NodedefTileGrid(PathNetwork pnet) {
        super();
        this.pnet = pnet;
        
        for (int x = 0; x < sizex; x++) {
            for (int y = 0; y < sizey; y++) {
                setTexture(x, y, pnet.getFlatTileHash(x, y));
            }            
        }
        
//        setTexture(, 0, 5);
        
        updateGeometry();
        
    }
    
    
    
}
