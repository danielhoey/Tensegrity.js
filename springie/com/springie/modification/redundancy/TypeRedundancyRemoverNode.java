package com.springie.modification.redundancy;

import java.util.Vector;

import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.elements.nodes.NodeType;

public class TypeRedundancyRemoverNode {
  NodeManager node_manager;

  public TypeRedundancyRemoverNode(NodeManager node_manager) {
    this.node_manager = node_manager;
  }

  public void removeRedundancy() {
    removeRedundancyInNodeTypes();
  }

  public void removeRedundancyInNodeTypes() {
    final Vector v = this.node_manager.node_type_factory.array;
    final int size = v.size();
    for (int i = size; --i >= 0;) {
      final NodeType nt = (NodeType) v.elementAt(i);
      if (equalspreviousNodeType(v, i)) {
        final int first = getFirstNodeType(v, nt, i);
        replaceNodeTypeWithPrevious(v, i, first);
      }
    }

    this.node_manager.each_has_its_own_type = false;
  }

  private void replaceNodeTypeWithPrevious(Vector v, int old, int nww) {
    final NodeType nt_old = (NodeType) v.elementAt(old);
    final NodeType nt_nww = (NodeType) v.elementAt(nww);
    final int n_o_n = this.node_manager.element.size();
    for (int i = n_o_n; --i >= 0;) {
      final Node n = (Node) this.node_manager.element.elementAt(i);
      if (n.type.equals(nt_old)) {
        n.type = nt_nww;
      }
    }

    v.remove(old);
  }

  private int getFirstNodeType(Vector v, NodeType nt, int max) {
    for (int i = max; --i >= 0;) {
      if (nt.equals(v.elementAt(i))) {
        return i;
      }
    }

    return -1;
  }

  private boolean equalspreviousNodeType(Vector v, int max) {
    final NodeType nt = (NodeType) v.elementAt(max);

    return getFirstNodeType(v, nt, max) > 0;
  }
}