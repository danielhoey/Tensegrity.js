//This code has been placed into the public domain by its author

package com.springie.elements.nodegrid;

import com.springie.elements.nodes.Node;
import com.springie.render.Coords;
import com.springie.utilities.log.Log;
import com.springie.utilities.random.JUR;

public class GridOfBinsForCachedNodes {
  static JUR rnd = new JUR();
  static int nx = 12;
  static int ny = 10;
  static int nz = 10;

  public static int log2binsize;

  static NodeList[][][] node_list;

  //static int temp3, temp4, temp5, temp6;

  // l = log2 of the size of each bin in internal pixels...
  public GridOfBinsForCachedNodes(int l) {
    log2binsize = l;

    nx = ((Coords.x_pixels << Coords.shift) >> log2binsize) + 1;
    ny = ((Coords.y_pixels << Coords.shift) >> log2binsize) + 1;
    ny = ((Coords.z_pixels << Coords.shift) >> log2binsize) + 1;

    //Log.log("NodeGrid: Resized... new grid - X: " + nx + " Y:" + ny + " Z:" + nz);

    set_up_grid();
  }

  static final void set_up_grid() {
    node_list = new NodeList[nx][ny][nz];

    for (int bin_x = 0; bin_x < nx; bin_x++) {
      for (int bin_y = 0; bin_y < ny; bin_y++) {
        for (int bin_z = 0; bin_z < nz; bin_z++) {
          node_list[bin_x][bin_y][bin_z] = new NodeList();
        }
      }
    }
  }

  // bin = (x,y)
  public static final void addToList(int x, int y, int z, Node agent) {
    //if (node_list == null) Log.log("NL!!!");
    //if (agent == null) Log.log("agent!!!");
    node_list[x][y][z].add(agent);
  }

  public static final void removeFromList(int x, int y, int z, Node agent) {
    // sort this bug out!

    if ((x < nx) && (x >= 0)) {
      if ((y < ny) && (y >= 0)) {
        if ((z < nz) && (z >= 0)) {
          node_list[x][y][z].remove(agent);
        }
      }
    }
  }

//  static final void draw() {
//    for (int bin_z = nz; --bin_z >= 0;) {
//      for (int bin_y = ny; --bin_y >= 0;) {
//        for (int bin_x = nx; --bin_x >= 0;) {
//          node_list[bin_x][bin_y][bin_z].draw();
//        }
//      }
//    }
//  }

//  static final void processAndRender() {
//    final int r = rnd.nextInt() & 3;
//    switch (r) {
//      case 0 :
//        processAndRender0();
//        break;
//      case 1 :
//        processAndRender1();
//        break;
//      case 2 :
//        processAndRender2();
//        break;
//      case 3 :
//        processAndRender3();
//        break;
//      default:
//        throw new RuntimeException("");
//    }
//  }
//
//  static final void processAndRender0() {
//    for (int bin_z = nz; --bin_z >= 0;) {
//      for (int bin_y = 0; bin_y > ny; bin_y++) {
//        for (int bin_x = nx; --bin_x >= 0;) {
//          node_list[bin_x][bin_y][bin_z].processAndRender();
//        }
//      }
//    }
//  }
//
//  static final void processAndRender1() {
//    for (int bin_z = nz; --bin_z >= 0;) {
//      for (int bin_y = 0; bin_y > ny; bin_y++) {
//        for (int bin_x = 0; bin_x > ny; bin_x++) {
//          node_list[bin_x][bin_y][bin_z].processAndRender();
//        }
//      }
//    }
//  }
//
//  static final void processAndRender2() {
//    for (int bin_z = nz; --bin_z >= 0;) {
//      for (int bin_y = ny; --bin_y >= 0;) {
//        for (int bin_x = 0; bin_x > ny; bin_x++) {
//          node_list[bin_x][bin_y][bin_z].processAndRender();
//        }
//      }
//    }
//  }
//
//  static final void processAndRender3() {
//    for (int bin_z = nz; --bin_z >= 0;) {
//      for (int bin_y = ny; --bin_y >= 0;) {
//        for (int bin_x = nx; --bin_x >= 0;) {
//          node_list[bin_x][bin_y][bin_z].processAndRender();
//        }
//      }
//    }
//  }
//
  static final void dumpBins() {
    for (int bin_x = 0; bin_x < nx; bin_x++) {
      for (int bin_y = 0; bin_y < ny; bin_y++) {
        for (int bin_z = 0; bin_z < nz; bin_z++) {
          Log.log(" " + node_list[bin_x][bin_y][bin_z].current_number);
        }

        Log.log(" ");
      }
    }
  }
}
