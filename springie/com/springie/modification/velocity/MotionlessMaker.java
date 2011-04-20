package com.springie.modification.velocity;

import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;

public class MotionlessMaker {

  public void freeze(NodeManager node_manager) {
    final int number_of_nodes = node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) node_manager.element.elementAt(counter);
      if (candidate.type.selected) {
        candidate.velocity.multiplyBy(0);
      }
    }
  }
}