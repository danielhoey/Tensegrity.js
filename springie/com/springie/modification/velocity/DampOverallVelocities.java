package com.springie.modification.velocity;

import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.geometry.Vector3D;

public class DampOverallVelocities {
  public static void damp(NodeManager node_manager) {
    final Vector3D velocity_total = new Vector3D(0,0,0);
    final int number_of_nodes = node_manager.element.size();
    int sf = number_of_nodes << 6;
    if (sf < 1) {
      sf = 1;
    }

    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) node_manager.element.elementAt(counter);
      velocity_total.addTuple3D(candidate.velocity);
    }

    velocity_total.divideBy(sf);

    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) node_manager.element.elementAt(counter);
      candidate.velocity.subtractTuple3D(velocity_total);
    }
  }
}
