package tradeworld.actions;

import javax.vecmath.Point3d;
import tradeworld.Game;
import tradeworld.ObjectId;
import tradeworld.PlayerId;
import tradeworld.World;
import tradeworld.war.Soldier;

class SpawnSoldier extends AbstractAction {

    private Point3d pos;
    private PlayerId playerid;

    public SpawnSoldier(Point3d pos, PlayerId playerid) {
        this.pos = pos;
        this.playerid = playerid;
    }

    @Override
    public void perform(World world) {
        Soldier soldier = new Soldier(pos);
        soldier.id = new ObjectId<Soldier>(soldier, this.id); // action id is unique
        soldier.playerid = playerid;
        world.putObject(soldier);

        Game game = world.getGame();

        if (clientid.equals(game.getClientid()) && game.soldier == null) {
            game.soldier = soldier;
        }
    }
}
