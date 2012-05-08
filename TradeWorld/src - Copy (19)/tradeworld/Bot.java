package tradeworld;

import javax.vecmath.Vector3d;
import tradeworld.war.Soldier;

/**
 *
 * @author Kotuc
 */
public class Bot {

    PlayerId playerid;
    private Soldier soldier;
    private Game game;

    public Bot(PlayerId playerid, Game game) {
        this.playerid = playerid;
        this.soldier = null;
        this.game = game;
    }
    private Time lastMove;

    public void decide() {

        if (soldier == null || !soldier.isAlive()) {
            // respawn
            this.soldier = game.respawnSoldier(playerid);
        }

        final Time time = game.getCurrentTime();

        // move
        if (lastMove == null || time.passed(lastMove.addSec(2))) {
            Vector3d vec = new Vector3d(Math.random() - 0.5, Math.random() - 0.5, 0);
            vec.scale(2.5);
            soldier.go(vec, time);
            lastMove = time;
        }

        synchronized (game.getWorld().getObjects()) {
            // shoot
            for (WorldObject worldObject : game.getWorld().getObjects()) {

                if (worldObject instanceof Soldier) {

                    Soldier target = (Soldier) worldObject;

                    if (!target.equals(soldier)) {

                        Vector3d vec = new Vector3d(Math.random() - 0.5, Math.random() - 0.5, 0);
                        vec.sub(target.getPos(), soldier.getPos());

                        if (vec.lengthSquared() > 1) {
                            vec.normalize();
                            vec.scale(5);
                            soldier.fire(vec, time);
                            break;

                        }
                    }
                }
            }
        }
    }
}
