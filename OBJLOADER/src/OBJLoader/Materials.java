package OBJLoader;

import com.sun.opengl.util.texture.Texture;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.media.opengl.GL;

public class Materials
{
  private String MODEL_DIR;
  private ArrayList<Material> materials;
  private String renderMatName = null;

  private boolean usingTexture = false;
  private boolean flipTexCoords = false;

  public Materials(String paramString1, String paramString2)
  {
    this.materials = new ArrayList();

    this.MODEL_DIR = paramString1;
    System.out.println("MODEL DIR: " + paramString1);
    if (this.MODEL_DIR.length() > 0) this.MODEL_DIR += "/";

    String str = this.MODEL_DIR + paramString2;
    try {
      System.out.println("Loading material from " + str);
      BufferedReader localBufferedReader = new BufferedReader(new FileReader(str));
      readMaterials(localBufferedReader);
      localBufferedReader.close();
    }
    catch (IOException localIOException) {
      System.out.println(localIOException.getMessage());
    }
  }

  private void readMaterials(BufferedReader paramBufferedReader)
  {
    try
    {
      Object localObject = null;

      String str1;
	while ((str1 = paramBufferedReader.readLine()) != null) {
         str1 = str1.trim();
        if (str1.length() == 0) {
          continue;
        }
        if (str1.startsWith("newmtl ")) {
          if (localObject != null) {
            this.materials.add((Material) localObject);
          }

          localObject = new Material(str1.substring(7));
        }
        if (str1.startsWith("map_Kd ")) {
          String str2 = this.MODEL_DIR + str1.substring(7);
          ((Material)localObject).loadTexture(str2);
        }
        if (str1.startsWith("Ka "))
          ((Material)localObject).setKa(readTuple3(str1));
        if (str1.startsWith("Kd "))
          ((Material)localObject).setKd(readTuple3(str1));
        if (str1.startsWith("Ks "))
          ((Material)localObject).setKs(readTuple3(str1));
        float f;
        if (str1.startsWith("Ns ")) {
          f = Float.valueOf(str1.substring(3)).floatValue();
          ((Material)localObject).setNs(f);
        }
        if (str1.charAt(0) == 'd') {
          f = Float.valueOf(str1.substring(2)).floatValue();
          ((Material)localObject).setD(f);
        }
        if (str1.startsWith("illum ")) {
          continue;
        }
        if (str1.charAt(0) == '#') {
          continue;
        }
        System.out.println("Ignoring MTL line: " + str1);
      }

      this.materials.add((Material) localObject);
    }
    catch (IOException localIOException) {
      System.out.println(localIOException.getMessage());
    }
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

  public void showMaterials()
  {
    System.out.println("No. of materials: " + this.materials.size());

    for (int i = 0; i < this.materials.size(); ++i) {
      Material localMaterial = (Material)this.materials.get(i);
      localMaterial.showMaterial();
    }
  }

  public boolean renderWithMaterial(String paramString, GL paramGL)
  {
    if (!(paramString.equals(this.renderMatName))) {
      this.renderMatName = paramString;
      switchOffTex(paramGL);

      Texture localTexture = getTexture(this.renderMatName);
      if (localTexture != null)
      {
        switchOnTex(localTexture, paramGL);

        this.flipTexCoords = localTexture.getMustFlipVertically();
      }

      setMaterialColors(this.renderMatName, paramGL);
    }
    return this.flipTexCoords;
  }

  public void switchOffTex(GL paramGL)
  {
    if (this.usingTexture) {
      paramGL.glDisable(3553);
      this.usingTexture = false;
    }
  }

  private void switchOnTex(Texture paramTexture, GL paramGL)
  {
    paramGL.glEnable(3553);
    this.usingTexture = true;
    paramTexture.bind();
  }

  private Texture getTexture(String paramString)
  {
    for (int i = 0; i < this.materials.size(); ++i) {
      Material localMaterial = (Material)this.materials.get(i);
      if (localMaterial.hasName(paramString))
        return localMaterial.getTexture();
    }
    return null;
  }

  private void setMaterialColors(String paramString, GL paramGL)
  {
    for (int i = 0; i < this.materials.size(); ++i) {
      Material localMaterial = (Material)this.materials.get(i);
      if (localMaterial.hasName(paramString))
        localMaterial.setMaterialColors(paramGL);
    }
  }
}