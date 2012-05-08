package tradeworld.graphics;

import tradeworld.RoadNetwork;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import javax.media.j3d.Appearance;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Material;
import javax.media.j3d.PickRay;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TextureAttributes;
import javax.media.j3d.TextureUnitState;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3d;

/**
 *
 * @author Kotuc
 */
public class Land3D extends Model3D {

    private Point3d[][] heighmap;
    /**
     *  Number of tiles
     */
    private int widthx, widthy;
    private final double SIDE = 1;
    private GeometryArray landGeometry;

    public Land3D(int widthx, int widthy) {
        initHeighMap(widthx, widthy, 0.0);

//        Sphere objFloor = new Sphere();
//        objFloor.setAppearance(new Appearance());
//        objFloor.setAppearance();
        Appearance appear = createLandAppearance();
        this.landGeometry = createLandGeometry();
//        objFloor.setCollidable(false);
        Shape3D objFloor = new Shape3D(landGeometry, appear);
        generateTexCoords();
        roadTextures();
        objFloor.setName("Land tiles");
        System.out.println("objFloor: " + objFloor);
        addChild(objFloor);
    }

    private void initHeighMap(int widthx, int widthy, double random) {
        this.widthx = widthx;
        this.widthy = widthy;
        heighmap = new Point3d[widthx + 1][widthy + 1];
        for (int y = 0; y < this.widthy + 1; y++) {
            for (int x = 0; x < this.widthx + 1; x++) {
                heighmap[x][y] = new Point3d(
                        (x * SIDE),
                        (y * SIDE),
                        (random * Math.random()));
            }
        }
    }

    public Point3d pick(PickRay pickRay) {
        Point3d point = new Point3d();
        Vector3d vec = new Vector3d();
        pickRay.get(point, vec);
        double t = -point.z / vec.z;
        vec.scale(t);
        point.add(vec);
        return point;
    }

    private GeometryArray createLandGeometry() {

        GeometryArray geometry =
                new QuadArray((widthx) * (widthy) * 4,
                GeometryArray.COORDINATES | GeometryArray.COLOR_3 | GeometryArray.NORMALS | GeometryArray.TEXTURE_COORDINATE_2,
                2, new int[]{0, 1});
        // coordinates


        Point3d[] coords = generateTilesCoords();
//        System.out.println("divne" + landGeometry + Arrays.toString(coords));
        geometry.setCoordinates(0, coords);

        geometry.setColors(0, generateColors(coords));



//        geometry.setTextureCoordinates(0, 0, generateTexCoords(coords));
//        geometry.setTextureCoordinates(1, 0, generateTexCoords(coords));

        { // normals
            GeometryInfo gi = new GeometryInfo(geometry);

            NormalGenerator ng = new NormalGenerator();
            ng.generateNormals(gi);

            geometry = gi.getGeometryArray();
        }

        geometry.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
        geometry.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
        geometry.setCapability(GeometryArray.ALLOW_COUNT_READ);
        geometry.setCapability(GeometryArray.ALLOW_TEXCOORD_WRITE);
// 	     landGeometry.setCapability(GeometryArray.BY_REFERENCE);

        return geometry;
    }

    public Point3d[] generateTilesCoords() {
        Point3d[] coords = new Point3d[(widthx) * (widthy) * 4];
        int i = 0;
        for (int y = 0; y < this.widthy; y++) {
            for (int x = 0; x < this.widthx; x++) {
                coords[i + 0] = heighmap[x][y];
                coords[i + 1] = heighmap[x + 1][y];
                coords[i + 2] = heighmap[x + 1][y + 1];
                coords[i + 3] = heighmap[x][y + 1];
                i += 4;
            }
        }
        return coords;
    }

    public Point3d[] generateTileCoords(int tilex, int tiley) {
        Point3d[] coords = new Point3d[4];
        coords[0] = heighmap[tilex][tiley];
        coords[1] = heighmap[tilex + 1][tiley];
        coords[2] = heighmap[tilex + 1][tiley + 1];
        coords[3] = heighmap[tilex][tiley + 1];
        return coords;
    }

    public Color3f[] generateColors(Point3d[] coords) { // colors
        Color3f[] colors = new Color3f[coords.length];
        for (int i = 0; i <
                colors.length; i++) {
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

        for (int i = 0; i < texs.length; i++) {
            texs[i] = new TexCoord2f();
//            TexCoord2f[] texs2 = helpTile((int) Math.round(Math.random()), (int) Math.round(Math.random()), 2, 2);
//            for (int j = 0; j < 4; j++) {
//                texs[i + j] = texs2[j];
//            }
        }

        return texs;

    }

    public void generateTexCoords() { // texture1

        for (int x = 0; x < widthx; x++) {
            for (int y = 0; y < widthy; y++) {
                TextureSelect textureSelect = new TextureSelect(
                        (int) Math.round(Math.random()), (int) Math.round(Math.random()), 2, 2,
                        1, 0);
                selectTexture(0, x, y, textureSelect);
            }
        }

//        for (int x = 0; x < widthx; x++) {
//            for (int y = 0; y < widthy; y++) {
//                TextureSelect textureSelect = new TextureSelect(
//                        new Random().nextInt(8), (int) Math.round(Math.random()), 8, 1,
//                        0, 0);
//                selectTexture(1, x, y, textureSelect);
//            }
//        }

//        for (int i = 0; i < coords.length; i += 4) {
//            TexCoord2f[] texs2 = helpTile((int) Math.round(Math.random()), (int) Math.round(Math.random()), 2, 2);
//            for (int j = 0; j < 4; j++) {
//                texs[i + j] = texs2[j];
//            }
//        }

//        return texs;

    }
    private RoadNetwork roadNetwork;

    public RoadNetwork getRoadNetwork() {
        return roadNetwork;
    }
    final TextureSelect[] selects = new TextureSelect[]{
        new TextureSelect(0, 0, 8, 1, 0, 0), // 0000
        new TextureSelect(1, 0, 8, 1, 2, 0), // 0001
        new TextureSelect(1, 0, 8, 1, 1, 0), // 0010
        new TextureSelect(2, 0, 8, 1, 2, 0), // 0011
        new TextureSelect(1, 0, 8, 1, 0, 0), // 0100
        new TextureSelect(3, 0, 8, 1, 1, 0), // 0101
        new TextureSelect(2, 0, 8, 1, 1, 0), // 0110
        new TextureSelect(4, 0, 8, 1, 0, 0), // 0111
        new TextureSelect(1, 0, 8, 1, 3, 0), // 1000
        new TextureSelect(2, 0, 8, 1, 3, 0), // 1001
        new TextureSelect(3, 0, 8, 1, 0, 0), // 1010
        new TextureSelect(4, 0, 8, 1, 1, 0), // 1011
        new TextureSelect(2, 0, 8, 1, 0, 0), // 1100
        new TextureSelect(4, 0, 8, 1, 2, 0), // 1101
        new TextureSelect(4, 0, 8, 1, 3, 0), // 1110
        new TextureSelect(5, 0, 8, 1, 0, 0) //  1111
    };

    void roadTextures() {

//        int[][] roads = new int[widthx + 1][widthy + 1];
//
//        for (int x = 0; x < widthx + 1; x++) {
//            for (int y = 0; y < widthy + 1; y++) {
//                if (Math.random() < 0.3) {
//                    roads[x][y] = 1;
//                }
//            }
//        }

        roadNetwork = new RoadNetwork(this);



        for (int x = 0; x < widthx; x++) {
            for (int y = 0; y < widthy; y++) {
//                int hash = (roads[x][y] * 8) +
//                (roads[x + 1][y] * 4) +
//                        (roads[x + 1][y + 1] * 2) +
//                (roads[x][y + 1] * 1);
                correctRoadTile(roadNetwork, x, y);
            }
        }
    }

    public void correctRoadTile(RoadNetwork roadNetwork, int x, int y) {
        if ((x < 0) || (y < 0) || (widthx <= x) || (widthy <= y)) {
            return;
        }
        int hash = ((roadNetwork.getPoint(x, y) != null) ? 8 : 0) +
                ((roadNetwork.getPoint(x + 1, y) != null) ? 4 : 0) +
                ((roadNetwork.getPoint(x + 1, y + 1) != null) ? 2 : 0) +
                ((roadNetwork.getPoint(x, y + 1) != null) ? 1 : 0);
        selectTexture(1, x, y, selects[hash]);
    }

    /**
     * Returns first index of tile.
     * @param tilex
     * @param tiley
     * @return
     */
    private int offset(int tilex, int tiley) {
        if (tilex < 0 || widthx <= tilex) {
            throw new ArrayIndexOutOfBoundsException("tilex " + tilex);
        }
        return 4 * (tilex + widthx * tiley);
    }

    public void selectTexture(int layer, int tilex, int tiley, TextureSelect select) {
        try {
            int offset = offset(tilex, tiley);
            landGeometry.setTextureCoordinates(layer, offset, select.texs);
        } catch (Exception e) {
            System.err.println("EX " + e.getMessage());
        }
    }

    public int getWidthx() {
        return widthx;
    }

    public int getWidthy() {
        return widthy;
    }

    public class TextureSelect {

        public static final int FLIP_NO = 0;
        public static final int FLIP_RL = 1;
        public static final int FLIP_TB = 3;
        public static final int ROT_0 = 0;
        public static final int ROT_90 = 1;
        public static final int ROT_180 = 2;
        public static final int ROT_270 = 3;
        private final TexCoord2f[] texs = new TexCoord2f[4];

        public TextureSelect(int texx, int texy, int texw, int texh) {
            this(texx, texy, texw, texh, ROT_0, FLIP_NO);
        }

        /**
         * Rotate first, Flip second.
         * @param texx
         * @param texy
         * @param texw
         * @param texh
         * @param rot
         * @param mirr
         */
        public TextureSelect(int texx, int texy, int texw, int texh, int rot, int mirr) {
            texs[((0 + rot) & 3) ^ mirr] = new TexCoord2f((float) (texx) / (texw), (float) (texy) / (texh));
            texs[((1 + rot) & 3) ^ mirr] = new TexCoord2f((float) (texx + 1) / (texw), (float) (texy) / (texh));
            texs[((2 + rot) & 3) ^ mirr] = new TexCoord2f((float) (texx + 1) / (texw), (float) (texy + 1) / (texh));
            texs[((3 + rot) & 3) ^ mirr] = new TexCoord2f((float) (texx) / (texw), (float) (texy + 1) / (texh));
        }
    }
//        public TextureSelect(int texx, int texy, int texw, int texh, int rot, int mirr) {
//            texs[0] = new TexCoord2f((float) (texx) / (texw), (float) (texy) / (texh));
//            texs[1] = new TexCoord2f((float) (texx + 1) / (texw), (float) (texy) / (texh));
//            texs[2] = new TexCoord2f((float) (texx + 1) / (texw), (float) (texy + 1) / (texh));
//            texs[3] = new TexCoord2f((float) (texx) / (texw), (float) (texy + 1) / (texh));
//        }

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
        tus[1] = Tools.loadTextureUnit("road-8-8.png", TextureAttributes.DECAL);

        // modulated light map
        tus[0] = Tools.loadTextureUnit("mega4.png", TextureAttributes.DECAL);
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
