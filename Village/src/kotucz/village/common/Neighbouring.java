package kotucz.village.common;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Kotuc
 * Date: 10.6.12
 * Time: 13:05
 * To change this template use File | Settings | File Templates.
 */
public enum Neighbouring {

    N4 {
        @Override
        public Dir[] getDirections() {
           return Dir4.values();
        }
    },
    N8 {
        @Override
        public Dir[] getDirections() {
           return Dir8.values();
        }
    };

    public abstract Dir[] getDirections();

    public Dir randomDir(Random random) {
        Dir[] directions = getDirections();

        return directions[random.nextInt(directions.length)];
    }

}
