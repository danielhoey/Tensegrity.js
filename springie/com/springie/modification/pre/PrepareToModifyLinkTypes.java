package com.springie.modification.pre;

import com.springie.elements.links.Link;
import com.springie.elements.links.LinkManager;
import com.springie.elements.links.LinkType;

public class PrepareToModifyLinkTypes {
  LinkManager link_manager;

  public PrepareToModifyLinkTypes(LinkManager link_manager) {
    this.link_manager = link_manager;
  }

  public boolean weNeedToMakeNewTypes() {
    if (this.link_manager.each_has_its_own_type) {
      return false;
    }

    final int number_of_links = this.link_manager.element.size();
    for (int c1 = number_of_links; --c1 >= 0;) {
      for (int c2 = c1; --c2 >= 0;) {
        final Link l1 = (Link) this.link_manager.element.elementAt(c1);
        final Link l2 = (Link) this.link_manager.element.elementAt(c2);
        if (l1.type == l2.type) {
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

    this.link_manager.each_has_its_own_type = true;
  }

  public void makeNewTypes() {
    final int number_of_links = this.link_manager.element.size();
    for (int counter = number_of_links; --counter >= 0;) {
      final Link candidate = (Link) this.link_manager.element.elementAt(counter);
      LinkType lt = this.link_manager.link_type_factory.getNew();
      lt.makeEqualTo(candidate.type);
      candidate.type = lt;
    }
  }
}