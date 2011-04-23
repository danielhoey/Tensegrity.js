//This code has been placed into the public domain by its author

package com.springie.presets;

import java.io.IOException;

import org.xml.sax.SAXException;

import com.springie.FrEnd;
import com.springie.io.in.readers.spr.ReaderSPR;
import com.springie.messages.ArgumentList;
import com.springie.utilities.random.JUR;

public class PresetObjects {
  static JUR rnd = new JUR();

  public static final String getCreatureDescription(ArgumentList arguments) {
    final ProceduralObjectInstance type = (ProceduralObjectInstance) arguments.getArguments(0);

    if (type == ProceduralObject.tube) {
      final int circum = ((Integer) arguments.getArguments(1)).intValue();
      final int length = ((Integer) arguments.getArguments(2)).intValue();
      return makeTube(circum, length);
    } else if (type == ProceduralObject.free_nodes) {
      final int n = ((Integer) arguments.getArguments(1)).intValue();
      return freeNodes(n);
    } else if (type == ProceduralObject.sphere_pack) {
      final int n = ((Integer) arguments.getArguments(1)).intValue();
      return boundNodes(n);
    } else if (type == ProceduralObject.matrix) {
      final int n1 = ((Integer) arguments.getArguments(1)).intValue();
      final int n2 = ((Integer) arguments.getArguments(2)).intValue();
      final int n3 = ((Integer) arguments.getArguments(3)).intValue();
      return makeMatrix(n1, n2, n3);
    }

    return null;
  }

  public static final String getCreatureDescription(String location) throws IOException, SAXException {
//    if (FrEnd.archive != null) {
//      if (!"".equals(FrEnd.archive)) {
//        location = FrEnd.archive;
//        FrEnd.last_file_path = location;
//      }
//    }
        
    FrEnd.setFilePath(location);
    
    return new ReaderSPR().translate(location);
  }

  static String getPathFromXMLFile(String leaf, String file) {
    try {
      final String out = new ReadXMLModelIndexFile().translate(leaf, file);
      //Log.log("getPathFromXMLFile out:" + out);
      return out;
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    }

    return "";
  }

  static final String boundNodes(int nodes) {
    // 4, 6, 12, 32, 42, 72, 92, 162, ?(252), 362
    final int number_of_spheres = nodes;
    final int scale_factor = 3;
    final int ix = rnd.nextInt(10000 * scale_factor);
    final int iy = rnd.nextInt(10000 * scale_factor);
    int initial_size = 1000 * scale_factor;
    String dome = "CR NG R:" + initial_size + " CH:0 C:0xff20ff20 ";
    dome += "N X:" + ix + " Y:" + iy + " Z:5000 ";

    initial_size = 500 * scale_factor;

    for (int i = 1; i <= number_of_spheres; i++) {
      final int rx = rnd.nextInt(10000 * scale_factor);
      final int ry = rnd.nextInt(10000 * scale_factor);

      dome += "NG R:" + initial_size + " CH:1 C:0xFFFF60A0 ";
      dome += "N X:" + rx + " Y:" + ry + " Z:" + (rx + ry) + " ";
    }

    final int length = 2000 * scale_factor;

    initial_size = 800 * scale_factor;
    dome += "LG L:" + length + " R:" + (initial_size >> 1) + " ";
    dome += "E:2 C:0xFFC0FFFF ";

    for (int i = 1; i <= number_of_spheres; i++) {
      dome += "LK V:0 V:" + i + " ";
    }

    return dome;
  }

  static final String freeNodes(int nodes) {
    // 4, 6, 12, 32, 42, 72, 92, 162, ?(252), 362
    final int number_of_spheres = nodes;
    final int scale_factor = 3;
    int initial_size = 2560 * scale_factor;
    String dome = "CR ";

    initial_size = 1060 * scale_factor;

    for (int i = 1; i <= number_of_spheres; i++) {
      final int rx = rnd.nextInt(10000 * scale_factor);
      final int ry = rnd.nextInt(10000 * scale_factor);

      dome += "NG R:" + initial_size + " CH:10 C:0xFF80C0FF ";
      dome += "N X:" + rx + " Y:" + ry + " Z:" + (rx + ry) + " ";
      dome += "DX:" + (rx & 7) + " DY:" + (ry & 7) + " DZ:" + (rx & 7) + " ";
    }

    for (int i = 1; i <= number_of_spheres; i++) {
      final int rx = rnd.nextInt(10000 * scale_factor);
      final int ry = rnd.nextInt(10000 * scale_factor);

      dome += "NG R:" + initial_size + " CH:-10 C:0xFFFF80C0 ";
      dome += "N X:" + ry + " Y:" + rx + " Z:" + (rx + ry) + " ";
    }

    return dome;
  }

  static final String makeMatrix(int n1, int n2, int n3) {
    String dome = "CR ";
    final int scale_factor = 3;

    final int ix = 80000;
    final int iy = 80000;
    final int iz = 80000;

    int i = 0;

    final int nodes_x = (int) (n1 * 1.0);
    final int nodes_y = (int) (n2 * 1.2);
    final int nodes_z = (int) (n3 * 1.3);
    final int nodes_sq = nodes_x * nodes_y;

    for (int z = 0; z < nodes_z; z++) {
      for (int y = 0; y < nodes_y; y++) {
        for (int x = 0; x < nodes_x; x++) {
          int r_x = ix + x * 6000 * scale_factor;
          int r_y = iy + y * 4000 * scale_factor;
          final int r_z = iz + z * 3500 * scale_factor;

          if ((y & 1) == 0) {
            r_x -= 3000 * scale_factor;
          }

          if ((z & 1) == 0) {
            r_x -= 3000 * scale_factor;
            r_y -= 2000 * scale_factor;
          }

          final int size = 3 * 256 * scale_factor;

          dome += "NG R:" + size + " C:0xFFFFFF20 ";
          dome += "N X:" + r_x + " Y:" + r_y + " Z:" + r_z + " ";
          final int length = 23 * 256 * scale_factor;
          final int e = 50;

          dome += "LG L:" + length + " E:" + e + " R:" + (length >> 3)
            + " C:0xFFFFFF ";

          if (x > 0) {
            dome += "LK V: " + i + " V:" + (i - 1) + " ";
          }
          if (y > 0) {
            dome += "LK V: " + i + " V:" + (i - nodes_x) + " ";
            if ((y & 1) == 0) {
              if (x > 0) {
                dome += "LK V: " + i + " V:" + (i - nodes_x - 1) + " ";
              }
            } else {
              if (x < nodes_x - 1) {
                dome += "LK V: " + i + " V:" + (i - nodes_x + 1) + " ";
              }
            }
          }
          if (z > 0) {
            dome += "LK V: " + i + " V:" + (i - nodes_sq) + " ";
            if ((z & 1) == 0) {
              if (x > 0) {
                dome += "LK V: " + i + " V:" + (i - nodes_sq - 1) + " ";
              }
              if (y > 0) {
                dome += "LK V: " + i + " V:" + (i - nodes_sq - nodes_x) + " ";
              }
            } else {
              if (x < nodes_x - 1) {
                dome += "LK V: " + i + " V:" + (i - nodes_sq + 1) + " ";
              }
              if (y < nodes_y - 1) {
                dome += "LK V: " + i + " V:" + (i - nodes_sq + nodes_x) + " ";
              }
            }
          }
          i++;
        }
      }
    }

    return dome;
  }

  static String makeTube(int circum, int nodes) {
    final int[] colours = new ColourFactory(65418).getColourArray(circum * 3);
    final int scale_factor = 1;
    String dome = "CR ";

    final int ix = 8000;
    final int iy = 8000;
    final int iz = 8000;

    int i = 0;
    final int length = 20 * 256 * scale_factor;

    final int circum_mo = circum - 1;

    for (int x = 0; x < nodes; x++) {
      final int r_x = ix + x * (length / 5) + rnd.nextInt(999);
      final int r_y = iy + x * (length / 5) + rnd.nextInt(999);
      final int r_z = iz + x * (length / 5) + rnd.nextInt(999);

      dome += "NG R:" + (length >> 2) + " C:0xff20ff20 ";
      dome += "N X:" + r_x + " Y:" + r_y + " Z:" + r_z + " ";

      dome += "LG L:" + length + " R:" + (length >> 3) + " ";
      dome += "E:60 ";
      int colour = colours[((x % circum) * 3) + 0] | 0x808080;
      dome += "C:" + colour + " ";

      if (x > 0) {
        dome += "LK V: " + i + " V:" + (i - 1) + " ";
      }

      if (circum > 2) {
        dome += "LG L:" + length + " R:" + (length >> 3) + " ";
        dome += "E:60 ";
        colour = colours[((x % circum) * 3) + 1] | 0x808080;
        dome += "C:" + colour + " ";

        if (x >= circum_mo) {
          dome += "LK V: " + i + " V:" + (i - circum_mo) + " ";
        }
      }
      if (circum > 1) {
        dome += "LG L:" + length + " R:" + (length >> 3) + " ";
        dome += "E:60 ";
        colour = colours[((x % circum) * 3) + 2] | 0x808080;
        dome += "C:" + colour + " ";

        if (x >= circum) {
          dome += "LK V: " + i + " V:" + (i - circum) + " ";
        }
      }

      i += 1;
    }

    return dome;
  }
}