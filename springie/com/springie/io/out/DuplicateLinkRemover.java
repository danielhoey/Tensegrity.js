package com.springie.io.out;

import com.springie.elements.links.Link;
import com.springie.elements.links.LinkManager;
import com.springie.elements.nodes.NodeManager;

public class DuplicateLinkRemover {
  NodeManager node_manager;

  LinkManager link_manager;

  public DuplicateLinkRemover(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;
  }

  public void removeDuplicateLinks() {
    final LinkManager link_manager = this.link_manager;

    final int n = link_manager.element.size();

    for (int i = n; --i >= 0;) {
      final Link l1 = (Link) link_manager.element.elementAt(i);
      for (int j = i - 1; --j >= 0;) {
        final Link l2 = (Link) link_manager.element.elementAt(j);
        final boolean c1 = (l1.node1 == l2.node2) && (l1.node2 == l2.node1);
        final boolean c2 = (l1.node1 == l2.node1) && (l1.node2 == l2.node2);
        if (c1 || c2) {
          link_manager.killNumberedLink(i);
        }
      }
    }
  }
}