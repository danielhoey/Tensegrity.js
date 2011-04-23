package com.springie.modification.resize;

import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.modification.pre.PrepareToModifyNodeTypes;

public class NodeSizeChanger {
  NodeManager node_manager;

  public NodeSizeChanger(NodeManager node_manager) {
    this.node_manager = node_manager;
    prepare();
  }

  public void resize(float scale_factor) {
    final int number_of_nodes = this.node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) this.node_manager.element.elementAt(counter);
      if (candidate.type.selected) {
        candidate.type.setSize((int)(candidate.type.radius * scale_factor));
      }
    }
  }

  void prepare() {
    final PrepareToModifyNodeTypes prepare = new PrepareToModifyNodeTypes(this.node_manager);
    prepare.prepare();
  }
}
