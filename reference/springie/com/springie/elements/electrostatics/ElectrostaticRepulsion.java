//This code has been placed into the public domain by its author

package com.springie.elements.electrostatics;

import java.util.Vector;

import com.springie.elements.nodes.Node;
import com.springie.render.Coords;

public class ElectrostaticRepulsion {
  public Vector charged = new Vector();

  private long charge_strength_factor = 20000;
  
  public boolean charge_active = true;
  
  public void repel() {
    if (this.charge_active) {
      applyForce();
    }
  }

  private void applyForce() {
    final int number_of_nodes = this.charged.size();
    for (int counter1 = number_of_nodes; --counter1 >= 0;) {
      final Node node1 = (Node) this.charged.elementAt(counter1);
      for (int counter2 = number_of_nodes; --counter2 > counter1;) {
        final Node node2 = (Node) this.charged.elementAt(counter2);
        applyForceToNodes(node1, node2);
      }
    }
  }

  private void applyForceToNodes(Node node1, Node node2) {
    final int d_x = (node1.pos.x - node2.pos.x) >> Coords.shift;
    final int d_y = (node1.pos.y - node2.pos.y) >> Coords.shift;
    final int d_z = (node1.pos.z - node2.pos.z) >> Coords.shift;

    final long actual_length_squared = 1 + (d_x * d_x) + (d_y * d_y) + (d_z * d_z);
    final int charge_1_times_charge_2 = node1.type.charge * node2.type.charge;
    final long factor = this.charge_strength_factor * charge_1_times_charge_2 * charge_1_times_charge_2;

    final int val = (int) (factor / actual_length_squared / actual_length_squared);

    int force_x = d_x * val;
    int force_y = d_y * val;
    int force_z = d_z * val;

    node1.velocity.x += force_x;
    node1.velocity.y += force_y;
    node1.velocity.z += force_z;

    node2.velocity.x -= force_x;
    node2.velocity.y -= force_y;
    node2.velocity.z -= force_z;
  }
}