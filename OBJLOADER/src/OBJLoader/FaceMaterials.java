package OBJLoader;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class FaceMaterials
{
  private HashMap<Integer, String> faceMats;
  private HashMap<String, Integer> matCount;

  public FaceMaterials()
  {
    this.faceMats = new HashMap();
    this.matCount = new HashMap();
  }

  public void addUse(int paramInt, String paramString)
  {
    if (this.faceMats.containsKey(Integer.valueOf(paramInt))) {
      System.out.println("Face index " + paramInt + " changed to use material " + paramString);
    }
    this.faceMats.put(Integer.valueOf(paramInt), paramString);

    if (this.matCount.containsKey(paramString)) {
      int i = ((Integer)this.matCount.get(paramString)).intValue() + 1;
      this.matCount.put(paramString, Integer.valueOf(i));
    }
    else {
      this.matCount.put(paramString, Integer.valueOf(1));
    }
  }

  public String findMaterial(int paramInt) {
    return ((String)this.faceMats.get(Integer.valueOf(paramInt)));
  }

  public void showUsedMaterials()
  {
    System.out.println("No. of materials used: " + this.matCount.size());

    Set localSet = this.matCount.keySet();
    Iterator localIterator = localSet.iterator();

    while (localIterator.hasNext()) {
      String str = (String)localIterator.next();
      int i = ((Integer)this.matCount.get(str)).intValue();

      System.out.print(str + ": " + i);
      System.out.println();
    }
  }
} 	