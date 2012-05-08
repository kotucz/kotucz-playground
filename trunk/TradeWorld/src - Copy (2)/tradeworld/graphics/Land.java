package tradeworld.graphics;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.image.TextureLoader;
import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Material;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.TextureUnitState;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.TexCoord2f;

/**
 *
 * @author Kotuc
 */
public class Land {

    private Point3d[][] heights;
    /**
     *  Number of tiles
     */
    private int widthx, widthy;
    private final int SIDE = 1;
    private BranchGroup objLand = new BranchGroup();
    private GeometryArray landGeometry;

    public BranchGroup getObjLand() {
        return objLand;
    }

    public Land() {
        initLandHeights(200, 200, 0.4);

//        Sphere objFloor = new Sphere();
//        objFloor.setAppearance(new Appearance());
//        objFloor.setAppearance();
        Appearance appear = createLandAppearance();
        this.landGeometry = createLandGeometry();
//        objFloor.setCollidable(false);
        Shape3D objFloor = new Shape3D(landGeometry, appear);
        objLand.addChild(objFloor);
    }

    private void initLandHeights(int widthx, int widthy, double random) {
        this.widthx = widthx;
        this.widthy = widthy;
        heights = new Point3d[widthx + 1][widthy + 1];
        for (int y = 0; y < this.widthy + 1; y++) {
            for (int x = 0; x < this.widthx + 1; x++) {
                heights[x][y] = new Point3d(
                        (double) (x * SIDE),
                        (double) (y * SIDE),
                        (double) (Math.random() * random));
            }
        }
    }

    private GeometryArray createLandGeometry() {

        GeometryArray geometry =
                new QuadArray((widthx) * (widthy) * 4,
                GeometryArray.COORDINATES | GeometryArray.COLOR_3 | GeometryArray.NORMALS | GeometryArray.TEXTURE_COORDINATE_2,
                2, new int[]{0, 1});
        // coordinates


        Point3d[] coords = generateCoords();
//        System.out.println("divne" + landGeometry + Arrays.toString(coords));
        geometry.setCoordinates(0, coords);

        geometry.setColors(0, generateColors(coords));

        geometry.setTextureCoordinates(0, 0, generateTexCoords(coords));
        geometry.setTextureCoordinates(1, 0, generateTexCoords(coords));

        { // normals
            GeometryInfo gi = new GeometryInfo(geometry);

            NormalGenerator ng = new NormalGenerator();
            ng.generateNormals(gi);

            geometry = gi.getGeometryArray();
        }

        geometry.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
        geometry.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
        geometry.setCapability(GeometryArray.ALLOW_COUNT_READ);
// 	     landGeometry.setCapability(GeometryArray.BY_REFERENCE);

        return geometry;
    }

    public Point3d[] generateCoords() {
        Point3d[] coords = new Point3d[(widthx) * (widthy) * 4];
        int i = 0;
        for (int y = 0; y < this.widthy; y++) {
            for (int x = 0; x < this.widthx; x++) {
                coords[i + 0] = heights[x][y];
                coords[i + 1] = heights[x + 1][y];
                coords[i + 2] = heights[x + 1][y + 1];
                coords[i + 3] = heights[x][y + 1];
                i += 4;
            }
        }
        return coords;
    }

    public Color3f[] generateColors(Point3d[] coords) { // colors
        Color3f[] colors = new Color3f[coords.length];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = colors[i] = new Color3f((float) Math.random(), (float) Math.random(), (float) Math.random());


        }
        return colors;
//        landGeometry.setColorRefFloat(new float[N * 3]);

    }

    public TexCoord2f[] generateTexCoordsEasy(Point3d[] coords) { // texture1
        TexCoord2f[] texs = new TexCoord2f[coords.length];
//        for (int i = 0; i < texs.length; i++) {
//            texs[i] = new TexCoord2f((float) Math.random(), (float) Math.random());
//        }
        for (int i = 0; i < texs.length; i += 4) {
            texs[i + 0] = new TexCoord2f(0, 0);
            texs[i + 1] = new TexCoord2f(1, 0);
            texs[i + 2] = new TexCoord2f(1, 1);
            texs[i + 3] = new TexCoord2f(0, 1);
        }
//        landGeometry.setColorRefFloat(new float[N * 3]);
//        landGeometry.setTexCoordRefFloat(0, texs);//ColorRefFloat(tex);
        return texs;
    }

    public TexCoord2f[] generateTexCoords(Point3d[] coords) { // texture1
        TexCoord2f[] texs = new TexCoord2f[coords.length];

        for (int i = 0; i < coords.length; i += 4) {
            TexCoord2f[] texs2 = helpTile((int) Math.round(Math.random()), (int) Math.round(Math.random()), 2, 2);
            for (int j = 0; j < 4; j++) {
                texs[i + j] = texs2[j];
            }
        }

        return texs;

    }

    public TexCoord2f[] helpTile(int texx, int texy, int texw, int texh) {
        TexCoord2f[] texs = new TexCoord2f[4];
        texs[0] = new TexCoord2f((float) (texx) / (texw), (float) (texy) / (texh));
        texs[1] = new TexCoord2f((float) (texx + 1) / (texw), (float) (texy) / (texh));
        texs[2] = new TexCoord2f((float) (texx + 1) / (texw), (float) (texy + 1) / (texh));
        texs[3] = new TexCoord2f((float) (texx) / (texw), (float) (texy + 1) / (texh));
        return texs;
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


//        {
////  landTexturing
//            try {
////            BufferedImage img = ImageIO.read(getClass().getResource("./res/images/land1.png"));
//                BufferedImage img1 = ImageIO.read(new File("./res/images/land1.png"));
//                BufferedImage img2 = ImageIO.read(new File("./res/images/land2.png"));
////            System.out.println("Image " + img + " " + img.getWidth() + "x" + img.getHeight());
//
////        TextureLoader loader = new TextureLoader("grass.jpg");
//                ImageComponent2D image1 = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, img1);//loader.getImage();
//                ImageComponent2D image2 = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, img2);//loader.getImage();
//
////            System.out.println("comp " + image.getWidth() + " x " + image.getHeight());
//
////            image.set(img);
////        if (image == null) {
////            System.out.println("no image");
////        }
//
////            Texture2D texture = new Texture2D();
////            texture.setImage(0, image);
//
////            Texture3D texture = new Texture3D();
//                Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
//                        image1.getWidth(), image1.getHeight());
////            texture.setImage(0, image);
////            texture.setImages(new ImageComponent[]{image1, image2});
//                texture.setImages(new ImageComponent[]{image1});
//
//                appear.setTexture(texture);
//            } catch (IOException ex) {
//                Logger.getLogger(Land.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }

        // texture units
        TextureUnitState tus[] = new TextureUnitState[2];

        // cover the floor with the first texture
//        tus[0] = loadTextureUnit("land1.png", TextureAttributes.DECAL);

        // add second texture (it has transparent parts)
        tus[1] = Tools.loadTextureUnit("road1.png", TextureAttributes.DECAL);

        // modulated light map
        tus[0] = Tools.loadTextureUnit("mega4.png", TextureAttributes.MODULATE);
        // tus[2] = lightMapTUS();

        appear.setTextureUnitState(tus);


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
//    private GeometryUpdater waterUpdater = new WaterUpdater();
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
