package kotucz.village.build;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import kotucz.village.common.Dir8;
import kotucz.village.common.MyBox;
import kotucz.village.game.Player;
import kotucz.village.tiles.Pos;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Kotuc
 */
public class Building {

    Pos pos;

    final Node node = new Node("Budova");
    
    public Building(Pos pos, Material mat16) {
        this.pos = pos;

        Set<Pos> occupyPosses = getOccupyPosses(pos);

        for (Pos occupyPoss : occupyPosses) {
            MyBox box = new MyBox(Vector3f.ZERO, new Vector3f(1, 1, 1));
            Geometry reBoxg = new Geometry("brick3", box);
            reBoxg.setUserData("test", "budova13654");
            reBoxg.setMaterial(mat16);
            reBoxg.setLocalTranslation(new Vector3f(occupyPoss.x, occupyPoss.y, 0));

            node.attachChild(reBoxg);
        }


//        {
//            MyBox box = new MyBox(Vector3f.ZERO, new Vector3f(1, 1, 1));
//            Geometry reBoxg = new Geometry("brick3", box);
//            reBoxg.setUserData("test", "budova13654");
//            reBoxg.setMaterial(mat16);
//            reBoxg.setLocalTranslation(new Vector3f(0, 0, 0));
//
//            node.attachChild(reBoxg);
//        }
//
//        {
//            MyBox box = new MyBox(Vector3f.ZERO, new Vector3f(1, 1, 1));
//            Geometry reBoxg = new Geometry("brick3", box);
//            reBoxg.setMaterial(mat16);
//            reBoxg.setLocalTranslation(new Vector3f(1, 0, 0));
//
//
//            node.attachChild(reBoxg);
//        }
//        {
//            MyBox box = new MyBox(Vector3f.ZERO, new Vector3f(1, 1, 1));
//            Geometry reBoxg = new Geometry("brick3", box);
//            reBoxg.setMaterial(mat16);
//            reBoxg.setLocalTranslation(new Vector3f(0, 1, 0));
//
//
//            node.attachChild(reBoxg);
//        }
    
//        node.setLocalTranslation(pos.x, pos.y, 0);
        
    }

    public Node getNode() {
        return node;
    }
    
    Player owner;

    public Player getOwner() {
        return owner;
    }

    public Set<Pos> getOccupiedPosses() {
        return getOccupyPosses(pos);
    }

    public static Set<Pos> getOccupyPosses(Pos pos) {
        HashSet<Pos> poses = new HashSet<Pos>();
        poses.add(pos);
        poses.add(pos.inDir(Dir8.E));
        poses.add(pos.inDir(Dir8.NE));
        poses.add(pos.inDir(Dir8.N));
        poses.add(pos.inDir(Dir8.NW));
        poses.add(pos.inDir(Dir8.W));
        return poses;
    }




}
