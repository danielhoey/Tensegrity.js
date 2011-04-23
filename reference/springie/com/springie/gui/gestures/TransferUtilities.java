// This code has been placed into the public domain by its author

package com.springie.gui.gestures;

import com.springie.FrEnd;
import com.springie.elements.links.Link;
import com.springie.elements.links.LinkManager;
import com.springie.elements.nodes.Node;
import com.springie.geometry.Point3D;

public class TransferUtilities {
  public Point3D[] transferPositions() {
    final int n = FrEnd.node_manager.element.size();
    final Point3D[] pos = new Point3D[n];

    for (int i = n; --i >= 0;) {
      final Node node = (Node) FrEnd.node_manager.element.elementAt(i);
      pos[i] = (Point3D) node.pos.clone();
    }

    return pos;
  }

  public int[] transferLinkLengths() {
    final LinkManager link_manager = FrEnd.node_manager.link_manager;
    final int n = link_manager.element.size();
    final int[] lengths = new int[n];

    for (int i = n; --i >= 0;) {
      final Link link = (Link) link_manager.element.elementAt(i);
      lengths[i] = link.type.length;
    }

    return lengths;
  }

  public int[] transferLinkRadii() {
    final LinkManager link_manager = FrEnd.node_manager.link_manager;
    final int n = link_manager.element.size();
    final int[] radii = new int[n];

    for (int i = n; --i >= 0;) {
      final Link link = (Link) link_manager.element.elementAt(i);
      radii[i] = link.type.radius;
    }

    return radii;
  }

  public int[] transferNodeRadii() {
    final int n = FrEnd.node_manager.element.size();
    final int[] radii = new int[n];

    for (int i = n; --i >= 0;) {
      final Node node = (Node) FrEnd.node_manager.element.elementAt(i);
      radii[i] = node.type.radius;
    }

    return radii;
  }
}