package com.springie.render;

import java.awt.Graphics;

/**
 * <HR>
 * <P>
 * Index to the other classes:
 * <UL>
 * <LI><a href="Shoals.html">Shoals </a>- the main class for the project;
 * </LI>
 * <LI><a href="Module.html">Module </a>- the class that implements
 * extensions;</LI>
 * <LI><a href="Node.html">Node </a>- the class that represents a single <a
 * href="Node.html">Node </a>;</LI>
 * <LI><a href="NodeManager.html">NodeManager </a>- The main <a
 * href="World.html">World </a> that fills all of space;</LI>
 * <LI><a href="Link.html">Link </a>- the class that represents a single <a
 * href="Link.html">Link </a>;</LI>
 * <LI><a href="LinkManager.html">LinkManager </a>- manages all <a
 * href="Link.html">Link </a>s in the system;</LI>
 * <LI><a href="World.html">World </a>- the class that represents a
 * collection of <a href="Node.html">Node </a>s;</LI>
 * <LI><a href="WorldManager.html">WorldManager </a>- manages all the <a
 * href="World.html">World </a>s;</LI>
 * </UL>
 * <P>
 * <HR>
 * <P>
 * public class Coords
 * <P>
 * This class handles coordinate transformations <BR>
 * The coordinate system is liable to change. <BR>
 * Consequently it would not be a good idea to rely on it too heavily. <BR>
 */

public class Coords {
  /**
   * Used internally to convert Coords. <BR>
   * When z = 0, this is the number of bits which internal Coords need to be
   * shifted right to convert them into screen pixels
   */
  public static final int shift = 8;
  public static final int shift_shifted = 1 << Coords.shift;
  
  public static int shift_constant_x;
  public static int shift_constant_y;
  public static int shift_constant_z = Coords.shift_shifted - (Coords.shift_shifted >> 2);
  
  public static final int shift_z = 10;
  public static final int shift_shifted_z = 1 << Coords.shift_z;

  public static int x_pixels = 800;
  public static int y_pixels = 600;
  public static int z_pixels = 1024;

  public static int x_pixelso2;
  public static int y_pixelso2;
  public static int z_pixelso2 = 512;

  public static int x_pixels_old = -1;
  public static int y_pixels_old = -1;

  // int -> pixels
  public static int getXCoords(int _x, int _z) {
    return Coords.x_pixelso2 + ((_x + shift_constant_x - (Coords.x_pixelso2 << Coords.shift)) / (Coords.shift_constant_z + (_z >> Coords.shift_z)));
  }

  public static int getYCoords(int _y, int _z) {
    return Coords.y_pixelso2 + ((_y + shift_constant_y - (Coords.y_pixelso2 << Coords.shift)) / (Coords.shift_constant_z + (_z >> Coords.shift_z)));
  }

  // keeps it about the same size...
  public static int getRadius(int _r, int _z) {
    return _r / (Coords.shift_constant_z + (_z >> Coords.shift_z));
  }

  //int -> Internal coordinates
  public static int getXCoordsInternal(int _x, int _z) {
    return Coords.getXCoords(_x, _z) << Coords.shift;
  }

  public static int getYCoordsInternal(int _y, int _z) {
    return Coords.getYCoords(_y, _z) << Coords.shift;
  }

  public static int getRadiusInternal(int _r, int _z) {
    return Coords.getRadius(_r, _z) << Coords.shift;
  }

  // int -> int
  public static int inverseXCoords(int _x, int _z) {
    return (((_x >> Coords.shift) - Coords.x_pixelso2) * (Coords.shift_constant_z + (_z >> Coords.shift_z))) + (Coords.x_pixelso2 << Coords.shift);
  }

  public static int inverseYCoords(int _y, int _z) {
    return (((_y >> Coords.shift) - Coords.y_pixelso2) * (Coords.shift_constant_z + (_z >> Coords.shift_z))) + (Coords.y_pixelso2 << Coords.shift);
  }
 
  static void drawLine2(Graphics g, int _x1, int _y1, int _z1, int _x2, int _y2, int _z2) {
    _x1 = Coords.getXCoords(_x1 << Coords.shift, _z1 << Coords.shift);
    _y1 = Coords.getYCoords(_y1 << Coords.shift, _z1 << Coords.shift);

    _x2 = Coords.getXCoords(_x2 << Coords.shift, _z2 << Coords.shift);
    _y2 = Coords.getYCoords(_y2 << Coords.shift, _z2 << Coords.shift);

    g.drawLine(_x1, _y1, _x2, _y2);
  }

  public static void drawLine(Graphics g, int _x1, int _y1, int _z1, int _x2, int _y2, int _z2) {
    _x1 = Coords.getXCoords(_x1, _z1);
    _y1 = Coords.getYCoords(_y1, _z1);

    _x2 = Coords.getXCoords(_x2, _z2);
    _y2 = Coords.getYCoords(_y2, _z2);

    g.drawLine(_x1, _y1, _x2, _y2);
  }

  public static int getInternalFromPixelCoords(int x) {
    return x << Coords.shift;
  }
}
