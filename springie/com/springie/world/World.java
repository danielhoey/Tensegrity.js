package com.springie.world;

import com.springie.FrEnd;
import com.springie.collisions.BinGrid;
import com.springie.composite.CompositeManager;
import com.springie.elements.base.BaseElementManager;
import com.springie.elements.clazz.Clazz;
import com.springie.elements.clazz.ClazzFactory;
import com.springie.elements.faces.Face;
import com.springie.elements.faces.FaceManager;
import com.springie.elements.faces.FaceType;
import com.springie.elements.links.Link;
import com.springie.elements.links.LinkManager;
import com.springie.elements.links.LinkType;
import com.springie.elements.nodegrid.GridOfBinsForCachedNodes;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeType;
import com.springie.elements.nodes.NodeTypeFactory;
import com.springie.geometry.Point3D;
import com.springie.render.Coords;
import com.springie.render.Renderer;
import com.springie.utilities.log.Log;
import com.springie.utilities.math.SquareRoot;
import com.springie.utilities.random.Hortensius32Fast;

/**
 * The class that manages collections of entities. <BR>
 * It can cover a collection of entities that are inside an egg - or the
 * collection of entities that is unconfined and fills all space.
 */
public class World extends BaseElementManager {
  public Renderer renderer = new Renderer();

  public Renderer renderer2 = new Renderer();

  public LinkManager link_manager;

  public FaceManager face_manager;

  public CompositeManager creature_manager;

  public Node associated_node;

  public NodeTypeFactory node_type_factory = new NodeTypeFactory();

  public ClazzFactory clazz_factory = new ClazzFactory();

  protected GridOfBinsForCachedNodes node_grid;

  public int[] node_depth_index = new int[0];

  public static int gravity_strength = 2;

  public static boolean gravity_active;

  protected static Node temp_agent;

  protected static Node temp2_agent;

  public static boolean collide_with_self; // used in collisions...

  public static int minimum_magnitude;

  public static int maximum_magnitude = 1 << 24; // 512; // 1024;

  protected static Hortensius32Fast rnd = new Hortensius32Fast();

  protected World() {
    init();
    set(null);
  }

  World(Node e) {
    init();
    set(e);
  }

  public final void initial_reset() {
    init();

    Node.initial();
  }

  void init() {
    this.element.removeAllElements();
    this.link_manager = new LinkManager();
    this.face_manager = new FaceManager();
    this.creature_manager = new CompositeManager();
    resetAll();
  }

  protected void set(Node e) {
    this.associated_node = e;
    resetAll();
  }

  public void initial() {
    set(null);
  }

  void resetAll() {
    reset();
    this.link_manager.reset();
    this.face_manager.reset();
    this.creature_manager.reset();
  }

  public Node copy(Node e) {
    temp_agent = new Node(e, this.node_type_factory, this.clazz_factory);
    this.element.addElement(temp_agent);

    return temp_agent;
  }

  //TODO
  //  public void bufferedUpdate() {
  //    Link.temp_private_world = this;
  //    this.link_manager.scrubTheLinks();
  //    this.link_manager.exerciseTheLinks();
  //
  //    final int number_of_nodes = this.element.size();
  //
  //    if (number_of_nodes > 0) {
  //      scrubTheAgents();
  //      if (!FrEnd.paused) {
  //        travelTheAgents();
  //      }
  //    }
  //
  //    this.link_manager.drawTheLinks();
  //
  //    if (number_of_nodes > 0) {
  //      drawTheAgents();
  //    }
  //
  //    if (FrEnd.check_collisions) {
  //      if (!FrEnd.paused) {
  //        collisionCheckNbyN();
  //      }
  //    }
  //  }

  void unbufferedUpdate() {
    final int number_of_nodes = this.element.size();
    Node.temp_private_world = this;
    if (FrEnd.frame_frequency == 0) {
      if (number_of_nodes > 0) {
        for (int counter = number_of_nodes; --counter >= 0;) {
          temp_agent = (Node) this.element.elementAt(counter);

          BinGrid.colourZero();
          // temp_agent.scrub(); //TODO

          if (!FrEnd.paused) {
            temp_agent.travel();
            confine();
          }

          //temp_agent.draw(); //TODO
        }
      }

      Link.temp_private_world = this;
      this.link_manager.performLinkUpdate();

      if (!FrEnd.paused) {
        if (FrEnd.check_collisions) {
          collisionCheckNbyN();
        }
      }
    } else {
      if (number_of_nodes > 0) {
        scrubTheAgents();
      }

      for (int temp = 0; temp <= FrEnd.frame_frequency; temp++) {
        if (!FrEnd.paused) {
          if (number_of_nodes > 0) {
            travelTheAgents();
          }

          Link.temp_private_world = this;
          this.link_manager.performLinkUpdate();

          if (FrEnd.check_collisions) {
            collisionCheckNbyN();
          }
        }
      }

      if (number_of_nodes > 0) {
        drawTheAgents();
      }

      for (int temp = 0; temp <= FrEnd.frame_frequency; temp++) {
        if (!FrEnd.paused) {
          if (FrEnd.check_collisions) {
            collisionCheckNbyN();
          }
        }
      }
    }
  }

  public void collisionCheckNbyN() {
    final int number_of_nodes = this.element.size();
    for (int temp = 0; temp < (number_of_nodes - 1); temp++) {
      World.temp_agent = (Node) this.element.elementAt(temp);
      for (int temp2 = temp + 1; temp2 < number_of_nodes; temp2++) {
        World.temp2_agent = (Node) this.element.elementAt(temp2);
        FrEnd.node_manager.collideTheseEntities();
      }
    }
  }

  public void drawTheAgents() {
    final int number_of_nodes = this.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      //((Node) this.element.elementAt(counter)).draw();//TODO
    }
  }

  public void scrubTheAgents() {
    final int number_of_nodes = this.element.size();
    for (int counter = 0; counter < number_of_nodes; counter++) {
      //((Node) this.element.elementAt(counter)).scrub();//TODO
    }
  }

  public void scrub() {
    scrubTheAgents(); // problems...
  }

  public void travelTheAgents() {
    final int number_of_nodes = this.element.size();
    Node.temp_private_world = this;
    for (int counter = number_of_nodes; --counter >= 0;) {
      temp_agent = (Node) this.element.elementAt(counter);
      temp_agent.travel();
      confine();
    }
  }

  public void setGlobalSize(int d) {
    final int number_of_nodes = this.element.size();

    for (int temp = 0; temp < number_of_nodes; temp++) {
      temp_agent = (Node) this.element.elementAt(temp);
      temp_agent.type.setSize((byte) d);
    }
  }

  // wastes memory, but who cares?
  /**
   * Adds a new Node to this world
   */
  public Node addNewAgent() {
    temp_agent = new Node(new Point3D(0, 0, 0), rnd.nextInt(),
      this.node_type_factory);
    this.element.addElement(temp_agent);

    return temp_agent;
  }

  public Node addNewAgent(Point3D pos, Clazz clazz, NodeType type) {
    temp_agent = new Node(new Point3D(pos), rnd.nextInt(),
      this.node_type_factory);
    this.element.addElement(temp_agent);
    temp_agent.clazz = clazz;
    temp_agent.type = type;

    return temp_agent;
  }

  /**
   * Adds a new Node to this world. The node is a clone of node e (though it
   * inherits no links from it).
   */
  public Node addNewAgent(Node e) {
    temp_agent = new Node(e, this.node_type_factory, this.clazz_factory);
    this.element.addElement(temp_agent);

    return temp_agent;
  }

  /**
   * Destroys a specified agent within this world
   */
  public boolean killThisNode(Node e) {
    final int number_of_nodes = this.element.size();
    for (int temp = number_of_nodes; --temp >= 0;) {
      if (this.element.elementAt(temp) == e) {
        killNumberedAgent(temp);
        return true;
      }
    }

    Log.log("Node not found!");

    return false;
  }

  public void killNumberedAgent(int n) {
    BinGrid.colourZero();
    final Node nod = (Node) this.element.elementAt(n);
    //nod.scrub();
    this.link_manager.killAllLinks(nod);
    this.element.removeElementAt(n);
  }

  /**
   * calculates the distance between e1 and e2...
   */
  public int distanceBetween(Node e1, Node e2) {
    return SquareRoot.fastSqrt(distanceSquaredBetween(e1, e2)) << Coords.shift;
  }

  int distanceSquaredBetween(Node e1, Node e2) {
    final int temp_x0 = (e1.pos.x - e2.pos.x) >> Coords.shift;
    final int temp_y0 = (e1.pos.y - e2.pos.y) >> Coords.shift;
    final int temp_z0 = (e1.pos.z - e2.pos.z) >> Coords.shift;

    return temp_x0 * temp_x0 + temp_y0 * temp_y0 + temp_z0 * temp_z0;
  }

  public int getAgentNumber(Node e) {
    return this.element.indexOf(e);
  }

  public boolean isSelection() {
    final int number_of_nodes = this.element.size();
    for (int temp = number_of_nodes; --temp >= 0;) {
      if (((Node) this.element.elementAt(temp)).type.selected) {
        return true;
      }
    }

    return false;
  }

  public void deselectAll() {
    final int number_of_nodes = this.element.size();
    for (int temp = number_of_nodes; --temp >= 0;) {
      final Node n = (Node) this.element.elementAt(temp);
      n.type.selected = false;
    }
    BinGrid.repaint_all_objects = true;
  }

  public void deleteSelected() {
    final int number_of_nodes = this.element.size();
    for (int temp = number_of_nodes; --temp >= 0;) {
      final Node n = (Node) this.element.elementAt(temp);
      if (n.type.selected) {
        FrEnd.killAllLinks(n);
        killThisNode(n);
        temp--;
      }
    }
  }

  public void setColourOfSelected(char c) {
    final int number_of_nodes = this.element.size();
    for (int temp = number_of_nodes; --temp >= 0;) {
      final Node n = (Node) this.element.elementAt(temp);
      if (n.type.selected) {
        n.clazz.colour = c;
        BinGrid.repaint_all_objects = true;
      }
    }
  }

  public void setSizeOfSelected(int s) {
    final int number_of_nodes = this.element.size();
    for (int temp = number_of_nodes; --temp >= 0;) {
      final Node n = (Node) this.element.elementAt(temp);
      if (n.type.selected) {
        n.type.setSize((byte) s);
        BinGrid.repaint_all_objects = true;
      }
    }
  }

  public void moveSelection(int _dx, int _dy) {
    final int number_of_nodes = this.element.size();
    for (int temp = number_of_nodes; --temp >= 0;) {
      final Node n = (Node) this.element.elementAt(temp);
      if (temp_agent.type.selected) {
        n.pos.x += _dx;
        n.pos.y += _dy;

        n.velocity.x = 0;
        n.velocity.y = 0;
        n.velocity.z = 0;

        n.boundaryCheck();

        // make sure bins are updated...
        n.findNewBin();
      }
    }
  }

  public void centre() {
    final int number_of_nodes = this.element.size();
    for (int temp = number_of_nodes; --temp >= 0;) {
      final Node n = (Node) this.element.elementAt(temp);
      n.pos.x = this.associated_node.pos.x;
      n.pos.y = this.associated_node.pos.y;
      n.pos.z = this.associated_node.pos.z;
      n.velocity.x = 0;
      n.velocity.y = 0;
      n.velocity.z = 0;
    }
  }

  void confine() {
    int temp_x0;
    int temp_y0;
    int temp_x1;
    int temp_y1;
    int temp_z0;
    int temp_z1;

    int tadx;
    int tady;
    int tadz;

    int temp_x;
    int temp_y;
    int temp_z;

    final int temp_detection_distance2 = this.associated_node.type.radius
      - temp_agent.type.radius;
    final int temp_detection_distance = temp_detection_distance2 >> Coords.shift;
    final int temp_detection_distance2o2 = temp_detection_distance2 >> 1;

    temp_x0 = temp_agent.pos.x - this.associated_node.pos.x;
    temp_x1 = (temp_x0 < 0) ? -temp_x0 : temp_x0; // hmmm...
    temp_y0 = temp_agent.pos.y - this.associated_node.pos.y;
    temp_y1 = (temp_y0 < 0) ? -temp_y0 : temp_y0;
    temp_z0 = temp_agent.pos.z - this.associated_node.pos.z;
    temp_z1 = (temp_z0 < 0) ? -temp_z0 : temp_z0;

    if ((temp_y1 > temp_detection_distance2o2)
      || (temp_x1 > temp_detection_distance2o2)
      || (temp_z1 > temp_detection_distance2o2)) {
      // probable collision...
      // inner diamond check...
      // (FrEnd.three_d)
      if ((temp_detection_distance2 - temp_x1) < temp_y1) {
        // 3d version of above needed...
        // make smaller to prevent overflow...
        tadx = temp_x1 >> Coords.shift;
        tady = temp_y1 >> Coords.shift;
        tadz = temp_z1 >> Coords.shift;

        int temp_radius = (tadx * tadx) + (tady * tady) + (tadz * tadz);

        // temp_detection_distance = temp_detection_distance >> shift;
        final int temp_detection_distance_squared = temp_detection_distance
          * temp_detection_distance; // temp_detection_distance_squared; //
        // temp_detection_distance *
        // temp_detection_distance;
        if (temp_radius >= temp_detection_distance_squared) {
          // collision
          collide_with_self = (temp_agent.creature == this.associated_node.creature)
            && (temp_agent.creature != null);
          // sqrt - really? :-(
          temp_radius = SquareRoot.fastSqrt(1 + temp_radius);

          // find unit vector along collision path...
          temp_x = temp_x0 / temp_radius;
          temp_y = temp_y0 / temp_radius;
          temp_z = temp_z0 / temp_radius;

          // compute the relative velocities...
          tadx = temp_agent.velocity.x - this.associated_node.velocity.x;
          tady = temp_agent.velocity.y - this.associated_node.velocity.y;
          tadz = temp_agent.velocity.z - this.associated_node.velocity.z;

          // dot product...
          int magnitude = ((tadx * temp_x) + (tady * temp_y) + (tadz * temp_z)) >> (Coords.shift - 1);

          // debug("magnitude " + magnitude);
          if (magnitude > 0) {
            int temp_count = ((temp_radius - temp_detection_distance) << 8)
              + minimum_magnitude;

            if (temp_count > maximum_magnitude) {
              temp_count = maximum_magnitude;
            }

            if (magnitude < temp_count) {
              magnitude = temp_count;
            }

            // crude hack - since impulse calculations don't seem to be running
            // yet...
            temp_x = -temp_x * magnitude;
            temp_y = -temp_y * magnitude;
            temp_z = -temp_z * magnitude;

            final int temp_ma = temp_agent.type.log_mass
              - this.associated_node.type.log_mass;

            if (temp_ma < 0) {
              tadx = temp_x >> -temp_ma;
              tady = temp_y >> -temp_ma;
              tadz = temp_z >> -temp_ma;

              temp_agent.velocity.x += tadx >> 9;
              temp_agent.velocity.y += tady >> 9;
              temp_agent.velocity.z += tadz >> 9;

              this.associated_node.velocity.x -= ((temp_x << 1) - tadx) >> 9;
              this.associated_node.velocity.y -= ((temp_y << 1) - tady) >> 9;
              this.associated_node.velocity.z -= ((temp_z << 1) - tadz) >> 9;
            } else {
              tadx = temp_x >> temp_ma;
              tady = temp_y >> temp_ma;
              tadz = temp_z >> temp_ma;

              temp_agent.velocity.x += ((temp_x << 1) - tadx) >> 9;
              temp_agent.velocity.y += ((temp_y << 1) - tady) >> 9;
              temp_agent.velocity.z += ((temp_z << 1) - tadz) >> 9;

              this.associated_node.velocity.x -= tadx >> 9;
              this.associated_node.velocity.y -= tady >> 9;
              this.associated_node.velocity.z -= tadz >> 9;
            }

            //applyDamping();
            applyCollisionDamping();
          }
        }
      }
    }
  }

  void applyCollisionDamping() {
    int average_dx;
    int average_dy;
    int average_dz;

    // internal damping...
    average_dx = temp_agent.velocity.x + this.associated_node.velocity.x + 8;
    average_dy = temp_agent.velocity.y + this.associated_node.velocity.y + 8;
    average_dz = temp_agent.velocity.z + this.associated_node.velocity.z + 8;

    temp_agent.velocity.x = ((temp_agent.velocity.x * 14) + average_dx) >> 4;
    temp_agent.velocity.y = ((temp_agent.velocity.y * 14) + average_dy) >> 4;
    temp_agent.velocity.z = ((temp_agent.velocity.z * 14) + average_dz) >> 4;

    this.associated_node.velocity.x = ((this.associated_node.velocity.x * 14) + average_dx) >> 4;
    this.associated_node.velocity.y = ((this.associated_node.velocity.y * 14) + average_dy) >> 4;
    this.associated_node.velocity.z = ((this.associated_node.velocity.z * 14) + average_dz) >> 4;
  }

  public boolean contains(Node e) {
    final int number_of_nodes = this.element.size();
    for (int temp = number_of_nodes; --temp >= 0;) {
      final Node n = (Node) this.element.elementAt(temp);
      if (n == e) {
        return true;
      }
    }

    return false;
  }

  final int getNodeNumber(Node e) {
    final int number_of_nodes = this.element.size();
    for (int temp = number_of_nodes; --temp >= 0;) {
      final Node n = (Node) this.element.elementAt(temp);
      if (n == e) {
        return temp;
      }
    }

    return -1;
  }

  public final void merge(World source) {
    transferNodes(source);

    transferLinks(source);

    transferFaces(source);

    transferClazzes(source);
  }

  private void transferClazzes(World source) {
    final int n = source.clazz_factory.array.size();

    for (int temp = 0; temp < n; temp++) {
      final Clazz clazz_from = (Clazz) source.clazz_factory.array
        .elementAt(temp);
      final Clazz clazz_to = this.clazz_factory.getNew(clazz_from.colour);
      changeValuesOfOldClazzesToNewClazzes(source, clazz_from, clazz_to);
    }
  }

  private void changeValuesOfOldClazzesToNewClazzes(World source,
    final Clazz clazz_from, Clazz clazz_to) {

    final int n_l = source.link_manager.element.size();
    for (int temp = 0; temp < n_l; temp++) {
      final Link link = (Link) source.link_manager.element.elementAt(temp);
      if (link.clazz.equals(clazz_from)) {
        link.clazz = clazz_to;
      }
    }
    
    final int n_f = source.face_manager.element.size();
    for (int temp = 0; temp < n_f; temp++) {
      final Face face = (Face) source.face_manager.element.elementAt(temp);
      if (face.clazz.equals(clazz_from)) {
        face.clazz = clazz_to;
      }
    }
    
    final int n_n = source.element.size();
    for (int temp = 0; temp < n_n; temp++) {
      final Node node = (Node) source.element.elementAt(temp);
      if (node.clazz.equals(clazz_from)) {
        node.clazz = clazz_to;
      }
    }
  }

  private void transferLinks(World source) {
    final int n = source.link_manager.element.size();

    for (int temp = 0; temp < n; temp++) {
      final Link link = (Link) source.link_manager.element.elementAt(temp);
      final LinkType type = this.link_manager.link_type_factory.getNew();
      type.makeEqualTo(link.type);
      link.type = type;
      this.link_manager.element.addElement(link);
    }
  }

  private void transferFaces(World source) {
    final int number_of_faces = source.face_manager.element.size();

    for (int temp = 0; temp < number_of_faces; temp++) {
      final Face face = (Face) source.face_manager.element.elementAt(temp);
      final FaceType type = this.face_manager.face_type_factory.getNew();
      type.makeEqualTo(face.type);
      face.type = type;
      this.face_manager.element.addElement(face);
    }
  }

  private void transferNodes(World source) {
    final int number_of_nodes_from = source.element.size();

    for (int temp = 0; temp < number_of_nodes_from; temp++) {
      final Node node = (Node) source.element.elementAt(temp);
      final NodeType type = this.node_type_factory.getNew();
      type.makeEqualTo(node.type);
      node.type = type;
      this.element.addElement(node);
    }
  }

  public LinkManager getLinkManager() {
    return this.link_manager;
  }
}