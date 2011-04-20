package com.springie.elements.faces;

import java.util.Vector;

public class FaceTypeFactory {
  public Vector array = new Vector();

  public FaceType getNew() {
    final FaceType type = new FaceType();
    this.array.addElement(type);
    return type;
  }
}
