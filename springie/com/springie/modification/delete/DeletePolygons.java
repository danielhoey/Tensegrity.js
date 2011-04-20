package com.springie.modification.delete;

import com.springie.collisions.BinGrid;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.modification.pre.PrepareToModifyFaceTypes;

public class DeletePolygons {
  NodeManager node_manager;

  public DeletePolygons(NodeManager node_manager) {
    this.node_manager = node_manager;

    prepare();
  }

  public void delete() {
    final int number_of_nodes = this.node_manager.element.size();

    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) this.node_manager.element
        .elementAt(counter);
      if (candidate.type.selected) {
        this.node_manager.face_manager.killAllPolygons(candidate);
      }
    }

    BinGrid.repaint_all_objects = true;
  }

  void prepare() {
    final PrepareToModifyFaceTypes prepare_f = new PrepareToModifyFaceTypes(
      this.node_manager.face_manager);
    prepare_f.prepare();
  }
}