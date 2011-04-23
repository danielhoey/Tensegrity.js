package com.springie.modification.geodesic;

import com.springie.elements.clazz.Clazz;
import com.springie.elements.links.LinkManager;
import com.springie.elements.links.LinkType;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.modification.DomeMakingUtilities;
import com.springie.utilities.random.JUR;

public class GeodesicMaker {
  JUR rnd = new JUR();

  int colour_link_geodesic = 0xFF00FF00;

  NodeManager node_manager;

  LinkManager link_manager;

  DomeMakingUtilities utils = new DomeMakingUtilities();

  public GeodesicMaker(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;
  }

  public void connectNodesInFirstLayer() {
    final int number_of_nodes = this.node_manager.element.size();
    for (int cnt = 100000; --cnt >= 0;) {
      final int a1 = this.rnd.nextInt(number_of_nodes);
      final Node node_1 = (Node) this.node_manager.element.elementAt(a1);
      final Node node_2 = getCellInContactWith(node_1);
      if (node_2 != null) {
        final Node node_3 = getCellInContactWith(node_2);
        if (node_1 != node_3) {
          if (node_3 != null) {
            if (this.utils.isNodeTouchingNode(node_1, node_3)) {
              if (!this.link_manager.isThereALinkBetween(node_1, node_2)) {
                joinNodesInFirstLayer(node_1, node_2);
              }
              if (!this.link_manager.isThereALinkBetween(node_2, node_3)) {
                joinNodesInFirstLayer(node_2, node_3);
              }
              if (!this.link_manager.isThereALinkBetween(node_3, node_1)) {
                joinNodesInFirstLayer(node_3, node_1);
              }
            }
          }
        }
      }
    }
  }

  private void joinNodesInFirstLayer(Node node_1, Node node_2) {
    final int length = node_1.type.radius + node_2.type.radius;
    final int colour = this.colour_link_geodesic;
    final LinkType type = this.link_manager.link_type_factory.getNew(length, 50);
    final Clazz clazz = this.node_manager.clazz_factory.getNew(colour);
    this.link_manager.setLink(node_1, node_2, type, clazz);
  }
  
  private Node getCellInContactWith(Node node_1) {
    final int number_of_nodes = this.node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final int a2 = this.rnd.nextInt(number_of_nodes);
      final Node node_2 = (Node) this.node_manager.element.elementAt(a2);
      if (node_1 != node_2) {
        if (node_1.clazz.colour == node_2.clazz.colour) {
          if (this.utils.isNodeTouchingNode(node_1, node_2)) {
            return node_2;
          }
        }
      }
    }

    return null;
  }
}