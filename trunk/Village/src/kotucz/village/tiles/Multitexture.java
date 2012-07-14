package kotucz.village.tiles;

/**
 *
 * @author Kotuc
 */
public class Multitexture {

    int width;
    int height;
    
    
    public Multitexture(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    
    public Subtexture getTex(int i) {
        int tx = i%4;
                
//        int ty = 3-(i/4);
        int ty = (i/4);
        
        return createSubtexture(tx*16, ty*16, (tx+1)*16, (ty+1)*16);
    }

    public Subtexture createSubtexture(int startX, int startY, int endX, int endY) {
        return new Subtexture((float) startX / width,
                (float) (height - endY) / height,
                (float) endX / width,
                (float) (height - startY) / height);
    }

    public Subtexture createRealSubtexture(float startX, float startY, float endX, float endY) {
        return new Subtexture((float) startX / width,
                (float) (height - endY) / height,
                (float) endX / width,
                (float) (height - startY) / height);
    }

    
    int numx;
    int numy;

    
    
}
