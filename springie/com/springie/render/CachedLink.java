// This code has been placed into the public domain by its author

package com.springie.render;

import java.awt.Polygon;

import com.springie.FrEnd;
import com.springie.collisions.BinGrid;
import com.springie.elements.DeepObjectColourCalculator;
import com.springie.elements.links.Link;
import com.springie.elements.links.LinkRenderType;
import com.springie.geometry.Point3D;
import com.springie.utilities.math.SquareRoot;

public class CachedLink {
  public Point3D preserved_node_1 = new Point3D(0, 0, 0); // needed?

  public Point3D preserved_node_2 = new Point3D(0, 0, 0); // needed?

  int radius;

  int selected_colour;

  int colour;

  boolean selected;

  public void draw(Link link) {
    if (!FrEnd.render_hidden_links && link.type.hidden) {
      return;
    }

    final int thickness = getThicknesss();
    BinGrid.setColour(this.colour);
    renderLink(link, thickness);

    BinGrid.setColour(this.selected_colour);
    if (this.selected) {
      renderLink(link, thickness * 21 >> 4);
      renderLink(link, thickness * 11 >> 4);
    }
  }

  public void scrub(Link link) {
    if (!FrEnd.render_hidden_links && link.type.hidden) {
      return;
    }

    final int thickness = getThicknesss();

    scrubLink(link, thickness);

    if (this.selected) {
      scrubLink(link, thickness * 21 >> 4);
      scrubLink(link, thickness * 11 >> 4);
    }
  }

  private void renderLink(Link link, int thickness) {
    final int display_type = link.getDisplayType();

    switch (display_type) {
      case LinkRenderType.SOLID:
        renderSolid(thickness);

        break;

      case LinkRenderType.MULTIPLE:
        renderMultiple(link, thickness);

        break;

      case LinkRenderType.CIRCLE_THIN:
        renderCircleThin();

        break;

      case LinkRenderType.CIRCLE_THICK:
        renderCircleThick();

        break;

      //        switch (FrEnd.quality) {
      //          case Quality.THICK_OUTLINE:
      //          case Quality.SOLID:
      //          case Quality._QUALITY_1A:
      //            renderCircleFillOval(diameter, radius);
      //
      //            break;
      //
      //          case Quality.MULTIPLE:
      //            renderCircleDrawOval(diameter, radius);
      //
      //            break;
      //
      //          case Quality._QUALITY_3:
      //          case Quality._QUALITY_3A:
      //            diameter >>= 1;
      //            radius >>= 1;
      //            BinGrid.graphics_handle.drawRect(
      //              ((this.preserved_node_1.x + this.preserved_node_2.x) >> 1)
      //                - radius,
      //              ((this.preserved_node_1.y + this.preserved_node_2.y) >> 1)
      //                - radius, diameter, diameter);
      //            break;
      //
      //          case Quality._QUALITY_4:
      //          case Quality._QUALITY_4A:
      //            diameter >>= 1;
      //            radius >>= 1;
      //            BinGrid.graphics_handle.fillRect(
      //              ((this.preserved_node_1.x + this.preserved_node_2.x) >> 1)
      //                - radius,
      //              ((this.preserved_node_1.y + this.preserved_node_2.y) >> 1)
      //                - radius, diameter, diameter);
      //
      //            break;
      //
      //          default:
      //            throw new RuntimeException("");
      //        }

      case LinkRenderType.DOTTED:
        renderDotted();

        break;

      case LinkRenderType.POINT:
        BinGrid.graphics_handle.fillRect(
          ((this.preserved_node_1.x + this.preserved_node_2.x) >> 1) - 1,
          ((this.preserved_node_1.y + this.preserved_node_2.y) >> 1) - 1, 2, 2);

        break;

      case LinkRenderType.INVISIBLE:
        // do nothing...
        break;

      default:
        throw new RuntimeException("");
    }
  }

  private void scrubLink(Link link, int thickness) {
    final int display_type = link.getDisplayType();

    switch (display_type) {
      case LinkRenderType.SOLID:
        BinGrid.colourZero();
        renderSolid(thickness);
        break;

      case LinkRenderType.MULTIPLE:
        BinGrid.colourZero();
        renderMultiple(link, thickness);
        break;

      case LinkRenderType.CIRCLE_THIN:
        BinGrid.colourZero();
        renderCircleThin();

        break;

      case LinkRenderType.CIRCLE_THICK:
        BinGrid.colourZero();
        renderCircleThick();

        break;

      //      case LinkRenderType.CIRCLE:
      //        int diameter = (Math
      //          .max(link.node1.type.radius, link.node2.type.radius)) << 1;
      //        //int diameter = (diameter_first > MAX_DIAMETER) ? MAX_DIAMETER
      //        // : diameter_first;
      //        int radius = diameter >> 1;
      //
      //        switch (FrEnd.quality) {
      //          case Quality.THICK_OUTLINE:
      //          case Quality.SOLID:
      //            BinGrid.colourZero();
      //            renderCircleFillOval(diameter, radius);
      //            break;
      //
      //          case Quality.MULTIPLE:
      //          case Quality._QUALITY_2A:
      //            BinGrid.colourZero();
      //            renderCircleDrawOval(diameter, radius);
      //            break;
      //
      //          case Quality._QUALITY_1A:
      //            BinGrid.graphics_handle.clearRect(
      //              ((this.preserved_node_1.x + this.preserved_node_2.x) >> 1)
      //                - radius,
      //              ((this.preserved_node_1.y + this.preserved_node_2.y) >> 1)
      //                - radius, diameter, diameter);
      //            break;
      //
      //          case Quality._QUALITY_3:
      //          case Quality._QUALITY_3A:
      //            diameter >>= 1;
      //            radius >>= 1;
      //            BinGrid.graphics_handle.clearRect(
      //              ((this.preserved_node_1.x + this.preserved_node_2.x) >> 1)
      //                - radius,
      //              ((this.preserved_node_1.y + this.preserved_node_2.y) >> 1)
      //                - radius, diameter + 1, diameter + 1);
      //
      //            break;
      //
      //          case Quality._QUALITY_4:
      //          case Quality._QUALITY_4A:
      //            diameter >>= 1;
      //            radius >>= 1;
      //            BinGrid.graphics_handle.clearRect(
      //              ((this.preserved_node_1.x + this.preserved_node_2.x) >> 1)
      //                - radius,
      //              ((this.preserved_node_1.y + this.preserved_node_2.y) >> 1)
      //                - radius, diameter, diameter);
      //
      //            break;
      //          case Quality._QUALITY_5:
      //          case Quality._QUALITY_6:
      //            //...
      //            break;
      //
      //          default:
      //            throw new RuntimeException("");
      //        }

      //break;

      case LinkRenderType.DOTTED:
        for (int temp = 0; temp <= Link.number_of_dots; temp++) {
          BinGrid.graphics_handle
            .clearRect(
              (this.preserved_node_1.x * temp)
                + (this.preserved_node_2.x * (Link.number_of_dots - temp)) >> Link.log_number_of_dots,
              (this.preserved_node_1.y * temp)
                + (this.preserved_node_2.y * (Link.number_of_dots - temp)) >> Link.log_number_of_dots,
              1, 1);
        }

        break;

      case LinkRenderType.POINT:
        BinGrid.graphics_handle.clearRect(
          ((this.preserved_node_1.x + this.preserved_node_2.x) >> 1) - 1,
          ((this.preserved_node_1.y + this.preserved_node_2.y) >> 1) - 1, 2, 2);

        break;

      case LinkRenderType.INVISIBLE:
        // do nothing...
        break;

      default:
        throw new RuntimeException("");
    }
  }

  private void renderCircleThick() {
    //final int diameter = this.radius << 1; //Math.max(this.radius,
      //link.node2.type.radius) << 1;
    //final int diameter = Math.max(link.node1.type.radius,
      //link.node2.type.radius) << 1;
    //final int radius = diameter >> 1;
    renderCircleFillOval();
  }

  private void renderCircleThin() {
    //final int diameter = this.radius << 1;
    renderCircleDrawOval(); // diameter, this.radius
    //final int diameter = Math.max(link.node1.type.radius,
    //  link.node2.type.radius) << 1;
    //final int radius = diameter >> 1;
    //renderCircleDrawOval(diameter, radius);
  }

  void cache(Link link, int mask) {
    this.radius = link.type.radius;
    this.selected = link.type.selected;
    final int depth = (link.node1.pos.z + link.node2.pos.z) >> 1;
    this.colour = DeepObjectColourCalculator.getColourOfDeepObject(
      link.clazz.colour, depth)
      & mask;

    this.selected_colour = DeepObjectColourCalculator.getColourOfDeepObject(
      0xFF4040, depth)
      & mask;

    if ((Link.link_display_length == Link.SHORT)
      && (!link.node1.type.hidden || link.node2.type.hidden)) {
      final int d_x = (link.node1.pos.x - link.node2.pos.x) >> Coords.shift;
      final int d_y = (link.node1.pos.y - link.node2.pos.y) >> Coords.shift;
      final int d_z = (link.node1.pos.z - link.node2.pos.z) >> Coords.shift;

      final int actual_length_squared = (d_x * d_x) + (d_y * d_y) + (d_z * d_z);
      final int actual_length = SquareRoot.fastSqrt(1 + actual_length_squared);

      final int unit_vector_x = (d_x << Coords.shift) / actual_length;
      final int unit_vector_y = (d_y << Coords.shift) / actual_length;
      final int unit_vector_z = (d_z << Coords.shift) / actual_length;

      if ((Link.link_display_length == Link.SHORT) && (!link.node1.type.hidden)) {
        getPreservedShortNode1Coordinates(link, unit_vector_x, unit_vector_y,
          unit_vector_z);
      } else {
        getPreservedNode1Coordinates(link);
      }

      if ((Link.link_display_length == Link.SHORT) && (!link.node2.type.hidden)) {
        getPreservedShortNode2Coordinates(link, unit_vector_x, unit_vector_y,
          unit_vector_z);
      } else {
        getPreservedNode2Coordinates(link);
      }
    } else {
      getPreservedNode1Coordinates(link);
      getPreservedNode2Coordinates(link);
    }
  }

  private void getPreservedShortNode2Coordinates(Link link,
    final int unit_vector_x, final int unit_vector_y, final int unit_vector_z) {
    final int r2 = link.node2.type.radius >> Coords.shift;

    this.preserved_node_2.z = link.node2.pos.z + (unit_vector_z * r2);
    this.preserved_node_2.x = Coords.getXCoords(link.node2.pos.x
      + (unit_vector_x * r2), this.preserved_node_2.z);
    this.preserved_node_2.y = Coords.getYCoords(link.node2.pos.y
      + (unit_vector_y * r2), this.preserved_node_2.z);
  }

  private void getPreservedShortNode1Coordinates(Link link,
    final int unit_vector_x, final int unit_vector_y, final int unit_vector_z) {
    final int r1 = link.node1.type.radius >> Coords.shift;

    this.preserved_node_1.z = link.node1.pos.z - (unit_vector_z * r1);
    this.preserved_node_1.x = Coords.getXCoords(link.node1.pos.x
      - (unit_vector_x * r1), this.preserved_node_1.z);
    this.preserved_node_1.y = Coords.getYCoords(link.node1.pos.y
      - (unit_vector_y * r1), this.preserved_node_1.z);
  }

  private void getPreservedNode2Coordinates(Link link) {
    this.preserved_node_2.z = link.node2.pos.z;
    this.preserved_node_2.x = Coords.getXCoords(link.node2.pos.x,
      this.preserved_node_2.z);
    this.preserved_node_2.y = Coords.getYCoords(link.node2.pos.y,
      this.preserved_node_2.z);
  }

  private void getPreservedNode1Coordinates(Link link) {
    this.preserved_node_1.z = link.node1.pos.z;
    this.preserved_node_1.x = Coords.getXCoords(link.node1.pos.x,
      this.preserved_node_1.z);
    this.preserved_node_1.y = Coords.getYCoords(link.node1.pos.y,
      this.preserved_node_1.z);
  }

  void renderSelectedLink(Link link) {
    final int display_type = link.getDisplayType();

    switch (display_type) {
      case LinkRenderType.CIRCLE_THICK:
      case LinkRenderType.CIRCLE_THIN:
        BinGrid.graphics_handle.drawOval(
          ((this.preserved_node_1.x + this.preserved_node_2.x) >> 1) - 8,
          ((this.preserved_node_1.y + this.preserved_node_2.y) >> 1) - 8, 16,
          16);

        break;

      default:
        int d_x = this.preserved_node_1.y - this.preserved_node_2.y;
        int d_y = this.preserved_node_1.x - this.preserved_node_1.x;

        final int start_dx = 1 + (SquareRoot
          .fastSqrt((d_x * d_x) + (d_y * d_y))); // pixels...

        d_x = (d_x << 3) / start_dx;
        d_y = (d_y << 3) / start_dx;

        BinGrid.graphics_handle.drawLine(this.preserved_node_1.x + d_x,
          this.preserved_node_1.y + d_y, this.preserved_node_2.x + d_x,
          this.preserved_node_2.y + d_y);
        BinGrid.graphics_handle.drawLine(this.preserved_node_1.x - d_x,
          this.preserved_node_1.y - d_y, this.preserved_node_2.x - d_x,
          this.preserved_node_2.y - d_y);

        d_x >>= 1;
        d_y >>= 1;

        BinGrid.graphics_handle.drawLine(this.preserved_node_1.x + d_x,
          this.preserved_node_1.y + d_y, this.preserved_node_2.x + d_x,
          this.preserved_node_2.y + d_y);
        BinGrid.graphics_handle.drawLine(this.preserved_node_1.x - d_x,
          this.preserved_node_1.y - d_y, this.preserved_node_2.x - d_x,
          this.preserved_node_2.y - d_y);

        break;
    }
  }

  void drawStrutWide(int thicknesss) {
    int d_x = this.preserved_node_1.y - this.preserved_node_2.y; // u. vec. at
    int d_y = this.preserved_node_2.x - this.preserved_node_1.x;

    final int start_length = 1 + (SquareRoot
      .fastSqrt((d_x * d_x) + (d_y * d_y))); // pixels...

    d_x = (d_x * thicknesss) / start_length;
    d_y = (d_y * thicknesss) / start_length;
    final int x1 = this.preserved_node_1.x + d_x;
    final int x2 = this.preserved_node_2.x + d_x;
    final int x3 = this.preserved_node_1.x - d_x;
    final int x4 = this.preserved_node_2.x - d_x;

    final int y1 = this.preserved_node_1.y + d_y;
    final int y2 = this.preserved_node_2.y + d_y;
    final int y3 = this.preserved_node_1.y - d_y;
    final int y4 = this.preserved_node_2.y - d_y;
    BinGrid.graphics_handle.drawLine(x1, y1, x2, y2);
    BinGrid.graphics_handle.drawLine(x3, y3, x4, y4);
  }

  void drawStrutSolid(int thicknesss) {
    BinGrid.graphics_handle.fillPolygon(getStrutPolygon(thicknesss));
  }

  public Polygon getStrutPolygon(int thicknesss) {
    int d_x = this.preserved_node_1.y - this.preserved_node_2.y; // u. vec. at
    int d_y = this.preserved_node_2.x - this.preserved_node_1.x;

    final int start_length = 1 + (SquareRoot
      .fastSqrt((d_x * d_x) + (d_y * d_y)));

    d_x = (d_x * thicknesss) / start_length;
    d_y = (d_y * thicknesss) / start_length;
    final int x1 = this.preserved_node_1.x + d_x;
    final int x2 = this.preserved_node_2.x + d_x;
    final int x3 = this.preserved_node_2.x - d_x;
    final int x4 = this.preserved_node_1.x - d_x;

    final int y1 = this.preserved_node_1.y + d_y;
    final int y2 = this.preserved_node_2.y + d_y;
    final int y3 = this.preserved_node_2.y - d_y;
    final int y4 = this.preserved_node_1.y - d_y;
    final int[] iax = { x1, x2, x3, x4, };
    final int[] iay = { y1, y2, y3, y4, };
    return new java.awt.Polygon(iax, iay, 4);
  }

  private void renderCircleDrawOval() {
    final int diameter = this.radius;
    BinGrid.graphics_handle.drawOval(
      ((this.preserved_node_1.x + this.preserved_node_2.x) >> 1) - this.radius,
      ((this.preserved_node_1.y + this.preserved_node_2.y) >> 1) - this.radius,
      diameter, diameter);
  }

  private void renderCircleFillOval() {
    final int diameter = this.radius;
    BinGrid.graphics_handle.fillOval(
      ((this.preserved_node_1.x + this.preserved_node_2.x) >> 1) - this.radius,
      ((this.preserved_node_1.y + this.preserved_node_2.y) >> 1) - this.radius,
      diameter, diameter);
  }

  private void renderDotted() {
    for (int temp = 0; temp <= Link.number_of_dots; temp++) {
      BinGrid.graphics_handle
        .fillRect(
          (this.preserved_node_1.x * temp)
            + (this.preserved_node_2.x * (Link.number_of_dots - temp)) >> Link.log_number_of_dots,
          (this.preserved_node_1.y * temp)
            + (this.preserved_node_2.y * (Link.number_of_dots - temp)) >> Link.log_number_of_dots,
          1, 1);
    }
  }

  private void renderMultiple(int n, int thickness) {
    if ((n & 1) != 0) {
      renderSingle();
    }

    if (n > 1) {
      renderTwoOrMore(n, thickness);
    }
  }

  private void renderSingle() {
    BinGrid.graphics_handle
      .drawLine(this.preserved_node_1.x, this.preserved_node_1.y,
        this.preserved_node_2.x, this.preserved_node_2.y);
  }

  private void renderSolid(int thickness) {
    drawStrutSolid(thickness);
  }

  private void renderMultiple(Link link, int thickness) {
    if (link.type.cable) {
      renderMultiple(Link.number_of_cable_render_divisions, thickness);
    } else {
      renderMultiple(Link.number_of_strut_render_divisions, thickness);
    }
  }

  private void renderTwoOrMore(int n, int thickness) {
    final int n2 = n - 1;
    final int t = thickness / n2;
    for (int i = 1 + (n & 1); i <= n2; i = i + 2) {
      drawStrutWide(t * i);
    }
  }
  
  public int getThicknesss() {
    return this.radius >> Coords.shift;
  }
}