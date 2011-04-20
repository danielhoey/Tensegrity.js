package com.springie.modification.flags;

import com.springie.elements.faces.Face;
import com.springie.elements.faces.FaceManager;
import com.springie.elements.links.Link;
import com.springie.elements.links.LinkManager;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.modification.post.PostModification;
import com.springie.modification.pre.PrepareToModifyLinkTypes;
import com.springie.modification.pre.PrepareToModifyNodeTypes;

public class FlagControllerHidden {
  NodeManager node_manager;

  LinkManager link_manager;

  FaceManager face_manager;

  public FlagControllerHidden(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;
    this.face_manager = node_manager.face_manager;
    prepare();
  }

  public void hide(boolean hidden) {
    hideNodes(hidden);
    hideLinks(hidden);
    hideFaces(hidden);
    new PostModification(this.node_manager).cleanup();
  }

  private void hideNodes(boolean hidden) {
    final int number_of_nodes = this.node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) this.node_manager.element
        .elementAt(counter);
      if (candidate.type.selected) {
        //candidate.type.setSize((candidate.type.radius * 9) >> 3);
        candidate.type.hidden = hidden;
      }
    }
  }

  private void hideLinks(boolean hidden) {
    final int n_o_l = this.link_manager.element.size();
    for (int temp = n_o_l; --temp >= 0;) {
      final Link l = (Link) this.link_manager.element.elementAt(temp);
      if (l.type.selected) {
        l.type.hidden = hidden;
      }
    }
  }

  private void hideFaces(boolean hidden) {
    final int n_o_l = this.face_manager.element.size();
    for (int temp = n_o_l; --temp >= 0;) {
      final Face f = (Face) this.face_manager.element.elementAt(temp);
      if (f.type.selected) {
        f.type.hidden = hidden;
      }
    }
  }

  void prepare() {
    final PrepareToModifyNodeTypes prepare_n = new PrepareToModifyNodeTypes(
      this.node_manager);
    prepare_n.prepare();
    final PrepareToModifyLinkTypes prepare_l = new PrepareToModifyLinkTypes(
      this.node_manager.link_manager);
    prepare_l.prepare();
  }
}

