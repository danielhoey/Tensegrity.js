package com.springie.modification.translation;

import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.geometry.Point3D;
import com.springie.geometry.Vector3D;
import com.springie.metrics.BoundingBox;
import com.springie.render.Coords;

public class CentreOnScreen {
  public static void centre(NodeManager node_manager) {
    final BoundingBox bb = new BoundingBox();
    bb.find(node_manager);

    final Point3D max = bb.min;
    final Point3D min = bb.max;

    final int max_screen_size_x = Coords
      .getInternalFromPixelCoords(Coords.x_pixels);
    final int max_screen_size_y = Coords
      .getInternalFromPixelCoords(Coords.y_pixels);
    final int max_screen_size_z = Coords
      .getInternalFromPixelCoords(Coords.z_pixels);

    final Vector3D offset_actual = new Vector3D(0, 0, 0);

    offset_actual.x = (max_screen_size_x - max.x - min.x) >> 1;
    offset_actual.y = (max_screen_size_y - max.y - min.y) >> 1;
    offset_actual.z = (max_screen_size_z - max.z - min.z) >> 1;

    final int number_of_nodes = node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) node_manager.element.elementAt(counter);
      candidate.pos.addTuple3D(offset_actual);
    }
  }

  public static void moveTowardsCentre(NodeManager node_manager) {
    final Point3D max = new Point3D(0, 0, 0);
    final Point3D min = new Point3D(Integer.MAX_VALUE, Integer.MAX_VALUE,
      Integer.MAX_VALUE);

    final int number_of_nodes = node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) node_manager.element.elementAt(counter);
      if (candidate.pos.x < min.x) {
        min.x = candidate.pos.x;
      }

      if (candidate.pos.y < min.y) {
        min.y = candidate.pos.y;
      }

      if (candidate.pos.z < min.z) {
        min.z = candidate.pos.z;
      }

      if (candidate.pos.x > max.x) {
        max.x = candidate.pos.x;
      }

      if (candidate.pos.y > max.y) {
        max.y = candidate.pos.y;
      }

      if (candidate.pos.z > max.z) {
        max.z = candidate.pos.z;
      }
    }

    final int max_screen_size_x = Coords
      .getInternalFromPixelCoords(Coords.x_pixels);
    final int max_screen_size_y = Coords
      .getInternalFromPixelCoords(Coords.y_pixels);
    final int max_screen_size_z = Coords
      .getInternalFromPixelCoords(Coords.z_pixels);

    final Vector3D offset_actual = new Vector3D(0, 0, 0);

    offset_actual.x = (max_screen_size_x - max.x - min.x) >> 7;
    offset_actual.y = (max_screen_size_y - max.y - min.y) >> 7;
    offset_actual.z = (max_screen_size_z - max.z - min.z) >> 7;
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) node_manager.element.elementAt(counter);
      candidate.pos.addTuple3D(offset_actual);
    }
  }
}