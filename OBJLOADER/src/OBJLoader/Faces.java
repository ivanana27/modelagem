package OBJLoader;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.media.opengl.GL;

public class Faces
{
  private static final float DUMMY_Z_TC = -5.0F;
  private ArrayList<int[]> facesVertIdxs;
  private ArrayList<int[]> facesTexIdxs;
  private ArrayList<int[]> facesNormIdxs;
  private ArrayList<Tuple3> verts;
  private ArrayList<Tuple3> normals;
  private ArrayList<Tuple3> texCoords;
  private DecimalFormat df = new DecimalFormat("0.##");

  public Faces(ArrayList<Tuple3> paramArrayList1, ArrayList<Tuple3> paramArrayList2, ArrayList<Tuple3> paramArrayList3)
  {
    this.verts = paramArrayList1;
    this.normals = paramArrayList2;
    this.texCoords = paramArrayList3;

    this.facesVertIdxs = new ArrayList();
    this.facesTexIdxs = new ArrayList();
    this.facesNormIdxs = new ArrayList();
  }

  public boolean addFace(String paramString)
  {
    try
    {
      paramString = paramString.substring(2);
      StringTokenizer localStringTokenizer1 = new StringTokenizer(paramString, " ");
      int i = localStringTokenizer1.countTokens();

      int[] arrayOfInt1 = new int[i];
      int[] arrayOfInt2 = new int[i];
      int[] arrayOfInt3 = new int[i];

      for (int j = 0; j < i; ++j) {
        String str = addFaceVals(localStringTokenizer1.nextToken());

        StringTokenizer localStringTokenizer2 = new StringTokenizer(str, "/");
        int k = localStringTokenizer2.countTokens();

        arrayOfInt1[j] = Integer.parseInt(localStringTokenizer2.nextToken());
        arrayOfInt2[j] = ((k > 1) ? Integer.parseInt(localStringTokenizer2.nextToken()) : 0);
        arrayOfInt3[j] = ((k > 2) ? Integer.parseInt(localStringTokenizer2.nextToken()) : 0);
      }

      this.facesVertIdxs.add(arrayOfInt1);
      this.facesTexIdxs.add(arrayOfInt2);
      this.facesNormIdxs.add(arrayOfInt3);
    }
    catch (NumberFormatException localNumberFormatException) {
      System.out.println("Incorrect face index");
      System.out.println(localNumberFormatException.getMessage());
      return false;
    }
    return true;
  }

  private String addFaceVals(String paramString)
  {
    char[] arrayOfChar = paramString.toCharArray();
    StringBuffer localStringBuffer = new StringBuffer();
    char c = 'x';

    for (int i = 0; i < arrayOfChar.length; ++i) {
      if ((arrayOfChar[i] == '/') && (c == '/'))
        localStringBuffer.append('0');
      c = arrayOfChar[i];
      localStringBuffer.append(c);
    }
    return localStringBuffer.toString();
  }

  public void renderFace(int paramInt, boolean paramBoolean, GL paramGL)
  {
    if (paramInt >= this.facesVertIdxs.size()) {
      return;
    }
    int[] arrayOfInt1 = (int[])(int[])this.facesVertIdxs.get(paramInt);
    int i;
    if (arrayOfInt1.length == 3)
      i = 4;
    else if (arrayOfInt1.length == 4)
      i = 7;
    else {
      i = 9;
    }

    paramGL.glBegin(i);

    int[] arrayOfInt2 = (int[])(int[])this.facesNormIdxs.get(paramInt);
    int[] arrayOfInt3 = (int[])(int[])this.facesTexIdxs.get(paramInt);

    for (int j = 0; j < arrayOfInt1.length; ++j) {
      if (arrayOfInt2[j] != 0) {
        Tuple3 localTuple32 = (Tuple3)this.normals.get(arrayOfInt2[j] - 1);

        paramGL.glNormal3f(localTuple32.getX(), localTuple32.getY(), localTuple32.getZ());
      }

      if (arrayOfInt3[j] != 0) {
        Tuple3 localTuple33 = (Tuple3)this.texCoords.get(arrayOfInt3[j] - 1);
        float f = localTuple33.getY();
        if (paramBoolean) {
          f = 1.0F - f;
        }
        if (localTuple33.getZ() == -5.0F)
          paramGL.glTexCoord2f(localTuple33.getX(), f);
        else {
          paramGL.glTexCoord3f(localTuple33.getX(), f, localTuple33.getZ());
        }

      }

      Tuple3 localTuple31 = (Tuple3)this.verts.get(arrayOfInt1[j] - 1);
      paramGL.glVertex3f(localTuple31.getX(), localTuple31.getY(), localTuple31.getZ());
    }

    paramGL.glEnd();
  }

  public int getNumFaces()
  {
    return this.facesVertIdxs.size();
  }
}