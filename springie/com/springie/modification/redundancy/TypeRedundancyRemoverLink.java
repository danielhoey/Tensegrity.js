package com.springie.modification.redundancy;

import java.util.Vector;

import com.springie.elements.links.Link;
import com.springie.elements.links.LinkType;
import com.springie.elements.nodes.NodeManager;

public class TypeRedundancyRemoverLink {
  NodeManager node_manager;

  public TypeRedundancyRemoverLink(NodeManager node_manager) {
    this.node_manager = node_manager;
  }

  public void removeRedundancy() {
    removeRedundancyInLinkTypes();
  }

  public void removeRedundancyInLinkTypes() {
    final Vector v = this.node_manager.link_manager.link_type_factory.array;
    final int size = v.size();
    for (int i = size; --i >= 0;) {
      final LinkType nt = (LinkType) v.elementAt(i);
      if (equalspreviousLinkType(v, i)) {
        final int first = getFirstLinkType(v, nt, i);
        replaceLinkTypeWithPrevious(v, i, first);
      }
    }

    this.node_manager.link_manager.each_has_its_own_type = false;
  }

  private void replaceLinkTypeWithPrevious(Vector v, int old, int nww) {
    final LinkType nt_old = (LinkType) v.elementAt(old);
    final LinkType nt_nww = (LinkType) v.elementAt(nww);
    final int n_o_l = this.node_manager.link_manager.element.size();
    for (int i = n_o_l; --i >= 0;) {
      final Link l = (Link) this.node_manager.link_manager.element.elementAt(i);
      if (l.type.equals(nt_old)) {
        l.type = nt_nww;
      }
    }

    v.remove(old);
  }

  private int getFirstLinkType(Vector v, LinkType t, int max) {
    for (int i = max; --i >= 0;) {
      if (t.equals(v.elementAt(i))) {
        return i;
      }
    }

    return -1;
  }

  private boolean equalspreviousLinkType(Vector v, int max) {
    final LinkType nt = (LinkType) v.elementAt(max);

    return getFirstLinkType(v, nt, max) > 0;
  }
}