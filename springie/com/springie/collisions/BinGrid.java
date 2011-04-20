package com.springie.collisions;

import java.awt.Color;
import java.awt.Graphics;

import com.springie.FrEnd;
import com.springie.composite.Reproduction;
import com.springie.elements.nodes.NodeManager;
import com.springie.explosions.fragments.LineFragmentManager;
import com.springie.explosions.particles.ParticleManager;
import com.springie.messages.ArgumentList;
import com.springie.utilities.random.JUR;
import com.springie.world.WorldManager;

/*
 * To do: ======
 * 
 * Add a "low number of bits" live/dead array - to postpone cache thrashing...?
 *  
 */

public class BinGrid {
  public static int virgin_applet;

  static int mask_value;

  static int BPC_value;

  //static int mutation_rate;

  static int radiation_rate;

  static int any_selection;

  static int rnd_frame;

  static int coverage_counter;

  static int invert_edges = 1;

  static int mouse_pressed;

  static int a_atgrid;

  static int sparse = 1;

  static int reproduction_delay_min = 16;

  static int reproduction_delay_max = 48;

  static int reverse;

  static int direction = 1;

  static int newborn;

  public static int generation; //?

  static int from_cell;

  static int from_cell1;

  static int current_direction;

  public static boolean repaint_all_objects = true;
  
  public static boolean repaint_some_objects = true;

  public static boolean double_buffering;

  public static Graphics graphics_handle;

  static int from_dir;

  static int side = 100;

  static JUR rnd = new JUR();

  static JUR temp_rnd = new JUR();

  static int smear_x;

  static int smear_y;

  static int hside;

  static int vside;

  static int max;

  static int cx;

  static int cy;

  static int ncx;

  static int ncy;

  static int new_a0;

  static int new_a1;

  static int new_status;

  static int new_value;

  static int new_direction;

  static int cell_number;

  static int width;

  static int height;

  static int to_direction[];

  static int from_direction[];

  static int develop[];

  static int repro_direction[];

  static int appletsize = 512;

  static final int MSGQ_SIZE = 32;

  static final int MONQ_SIZE = 512; // expands...

  static final int EDGELIST_SIZE = 1024; // !

  //static final int COLOUR_MAX = 256;

//  static final int _MUT_OFF = -1;
//
//  static final int _MUT_VLOW = 1;
//
//  static final int _MUT_LOW = 2;
//
//  static final int _MUT_QLOW = 3;
//
//  static final int _MUT_MEDIUM = 4;
//
//  static final int _MUT_QHIGH = 6;
//
//  static final int _MUT_HIGH = 8;
//
//  static final int _MUT_VHIGH = 10;

  static final int _FRAME_1 = 0;

  static final int _FRAME_2 = 1;

  static final int _FRAME_4 = 3;

  static final int _FRAME_8 = 7;

  static final int _FRAME_16 = 15;

  static final int _FRAME_32 = 31;

  static final Color WorldColour = Color.black;

  //static final Color GridColour = Color.black;

  //static final Color PageColour = new Color(0xF0, 0xE8, 0xFF);

  static final Color agent_colour = Color.white;

  public static final Color bg_colour = Color.black;

  public static final Color black = new Color(0);

  static int LUT_NUMBER = 16;

  public static void initialise(int resolutionx, int resolutiony) {
    //int i, j;

    // LUT_Table = new int[16][LUT_NUMBER];

    //BinGrid.mutation_rate = BinGrid._MUT_OFF;

    BinGrid.set_size(resolutionx, resolutiony);

    //BinGrid.colours = new Color[BinGrid.COLOUR_MAX];

    // colours[0] = new Color(64,64,64);

    // colours[1] = new Color(255,255,255);

    //for (int c = 0; c < BinGrid.COLOUR_MAX; c++) {
    //BinGrid.colours[c] = new Color((c & 7) * 32, (c & 56) * 4, (c & 448) /
    // 2);
    // colours[c] = new Color(c & 255,(64 + ((c * 3) >> 2)) & 255,(64 + ((c <<
    // 3) & 127) + (c >>> 2)) & 255);
    //}

    FrEnd.node_manager = new NodeManager();
    ParticleManager.initial();

    LineFragmentManager.initial();
    // Shoals.node_manager.creature_manager.initial();
    //StarManager.initial();
    WorldManager.initial();
  }

  public static void clear() {
    FrEnd.node_manager.initialSetUp();
    //informModulesOfReset();
  }

  public static void clearAndThenAddInitialObjects() {
    FrEnd.node_manager.initial();
    informModulesOfReset();
  }

  public static void clearAndThenAddProceduralObjects(ArgumentList al) {
    FrEnd.node_manager.initialWithPreset(al);
    informModulesOfReset();
  }

  private static void informModulesOfReset() {
    WorldManager.initial();
    if (FrEnd.module) {
      FrEnd.extension.calledOnReset();
    }
  }

  public static void addBoids() {
    FrEnd.node_manager.add();
  }

  //public static void clearAllNodes() {
    //WorldManager.reset();
    //FrEnd.node_manager.creature_manager.initial();
    //FrEnd.node_manager.element.removeAllElements();
  //}

  public static void set_size(int h, int v) {
    BinGrid.hside = h;
    BinGrid.vside = v;
  }

  static void resetGrid() {
    //BinGrid.blankGrid();

    BinGrid.repaint_all_objects = true;
  }

  static void setGrid() {

    BinGrid.repaint_all_objects = true;
  }

  public static final void calculate_grid() {
    if (BinGrid.double_buffering) {
      BinGrid.graphics_handle = FrEnd.main_canvas.graphics_handle;
      //FrEnd.node_manager.bufferedUpdate();//TODO
      //WorldManager.bufferedUpdate();

      ParticleManager.update();
      LineFragmentManager.update();
      //StarManager.update();
      //Selection.performSelection();
      Reproduction.handleReproduction(FrEnd.node_manager.creature_manager);

      BinGrid.incrementGenerationCount();
    }
  }

  static final void deal_with_agents() {
    BinGrid.incrementGenerationCount();
    if (!FrEnd.paused) {
      ParticleManager.update();
      LineFragmentManager.update();
      //StarManager.update();
      //Selection.performSelection();
      Reproduction.handleReproduction(FrEnd.node_manager.creature_manager);
    }

    FrEnd.node_manager.mainAgentUpdate();

    WorldManager.unbufferedUpdate();
  }

  private static void incrementGenerationCount() {
    if (!FrEnd.paused) {
      BinGrid.generation += BinGrid.direction;
    }
  }

  // the performance of this code can be improved...
  static final void process_one_edge_cell() {
    /*
     * cell_w = safeRead(cx - 1,cy ); cell_e = safeRead(cx + 1,cy ); cell_n =
     * safeRead(cx ,cy - 1); cell_s = safeRead(cx ,cy + 1);
     * 
     * if ((cell_w | cell_e | cell_s | cell_n) == DEAD) { if (out_grid[cx ][cy ] !=
     * DEAD) { out_grid[cx ][cy ] = DEAD; } } else { cell_c = in_grid[cx ][cy ];
     * 
     * cell_process(); }
     */
  }

  static final void cell_process() {
    /*
     * if (cell_c < 0) { cell_future = cell_c; } else { // (cell_c < < 2) + if
     * ((generation & 1) == 0) temp = 4; else temp = 8;
     * 
     * max = cell_c; if (cell_n > max) max = cell_n; if (cell_s > max) max =
     * cell_s; if (cell_e > max) max = cell_e; if (cell_w > max) max = cell_w;
     * 
     * cell_future = ((((cell_c + cell_c + cell_c + max + // cell_c + max cell_w +
     * cell_w + cell_w + cell_e + cell_e + cell_e + cell_n + cell_n + cell_n +
     * cell_s + cell_s + cell_s )) + temp) >>> 4);
     * 
     * //if (cell_future < 0) //cell_future = 0;
     * 
     * cell_future = cell_future & 255; }
     * 
     * out_grid[cx][cy] = cell_future;
     */
  }

  static final void trivialCopy() {
    /*
     * if ((hside == 1) & (vside == 1)) { out_grid[0][0] = in_grid[0][0]; } else {
     * if (vside == 1) { for (cx = 0; cx < hside; cx++) { out_grid[cx][0] =
     * in_grid[cx][0]; } } else { if (hside == 1) { for (cy = 0; cy < vside;
     * cy++) { out_grid[0][cy] = in_grid[0][cy]; } } else { for (cx = 0; cx <
     * hside; cx++) { for (cy = 0; cy < vside; cy++) { out_grid[cx][cy] =
     * in_grid[cy][cy]; } } } } }
     */
  }

  public static void setColour(int colour) {
    BinGrid.graphics_handle.setColor(new Color(colour));
    if (FrEnd.xor) {
      BinGrid.graphics_handle.setXORMode(black);
    }
  }

  public static final void redraw_changed(Graphics g) {
    BinGrid.graphics_handle = g;

    if (FrEnd.module) {
      FrEnd.extension.update();
    }

    if (BinGrid.repaint_all_objects) {
      BinGrid.graphics_handle.clearRect(0, 0, 19999, 19999);

      if (BinGrid.virgin_applet > 0) {
        BinGrid.virgin_applet--;
      } else {
        BinGrid.repaint_all_objects = false;
      }

      FrEnd.node_manager.renderer.clear();
      FrEnd.node_manager.renderer2.clear();
    }

    BinGrid.deal_with_agents();
  }

  static int randomCell() {
    final int temp = BinGrid.rnd.nextInt() & 255;

    return temp;
  }

  public static final void colourZero() {
    BinGrid.setColour(0);
  }
}

  //  static final int getCoveragePercentage() {
  //    int temp = BinGrid.coverage_counter * 101 / (BinGrid.hside *
  // BinGrid.vside);
  //    if (temp > 99) {
  //      temp = 100;
  //    }
  //
  //    return temp;
  //  }
  //
  //  static void drawDepthCues() {
  //    if (FrEnd.three_d) {
  //      if (FrEnd.depth_cues) {
  //        //last_colour = 0; // because of bugs...
  //        BinGrid.colourWhite();
  //
  //        //int temp = (Coords.y_pixels * (NodeManager.piston + 24)) >>> (10 -
  //        // Coords.shift);
  //        if ((FrEnd.generation & 1) == 0) {
  //          switch ((FrEnd.generation >> 1) & 7) {
  //            case 0:
  //              Coords.drawLine(BinGrid.graphics_handle, 0, 0, 0, 0, 0,
  //                Coords.z_pixels << Coords.shift);
  //              break;
  //
  //            case 1:
  //              //Coords.drawLine(BinGrid.graphics_handle, Coords.x_pixels <<
  //              // Coords.shift, 0, 0, Coords.x_pixels << shift, 0,
  //              // Coords.z_pixels << Coords.shift);
  //              break;
  //
  //            case 2:
  //              //Coords.drawLine(BinGrid.graphics_handle, 0, temp, 0, 0, temp,
  //              // Coords.z_pixels << shift);
  //              break;
  //
  //            case 3:
  //              //Coords.drawLine(BinGrid.graphics_handle, Coords.x_pixels <<
  //              // shift, temp, 0, Coords.x_pixels << shift, temp, Coords.z_pixels
  //              // << shift);
  //              break;
  //
  //            case 4:
  //              //Coords.drawLine(BinGrid.graphics_handle, Coords.x_pixels <<
  //              // shift, 0, Coords.z_pixels << shift, Coords.x_pixels << shift,
  //              // temp, Coords.z_pixels << shift);
  //              break;
  //
  //            case 5:
  //              //Coords.drawLine(BinGrid.graphics_handle, 0, temp,
  //              // Coords.z_pixels << shift, Coords.x_pixels << shift, temp,
  //              // Coords.z_pixels << shift);
  //              break;
  //
  //            case 6:
  //              //Coords.drawLine(BinGrid.graphics_handle, Coords.x_pixels <<
  //              // shift, 0, Coords.z_pixels << shift, 0, 0, Coords.z_pixels <<
  //              // shift);
  //              break;
  //
  //            case 7:
  //              //Coords.drawLine(BinGrid.graphics_handle, 0, temp,
  //              // Coords.z_pixels << shift, 0, 0, Coords.z_pixels << shift);
  //              break;
  //
  //            default:
  //              throw new RuntimeException("PError");
  //          }
  //        }
  //      }
  //    }
  //  }

  //public static final void colourRed() {
  // if (last_colour != 7) {
  //colour = 7;
  //  BinGrid.setColour(0xFFFF0000);
  //}
  //}

  //static final void colourWhite() {
  //  BinGrid.setColour(0xFFFFFFFF);
  //}
