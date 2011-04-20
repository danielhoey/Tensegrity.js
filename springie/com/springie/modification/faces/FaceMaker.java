package com.springie.modification.faces;

import java.util.Vector;

import com.springie.elements.clazz.Clazz;
import com.springie.elements.faces.FaceManager;
import com.springie.elements.faces.FaceType;
import com.springie.elements.links.LinkManager;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.modification.DomeMakingUtilities;
import com.springie.utilities.random.JUR;

public class FaceMaker {
  JUR rnd = new JUR();
  NodeManager node_manager;

  LinkManager link_manager;

  DomeMakingUtilities utils = new DomeMakingUtilities();

  public FaceMaker(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;
  }
  
  public void addPolygons(int number) {
    for (int cnt = 3000; --cnt >= 0;) {
      tryToMakePolygon(number);
    }
  }

  public void tryToMakePolygon(int number) {
    final int number_of_nodes = this.node_manager.element.size();
    final Vector node = new Vector();

    final int a0 = this.rnd.nextInt(number_of_nodes);
    final Node n0 = (Node) this.node_manager.element.elementAt(a0);

    if (!n0.type.selected) {
      return;
    }

    node.addElement(n0);

    Node last_node = n0;
    for (int cnt = 1; cnt < number; cnt++) {
      final Node can = this.utils.getRandomCellLinkedTo(this.node_manager,
        last_node);

      if (nodeIsOnList(can, node)) {
        return;
      }

      node.addElement(can);
      last_node = can;
    }

    if (!this.link_manager.isThereALinkBetween(n0, last_node)) {
      return;
    }

    final FaceManager polygon_manager = this.node_manager.face_manager;

    if (polygon_manager.isThereAPolygonWithNodes(node)) {
      return;
    }

    // make it...
    final FaceType type = polygon_manager.face_type_factory.getNew();
    
    final Clazz clazz = this.node_manager.clazz_factory.getNew(0x80FFFF80);

    polygon_manager.setPolygon(node, type, clazz);
  }
  
  private boolean nodeIsOnList(Node node_to_check, Vector node_list) {
    final int number_of_nodes = node_list.size();
    for (int cnt = number_of_nodes; --cnt >= 0;) {
      final Node candidate = (Node) node_list.elementAt(cnt);
      if (candidate == node_to_check) {
        return true;
      }
    }

    return false;
  }
}
