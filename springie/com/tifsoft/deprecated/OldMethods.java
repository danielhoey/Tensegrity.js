package com.tifsoft.deprecated;

import java.awt.Component;

public class OldMethods {

  public int getWidth(Component c) {
    return c.size().width;
  }

  public int getHeight(Component c) {
    return c.size().height;
  }

  public static boolean isInsidePolygon(int x, int y, final java.awt.Polygon polygon) {
    return polygon.inside(x, y);
  }
}