// This code has been placed into the public domain by its author

package com.springie.explosions.fragments;

import java.awt.Color;

import com.springie.FrEnd;
import com.springie.collisions.BinGrid;

public class LineFragmentManager {
  static LineFragment linefragment[];

  static LineFragment temp_linefragment;

  static int MAX_LINEFRAGMENT_NUMBER = 512;

  static int current_max_number;

  static int current_number;

  static int i;

  public static final void initial() {
    linefragment = new LineFragment[MAX_LINEFRAGMENT_NUMBER];
  }

  public static void add(int ix, int iy, int jx, int jy, int idx, int idy,
    int rotspeed) {
    if (current_max_number < MAX_LINEFRAGMENT_NUMBER) {
      if (linefragment[current_max_number] == null) {
        linefragment[current_max_number++] = new LineFragment(ix, iy, jx, jy,
          idx, idy, rotspeed);
      } else {
        linefragment[current_max_number++].set(ix, iy, jx, jy, idx, idy,
          rotspeed);
      }
    }
  }

  public static void update() {
    if (FrEnd.show_exhaust) {
      if (current_max_number > 0) {
        BinGrid.graphics_handle.setColor(BinGrid.bg_colour);

        for (i = 0; i < current_max_number; i++) {
          linefragment[i].scrub();
          linefragment[i].travel();
        }

        BinGrid.graphics_handle.setColor(Color.white);
        i = 0;
        do {
          if (linefragment[i].count < 0) {
            if (i != current_max_number - 1) {
              temp_linefragment = linefragment[i];
              linefragment[i] = linefragment[current_max_number - 1];
              linefragment[current_max_number - 1] = temp_linefragment;
            }

            current_max_number--;
            i--; // loopy?
          } else {
            if (i == current_max_number >> 1) {
              BinGrid.graphics_handle.setColor(Color.yellow);
            }

            if (i == current_max_number >> 2) {
              BinGrid.graphics_handle.setColor(Color.red);
            }

            linefragment[i].draw();
          }

          i++;
        } while (i < current_max_number);
      }
    }

    // used to leave colour OOO/undefined - so...
    // Shoals_Grid.colour = -1;
    //ShoalsGrid.last_colour = -1;
  }

}