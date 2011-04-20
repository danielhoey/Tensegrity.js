package com.springie.modification.stellation;

import java.util.Vector;

import com.springie.elements.clazz.Clazz;
import com.springie.elements.faces.Face;
import com.springie.elements.faces.FaceManager;
import com.springie.elements.links.LinkManager;
import com.springie.elements.links.LinkType;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.elements.nodes.NodeType;
import com.springie.geometry.Point3D;
import com.springie.geometry.Vector3D;
import com.springie.modification.DomeMakingUtilities;
import com.springie.utilities.random.JUR;

public class StellationMaker {
  int colour_link_between_layers = 0xFFC0C0FF;

  JUR rnd = new JUR();

  NodeManager node_manager;

  LinkManager link_manager;

  private FaceManager face_manager;

  Point3D centre;

  float factor;

  int colour;

  DomeMakingUtilities utils = new DomeMakingUtilities();

  public StellationMaker(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;
    this.face_manager = node_manager.face_manager;
  }

  public void addStellations(float factor, int colour) {
    this.factor = factor;
    this.colour = colour;
    this.centre = this.node_manager.getCentre();

    for (int count = 0; count < this.face_manager.element.size(); count++) {
      final Face face = (Face) this.face_manager.element.elementAt(count);
      if (face.type.selected) {
        tryToMakeStellation(face);
      }
    }
  }

  public void tryToMakeStellation(Face face) {
    //final int number_of_nodes = this.node_manager.element.size();
    final Vector node_list = face.node;
    //Vector();

    //    final int a0 = this.rnd.nextInt(number_of_nodes);
    //    final Node n0 = (Node) this.node_manager.element.elementAt(a0);
    //
    //    if (!n0.type.selected) {
    //      return;
    //    }
    //
    //    node_list.addElement(n0);
    //
    //    Node last_node = n0;
    //    for (int cnt = 1; cnt < number; cnt++) {
    //      Node can;
    //      do {
    //        can = this.utils.getRandomSelectedCellLinkedTo(this.node_manager,
    //          last_node);
    //      } while (nodeIsOnList(can, node_list));
    //
    //      node_list.addElement(can);
    //      last_node = can;
    //    }

    //    if (!this.link_manager.isThereALinkBetween(n0, last_node)) {
    //      return;
    //    }

    //if (!thereIsAlreadyANodeConnectedToEachOfTheseNodes(node_list)) {
    final Point3D c_of_g = centreOfGravityOfNodeList(node_list);

    final Vector3D vec = new Vector3D(c_of_g, this.centre);
    vec.multiplyBy(this.factor);
    final Point3D proposed = (Point3D) this.centre.clone();
    proposed.addTuple3D(vec);

    final NodeType type = this.node_manager.node_type_factory.getNew();
    type.radius = 5000;
    type.log_mass = 4;

    final Clazz clazz = this.node_manager.clazz_factory.getNew(this.colour);

    final Node added = this.node_manager.addNewAgent(
      (Point3D) proposed.clone(), clazz, type);

    final int number_of_nodes_in_face = face.node.size();
    for (int idx = 0; idx < number_of_nodes_in_face; idx++) {
      final Node n = (Node) node_list.elementAt(idx);
      final int length = this.node_manager.distanceBetween(n, added);
      joinNodesBetweenLayers(n, added, length);
    }
  }

  private Point3D centreOfGravityOfNodeList(final Vector node) {
    final int number = node.size();
    final Point3D c_of_g = new Point3D(0, 0, 0);
    for (int idx = 0; idx < number; idx++) {
      final Node n = (Node) node.elementAt(idx);
      c_of_g.addTuple3D(n.pos);
    }

    c_of_g.divideBy(number);
    return c_of_g;
  }

  //  private boolean thereIsAlreadyANodeConnectedToEachOfTheseNodes(Vector list,
  //    int colour) {
  //    final int number_of_nodes = this.node_manager.element.size();
  //    final int number_in_list = list.size();
  //    for (int i = 0; i < number_of_nodes; i++) {
  //      final Node n = (Node) this.node_manager.element.elementAt(i);
  //      if (n.clazz.colour == colour) {
  //        final int count = countNumberOfNodesLinked(list, n);
  //
  //        if (count == number_in_list) {
  //          return true;
  //        }
  //      }
  //    }
  //
  //    return false;
  //  }

  //  private boolean thereIsAlreadyANodeConnectedToEachOfTheseNodes(Vector list)
  // {
  //    final int number_of_nodes = this.node_manager.element.size();
  //    final int number_in_list = list.size();
  //    for (int i = 0; i < number_of_nodes; i++) {
  //      final Node n = (Node) this.node_manager.element.elementAt(i);
  //      final int count = countNumberOfNodesLinked(list, n);
  //
  //      if (count == number_in_list) {
  //        return true;
  //      }
  //    }
  //
  //    return false;
  //  }

  //  private int countNumberOfNodesLinked(Vector list, final Node n) {
  //    final int number_in_list = list.size();
  //    int count = 0;
  //    for (int idx = 0; idx < number_in_list; idx++) {
  //      final Node test = (Node) list.elementAt(idx);
  //
  //      if (this.link_manager.isThereALinkBetween(test, n)) {
  //        count++;
  //      }
  //    }
  //    return count;
  //  }

  private void joinNodesBetweenLayers(Node node_1, Node node_2, int length) {
    final LinkType type = this.link_manager.link_type_factory
      .getNew(length, 30);
    final Clazz clazz = this.node_manager.clazz_factory
      .getNew(this.colour_link_between_layers);
    this.link_manager.setLink(node_1, node_2, type, clazz);
  }

  //  private boolean nodeIsOnList(Node node_to_check, Vector node_list) {
  //    final int number_of_nodes = node_list.size();
  //    for (int cnt = number_of_nodes; --cnt >= 0;) {
  //      final Node candidate = (Node) node_list.elementAt(cnt);
  //      if (candidate == node_to_check) {
  //        return true;
  //      }
  //    }
  //
  //    return false;
  //  }
}