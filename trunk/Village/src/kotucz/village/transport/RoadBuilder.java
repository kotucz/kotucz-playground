package kotucz.village.transport;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.sun.istack.internal.Nullable;
import kotucz.village.common.Dir;
import kotucz.village.common.Dir4;
import kotucz.village.game.GameMap;
import kotucz.village.tiles.Pos;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Kotuc
 */
public class RoadBuilder {

    GameMap map;

    private final GameMap gameMap;
    private final UnidirectionalPathNetwork pnet;

    public RoadBuilder(GameMap gameMap, UnidirectionalPathNetwork pnet) {
        this.gameMap = gameMap;
        this.pnet = pnet;
    }

    public List<Pos> buildPath(Pos from, Pos to) {
        List<Pos> path = PathFinding.findPath(new PathGraph<Pos>() {
            @Override
            public Iterable<Pos> getNexts(final Pos pos) {

                List<Pos> list = new LinkedList<Pos>();

                for (Dir dir4 : Dir4.values()) {
                    Pos pos1 = pos.inDir(dir4);
                    if (pnet.lingrid.isOutOfBounds(pos1)) {
                        continue;
                    } else if (gameMap.isBuildable(pos)) {

                        list.add(pos1);
                    }


                }
                return list;


//                return Iterables.transform(Arrays.asList(Dir4.values()), new Function<Dir, Pos>() {
//                    @Override
//                    public Pos apply(@Nullable Dir dir) {
//                        return pos.inDir(dir);
//                    }
//                });
            }

            @Override
            public double getEdgeDistance(Pos from, Pos to) {
                return 1;
            }

        }, from, to);

        return path;

//        pnet.build(path);
    }




}
