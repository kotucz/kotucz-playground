package hypergame;

import hypergame.eagleeye.Table;
import hypergame.eagleeye.TableEntity;
import hypergame.platformer.LevelCreator;
import org.jbox2d.collision.AABB;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import javax.media.opengl.GL2;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Kotuc
 */
public class Game {

    public final World physWorld;
    //    private Body ground;
    public final List<Entity> entities = new LinkedList<Entity>();
    private static final boolean LOAD_TABLE = false;

    public Camera camera = new Camera();

    public Game() {

        AABB aabb = new AABB(new Vec2(-10, -10), new Vec2(10, 10));
//        Vec2 grav = new Vec2(0, -10);
        Vec2 grav = new Vec2(0, 0);
//        this.physWorld = new World(aabb, grav, true);
//        this.physWorld = new World(grav, false);
        this.physWorld = new World(grav, true);
//        physWorld.

        // TODO load table or level
        if (LOAD_TABLE) {
            Table table = new Table(this);
            camera.scale = 200;
            camera.xoff = 350;
            camera.yoff = 450;
        } else {
            LevelCreator level = new LevelCreator(this);
            level.create();

        }

//        table.create();
//        createPhysSide();
    }

    public World getPhysWorld() {
        return physWorld;
    }


    void update() {
        for (Entity entity : entities) {
            entity.update(0.01f);
        }
//        entities.get(67).body.m_force.addLocal(0.0f, 1f);
        physWorld.step(0.01f, 10, 10);
//        entities.get(67).body.m_xf.position.addLocal(0.0f, 0.01f);
    }

    void shake(float vx, float vy) {
        System.out.println("shake " + vx + " " + vy);

        for (Body b = physWorld.getBodyList(); b != null; b = b.getNext()) {
            b.applyForce(new Vec2(vx, vy), b.getPosition());

        }
    }

//    void paint(Displayer gl) {
//        for (Entity entity : entities) {
//            entity.paint(gl);
//        }
//    }

    public Body createBody(BodyDef bd) {
        return physWorld.createBody(bd);
    }

    public void addEntity(Entity entity) {
        entity.game = this;
        entities.add(entity);

    }


    void paint(Graphics2D g) {

        g.setColor(Color.white);
        g.fillRect(0, 0, 1000, 1000);


        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHints(rh);


//        g2.addRenderingHints();

//        final int scale = 200;

        g.translate(camera.xoff, camera.yoff);
        g.scale(camera.scale, -camera.scale);

        Stroke stroke = new BasicStroke(0.005f);
        g.setStroke(stroke);

        AWTDisplayer g1 = new AWTDisplayer(g);
        for (Entity entity : entities) {
            entity.paint(g1);
        }

        g.setTransform(AffineTransform.getTranslateInstance(0, 0));

        g.setColor(Color.black);
        g.drawString("" + getRunTime() / 100, 20, 20);


    }

    private long starttime = System.currentTimeMillis();

    private long getRunTime() {
        return System.currentTimeMillis() - starttime;
    }

    public <E> List<E> filterEntities(Class<E> clss) {
        List<E> filtered = new LinkedList<E>();
        for (Entity entity : entities) {
            if (clss.isInstance(entity)) {
                filtered.add((E) entity);
            }
        }
        return filtered;
    }

    public class Camera {

        public double scale = 200;
        public double xoff = 350;
        public double yoff = 450;
    }

}
