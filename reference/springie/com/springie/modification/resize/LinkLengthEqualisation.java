package com.springie.modification.resize;

import com.springie.elements.links.Link;
import com.springie.elements.links.LinkManager;
import com.springie.elements.nodes.NodeManager;
import com.springie.modification.pre.PrepareToModifyLinkTypes;

public class LinkLengthEqualisation {
  NodeManager node_manager;

  LinkManager link_manager;

  public LinkLengthEqualisation(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;
    prepare();
  }

  public void equalise() {
    int number = 0;
    long total_upper_length_limit = 0;
    final int n_o_l = this.link_manager.element.size();
    for (int temp = n_o_l; --temp >= 0;) {
      final Link l = (Link) this.link_manager.element.elementAt(temp);

      if (l.type.selected) {
        number++;
        total_upper_length_limit += l.type.length;
      }
    }

    if (number == 0) {
      number = 1;
    }

    final int average_upper_length_limit = (int) (total_upper_length_limit / number);

    for (int temp = n_o_l; --temp >= 0;) {
      final Link l = (Link) this.link_manager.element.elementAt(temp);
      if (l.type.selected) {
        l.type.setLength(average_upper_length_limit);
      }
    }
  }

  void prepare() {
    final PrepareToModifyLinkTypes prepare = new PrepareToModifyLinkTypes(
      this.node_manager.link_manager);
    prepare.prepare();
  }
}
