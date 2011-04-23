package com.springie.elements.clazz;

import java.util.Vector;

public class ClazzFactory {
  public Vector array = new Vector();

  public Clazz getNew(int colour) {
    final Clazz clazz = new Clazz(colour);
    this.array.addElement(clazz);
    return clazz;
  }
  
//  public Clazz getNew(Clazz current) {
//    final Clazz clazz = new Clazz(current);
//    this.array.addElement(clazz);
//    return clazz;
//  }
}
