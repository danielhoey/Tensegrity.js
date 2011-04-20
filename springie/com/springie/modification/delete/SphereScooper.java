package com.springie.modification.delete;

import com.springie.elements.links.LinkManager;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.modification.pre.PrepareToModifyNodeTypes;

public class SphereScooper {
  NodeManager node_manager;
  LinkManager link_manager;

  public SphereScooper(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;
    
    prepare();
  }

  public void delete() {
    long t_x = 0;
    long t_y = 0;
    long t_z = 0;
    final int number_of_nodes = this.node_manager.element.size();

    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) this.node_manager.element.elementAt(counter);
      t_x += candidate.pos.x;
      t_y += candidate.pos.y;
      t_z += candidate.pos.z;
    }

    final int av_x = (int) t_x / number_of_nodes;
    final int av_y = (int) t_y / number_of_nodes;
    final int av_z = (int) t_z / number_of_nodes;

    final long max_d = getMaxDistance(av_x, av_y, av_z);
    final long r_outer = (long)(max_d * 0.45);
    final long r_inner = (long)(max_d * 0.11);

    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) this.node_manager.element.elementAt(counter);
      final long d_x = candidate.pos.x - av_x;
      final long d_y = candidate.pos.y - av_y;
      final long d_z = candidate.pos.z - av_z;
      final long dist = d_x * d_x + d_y * d_y + d_z * d_z;
      if (dist > r_outer) {
        candidate.simplyKill();
      }

      if (dist < r_inner) {
        candidate.simplyKill();
      }
    }
  }

  private long getMaxDistance(int av_x, int av_y, int av_z) {
    long max_d = 0;
    final int number_of_nodes = this.node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) this.node_manager.element.elementAt(counter);
      final long d_x = candidate.pos.x - av_x;
      final long d_y = candidate.pos.y - av_y;
      final long d_z = candidate.pos.z - av_z;
      long dist = d_x * d_x + d_y * d_y + d_z * d_z;
      if (dist > max_d) {
        max_d = dist;
      }
    }

    return max_d;
  }
  
  void prepare() {
    final PrepareToModifyNodeTypes prepare_n = new PrepareToModifyNodeTypes(this.node_manager);
    prepare_n.prepare();
  }
}