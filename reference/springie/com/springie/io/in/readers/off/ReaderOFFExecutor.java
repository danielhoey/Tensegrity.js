// * Read in information from files...

package com.springie.io.in.readers.off;

import com.springie.elements.clazz.Clazz;
import com.springie.elements.faces.Face;
import com.springie.elements.faces.FaceManager;
import com.springie.elements.links.LinkManager;
import com.springie.elements.links.LinkType;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.io.in.AutomaticNodeColourer;
import com.springie.modification.post.PostModification;
import com.springie.modification.resize.ScaleToFitScreen;
import com.springie.modification.translation.CentreOnScreen;
import com.springie.utilities.log.Log;
import com.tifsoft.utilities.execute.Executor;

public class ReaderOFFExecutor implements Executor {
  public Object execute(Object o) {
    final NodeManager node_manager = (NodeManager) o;

    ScaleToFitScreen.scale(node_manager);
    CentreOnScreen.centre(node_manager);

    new PostModification(node_manager).cleanup();

    makeStrutsFromFaces(node_manager);

    deleteFacesWithTwoNodes(node_manager);

    new AutomaticNodeColourer(node_manager).execute();

    //printTwoWayLinks(node_manager);

    //new DeriveLinkColoursFromNodeColours(node_manager).execute();
    //new AverageNodeColoursFromLinkColours(node_manager).execute();

    return null;
  }

  private void makeStrutsFromFaces(NodeManager node_manager) {
    final LinkManager link_manager = node_manager.link_manager;
    final FaceManager face_manager = node_manager.face_manager;

    final int n_faces = face_manager.element.size();

    for (int i = 0; i < n_faces; i++) {
      final Face face = (Face) face_manager.element.elementAt(i);
      final int n_nodes = face.node.size();
      for (int j = 0; j < n_nodes; j++) {
        final Node node1 = (Node) face.node.elementAt(j);
        final Node node2 = (Node) face.node.elementAt((j + 1) % n_nodes);
        if (!link_manager.isThereALinkBetween(node1, node2)) {
          joinNodes(node_manager, node1, node2);
        } else {
          Log.log("dlF!");
        }
      }
    }
  }

  private void deleteFacesWithTwoNodes(NodeManager node_manager) {
    final FaceManager face_manager = node_manager.face_manager;

    final int n_faces = face_manager.element.size();

    for (int i = n_faces; --i >= 0;) {
      final Face face = (Face) face_manager.element.elementAt(i);
      final int n_nodes = face.node.size();
      if (n_nodes == 2) {
        face_manager.element.removeElementAt(i);
      }
    }
  }

  private void joinNodes(NodeManager node_manager, Node node_1, Node node_2) {
    final LinkManager link_manager = node_manager.link_manager;

    final int length = node_manager.distanceBetween(node_1, node_2);
    int radius = length / 12;
    int radius_max = (node_1.type.radius + node_2.type.radius) / 3;
    if (radius > radius_max) {
      radius = radius_max;
    }
    final LinkType type = link_manager.link_type_factory.getNew(length, radius);

    final Clazz clazz = node_manager.clazz_factory.getNew(0xFFFFFF40);
    link_manager.setLink(node_1, node_2, type, clazz);
  }
}