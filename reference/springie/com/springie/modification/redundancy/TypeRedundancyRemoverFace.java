package com.springie.modification.redundancy;

import java.util.Vector;

import com.springie.elements.faces.Face;
import com.springie.elements.faces.FaceType;
import com.springie.elements.nodes.NodeManager;

public class TypeRedundancyRemoverFace {
  NodeManager node_manager;

  public TypeRedundancyRemoverFace(NodeManager node_manager) {
    this.node_manager = node_manager;
  }

  public void removeRedundancy() {
    removeRedundancyInFaceTypes();
  }

  // polygons...
  public void removeRedundancyInFaceTypes() {
    final Vector v = this.node_manager.face_manager.face_type_factory.array;
    final int size = v.size();
    for (int i = size; --i >= 0;) {
      final FaceType nt = (FaceType) v.elementAt(i);
      if (equalspreviousPolygonType(v, i)) {
        final int first = getFirstPolygonType(v, nt, i);
        replacePolygonTypeWithPrevious(v, i, first);
      }
    }
  }

  private void replacePolygonTypeWithPrevious(Vector v, int old, int nww) {
    final FaceType nt_old = (FaceType) v.elementAt(old);
    final FaceType nt_nww = (FaceType) v.elementAt(nww);
    final int n_o_p = this.node_manager.face_manager.element.size();
    for (int i = n_o_p; --i >= 0;) {
      final Face p = (Face) this.node_manager.face_manager.element.elementAt(i);
      if (p.type.equals(nt_old)) {
        p.type = nt_nww;
      }
    }

    v.remove(old);
  }

  private int getFirstPolygonType(Vector v, FaceType t, int max) {
    for (int i = max; --i >= 0;) {
      if (t.equals(v.elementAt(i))) {
        return i;
      }
    }

    return -1;
  }

  private boolean equalspreviousPolygonType(Vector v, int max) {
    final FaceType t = (FaceType) v.elementAt(max);

    return getFirstPolygonType(v, t, max) > 0;
  }
}