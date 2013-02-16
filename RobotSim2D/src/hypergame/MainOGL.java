package hypergame;

import com.jogamp.opengl.util.Animator;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
* @author Kotuc
*/
public class MainOGL {


    public static void main(String[] args) {
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);

        Frame frame = new Frame("AWT Window Test");
        frame.setSize(700, 700);
        frame.add(canvas);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);


            }
        });

        final Game game = new Game();

        final Renderer simpleScene = new Renderer(game);
        canvas.addGLEventListener(simpleScene);
        frame.addComponentListener(new ComponentAdapter() {

            Point oldloc = null;

            @Override
            public void componentMoved(ComponentEvent e) {
                Point nloc = e.getComponent().getLocation();
                if (oldloc != null) {
                    float vx = -(nloc.x - oldloc.x) * 0.1f;
                    float vy = (nloc.y - oldloc.y) * 0.1f;
                    game.shake(vx, vy);
                }
                oldloc = nloc;
            }
        });


        Animator animator = new Animator(canvas);
        animator.add(canvas);
        animator.setRunAsFastAsPossible(true);
        animator.start();


    }


}
