// * Read in information from files...

package com.springie.io.in.readers.m;

import java.util.StringTokenizer;
import java.util.Vector;

import com.springie.geometry.Point3D;
import com.springie.utilities.log.Log;

public class ReaderM {
  Vector nodes = new Vector();

  Vector faces = new Vector();

  Vector current_polygon = new Vector();

  int scale_factor = 30000;

  int node_number;

  boolean in_polygon;

  boolean first_node = true;

  public String translate(String in) {
    final byte[] ba = { 10, 13 };
    final String c_r = new String(ba);

    final StringTokenizer st = new StringTokenizer(in, ", [](){}" + c_r);

    final StringBuffer out = parseTheFile(st);

    return out.toString();
  }

  private StringBuffer parseTheFile(final StringTokenizer st) {
    final StringBuffer out = new StringBuffer();
    extractNodes(st, out);
    outputFaces(out);

    Log.log("" + out);
    
    return out;
  }

  private void outputFaces(StringBuffer out) {
    final int n = this.faces.size();
    out.append("PG C:0xFFB0FFFF ");

    for (int i = 0; i < n; i++) {
      out.append("P ");
      final Vector face = (Vector) this.faces.elementAt(i);
      final int number_of_nodes = face.size();
      for (int j = 0; j < number_of_nodes; j++) {
        final Integer integer = (Integer) face.elementAt(j);
        out.append("V:" + integer.intValue() + " ");
      }
    }
  }

  private void extractNodes(final StringTokenizer st, final StringBuffer out) {
    String token = null;
    boolean first_polygon = true;
    boolean point = false;
    boolean rgb_color = false;
    boolean point_size = false;
    int colour = 0xFFFFB0FF;
    int radius = 0;

    out.append("CR NG R:0 C:0xFFFFB0FF ");

    token = getNextValidToken(st);

    do {
      boolean abnormal;
      do {
        token = getNextValidToken(st);
        
        final boolean polygon = "Polygon".equals(token);
        point = "Point".equals(token);
        point_size = "PointSize".equals(token);
        rgb_color = "RGBColor".equals(token);

        final boolean finished = !point_size && !rgb_color && !polygon && !point && token.charAt(0) > 64;
        abnormal = finished || polygon;

        if (abnormal) {
          this.in_polygon = true;
          if (first_polygon) {
            first_polygon = false;
          } else {
            this.faces.addElement(this.current_polygon);
          }

          this.current_polygon = new Vector();
          if (finished) {
            Log.log("TERM TOK:" + token);
            return;
          }
        }
      } while (abnormal);

      if (rgb_color) {
        token = getNextValidToken(st);
        final double r = Double.valueOf(token).doubleValue();
        token = getNextValidToken(st);
        final double g = Double.valueOf(token).doubleValue();
        token = getNextValidToken(st);
        final double b = Double.valueOf(token).doubleValue();
        
        final int ir = (int)(r * 255);
        final int ig = (int)(g * 255);
        final int ib = (int)(b * 255);
        colour = ir << 16 | ig << 8 | ib;
      } else if (point_size) {
        token = getNextValidToken(st);
        final double dradius = Double.valueOf(token).doubleValue();
        radius = (int) (dradius * this.scale_factor);
      } else if (point) {
        token = getNextValidToken(st);
        final double xd = Double.valueOf(token).doubleValue();
        token = getNextValidToken(st);
        final double yd = Double.valueOf(token).doubleValue();
        token = getNextValidToken(st);
        final double zd = Double.valueOf(token).doubleValue();

        final int x = (int) (xd * this.scale_factor);
        final int y = (int) (yd * this.scale_factor);
        final int z = (int) (zd * this.scale_factor);

        out.append("NG R:" + radius + " ");
        out.append("C:0xFF" + Long.toString(colour & 0xFFFFFF , 16) + " ");
        out.append("N X:" + x + " Y:" + y + " Z:" + z + " ");
        Log.log("X:" + x + " Y:" + y + " Z:" + z + " ");

        this.current_polygon.addElement(new Integer(this.node_number));
      } else if (this.in_polygon) {
        final double xd = Double.valueOf(token).doubleValue();
        token = getNextValidToken(st);
        final double yd = Double.valueOf(token).doubleValue();
        token = getNextValidToken(st);
        final double zd = Double.valueOf(token).doubleValue();

        final Point3D node = new Point3D(0, 0, 0);
        node.x = (int) (xd * this.scale_factor);
        node.y = (int) (yd * this.scale_factor);
        node.z = (int) (zd * this.scale_factor);

        this.node_number = findNumberOfNode(node);
        if (this.node_number == -1) {
          this.node_number = this.nodes.size();
          this.nodes.addElement(node);
          out.append("N X:" + node.x + " Y:" + node.y + " Z:" + node.z + " ");
        }
        this.current_polygon.addElement(new Integer(this.node_number));
      }
    } while (true);
  }

  private int findNumberOfNode(Point3D node) {
    final int n = this.nodes.size();
    for (int i = 0; i < n; i++) {
      final Point3D p = (Point3D) this.nodes.elementAt(i);
      if (p.equals(node)) {
        return i;
      }
    }
    return -1;
  }

  static String getNextValidToken(StringTokenizer st) {
    boolean found;
    String tok;
    do {
      tok = st.nextToken();

      while ((tok.length() > 1) && (tok.charAt(0) < 33)) {
        tok = tok.substring(1);
      }

      found = true;
      if (tok.length() < 1) {
        found = false;
      }
      if ("".equals(tok)) {
        found = false;
      }
    } while (!found);

    final int i = tok.indexOf("*^");

    if (i > 0) {
      tok = tok.substring(0, i) + "E" + tok.substring(i + 2);
    }

    return tok;
  }
}