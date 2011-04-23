package com.springie.geometry;

public class Tuple3D implements Cloneable {
  public int x;

  public int y;

  public int z;

  public Tuple3D(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Tuple3D(Tuple3D t) {
    this.x = t.x;
    this.y = t.y;
    this.z = t.z;
  }

  public void addTuple3D(Tuple3D delta) {
    this.x += delta.x;
    this.y += delta.y;
    this.z += delta.z;
  }

  public void subtractTuple3D(Tuple3D delta) {
    this.x -= delta.x;
    this.y -= delta.y;
    this.z -= delta.z;
  }

  public void divideBy(int number) {
    this.x /= number;
    this.y /= number;
    this.z /= number;
  }

  public void multiplyBy(int factor) {
    this.x *= factor;
    this.y *= factor;
    this.z *= factor;
  }

  public void multiplyBy(float factor) {
    this.x *= factor;
    this.y *= factor;
    this.z *= factor;
  }

  public boolean equals(Object o) {
    if (!(o instanceof Tuple3D)) {
      return false;
    }

    final Tuple3D t = (Tuple3D) o;
    if (t.x != this.x) {
      return false;
    }
    if (t.y != this.y) {
      return false;
    }
    if (t.z != this.z) {
      return false;
    }
    return true;
  }

  public int hashCode() {
    return this.x * this.y * this.z;
  }

  public Object clone() {
    try {
      super.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return new Tuple3D(this.x, this.y, this.z);
  }
}