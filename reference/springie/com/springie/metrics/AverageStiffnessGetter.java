package com.springie.metrics;

import com.springie.elements.links.Link;
import com.springie.elements.links.LinkManager;
import com.springie.elements.nodes.NodeManager;

public class AverageStiffnessGetter {
  NodeManager node_manager;

  LinkManager link_manager;

  public AverageStiffnessGetter(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;
  }

  public int getAverage() {
    final int number = this.link_manager.element.size();
    int count = 0;
    long total = 0;
    for (int counter = number; --counter >= 0;) {
      final Link candidate = (Link) this.link_manager.element.elementAt(counter);
      if (candidate.type.selected) {
        total += candidate.type.damping;
        count++;
      }
    }

    if (count == 0) {
      return 0;
    }

    return (int) (total / count);
  }
}