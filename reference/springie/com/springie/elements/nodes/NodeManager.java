//This code has been placed into the public domain by its author

package com.springie.elements.nodes;

import com.springie.FrEnd;
import com.springie.elements.electrostatics.ElectrostaticRepulsion;
import com.springie.elements.nodegrid.GridOfBinsForCachedNodes;
import com.springie.geometry.Point3D;
import com.springie.geometry.Vector3D;
import com.springie.messages.ArgumentList;
import com.springie.modification.post.PostModification;
import com.springie.modification.translation.CentreOnScreen;
import com.springie.modification.velocity.DampOverallVelocities;
import com.springie.presets.PresetObjects;
import com.springie.render.CachedNode;
import com.springie.render.Coords;
import com.springie.utilities.log.Log;
import com.springie.utilities.math.SquareRoot;
import com.springie.world.World;


/**
 * The class that manages the particular collection of entities that is
 * universal - and is not confined to a particular egg.
 */
public class NodeManager extends World {

  static int max_poss_dyn_size = 500;

  static int min_poss_dyn_size = 10;

  static Node min_x_node;
  static Node max_x_node;

  static Node min_y_node;
  static Node max_y_node;

  static int general_size = 256;

  public ElectrostaticRepulsion electrostatic = new ElectrostaticRepulsion();

  public static int origin_shift_x;

  public static int origin_shift_y;

  public static int eye_distance = 7000;

  public static int mask_left = 0xFF0000;

  public static int mask_right = 0x00FFFF;

  public boolean nodes_have_been_deleted;

  public NodeManager() {
    super();
  }

  public final void initial() {
    initialSetUp();

    if (!FrEnd.module) {
      addCreature();
    }
  }

  public final void initialWithPreset(ArgumentList preset_type) {
    initialSetUp();

    addCreatureFromPresetObject(preset_type);
  }

  public void initialSetUp() {
    set(null);
    initial_reset();
    resetNodeGrid(); // first of all...

    NodeManager.max_poss_dyn_size = Integer.MAX_VALUE;
    NodeManager.min_poss_dyn_size = 10;
  }

  public final void add() {
    addCreature(); // 64 + World.rnd.nextInt(128));
  }

  //addPresetObject(int size, Object preset_object) {
  //addCreatureFromPresetObject(size, preset_object);
  //}

  private void addCreature() {
    final String desc = FrEnd.choose_initial.choice.getSelectedItem();
    
    final String location = (String) FrEnd.choose_initial.hashtable.get(desc);
    
    addCreatureFromLocation(location);
  }

  void addCreatureFromPresetObject(ArgumentList preset_type) {
    String d = null;
    d = PresetObjects.getCreatureDescription(preset_type);
    FrEnd.data_input.addFromString(d, FrEnd.node_manager);
  }

  public void addCreatureFromLocation(String location) {
    String d = null;
    if (FrEnd.archive != null) {
      if (!"".equals(FrEnd.archive)) {
        location = FrEnd.archive;
        FrEnd.last_file_path = location;
      }
    }
    
    try {
      d = PresetObjects.getCreatureDescription(location);
    } catch (Exception e) {
      Log.log("Problem getting information from location:" + location);
      e.printStackTrace();
    }
    
    FrEnd.data_input.addFromString(d, FrEnd.node_manager);
  }

  // called on resize...
  public final void resetNodeGrid() {
    final int gcd = (NodeManager.general_size + 4) << Coords.shift;

    int temp = 8;

    while (gcd > (1 << temp)) {
      temp++;
    }

    temp = 14;

    this.node_grid = new GridOfBinsForCachedNodes(temp);

    ensureEntitiesAreBinnedAgain();
  }

  final void ensureEntitiesAreBinnedAgain() {
    final int number_of_nodes = this.element.size();
    for (int temp = 0; temp < number_of_nodes; temp++) {
      final Node n = (Node) this.element.elementAt(temp);
      n.current_bin.x = -1;
      n.current_bin.y = -1;
      n.current_bin.z = -1;
    }
  }

  final void x_athonEdgeCheck() {
    //    if (FrEnd.x_athon) {
    //      int min_x = MainCanvas.lower_quarter_x;
    //      int min_y = MainCanvas.lower_quarter_y;
    //      int max_x = MainCanvas.upper_quarter_x;
    //      int max_y = MainCanvas.upper_quarter_y;
    //
    //      NodeManager.min_x_node = null;
    //      NodeManager.max_x_node = null;
    //      NodeManager.min_y_node = null;
    //      NodeManager.max_y_node = null;
    //
    //      int _temp_x;
    //      int _temp_y;
    //
    //      // establish max and min values...
    //      final int number_of_nodes = this.node.size();
    //      for (int temp = number_of_nodes; --temp >= 0;) {
    //        final Node temp_agent = (Node) this.node.elementAt(temp);
    //        if (temp_agent.creature != null) {
    //          _temp_x = temp_agent.pos.x;
    //          _temp_y = temp_agent.pos.y;
    //          if (_temp_x > max_x) {
    //            max_x = _temp_x;
    //            NodeManager.max_x_node = temp_agent;
    //          } else {
    //            if (_temp_x < min_x) {
    //              min_x = _temp_x;
    //              NodeManager.min_x_node = temp_agent;
    //            }
    //          }
    //
    //          if (_temp_y > max_y) {
    //            max_y = _temp_y;
    //            NodeManager.max_y_node = temp_agent;
    //          } else {
    //            if (_temp_y < min_y) {
    //              min_y = _temp_y;
    //              NodeManager.min_y_node = temp_agent;
    //            }
    //          }
    //        }
    //      }
    //
    //      NodeManager.origin_shift_x = 0;
    //      NodeManager.origin_shift_y = 0;
    //    }
  }

  /*
   * public final void bufferedUpdate() { x_athonEdgeCheck(); if
   * (FrEnd.links_are_active) { if (number_of_links > 0) {
   * link_manager.scrubTheLinks(); link_manager.exerciseTheLinks(); } }
   * 
   * if (number_of_nodes > 0) { scrubTheAgents(); if (!FrEnd.paused) {
   * travelTheAgents(); } }
   * 
   * if (FrEnd.links_are_active) { if (number_of_links > 0) {
   * link_manager.drawTheLinks(); } }
   * 
   * apply_acceleration();
   * 
   * if (number_of_nodes > 0) { drawTheAgents(); }
   * 
   * if (FrEnd.check_collisions) { if (!FrEnd.paused) { collisionCheck(); } } }
   */

  public final void mainAgentUpdate() {
    Node.temp_private_world = this;
    x_athonEdgeCheck();
    if (FrEnd.frame_frequency == 0) {
      updateAgentsAfterOneFrame();
    } else {
      updateAgentsAfterMoreThanOneFrameHasPassed();
    }
  }

  private void updateAgentsAfterOneFrame() {
    final int number_of_nodes = this.element.size();

    if (applyForces()) {
      final int n_o_l = this.link_manager.element.size();
      if (n_o_l > 0) {
        this.link_manager.performLinkUpdate();
      }
    }

    if (number_of_nodes > 0) {
      performNodeUpdate();
    }

    if (applyForces()) {
      applyAcceleration();
      if (FrEnd.check_collisions) {
        collisionCheck();
      }
    }

    nodeAndLinkRender();
  }

  boolean applyForces() {
    if (FrEnd.paused) {
      return false;
    }

    if (FrEnd.forces_disabled_during_gesture) {
      return false;
    }

    return true;
  }

  private void updateAgentsAfterMoreThanOneFrameHasPassed() {
    final int number_of_nodes = this.element.size();
    final int n_o_l = this.link_manager.element.size();
    if (number_of_nodes > 0) {
      scrubTheAgents();
    }

    for (int temp = 0; temp <= FrEnd.frame_frequency; temp++) {
      if (applyForces()) {
        if (number_of_nodes > 0) {
          travelTheAgents();

        }

        if (n_o_l > 0) {
          this.link_manager.performLinkUpdate();
        }

        applyAcceleration();
        if (FrEnd.check_collisions) {
          collisionCheck();
        }
      }
    }

    //if (number_of_nodes > 0) {
    //drawTheAgents();
    //}
  }

  //*this* is the important rendering code...
  private void performNodeUpdate() {
    // *should* do this in some sort of random order - to avoid artefacts...
    if (applyForces()) {
      final int number_of_nodes = this.element.size();
      for (int counter = number_of_nodes; --counter >= 0;) {
        ((Node) this.element.elementAt(counter)).travel();
      }
    }
  }

  private void nodeAndLinkRender() {
    sortIndex();

    if (FrEnd.render_anaglyph) {
      nodeAndLinkRenderAnaglyph();
    } else {
      nodeAndLinkRenderNormal();
    }
  }

  private void nodeAndLinkRenderNormal() {
    //Log.log("nodeAndLinkRenderNormal");
    final int number_of_nodes = this.element.size();
    this.renderer.renderer_node.ensureCapacity(number_of_nodes);
    final int mask = 0xFFFFFFFF;

    for (int counter = number_of_nodes; --counter >= 0;) {
      final int num = this.node_depth_index[counter];
      final Node node = (Node) this.element.elementAt(num);
      final CachedNode cached_node = this.renderer.renderer_node.array[num];
      if (FrEnd.render_nodes) {
        cached_node.renderNodes(node, mask);
      }

      if (FrEnd.render_links) {
        cached_node.renderLinks(this.renderer.renderer_link, this.link_manager,
          node, mask);
      }

      if (FrEnd.render_polygons) {
        cached_node.renderPolygons(this.renderer.renderer_face,
          this.face_manager, node, mask);
      }
    }
  }

  private void nodeAndLinkRenderAnaglyph() {
    int anaglyph_distance = NodeManager.eye_distance;
    final int number_of_nodes = this.element.size();

    this.renderer.renderer_node.ensureCapacity(number_of_nodes);
    this.renderer2.renderer_node.ensureCapacity(number_of_nodes);

    for (int counter = number_of_nodes; --counter >= 0;) {
      final int num = this.node_depth_index[counter];
      final Node n = (Node) this.element.elementAt(num);
      final CachedNode cn1 = this.renderer.renderer_node.array[num];
      final CachedNode cn2 = this.renderer2.renderer_node.array[num];
      if (FrEnd.render_nodes) {
        Coords.shift_constant_x -= anaglyph_distance;
        cn1.renderNodes(n, mask_right);
        Coords.shift_constant_x += anaglyph_distance << 1;
        cn2.renderNodes(n, mask_left);
        Coords.shift_constant_x -= anaglyph_distance;
      }

      if (FrEnd.render_links) {
        Coords.shift_constant_x -= anaglyph_distance;
        cn1.renderLinks(this.renderer.renderer_link, this.link_manager, n,
          mask_right);
        Coords.shift_constant_x += anaglyph_distance << 1;
        cn2.renderLinks(this.renderer2.renderer_link, this.link_manager, n,
          mask_left);
        Coords.shift_constant_x -= anaglyph_distance;
      }

      if (FrEnd.render_polygons) {
        Coords.shift_constant_x -= anaglyph_distance;
        cn1.renderPolygons(this.renderer.renderer_face, this.face_manager, n,
          mask_right);
        Coords.shift_constant_x += anaglyph_distance << 1;
        cn1.renderPolygons(this.renderer2.renderer_face, this.face_manager, n,
          mask_left);
        Coords.shift_constant_x -= anaglyph_distance;
      }
    }
  }

  private void setUpNewNodeDepthIndex() {
    //Log.log("EVERY TIME?"); // NO: fine...
    final int number_of_nodes = this.element.size();
    this.node_depth_index = new int[number_of_nodes];
    for (int temp = number_of_nodes; --temp >= 0;) {
      this.node_depth_index[temp] = temp;
    }
  }

  // to do - keep track of first and last flip...
  void sortIndex() {
    final int number_of_nodes = this.element.size();
    if (number_of_nodes != this.node_depth_index.length) {
      setUpNewNodeDepthIndex();
    }

    if (FrEnd.redraw_deepest_first) {
      performTheNodeSort();
    }
  }

  private void performTheNodeSort() {
    // perform a dimwitted bubble sort... TODO improve sort... 
    //Log.log("sortIndex");
    final int number_of_nodes = this.element.size();

    for (int i = number_of_nodes - 1; --i >= 0;) {
      boolean flipped = false;
      for (int j = 0; j <= i; j++) {
        final int k = j + 1;
        final int j1 = this.node_depth_index[j];
        final int k1 = this.node_depth_index[k];
        final Node a = (Node) this.element.elementAt(j1);
        final Node b = (Node) this.element.elementAt(k1);
        if (a.pos.z > b.pos.z) {
          int temp = this.node_depth_index[j];
          this.node_depth_index[j] = this.node_depth_index[k];
          this.node_depth_index[k] = temp;

          flipped = true;
        }
      }

      if (!flipped) {
        return;
      }
    }
  }

  final void agentExpansion() {
    final int number = this.node_type_factory.array.size();
    if (FrEnd.node_growth) {
      for (int temp = 0; temp < number; temp++) {
        final NodeType type = (NodeType) this.node_type_factory.array
          .elementAt(temp);
        if (type.radius < NodeManager.max_poss_dyn_size) {
          type.setSize(type.radius + 6);
        }
      }
    }

    if (FrEnd.continuously_centre) {
      CentreOnScreen.moveTowardsCentre(this);
      DampOverallVelocities.damp(this);
    }
  }

  public void collisionCheckNbyN() {
    final int number_of_nodes = this.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      temp_agent = (Node) this.element.elementAt(counter);

      for (int counter2 = counter + 1; counter2 < number_of_nodes; counter2++) {
        temp2_agent = (Node) this.element.elementAt(counter2);
        collideTheseEntities();
      }
    }
  }

  final void collisionCheck() {
    agentExpansion();

    //if (FrEnd.oscd) {
    collisionCheckNbyN();
    //} else {
    //findNewBins();
    //  collisionCheckUsingBins();
    //}
  }

  /*
   * private void collisionCheckUsingBins() { //findNewBins();
   * 
   * for (temp = 0; temp < number_of_nodes; temp++) { setUpTempAgent();
   * 
   * new_bin_x = temp_agent.current_bin_x; new_bin_y = temp_agent.current_bin_y;
   * new_bin_z = temp_agent.current_bin_z; // C list =
   * NodeGrid.node_list[new_bin_x][new_bin_y][new_bin_z]; temp_x0 =
   * list.getNumberInList(temp_agent) + 1; // temp_x0 = 0; //if (temp_x0 <
   * list.current_number) { for (temp2 = temp_x0; temp2 < (list.current_number);
   * temp2++) { temp2_agent = list.node[temp2];
   * 
   * collideTheseEntities(); } // W if (new_bin_x > 0) { list =
   * NodeGrid.node_list[new_bin_x - 1][new_bin_y]; for (temp2 = 0; temp2 <
   * (list.current_number); temp2++) { temp2_agent = list.node[temp2];
   * collideTheseEntities(); } // NW if (new_bin_y > 0) { list =
   * NodeGrid.node_list[new_bin_x - 1][new_bin_y - 1]; for (temp2 = 0; temp2 <
   * (list.current_number); temp2++) { temp2_agent = list.node[temp2];
   * collideTheseEntities(); } } // SW if (new_bin_y < (NodeGrid.ny - 1)) { list =
   * NodeGrid.node_list[new_bin_x - 1][new_bin_y + 1]; for (temp2 = 0; temp2 <
   * (list.current_number); temp2++) { temp2_agent = list.node[temp2];
   * collideTheseEntities(); } } } // U if (new_bin_y > 0) { list =
   * NodeGrid.node_list[new_bin_x][new_bin_y - 1]; for (temp2 = 0; temp2 <
   * (list.current_number); temp2++) { temp2_agent = list.node[temp2];
   * collideTheseEntities(); } } } }
   */

  // final void setUpTempAgent() {
  //if (temp < number_of_nodes) {
  //temp_agent = node[temp];
  //} else {
  //temp_agent = (Node) leader[temp - number_of_nodes];
  //}
  //}
  //final void setUpTemp2Agent() {
  //if (temp2 < number_of_nodes) {
  //temp2_agent = node[temp2];
  // } else {
  //  temp2_agent = (Node) leader[temp2 - number_of_nodes];
  //}
  //}
  public final void findNewBins() {
    final int number_of_nodes = this.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      temp_agent = (Node) this.element.elementAt(counter);

      int new_bin_x = temp_agent.pos.x >> GridOfBinsForCachedNodes.log2binsize;
      int new_bin_y = temp_agent.pos.y >> GridOfBinsForCachedNodes.log2binsize;
      int new_bin_z = temp_agent.pos.z >> GridOfBinsForCachedNodes.log2binsize;

      if (new_bin_x < 0) {
        new_bin_x = 0;
      }

      if (new_bin_y < 0) {
        new_bin_y = 0;
      }

      if (new_bin_z < 0) {
        new_bin_z = 0;
      }

      if ((temp_agent.current_bin.x != new_bin_x)
        || (temp_agent.current_bin.y != new_bin_y)
        || (temp_agent.current_bin.z != new_bin_z)) {
        GridOfBinsForCachedNodes.addToList(new_bin_x, new_bin_y, new_bin_z,
          temp_agent);
        GridOfBinsForCachedNodes.removeFromList(temp_agent.current_bin.x,
          temp_agent.current_bin.y, temp_agent.current_bin.z, temp_agent);

        temp_agent.current_bin.x = new_bin_x;
        temp_agent.current_bin.y = new_bin_y;
        temp_agent.current_bin.z = new_bin_z;
      }
    }
  }

  boolean sameSign(int a, int b) {
    return ((a ^ b) & 0x80000000) == 0;
  }

  final void applyAcceleration() {
    if (FrEnd.forces_disabled_during_gesture) {
      return;
    }

    FrEnd.node_manager.creature_manager.update();

    //findCoordsOfAttractionPoint();

    // apply acceleration...
    final int number_of_nodes = this.element.size();
    if (number_of_nodes > 0) {
      for (int temp = 0; temp < number_of_nodes; temp++) {
        temp_agent = (Node) this.element.elementAt(temp);
        applyGravity();
      }
    }
  }

  private void applyGravity() {
    if (gravity_active) {
      temp_agent.velocity.y += World.gravity_strength;
    }
  }

  // z ??
  public final byte arcTangent(int dx, int dy) {
    final int temp_x0 = World.rnd.nextInt();

    if ((dx < 0) && (dy < 0)) {
      if (dx < dy) {
        return (byte) ((temp_x0 & 0xF) + (temp_x0 >>> 31));
      }
      return (byte) ((temp_x0 & 0xF) + (temp_x0 >>> 31) + 0x10);
    }
    if ((dx < 0) && (dy >= 0)) {
      if (-dx < dy) {
        return (byte) ((temp_x0 & 0xF) + (temp_x0 >>> 31) + 0x60);
      }
      return (byte) (((temp_x0 & 0xF) + (temp_x0 >>> 31) + 0x70) & Node.TRIG_TAB_SIZEMO);
    }
    if ((dx >= 0) && (dy < 0)) {
      if (dx < -dy) {
        return (byte) ((temp_x0 & 0xF) + (temp_x0 >>> 31) + 0x20);
      }
      return (byte) ((temp_x0 & 0xF) + (temp_x0 >>> 31) + 0x30);
    }
    if (dx > dy) {
      return (byte) ((temp_x0 & 0xF) + (temp_x0 >>> 31) + 0x40);
    }
    return (byte) ((temp_x0 & 0xF) + (temp_x0 >>> 31) + 0x50);
  }

  // accepts temp_agent and temp2_agent
  public final void collideTheseEntities() {
    int tadx;
    int tady;
    int tadz;

    int temp_x;
    int temp_y;
    int temp_z;

    int temp_x0;
    int temp_y0;
    int temp_z0;

    int temp_x1;
    int temp_y1;
    int temp_z1;

    final int temp_detection_distance2 = temp_agent.type.radius
      + temp2_agent.type.radius;
    final int temp_detection_distance = temp_detection_distance2 >> Coords.shift;
    final Point3D pos = temp_agent.pos;
    final Point3D pos2 = temp2_agent.pos;

    temp_x0 = pos.x - pos2.x;
    temp_x1 = (temp_x0 < 0) ? -temp_x0 : temp_x0; // hmmm...
    if (temp_x1 < temp_detection_distance2) {

      temp_y0 = pos.y - pos2.y;
      temp_y1 = (temp_y0 < 0) ? -temp_y0 : temp_y0;
      if (temp_y1 < temp_detection_distance2) {

        temp_z0 = pos.z - pos2.z;
        temp_z1 = (temp_z0 < 0) ? -temp_z0 : temp_z0;
        //if (!FrEnd.three_d) {
        //}
        if (temp_z1 < temp_detection_distance2) {
          // probable collision...
          // outer diamond check...
          if (((temp_detection_distance2 + (temp_detection_distance2 >> 1)) - temp_x1) > temp_y1) {
            // make smaller to prevent overflow...
            tadx = temp_x1 >> Coords.shift;
            tady = temp_y1 >> Coords.shift;
            tadz = temp_z1 >> Coords.shift;

            int temp_radius = (tadx * tadx) + (tady * tady) + (tadz * tadz); // now
            // check using circle...
            // temp_detection_distance = temp_detection_distance >> shift;
            final int temp_detection_distance_squared = temp_detection_distance
              * temp_detection_distance;
            if (temp_radius < temp_detection_distance_squared) {
              // collision
              collide_with_self = (temp_agent.creature == temp2_agent.creature)
                && (temp_agent.creature != null);
              // sqrt - really? :-(
              temp_radius = SquareRoot.fastSqrt(1 + temp_radius);

              // find unit vector along collision path...
              // unit vector * 65536
              temp_x = temp_x0 / temp_radius;
              temp_y = temp_y0 / temp_radius;
              temp_z = temp_z0 / temp_radius;

              // compute the relative velocities...
              tadx = temp_agent.velocity.x - temp2_agent.velocity.x;
              tady = temp_agent.velocity.y - temp2_agent.velocity.y;
              tadz = temp_agent.velocity.z - temp2_agent.velocity.z;

              // dot product...
              int magnitude = -(((tadx * temp_x) + (tady * temp_y) + (tadz * temp_z)) >> Coords.shift);
              // magnitude = (magnitude < 0) ? -magnitude : magnitude; // ???
              if (magnitude > 0) {
                if (collide_with_self || !FrEnd.collide_self_only) {
                  int temp_count;
                  if (collide_with_self) {
                    temp_count = (temp_detection_distance - temp_radius) << 6; // !
                  } else {
                    temp_count = ((temp_detection_distance - temp_radius) << 8) // !
                      + World.minimum_magnitude;
                  }

                  if (temp_count > World.maximum_magnitude) {
                    temp_count = World.maximum_magnitude;
                  }

                  if (magnitude < temp_count) {
                    magnitude = temp_count;
                  }

                  // /very/ crude hack - since impulse calculations
                  // don't seem to be running yet...
                  temp_x = temp_x * magnitude;
                  temp_y = temp_y * magnitude;
                  temp_z = temp_z * magnitude;

                  final int temp_ma = temp_agent.type.log_mass
                    - temp2_agent.type.log_mass;

                  if (temp_ma < 0) {
                    tadx = temp_x >> -temp_ma;
                    tady = temp_y >> -temp_ma;
                    tadz = temp_z >> -temp_ma;

                    temp_agent.velocity.x += tadx >> 9;
                    temp_agent.velocity.y += tady >> 9;
                    temp_agent.velocity.z += tadz >> 9;

                    temp2_agent.velocity.x -= ((temp_x << 1) - tadx) >> 9;
                    temp2_agent.velocity.y -= ((temp_y << 1) - tady) >> 9;
                    temp2_agent.velocity.z -= ((temp_z << 1) - tadz) >> 9;
                  } else {
                    tadx = temp_x >> temp_ma;
                    tady = temp_y >> temp_ma;
                    tadz = temp_z >> temp_ma;

                    temp_agent.velocity.x += ((temp_x << 1) - tadx) >> 9;
                    temp_agent.velocity.y += ((temp_y << 1) - tady) >> 9;
                    temp_agent.velocity.z += ((temp_z << 1) - tadz) >> 9;

                    temp2_agent.velocity.x -= tadx >> 9;
                    temp2_agent.velocity.y -= tady >> 9;
                    temp2_agent.velocity.z -= tadz >> 9;
                  }
                  shrinkNodesOnCollision();
                }
              }

              /*
               * temp_count = 12;
               * 
               * do { temp_agent.x += (tadx >> 4); temp_agent.y += (tady >> 4);
               * temp2_agent.x -= (tadx >> 4); temp2_agent.y -= (tady >> 4);
               * 
               * temp_x0 = temp_agent.x - temp2_agent.x; temp_x1 = (temp_x0 < 0) ?
               * -temp_x0 : temp_x0;
               * 
               * temp_y0 = temp_agent.y - temp2_agent.y; temp_y1 = (temp_y0 < 0) ?
               * -temp_y0 : temp_y0;
               * 
               * temp_x1) + (temp_y1 * temp_y1); // now check using circle... }
               * while ((temp_radius < detection_radius_squared) &&
               * (temp_count-- > 0));
               */

              /*
               * temp_agent.x += temp_agent.dx; temp_agent.y += temp_agent.dy;
               * temp2_agent.x += temp2_agent.dx; temp2_agent.y +=
               * temp2_agent.dy;
               */

            }
          }
        }
      }
    }
  }

  private void shrinkNodesOnCollision() {
    if (FrEnd.node_growth) {
      if (temp_agent.type.radius > NodeManager.min_poss_dyn_size) {
        temp_agent.type.setSize(temp_agent.type.radius - 10);
      }
      if (temp2_agent.type.radius > NodeManager.min_poss_dyn_size) {
        temp2_agent.type.setSize(temp2_agent.type.radius - 10);
      }
    }
  }

  // picker algorithm - simple - and slow...
  final Node isThereOne2(int _x, int _y) {
    //Log.log("isThereOne:" + _x + "," + _y);
    int tadx;
    int tady;
    Node candidate = null;

    final int number_of_nodes = this.element.size();
    for (int temp = 0; temp < number_of_nodes; temp++) {
      temp_agent = (Node) this.element.elementAt(temp);
      if (!temp_agent.type.hidden || FrEnd.render_hidden_nodes) {
        final int temp_detection_distance = Coords.getRadiusInternal(
          temp_agent.type.radius, temp_agent.pos.z);
        final int temp_x0 = Coords.getXCoordsInternal(temp_agent.pos.x,
          temp_agent.pos.z)
          - _x;
        final int temp_x1 = (temp_x0 < 0) ? -temp_x0 : temp_x0;
        if (temp_x1 < temp_detection_distance) {
          final int temp_y0 = Coords.getYCoordsInternal(temp_agent.pos.y,
            temp_agent.pos.z)
            - _y;
          final int temp_y1 = (temp_y0 < 0) ? -temp_y0 : temp_y0;
          //Log.log("temp_y1:" + temp_y1);
          //Log.log("temp_detection_distance:" + temp_detection_distance);
          if (temp_y1 < temp_detection_distance) {
            // probable collision...
            // make smaller to prevent overflow...
            tadx = temp_x1 >> Coords.shift;
            tady = temp_y1 >> Coords.shift;
            final int temp_radius = (tadx * tadx) + (tady * tady);
            // check using circle...
            final int tadx2 = temp_agent.type.radius >> Coords.shift;
            final int temp_detection_distance_squared = tadx2 * tadx2;
            if (temp_radius < temp_detection_distance_squared) {
              //Log.log("temp_detection_distance:" + temp_detection_distance);
              if ((candidate == null) || (candidate.pos.z > temp_agent.pos.z)) {
                candidate = temp_agent;
              }
            }
          }
        }
      }
    }

    return candidate;
  }

  // picker algorithm choice...
  public final Node isThereOne(int x, int y) {
    //if (FrEnd.oscd) {
    return isThereOne2(x, y);
    //} else {
    //  return isThereOne1(x, y);
    //}
  }

  public final void setRadiusOfSelected(int radius) {
    final int n_o_n = this.element.size();
    for (int temp = n_o_n; --temp >= 0;) {
      final Node node = (Node) this.element.elementAt(temp);
      if (node.type.selected) {
        node.type.radius = radius;
      }
    }
  }

  public final void setChargeOfSelected(int charge) {
    final int n_o_n = this.element.size();
    for (int temp = n_o_n; --temp >= 0;) {
      final Node node = (Node) this.element.elementAt(temp);
      if (node.type.selected) {
        node.type.charge = charge;
      }
    }

    new PostModification(this).cleanup();
  }

  /*
   * // picker algorithm - complex and quick... // not well maintained...
   * private Node isThereOne1(int x, int y) { new_bin_x = x >>>
   * NodeGrid.log2binsize; new_bin_y = y >>> NodeGrid.log2binsize;
   * 
   * if (new_bin_x < 0) new_bin_x = 0;
   * 
   * if (new_bin_x >= NodeGrid.nx) new_bin_x = NodeGrid.nx - 1;
   * 
   * if (new_bin_y < 0) new_bin_y = 0;
   * 
   * if (new_bin_y >= NodeGrid.ny) new_bin_y = NodeGrid.ny - 1;
   * 
   * list = NodeGrid.node_list[new_bin_x][new_bin_y][new_bin_z];
   * 
   * temp_agent = checkThisList(x, y); if (temp_agent != null) { return
   * temp_agent; }
   * 
   * if (new_bin_x > 0) { list = NodeGrid.node_list[new_bin_x - 1][new_bin_y];
   * 
   * temp_agent = checkThisList(x, y); if (temp_agent != null) { return
   * temp_agent; }
   * 
   * if (new_bin_y > 0) { list = NodeGrid.node_list[new_bin_x - 1][new_bin_y -
   * 1];
   * 
   * temp_agent = checkThisList(x, y); if (temp_agent != null) { return
   * temp_agent; } }
   * 
   * if (new_bin_y < (NodeGrid.ny - 1)) { list = NodeGrid.node_list[new_bin_x -
   * 1][new_bin_y + 1];
   * 
   * temp_agent = checkThisList(x, y); if (temp_agent != null) { return
   * temp_agent; } } }
   * 
   * if (new_bin_x < (NodeGrid.nx - 1)) { list = NodeGrid.node_list[new_bin_x +
   * 1][new_bin_y];
   * 
   * temp_agent = checkThisList(x, y); if (temp_agent != null) { return
   * temp_agent; }
   * 
   * if (new_bin_y > 0) { list = NodeGrid.node_list[new_bin_x + 1][new_bin_y -
   * 1];
   * 
   * temp_agent = checkThisList(x, y); if (temp_agent != null) { return
   * temp_agent; } }
   * 
   * if (new_bin_y < (NodeGrid.ny - 1)) { list = NodeGrid.node_list[new_bin_x +
   * 1][new_bin_y + 1];
   * 
   * temp_agent = checkThisList(x, y); if (temp_agent != null) { return
   * temp_agent; } } } // D if (new_bin_y > 0) { list =
   * NodeGrid.node_list[new_bin_x][new_bin_y - 1];
   * 
   * temp_agent = checkThisList(x, y); if (temp_agent != null) { return
   * temp_agent; } } // U if (new_bin_y < (NodeGrid.ny - 1)) { list =
   * NodeGrid.node_list[new_bin_x][new_bin_y + 1];
   * 
   * temp_agent = checkThisList(x, y); if (temp_agent != null) { return
   * temp_agent; } } return null; }
   */

  /*
   * Node checkThisListx(int _x, int _y) { int tadx; int tady; //int tadz;
   * 
   * for (int temp = 0; temp < NodeManager.list.current_number; temp++) {
   * temp_agent = NodeManager.list.node[temp]; // temp_detection_distance =
   * ((int)temp_agent.detection_radius) < < shift; final int
   * temp_detection_distance = Coords.getRadius(temp_agent.type.radius,
   * temp_agent.pos.z); // temp_x0 = temp_agent.x - x; final int temp_x0 =
   * Coords.getXCoordsInternal(temp_agent.pos.x, temp_agent.pos.z) - _x; final
   * int temp_x1 = (temp_x0 < 0) ? -temp_x0 : temp_x0; if (temp_x1 <
   * temp_detection_distance) { // temp_y0 = temp_agent.y - y; final int temp_y0 =
   * Coords.getYCoordsInternal(temp_agent.pos.y, temp_agent.pos.z) - _y;
   * 
   * final int temp_y1 = (temp_y0 < 0) ? -temp_y0 : temp_y0; if (temp_y1 <
   * temp_detection_distance) { // probable collision... // make smaller to
   * prevent overflow... tadx = temp_x1 >> Coords.shift; tady = temp_y1 >>
   * Coords.shift; temp_radius = (tadx * tadx) + (tady * tady); // now // check
   * using circle... final int temp_detection_distance_squared =
   * (temp_agent.type.radius >> Coords.shift) (temp_agent.type.radius >>
   * Coords.shift); // emp_detection_distance_squared *=
   * temp_detection_distance_squared; // // temp_detection_distance *
   * temp_detection_distance; if (temp_radius < temp_detection_distance_squared) { //
   * collision return temp_agent; } } } }
   * 
   * return null; }
   */

  //public void drawTheAgents() {
  //ShoalsGrid.last_colour = -1;
  // Shoals_Grid.graphics_handle.setColor(Shoals_Grid.agent_colour);
  //for (counter = 0; counter < number_of_nodes; counter++) {
  //node[counter].draw();
  //}
  //NodeGrid.drawAgents();
  //}
  //  public void scrubTheAgents() {
  //    final int number_of_nodes = this.element.size();
  //    BinGrid.graphics_handle.setColor(BinGrid.bg_colour);
  //    for (int counter = 0; counter < number_of_nodes; counter++) {
  //      final Node node = (Node) this.element.elementAt(counter);
  //      ((CachedNode)
  // this.renderer.renderer_node.elements.elementAt(counter)).scrub(node);
  //    }
  //  }
  public void travelTheAgents() {
    Node.temp_private_world = this;
    final int number_of_nodes = this.element.size();
    // for (agent_counter = 0; agent_counter < number_of_agents;
    // agent_counter++) {
    for (int counter = number_of_nodes; --counter >= 0;) {
      ((Node) this.element.elementAt(counter)).travel();
    }
  }

  /*
   * final void drawTheLeaders() { //ShoalsGrid.last_colour = -1; for (counter =
   * 0; counter < number_of_leaders; counter++) { leader[counter].draw(); } }
   * 
   * final void travelTheLeaders() { Node.temp_private_world = this; // for
   * (agent_counter = 0; agent_counter < number_of_leaders; // agent_counter++) {
   * for (counter = number_of_leaders; --counter >= 0;) {
   * leader[counter].travel(); } }
   * 
   * final void scrubTheLeaders() {
   * ShoalsGrid.graphics_handle.setColor(ShoalsGrid.bg_colour); for (counter =
   * 0; counter < number_of_leaders; counter++) { leader[counter].scrub(); }
   */

  public void setGlobalSize(int d) {
    NodeManager.general_size = d;

    final int number_of_nodes = this.element.size();
    for (int temp = 0; temp < number_of_nodes; temp++) {
      ((Node) this.element.elementAt(temp)).type.setSize(d);
    }

    //for (int temp = 0; temp < Node.TRIG_TAB_SIZE; temp++) {
    //Node.ship_image_array[temp] = null;
    //}
  }

  //void setGlobalEggSize(int d) {
  // general_eggsize = d;

  // for (counter = 0; counter < number_of_leaders; counter++) {
  //   leader[counter].setSize(d);
  // }

  //Node.ring_image = null;
  // }

  //public final void changeNumber() {
  //}

  /*
   * final int number_of_nodes = node.size(); if
   * (NodeManager.target_number_of_agents != number_of_nodes) { while
   * (NodeManager.target_number_of_agents > number_of_nodes) { addNewAgent(); }
   * 
   * while (NodeManager.target_number_of_agents < number_of_nodes) {
   * killAgent(); }
   * 
   * resetNodeGrid(); } }/* /* final void changeLeaderNumber() { if
   * ((target_number_of_leaders != number_of_leaders)) { while
   * (target_number_of_leaders > number_of_leaders) { addNewLeader(); }
   * 
   * while (target_number_of_leaders < number_of_leaders) { killLeader(); }
   * 
   * resetNodeGrid(); } } // wastes memory, but who cares? public final void
   * addNewLeader() { number_of_leaders++;
   * 
   * new_array_of_leaders = new NodeLeader[number_of_leaders];
   * 
   * for (temp = 0; temp < (number_of_leaders - 1); temp++) {
   * new_array_of_leaders[temp] = leader[temp]; }
   * 
   * if (number_of_leaders > 1) { temp = rnd.nextInt(number_of_leaders - 1);
   * temp_x = ((Node) leader[temp]).x + rnd.nextInt(256); temp_y = ((Node)
   * leader[temp]).y + rnd.nextInt(256);
   * 
   * new_array_of_leaders[number_of_leaders - 1] = new NodeLeader(temp_x,
   * temp_y, rnd .nextInt()); ((Node) new_array_of_leaders[number_of_leaders -
   * 1]).colour = ((Node) leader[temp]).colour; ((Node)
   * new_array_of_leaders[number_of_leaders - 1]) .setSize(((Node)
   * leader[temp]).diameter); } else { temp_x = rnd.nextInt(Coords.x_pixels) < <
   * shift; temp_y = rnd.nextInt(Coords.y_pixels) < < shift;
   * 
   * new_array_of_leaders[number_of_leaders - 1] = new NodeLeader(temp_x,
   * temp_y, rnd .nextInt()); }
   * 
   * leader = new_array_of_leaders; }
   */

  //  public void killAgent() {
  //    final int number_of_nodes = this.node.size();
  //    if (number_of_nodes > 0) {
  //      this.link_manager.killAllLinks((Node) this.node.lastElement());
  //    }
  //
  //  }
  public final boolean killThisNode(Node e) {
    final int number_of_nodes = this.element.size();
    for (int temp = 0; temp < number_of_nodes; temp++) {
      if (this.element.elementAt(temp) == e) {
        killNumberedAgent(temp);
        return true;
      }
    }

    return false;
  }

  public void killNumberedAgent(int n) {
    final Node node = (Node) this.element.elementAt(n);
    this.link_manager.killAllLinks(node);
    this.face_manager.killAllPolygons(node);
    this.element.removeElementAt(n);

    // IMPORTANT: remove it from the bins...
    node.removeFromBin();
    
    this.nodes_have_been_deleted = true;
  }

  public Node getSelectedNode() {
    final int number_of_nodes = this.element.size();
    for (int temp = 0; temp < number_of_nodes; temp++) {
      //number_of_nodes--;
      final Node n = (Node) this.element.elementAt(temp);

      if (n.type.selected) {
        return n;
      }
    }

    return null;
  }

  public void deselectAll() {
    final int number_of_nodes = this.element.size();
    for (int temp = 0; temp < number_of_nodes; temp++) {
      final Node n = (Node) this.element.elementAt(temp);
      if (n.type.selected) {
        n.type.selected = false;
      }
    }

    FrEnd.updateGUIToReflectSelectionChange();
  }

  public void deselectAll(int colour) {
    final int number_of_nodes = this.element.size();
    for (int temp = 0; temp < number_of_nodes; temp++) {
      final Node n = (Node) this.element.elementAt(temp);
      if (n.clazz.colour == colour) {
        if (n.type.selected) {
          n.type.selected = false;
          //BinGrid.RepaintAll = true;
        }
      }
    }
    FrEnd.updateGUIToReflectSelectionChange();
  }

  public void selectAll() {
    final int number_of_nodes = this.element.size();
    for (int temp = 0; temp < number_of_nodes; temp++) {
      final Node node = (Node) this.element.elementAt(temp);
      if (!node.type.hidden || FrEnd.render_hidden_nodes) {
        if (!node.type.selected) {
          node.type.selected = true;
        }
      }
    }
    FrEnd.updateGUIToReflectSelectionChange();
  }

  public void selectAll(int colour) {
    final int number_of_nodes = this.element.size();
    for (int temp = 0; temp < number_of_nodes; temp++) {
      final Node node = (Node) this.element.elementAt(temp);
      if (node.clazz.colour == colour) {
        if (!node.type.hidden || FrEnd.render_hidden_nodes) {
          node.type.selected = true;
        }
      }
    }
    FrEnd.updateGUIToReflectSelectionChange();
  }

  public void deleteSelected() {
    final int number_of_nodes = this.element.size();
    for (int temp2 = number_of_nodes; --temp2 >= 0;) {
      final Node n = (Node) this.element.elementAt(temp2);
      if (n.type.selected) {
        killNumberedAgent(temp2);
      }
    }
    FrEnd.updateGUIToReflectSelectionChange();
  }

  public final void setColourOfSelected(int c) {
    boolean clean_up = false;
    final int number_of_nodes = this.element.size();
    for (int temp2 = 0; temp2 < number_of_nodes; temp2++) {
      final Node n = (Node) this.element.elementAt(temp2);
      if (n.type.selected) {
        n.clazz.colour = (char) c;
        clean_up = true;
      }
    }
    if (clean_up) {
      FrEnd.postCleanup();
    }
  }

  public void setSizeOfSelected(int s) {
    boolean clean_up = false;
    final int number_of_nodes = this.element.size();
    for (int temp2 = 0; temp2 < number_of_nodes; temp2++) {
      final Node n = (Node) this.element.elementAt(temp2);
      if (n.type.selected) {
        n.type.setSize(s);
        clean_up = true;
      }
    }
    if (clean_up) {
      FrEnd.postCleanup();
    }
  }

  public void moveSelection(int _dx, int _dy) {
    final int number_of_nodes = this.element.size();
    for (int temp2 = 0; temp2 < number_of_nodes; temp2++) {
      final Node n = (Node) this.element.elementAt(temp2);
      if (n.type.selected) {
        n.pos.x += _dx;
        n.pos.y += _dy;
        n.velocity.x = 0;
        n.velocity.y = 0;

        n.boundaryCheck();

        // make sure bins are updated...
        n.findNewBin();
      }
    }
  }

  public Point3D getCentre() {
    long x = 0;
    long y = 0;
    long z = 0;

    int number_of_nodes = this.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) this.element.elementAt(counter);
      x += candidate.pos.x;
      y += candidate.pos.y;
      z += candidate.pos.z;
    }

    if (number_of_nodes == 0) {
      number_of_nodes = 1;
    }

    return new Point3D((int) x / number_of_nodes, (int) y / number_of_nodes,
      (int) z / number_of_nodes);
  }

  public void applyViscousDrag() {
    if (Node.viscocity != 0) {
      final int number_of_nodes = this.element.size();
      final int vd = 1024 - Node.viscocity;

      for (int counter = number_of_nodes; --counter >= 0;) {
        final Node node = (Node) this.element.elementAt(counter);
        final Vector3D velocity = node.velocity;
        velocity.x = (velocity.x * vd + 512) >> 10;
        velocity.y = (velocity.y * vd + 512) >> 10;
        velocity.z = (velocity.z * vd + 512) >> 10;
      }
    }
  }

  public void makeSureNoClazzesOrTypesAreFlagged() {
    this.each_has_its_own_type = false;
    this.link_manager.each_has_its_own_type = false;
    this.face_manager.each_has_its_own_type = false;
    this.each_has_its_own_clazz = false;
  }

  public int getNumberOfSelectedNodes() {
    int number = 0;
    final int number_of_nodes = this.element.size();
    for (int temp = 0; temp < number_of_nodes; temp++) {
      final Node n = (Node) this.element.elementAt(temp);
      if (n.type.selected) {
        number++;
      }
    }

    return number;
  }
}