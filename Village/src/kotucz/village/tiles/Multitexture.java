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

    public Subtexture createSubtexture(int startX, int startY, int endX, int endY) {
        return new Subtexture((float) startX / width,
                (float) (height - endY) / height,
                (float) endX / width,
                (float) (height - startY) / height);
    }
}
