package kotucz.village.tiles;

/**
 *
 * @author Kotuc
 */
public class Multitexture1 {

    final LinearGrid linearGrid;

    public Multitexture1(LinearGrid linearGrid) {
        this.linearGrid = linearGrid;
    }                  
    
    public Subtexture getTex(int i) {
        int tx = linearGrid.getX(i);
                
//        int ty = 3-(i/4);
        int ty = linearGrid.getY(i);
//        
//        return createSubtexture(tx*16, ty*16, (tx+1)*16, (ty+1)*16);
//    }
//
//    public Subtexture createSubtexture(int startX, int startY, int endX, int endY) {
        return new Subtexture((float) (tx) / linearGrid.getSizeX(),
                (float) (linearGrid.getSizeY() - ty -1) / linearGrid.getSizeY(),
                (float) (tx+1) / linearGrid.getSizeX(),
                (float) (linearGrid.getSizeY() - ty ) / linearGrid.getSizeY());
    }

    
    
}
