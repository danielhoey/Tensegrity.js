package com.springie.elements.clazz;

public class Clazz {
  public int colour = 0xFFFFFFFF;

  public Clazz(int colour) {
    this.colour = colour;
  }

  public Clazz(Clazz clazz) {
    makeEqualTo(clazz);
  }

  private void makeEqualTo(Clazz clazz) {
    this.colour = clazz.colour;
  }

  public boolean equals(Object o) {
    final Clazz c = (Clazz) o;
    if (this.colour != c.colour) {
      return false;
    }

    return true;
  }

  public int hashCode() {
    return this.colour;
  }
}