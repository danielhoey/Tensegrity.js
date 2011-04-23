//This code has been placed into the public domain by its author

package com.springie.elements.faces;

import java.util.Vector;

import com.springie.FrEnd;
import com.springie.elements.base.BaseElementManager;
import com.springie.elements.clazz.Clazz;
import com.springie.elements.nodes.Node;
import com.springie.render.Coords;
import com.tifsoft.deprecated.OldMethods;

public class FaceManager extends BaseElementManager {
  static int agent_counter;

  public FaceTypeFactory face_type_factory = new FaceTypeFactory();

  static Node temp_node;

  static int threshold = 32;

  static int bigthreshold = threshold << Coords.shift;

  public FaceManager() {
    //this.element = new Vector();

    reset();
  }

  /**
   * Sets a polygon between e1 and e2, with target length lenth, elasticity,
   * colour and status flags specified
   */
  public final Face setPolygon(Vector v, FaceType type, Clazz clazz) {
    final Face p = new Face(v, type, clazz);
    this.element.addElement(p);

    //final int npoints = v.size();
    //for (int k = npoints; --k >= 0;) {
    //final Node n = (Node) v.elementAt(k);
    //n.list_of_polygons.addElement(p);
    //}

    return p;
  }

  public final Face setPolygon(FaceType type, Clazz clazz) {
    final Face p = new Face(new Vector(), type, clazz);
    this.element.addElement(p);

    return p;
  }

  // hmm...
  /**
   * Destroys all polygons to Node e
   */
  public final void killAllPolygons(Node node) {
    final int number = this.element.size();
    for (int temp = number; --temp >= 0;) {
      final Face face = (Face) this.element.elementAt(temp);
      if (face.containsNodes(node)) {
        killNumberedPolygon(temp);
      }
    }
  }

  final void killNumberedPolygon(int n) {
    this.element.removeElementAt(n);
  }

  final int getNumberOfPolygon(Face lk) {
    final int n_o_l = this.element.size();
    for (int temp = n_o_l; --temp >= 0;) {
      final Face l = (Face) this.element.elementAt(temp);
      if (l == lk) {
        return temp;
      }
    }

    return -1; // not found...
  }

  //  public final void drawThePolygons() {
  //    final int n_o_l = this.element.size();
  //    for (int temp = n_o_l; --temp >= 0;) {
  //      final Face face = (Face) this.element.elementAt(temp);
  //      face.draw(face);
  //    }
  //  }
  //
  //  public final void scrubThePolygons() {
  //    BinGrid.colourZero();
  //    final int n_o_l = this.element.size();
  //    for (int temp = n_o_l; --temp >= 0;) {
  //      final Face l = (Face) this.element.elementAt(temp);
  //      l.scrub(face);
  //    }
  //  }
  //
  //  public final void polygonRender() {
  //    final int n_o_l = this.element.size();
  //    for (int temp = n_o_l; --temp >= 0;) {
  //      final Face l = (Face) this.element.elementAt(temp);
  //      l.scrub(face);
  //      l.draw(face);
  //    }
  //  }

  public boolean isThereAPolygonWithNodes(Vector node_list) {
    final int n_o_l = this.element.size();
    for (int temp = n_o_l; --temp >= 0;) {
      final Face poly = (Face) this.element.elementAt(temp);
      if (poly.hasExactlyTheseNodes(node_list)) {
        return true;
      }
    }

    return false;
  }

  public final void deselectAll() {
    final int n = this.element.size();
    for (int temp = n; --temp >= 0;) {
      final Face poly = (Face) this.element.elementAt(temp);
      poly.type.selected = false;
    }
    FrEnd.updateGUIToReflectSelectionChange();
  }

  public final void selectAll() {
    final int n = this.element.size();
    for (int temp = n; --temp >= 0;) {
      final Face face = (Face) this.element.elementAt(temp);
      if (!face.type.hidden || FrEnd.render_hidden_faces) {
        face.type.selected = true;
      }
    }
    FrEnd.updateGUIToReflectSelectionChange();
  }

  public final Face isThereOne(int x, int y) {
    Face best = null;
    int best_z = Integer.MAX_VALUE;

    final int n_o_l = this.element.size();
    for (int temp = n_o_l; --temp >= 0;) {
      final Face p = (Face) this.element.elementAt(temp);
      if (!p.type.hidden || FrEnd.render_hidden_faces) {
        final int npoints = p.node.size();

        int sum_z = 0;
        final int[] awt_points_x = new int[npoints];
        final int[] awt_points_y = new int[npoints];

        for (int i = npoints; --i >= 0;) {
          final Node n = (Node) p.node.elementAt(i);

          int nx = Coords.getXCoordsInternal(n.pos.x, n.pos.z);
          int ny = Coords.getXCoordsInternal(n.pos.y, n.pos.z);
          awt_points_x[i] = nx;
          awt_points_y[i] = ny;

          sum_z += n.pos.z;
        }

        final java.awt.Polygon awt_polygon = new java.awt.Polygon(awt_points_x,
          awt_points_y, npoints);

        int av_z = sum_z / npoints;

        if (best_z > av_z) {
          if (OldMethods.isInsidePolygon(x, y, awt_polygon)) {
            best = p;
            best_z = av_z;
          }
        }
      }
    }

    return best;
  }

  public boolean isSelection() {
    final int number = this.element.size();
    for (int temp = number; --temp >= 0;) {
      final Face l = (Face) this.element.elementAt(temp);
      if (l.type.selected) {
        return true;
      }
    }

    return false;
  }

  public final Face getFirstSelectedPolygon() {
    final int n = this.element.size();
    for (int temp = n; --temp >= 0;) {
      final Face p = (Face) this.element.elementAt(temp);
      if (p.type.selected) {
        return p;
      }
    }

    return null;
  }

  public final void selectAll(int colour) {
    final int number = this.element.size();
    for (int temp = number; --temp >= 0;) {
      final Face face = (Face) this.element.elementAt(temp);
      if (face.clazz.colour == colour) {
        if (!face.type.hidden || FrEnd.render_hidden_faces) {
          face.type.selected = true;
        }
      }
    }
    FrEnd.updateGUIToReflectSelectionChange();
  }

  public final void selectAllWithNSides(int n) {
    final int number = this.element.size();
    for (int temp = number; --temp >= 0;) {
      final Face p = (Face) this.element.elementAt(temp);
      if (p.node.size() == n) {
        p.type.selected = true;
      }
    }
    FrEnd.updateGUIToReflectSelectionChange();
  }

  public final void deleteSelected() {
    final int number = this.element.size();
    for (int temp = number; --temp >= 0;) {
      final Face p = (Face) this.element.elementAt(temp);
      if (p.type.selected) {
        kill(p);
      }
    }
    FrEnd.updateGUIToReflectSelectionChange();
  }

  final void killNumbered(int n) {
    //final Face p = (Face) this.element.elementAt(n);
    //final int npoints = p.node.size();
    this.element.removeElementAt(n);
    // TODO!

    //for (int i = npoints; --i >= 0;) {
    //final Node node = (Node) p.node.elementAt(i);
    //node.list_of_polygons.removeElement(p);
    //}
  }

  final int getNumberOf(Face polygon) {
    final int number = this.element.size();
    for (int temp = number; --temp >= 0;) {
      final Face p = (Face) this.element.elementAt(temp);
      if (p == polygon) {
        return temp;
      }
    }

    return -1; // not found...
  }

  public final void kill(Face polygon) {
    final int temp = getNumberOf(polygon);
    killNumbered(temp);
  }
}