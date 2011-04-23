//This code has been placed into the public domain by its author

package com.springie.extension;

import com.springie.elements.nodes.Node;
import com.springie.utilities.general.Forget;

/**
 * Here is a sample module. <BR>
 * Only modules which are called "Module" will function. <BR>
 * The class is instantiated once - there should be little need to use static
 * objects. <BR>
 * This class creates some spinning, multi-coloured tetrahedra. <BR>
 * After a short while, two objects in the system explode. <BR>
 */
public class Module implements ModuleInterface {
  Node death_node;

  Node egg_node;

  int count;

  /**
   * Performs any one-time set-up operations
   */
  public void oneOffInitialisation() {
    Forget.about(null);
  }

  /**
   * Called when the user hits reset
   */
  public void calledOnReset() {
    //World world;
    //final NodeManager node_manager = FrEnd.getNodeManager();
    //LinkManager link_manager = node_manager.getLinkManager();

    // reset counter...
    //this.count = 0;

    // makes a big sphere...
    //this.egg_node = FrEnd.node_manager.addNewAgent(10000, 10000, 10000,
    //(char) 511, (byte) 96, (byte) 15, ConstantsNode.DRAG);

    // allows it to have a world inside it...
    //world = WorldManager.associateNewWorldWith(this.egg_node);

    // Shoals_Input.addFromString(_desc, pw, 0, 0, 256);

    // adds three things inside it
    // x, y, z, colour, diameter, log of mass, status flags
    //world.addNewAgent(200, 100, 100, (char) 127, (byte) 10, (byte) 18, 0);
    //world.addNewAgent(100, 200, 100, (char) 127, (byte) 13, (byte) 17, 0);
    //world.addNewAgent(100, 100, 200, (char) 127, (byte) 20, (byte) 16, 0);

    // a tetrahedron inside the "egg".
    //makeTetrahedron(world, 50);

    // one floats around outside...
    //node_manager.addNewAgent(400, 400, 500, (char) 62, (byte) 24, (byte) 16,
    // 0);

    //for (int i = 0; i++ <= 16;) {
    //makeTetrahedron(node_manager, 50 + i * 10);
    //}
  }

  //  void makeTetrahedron(World world, int scale_factor) {
  //    Node node1;
  //    Node node2;
  //    Node node3;
  //    Node node4;
  //    final LinkManager link_manager = world.getLinkManager();
  //
  //// node1 = world.addNewAgent(400, 400, 500, (char) scale_factor,
  //// (byte) (scale_factor >> 2), (byte) 16);
  //// node2 = world.addNewAgent(400, 500, 500, (char) scale_factor,
  //// (byte) (scale_factor >> 2), (byte) 16);
  //// node3 = world.addNewAgent(400, 450, 500, (char) scale_factor,
  //// (byte) (scale_factor >> 2), (byte) 16);
  //// node4 = world.addNewAgent(400, 450, 500, (char) scale_factor,
  //// (byte) (scale_factor >> 2), (byte) 16);
  //
  //    final LinkType type = LinkTypeFactory.getNew(200, 50);
  //    
  //    final Clazz clazz = ClazzFactory.getNew(0xFF40FF80);
  //    link_manager.setLink(node1, node2, type, clazz);
  //    link_manager.setLink(node2, node3, type, clazz);
  //    link_manager.setLink(node3, node1, type, clazz);
  //
  //    link_manager.setLink(node1, node4, type, clazz);
  //    link_manager.setLink(node2, node4, type, clazz);
  //    link_manager.setLink(node3, node4, type, clazz);
  //
  //    this.death_node = node4;
  //  }

  /**
   * Called once during every frame
   */
  public void update() {
    this.count++;

    if (this.count == 1000) {
      this.death_node.simplyKill();
    }

    if (this.count == 2000) {
      this.egg_node.simplyKill();
    }
  }
}