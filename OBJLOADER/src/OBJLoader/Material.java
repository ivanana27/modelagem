package OBJLoader;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureIO;
import java.io.File;
import java.io.PrintStream;
import javax.media.opengl.GL;

public class Material
{
  private String name;
  private Tuple3 ka;
  private Tuple3 kd;
  private Tuple3 ks;
  private float ns;
  private float d;
  private String texFnm;
  private Texture texture;

  public Material(String paramString)
  {
    this.name = paramString;

    this.d = 1.0F; this.ns = 0.0F;
    this.ka = null; this.kd = null; this.ks = null;

    this.texFnm = null;
    this.texture = null;
  }

  public void showMaterial()
  {
    System.out.println(this.name);
    if (this.ka != null)
      System.out.println("  Ka: " + this.ka.toString());
    if (this.kd != null)
      System.out.println("  Kd: " + this.kd.toString());
    if (this.ks != null)
      System.out.println("  Ks: " + this.ks.toString());
    if (this.ns != 0.0F)
      System.out.println("  Ns: " + this.ns);
    if (this.d != 1.0F)
      System.out.println("  d: " + this.d);
    if (this.texFnm != null)
      System.out.println("  Texture file: " + this.texFnm);
  }

  public boolean hasName(String paramString)
  {
    return this.name.equals(paramString);
  }

  public void setD(float paramFloat)
  {
    this.d = paramFloat; }

  public float getD() {
    return this.d;
  }

  public void setNs(float paramFloat) {
    this.ns = paramFloat; }

  public float getNs() {
    return this.ns;
  }

  public void setKa(Tuple3 paramTuple3) {
    this.ka = paramTuple3; }

  public Tuple3 getKa() {
    return this.ka;
  }

  public void setKd(Tuple3 paramTuple3) {
    this.kd = paramTuple3; }

  public Tuple3 getKd() {
    return this.kd;
  }

  public void setKs(Tuple3 paramTuple3) {
    this.ks = paramTuple3; }

  public Tuple3 getKs() {
    return this.ks;
  }

  public void setMaterialColors(GL paramGL)
  {
    float[] arrayOfFloat;
    if (this.ka != null) {
      arrayOfFloat = new float[] { this.ka.getX(), this.ka.getY(), this.ka.getZ(), 1.0F };
      paramGL.glMaterialfv(1032, 4608, arrayOfFloat, 0);
    }
    if (this.kd != null) {
      arrayOfFloat = new float[] { this.kd.getX(), this.kd.getY(), this.kd.getZ(), 1.0F };
      paramGL.glMaterialfv(1032, 4609, arrayOfFloat, 0);
    }
    if (this.ks != null) {
      arrayOfFloat = new float[] { this.ks.getX(), this.ks.getY(), this.ks.getZ(), 1.0F };
      paramGL.glMaterialfv(1032, 4610, arrayOfFloat, 0);
    }

    if (this.ns != 0.0F) {
      paramGL.glMaterialf(1032, 5633, this.ns);
    }

    if (this.d == 1.0F)
      return;
  }

  public void loadTexture(String paramString)
  {
    try
    {
      this.texFnm = paramString;
      this.texture = TextureIO.newTexture(new File(this.texFnm), false);
      this.texture.setTexParameteri(10240, 9728);
      this.texture.setTexParameteri(10241, 9728);
    }
    catch (Exception localException) {
      System.out.println("Error loading texture " + this.texFnm);
    }
  }

  public void setTexture(Texture paramTexture) {
    this.texture = paramTexture; }

  public Texture getTexture() {
    return this.texture;
  }
}