// This code has been placed into the public domain by its author
package com.springie.modification.stellation;

import com.springie.elements.clazz.Clazz;
import com.springie.elements.links.LinkManager;
import com.springie.elements.links.LinkType;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.elements.nodes.NodeType;
import com.springie.geometry.Point3D;
import com.springie.modification.post.PostModification;
import com.springie.modification.pre.PrepareToModifyLinkTypes;
import com.springie.modification.pre.PrepareToModifyNodeTypes;

public class CentralHubCreator {
  NodeManager node_manager;

  LinkManager link_manager;

  PostModification post_modification;

  int colour = 0xFF85CFAF;

  int colour_link = 0xFFCEA589;

  public CentralHubCreator(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;
    this.post_modification = new PostModification(node_manager);

    prepare();
  }

  public void create() {
    final Point3D centre = new Point3D(0, 0, 0);
    int count = 0;
    int radius = 0;

    final int number_of_nodes = this.node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) this.node_manager.element
        .elementAt(counter);
      if (candidate.type.selected) {
        centre.addTuple3D(candidate.pos);
        count++;
        radius += candidate.type.radius;
      }
    }

    centre.divideBy(count);
    radius /= count;

    final NodeType type = this.node_manager.node_type_factory.getNew();
    type.radius = radius;
    type.log_mass = 4;

    final Clazz clazz = this.node_manager.clazz_factory.getNew(this.colour);

    final Node added = this.node_manager.addNewAgent((Point3D) centre.clone(),
      clazz, type);

    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) this.node_manager.element
        .elementAt(counter);
      if (candidate.type.selected) {
        final int length = this.node_manager.distanceBetween(candidate, added);
        joinNodesBetweenLayers(candidate, added, length);
      }
    }
  }

  private void joinNodesBetweenLayers(Node node_1, Node node_2, int length) {
    final LinkType type = this.link_manager.link_type_factory
      .getNew(length, 30);
    final Clazz clazz = this.node_manager.clazz_factory
      .getNew(this.colour_link);
    this.link_manager.setLink(node_1, node_2, type, clazz);
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