// * Read in information from files...

package com.springie.io.in.readers.off;

import java.util.StringTokenizer;

import com.springie.utilities.general.Forget;

// See: http://astronomy.swin.edu.au/~pbourke/polyhedra/waterman/gen/
// http://www.cs.sunysb.edu/~algorith/implement/CONVEX-HULL-JAVA/implement.shtml
// geomview: http://www.geom.umn.edu/software/download/geomview.html
// OOGL: http://www.geom.uiuc.edu/software/geomview/ooglman.html
// Other 3D file formats: http://tetgen.berlios.de/features.html

public class ReaderOFF {
  public static String translate(String in) {
    final byte[] ba = { 10 };
    final String c_r = new String(ba);

    final StringTokenizer st = new StringTokenizer(in, c_r);

    final StringBuffer out = parseTheFile(st);

    return out.toString();
  }

  private static StringBuffer parseTheFile(final StringTokenizer st) {
    final StringBuffer out = new StringBuffer();

    String line = st.nextToken();// OFF
    if (line.startsWith("OFF")) {
      line = getNextValidLine(st);
    }

    final StringTokenizer st2 = new StringTokenizer(line);

    String tok = st2.nextToken();
    final int number_of_nodes = Integer.parseInt(tok);

    int number_of_faces = 0;
    if (st2.hasMoreTokens()) {
      tok = st2.nextToken();
      number_of_faces = Integer.parseInt(tok);
    }

    int number_of_edges = 0;
    if (st2.hasMoreTokens()) {
      tok = st2.nextToken();
      number_of_edges = Integer.parseInt(tok);
    }

    //Log.log("OFF: number_of_nodes:" + number_of_nodes);
    dealWithNodes(st, out, number_of_nodes);

    //Log.log("OFF: number_of_faces:" + number_of_nodes);
    dealWithFaces(st, out, number_of_faces);

    Forget.about(number_of_edges);

    return out;
  }

  private static void dealWithNodes(final StringTokenizer st,
    final StringBuffer out, final int number_of_nodes) {
    out.append("CR NG R:0 C:0x0 ");
    final int scale_factor = 10000;
    for (int i = 0; i < number_of_nodes; i++) {
      final String line = getNextValidLine(st);
      final StringTokenizer st2 = new StringTokenizer(line);
      String tok = st2.nextToken();
      final int x = (int) (Double.valueOf(tok).doubleValue() * scale_factor);

      tok = st2.nextToken();
      final int y = (int) (Double.valueOf(tok).doubleValue() * scale_factor);

      tok = st2.nextToken();
      final int z = (int) (Double.valueOf(tok).doubleValue() * scale_factor);

      out.append("N X:" + x + " Y:" + y + " Z:" + z + " ");
    }
  }

  private static void dealWithFaces(final StringTokenizer st,
    final StringBuffer out, final int number_of_faces) {
    out.append("PG C:0x8000C0C0 ");
    for (int i = 0; i < number_of_faces; i++) {
      final String line = getNextValidLine(st);
      final StringTokenizer st2 = new StringTokenizer(line);
      String tok = st2.nextToken();
      final int n = Integer.parseInt(tok);

      out.append("P ");
      for (int j = 0; j < n; j++) {

        tok = st2.nextToken();
        final int v = Integer.parseInt(tok);

        out.append("V:" + v + " ");
      }
    }
  }

  static String getNextValidLine(StringTokenizer st) {
    boolean found;
    String tok;
    do {
      tok = st.nextToken();

      while ((tok.length() > 1) && (tok.charAt(0) == ' ')) {
        tok = tok.substring(1);
      }

      found = true;
      if (tok.length() < 1) {
        found = false;
      } else if (tok.charAt(0) == '#') {
        found = false;
      }
    } while (!found);

    return tok;
  }
}