// This code has been placed into the public domain by its author
package com.springie.io.out;

import com.springie.elements.links.LinkManager;
import com.springie.elements.nodes.NodeManager;
import com.springie.io.out.writers.spr.WriterSpr;

public class Serialiser {
  NodeManager node_manager;

  LinkManager link_manager;

  public Serialiser(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;
  }

  public String toString() {
    final WriterSpr writer = new WriterSpr(this.node_manager);
    return writer.generateString();
  }
}