// This code has been placed into the public domain by its author
package com.springie.io.in;

import com.springie.elements.links.LinkManager;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.modification.pre.PrepareToModifyNodeClazzes;
import com.springie.presets.ColourFactory;

public class AutomaticNodeColourer {
  NodeManager node_manager;

  LinkManager link_manager;

  public AutomaticNodeColourer(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;

    prepare();
  }

  void prepare() {
    final PrepareToModifyNodeClazzes prepare_mn = new PrepareToModifyNodeClazzes(
      this.node_manager);
    prepare_mn.prepare();
  }

  public void execute() {
    final int number_of_nodes = this.node_manager.element.size();
    final int[] colours = new ColourFactory(52746)
      .getColourArray(number_of_nodes);

    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) this.node_manager.element
        .elementAt(counter);
      if (candidate.clazz.colour == 0) {
        final int number_of_links = candidate.list_of_links.size();
        candidate.clazz.colour = colours[number_of_links];
      }
    }
  }
}