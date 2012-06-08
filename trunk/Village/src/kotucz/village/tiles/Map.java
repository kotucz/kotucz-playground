package kotucz.village.tiles;

import com.jme3.scene.Node;
import kotucz.village.game.MyGame;

/**
 *
 * @author Kotuc
 */
public class Map {

    final MyGame myGame;

    final LinearGrid lingrid;
    
    final Node rootNode;
    
    public Map(MyGame myGame, Node rootNode) {
        this.myGame = myGame;
        
        this.lingrid = new LinearGrid(16, 16);
        
        this.rootNode = rootNode;
        
        
    }
    
    
    
    
}
