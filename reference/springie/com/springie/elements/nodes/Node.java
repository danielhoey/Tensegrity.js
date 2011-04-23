//This code has been placed into the public domain by its author

package com.springie.elements.nodes;

import com.springie.FrEnd;
import com.springie.collisions.BinGrid;
import com.springie.composite.Composite;
import com.springie.elements.base.BaseElement;
import com.springie.elements.clazz.ClazzFactory;
import com.springie.elements.lists.ListOfIntegers;
import com.springie.elements.nodegrid.GridOfBinsForCachedNodes;
import com.springie.explosions.fragments.LineFragmentManager;
import com.springie.explosions.particles.Particle;
import com.springie.explosions.particles.ParticleManager;
import com.springie.geometry.Point3D;
import com.springie.geometry.Vector3D;
import com.springie.render.Coords;
import com.springie.world.World;

/**
 * This class represents a single "node" in the system
 */
public class Node extends BaseElement {
  public Point3D pos = new Point3D(0, 0, 0);
  public Vector3D velocity = new Vector3D(0, 0, 0);
  public NodeType type;
  public boolean fixed;
  public ListOfIntegers list_of_links = new ListOfIntegers();
  public ListOfIntegers list_of_polygons = new ListOfIntegers();
  public Point3D current_bin = new Point3D(-1, -1, -1);
  public Composite creature; // TODO get rid of this...?
  public static int thickness = 6; // display only...
  public static int max_speed = Integer.MAX_VALUE;
  public static int viscocity;
  public static int friction_strength;
  static Node temp_agent;
  static Node temp2_agent;
  public static int[] sin_tab;
  public static int[] cos_tab;
  public static int lut_shift = 8;
  public static int lut_magnitude = 1 << lut_shift;
  public static int LOG_TRIG_TAB_SIZE = 7;
  public static int TRIG_TAB_SIZE = 1 << LOG_TRIG_TAB_SIZE;
  public static int TRIG_TAB_SIZEMO = TRIG_TAB_SIZE - 1;
  static int TRIG_TAB_SIZEO2 = TRIG_TAB_SIZE >>> 1;
  public static World temp_private_world;
  public static int number_of_render_divisions = 1;

  public Node() {
    //setUpVectors();
  }

  public Node(Point3D pos, int seed, NodeTypeFactory node_type_factory) {
    this.pos = pos;
    this.type = node_type_factory.getNew();//new NodeType(); //aargh!

    NodeType.static_rnd.setSeed(seed);

    this.velocity = new Vector3D(3, 4, 5);

    this.type.setSize(NodeManager.general_size);
    this.type.log_mass = 16;
  }

  public Node(Node e, NodeTypeFactory node_type_factory, ClazzFactory clazz_factory) {
    //setUpVectors();
    set(e, node_type_factory, clazz_factory);
  }

  public void set(Point3D pos, int seed, NodeTypeFactory node_type_factory) {
    this.pos = pos;

    this.type = node_type_factory.getNew();

    NodeType.static_rnd.setSeed(seed);
    this.velocity = new Vector3D(3, 4, 5);

    this.type.setSize(NodeManager.general_size);
    this.type.log_mass = 16;
  }

  public void set(Node e, NodeTypeFactory node_type_factory, ClazzFactory clazz_factory) {
    this.pos.x = e.pos.x;
    this.pos.y = e.pos.y;
    this.pos.z = e.pos.z;
    this.velocity.x = e.velocity.x;
    this.velocity.y = e.velocity.y;
    this.velocity.z = e.velocity.z;

    this.type = node_type_factory.getNew(e.type);

    this.clazz = e.clazz;
    this.clazz = clazz_factory.getNew(e.clazz.colour);

    this.creature = e.creature;

    this.current_bin.x = e.current_bin.x;
    this.current_bin.y = e.current_bin.y;
    this.current_bin.z = e.current_bin.z;

    //    this.preserved.x = e.preserved.x;
    //    this.preserved.y = e.preserved.y;
    //    this.preserved.z = e.preserved.z;

    //this.list_of_links.removeAllElements(); // no links yet, though...
  }

  // need "reset node" routines here...
  public static final void initial() {
    sin_tab = new int[TRIG_TAB_SIZE];
    cos_tab = new int[TRIG_TAB_SIZE];
    for (int temp = 0; temp < TRIG_TAB_SIZE; temp++) {
      sin_tab[temp] = (int) (lut_magnitude
        * (java.lang.Math.sin((temp * 2 * java.lang.Math.PI) / TRIG_TAB_SIZE)) + 0.5);
      cos_tab[temp] = (int) (lut_magnitude
        * (java.lang.Math.cos((temp * 2 * java.lang.Math.PI) / TRIG_TAB_SIZE)) + 0.5);
    }

    //ship_image_array = new Image[number_of_ships];
  }

  /**
   * true iff this node is currently selected...
   */
  public boolean isSelected() {
    return this.type.selected;
  }

  public void travel() {
    capVelocities();

    final boolean motionless = this.type.fixed || (FrEnd.dragged_node != null && this.type.selected);
    
    if (motionless) {
      this.velocity = new Vector3D(0, 0, 0);
    } else {
      this.pos.addTuple3D(this.velocity);
    }

    applyFriction();

    boundaryCheck();

    if (!FrEnd.three_d) {
      this.pos.z = Coords.getInternalFromPixelCoords(400);
    }
  }

  /**
   * Generates a pretty explosion...
   */
  void explode() {
    if (FrEnd.explosions) {
      if (!FrEnd.xor) {
        final int _x = Coords.getXCoordsInternal(this.pos.x, this.pos.z);
        final int _y = Coords.getYCoordsInternal(this.pos.y, this.pos.z);

        final int _dx = Coords.getRadius(this.velocity.x, this.pos.z) << Coords.shift;
        final int _dy = Coords.getRadius(this.velocity.y, this.pos.z) << Coords.shift;

        final int _r = Coords.getRadius(this.type.radius, this.pos.z);

        for (int temp = 0; temp < TRIG_TAB_SIZE; temp += 16) {
          final int temp3 = NodeType.static_rnd.nextInt();
          int temp_radius = 0x100 + (temp3 & 0xff);
          int temp_x = temp3 >> 24;
          int temp_y = (temp3 << 8) >> 24;

          ParticleManager.add(_x - cos_tab[temp] * this.type.radius, _y
            - sin_tab[temp] * _r, _dx
            - ((cos_tab[temp] * temp_radius) >> lut_shift) + temp_x, _dy
            - ((sin_tab[temp] * temp_radius) >> lut_shift) + temp_y,
            Particle.POINT);
          //temp++;
          temp_radius = 0x100 + ((temp3 >> 8) & 0xff);
          temp_x = (temp3 << 22) >> 24;
          temp_y = (temp3 << 18) >> 24;

          ParticleManager.add(_x - cos_tab[temp] * this.type.radius, _y
            - sin_tab[temp] * _r, _dx
            - ((cos_tab[temp] * temp_radius) >> lut_shift) + temp_x, _dy
            - ((sin_tab[temp] * temp_radius) >> lut_shift) + temp_y,
            Particle.POINT);

          temp++;
          temp_radius = 0x100 + ((temp3 >> 10) & 0xff);
          temp_x = (temp3 << 12) >> 24;
          temp_y = (temp3 << 4) >> 24;

          ParticleManager.add(_x - cos_tab[temp] * this.type.radius, _y
            - sin_tab[temp] * _r, _dx
            - ((cos_tab[temp] * temp_radius) >> lut_shift) + temp_x, _dy
            - ((sin_tab[temp] * temp_radius) >> lut_shift) + temp_y,
            Particle.CROSS);
          //temp++;
          temp_radius = 0x100 + ((temp3 >> 12) & 0xff);
          temp_x = (temp3 << 16) >> 24;
          temp_y = (temp3 << 20) >> 24;

          ParticleManager.add(_x - cos_tab[temp] * this.type.radius, _y
            - sin_tab[temp] * _r, _dx
            - ((cos_tab[temp] * temp_radius) >> lut_shift) + temp_x, _dy
            - ((sin_tab[temp] * temp_radius) >> lut_shift) + temp_y,
            Particle.BLOB);
        }

        final int temp5 = ((20 * 256) / this.type.radius) + 1;

        for (int temp = 0; temp <= (TRIG_TAB_SIZE - temp5); temp = temp + temp5) {
          final int temp4 = (temp + temp5) & TRIG_TAB_SIZEMO;
          final int temp3 = NodeType.static_rnd.nextInt();
          final int temp_radius = temp3 & 0x1ff;
          final int temp_x = 0;
          final int temp_y = 0;

          LineFragmentManager.add(_x - cos_tab[temp] * _r, _y - sin_tab[temp]
            * _r, _x - cos_tab[temp4] * _r, _y - sin_tab[temp4] * _r, _dx
            - ((cos_tab[temp] * temp_radius) >> lut_shift) + temp_x, _dy
            - ((sin_tab[temp] * temp_radius) >> lut_shift) + temp_y, 1024);
        }
      }
    }
  }

  void capVelocities() {
    if (FrEnd.boundaries) {
      boundaryCheck();
    }
  }

  public void boundaryCheck() {
    final int radius = this.type.radius;
    if ((this.pos.x + radius) > (Coords.x_pixels << Coords.shift)) {
      this.pos.x = (Coords.x_pixels << Coords.shift) - radius;
      if (this.velocity.x > 0) {
        this.velocity.x = -(int) (this.velocity.x * 0.95);
      }
    }

    if (this.pos.x < radius) {
      this.pos.x = radius;
      if (this.velocity.x < 0) {
        this.velocity.x = -(int) (this.velocity.x * 0.95);
      }
    }

    if ((this.pos.y + radius) > (Coords.y_pixels << Coords.shift)) {
      this.pos.y = (Coords.y_pixels << Coords.shift) - radius;
      if (this.velocity.y > 0) {
        this.velocity.y = -(int) (this.velocity.y * 0.95);
      }
    }

    if (this.pos.y < radius) {
      this.pos.y = radius;
      if (this.velocity.y < 0) {
        this.velocity.y = -(int) (this.velocity.y * 0.95);
      }
    }

    if ((this.pos.z + radius) > (Coords.z_pixels << Coords.shift)) {
      this.pos.z = (Coords.z_pixels << Coords.shift) - radius;
      if (this.velocity.z > 0) {
        this.velocity.z = -(int) (this.velocity.z * 0.95);
      }
    }

    if (this.pos.z < radius) {
      this.pos.z = radius;
      if (this.velocity.z < 0) {
        this.velocity.z = -(int) (this.velocity.z * 0.95);
      }
    }
  }

  void applyFriction() {
    // cap velocities...
    if (this.velocity.x > max_speed) {
      this.velocity.x = max_speed;
    } else {
      if (this.velocity.x < -max_speed) {
        this.velocity.x = -max_speed;
      }
    }

    if (this.velocity.y > max_speed) {
      this.velocity.y = max_speed;
    } else {
      if (this.velocity.y < -max_speed) {
        this.velocity.y = -max_speed;
      }
    }

    if (this.velocity.z > max_speed) {
      this.velocity.z = max_speed;
    } else {
      if (this.velocity.z < -max_speed) {
        this.velocity.z = -max_speed;
      }
    }

    //    if ((this.status & ConstantsNode.DRAG) != 0) {
    //      // if ((this.status & ConstantsNode.ACTIVE_FEET) != 0) {
    //      // this.velocity.x = this.velocity.x >> 6;
    //      // this.velocity.y = this.velocity.y >> 6;
    //      // this.velocity.z = this.velocity.z >> 6;
    //      // } else {
    //      if (friction_strength > 0) {
    //        this.velocity.x = (this.velocity.x * (256 - friction_strength)) >> 8;
    //        this.velocity.y = (this.velocity.y * (256 - friction_strength)) >> 8;
    //        this.velocity.z = (this.velocity.z * (256 - friction_strength)) >> 8;
    //      }
    //    }
    //}
  }

  public final void findNewBin() {
    if (!FrEnd.oscd) {
      final int new_bin_x = this.pos.x >>> GridOfBinsForCachedNodes.log2binsize;
      final int new_bin_y = this.pos.y >>> GridOfBinsForCachedNodes.log2binsize;
      final int new_bin_z = this.pos.z >>> GridOfBinsForCachedNodes.log2binsize;

      if ((this.current_bin.x != new_bin_x)
        || (this.current_bin.y != new_bin_y)) {
        GridOfBinsForCachedNodes.addToList(new_bin_x, new_bin_y, new_bin_z,
          this);
        GridOfBinsForCachedNodes.removeFromList(this.current_bin.x,
          this.current_bin.y, this.current_bin.z, this);

        this.current_bin.x = new_bin_x;
        this.current_bin.y = new_bin_y;
        this.current_bin.z = new_bin_z;
      }
    }
  }

  final void removeFromBin() {
    if (!FrEnd.oscd) { //!?
      GridOfBinsForCachedNodes.removeFromList(this.current_bin.x,
        this.current_bin.y, this.current_bin.z, this);
    }
  }

  /**
   * Sets into motion a chain of events that will result in the destruction of
   * this node, and - all the other objects it is attached to...
   */
  public void simplyKill() {
    explode();

    temp_private_world.link_manager.killAllLinks(this);

    temp_private_world.killThisNode(this);

    BinGrid.repaint_all_objects = true;
  }

//  public void addLink(int link_number) {
//    final int n = getNumberOfLinks();
//    int[] new_list_of_links = new int[n + 1];
//    for (int i = 0; i < n; i++) {
//      new_list_of_links[i] = this.list_of_links[i];
//    }
//    new_list_of_links[n] = link_number;
//    this.list_of_links = new_list_of_links;
//  }
  
//  public void removeLink(int link_number) {
//    if (!hasThisLink(link_number)) {
//      Log.log("Warning: node.removeLink called for unknown link");
//    }
//  }
}
