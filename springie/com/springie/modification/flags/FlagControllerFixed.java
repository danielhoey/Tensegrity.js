package com.springie.modification.flags;

import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.modification.post.PostModification;
import com.springie.modification.pre.PrepareToModifyNodeTypes;

public class FlagControllerFixed {
  NodeManager node_manager;

  public FlagControllerFixed(NodeManager node_manager) {
    this.node_manager = node_manager;
    prepare();
  }
  public void fix(boolean fixed) {
    fixNodes(fixed);
  }
  
  private void fixNodes(boolean fixed) {
    final int number_of_nodes = this.node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) this.node_manager.element.elementAt(counter);
      if (candidate.type.selected) {
        //candidate.type.setSize((candidate.type.radius * 9) >> 3);
        candidate.type.fixed = fixed;
      }
    }

    new PostModification(this.node_manager).cleanup();
  }

  void prepare() {
    final PrepareToModifyNodeTypes prepare_n = new PrepareToModifyNodeTypes(this.node_manager);
    prepare_n.prepare();
  }
}

