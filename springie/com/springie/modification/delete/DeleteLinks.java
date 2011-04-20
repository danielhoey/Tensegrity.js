package com.springie.modification.delete;

import com.springie.FrEnd;
import com.springie.collisions.BinGrid;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.modification.pre.PrepareToModifyLinkTypes;
import com.springie.modification.pre.PrepareToModifyNodeTypes;

public class DeleteLinks {
  NodeManager node_manager;

  public DeleteLinks(NodeManager node_manager) {
    this.node_manager = node_manager;
    
    prepare();
  }

  public void delete() {
    final int number_of_nodes = this.node_manager.element.size();

    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) this.node_manager.element.elementAt(counter);
      if (candidate.type.selected) {
        FrEnd.killAllLinks(candidate);
      }
    }
    
    BinGrid.repaint_all_objects = true;
  }
  
  public void prepare() {
    final PrepareToModifyNodeTypes prepare_n = new PrepareToModifyNodeTypes(this.node_manager);
    prepare_n.prepare();
    final PrepareToModifyLinkTypes prepare_l = new PrepareToModifyLinkTypes(this.node_manager.link_manager);
    prepare_l.prepare();
  }
}
