package kotuc.chaos;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.GeometryUpdater;
import javax.media.j3d.Material;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

/**
 *
 * @author Kotuc
 */
public class Land extends PhysicEntity {

    private Point3d[][] verts = null;
    private int widthx = 20,  widthy = 20;
    private final int EDGE = 1;
    private BranchGroup objLand = null;

    public Land() {
        initLandVerts(1);
        objLand = (BranchGroup) getMainNode();
        objLand.setPickable(false);
        Shape3D objFloor = new Shape3D(createLandGeometry());
//        Sphere objFloor = new Sphere();
//        objFloor.setAppearance(new Appearance());
        objFloor.setAppearance(createLandAppearance());
        objFloor.setCollidable(false);
        objLand.addChild(objFloor);
    }

    private void initLandVerts(double random) {
        verts = new Point3d[widthx][widthy];
        for (int y = 0; y < this.widthy; y++) {
            for (int x = 0; x < this.widthx; x++) {
                verts[x][y] = new Point3d(
                        (double) (x * EDGE),
                        (double) (y * EDGE),
                        (double) (Math.random() * random));
            }
        }
    }

    private GeometryArray createLandGeometry() {
        int N = (widthx) * (widthy) * 4;

        landGeometry =
                new QuadArray(N,
                GeometryArray.COORDINATES | GeometryArray.COLOR_3 | GeometryArray.NORMALS | GeometryArray.BY_REFERENCE //| GeometryArray.TEXTURE_COORDINATE_2
                );

        int i = 0;

        float[] coords = new float[N * 3];

        for (int y = 0; y < this.widthy - 1; y++) {
            for (int x = 0; x < this.widthx - 1; x++) {
//				Point3f v = new Point3f(verts[x][y]);
                coords[i * 3] = (float) verts[x][y].x;
                coords[i * 3 + 1] = (float) verts[x][y].y;
                coords[i * 3 + 2] = (float) verts[x][y].z;
                i++;
//				v = new Point3f(verts[x+1][y]);

                coords[i * 3] = (float) verts[x + 1][y].x;
                coords[i * 3 + 1] = (float) verts[x + 1][y].y;
                coords[i * 3 + 2] = (float) verts[x + 1][y].z;
                i++;
//				v = new Point3f(verts[x+1][y+1]);

                coords[i * 3] = (float) verts[x + 1][y + 1].x;
                coords[i * 3 + 1] = (float) verts[x + 1][y + 1].y;
                coords[i * 3 + 2] = (float) verts[x + 1][y + 1].z;
                i++;
//				v = new Point3f(verts[x][y+1]);

                coords[i * 3] = (float) verts[x][y + 1].x;
                coords[i * 3 + 1] = (float) verts[x][y + 1].y;
                coords[i * 3 + 2] = (float) verts[x][y + 1].z;
                i++;

            }

        }

        landGeometry.setCoordRefFloat(coords);
        landGeometry.setColorRefFloat(new float[N * 3]);

//      landGeometry.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
//		landGeometry.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
//		landGeometry.setCapability(GeometryArray.ALLOW_COUNT_READ);
//      landGeometry.setCapability(GeometryArray.BY_REFERENCE);

        GeometryInfo gi = new GeometryInfo(landGeometry);

        NormalGenerator ng = new NormalGenerator();
        ng.generateNormals(gi);

        landGeometry = gi.getGeometryArray();

        landGeometry.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
        landGeometry.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
        landGeometry.setCapability(GeometryArray.ALLOW_COUNT_READ);
// 	     landGeometry.setCapability(GeometryArray.BY_REFERENCE);

        return landGeometry;
    }

    private GeometryArray createLand3Geometry() {
        int N = (widthx) * (widthy) * 4;

        landGeometry =
                new QuadArray(N,
                GeometryArray.COORDINATES | GeometryArray.COLOR_3 //                                            | GeometryArray.NORMALS
                | GeometryArray.BY_REFERENCE //                                            | GeometryArray.TEXTURE_COORDINATE_2
                );

        int i = 0;

        float[] coords = new float[N * 3];

        for (int y = 0; y < this.widthy - 1; y++) {
            for (int x = 0; x < this.widthx - 1; x++) {
//				Point3f v = new Point3f(verts[x][y]);
                coords[i * 3] = (float) x;
                coords[i * 3 + 1] = (float) y;
                coords[i * 3 + 2] = 0.0f;
                i++;
//				v = new Point3f(verts[x+1][y]);

                coords[i * 3] = (float) x + 1.0f;
                coords[i * 3 + 1] = (float) y + 0.0f;
                coords[i * 3 + 2] = 0.0f;
                i++;
//				v = new Point3f(verts[x+1][y+1]);

                coords[i * 3] = (float) x + 1.0f;
                coords[i * 3 + 1] = (float) y + 1.0f;
                coords[i * 3 + 2] = 0.0f;
                i++;
//				v = new Point3f(verts[x][y+1]);

                coords[i * 3] = (float) x + 0.0f;
                coords[i * 3 + 1] = (float) y + 1.0f;
                coords[i * 3 + 2] = 0.0f;
                i++;

            }

        }

        landGeometry.setCoordRefFloat(coords);
        landGeometry.setColorRefFloat(new float[N * 3]);

//      landGeometry.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
//		landGeometry.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
//		landGeometry.setCapability(GeometryArray.ALLOW_COUNT_READ);
//      landGeometry.setCapability(GeometryArray.BY_REFERENCE);

//		GeometryInfo gi = new GeometryInfo(landGeometry);

//        NormalGenerator ng = new NormalGenerator();
//        ng.generateNormals(gi);

//        landGeometry = gi.getGeometryArray();

        landGeometry.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
        landGeometry.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
        landGeometry.setCapability(GeometryArray.ALLOW_COUNT_READ);
// 	     landGeometry.setCapability(GeometryArray.BY_REFERENCE);

        return landGeometry;
    }

    private Appearance createLandAppearance() {

        Appearance appear = new Appearance();

//        landAppearance.setColoringAttributes (new ColoringAttributes (new Color3f (0.0f, 0.0f, 1.0f),1));
//        landAppearance.setPolygonAttributes(new PolygonAttributes);

        Material ma = new Material();

        ma.setAmbientColor(new Color3f(0.32f, 0.32f, 0.32f));
        ma.setSpecularColor(new Color3f(0.2f, 0.2f, 0.2f));
        ma.setEmissiveColor(new Color3f(0.41f, 0.41f, 0.41f));
        ma.setDiffuseColor(new Color3f(1.0f, 1.0f, 1.0f));
        appear.setMaterial(ma);
//        ma.setShininess(1.0f);



//  landTexturing

//    	NewTextureLoader loader = new NewTextureLoader("grass.jpg");
// 	  	ImageComponent2D image = loader.getImage();
//		if (image==null) System.out.println("no image");
//		Texture2D texture = new Texture2D();
//     	texture.setImage(0, image);
//   	landAppearance.setTexture(texture);



//        TextureLoader loader = new TextureLoader("./images/grass.jpg", null);
//        ImageComponent2D image = loader.getImage();
//
//        if (image == null) {
//            System.out.println("load failed for texture grass.jpg");
//        }
//
//        Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
//                image.getWidth(), image.getHeight());
//        texture.setImage(0, image);
//        appear.setTexture(texture);
//
//        TextureAttributes ta = new TextureAttributes();
//        ta.setTextureMode(TextureAttributes.MODULATE);
//        appear.setTextureAttributes(ta);
//        appear.setTexCoordGeneration(new TexCoordGeneration(TexCoordGeneration.OBJECT_LINEAR, TexCoordGeneration.TEXTURE_COORDINATE_2));


//		return new Appearance();

        return appear;
    }
    private GeometryUpdater waterUpdater = new WaterUpdater();
    private GeometryArray landGeometry;
    /*
    void initPhaseMap() {
    phaseMap=new int[widthx][widthy][];
    for (int x=0; x<widthx; x++) {
    for (int y=0; y<widthy; y++) {
    if (true) {
    phaseMap[x][y]=new int[1];
    phaseMap[x][y][0]=SOLID;
    } else {
    phaseMap[x][y]=new int[5];
    phaseMap[x][y][0]=SOLID;
    }
    }
    }

    createPhaseMap();

    }
     */
    /*
    BranchGroup objPhaseMap;

    public BranchGroup createPhaseMap () {

    objPhaseMap = new BranchGroup();

    for (int x=0; x<phaseMap.length; x++) {
    for (int y=0; y<phaseMap[x].length; y++) {
    for (int z=0; z<phaseMap[x][y].length; z++) {

    Appearance phaseAppear = new Appearance();
    phaseAppear.setPolygonAttributes(new PolygonAttributes(PolygonAttributes.POLYGON_LINE, PolygonAttributes.CULL_NONE, 0.0f));

    switch (phaseMap[x][y][z]) {
    case SOLID :
    phaseAppear.setColoringAttributes(new ColoringAttributes(0.0f, 1.0f, 0.0f, ColoringAttributes.FASTEST));
    break;
    case WATER :
    phaseAppear.setColoringAttributes(new ColoringAttributes(0.0f, 0.0f, 1.0f, ColoringAttributes.FASTEST));
    break;
    case AIR :
    phaseAppear.setColoringAttributes(new ColoringAttributes(1.0f, 1.0f, 1.0f, ColoringAttributes.FASTEST));
    break;
    default :
    break;
    }

    Transform3D t3dSet = new Transform3D();
    t3dSet.set(new Vector3f((float)x+0.5f, (float)y+0.5f, (float)z-0.5f));
    TransformGroup objTrans = new TransformGroup(t3dSet);

    objTrans.addChild(new Box(0.49f, 0.49f, 0.49f, phaseAppear));
    objPhaseMap.addChild(objTrans);
    }
    }
    }

    objLand.addChild(objPhaseMap);

    return objPhaseMap;
    }

    int getPhase (Point3d point) {
    int x= (int)Math.floor(point.x);
    int y= (int)Math.floor(point.y);
    int z= (int)Math.floor(point.z)-1;

    if ((x<phaseMap.length)&&x>=0&&(y<phaseMap[x].length)&&y>=0&&(z<phaseMap[x][y].length)&&z>=0)
    return phaseMap[x][y][z];
    return AIR;
    }
     */
}
