// This code has been placed into the public domain by its author

package com.springie.modification.projection;

import com.springie.elements.clazz.Clazz;
import com.springie.elements.clazz.ClazzFactory;
import com.springie.elements.nodes.NodeManager;
import com.springie.utilities.random.Hortensius32Fast;

public class MakeSlightAdjustmentsToClazzes {
  NodeManager node_manager;

  Hortensius32Fast rnd = new Hortensius32Fast();

  public MakeSlightAdjustmentsToClazzes(NodeManager node_manager) {
    this.node_manager = node_manager;
  }

  public void adjust() {
    final int xor = this.rnd.nextInt() & 0xFF0F0F0F;
    
    final ClazzFactory clazz_factory = this.node_manager.clazz_factory;
    final int n = clazz_factory.array.size();

    for (int i = n; --i >= 0;) {
      final Clazz clazz = (Clazz) clazz_factory.array.elementAt(i);
      clazz.colour = clazz.colour ^ xor;
    }
  }
}