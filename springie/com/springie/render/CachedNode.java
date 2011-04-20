// This code has been placed into the public domain by its author

package com.springie.render;

import com.springie.FrEnd;
import com.springie.collisions.BinGrid;
import com.springie.constants.Quality;
import com.springie.elements.DeepObjectColourCalculator;
import com.springie.elements.faces.Face;
import com.springie.elements.faces.FaceManager;
import com.springie.elements.links.Link;
import com.springie.elements.links.LinkManager;
import com.springie.elements.lists.ListOfIntegers;
import com.springie.elements.nodes.Node;
import com.springie.geometry.Point3D;

public class CachedNode {
  public Point3D pos = new Point3D(0, 0, 0);

  int radius;

  int colour;

  int selected_colour;

  boolean selected;

  public void draw(Node node) {
    if (!FrEnd.render_hidden_nodes && node.type.hidden) {
      return;
    }

    BinGrid.setColour(this.colour);

    final int temp_x = this.pos.x - this.radius;
    final int temp_y = this.pos.y - this.radius;
    final int temp_diameter = this.radius << 1;
    final int thickness = Node.thickness;

    switch (FrEnd.quality) {
      case Quality.SOLID:
        renderSolid(temp_x, temp_y, temp_diameter);

        break;

      case Quality.THICK_OUTLINE:
        renderSolid(temp_x, temp_y, temp_diameter);

        BinGrid.setColour(0);
        BinGrid.graphics_handle.fillOval(temp_x + thickness,
          temp_y + thickness, temp_diameter - thickness - thickness,
          temp_diameter - thickness - thickness);

        break;

      case Quality._QUALITY_1A:
        renderSolid(temp_x, temp_y, temp_diameter);

        break;

      case Quality.MULTIPLE:
        renderMultiple(temp_x, temp_y, temp_diameter);

        break;

      case Quality._QUALITY_3A:
        BinGrid.graphics_handle.drawRect(this.pos.x - (this.radius >> 1),
          this.pos.y - (this.radius >> 1), this.radius, this.radius);
        break;

      case Quality._QUALITY_3:
        BinGrid.graphics_handle.drawRect(this.pos.x - (this.radius >> 1),
          this.pos.y - (this.radius >> 1), this.radius, this.radius);

        break;

      case Quality._QUALITY_4A:
        BinGrid.graphics_handle.fillRect(this.pos.x - (this.radius >> 1),
          this.pos.y - (this.radius >> 1), this.radius, this.radius);

        break;

      case Quality._QUALITY_4:
        BinGrid.graphics_handle.fillRect(this.pos.x - (this.radius >> 1),
          this.pos.y - (this.radius >> 1), this.radius, this.radius);

        break;

      case Quality._QUALITY_5:
        BinGrid.graphics_handle.fillRect(this.pos.x - 1, this.pos.y - 1, 2, 2);

        break;

      case Quality._QUALITY_6:
        break;

      default:
        throw new RuntimeException("");
    }

    plotChargeSymbols(node, this.colour);

    if (this.selected) {
      BinGrid.setColour(this.selected_colour);
      drawSelectedNode(temp_x, temp_y, temp_diameter);
      //      BinGrid.graphics_handle.drawOval(temp_x - 8, temp_y - 8,
      //      temp_diameter + 16, temp_diameter + 16);
    }
  }

  private void renderSolid(final int temp_x, final int temp_y,
    final int temp_diameter) {
    BinGrid.graphics_handle.fillOval(temp_x, temp_y, temp_diameter,
      temp_diameter);
  }

  // colour set up?
  public void scrub(Node node) {
    if (!FrEnd.render_hidden_nodes && node.type.hidden) {
      return;
    }

    final int temp_x = this.pos.x - this.radius;
    final int temp_y = this.pos.y - this.radius;
    final int temp_diameter = this.radius << 1;

    final int temp2 = FrEnd.quality;

    switch (temp2) {
      case Quality.THICK_OUTLINE:
      case Quality.SOLID:
        BinGrid.colourZero();
        renderSolid(temp_x, temp_y, temp_diameter);
        break;

      case Quality._QUALITY_1A:
        BinGrid.graphics_handle.clearRect(temp_x, temp_y, temp_diameter,
          temp_diameter);

        break;

      //      case Quality._QUALITY_2A:
      //        BinGrid.graphics_handle.clearRect(temp_x, temp_y, temp_diameter + 1,
      //          temp_diameter + 1);
      //
      //        break;

      //case Quality.THIN_OUTLINE:
      case Quality.MULTIPLE:
        BinGrid.colourZero();
        renderMultiple(temp_x, temp_y, temp_diameter);

        break;

      case Quality._QUALITY_3A:
        BinGrid.graphics_handle.clearRect(this.pos.x - (this.radius >> 1),
          this.pos.y - (this.radius >> 1), this.radius + 1, this.radius + 1);

        break;

      case Quality._QUALITY_3:
        BinGrid.graphics_handle.clearRect(this.pos.x - (this.radius >> 1),
          this.pos.y - (this.radius >> 1), this.radius + 1, this.radius + 1);

        break;

      case Quality._QUALITY_4A:
        BinGrid.graphics_handle.clearRect(this.pos.x - (this.radius >> 1),
          this.pos.y - (this.radius >> 1), this.radius, this.radius);

        break;

      case Quality._QUALITY_4:
        BinGrid.graphics_handle.clearRect(this.pos.x - (this.radius >> 1),
          this.pos.y - (this.radius >> 1), this.radius, this.radius);

        break;

      case Quality._QUALITY_5:
        BinGrid.graphics_handle.clearRect(this.pos.x - 1, this.pos.y - 1, 2, 2);

        break;

      case Quality._QUALITY_6:
        break;

      default:
        throw new RuntimeException("");
    }

    scrubChargeSymbols(node);

    if (this.selected) {
      BinGrid.colourZero();
      drawSelectedNode(temp_x, temp_y, temp_diameter);
    }
  }

  private void drawSelectedNode(final int temp_x, final int temp_y,
    final int temp_diameter) {
    BinGrid.graphics_handle.drawOval(temp_x - 6, temp_y - 6,
      temp_diameter + 12, temp_diameter + 12);
  }

  private void cache(Node node, int mask) {
    if (!FrEnd.render_hidden_nodes && node.type.hidden) {
      return;
    }

    this.pos.x = Coords.getXCoords(node.pos.x, node.pos.z);
    this.pos.y = Coords.getYCoords(node.pos.y, node.pos.z);
    this.radius = Coords.getRadius(node.type.radius, node.pos.z);
    this.selected = node.type.selected;
    //this.charge = node.type.charge;

    this.colour = DeepObjectColourCalculator.getColourOfDeepObject(
      node.clazz.colour, node.pos.z)
      & mask;

    if (this.selected) {
      this.selected_colour = DeepObjectColourCalculator.getColourOfDeepObject(
        0xFF4040, this.pos.z)
        & mask;
    }
  }

  private void renderMultiple(final int x, final int y, final int diameter) {
    final int n = Node.number_of_render_divisions - 1;
    if (n < 0) {
      return;
    }

    final int inner = (diameter * 3) >> 3;
    int delta = 1;
    int target = 0;

    if (n != 0) {
      delta = Math.max(inner / n, 1);
      target = delta * n;
    }

    for (int i = 0; i <= target; i += delta) {
      final int i_o2 = i >> 1;
      final int d_mi = diameter - i;
      BinGrid.graphics_handle.drawOval(x + i_o2, y + i_o2, d_mi, d_mi);
    }
  }

  private void plotChargeSymbols(Node node, int colour) {
    if (node.type.charge != 0) {
      BinGrid.setColour(colour);
      renderTheChargeSymbol(node);
    }
  }

  private void scrubChargeSymbols(Node node) {
    if (node.type.charge != 0) {
      BinGrid.setColour(0);
      renderTheChargeSymbol(node);
    }
  }

  private void renderTheChargeSymbol(Node node) {
    if (FrEnd.render_charges) {
      if (node.type.charge > 0) {
        plotPositiveChargeSign();
      } else {
        plotNegativeChargeSign();
      }
    }
  }

  private void plotPositiveChargeSign() {
    final int x1 = this.pos.x - (this.radius >> 1);
    final int x2 = this.pos.x - (this.radius >> 3);
    final int x3 = this.pos.x + (this.radius >> 3);
    final int x4 = this.pos.x + (this.radius >> 1);

    final int y1 = this.pos.y - (this.radius >> 1);
    final int y2 = this.pos.y - (this.radius >> 3);
    final int y3 = this.pos.y + (this.radius >> 3);
    final int y4 = this.pos.y + (this.radius >> 1);

    final int[] x = { x3, x3, x4, x4, x3, x3, x2, x2, x1, x1, x2, x2 };
    final int[] y = { y1, y2, y2, y3, y3, y4, y4, y3, y3, y2, y2, y1 };

    BinGrid.graphics_handle.drawPolygon(x, y, 12);
  }

  private void plotNegativeChargeSign() {
    final int x1 = this.pos.x - (this.radius >> 1);

    final int y1 = this.pos.y - (this.radius >> 3);

    BinGrid.graphics_handle.drawRect(x1, y1, this.radius, this.radius >> 2);
  }

  public void renderNodes(final Node node, int mask) {
    if (FrEnd.xor) {
      draw(node);
    } else {
      scrub(node);
    }

    cache(node, mask);
    draw(node);
  }

  public void renderLinks(RendererLink renderer_link, LinkManager link_manager,
    final Node n, int mask) {
    final ListOfIntegers list_of_links = n.list_of_links;
    final int number = list_of_links.size();
    final int total_number = link_manager.element.size();

    renderer_link.ensureCapacity(total_number);

    for (int l = number; --l >= 0;) {
      final int num_of_link = list_of_links.retreive(l);
      final Link link = (Link) link_manager.element.elementAt(num_of_link);
      final CachedLink cached_link = renderer_link.array[num_of_link];
      final Node other = link.theOtherEnd(n);
      if (n.pos.z >= other.pos.z) {
        if (FrEnd.xor) {
          cached_link.draw(link);
        } else {
          cached_link.scrub(link);
        }
        cached_link.cache(link, mask);
        cached_link.draw(link);
      }
    }
  }

  public void renderPolygons(RendererFace renderer_face,
    FaceManager face_manager, final Node n, int mask) {
    final ListOfIntegers polygons = n.list_of_polygons;

    final int total_number = face_manager.element.size();
    renderer_face.ensureCapacity(total_number);

    for (int l = polygons.size(); --l >= 0;) {
      final int face_number = polygons.retreive(l);
      final Face polygon = (Face) face_manager.element.elementAt(face_number);
      final CachedFace cached_face = renderer_face.array[face_number];
      int cnt = 0;
      final int npolygon = polygon.node.size();

      for (int i = npolygon; --i >= 0;) {
        final Node node = (Node) polygon.node.elementAt(i);
        if (n.pos.z >= node.pos.z) {
          cnt++;
        }
      }

      if (cnt == npolygon) {
        if (FrEnd.xor) {
          cached_face.draw(polygon);
        } else {
          cached_face.scrub(polygon);
        }
        cached_face.cache(polygon, mask);
        cached_face.draw(polygon);
      }
    }
  }
}