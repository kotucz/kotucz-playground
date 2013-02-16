package hypergame.platformer;


import hypergame.Entity;
import hypergame.Game;
import hypergame.eagleeye.TableEntity;
import hypergame.eagleeye.Pawn;
import hypergame.eagleeye.Robot;
import hypergame.eagleeye.ScoringArea;
import hypergame.eagleeye.Team;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;

/**
 *
 * @author Kotuc
 */
public class LevelCreator {

    final float width = 3.0f;
    final float height = 2.1f;
    final float pawnRadius = 0.1f;
    Game game;

    public LevelCreator(Game game) {
        this.game = game;

    }

    public void create() {
//        game.physWorld.setGravity(new Vec2(0, -9.81f));
        game.physWorld.setGravity(new Vec2(0, -20f));
        game.camera.scale = 30;
        game.camera.xoff = 1*game.camera.scale;
        game.camera.yoff = 10*game.camera.scale;
//        createPhysSide();
//        createPatches();
        createPlatforms();
        game.addEntity(createBall(1, 1));
        game.addEntity(createBall(2, 2));
        game.addEntity(createBox(0, 3, 3, 2));
//        game.addEntity(new Robot(game));
        Player player = new Player(game);
        game.addEntity(player);
        game.physWorld.setContactListener(player);
    }

    private void createPhysSide() {
//        AABB aabb = new AABB(new Vec2(-1000, -1000), new Vec2(1000, 1000));
//        Vec2 grav = new Vec2(0, -10);
//        this.physWorld = new World(aabb, grav, true);

        {
            PolygonShape sd = new PolygonShape();
            sd.setAsBox(50.0f, 10.0f);


            BodyDef bd = new BodyDef();
            bd.position.set(0.0f, -10.0f);
            Body ground = game.physWorld.createBody(bd);
            ground.createFixture(sd, 0);
            game.addEntity(new Entity(ground));
        }

        {
            PolygonShape sd = new PolygonShape();
            float a = 0.5f;
            sd.setAsBox(a, a);

            FixtureDef fd = new FixtureDef();
            fd.shape = sd;
            fd.density = 5.0f;
            fd.restitution = 0.0f;
            fd.friction = 0.5f;

            Vec2 x = new Vec2(-10.0f, 0.75f);
            Vec2 y = new Vec2();
            Vec2 deltaX = new Vec2(0.5625f, 2.0f);
            Vec2 deltaY = new Vec2(1.125f, 0.0f);

            for (int i = 0; i < 25; ++i) {
                y.set(x);

                for (int j = i; j < 25; ++j) {
                    BodyDef bd = new BodyDef();
                    bd.type = BodyType.DYNAMIC;
                    bd.position.set(y);
                    Body body = game.physWorld.createBody(bd);
                    body.createFixture(fd);

//                    body.setMassFromShapes();

                    game.addEntity(new Entity(body));

                    y.addLocal(deltaY);
                }

                x.addLocal(deltaX);
            }


        }

        {
            PolygonShape sd = new PolygonShape();
            float a = 0.5f;
            sd.setAsBox(a, a);
            FixtureDef fd = new FixtureDef();
            fd.shape = sd;

            fd.density = 5.0f;
            fd.restitution = 0.0f;
            fd.friction = 0.5f;


            Vec2 pos = new Vec2(-20.0f, 10f);

            BodyDef bd = new BodyDef();
            bd.position.set(pos);
            Body body = game.physWorld.createBody(bd);
            body.setBullet(true);
            body.createFixture(fd);
//            body.setMassFromShapes();

            Vec2 vel = new Vec2(1000, 0);

            body.setLinearVelocity(vel);

            game.addEntity(new Entity(body));
        }
    }

    private void createPlatforms() {

        game.addEntity(createPlatform(0, 0, 10, -1)); // floor

        game.addEntity(createPlatform(5f, 4f, 8f, 3)); // bottom
        
        game.addEntity(createPlatform(-3, 3, 0, -1)); // left

        game.addEntity(createPlatform(10, 3, 12, 0)); // left
//        game.addEntity(createMantinel(1.511f, 1.05f, 0.011f, 1.05f)); // right


        //game.addEntity(createMantinelMm(1.3f, 0.411f, 0.2f, 0.011f)); // right starting
        //game.addEntity(createMantinelMm(-1.3f, 0.411f, 0.2f, 0.011f)); // left starting
//        game.addEntity(createMantinelMm(1500, 2100-400, 1500-400, 2100-400-22)); // right starting
//        game.addEntity(createMantinelMm(-1500, 2100-400, -1500+400, 2100-400-22)); // right starting

        // right secured
//        game.addEntity(createMantinelMm(350, 0, 350+22, 250));
//        game.addEntity(createMantinelMm(700+350-22, 0, 700+350, 250));
//        game.addEntity(createMantinelMm(350, 0, 700+350, 120));

        // left secured
//        game.addEntity(createMantinelMm(-350-22, 0, -350, 250)); // left starting
//        game.addEntity(createMantinelMm(-700-350, 0, -700-350+22, 250)); // left starting
//        game.addEntity(createMantinelMm(-700-350, 0, -350, 120));

    }

    private TableEntity createMantinelMm(int minx, int miny, int maxx, int maxy) {
        return createMantinel((minx+maxx)/2000f, (miny+maxy)/2000f, Math.abs(maxx-minx)/2000f, Math.abs(maxy-miny)/2000f);
    }

    private TableEntity createMantinel(float x, float y, float halfw, float halfh) {
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(halfw, halfh);



        BodyDef bd = new BodyDef();
        bd.position.set(x, y);
        Body body = game.createBody(bd);
        FixtureDef def = new FixtureDef();
        def.shape = ps;
        body.createFixture(def);

        return new TableEntity(body);

    }

    private Entity createPlatform(float left, float top, float right, float bottom) {
        PolygonShape ps = new PolygonShape();
        ps.setAsBox((right-left)/2f, (top-bottom)/2f);

        BodyDef bd = new BodyDef();
        bd.position.set((right+left)/2f, (top+bottom)/2f);
        Body body = game.createBody(bd);
        FixtureDef def = new FixtureDef();
        def.shape = ps;
        body.createFixture(def);

        return new TableEntity(body);

    }

    private Entity createBox(float left, float top, float right, float bottom) {
        PolygonShape ps = new PolygonShape();
        ps.setAsBox((right-left)/2f, (top-bottom)/2f);

        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set((right+left)/2f, (top+bottom)/2f);
        Body body = game.createBody(bd);
        FixtureDef def = new FixtureDef();
        def.shape = ps;
        def.density = 1;
        def.friction = 0.5f;
        body.createFixture(def);

        return new TableEntity(body);

    }


    private Pawn createBall(float x, float y) {
//        PolygonDef sd = new PolygonDef();
//        sd.setAsBox(, halfh);
        CircleShape cd = new CircleShape();
        cd.m_radius = 0.5f;
        FixtureDef sd = new FixtureDef();
        sd.shape = cd;
        sd.density = 10f;
        sd.restitution = 0.1f;
        sd.friction = 0.1f;

        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(x, y);
//        bd.linearDamping = 0.5f;
//        bd.angularDamping = 0.5f;

        Body body = game.createBody(bd);
        body.createFixture(sd);
        // TODO mass
//        body.setMassFromShapes();
//        body.resetMassData();
//        body.m_mass = 10;

        body.setBullet(true);

        return new Pawn(body);

    }

    private void createPatches() {
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 6; y++) {
                ScoringArea patch = new ScoringArea((((x^y)&1)==1)?Team.BLUE:Team.RED);
                patch.rect.x = (x-3)*0.35f;
                patch.rect.y = (y)*0.35f;
                game.addEntity(patch);
            }
        }
    }





}
