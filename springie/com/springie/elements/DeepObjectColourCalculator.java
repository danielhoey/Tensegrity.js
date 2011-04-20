package com.springie.elements;

import com.springie.render.Coords;

public class DeepObjectColourCalculator {
  public static int factor = 750;
  
  public static int getColourOfDeepObject(int colour, int z) {
    final int depth = Coords.z_pixels - (((z >> Coords.shift) * factor) >> 10);
    int r = (colour >> 16) & 0xFF;
    int g = (colour >> 8) & 0xFF;
    int b = (colour >> 0) & 0xFF;
    r = (r * depth) >> 10;
    g = (g * depth) >> 10;
    b = (b * depth) >> 10;
    if (r > 255) {
      r = 255;
    }
    if (g > 255) {
      g = 255;
    }
    if (b > 255) {
      b = 255;
    }

    return 0xFF000000 | b | g << 8 | r << 16;
  }
}