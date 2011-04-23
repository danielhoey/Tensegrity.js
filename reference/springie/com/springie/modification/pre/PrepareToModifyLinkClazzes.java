package com.springie.modification.pre;

import com.springie.elements.clazz.Clazz;
import com.springie.elements.links.Link;
import com.springie.elements.links.LinkManager;
import com.springie.elements.nodes.NodeManager;

public class PrepareToModifyLinkClazzes {
  NodeManager node_manager;

  LinkManager link_manager;

  public PrepareToModifyLinkClazzes(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;
  }

  public boolean weNeedToMakeNewLinkClazzes() {
    if (this.link_manager.each_has_its_own_clazz) {
      return false;
    }

    if (thereIsALinkClazzClash()) {
      return true;
    }

    return false;
  }

  private boolean thereIsALinkClazzClash() {
    final int number = this.link_manager.element.size();
    for (int c1 = number; --c1 >= 0;) {
      for (int c2 = c1; --c2 >= 0;) {
        final Link l1 = (Link) this.link_manager.element.elementAt(c1);
        final Link l2 = (Link) this.link_manager.element.elementAt(c2);
        if (l1.clazz == l2.clazz) {
          return true;
        }
      }
    }
    return false;
  }

  public void prepare() {
    if (weNeedToMakeNewLinkClazzes()) {
      makeNewLinkClazzes();
    }

    this.link_manager.each_has_its_own_clazz = true;
  }

  public void makeNewLinkClazzes() {
    final int number = this.link_manager.element.size();
    for (int counter = number; --counter >= 0;) {
      final Link candidate = (Link) this.link_manager.element
        .elementAt(counter);
      Clazz clazz = this.node_manager.clazz_factory
        .getNew(candidate.clazz.colour);
      candidate.clazz = clazz;
    }
  }
}