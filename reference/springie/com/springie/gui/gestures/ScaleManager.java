// This code has been placed into the public domain by its author

package com.springie.gui.gestures;

import com.springie.FrEnd;
import com.springie.collisions.BinGrid;
import com.springie.elements.links.Link;
import com.springie.elements.links.LinkManager;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.geometry.Point3D;
import com.springie.render.Coords;
import com.springie.utilities.general.Forget;

public class ScaleManager {
  private int start_x;

  int[] link_length;

  int[] node_radius;

  int[] link_radius;

  Point3D centre;

  NodeManager node_manager;

  LinkManager link_manager;

  public void initialise(NodeManager node_manager, int x, int y) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;

    if (FrEnd.button_virginity) {
      this.start_x = x;

      setup();
    } else {
      performScale(x, y);
    }
  }

  public void setup() {
    this.link_length = new TransferUtilities().transferLinkLengths();
    this.link_radius = new TransferUtilities().transferLinkRadii();
    this.node_radius = new TransferUtilities().transferNodeRadii();

    this.centre = this.node_manager.getCentre();
    //FrEnd.forces_disabled_during_gesture = true;
  }

  public void performScale(int x, int y) {
    Forget.about(y);
    final float scale_factor = 1F + (x - this.start_x)
      / (float) (1 << (Coords.shift + 7));

    performScale(scale_factor);
  }

  public void performScale(final float scale_factor) {
    rescaleNodes(scale_factor);

    rescaleLinks(scale_factor);

    BinGrid.repaint_some_objects = true;
//    FrEnd.postCleanup(); // *unless* cache contains radii...
  }

  private void rescaleNodes(final float scale_factor) {
    final int n = this.node_manager.element.size();

    for (int i = n; --i >= 0;) {
      final Node node = (Node) FrEnd.node_manager.element.elementAt(i);

      node.type.radius = (int) (this.node_radius[i] * scale_factor);
    }
  }

  private void rescaleLinks(final float scale_factor) {
    final LinkManager link_manager = this.link_manager;
    final int n_lk = link_manager.element.size();
    for (int i = n_lk; --i >= 0;) {
      rescaleLink(scale_factor, i);
    }
  }

  private void rescaleLink(final float scale_factor, int i) {
    final LinkManager link_manager = this.link_manager;
    final Link link = (Link) link_manager.element.elementAt(i);

    link.type.length = (int) (this.link_length[i] * scale_factor);
    link.type.radius = (int) (this.link_radius[i] * scale_factor);
  }

  public void terminate(int x, int y) {
    if (this.link_length != null) {
      performScale(x, y);
      this.link_length = null;
      //FrEnd.forces_disabled_during_gesture = false;
    }
  }
}