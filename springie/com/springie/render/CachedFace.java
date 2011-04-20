// This code has been placed into the public domain by its author

package com.springie.render;

import java.awt.Point;
import java.util.Vector;

import com.springie.FrEnd;
import com.springie.collisions.BinGrid;
import com.springie.elements.DeepObjectColourCalculator;
import com.springie.elements.faces.Face;
import com.springie.elements.faces.FaceRenderTypes;
import com.springie.elements.nodes.Node;

public class CachedFace {

  public Vector render = new Vector();

  public Point render_centre = new Point(0, 0);

  int colour;

  int selected_colour;

  boolean selected;

  public void draw(Face face) {
    if (!face.type.hidden || FrEnd.render_hidden_faces) {
      //final Node n1 = (Node) face.node.elementAt(0);

      BinGrid.setColour(this.colour);

      renderFace(face, Face.face_display_type);

      if (this.selected) {
        BinGrid.setColour(this.selected_colour);
        renderSelectedFace(face);
      }
    }
  }

  public void scrub(Face face) {
    if (!FrEnd.render_hidden_links && face.type.hidden) {
      return;
    }

    BinGrid.colourZero();
    renderFace(face, Face.face_display_type);

    if (this.selected) {
      //BinGrid.colourZero();
      renderSelectedFace(face);
    }
  }

  private void renderFace(Face face, int type) {
    switch (type) {
      case FaceRenderTypes.CONCENTRIC:
        renderThinConcentric(face, Face.number_of_render_divisions);

        break;

      case FaceRenderTypes.SEGMENTED:
        renderThinSegmented(face, Face.number_of_render_divisions);
        break;

      case FaceRenderTypes.CLOCKWISE:
        renderThinClockwise(face, Face.number_of_render_divisions);
        break;

      case FaceRenderTypes.ANTI_CLOCKWISE:
        renderThinAntiClockwise(face, Face.number_of_render_divisions);
        break;

      default:
        throw new RuntimeException("");
    }
  }

  void cache(Face face, int mask) {
    final Node n1 = (Node) face.node.elementAt(0);

    this.colour = DeepObjectColourCalculator.getColourOfDeepObject(
      face.clazz.colour, n1.pos.z)
      & mask;

    this.selected_colour = DeepObjectColourCalculator.getColourOfDeepObject(
      0xFF4040, n1.pos.z)
      & mask;

    final int npoints = face.node.size();
    final int rpoints = this.render.size();
    if (rpoints != npoints) {
      setUpRenderingScratchSpace(face);
    }

    getCoordsOfCentre(face);

    this.selected = face.type.selected;

    //final Node n1 = (Node) face.node.elementAt(0);
    this.selected_colour = DeepObjectColourCalculator.getColourOfDeepObject(
      0xFF2020, n1.pos.z)
      & mask;
  }

  private void getCoordsOfCentre(Face face) {
    final int npoints = face.node.size();

    int sum_x = 0;
    int sum_y = 0;

    for (int i = npoints; --i >= 0;) {
      final Node n = (Node) face.node.elementAt(i);
      final Point scratch = (Point) this.render.elementAt(i);

      scratch.x = Coords.getXCoords(n.pos.x, n.pos.z);
      scratch.y = Coords.getYCoords(n.pos.y, n.pos.z);
      sum_x += scratch.x;
      sum_y += scratch.y;
    }

    this.render_centre.x = sum_x / npoints;
    this.render_centre.y = sum_y / npoints;
  }

  private void renderThinConcentric(Face face, int num) {
    final int npoints = face.node.size();
    final int rpoints = this.render.size();
    if (rpoints != npoints) {
      setUpRenderingScratchSpace(face);
    }

    final int actual_number = num << 1;

    for (int i = 1; i < actual_number; i = i + 2) {
      final int j = actual_number - i;
      for (int k = npoints; --k >= 0;) {
        final Point r1 = (Point) this.render.elementAt(k);
        final Point r2 = (Point) this.render.elementAt((k + 1) % npoints);

        final int x1 = (this.render_centre.x * i + r1.x * j) / actual_number;
        final int y1 = (this.render_centre.y * i + r1.y * j) / actual_number;

        final int x2 = (this.render_centre.x * i + r2.x * j) / actual_number;
        final int y2 = (this.render_centre.y * i + r2.y * j) / actual_number;

        BinGrid.graphics_handle.drawLine(x1, y1, x2, y2);
      }
    }
  }

  private void renderThinSegmented(Face face, int num) {
    final int npoints = face.node.size();
    final int rpoints = this.render.size();
    if (rpoints != npoints) {
      setUpRenderingScratchSpace(face);
    }

    final int actual_number = num << 1;

    for (int k = npoints; --k >= 0;) {
      final Point r1 = (Point) this.render.elementAt(k);
      final Point r2 = (Point) this.render.elementAt((k + 1) % npoints);
      final Point r3 = this.render_centre;
      final int c_x = (r1.x + r2.x) >> 1;
      final int c_y = (r1.y + r2.y) >> 1;

      for (int i = 1; i < actual_number; i = i + 2) {
        final int j = actual_number - i;

        final int x1 = (c_x * i + r1.x * j) / actual_number;
        final int y1 = (c_y * i + r1.y * j) / actual_number;

        final int x2 = (c_x * i + r2.x * j) / actual_number;
        final int y2 = (c_y * i + r2.y * j) / actual_number;

        final int x3 = (c_x * i + r3.x * j) / actual_number;
        final int y3 = (c_y * i + r3.y * j) / actual_number;

        //BinGrid.graphics_handle.drawLine(x1, y1, x2, y2);
        BinGrid.graphics_handle.drawLine(x2, y2, x3, y3);
        BinGrid.graphics_handle.drawLine(x3, y3, x1, y1);
      }
    }
  }

  private void renderThinClockwise(Face face, int num) {
    final int npoints = face.node.size();
    final int rpoints = this.render.size();
    if (rpoints != npoints) {
      setUpRenderingScratchSpace(face);
    }

    final int actual_number = num << 1;

    for (int i = 1; i < actual_number; i = i + 2) {
      final int j = actual_number - i;
      for (int k = npoints; --k >= 0;) {
        final Point r1 = (Point) this.render.elementAt(k);
        final Point r2 = (Point) this.render.elementAt((k + 1) % npoints);

        final int x1 = (r2.x * i + this.render_centre.x * j) / actual_number;
        final int y1 = (r2.y * i + this.render_centre.y * j) / actual_number;

        final int x2 = (r2.x * i + r1.x * j) / actual_number;
        final int y2 = (r2.y * i + r1.y * j) / actual_number;

        BinGrid.graphics_handle.drawLine(x1, y1, x2, y2);
      }
    }
  }

  private void renderThinAntiClockwise(Face face, int num) {
    final int npoints = face.node.size();
    final int rpoints = this.render.size();
    if (rpoints != npoints) {
      setUpRenderingScratchSpace(face);
    }

    final int actual_number = num << 1;

    for (int i = 1; i < actual_number; i = i + 2) {
      final int j = actual_number - i;
      for (int k = npoints; --k >= 0;) {
        final Point r1 = (Point) this.render.elementAt(k);
        final Point r2 = (Point) this.render.elementAt((k + 1) % npoints);

        final int x1 = (r1.x * i + this.render_centre.x * j) / actual_number;
        final int y1 = (r1.y * i + this.render_centre.y * j) / actual_number;

        final int x2 = (r1.x * i + r2.x * j) / actual_number;
        final int y2 = (r1.y * i + r2.y * j) / actual_number;

        BinGrid.graphics_handle.drawLine(x1, y1, x2, y2);
      }
    }
  }

  private void setUpRenderingScratchSpace(Face face) {
    if (face.node != null) {
      this.render = new Vector();

      final int npoints = face.node.size();
      for (int i = npoints; --i >= 0;) {
        this.render.addElement(new Point(0, 0));
      }
    }
  }

  void renderSelectedFace(Face face) {
    renderFace(face, Face.face_display_type ^ 3);
  }
}