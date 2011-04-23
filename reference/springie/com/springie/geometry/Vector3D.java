package com.springie.geometry;

public class Vector3D extends Tuple3D {
  public Vector3D(int x, int y, int z) {
    super(x, y, z);
  }

  public Vector3D(Tuple3D t) {
    super(t);
  }

  public Vector3D(Tuple3D a, Tuple3D b) {
    super(a.x - b.x, a.y - b.y, a.z - b.z);
  }

  int getLength() {
    return 0;
  }

  long getLengthSquared() {
    return 0;
  }

  public Object clone() {
    super.clone();
    return new Vector3D(this.x, this.y, this.z);
  }
}