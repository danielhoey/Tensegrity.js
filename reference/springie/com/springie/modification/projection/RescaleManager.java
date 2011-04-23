// This code has been placed into the public domain by its author

package com.springie.modification.projection;

import com.springie.FrEnd;
import com.springie.elements.links.Link;
import com.springie.elements.links.LinkManager;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.geometry.Point3D;
import com.springie.gui.gestures.TransferUtilities;

public class RescaleManager {
  int[] link_length;

  int[] node_radius;

  int[] link_radius;

  Point3D centre;

  NodeManager node_manager;

  LinkManager link_manager;

  public RescaleManager(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;
  }

  public void setup() {
    final TransferUtilities transfer_utilities = new TransferUtilities();
    
    this.link_length = transfer_utilities.transferLinkLengths();
    this.link_radius = transfer_utilities.transferLinkRadii();
    this.node_radius = transfer_utilities.transferNodeRadii();

    this.centre = this.node_manager.getCentre();
  }

  public void performRescale(final float scale_factor) {
    rescaleNodes(scale_factor);

    rescaleLinks(scale_factor);

    FrEnd.postCleanup(); // *unless* cache contains radii...
  }

  private void rescaleNodes(final float scale_factor) {
    final int n = this.node_manager.element.size();

    for (int i = n; --i >= 0;) {
      final Node node = (Node) FrEnd.node_manager.element.elementAt(i);

      node.type.radius = (int) (this.node_radius[i] * scale_factor);

      final Point3D pos = node.pos;
      
      pos.subtractTuple3D(this.centre);
      
      pos.x = (int) (pos.x * scale_factor);
      pos.y = (int) (pos.y * scale_factor);
      pos.z = (int) (pos.z * scale_factor);

      pos.addTuple3D(this.centre);    
    }
  }

  private void rescaleLinks(final float scale_factor) {
    final int n_lk = this.link_manager.element.size();
    for (int i = n_lk; --i >= 0;) {
      rescaleLink(scale_factor, i);
    }
  }

  private void rescaleLink(final float scale_factor, int i) {
    final Link link = (Link) this.link_manager.element.elementAt(i);

    link.type.length = (int) (this.link_length[i] * scale_factor);
    link.type.radius = (int) (this.link_radius[i] * scale_factor);
  }
}