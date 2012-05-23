package hypergame;

import org.jbox2d.collision.CircleDef;
import org.jbox2d.collision.PolygonDef;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;

/**
 *
 * @author Kotuc
 */
public class Table {

    final float width = 3.0f;
    final float height = 2.1f;
    final float pawnRadius = 0.1f;
    Game game;

    public Table(Game game) {
        this.game = game;
        create();
    }

    private void create() {
        createPatches();
        createMantinels();
        createPawns();        
        game.addEntity(new Robot(game));
    }

    private void createMantinels() {

        game.addEntity(createMantinel(0.0f, 2.111f, 1.5f, 0.011f )); // top

        game.addEntity(createMantinel(0.0f, -0.011f, 1.5f, 0.011f )); // bottom
        
        game.addEntity(createMantinel(-1.511f, 1.05f, 0.011f, 1.05f)); // left

        game.addEntity(createMantinel(1.511f, 1.05f, 0.011f, 1.05f)); // right


        //game.addEntity(createMantinelMm(1.3f, 0.411f, 0.2f, 0.011f)); // right starting
        //game.addEntity(createMantinelMm(-1.3f, 0.411f, 0.2f, 0.011f)); // left starting
        game.addEntity(createMantinelMm(1500, 2100-400, 1500-400, 2100-400-22)); // right starting
        game.addEntity(createMantinelMm(-1500, 2100-400, -1500+400, 2100-400-22)); // right starting

        // right secured
        game.addEntity(createMantinelMm(350, 0, 350+22, 250));
        game.addEntity(createMantinelMm(700+350-22, 0, 700+350, 250));
        game.addEntity(createMantinelMm(350, 0, 700+350, 120));

        // left secured
        game.addEntity(createMantinelMm(-350-22, 0, -350, 250)); // left starting
        game.addEntity(createMantinelMm(-700-350, 0, -700-350+22, 250)); // left starting
        game.addEntity(createMantinelMm(-700-350, 0, -350, 120));

    }

    private Entity createMantinelMm(int minx, int miny, int maxx, int maxy) {
        return createMantinel((minx+maxx)/2000f, (miny+maxy)/2000f, Math.abs(maxx-minx)/2000f, Math.abs(maxy-miny)/2000f);
    }

    private Entity createMantinel(float x, float y, float halfw, float halfh) {
        PolygonDef sd = new PolygonDef();
        sd.setAsBox(halfw, halfh);

        BodyDef bd = new BodyDef();
        bd.position.set(x, y);
        Body body = game.createBody(bd);
        body.createShape(sd);

        return new Entity(body);

    }

    private Pawn createPawn(float x, float y) {
//        PolygonDef sd = new PolygonDef();
//        sd.setAsBox(, halfh);
        CircleDef sd = new CircleDef();
        sd.radius = 0.1f;
        sd.density = 10f;
        sd.restitution = 0.1f;

        BodyDef bd = new BodyDef();
        bd.position.set(x, y);
        bd.linearDamping = 0.1f;
        bd.angularDamping = 0.1f;        

        Body body = game.createBody(bd);
        body.createShape(sd);
        body.setMassFromShapes();

        body.setBullet(true);

        return new Pawn(body);

    }

    private void createPatches() {
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 6; y++) {
                ScoringArea patch = new ScoringArea((((x^y)&1)==1)?Team.BLUE:Team.RED);
                patch.rect.x = (x-3)*0.35f;
                patch.rect.y = (y+1)*0.35f;
                game.addEntity(patch);
            }
        }
    }

    private void createPawns() {
//        game.addEntity(createPawn(1, 1));

        // center
        game.addEntity(createPawn(0, 3*0.35f));

        putPawns(1, 1);
        putPawns(1, 5);

        putPawns(2, 2);
        putPawns(2, 4);

        putDispensePawns(1, 1);
        putDispensePawns(2, 1);
        putDispensePawns(3, 1);
        putDispensePawns(4, 1);
        putDispensePawns(5, 1);

    }

    private void putPawns(int line, int pos) {
//        game.addEntity(createPawn(1, 1));

        // center
        game.addEntity(createPawn(line*0.35f, (6-pos)*0.35f));
        game.addEntity(createPawn(-line*0.35f, (6-pos)*0.35f));

    }

    private void putDispensePawns(int pos, int type) {
//        game.addEntity(createPawn(1, 1));

        float offs = (float)(Math.random()*0.2f);

        // center
        game.addEntity(createPawn(3*0.35f+0.1f+offs, 0.01f+(pos)*0.28f));
        game.addEntity(createPawn(-(3*0.35f+0.1f+offs), 0.01f+(pos)*0.28f));

    }

}
