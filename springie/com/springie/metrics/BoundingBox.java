package com.springie.metrics;

import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.elements.nodes.NodeType;
import com.springie.geometry.Point3D;

public class BoundingBox {
  int min_v = Integer.MIN_VALUE;
  int max_v = Integer.MAX_VALUE;
  
  public Point3D max;

  public Point3D min;

  public void find(NodeManager node_manager) {
    this.max = new Point3D(this.min_v, this.min_v, this.min_v);
    this.min = new Point3D(this.max_v, this.max_v, this.max_v);

    final int number_of_nodes = node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) node_manager.element.elementAt(counter);
      final Point3D pos = candidate.pos;
      final NodeType type = candidate.type;
      final int radius = type.radius;
      
      if (pos.x - radius < this.min.x) {
        this.min.x = pos.x - radius;
      }

      if (pos.y - radius < this.min.y) {
        this.min.y = pos.y - radius;
      }

      if (pos.z - radius < this.min.z) {
        this.min.z = pos.z - radius;
      }

      if (pos.x + radius > this.max.x) {
        this.max.x = pos.x + radius;
      }

      if (pos.y + radius > this.max.y) {
        this.max.y = pos.y + radius;
      }

      if (pos.z + radius > this.max.z) {
        this.max.z = pos.z + radius;
      }
    }
  }
}