package OBJLoader;

import java.io.PrintStream;
import java.text.DecimalFormat;

public class ModelDimensions
{
  private float leftPt;
  private float rightPt;
  private float topPt;
  private float bottomPt;
  private float farPt;
  private float nearPt;
  private DecimalFormat df = new DecimalFormat("0.##");

  public ModelDimensions()
  {
    this.leftPt = 0.0F; this.rightPt = 0.0F;
    this.topPt = 0.0F; this.bottomPt = 0.0F;
    this.farPt = 0.0F; this.nearPt = 0.0F;
  }

  public void set(Tuple3 paramTuple3)
  {
    this.rightPt = paramTuple3.getX();
    this.leftPt = paramTuple3.getX();

    this.topPt = paramTuple3.getY();
    this.bottomPt = paramTuple3.getY();

    this.nearPt = paramTuple3.getZ();
    this.farPt = paramTuple3.getZ();
  }

  public void update(Tuple3 paramTuple3)
  {
    if (paramTuple3.getX() > this.rightPt)
      this.rightPt = paramTuple3.getX();
    if (paramTuple3.getX() < this.leftPt) {
      this.leftPt = paramTuple3.getX();
    }
    if (paramTuple3.getY() > this.topPt)
      this.topPt = paramTuple3.getY();
    if (paramTuple3.getY() < this.bottomPt) {
      this.bottomPt = paramTuple3.getY();
    }
    if (paramTuple3.getZ() > this.nearPt)
      this.nearPt = paramTuple3.getZ();
    if (paramTuple3.getZ() < this.farPt)
      this.farPt = paramTuple3.getZ();
  }

  public float getWidth()
  {
    return (this.rightPt - this.leftPt); }

  public float getHeight() {
    return (this.topPt - this.bottomPt); }

  public float getDepth() {
    return (this.nearPt - this.farPt);
  }

  public float getLargest()
  {
    float f1 = getHeight();
    float f2 = getDepth();

    float f3 = getWidth();
    if (f1 > f3)
      f3 = f1;
    if (f2 > f3) {
      f3 = f2;
    }
    return f3;
  }

  public Tuple3 getCenter()
  {
    float f1 = (this.rightPt + this.leftPt) / 2.0F;
    float f2 = (this.topPt + this.bottomPt) / 2.0F;
    float f3 = (this.nearPt + this.farPt) / 2.0F;
    return new Tuple3(f1, f2, f3);
  }

  public void reportDimensions()
  {
    Tuple3 localTuple3 = getCenter();

    System.out.println("x Coords: " + this.df.format(this.leftPt) + " to " + this.df.format(this.rightPt));
    System.out.println("  Mid: " + this.df.format(localTuple3.getX()) + "; Width: " + this.df.format(getWidth()));

    System.out.println("y Coords: " + this.df.format(this.bottomPt) + " to " + this.df.format(this.topPt));
    System.out.println("  Mid: " + this.df.format(localTuple3.getY()) + "; Height: " + this.df.format(getHeight()));

    System.out.println("z Coords: " + this.df.format(this.nearPt) + " to " + this.df.format(this.farPt));
    System.out.println("  Mid: " + this.df.format(localTuple3.getZ()) + "; Depth: " + this.df.format(getDepth()));
  }
}