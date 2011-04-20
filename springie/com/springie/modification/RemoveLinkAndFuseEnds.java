// This code has been placed into the public domain by its author
package com.springie.modification;

import com.springie.FrEnd;
import com.springie.elements.links.Link;
import com.springie.elements.links.LinkManager;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.modification.post.PostModification;
import com.springie.modification.pre.PrepareToModifyLinkTypes;
import com.springie.modification.pre.PrepareToModifyNodeTypes;

public class RemoveLinkAndFuseEnds {
  NodeManager node_manager;

  LinkManager link_manager;
  
  PostModification post_modification;

  public RemoveLinkAndFuseEnds(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;
    this.post_modification = new PostModification(node_manager);
    
    prepare();
  }

  public void action() {
    final int n_o_l = this.link_manager.element.size();
    for (int temp = n_o_l; --temp >= 0;) {
      final Link l = (Link) this.link_manager.element.elementAt(temp);
      if (l.type.selected) {
        deleteAndFuseEnds(l);
      }
    }
    
    FrEnd.postCleanup();
  }

  private void deleteAndFuseEnds(Link l) {
    final Node n1 = l.node1;
    final Node n2 = l.node2;
    this.link_manager.killSpecifiedLink(l);

    //final ListOfIntegers list_of_links = n2.list_of_links;

//    final int n_o_l = list_of_links.size();
//    for (int temp = n_o_l; --temp >= 0;) {
//      final int l_n = list_of_links.retreive(temp);
//      final Link lk = (Link) this.link_manager.element.elementAt(l_n);
//      if (lk.node1 == n2) {
//        this.link_manager.setLink(lk.node2, n1, lk.type, lk.clazz);
//      } else if (lk.node2 == n2) {
//        this.link_manager.setLink(lk.node1, n1, lk.type, lk.clazz);
//      }
//    }
    //final ListOfIntegers list_of_links = n2.list_of_links;

    rearrangeLinks(n1, n2);

    this.node_manager.killThisNode(n2);
  }

  private void rearrangeLinks(final Node n1, final Node n2) {
    final int n_o_l = this.link_manager.element.size();
    for (int temp = n_o_l; --temp >= 0;) {
      final Link lk = (Link) this.link_manager.element.elementAt(temp);
      if (lk.node1 == n2) {
        makeLink(n1, lk.node2, lk);
      } else if (lk.node2 == n2) {
        makeLink(n1, lk.node1, lk);
      }
    }
  }

  private void makeLink(final Node n1, final Node n2, final Link lk) {
    this.link_manager.setLink(n1, n2, lk.type, lk.clazz);
  }

  void prepare() {
    final PrepareToModifyLinkTypes prepare_l = new PrepareToModifyLinkTypes(
      this.node_manager.link_manager);
    prepare_l.prepare();

    final PrepareToModifyNodeTypes prepare_n = new PrepareToModifyNodeTypes(
      this.node_manager);
    prepare_n.prepare();
  }
}