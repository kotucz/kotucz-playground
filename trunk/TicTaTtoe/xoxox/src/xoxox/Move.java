package xoxox;

/**
 *
 * @author xkotula
 */
public class Move {

    final int x, y;

//    final Sign s;

    public Move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Move add(Dir dir, int mul) {
        return new Move(x+dir.dx*mul, y+dir.dy*mul);
    }


//    public Move(int x, int y, Sign s) {
//        this.x = x;
//        this.y = y;
//        this.s = s;
//    }

    

}
