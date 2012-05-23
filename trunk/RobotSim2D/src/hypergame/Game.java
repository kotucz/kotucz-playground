package hypergame;

import com.jogamp.opengl.util.Animator;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;
import java.util.List;
import javax.media.opengl.GL2;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

/**
 *
 * @author Kotuc
 */
public class Game {

    final World physWorld;
//    private Body ground;
    final List<Entity> entities = new LinkedList<Entity>();

    public Game() {

        AABB aabb = new AABB(new Vec2(-10, -10), new Vec2(10, 10));
//        Vec2 grav = new Vec2(0, -10);
        Vec2 grav = new Vec2(0, 0);
        this.physWorld = new World(aabb, grav, true);

        Table table = new Table(this);
//        table.create();
//        createPhysSide();
    }

    public World getPhysWorld() {
        return physWorld;
    }

//    private void createPhysSide() {
//        AABB aabb = new AABB(new Vec2(-1000, -1000), new Vec2(1000, 1000));
//        Vec2 grav = new Vec2(0, -10);
//        this.physWorld = new World(aabb, grav, true);
//
//        {
//            PolygonDef sd = new PolygonDef();
//            sd.setAsBox(50.0f, 10.0f);
//
//            BodyDef bd = new BodyDef();
//            bd.position.set(0.0f, -10.0f);
//            ground = physWorld.createBody(bd);
//            ground.createShape(sd);
//            addEntity(new Entity(ground));
//        }
//
//        {
//            PolygonDef sd = new PolygonDef();
//            float a = 0.5f;
//            sd.setAsBox(a, a);
//            sd.density = 5.0f;
//            sd.restitution = 0.0f;
//            sd.friction = 0.5f;
//
//            Vec2 x = new Vec2(-10.0f, 0.75f);
//            Vec2 y = new Vec2();
//            Vec2 deltaX = new Vec2(0.5625f, 2.0f);
//            Vec2 deltaY = new Vec2(1.125f, 0.0f);
//
//            for (int i = 0; i < 25; ++i) {
//                y.set(x);
//
//                for (int j = i; j < 25; ++j) {
//                    BodyDef bd = new BodyDef();
//                    bd.position.set(y);
//                    Body body = physWorld.createBody(bd);
//                    body.createShape(sd);
//                    body.setMassFromShapes();
//
//                    addEntity(new Entity(body));
//
//                    y.addLocal(deltaY);
//                }
//
//                x.addLocal(deltaX);
//            }
//
//
//        }
//
//        {
//            PolygonDef sd = new PolygonDef();
//            float a = 0.5f;
//            sd.setAsBox(a, a);
//            sd.density = 5.0f;
//            sd.restitution = 0.0f;
//            sd.friction = 0.5f;
//
//
//            Vec2 pos = new Vec2(-20.0f, 10f);
//
//            BodyDef bd = new BodyDef();
//            bd.position.set(pos);
//            Body body = physWorld.createBody(bd);
//            body.setBullet(true);
//            body.createShape(sd);
//            body.setMassFromShapes();
//
//            Vec2 vel = new Vec2(1000, 0);
//
//            body.setLinearVelocity(vel);
//
//            addEntity(new Entity(body));
//        }
//    }
    void update() {
        for (Entity entity : entities) {
            entity.update(0.01f);
        }
        physWorld.step(0.01f, 10);
    }

    void shake(float vx, float vy) {
        System.out.println("shake " + vx + " " + vy);

        for (Body b = physWorld.getBodyList(); b != null; b = b.getNext()) {
            b.applyForce(new Vec2(vx, vy), b.getPosition());

        }
    }

    void draw(GL2 gl) {

        for (Entity entity : entities) {
            entity.draw(gl);
        }

    }

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

    Body createBody(BodyDef bd) {
        return physWorld.createBody(bd);
    }

    void addEntity(Entity entity) {
        entity.game = this;
        entities.add(entity);

    }

    void paint(Graphics g) {

        g.setColor(Color.white);
        g.fillRect(0, 0, 1000, 1000);

        Graphics2D g2 = (Graphics2D) g;

        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHints(rh);


//        g2.addRenderingHints();

//        final int scale = 200;
        final double scale = 200;
        final double xoff = 350;
        final double yoff = 450;

        g2.translate(xoff, yoff);
        g2.scale(scale, -scale);

        Stroke stroke = new BasicStroke(0.005f);
        g2.setStroke(stroke);

        for (Entity entity : entities) {
            entity.paint(g);
        }

        g2.setTransform(AffineTransform.getTranslateInstance(0, 0));

        g.setColor(Color.black);
        g.drawString("" + getRunTime() / 100, 20, 20);


    }
    private long starttime = System.currentTimeMillis();

    private long getRunTime() {
        return System.currentTimeMillis() - starttime;
    }

    <E> List<E> filterEntities(Class<E> clss) {
        List<E> filtered = new LinkedList<E>();
        for (Entity entity : entities) {
            if (clss.isInstance(entity)) {
                filtered.add((E)entity);
            }
        }
        return filtered;
    }
}
