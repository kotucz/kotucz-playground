package kotucz.village.tiles;

import com.jme3.math.Vector3f;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.AbstractBox;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;

/**
 * A box with solid (filled) faces.
 * 
 * @author Kotuc
 */
public class TileGrid extends AbstractBox {

    Multitexture mtex = new Multitexture(16*4, 16*4);
    
    int sizex = 16;
    int sizey = 16;
    int numtiles = sizex * sizey;
    // evry tile has two triangles - 6 vertices
    private final short[] GEOMETRY_INDICES_DATA = new short[6*numtiles]; 
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
    private final float[] GEOMETRY_TEXTURE_DATA = new float[4 * 2 * numtiles];
//        {
//        1, 0, 0, 0, 0, 1, 1, 1, // back
//        1, 0, 0, 0, 0, 1, 1, 1, // right
//        1, 0, 0, 0, 0, 1, 1, 1, // front
//        1, 0, 0, 0, 0, 1, 1, 1, // left
//        1, 0, 0, 0, 0, 1, 1, 1, // top
//        1, 0, 0, 0, 0, 1, 1, 1 // bottom
//    };

    public void setTexture(int x, int y, int tex) {
      setTexture(index(x, y), mtex.getTex(tex)); 
      
    }    
        
    public void setTexture(int face, Subtexture s) {


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
        System.arraycopy(texs, 0, GEOMETRY_TEXTURE_DATA, off, texs.length);

        updateGeometry();

    }

    /**
     * 
     * Creates a new box.
     * <p>
     * The box has the given center and extends in the out from the center by
     * the given amount in <em>each</em> direction. So, for example, a box
     * with extent of 0.5 would be the unit cube.
     * 
     * @param center the center of the box.
     * @param x the size of the box along the x axis, in both directions.
     * @param y the size of the box along the y axis, in both directions.
     * @param z the size of the box along the z axis, in both directions.
     */
    public TileGrid() {
        super();
        for (int i = 0; i < numtiles; i++) {
            
            
            
            setTexture(i, mtex.createSubtexture(16*(i%3), 16*(i%3), 16*(i%3)+16, 16*(i%3)+16));    
            
        }
        
        updateGeometry();
    }

    /**
     * Constructor instantiates a new <code>Box</code> object.
     * <p>
     * The minimum and maximum point are provided, these two points define the
     * shape and size of the box but not it's orientation or position. You should
     * use the {@link com.jme3.scene.Spatial#setLocalTranslation(com.jme3.math.Vector3f) }
     * and {@link com.jme3.scene.Spatial#setLocalRotation(com.jme3.math.Quaternion) }
     * methods to define those properties.
     * 
     * @param min the minimum point that defines the box.
     * @param max the maximum point that defines the box.
     */


    protected void duUpdateGeometryIndices() {
//        if (getBuffer(Type.Index) == null) {
           
//        }
    }

//    protected void duUpdateGeometryNormals() {
//        if (getBuffer(Type.Normal) == null) {
//            setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(GEOMETRY_NORMALS_DATA));
//        }
//    }

    protected void duUpdateGeometryTextures() {
//        if (getBuffer(Type.TexCoord) == null){
       
//        }
    }

    protected void duUpdateGeometryVertices() {

        final float[] allll = new float[4 * 3 * numtiles];
        final float[] normals = new float[4 * 3 * numtiles];
//        Vector3f[] v = computeVertices();


        for (int y = 0; y < sizey; y++) {
            for (int x = 0; x < sizex; x++) {
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

                System.arraycopy(name, 0, allll, 4 * 3 * index(x, y), name.length);

                                                         
                
                
                GEOMETRY_INDICES_DATA[index(x, y) * 6 + 0] = (short)(index(x, y)*4 + 0);
                GEOMETRY_INDICES_DATA[index(x, y) * 6 + 1] = (short)(index(x, y)*4 + 1);
                GEOMETRY_INDICES_DATA[index(x, y) * 6 + 2] = (short)(index(x, y)*4 + 2);
                GEOMETRY_INDICES_DATA[index(x, y) * 6 + 3] = (short)(index(x, y)*4 + 0);
                GEOMETRY_INDICES_DATA[index(x, y) * 6 + 4] = (short)(index(x, y)*4 + 2);
                GEOMETRY_INDICES_DATA[index(x, y) * 6 + 5] = (short)(index(x, y)*4 + 3);
                
                
                
            }
            
            
            
            
        }

        for (int i = 2; i < normals.length; i += 3) {
            normals[i] = 1; // z up

        }

         setBuffer(Type.Index, 3, BufferUtils.createShortBuffer(GEOMETRY_INDICES_DATA));

        {
            FloatBuffer positionBuffer = BufferUtils.createVector3Buffer(4 * numtiles);
            positionBuffer.put(allll);
            setBuffer(Type.Position, 3, positionBuffer);
        }
        {
            FloatBuffer normalbuffer = BufferUtils.createVector3Buffer(4 * numtiles);
            normalbuffer.put(normals);
            setBuffer(Type.Normal, 3, normalbuffer);
        }

         setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(GEOMETRY_TEXTURE_DATA));
        
        updateBound();
    }

    private int index(int tilex, int tiley) {
        assertTileBounds(tilex, tiley);
        return (tilex + sizex * tiley);
    }

    void assertTileBounds(int tilex, int tiley) {

        if ((tilex < 0 || sizex <= tilex || tiley < 0 || sizey <= tiley)) {
            throw new ArrayIndexOutOfBoundsException("tile out of bounds " + tilex + ", " + tiley);
        }
    }

    @Override
    protected void duUpdateGeometryNormals() {
        // used elsewhere;
    }
}
