// This code has been placed into the public domain by its author

package com.springie.explosions.fragments;

import com.springie.collisions.BinGrid;
import com.springie.elements.nodes.Node;
import com.springie.render.Coords;
import com.springie.utilities.math.SquareRoot;
import com.springie.utilities.random.Hortensius32Fast;
import com.springie.FrEnd;

public class LineFragment {
  int x;

  int y;

  int dx;

  int dy;

  int direction;

  int angular_velocity;

  byte length;

  byte count;

  // length increment should exist as well...

  static int temp_x;

  static int temp_y;

  static int temp_dx;

  static int temp_dy;

  static int temp;

  static int size_x;

  static int size_y;

  static Hortensius32Fast rnd = new Hortensius32Fast();

  LineFragment(int ix, int iy, int jx, int jy, int idx, int idy, int rotspeed) {
    this.x = (ix + jx) >> 1;
    this.y = (iy + jy) >> 1;

    jx = (jx - ix) >> Coords.shift;
    jy = (jy - iy) >> Coords.shift;

    this.length = (byte) ((SquareRoot.fastSqrt(1 + (jx * jx) + (jy * jy)) >> 1));

    this.dx = idx;
    this.dy = idy;

    this.direction = (FrEnd.node_manager.arcTangent(jx, jy)) << 8;

    this.angular_velocity = rnd.nextInt(rotspeed) - (rotspeed >> 1);
    if ((this.angular_velocity < 256) && (this.angular_velocity >= 0)) {
      this.angular_velocity = 256;
    }
    if ((this.angular_velocity > -256) && (this.angular_velocity <= 0)) {
      this.angular_velocity = -256;
    }

    this.count = (byte) ((this.x + this.dx + this.dy) & 63);
  }

  // in internal co-ordinates....
  final void set(int ix, int iy, int jx, int jy, int idx, int idy, int rotspeed) {
    this.x = (ix + jx) >> 1;
    this.y = (iy + jy) >> 1;

    jx = (jx - ix) >> Coords.shift;
    jy = (jy - iy) >> Coords.shift;

    this.length = (byte) ((SquareRoot.fastSqrt(1 + (jx * jx) + (jy * jy)) >> 1));

    this.dx = idx;
    this.dy = idy;

    this.direction = (FrEnd.node_manager.arcTangent(jx, jy)) << 8;

    this.angular_velocity = rnd.nextInt(rotspeed) - (rotspeed >> 1);
    if ((this.angular_velocity < 128) && (this.angular_velocity > 0)) {
      this.angular_velocity = 128;
    }
    if ((this.angular_velocity > -128) && (this.angular_velocity < 0)) {
      this.angular_velocity = -128;
    }

    this.count = (byte) ((this.x + this.dx + this.dy) & 63);
  }

  void draw() {
    temp_x = this.x >>> Coords.shift;
    temp_y = this.y >>> Coords.shift;

    temp_dx = (this.length * Node.cos_tab[(this.direction >>> 8) & 255]) >> Node.lut_shift;
    temp_dy = (this.length * Node.sin_tab[(this.direction >>> 8) & 255]) >> Node.lut_shift;

    BinGrid.graphics_handle.drawLine(temp_x - temp_dx, temp_y - temp_dy, temp_x
      + temp_dx, temp_y + temp_dy);
  }

  void scrub() {
    temp_x = this.x >>> Coords.shift;
    temp_y = this.y >>> Coords.shift;

    temp_dx = (this.length * Node.cos_tab[(this.direction >>> 8) & 255]) >> Node.lut_shift;
    temp_dy = (this.length * Node.sin_tab[(this.direction >>> 8) & 255]) >> Node.lut_shift;

    BinGrid.graphics_handle.drawLine(temp_x - temp_dx, temp_y - temp_dy, temp_x
      + temp_dx, temp_y + temp_dy);
  }

  void travel() {
    this.x = this.x + this.dx;
    this.y = this.y + this.dy;

    this.direction = (this.direction + this.angular_velocity)
      & (255 | (Node.TRIG_TAB_SIZEMO << 8));

    // if ((count & 3) == 0) {
    /*
     * if (angular_velocity > 0) { angular_velocity = angular_velocity -
     * angular_velocity < < 2; } else { angular_velocity++; }
     */

    /*
     * if ((count & 31) == 0) { angular_velocity = angular_velocity -
     * angular_velocity < < 5; if (length > 0) { length--; } }
     */

    // }
    this.count--;
  }

}