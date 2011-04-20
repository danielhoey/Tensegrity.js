package com.springie.geometry;

public class Point3D extends Tuple3D implements Cloneable {
  public Point3D(int x, int y, int z) {
    super(x, y, z);
  }

  public Point3D(Tuple3D t) {
    super(t);
  }

  public Object clone() {
    super.clone();
    return new Point3D(this.x, this.y, this.z);
  }
}