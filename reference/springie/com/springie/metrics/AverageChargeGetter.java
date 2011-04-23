package com.springie.metrics;

import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;

public class AverageChargeGetter {
  NodeManager node_manager;

  public AverageChargeGetter(NodeManager node_manager) {
    this.node_manager = node_manager;
  }

  public int getAverage() {
    final int number = this.node_manager.element.size();
    int count = 0;
    long total = 0;
    for (int counter = number; --counter >= 0;) {
      final Node candidate = (Node) this.node_manager.element.elementAt(counter);
      if (candidate.type.selected) {
        total += candidate.type.charge;
        count++;
      }
    }

    if (count == 0) {
      return 0;
    }

    return (int) (total / count);
  }
}