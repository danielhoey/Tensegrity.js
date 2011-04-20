package com.springie.io.out;

import com.springie.elements.clazz.Clazz;
import com.springie.elements.links.Link;
import com.springie.elements.links.LinkManager;
import com.springie.elements.links.LinkType;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.elements.nodes.NodeType;
import com.springie.elements.nodes.NodeTypeFactory;

public class GarbageCollection {
  NodeManager node_manager;
  LinkManager link_manager;

  public GarbageCollection(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;
  }

  public void cleanUp() {
    removeNodeTypes(this.node_manager);
    removeLinkTypes();
    removeClazzes();
    
    this.node_manager.each_has_its_own_type = false;
    this.node_manager.link_manager.each_has_its_own_type = false;
    this.node_manager.face_manager.each_has_its_own_type = false;
  }

  private void removeClazzes() {
    final int number = this.node_manager.clazz_factory.array.size();
    for (int i = number; --i >= 0;) {
      final Clazz clazz = (Clazz) this.node_manager.clazz_factory.array.elementAt(i);
      final boolean any_n = new AreThereAny().nodes(clazz);
      final boolean any_l = new AreThereAny().links(clazz);
      final boolean any_p = new AreThereAny().polygons(clazz);

      if (!(any_n || any_l || any_p)) {
        this.node_manager.clazz_factory.array.remove(i);
      }
    }
  }

  private void removeLinkTypes() {
    final LinkManager link_manager = this.node_manager.link_manager;
    final int link_type_number = link_manager.link_type_factory.array.size();

    for (int lt = link_type_number; --lt >= 0;) {
      final LinkType link_type = (LinkType) link_manager.link_type_factory.array.elementAt(lt);
      final boolean any = findLinks(link_manager, link_type);
      if (!any) {
        link_manager.link_type_factory.array.remove(lt);
      }
    }
  }

  private boolean findLinks(LinkManager link_manager, LinkType link_type) {
    final int n_o_l = link_manager.element.size();
    for (int temp = n_o_l; --temp >= 0;) {
      final Link link = (Link) link_manager.element.elementAt(temp);
      if (link.type == link_type) {
        return true;
      }
    }
    return false;
  }

  private void removeNodeTypes(NodeManager node_manager) {
    final NodeTypeFactory node_type_factory = node_manager.node_type_factory;
    final int node_type_number = node_type_factory.array.size();
    for (int nt = node_type_number; --nt >= 0;) {
      final NodeType node_type = (NodeType) node_type_factory.array
        .elementAt(nt);
      final boolean any = findNodes(this.node_manager, node_type);

      if (!any) {
        node_type_factory.array.remove(nt);
      }
    }
  }

  private boolean findNodes(final NodeManager node_manager,
    final NodeType node_type) {
    final int number_of_nodes = node_manager.element.size();
    for (int n = number_of_nodes; --n >= 0;) {
      final Node node = (Node) node_manager.element.elementAt(n);
      if (node.type == node_type) {
        return true;
      }
    }
    return false;
  }
}