// This code has been placed into the public domain by its author

package com.springie.gui.gestures;

import com.springie.FrEnd;
import com.springie.collisions.BinGrid;
import com.springie.elements.links.Link;
import com.springie.elements.links.LinkManager;
import com.springie.elements.nodes.Node;
import com.springie.geometry.Point3D;
import com.springie.render.Coords;
import com.springie.utilities.general.Forget;

public class DietManager {
  private int start_x;

  int[] node_radius;

  int[] link_radius;
  
  Point3D centre;

  public void initialise(int x, int y) {
    if (FrEnd.button_virginity) {
      this.start_x = x;

      //this.link_length = new TransferUtilities().transferLinkLengths();
      this.link_radius = new TransferUtilities().transferLinkRadii();
      this.node_radius = new TransferUtilities().transferNodeRadii();

      this.centre = FrEnd.node_manager.getCentre();
      //FrEnd.forces_disabled_during_gesture = true;
    } else {
      performScale(x, y);
    }
  }

  public void performScale(int x, int y) {
    Forget.about(y);
    final float scale_factor = 1F + (x - this.start_x)
      / (float) (1 << (Coords.shift + 7));

    rescaleNodes(scale_factor);

    rescaleLinks(scale_factor);
    
    BinGrid.repaint_some_objects = true;
    //FrEnd.postCleanup();
  }

  private void rescaleNodes(final float scale_factor) {
    final int n = FrEnd.node_manager.element.size();

    for (int i = n; --i >= 0;) {
      final Node node = (Node) FrEnd.node_manager.element.elementAt(i);

      node.type.radius = (int)(this.node_radius[i] * scale_factor);
    }
  }

  private void rescaleLinks(final float scale_factor) {
    final LinkManager link_manager = FrEnd.node_manager.link_manager;
    final int n_lk = link_manager.element.size();
    for (int i = n_lk; --i >= 0;) {
      rescaleLink(scale_factor, link_manager, i);
    }
  }

  private void rescaleLink(final float scale_factor, final LinkManager link_manager, int i) {
    final Link link = (Link) link_manager.element.elementAt(i);
    
    //link.type.length = (int)(this.link_length[i] * scale_factor);
    link.type.radius = (int)(this.link_radius[i] * scale_factor);
  }

  public void terminate(int x, int y) {
    if (this.node_radius != null) {
      performScale(x, y);
      this.node_radius = null;
      //FrEnd.forces_disabled_during_gesture = false;
    }
  }
}