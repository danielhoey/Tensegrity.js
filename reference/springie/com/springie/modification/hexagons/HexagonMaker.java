package com.springie.modification.hexagons;

import com.springie.elements.clazz.Clazz;
import com.springie.elements.links.Link;
import com.springie.elements.links.LinkManager;
import com.springie.elements.links.LinkType;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.elements.nodes.NodeType;
import com.springie.geometry.Point3D;
import com.springie.modification.DomeMakingUtilities;
import com.springie.utilities.random.JUR;

public class HexagonMaker {
  JUR rnd = new JUR();

  int colour_link_between_layers = 0xFF6060FF;

  int colour_level_link_between_outer_hex = 0xFFFFFFC0;

  int colour_node_inner_eden = 0xFFFF40FF;

  int colour_level_link_between_triaxial = 0xFFC0FFFF;

  int colour_node_outer_hex = 0xFFFFFF40;

  int colour_node_triaxial = 0xFF40FFFF;

  int colour_level_geodesic = 0xFFFF2020;

  DomeMakingUtilities utils = new DomeMakingUtilities();

  NodeManager node_manager;

  LinkManager link_manager;

  public HexagonMaker(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;
  }

//  public void connectNodesToNearestNodes(int how_many) {
//    final int colour = FrEnd.panel_edit_edit.cp_gas.getColour();
//    final int[] selected_node_numbers = getArrayOfSelectedNodes();
//    Log.log("selected_node_numbers" + selected_node_numbers.length);
//    final int[] target_node_numbers = getArrayOfNodesOfColour(colour);
//    Log.log("target_node_numbers" + target_node_numbers.length);
//    final int number_of_selected_nodes = selected_node_numbers.length;
//
//    for (int counter = number_of_selected_nodes; --counter >= 0;) {
//      final int i = selected_node_numbers[counter];
//      final Node source = (Node) this.node_manager.element.elementAt(i);
//      final ListOfNearest list_of_nearest = getNearestNodes(source,
//        target_node_numbers, how_many);
//      createLinks(i, list_of_nearest);
//    }
//
//    FrEnd.postCleanup();
//  }
//
//  private int[] getArrayOfSelectedNodes() {
//    final int number_of_nodes = this.node_manager.element.size();
//    final int number_of_selected_nodes = this.node_manager
//      .getNumberOfSelectedNodes();
//    final int[] nodes = new int[number_of_selected_nodes];
//    int idx = 0;
//    for (int counter = number_of_nodes; --counter >= 0;) {
//      final Node candidate = (Node) this.node_manager.element
//        .elementAt(counter);
//      if (candidate.type.selected) {
//        nodes[idx++] = counter;
//      }
//    }
//    return nodes;
//  }
//
//  private int[] getArrayOfNodesOfColour(int colour) {
//    final int number_of_nodes = this.node_manager.element.size();
//    //final int number_of_selected_nodes = this.node_manager
//    //.getNumberOfSelectedNodes();
//    //final int[] nodes = new int[number_of_selected_nodes];
//    final Vector nodes = new Vector();
//    //int idx = 0;
//    for (int counter = number_of_nodes; --counter >= 0;) {
//      final Node candidate = (Node) this.node_manager.element
//        .elementAt(counter);
//      if (candidate.clazz.colour == colour) {
//        nodes.addElement(new Integer(counter));
//        //nodes[idx++] = counter;
//      }
//    }
//
//    final int size = nodes.size();
//    final int[] array = new int[size];
//
//    for (int counter = size; --counter >= 0;) {
//      array[counter] = ((Integer) (nodes.elementAt(counter))).intValue();
//    }
//
//    return array;
//  }
//
//  private void createLinks(int nn_1, ListOfNearest list_of_nearest) {
//    final int number = list_of_nearest.size;
//    final Node candidate = (Node) this.node_manager.element.elementAt(nn_1);
//    for (int counter = number; --counter >= 0;) {
//      final int nn_2 = list_of_nearest.array[counter];
//      if (nn_2 > nn_1) {
//        final Node destination = (Node) this.node_manager.element
//          .elementAt(nn_2);
//
//        if (!this.link_manager.isThereALinkBetween(destination, candidate)) {
//          joinNodesInOuterHexLayer(candidate, destination);
//        }
//      }
//    }
//  }
//
//  private ListOfNearest getNearestNodes(Node source, int[] target_node_numbers,
//    int max) {
//    final ListOfNearest list_of_nearest = new ListOfNearest(max);
//
//    final int number = target_node_numbers.length;
//
//    for (int idx = number; --idx >= 0;) {
//      final int num_of_candidate_node = target_node_numbers[idx];
//      final Node candidate = (Node) this.node_manager.element
//        .elementAt(num_of_candidate_node);
//      if (source != candidate) {
//        final int d = this.node_manager.distanceBetween(source, candidate);
//        if (d < list_of_nearest.largest_proximity) {
//          list_of_nearest.update(d, num_of_candidate_node);
//        }
//      }
//    }
//
//    return list_of_nearest;
//  }

  public void oldconnectNodesInOuterHexLayer() {
    final int number_of_nodes = this.node_manager.element.size();

    final int[] nonoc = setupCounts(this.colour_node_outer_hex);

    for (int counter = number_of_nodes; --counter >= 0;) {
      //Log.log("" + counter);
      final Node candidate = (Node) this.node_manager.element
        .elementAt(counter);
      if (candidate.type.selected) {
        while (nonoc[counter] < 3) {
          final int idx = getIndexOfNearestNodeOfColourQuick(candidate, nonoc,
            3);
          final Node found = (Node) this.node_manager.element.elementAt(idx);

          joinNodesInOuterHexLayer(candidate, found);
          nonoc[counter]++;
          nonoc[idx]++;
        }
      }
    }
  }

  int[] setupCounts(int colour) {
    final int number_of_nodes = this.node_manager.element.size();
    final int[] nonoc = new int[number_of_nodes];
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) this.node_manager.element
        .elementAt(counter);
      if (candidate.type.selected) {
        nonoc[counter] = quickNumberOfLinksOfColour(candidate, colour);
      }
    }

    return nonoc;
  }

  //// public void connectNodesInInnerHexLayer() {
  //    final int number_of_nodes = this.node_manager.element.size();
  //    for (int counter = number_of_nodes; --counter >= 0;) {
  //      final Node candidate = (Node) this.node_manager.element.elementAt(counter);
  //      if (candidate.type.selected) {
  //      if (candidate.clazz.colour == this.colour_node_inner_hex) {
  //        while (numberOfLinksOfColour(candidate,
  //          this.colour_level_link_between_inner_hex) < 3) {
  //          final Node node = nextNearestNodeOfColour(candidate,
  //            this.colour_node_inner_hex, 3);
  //
  //          joinNodesInInnerHexLayer(candidate, node);
  //        }
  //      }
  //    }
  //  }

  public void connectNodesInTriaxialOuterLayer() {
    final int number_of_nodes = this.node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) this.node_manager.element
        .elementAt(counter);
      if (candidate.clazz.colour == this.colour_node_triaxial) {
        while (numberOfLinksOfColour(candidate,
          this.colour_level_link_between_triaxial) < 4) {
          final Node node = nextNearestNodeOfColour(candidate,
            this.colour_node_triaxial, 4);

          joinNodesInTriaxialOuterLayer(candidate, node);
        }
      }
    }
  }

  private Node nextNearestNodeOfColour(Node node, int link_colour,
    int max_already) {
    final int i = getIndexOfNearestNodeOfColour(node, link_colour, max_already);
    if (i < 0) {
      return null;
    }

    return (Node) this.node_manager.element.elementAt(i);
  }

  private int getIndexOfNearestNodeOfColourQuick(Node node, int[] array,
    int max_already) {
    int index_of_nearest = -1;
    int min_distance = Integer.MAX_VALUE;

    final int number_of_nodes = this.node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) this.node_manager.element
        .elementAt(counter);
      if (candidate.type.selected) {
        if (array[counter] < max_already) {
          if (node != candidate) {
            if (!this.link_manager.isThereALinkBetween(node, candidate)) {
              int d = this.node_manager.distanceBetween(node, candidate);
              if (d < min_distance) {
                index_of_nearest = counter;
                min_distance = d;
              }
            }
          }
        }
      }
    }

    return index_of_nearest;
  }

  private int getIndexOfNearestNodeOfColour(Node node, int link_colour,
    int max_already) {
    int index_of_nearest = -1;
    int min_distance = Integer.MAX_VALUE;

    final int number_of_nodes = this.node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) this.node_manager.element
        .elementAt(counter);
      if (node != candidate) {
        if (candidate.type.selected) {
          if (!this.link_manager.isThereALinkBetween(node, candidate)) {
            if (numberOfLinksOfColour(node, link_colour) < max_already) {
              int d = this.node_manager.distanceBetween(node, candidate);
              if (d < min_distance) {
                index_of_nearest = counter;
                min_distance = d;
              }
            }
          }
        }
      }
    }

    return index_of_nearest;
  }

  // improve this (or avoid it!)...
  private int numberOfLinksOfColour(Node node, int colour) {
    int count = 0;

    final int n_o_l = this.link_manager.element.size();
    for (int temp = n_o_l; --temp >= 0;) {
      final Link link = (Link) this.link_manager.element.elementAt(temp);
      if (link.clazz.colour == colour) {
        if ((link.node1 == node) || (link.node2 == node)) {
          count++;
        }
      }
    }
    //Log.log("count " + count);
    return count;
  }

  private int quickNumberOfLinksOfColour(Node node, int colour) {
    int count = 0;

    final int n_o_l = node.list_of_links.size();
    for (int temp = n_o_l; --temp >= 0;) {
      final int i = node.list_of_links.retreive(temp);
      final Link link = (Link) this.link_manager.element.elementAt(i);
      if (link.clazz.colour == colour) {
        count++;
      }
    }
    //Log.log("count " + count);
    return count;
  }

  public void createNodesInTriaxialOuterLayer(float s_f) {
    final int number_of_links = this.link_manager.element.size();
    final Point3D centre = this.node_manager.getCentre();

    for (int cnt = number_of_links; --cnt >= 0;) {
      final Link link = (Link) this.link_manager.element.elementAt(cnt);
      if (link.type.selected) {
        makeNewEdenNode(s_f, centre, link);
      }
    }
  }

//  public void createNodesInInnerEdenLayer(float s_f) {
//    final int number_of_nodes = this.node_manager.element.size();
//    for (int cnt = 1000; --cnt >= 0;) {
//      final int a1 = this.rnd.nextInt(number_of_nodes);
//      final Node node_1 = (Node) this.node_manager.element.elementAt(a1);
//      if (node_1.clazz.colour != this.colour_node_triaxial) {
//        continue;
//      }
//      final Node node_2 = this.utils.getRandomCellLinkedTo(this.node_manager,
//        node_1);
//      if (node_2 == null) {
//        continue;
//      }
//      if (node_2.clazz.colour != this.colour_node_triaxial) {
//        continue;
//      }
//
//      final Node node_3 = this.utils.getRandomCellLinkedTo(this.node_manager,
//        node_2);
//      if (node_1 == node_3) {
//        continue;
//      }
//      if (node_3 == null) {
//        continue;
//      }
//      if (node_3.clazz.colour != this.colour_node_triaxial) {
//        continue;
//      }
//
//      if (this.link_manager.isThereALinkBetween(node_1, node_3)) {
//        if (!this.utils.isThereANodeLinkedTo(this.node_manager, node_1, node_2,
//          node_3, this.colour_node_inner_eden)) {
//          int new_x = (node_1.pos.x + node_2.pos.x + node_3.pos.x) / 3;
//          int new_y = (node_1.pos.y + node_2.pos.y + node_3.pos.y) / 3;
//          int new_z = (node_1.pos.z + node_2.pos.z + node_3.pos.z) / 3;
//
//          final Point3D centre = this.node_manager.getCentre();
//
//          int vec_x = (int) ((new_x - centre.x) * s_f);
//          int vec_y = (int) ((new_y - centre.y) * s_f);
//          int vec_z = (int) ((new_z - centre.z) * s_f);
//
//          new_x += vec_x;
//          new_y += vec_y;
//          new_z += vec_z;
//
//          final NodeType type = this.node_manager.node_type_factory.getNew();
//          type.radius = (node_1.type.radius + node_2.type.radius) >> 2;
//          type.log_mass = (node_1.type.log_mass + node_2.type.log_mass) >> 1;
//
//          final Clazz clazz = this.node_manager.clazz_factory
//            .getNew(this.colour_node_inner_eden);
//          final Point3D pos = new Point3D(new_x, new_y, new_z);
//
//          final Node added = this.node_manager.addNewAgent(pos, clazz, type);
//
//          final int length_1 = this.node_manager.distanceBetween(node_1, added);
//          final int length_2 = this.node_manager.distanceBetween(node_2, added);
//          final int length_3 = this.node_manager.distanceBetween(node_3, added);
//
//          final int length = (length_1 + length_2 + length_3) / 3;
//
//          joinNodesBetweenLayers(node_1, added, length);
//          joinNodesBetweenLayers(node_2, added, length);
//          joinNodesBetweenLayers(node_3, added, length);
//        }
//      }
//    }
//  }
//
//  public void zcreateNodesInInnerEdenLayer(float s_f) {
//    final int number_of_nodes = this.node_manager.element.size();
//    for (int cnt = 1000; --cnt >= 0;) {
//      final int a1 = this.rnd.nextInt(number_of_nodes);
//      final Node node_1 = (Node) this.node_manager.element.elementAt(a1);
//      if (node_1.clazz.colour != this.colour_node_triaxial) {
//        continue;
//      }
//      final Node node_2 = this.utils.getRandomCellLinkedTo(this.node_manager,
//        node_1);
//      if (node_2 == null) {
//        continue;
//      }
//      if (node_2.clazz.colour != this.colour_node_triaxial) {
//        continue;
//      }
//
//      final Node node_3 = this.utils.getRandomCellLinkedTo(this.node_manager,
//        node_2);
//      if (node_1 == node_3) {
//        continue;
//      }
//      if (node_3 == null) {
//        continue;
//      }
//      if (node_3.clazz.colour != this.colour_node_triaxial) {
//        continue;
//      }
//
//      if (this.link_manager.isThereALinkBetween(node_1, node_3)) {
//        if (!this.utils.isThereANodeLinkedTo(this.node_manager, node_1, node_2,
//          node_3, this.colour_node_inner_eden)) {
//          int new_x = (node_1.pos.x + node_2.pos.x + node_3.pos.x) / 3;
//          int new_y = (node_1.pos.y + node_2.pos.y + node_3.pos.y) / 3;
//          int new_z = (node_1.pos.z + node_2.pos.z + node_3.pos.z) / 3;
//
//          final Point3D centre = this.node_manager.getCentre();
//
//          int vec_x = (int) ((new_x - centre.x) * s_f);
//          int vec_y = (int) ((new_y - centre.y) * s_f);
//          int vec_z = (int) ((new_z - centre.z) * s_f);
//
//          new_x += vec_x;
//          new_y += vec_y;
//          new_z += vec_z;
//
//          final NodeType type = this.node_manager.node_type_factory.getNew();
//          type.radius = (node_1.type.radius + node_2.type.radius) >> 2;
//          type.log_mass = (node_1.type.log_mass + node_2.type.log_mass) >> 1;
//
//          final Clazz clazz = this.node_manager.clazz_factory
//            .getNew(this.colour_node_inner_eden);
//          final Point3D pos = new Point3D(new_x, new_y, new_z);
//
//          final Node added = this.node_manager.addNewAgent(pos, clazz, type);
//
//          final int length_1 = this.node_manager.distanceBetween(node_1, added);
//          final int length_2 = this.node_manager.distanceBetween(node_2, added);
//          final int length_3 = this.node_manager.distanceBetween(node_3, added);
//
//          final int length = (length_1 + length_2 + length_3) / 3;
//
//          joinNodesBetweenLayers(node_1, added, length);
//          joinNodesBetweenLayers(node_2, added, length);
//          joinNodesBetweenLayers(node_3, added, length);
//        }
//      }
//    }
//  }
  
  private void makeNewEdenNode(float s_f, final Point3D centre, final Link link) {
    final Node node_1 = link.node1;
    final Node node_2 = link.node2;
    int new_x = (node_1.pos.x + node_2.pos.x) / 2;
    int new_y = (node_1.pos.y + node_2.pos.y) / 2;
    int new_z = (node_1.pos.z + node_2.pos.z) / 2;

    int vec_x = (int) ((new_x - centre.x) * s_f);
    int vec_y = (int) ((new_y - centre.y) * s_f);
    int vec_z = (int) ((new_z - centre.z) * s_f);

    new_x += vec_x;
    new_y += vec_y;
    new_z += vec_z;

    final NodeType type = this.node_manager.node_type_factory.getNew();
    type.radius = (node_1.type.radius + node_2.type.radius) >> 2;
    type.log_mass = (node_1.type.log_mass + node_2.type.log_mass) >> 1;

    final Clazz clazz = this.node_manager.clazz_factory
      .getNew(this.colour_node_triaxial);
    final Point3D pos = new Point3D(new_x, new_y, new_z);

    final Node added = this.node_manager.addNewAgent(pos, clazz, type);

    final int length_1 = this.node_manager.distanceBetween(node_1, added);
    final int length_2 = this.node_manager.distanceBetween(node_2, added);

    final int length = (length_1 + length_2) / 2;

    joinNodesBetweenLayers(node_1, added, length);
    joinNodesBetweenLayers(node_2, added, length);
  }

  public void domeNodesContract() {
    final int number_of_nodes = this.node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) this.node_manager.element
        .elementAt(counter);
      if (candidate.clazz.colour != 0) {
        candidate.type.setSize((candidate.type.radius * 7) >> 3);
      }
    }
  }

  public void domeNodesExpand() {
    final int number_of_nodes = this.node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) this.node_manager.element
        .elementAt(counter);
      if (candidate.clazz.colour != 0) {
        candidate.type.setSize((candidate.type.radius * 9) >> 3);
      }
    }
  }

  private void joinNodesBetweenLayers(Node node_1, Node node_2, int length) {
    final LinkType type = this.link_manager.link_type_factory
      .getNew(length, 30);
    final Clazz clazz = this.node_manager.clazz_factory
      .getNew(this.colour_link_between_layers);
    this.link_manager.setLink(node_1, node_2, type, clazz);
  }

  private void joinNodesInOuterHexLayer(Node node_1, Node node_2) {
    final int length = this.node_manager.distanceBetween(node_1, node_2);
    final LinkType type = this.link_manager.link_type_factory
      .getNew(length, 50);

    final Clazz clazz = this.node_manager.clazz_factory
      .getNew(this.colour_level_link_between_outer_hex);
    this.link_manager.setLink(node_1, node_2, type, clazz);
  }

  private void joinNodesInTriaxialOuterLayer(Node node_1, Node node_2) {
    final int length = this.node_manager.distanceBetween(node_1, node_2);
    final LinkType type = this.link_manager.link_type_factory
      .getNew(length, 50);
    final Clazz clazz = this.node_manager.clazz_factory
      .getNew(this.colour_level_link_between_triaxial);
    this.link_manager.setLink(node_1, node_2, type, clazz);
  }
}

//  private void joinNodesInInnerHexLayer(Node node_1, Node node_2) {
//    final int length = this.node_manager.distanceBetween(node_1, node_2);
//    final LinkType type = LinkTypeFactory.getNew(length, 50);
//    final Clazz clazz = ClazzFactory
//      .getNew(this.colour_level_link_between_inner_hex);
//    this.link_manager.setLink(node_1, node_2, type, clazz);
//  }

//  public void createNodesInOuterLayer(float s_f) {
//    final int number_of_nodes = this.node_manager.node.size();
//    final Point3D centre = this.node_manager.returnCentre();
//
//    for (int cnt = 1000; --cnt >= 0;) {
//      final int a1 = this.rnd.nextInt(number_of_nodes);
//      final Node node_1 = (Node) this.node_manager.node.elementAt(a1);
//      if (node_1.clazz.colour != this.colour_level_geodesic) {
//        continue;
//      }
//      final Node node_2 = this.utils.getRandomCellLinkedTo(this.node_manager,
//        node_1);
//      if (node_2 == null) {
//        continue;
//      }
//      if (node_2.clazz.colour != this.colour_level_geodesic) {
//        continue;
//      }
//
//      final Node node_3 = this.utils.getRandomCellLinkedTo(this.node_manager,
//        node_2);
//      if (node_1 == node_3) {
//        continue;
//      }
//      if (node_3 == null) {
//        continue;
//      }
//      if (node_3.clazz.colour != this.colour_level_geodesic) {
//        continue;
//      }
//
//      if (this.link_manager.isThereALinkBetween(node_1, node_3)) {
//        if (!this.utils.isThereANodeLinkedTo(this.node_manager, node_1, node_2,
//          node_3, this.colour_node_outer_hex)) {
//          int new_x = (node_1.pos.x + node_2.pos.x + node_3.pos.x) / 3;
//          int new_y = (node_1.pos.y + node_2.pos.y + node_3.pos.y) / 3;
//          int new_z = (node_1.pos.z + node_2.pos.z + node_3.pos.z) / 3;
//
//
//          int vec_x = (int) ((new_x - centre.x) * s_f);
//          int vec_y = (int) ((new_y - centre.y) * s_f);
//          int vec_z = (int) ((new_z - centre.z) * s_f);
//
//          new_x += vec_x;
//          new_y += vec_y;
//          new_z += vec_z;
//
//          final Node added = this.node_manager.addNewAgent(new_x, new_y, new_z,
//            this.colour_node_outer_hex, 5000, (byte) 4);
//
//          final int length_1 = this.node_manager.distanceBetween(node_1, added);
//          final int length_2 = this.node_manager.distanceBetween(node_2, added);
//          final int length_3 = this.node_manager.distanceBetween(node_3, added);
//
//          final int length = (length_1 + length_2 + length_3) / 3;
//
//          joinNodesBetweenLayers(node_1, added, length);
//          joinNodesBetweenLayers(node_2, added, length);
//          joinNodesBetweenLayers(node_3, added, length);
//        }
//      }
//    }
//  }
//
//  public void createNodesInInnerLayer(float s_f) {
//    final Point3D centre = this.node_manager.returnCentre();
//
//    final int number_of_nodes = this.node_manager.node.size();
//    for (int cnt = 1000; --cnt >= 0;) {
//      final int a1 = this.rnd.nextInt(number_of_nodes);
//      final Node node_1 = (Node) this.node_manager.node.elementAt(a1);
//      if (node_1.clazz.colour != this.colour_level_geodesic) {
//        continue;
//      }
//      final Node node_2 = this.utils.getRandomCellLinkedTo(this.node_manager,
//        node_1);
//      if (node_2 == null) {
//        continue;
//      }
//      if (node_2.clazz.colour != this.colour_level_geodesic) {
//        continue;
//      }
//
//      final Node node_3 = this.utils.getRandomCellLinkedTo(this.node_manager,
//        node_2);
//      if (node_1 == node_3) {
//        continue;
//      }
//      if (node_3 == null) {
//        continue;
//      }
//      if (node_3.clazz.colour != this.colour_level_geodesic) {
//        continue;
//      }
//
//      if (this.link_manager.isThereALinkBetween(node_1, node_3)) {
//        if (!this.utils.isThereANodeLinkedTo(this.node_manager, node_1, node_2,
//          node_3, this.colour_node_inner_hex)) {
//          int new_x = (node_1.pos.x + node_2.pos.x + node_3.pos.x) / 3;
//          int new_y = (node_1.pos.y + node_2.pos.y + node_3.pos.y) / 3;
//          int new_z = (node_1.pos.z + node_2.pos.z + node_3.pos.z) / 3;
//
//          int vec_x = (int) ((new_x - centre.x) * s_f);
//          int vec_y = (int) ((new_y - centre.y) * s_f);
//          int vec_z = (int) ((new_z - centre.z) * s_f);
//
//          new_x += vec_x;
//          new_y += vec_y;
//          new_z += vec_z;
//
//          final Node added = this.node_manager.addNewAgent(new_x, new_y, new_z,
//            this.colour_node_inner_hex, 5000, (byte) 4);
//
//          final int length_1 = this.node_manager.distanceBetween(node_1, added);
//          final int length_2 = this.node_manager.distanceBetween(node_2, added);
//          final int length_3 = this.node_manager.distanceBetween(node_3, added);
//
//          final int length = (length_1 + length_2 + length_3) / 3;
//
//          joinNodesBetweenLayers(node_1, added, length);
//          joinNodesBetweenLayers(node_2, added, length);
//          joinNodesBetweenLayers(node_3, added, length);
//        }
//      }
//    }
//  }
