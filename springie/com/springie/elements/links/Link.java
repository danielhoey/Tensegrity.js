//This code has been placed into the public domain by its author

package com.springie.elements.links;

import com.springie.FrEnd;
import com.springie.elements.base.BaseElement;
import com.springie.elements.clazz.Clazz;
import com.springie.elements.nodes.Node;
import com.springie.explosions.fragments.LineFragmentManager;
import com.springie.render.Coords;
import com.springie.utilities.math.SquareRoot;
import com.springie.utilities.random.JUR;
import com.springie.world.World;

public class Link extends BaseElement {
  public Node node1;

  public Node node2;

  public LinkType type;

  public static int number_of_strut_render_divisions = 2;

  public static int number_of_cable_render_divisions = 1;

  public static int elasticity_global = 2200;

  public static int damping_global = 660;

  public static World temp_private_world;

  static final int MAX_DIAMETER = 16;

  public static final int INVISIBLE = 0;

  public static final int LONG = 0;

  public static final int SHORT = 1;

  public static final int number_of_dots = 2;

  public static final int log_number_of_dots = 1;

  public static int link_display_struts_type = LinkRenderType.MULTIPLE;

  public static int link_display_cables_type = LinkRenderType.MULTIPLE;

  public static int link_display_length = SHORT;

  public Link(Node e1, Node e2, LinkType type, Clazz clazz) {
    this.node1 = e1;
    this.node2 = e2;
    this.type = type;
    this.clazz = clazz;
  }

  Link(Node e1, Node e2, Link l) {
    set(l);
    this.node1 = e1;
    this.node2 = e2;
  }

  void set(Node e1, Node e2, int l, int e) {
    this.node1 = e1;
    this.node2 = e2;
    this.type = new LinkType(l, e);
  }

  void set(Node e1, Node e2, LinkType type, Clazz clazz) {
    this.node1 = e1;
    this.node2 = e2;
    this.type = type;
    this.clazz = clazz;
  }

  // DANGER
  void set(Link l) {
    this.type = l.type;
    this.clazz = l.clazz;

    this.node1 = l.node1;
    this.node2 = l.node2;
  }

  void set(Node e1, Node e2, Link l) {
    set(l);
    // override the defaults...
    this.node1 = e1;
    this.node2 = e2;
  }

  public int getActualLength() {
    final int delta_x = (this.node1.pos.x - this.node2.pos.x) >> Coords.shift;
    final int delta_y = (this.node1.pos.y - this.node2.pos.y) >> Coords.shift;
    final int delta_z = (this.node1.pos.z - this.node2.pos.z) >> Coords.shift;

    final int actual_length_squared = (delta_x * delta_x) + (delta_y * delta_y)
      + (delta_z * delta_z);
    return (SquareRoot.fastSqrt(1 + actual_length_squared)) << Coords.shift;
  }

  void applyForce() {
    if (this.type.elasticity != 0) {
      applyForceToNodes();
    }
  }

  private void applyForceToNodes() {
    final int d_x = (this.node1.pos.x - this.node2.pos.x) >> Coords.shift;
    final int d_y = (this.node1.pos.y - this.node2.pos.y) >> Coords.shift;
    final int d_z = (this.node1.pos.z - this.node2.pos.z) >> Coords.shift;

    final int current_length = this.type.length >> Coords.shift;

    final int actual_length_squared = (d_x * d_x) + (d_y * d_y) + (d_z * d_z);

    final int l = this.type.length >> Coords.shift;
    final int temp_length_limit_squared = l * l;

    if (actual_length_squared < temp_length_limit_squared) {
      if (this.type.cable) {
        return;
      }
    }

    int d_dx;
    int d_dy;
    int d_dz;
    int force_x;
    int force_y;
    int force_z;
    int unit_vector_x;
    int unit_vector_y;
    int unit_vector_z;
    int parting;

    final int actual_length = SquareRoot.fastSqrt(1 + actual_length_squared);
    d_dx = this.node1.velocity.x - this.node2.velocity.x;
    d_dy = this.node1.velocity.y - this.node2.velocity.y;
    d_dz = this.node1.velocity.z - this.node2.velocity.z;

    unit_vector_x = (d_x << Coords.shift) / actual_length;
    unit_vector_y = (d_y << Coords.shift) / actual_length;
    unit_vector_z = (d_z << Coords.shift) / actual_length;

    parting = (d_x * d_dx) + (d_y * d_dy) + (d_z * d_dz);
    parting = parting / actual_length;

    final int temp_damping = (damping_global * this.type.damping) >> 8;
    final int temp_elasticity = (elasticity_global * this.type.elasticity) >> 8;

    final int val = ((((actual_length - current_length) * temp_elasticity) + ((parting * temp_damping) >> 7)) + 512) >> 10;

    force_x = unit_vector_x * val;
    force_y = unit_vector_y * val;
    force_z = unit_vector_z * val;

    this.node1.velocity.x -= force_x;
    this.node1.velocity.y -= force_y;
    this.node1.velocity.z -= force_z;

    this.node2.velocity.x += force_x;
    this.node2.velocity.y += force_y;
    this.node2.velocity.z += force_z;

  }

  void explodeThisLink() {
    final JUR rnd = new JUR();

    int start_x = 0;//  this.node1.preserved.x << Coords.shift; // TODO!!!
    int start_y = 0;//  this.node1.preserved.y << Coords.shift;
    int d_x = 0;//  this.node2.preserved.x - this.node1.preserved.x;
    int d_y = 0;//  this.node2.preserved.y - this.node1.preserved.y;

    int temp2 = SquareRoot.fastSqrt(1 + (d_x * d_x) + (d_y * d_y));

    if (temp2 > 20) {
      temp2 = temp2 / 10;
    } else {
      temp2 = 4;
    }

    d_x = (d_x << Coords.shift) / temp2;
    d_y = (d_y << Coords.shift) / temp2;

    int start_dx = this.node1.velocity.x;
    int start_dy = this.node1.velocity.y;

    int d_dx = (this.node2.velocity.x - this.node1.velocity.x) / temp2;
    int d_dy = (this.node2.velocity.y - this.node1.velocity.y) / temp2;

    if (FrEnd.explosions) {
      if (!FrEnd.xor) {
        for (int temp = 0; temp < temp2; temp++) {
          int temp3 = rnd.nextInt();
          final int temp4 = (temp3 << 8) >> 23;
          temp3 = temp3 >> 23;

          LineFragmentManager.add(start_x, start_y, start_x + d_x, start_y
            + d_y, start_dx + temp3, start_dy + temp4, 4);
          start_x += d_x;
          start_y += d_y;
          start_dx += d_dx;
          start_dy += d_dy;
        }
      }
    }
  }

  public int getThicknesss() {
    return this.type.radius >> Coords.shift;
  }

  public int getDisplayType() {
    final int type = this.type.cable ? Link.link_display_cables_type
      : Link.link_display_struts_type;
    return type;
  }

  /**
   * Given an this.node (which is assumed to be at one end of this link) return
   * the this.node at the other end
   */
  public final Node theOtherEnd(Node e) {
    if (this.node1 == e) {
      return this.node2;
    }

    return this.node1;
  }

  //public void setNode1(Node temp_node1) {
    // TODO Auto-generated method stub
    //this.node1
    
  //}
}