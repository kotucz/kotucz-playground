package kotucz.village.tiles;

import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;

/**
 * A box with solid (filled) faces.
 * 
 * @author Kotuc
 */
public class MeshTileGrid extends Mesh {

    final LinearGrid lingrid;
    // evry tile has two triangles - 6 vertices
//    GEOMETRY_INDICES_DATA; 
//    {
//      `  2, 1, 0, 3, 2, 0, // back
//        6, 5, 4, 7, 6, 4, // right
//        10, 9, 8, 11, 10, 8, // front
//        14, 13, 12, 15, 14, 12, // left
//        18, 17, 16, 19, 18, 16, // top
//        22, 21, 20, 23, 22, 20 // bottom
//    };
//    private final float[] GEOMETRY_NORMALS_DATA = {
//        0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, // back
//        1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, // right
//        0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, // front
//        -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, // left
//        0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, // top
//        0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0 // bottom
//    };
    private final float[] geometryTextureDate;
//        {
//        1, 0, 0, 0, 0, 1, 1, 1, // back
//        1, 0, 0, 0, 0, 1, 1, 1, // right
//        1, 0, 0, 0, 0, 1, 1, 1, // front
//        1, 0, 0, 0, 0, 1, 1, 1, // left
//        1, 0, 0, 0, 0, 1, 1, 1, // top
//        1, 0, 0, 0, 0, 1, 1, 1 // bottom
//    };

    public void setTexture(Pos pos, Subtexture s) {

        setTexture(lingrid.index(pos), s);
    }

    public void setTexture(int x, int y, Subtexture s) {

        setTexture(lingrid.index(x, y), s);
    }

    public void setTexture(int face, Subtexture s) {

//        int face = index(x, y);

//        float startU = 0.5f;   //     X
//        float endU = 1;
//        
//        float startV = 0;   //     Y 
//        float endV = 0.5f;     

        final float[] texs = new float[]{
            s.startU, s.startV,
            s.endU, s.startV,
            s.endU, s.endV,
            s.startU, s.endV
        };

        int off = 8 * face;
        System.arraycopy(texs, 0, geometryTextureDate, off, texs.length);

//        createGeometry();

    }

    public MeshTileGrid(LinearGrid lgrid) {
        super();
        this.lingrid = lgrid;



        createGeometry();

        geometryTextureDate = new float[4 * 2 * lingrid.getTotalNum()];



    }

    void updateTexture() {
        setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(geometryTextureDate));
    }

    private void createGeometry() {

        final float[] coordinates = new float[4 * 3 * lingrid.getTotalNum()];
        final float[] normals = new float[4 * 3 * lingrid.getTotalNum()];
//        Vector3f[] v = computeVertices();

        final short[] geometryIndices = new short[6 * lingrid.getTotalNum()];

        for (int y = 0; y < lingrid.getSizeY(); y++) {
            for (int x = 0; x < lingrid.getSizeX(); x++) {
                Vector3f[] v = new Vector3f[]{
                    new Vector3f(x, y, 0),
                    new Vector3f(x + 1, y, 0),
                    new Vector3f(x + 1, y + 1, 0),
                    new Vector3f(x, y + 1, 0)
                };
                final float[] name = new float[]{
                    v[0].x, v[0].y, v[0].z,
                    v[1].x, v[1].y, v[1].z,
                    v[2].x, v[2].y, v[2].z,
                    v[3].x, v[3].y, v[3].z
                };

                System.arraycopy(name, 0, coordinates, 4 * 3 * lingrid.index(x, y), name.length);

                geometryIndices[lingrid.index(x, y) * 6 + 0] = (short) (lingrid.index(x, y) * 4 + 0);
                geometryIndices[lingrid.index(x, y) * 6 + 1] = (short) (lingrid.index(x, y) * 4 + 1);
                geometryIndices[lingrid.index(x, y) * 6 + 2] = (short) (lingrid.index(x, y) * 4 + 2);
                geometryIndices[lingrid.index(x, y) * 6 + 3] = (short) (lingrid.index(x, y) * 4 + 0);
                geometryIndices[lingrid.index(x, y) * 6 + 4] = (short) (lingrid.index(x, y) * 4 + 2);
                geometryIndices[lingrid.index(x, y) * 6 + 5] = (short) (lingrid.index(x, y) * 4 + 3);



            }




        }

        for (int i = 2; i < normals.length; i += 3) {
            normals[i] = 1; // z up

        }

        setBuffer(Type.Index, 3, BufferUtils.createShortBuffer(geometryIndices));

        {
            FloatBuffer positionBuffer = BufferUtils.createVector3Buffer(4 * lingrid.getTotalNum());
            positionBuffer.put(coordinates);
            setBuffer(Type.Position, 3, positionBuffer);
        }
        {
            FloatBuffer normalbuffer = BufferUtils.createVector3Buffer(4 * lingrid.getTotalNum());
            normalbuffer.put(normals);
            setBuffer(Type.Normal, 3, normalbuffer);
        }



        updateBound();
    }
}
