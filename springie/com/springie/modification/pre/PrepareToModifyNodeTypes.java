package com.springie.modification.pre;

import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.elements.nodes.NodeType;

public class PrepareToModifyNodeTypes {
  NodeManager node_manager;

  public PrepareToModifyNodeTypes(NodeManager node_manager) {
    this.node_manager = node_manager;
  }

  public boolean weNeedToMakeNewTypes() {
    if (this.node_manager.each_has_its_own_type) {
      return false;
    }
    final int number_of_nodes = this.node_manager.element.size();
    for (int c1 = number_of_nodes; --c1 >= 0;) {
      for (int c2 = c1; --c2 >= 0;) {
        final Node n1 = (Node) this.node_manager.element.elementAt(c1);
        final Node n2 = (Node) this.node_manager.element.elementAt(c2);
        if (n1.type == n2.type) {
          return true;
        }
      }
    }
    return false;
  }

  public void prepare() {
    if (weNeedToMakeNewTypes()) {
      makeNewTypes();
    }

    this.node_manager.each_has_its_own_type = true;
  }

  public void makeNewTypes() {
    final int number_of_nodes = this.node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) this.node_manager.element.elementAt(counter);
      NodeType nt = this.node_manager.node_type_factory.getNew();
      nt.makeEqualTo(candidate.type);
      candidate.type = nt;
    }
  }
}
