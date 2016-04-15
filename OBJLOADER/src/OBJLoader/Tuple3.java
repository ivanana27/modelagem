package OBJLoader;

public class Tuple3
{
  private float x;
  private float y;
  private float z;

  public Tuple3(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    this.x = paramFloat1;
    this.y = paramFloat2;
    this.z = paramFloat3;
  }

  public String toString()
  {
    return "( " + this.x + ", " + this.y + ", " + this.z + " )";
  }

  public void setX(float paramFloat) {
    this.x = paramFloat; }

  public float getX() {
    return this.x;
  }

  public void setY(float paramFloat) {
    this.y = paramFloat; }

  public float getY() {
    return this.y;
  }

  public void setZ(float paramFloat) {
    this.z = paramFloat; }

  public float getZ() {
    return this.z;
  }
}


