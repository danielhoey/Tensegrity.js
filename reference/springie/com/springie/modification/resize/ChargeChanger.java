package com.springie.modification.resize;

import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.modification.post.PostModification;
import com.springie.modification.pre.PrepareToModifyNodeTypes;

public class ChargeChanger {
  NodeManager node_manager;

  public ChargeChanger(NodeManager node_manager) {
    this.node_manager = node_manager;
    prepare();
  }

  public void scaleBy(float scale_factor) {
    final int number_of_nodes = this.node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) this.node_manager.element.elementAt(counter);
      if (candidate.type.selected) {
        candidate.type.charge = (int) (candidate.type.charge * scale_factor);
      }
    }
    
    new PostModification(this.node_manager).cleanup();
  }

  void prepare() {
    final PrepareToModifyNodeTypes prepare = new PrepareToModifyNodeTypes(
      this.node_manager);
    prepare.prepare();
  }
}