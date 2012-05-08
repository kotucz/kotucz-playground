/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradeworld.actions;

import tradeworld.Player;
import tradeworld.PlayerId;
import tradeworld.World;

/**
 *
 * @author Kotuc
 */
public class BuildRoad extends AbstractAction {

    private static final long serialVersionUID = 532890802305L;
    private int x, y;
    private PlayerId playerid;

    public BuildRoad(int x, int y, Player player) {
        this.x = x;
        this.y = y;
        this.playerid = player.getId();
    }

    public boolean validate(World world) {
        if (world.getGame().getPlayer(playerid).getCash() < 100) {
            System.err.println("Not enough money!:");
            return false;
        }
        return true;
    }

    public void perform(World world) {
        world.getRoadNetwork().addPoint(x, y);
        world.getGame().getPlayer(playerid).pay(100);
        System.out.println("road built " + x + ", " + y);
    }

    @Override
    public String toString() {
        return "Build road " + x + ", " + y;
    }
}
