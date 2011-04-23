package com.springie.modification.flags;

import com.springie.elements.links.Link;
import com.springie.elements.links.LinkManager;
import com.springie.elements.nodes.NodeManager;
import com.springie.modification.post.PostModification;
import com.springie.modification.pre.PrepareToModifyLinkTypes;

public class FlagControllerDisabled {
  NodeManager node_manager;
  LinkManager link_manager;

  public FlagControllerDisabled(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;
    prepare();
  }

  public void disable(boolean disabled) {
    disableLinks(disabled);
  }

  private void disableLinks(boolean disabled) {
    final int n_o_l = this.link_manager.element.size();
    for (int temp = n_o_l; --temp >= 0;) {
      final Link l = (Link) this.link_manager.element.elementAt(temp);
      if (l.type.selected) {
        l.type.disabled = disabled;
      }
    }

    new PostModification(this.node_manager).cleanup();
  }

  void prepare() {
    final PrepareToModifyLinkTypes prepare_l = new PrepareToModifyLinkTypes(
      this.link_manager);
    prepare_l.prepare();
  }
}

