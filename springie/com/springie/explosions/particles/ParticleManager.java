// This code has been placed into the public domain by its author

package com.springie.explosions.particles;

import java.awt.Color;

import com.springie.FrEnd;
import com.springie.collisions.BinGrid;

public class ParticleManager {
  static Particle particle[];

  static Particle temp_particle;

  static int MAX_PARTICLE_NUMBER = 512; // fixed

  // upper
  static int current_max_number;

  static int current_number;

  static int i;

  public static final void initial() {
    particle = new Particle[MAX_PARTICLE_NUMBER];
  }

  public static void add(int ix, int iy, int idx, int idy, int type) {
    if (current_max_number < MAX_PARTICLE_NUMBER) {
      if (particle[current_max_number] == null) {
        particle[current_max_number++] = new Particle(ix, iy, idx, idy, type);
      } else {
        particle[current_max_number++].set(ix, iy, idx, idy, type);
      }
    }
  }

  public static void update() {
    if (FrEnd.show_exhaust) {
      if (current_max_number > 0) {
        BinGrid.graphics_handle.setColor(BinGrid.bg_colour);

        for (i = 0; i < current_max_number; i++) {
          particle[i].scrub();
          particle[i].travel();
        }

        // for (i = 0; i < current_max_number; i++) {
        // particle[i].draw();
        // }

        BinGrid.graphics_handle.setColor(Color.white);
        i = 0;
        do {
          if (particle[i].count < 0) {
            if (i != current_max_number - 1) {
              temp_particle = particle[i];
              particle[i] = particle[current_max_number - 1];
              particle[current_max_number - 1] = temp_particle;
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

            particle[i].draw();
          }

          i++;
        } while (i < current_max_number);
      }
    }

    // used to leave colour OOO/undefined - so...
    // Shoals_Grid.colour = -1;
    //ShoalsGrid.last_colour = -1;
  }

  /*
   * static void update() { //for (i = 0; i < current_max_number; i++) { i = 0;
   * do { particle[i].scrub(); particle[i].travel(); particle[i].draw();
   * 
   * if (particle[i].count < 0) { }
   * 
   * i++; } while (i < current_max_number); }
   */

}