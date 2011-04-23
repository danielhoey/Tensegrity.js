package com.springie.modification.pre;

import com.springie.elements.clazz.Clazz;
import com.springie.elements.clazz.ClazzFactory;
import com.springie.elements.faces.Face;
import com.springie.elements.faces.FaceManager;
import com.springie.elements.nodes.NodeManager;

public class PrepareToModifyFaceClazzes {
  NodeManager node_manager;

  FaceManager face_manager;

  public PrepareToModifyFaceClazzes(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.face_manager = node_manager.face_manager;
  }

  public boolean weNeedToMakeNewLinkClazzes() {
    if (this.face_manager.each_has_its_own_clazz) {
      return false;
    }

    if (thereIsALinkClazzClash()) {
      return true;
    }

    return false;
  }

  private boolean thereIsALinkClazzClash() {
    final int number = this.face_manager.element.size();
    for (int c1 = number; --c1 >= 0;) {
      for (int c2 = c1; --c2 >= 0;) {
        final Face l1 = (Face) this.face_manager.element.elementAt(c1);
        final Face l2 = (Face) this.face_manager.element.elementAt(c2);
        if (l1.clazz == l2.clazz) {
          return true;
        }
      }
    }
    return false;
  }

  public void prepare() {
    if (weNeedToMakeNewLinkClazzes()) {
      makeNewLinkClazzes();
    }

    this.face_manager.each_has_its_own_clazz = true;
  }

  public void makeNewLinkClazzes() {
    final int number = this.face_manager.element.size();
    for (int counter = number; --counter >= 0;) {
      final Face candidate = (Face) this.face_manager.element.elementAt(counter);
      final int colour = candidate.clazz.colour;
      final ClazzFactory clazz_factory = this.node_manager.clazz_factory;
      final Clazz clazz = clazz_factory.getNew(colour);
      candidate.clazz = clazz;
    }
  }
}