package hypergame;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;


public class Renderer implements GLEventListener {

    Game game;
    private double theta = 0;
    private double sin = 0;
    private double cos = 0;
    private Texture texture;

    public Renderer(Game game) {
        this.game = game;

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        update();
        render(
                drawable);


    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void init(GLAutoDrawable drawable) {
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
    }

    void update() {
        theta += 0.01;
        sin = Math.sin(theta);
        cos = Math.cos(theta);
        game.update();
    }



    private void render(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        gl.glPushMatrix();
        //gl.glMultMatrixd(new double[] {0.1, 0, 0, 0, 0, 0.1, 0, 0, 0, 0, 0.1, 0, 0, 0, 0, 1}, i);
        gl.glScaled(0.3, 0.3, 1);
        // draw a triangle filling the window
//        gl.glBegin(GL.GL_TRIANGLES);
//        gl.glColor3f(1, 0, 0);
//        gl.glVertex2d(-c, -c);
//        gl.glColor3f(0, 1, 0);
//        gl.glVertex2d(0, c);
//        gl.glColor3f(0, 0, 1);
//        gl.glVertex2d(s, -s);
//        gl.glEnd();



        if (texture == null) {
            try {
                texture = TextureIO.newTexture(new File("./terkl.jpg"), true);



            } catch (IOException ex) {
                Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (GLException ex) {
                Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE, null, ex);
            }



        }

//        if (texture != null) {
//            texture.enable();
//            texture.bind();
//            gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
//            TextureCoords coords = texture.getImageTexCoords();
//
//            gl.glBegin(GL2.GL_QUADS);
//            gl.glTexCoord2f(coords.left(), coords.bottom());
//            gl.glVertex3f(0, 0, 0);
//            gl.glTexCoord2f(coords.right(), coords.bottom());
//            gl.glVertex3f(1, 0, 0);
//            gl.glTexCoord2f(coords.right(), coords.top());
//            gl.glVertex3f(1, 1, 0);
//            gl.glTexCoord2f(coords.left(), coords.top());
//            gl.glVertex3f(0, 1, 0);
//            gl.glEnd();
//            texture.disable();
//
//        }

        draw(gl);

//        try {
//            ImageIO.write(Screenshot.readToBufferedImage(250, 260), "png", new File("screen.png"));
//        } catch (IOException ex) {
//            Logger.getLogger(SimpleScene.class.getName()).log(Level.SEVERE, null, ex);
//        }

        gl.glPopMatrix();

    }

    void draw(GL2 gl) {

//        if (texture != null) {
//            texture.enable();
//            texture.bind();
//            gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);

//         TODO   game.draw(gl);

//            texture.disable();
//
//        }
    }
}
