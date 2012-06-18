package kotucz.village.game;

import com.jme3.scene.Node;
import kotucz.village.game.MyGame;
import kotucz.village.tiles.LinearGrid;
import kotucz.village.transport.BlockingTraffic;
import kotucz.village.transport.UnidirectionalPathNetwork;

import java.util.Random;

/**
 *
 * @author Kotuc
 */
public class Map {

    final MyGame myGame;

    final LinearGrid lingrid;
    
    final Node rootNode;

    UnidirectionalPathNetwork pnet;


    BlockingTraffic traffic;

    
    public Map(MyGame myGame, Node rootNode) {
        this.myGame = myGame;
        
        this.lingrid = myGame.lingrid;
        
        this.rootNode = rootNode;

        //            pnet = new PathNetwork(tileGrid);
        pnet = new UnidirectionalPathNetwork(lingrid);
//            pnet.randomlySelect(80);
        pnet.generateRandomWalk(myGame.random);

        traffic = new BlockingTraffic(pnet);





    }





    
}
