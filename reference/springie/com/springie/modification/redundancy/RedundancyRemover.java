package com.springie.modification.redundancy;

import com.springie.elements.nodes.NodeManager;

public class RedundancyRemover {
  NodeManager node_manager;

  public RedundancyRemover(NodeManager node_manager) {
    this.node_manager = node_manager;
  }

  public void removeRedundancy() {
    final ClazzRedundancyRemover crr = new ClazzRedundancyRemover(this.node_manager);
    crr.removeRedundancy();
    
    final TypeRedundancyRemoverNode trrn = new TypeRedundancyRemoverNode(this.node_manager);
    trrn.removeRedundancy();
    
    final TypeRedundancyRemoverLink trrl = new TypeRedundancyRemoverLink(this.node_manager);
    trrl.removeRedundancy();
    
    final TypeRedundancyRemoverFace trrp = new TypeRedundancyRemoverFace(this.node_manager);
    trrp.removeRedundancy();
  }
}