package com.springie.modification.pre;

import com.springie.elements.faces.Face;
import com.springie.elements.faces.FaceManager;
import com.springie.elements.faces.FaceType;

public class PrepareToModifyFaceTypes {
  FaceManager polygon_manager;

  public PrepareToModifyFaceTypes(FaceManager polygon_manager) {
    this.polygon_manager = polygon_manager;
  }

  public boolean weNeedToMakeNewTypes() {
    if (this.polygon_manager.each_has_its_own_type) {
      return false;
    }

    final int number_of_links = this.polygon_manager.element.size();
    for (int c1 = number_of_links; --c1 >= 0;) {
      for (int c2 = c1; --c2 >= 0;) {
        final Face l1 = (Face) this.polygon_manager.element.elementAt(c1);
        final Face l2 = (Face) this.polygon_manager.element.elementAt(c2);
        if (l1.type == l2.type) {
          return true;
        }
      }
    }
    return false;
  }

  public void prepare() {
    if (weNeedToMakeNewTypes()) {
      makeNewTypes();
    }

    this.polygon_manager.each_has_its_own_type = true;
  }

  public void makeNewTypes() {
    final int number = this.polygon_manager.element.size();
    for (int counter = number; --counter >= 0;) {
      final Face candidate = (Face) this.polygon_manager.element.elementAt(counter);
      FaceType type = this.polygon_manager.face_type_factory.getNew();
      type.makeEqualTo(candidate.type);
      candidate.type = type;
    }
  }
}