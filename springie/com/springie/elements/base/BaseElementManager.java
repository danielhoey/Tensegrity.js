package com.springie.elements.base;

import java.util.Vector;

public class BaseElementManager {
  public Vector element = new Vector();

  public boolean each_has_its_own_type;

  public boolean each_has_its_own_clazz;

  public void reset() {
    this.element.removeAllElements();
    this.each_has_its_own_type = false;
    this.each_has_its_own_clazz = false;
  }
}