package kotucz.village.game;

import com.jme3.scene.Node;
import kotucz.village.build.Buildings;
import kotucz.village.game.MyGame;
import kotucz.village.tiles.LinearGrid;
import kotucz.village.tiles.NodeSetGrid;
import kotucz.village.tiles.Pos;
import kotucz.village.transport.BlockingTraffic;
import kotucz.village.transport.UnidirectionalPathNetwork;

import java.util.Random;

/**
 *
 * @author Kotuc
 */
public class GameMap {

    final MyGame myGame;

    final LinearGrid lingrid;
    
    final Node rootNode;

    UnidirectionalPathNetwork pnet;


    BlockingTraffic traffic;

    Buildings buildings;

    NodeSetGrid water;

    
    public GameMap(MyGame myGame, Node rootNode) {
        this.myGame = myGame;
        
        this.lingrid = myGame.lingrid;
        
        this.rootNode = rootNode;

        //            pnet = new PathNetwork(tileGrid);
        pnet = new UnidirectionalPathNetwork(lingrid);
//            pnet.randomlySelect(80);
        pnet.generateRandomWalk(myGame.random);

        traffic = new BlockingTraffic(pnet);

        buildings = new Buildings(lingrid);



    }

    public boolean isBuildable(Pos pos) {
        return water.getNodesHash(pos)==0 && buildings.get(pos) == null && pnet.getRoadPoint(pos) == null;
    }



    
}
