package com.springie.modification.resize;

import com.springie.elements.links.Link;
import com.springie.elements.links.LinkManager;
import com.springie.elements.nodes.NodeManager;
import com.springie.modification.pre.PrepareToModifyLinkTypes;

public class LinkResetter {
  NodeManager node_manager;
  LinkManager link_manager;

  public LinkResetter(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;
    prepare();
  }

  public void reset() {
    final int n_o_l = this.link_manager.element.size();
    for (int temp = n_o_l; --temp >= 0;) {
      final Link l = (Link) this.link_manager.element.elementAt(temp);
      if (l.type.selected) { 
        final int length = l.getActualLength();
        l.type.setLength(length);
      }
    }
  }

  void prepare() {
    final PrepareToModifyLinkTypes prepare = new PrepareToModifyLinkTypes(this.node_manager.link_manager);
    prepare.prepare();
  }
}
