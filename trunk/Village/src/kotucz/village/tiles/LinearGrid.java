package kotucz.village.tiles;

import java.util.Iterator;

/**
 * @author Kotuc
 */
public final class LinearGrid implements Iterable<Tile> {

    final int sizeX;
    final int sizeY;

    public LinearGrid(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int getTotalNum() {

        return sizeX * sizeY;
    }

    public int index(int tilex, int tiley) {
        assertTileBounds(tilex, tiley);
        return (tilex + sizeX * tiley);
    }

    public int index(Pos pos) {
        return index(pos.x, pos.y);

    }

    void assertTileBounds(int tilex, int tiley) {

        if (isOutOfBounds(tilex, tiley)) {
            throw new ArrayIndexOutOfBoundsException("tile out of bounds " + tilex + ", " + tiley);
        }
    }

    public boolean isOutOfBounds(int tilex, int tiley) {
        return (tilex < 0 || sizeX <= tilex || tiley < 0 || sizeY <= tiley);
    }

    public boolean isOutOfBounds(Pos pos) {
        return isOutOfBounds (pos.x, pos.y);
    }


    public int getX(int index) {
        return index % sizeX;
    }

    public int getY(int index) {
        return index / sizeX;
    }

    public Iterator<Tile> iterator() {
        return new Iterator<Tile>() {

            int i = -1;

            public boolean hasNext() {
                return i < getTotalNum() - 1;
            }

            public Tile next() {
                i++;
                return new Tile(getX(i), getY(i), i);
            }

            public void remove() {
                throw new UnsupportedOperationException("Not supported.");
            }
        };
    }

    public Pos getPos(int index) {
        return new Pos(getX(index), getY(index));
    }


}
