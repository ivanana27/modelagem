package OBJLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.StringTokenizer;
import com.jogamp.opengl.GL;

public class OBJModel
{
  private static final float DUMMY_Z_TC = -5.0F;
  private ArrayList<Tuple3> verts;
  private ArrayList<Tuple3> normals;
  private ArrayList<Tuple3> texCoords;
  private boolean hasTCs3D;
  private boolean flipTexCoords;
  private Faces faces;
  private FaceMaterials faceMats;
  private Materials materials;
  private ModelDimensions modelDims;
  private ModelDimensions modelDimsScaled;
  private String modelNm;
  private float maxSize;
  private int modelDispList;

  public OBJModel(String paramString, GL paramGL)
  {
    this(paramString, 1.0F, paramGL, false);
  }

  public OBJModel(String paramString, float paramFloat, GL paramGL, boolean paramBoolean)
  {
    this.hasTCs3D = true;

    this.flipTexCoords = false;

    this.modelNm = paramString;
    this.maxSize = paramFloat;
    initModelData(this.modelNm);

    loadModel(this.modelNm);
    centerScale();
    drawToList(paramGL);

    if (paramBoolean)
      reportOnModel();
  }

  public ModelDimensions getModelDims()
  {
    return this.modelDimsScaled;
  }

  private void initModelData(String paramString) {
    this.verts = new ArrayList();
    this.normals = new ArrayList();
    this.texCoords = new ArrayList();

    this.faces = new Faces(this.verts, this.normals, this.texCoords);
    this.faceMats = new FaceMaterials();
    this.modelDims = new ModelDimensions();
  }

  private void loadModel(String paramString)
  {
    String str = paramString + ".obj";
    try {
      System.out.println("Loading model from " + str + " ...");
      File localFile = new File(str);
      BufferedReader localBufferedReader = new BufferedReader(new FileReader(localFile));

      readModel(localBufferedReader, localFile.getParent());
      localBufferedReader.close();
    }
    catch (IOException localIOException) {
      System.out.println(localIOException.getMessage());
      System.exit(1);
    }
  }

  private void readModel(BufferedReader paramBufferedReader, String paramString)
  {
    boolean bool1 = true;
    if (paramString == null) paramString = "";

    int i = 0;

    boolean bool2 = true;
    boolean bool3 = true;
    int j = 0;
    try
    {
      String str;
	while (((str = paramBufferedReader.readLine()) != null) && (bool1)) {
        ++i;
        if (str.length() > 0);
         str = str.trim();

        if (str.startsWith("v ")) {
          bool1 = addVert(str, bool2);
          if (bool2);
          bool2 = false;
        }
        if (str.startsWith("vt")) {
          bool1 = addTexCoord(str, bool3);
          if (bool3);
          bool3 = false;
        }
        if (str.startsWith("vn"))
          bool1 = addNormal(str);
        if (str.startsWith("f ")) {
          bool1 = this.faces.addFace(str);
          ++j;
        }
        if (str.startsWith("mtllib "))
          this.materials = new Materials(paramString, str.substring(7));
        if (str.startsWith("usemtl "))
          this.faceMats.addUse(j, str.substring(7));
        if (str.charAt(0) == 'g') {
          continue;
        }
        if (str.charAt(0) == 's') {
          continue;
        }
        if (str.charAt(0) == '#') {
          continue;
        }
        System.out.println("Ignoring line " + i + " : " + str);
      }
    }
    catch (IOException localIOException)
    {
      System.out.println(localIOException.getMessage());
      System.exit(1);
    }

    if (!(bool1)) {
      System.out.println("Error loading model");
      System.exit(1);
    }
  }

  private boolean addVert(String paramString, boolean paramBoolean)
  {
    Tuple3 localTuple3 = readTuple3(paramString);
    if (localTuple3 != null) {
      this.verts.add(localTuple3);
      if (paramBoolean)
        this.modelDims.set(localTuple3);
      else
        this.modelDims.update(localTuple3);
      return true;
    }
    return false;
  }

  private Tuple3 readTuple3(String paramString)
  {
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, " ");
    localStringTokenizer.nextToken();
    try
    {
      float f1 = Float.parseFloat(localStringTokenizer.nextToken());
      float f2 = Float.parseFloat(localStringTokenizer.nextToken());
      float f3 = Float.parseFloat(localStringTokenizer.nextToken());

      return new Tuple3(f1, f2, f3);
    }
    catch (NumberFormatException localNumberFormatException) {
      System.out.println(localNumberFormatException.getMessage());
    }
    return null;
  }

  private boolean addTexCoord(String paramString, boolean paramBoolean)
  {
    if (paramBoolean) {
      this.hasTCs3D = checkTC3D(paramString);
      System.out.println("Using 3D tex coords: " + this.hasTCs3D);
    }

    Tuple3 localTuple3 = readTCTuple(paramString);
    if (localTuple3 != null) {
      this.texCoords.add(localTuple3);
      return true;
    }
    return false;
  }

  private boolean checkTC3D(String paramString)
  {
    String[] arrayOfString = paramString.split("\\s+");
    return (arrayOfString.length == 4);
  }

  private Tuple3 readTCTuple(String paramString)
  {
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, " ");
    localStringTokenizer.nextToken();
    try
    {
      float f1 = Float.parseFloat(localStringTokenizer.nextToken());
      float f2 = Float.parseFloat(localStringTokenizer.nextToken());

      float f3 = -5.0F;
      if (this.hasTCs3D) {
        f3 = Float.parseFloat(localStringTokenizer.nextToken());
      }
      return new Tuple3(f1, f2, f3);
    }
    catch (NumberFormatException localNumberFormatException) {
      System.out.println(localNumberFormatException.getMessage());
    }
    return null;
  }

  private boolean addNormal(String paramString)
  {
    Tuple3 localTuple3 = readTuple3(paramString);
    if (localTuple3 != null) {
      this.normals.add(localTuple3);
      return true;
    }
    return false;
  }

  private void centerScale()
  {
    Tuple3 localTuple31 = this.modelDims.getCenter();

    float f1 = 1.0F;
    float f2 = this.modelDims.getLargest();

    if (f2 != 0.0F)
      f1 = this.maxSize / f2;
    System.out.println("Scale factor: " + f1);

    this.modelDimsScaled = new ModelDimensions();

    for (int i = 0; i < this.verts.size(); ++i) {
      Tuple3 localTuple32 = (Tuple3)this.verts.get(i);

      float f3 = (localTuple32.getX() - localTuple31.getX()) * f1;
      localTuple32.setX(f3);
      float f4 = (localTuple32.getY() - localTuple31.getY()) * f1;
      localTuple32.setY(f4);
      float f5 = (localTuple32.getZ() - localTuple31.getZ()) * f1;
      localTuple32.setZ(f5);

      if (i == 0)
        this.modelDimsScaled.set(localTuple32);
      else
        this.modelDimsScaled.update(localTuple32);
    }
  }

  private void drawToList(GL paramGL)
  {
    this.modelDispList = paramGL.glGenLists(1);
    paramGL.glNewList(this.modelDispList, 4864);

    draw(paramGL);

    paramGL.glEndList();
  }

  public void drawList(GL paramGL)
  {
    paramGL.glCallList(this.modelDispList);
  }

  public void draw(GL paramGL)
  {
    paramGL.glPushMatrix();

    for (int i = 0; i < this.faces.getNumFaces(); ++i) {
      String str = this.faceMats.findMaterial(i);
      if (str != null) {
        this.flipTexCoords = this.materials.renderWithMaterial(str, paramGL);
      }
      this.faces.renderFace(i, this.flipTexCoords, paramGL);
    }
    this.materials.switchOffTex(paramGL);
    paramGL.glPopMatrix();
  }

  private void reportOnModel()
  {
    System.out.println("No. of vertices: " + this.verts.size());
    System.out.println("No. of normal coords: " + this.normals.size());
    System.out.println("No. of tex coords: " + this.texCoords.size());
    System.out.println("No. of faces: " + this.faces.getNumFaces());

    this.modelDims.reportDimensions();
    System.out.println("After scaling:\n");
    this.modelDimsScaled.reportDimensions();

    if (this.materials != null)
      this.materials.showMaterials();
    this.faceMats.showUsedMaterials();
  }
}