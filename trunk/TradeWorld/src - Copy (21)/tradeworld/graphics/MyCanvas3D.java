package tradeworld.graphics;

import java.awt.Color;
import java.awt.GraphicsConfiguration;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.J3DGraphics2D;
import tradeworld.Game;
import tradeworld.WorldObject;

/**
 *
 * @author Kotuc
 */
public class MyCanvas3D extends Canvas3D {

    final Game game;

    MyCanvas3D(Game game, GraphicsConfiguration preferredConfiguration) {
        super(preferredConfiguration);
        this.game = game;
    }

    @Override
    public void preRender() {
        //J3DGraphics2D g = getGraphics2D();
//        Graphics g = getGraphics();
//        int width = getWidth();
//        int height = getHeight();
//        System.out.println("drawing  pre " + width + " " + height + " " + g);
//        g.setColor(Color.GREEN);
//        g.fillRect(100, 50, 50, 50);
//        g.drawLine(width / 2 - 5, height / 2, width / 2 + 5, height / 2);
//        g.drawLine(width / 2, height / 2 - 5, width / 2, height / 2 + 5);
    }

    @Override
    public void postRender() {



        int width = getWidth();
        int height = getHeight();
//        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g = bufferedImage.createGraphics();

        J3DGraphics2D g = getGraphics2D();

//        System.out.println("drawing post " + width + " " + height + " " + g);
        g.setColor(Color.ORANGE);
        g.fillRect(50, 50, 50, 50);
        g.drawLine(width / 2 - 5, height / 2, width / 2 + 5, height / 2);
        g.drawLine(width / 2, height / 2 - 5, width / 2, height / 2 + 5);

        if (game.soldier != null) {
            int x = 100;
            for (WorldObject worldObject : game.soldier.getPicks()) {
                g.drawString(worldObject.toString(), 20, x);
                x+=20;
            }
        }

//        getGraphics2D().drawAndFlushImage(bufferedImage, 0, 0, this);
        g.flush(false);
//        try {
//            Thread.sleep(1);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(MyCanvas3D.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public void postSwap() {
//        stopRenderer();
//        System.out.println("drawing swap  ");
    }

    public void render() {
////        setSize(100, 100);
//        setOffScreenBuffer(new ImageComponent2D(ImageComponent2D.FORMAT_RGB, 640, 480));
//        getScreen3D().setSize(640, 480);
//        getScreen3D().setPhysicalScreenWidth(0.6);
//        getScreen3D().setPhysicalScreenWidth(0.4);
////        Size(640, 480);
//        //stopRenderer();
//        renderOffScreenBuffer();
////        startRenderer();
    }
}
