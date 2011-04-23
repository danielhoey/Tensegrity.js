package com.springie.modification.resize;

import com.springie.elements.links.LinkManager;
import com.springie.elements.links.LinkType;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.elements.nodes.NodeType;
import com.springie.geometry.Point3D;
import com.springie.geometry.Vector3D;
import com.springie.gui.panels.preferences.PanelPreferencesIO;
import com.springie.render.Coords;

public class ScaleToFitScreen {
  public static void scale(NodeManager node_manager) {
    final Point3D max = new Point3D(Integer.MIN_VALUE, Integer.MIN_VALUE,
      Integer.MIN_VALUE);
    final Point3D min = new Point3D(Integer.MAX_VALUE, Integer.MAX_VALUE,
      Integer.MAX_VALUE);

    final int number_of_nodes = node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) node_manager.element.elementAt(counter);
      final int radius = candidate.type.radius;
      if (candidate.pos.x - radius < min.x) {
        min.x = candidate.pos.x - radius;
      }

      if (candidate.pos.y - radius < min.y) {
        min.y = candidate.pos.y - radius;
      }

      if (candidate.pos.z < min.z - radius) {
        min.z = candidate.pos.z - radius;
      }

      if (candidate.pos.x + radius > max.x) {
        max.x = candidate.pos.x + radius;
      }

      if (candidate.pos.y + radius > max.y) {
        max.y = candidate.pos.y + radius;
      }

      if (candidate.pos.z + radius > max.z) {
        max.z = candidate.pos.z + radius;
      }
    }

    // make sure these are set up...
    
    //FrEnd.main_canvas.setUpCoordsSize(); doesn't work initially...
    
    final int max_screen_size_x = Coords
      .getInternalFromPixelCoords(Coords.x_pixels);
    final int max_screen_size_y = Coords
      .getInternalFromPixelCoords(Coords.y_pixels);
    final int max_screen_size_z = Coords
      .getInternalFromPixelCoords(Coords.z_pixels);

    final Vector3D scale = new Vector3D(0, 0, 0);
    
    final int sf = 10 * PanelPreferencesIO.import_scale;

    scale.x = (max_screen_size_x * sf) / (max.x - min.x + 1);
    scale.y = (max_screen_size_y * sf) / (max.y - min.y + 1);
    scale.z = (max_screen_size_z * sf) / (max.z - min.z + 1);

    final int scale_factor = Math.min(Math.min(scale.x, scale.y), scale.z);

    scalePositions(node_manager, scale_factor);
    scaleVelocities(node_manager, scale_factor);
    scaleNodeRadii(node_manager, scale_factor);
    scaleCharges(node_manager, scale_factor);
    scaleLengths(node_manager.link_manager, scale_factor);
  }

  private static void scalePositions(NodeManager node_manager, int scale_factor) {
    final int number_of_nodes = node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) node_manager.element.elementAt(counter);
      candidate.pos.multiplyBy(scale_factor);
      candidate.pos.divideBy(1024);
      //Log.log("scalePositions: X: " + candidate.pos.x + " Y:" +
      // candidate.pos.y + " Z:" + candidate.pos.z);
    }
  }

  private static void scaleVelocities(NodeManager node_manager, int scale_factor) {
    final int number_of_nodes = node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) node_manager.element.elementAt(counter);
      candidate.velocity.multiplyBy(scale_factor);
      candidate.velocity.divideBy(1024);
    }
  }

  private static void scaleNodeRadii(NodeManager node_manager, int scale_factor) {
    final int number_of_node_types = node_manager.node_type_factory.array
      .size();

    for (int counter = number_of_node_types; --counter >= 0;) {
      final NodeType type = (NodeType) node_manager.node_type_factory.array
        .elementAt(counter);
      //Log.log("radius IN: X: " + type.radius);
      type.setSize((type.radius * scale_factor) >> 10);
      //Log.log("radius OUT: X: " + type.radius);
    }
  }

  private static void scaleCharges(NodeManager node_manager, int scale_factor) {
    final int number_of_node_types = node_manager.node_type_factory.array
      .size();

    for (int counter = number_of_node_types; --counter >= 0;) {
      final NodeType type = (NodeType) node_manager.node_type_factory.array
        .elementAt(counter);
      type.charge = (((type.charge * scale_factor) >> 10) * scale_factor) >> 10;
    }
  }

  private static void scaleLengths(LinkManager link_manager, int scale_factor) {
    final int max_size = link_manager.link_type_factory.array.size();

    for (int counter = max_size; --counter >= 0;) {
      final LinkType type = (LinkType) link_manager.link_type_factory.array.elementAt(counter);
      final int length = (type.length * scale_factor) >> 10;
      type.setLength(length);
      type.radius = (type.radius * scale_factor) >> 10;
    }
  }
}