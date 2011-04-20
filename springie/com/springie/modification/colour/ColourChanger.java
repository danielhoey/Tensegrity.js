package com.springie.modification.colour;

import com.springie.collisions.BinGrid;
import com.springie.elements.faces.Face;
import com.springie.elements.faces.FaceManager;
import com.springie.elements.links.Link;
import com.springie.elements.links.LinkManager;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.modification.pre.PrepareToModifyFaceClazzes;
import com.springie.modification.pre.PrepareToModifyLinkClazzes;
import com.springie.modification.pre.PrepareToModifyNodeClazzes;

public class ColourChanger {
  NodeManager node_manager;

  LinkManager link_manager;

  FaceManager manager_faces;

  public ColourChanger(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;
    this.manager_faces = node_manager.face_manager;

    prepare();
  }

  public void setColour(int argb) {
    setNodeColour(argb);
    setLinkColour(argb);
    setFaceColour(argb);
  }

  void prepare() {
    final PrepareToModifyNodeClazzes prepare_mn = new PrepareToModifyNodeClazzes(
      this.node_manager);
    prepare_mn.prepare();

    final PrepareToModifyLinkClazzes prepare_ml = new PrepareToModifyLinkClazzes(
      this.node_manager);
    prepare_ml.prepare();

    final PrepareToModifyFaceClazzes prepare_mp = new PrepareToModifyFaceClazzes(
      this.node_manager);
    prepare_mp.prepare();
  }

  private void setNodeColour(int argb) {
    final int number_of_nodes = this.node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) this.node_manager.element
        .elementAt(counter);
      if (candidate.type.selected) {
        candidate.clazz.colour = argb;
      }
    }

    BinGrid.repaint_some_objects = true;
  }

  private void setLinkColour(int argb) {
    final int max_size = this.link_manager.element.size();

    for (int counter = max_size; --counter >= 0;) {
      final Link l = (Link) this.link_manager.element.elementAt(counter);
      if (l.type.selected) {
        l.clazz.colour = argb;
      }
    }

    BinGrid.repaint_some_objects = true;
  }

  private void setFaceColour(int argb) {
    final int max_size = this.manager_faces.element.size();

    for (int counter = max_size; --counter >= 0;) {
      final Face p = (Face) this.manager_faces.element.elementAt(counter);
      if (p.type.selected) {
        p.clazz.colour = argb;
      }
    }

    BinGrid.repaint_some_objects = true;
  }
}