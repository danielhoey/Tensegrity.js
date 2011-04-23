package com.springie.explosions.particles;

import com.springie.collisions.BinGrid;

public class Particle {
  static int shift = 8;

  int x; // Image offScrImage;
  int y; //

  int dx; //
  int dy; //

  int count;

  static int friction_strength = 4;

  int temp_x;
  int temp_y;

  int type;

  static int temp;

  // static int diameter = 18;
  // static int radius = diameter >>> 1;

  static int size_x;
  static int size_y;

  public static final int POINT = 0;
  public static final int CROSS = 1;
  public static final int BLOB = 2;

  static final int[] size_LUT_x = {0, 1, 1, 2, 2, 3, 3, 4};

  static final int[] size_LUT_y = {0, 0, 1, 1, 2, 2, 3, 3};

  //Particle() {
  //}

  Particle(int ix, int iy, int idx, int idy, int t) {
    this.x = ix; // - idx;
    this.y = iy; // - idy;

    this.dx = idx;
    this.dy = idy;

    this.count = (this.x + this.dx + this.dy) & 63;
    this.type = t;
  }

  // in internal co-ordinates....
  final void set(int ix, int iy, int idx, int idy, int t) {
    this.x = ix; // - idx;
    this.y = iy; //; - idy;

    this.dx = idx;
    this.dy = idy;

    this.count = (this.x + this.dx + this.dy) & 63;
    this.type = t;
  }

  //static final void initial() {
  //}

  void draw() {
    switch (this.type) {
      case BLOB :
        BinGrid.graphics_handle.fillRect(this.x >>> shift, this.y >>> shift,
            size_LUT_x[this.count >> 3], size_LUT_y[this.count >> 3]);

        break;

      case POINT :
        BinGrid.graphics_handle.fillRect(this.x >>> shift, this.y >>> shift, 1, 1);

        break;

      case CROSS :
        size_x = size_LUT_x[this.count >> 3];
        size_y = size_LUT_y[this.count >> 3];

        BinGrid.graphics_handle.drawLine((this.x >>> shift) - size_x, this.y >>> shift,
            (this.x >>> shift) + size_x, this.y >>> shift);
        BinGrid.graphics_handle.drawLine(this.x >>> shift, (this.y >>> shift) - size_y,
          this.x >>> shift, (this.y >>> shift) + size_y);

        break;
      default:
        throw new RuntimeException("");
    }
  }

  void scrub() {
    switch (this.type) {
      case BLOB :
        BinGrid.graphics_handle.fillRect(this.x >>> shift, this.y >>> shift,
            size_LUT_x[this.count >> 3], size_LUT_y[this.count >> 3]);

        break;

      case POINT :
        BinGrid.graphics_handle.fillRect(this.x >>> shift, this.y >>> shift, 1, 1);

        break;

      case CROSS :
        size_x = size_LUT_x[this.count >> 3];
        size_y = size_LUT_y[this.count >> 3];

        BinGrid.graphics_handle.drawLine((this.x >>> shift) - size_x, this.y >>> shift,
            (this.x >>> shift) + size_x, this.y >>> shift);
        BinGrid.graphics_handle.drawLine(this.x >>> shift, (this.y >>> shift) - size_y,
          this.x >>> shift, (this.y >>> shift) + size_y);

        break;
      default:
        throw new RuntimeException("");
    }
  }

  void travel() {
    this.x = this.x + this.dx;
    this.y = this.y + this.dy;

    // applyFriction();

    this.count--;

    // size = 1 + (count >> 3);

    //if (count < 0) {

    // }
    /*
     * if ((x >> shift) > Shoals_Canvas.gsx) { x = Shoals_Canvas.gsx < < shift;
     * //x - dx; 0.9); }
     *
     * if (x < 0) { x = 0; 0.9); }
     *
     * if ((y >> shift) > Shoals_Canvas.gsy) { y = Shoals_Canvas.gsy < < shift;
     * //x - dx; 0.9); }
     *
     * if (y < 0) { y = 0; 0.9); }
     */
    // absdx = (dx > 0) ? dx : -dx;
    // absdy = (dy > 0) ? dy : -dy;
    //  adjustDirection();
  }

  /*
   * void applyFriction() {
   *  // cap velocities... if ((Shoals_Grid.generation & 7) == 0) { if (dx > 0) {
   * dx -= friction_strength; }
   *
   * if (dx < 0) { dx += friction_strength; }
   *
   * if (dy > 0) { dy -= friction_strength; }
   *
   * if (dy < 0) { dy += friction_strength; } } }
   */

}
