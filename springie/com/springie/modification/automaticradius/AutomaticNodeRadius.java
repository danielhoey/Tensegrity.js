// This code has been placed into the public domain by its author
package com.springie.modification.automaticradius;

import com.springie.FrEnd;
import com.springie.elements.links.Link;
import com.springie.elements.lists.ListOfIntegers;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.modification.pre.PrepareToModifyNodeTypes;

public class AutomaticNodeRadius {
  NodeManager node_manager;

  public AutomaticNodeRadius(NodeManager node_manager) {
    this.node_manager = node_manager;
    prepare();
  }

  public void setInitially() {
    FrEnd.perform_selection.selectAll();
    set(1.5, true);
    FrEnd.perform_selection.deselectAll();
  }

  public void set(double ans_d, boolean only_set_if_zero) {
    final int number_of_nodes = this.node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) this.node_manager.element
        .elementAt(counter);
      if (candidate.type.selected) {
        final int largest_radius = getRadiusOfLargestLink(candidate);
        if ((!only_set_if_zero) || (candidate.type.radius == 0)) {
          candidate.type.setSize((int) (largest_radius * ans_d));
        }
      }
    }
  }

  private int getRadiusOfLargestLink(Node node) {
    final ListOfIntegers list_of_links = node.list_of_links;
    final int size = list_of_links.size();
    int max = Integer.MIN_VALUE;
    for (int counter = size; --counter >= 0;) {
      final int l_n = list_of_links.retreive(counter);
      final Link link = (Link) this.node_manager.link_manager.element
        .elementAt(l_n);
      int radius = link.type.radius;
      if (radius > max) {
        max = radius;
      }
    }

    if (max != Integer.MIN_VALUE) {
      return max;
    }

    return 0;
  }

  void prepare() {
    final PrepareToModifyNodeTypes prepare = new PrepareToModifyNodeTypes(
      this.node_manager);
    prepare.prepare();
  }
}