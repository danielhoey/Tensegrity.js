package com.springie.modification.pre;

import com.springie.elements.clazz.Clazz;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;

public class PrepareToModifyNodeClazzes {
  NodeManager node_manager;

  public PrepareToModifyNodeClazzes(NodeManager node_manager) {
    this.node_manager = node_manager;
  }

  public boolean weNeedToMakeNewClazzes() {
    if (this.node_manager.each_has_its_own_clazz) {
      return false;
    }

    if (thereIsANodeClazzClash()) {
      return true;
    }

    return false;
  }

  private boolean thereIsANodeClazzClash() {
    final int number_of_nodes = this.node_manager.element.size();
    for (int c1 = number_of_nodes; --c1 >= 0;) {
      for (int c2 = c1; --c2 >= 0;) {
        final Node n1 = (Node) this.node_manager.element.elementAt(c1);
        final Node n2 = (Node) this.node_manager.element.elementAt(c2);
        if (n1.clazz == n2.clazz) {
          return true;
        }
      }
    }
    return false;
  }

  public void prepare() {
    if (weNeedToMakeNewClazzes()) {
      makeNewClazzes();
    }

    this.node_manager.each_has_its_own_clazz = true;
  }

  public void makeNewClazzes() {
    final int number_of_nodes = this.node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) this.node_manager.element.elementAt(counter);
      Clazz clazz = this.node_manager.clazz_factory.getNew(candidate.clazz.colour);
      candidate.clazz = clazz;
    }
  }
}