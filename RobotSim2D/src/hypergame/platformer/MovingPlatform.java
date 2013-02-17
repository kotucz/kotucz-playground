package hypergame.platformer;

import hypergame.Entity;
import hypergame.Game;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;

class MovingPlatform extends Entity {

    Vec2 pos = new Vec2();
    Vec2 dir = new Vec2();
    float dist = 0;
    float maxDist = 0;

    public MovingPlatform(Game game, float x, float y, float width, float height, float dx, float dy, float maxDist) {
        body = createBox(game.physWorld, BodyType.KINEMATIC, width, height, 1);
        pos.x = x;
        pos.y = y;
        dir.x = dx;
        dir.y = dy;
        this.maxDist = maxDist;
        body.setTransform(pos, 0);
//        platform.getFixtureList().get(0).setUserData("p");
        body.setUserData(this);
    }

    @Override
    public void update(float deltaTime) {
        dist += dir.length() * deltaTime;
        if (dist > maxDist) {
            dir.mulLocal(-1);
            dist = 0;
        }

        body.setLinearVelocity(dir);
    }

    private Body createBox(World world, BodyType type, float width, float height, float density) {
        BodyDef def = new BodyDef();
        def.type = type;
        Body box = world.createBody(def);

        PolygonShape poly = new PolygonShape();
        poly.setAsBox(width, height);
        box.createFixture(poly, density);

        return box;
    }
}

