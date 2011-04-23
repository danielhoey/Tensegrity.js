package com.springie.modification.polyhedra;

import java.util.Vector;

import com.springie.FrEnd;
import com.springie.elements.clazz.Clazz;
import com.springie.elements.links.LinkManager;
import com.springie.elements.links.LinkType;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.utilities.log.Log;

public class MakeLinksToNearestNode {
  int colour_of_link_to_nearest = 0xFFFFC0FF;

  private NodeManager node_manager;

  private LinkManager link_manager;

  public MakeLinksToNearestNode(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;
  }

  public void connectNodesToNearestNodes(int how_many) {
    final int colour = FrEnd.panel_edit_edit.cp_gas.getColour();
    final int[] selected_node_numbers = getArrayOfSelectedNodes();
    //Log.log("selected_node_numbers" + selected_node_numbers.length);
    final int[] target_node_numbers = getArrayOfNodesOfColour(colour);
    //Log.log("target_node_numbers" + target_node_numbers.length);
    final int number_of_selected_nodes = selected_node_numbers.length;
    
    if (target_node_numbers.length < how_many) {
      throw new RuntimeException("Too few targets selected");
    }

    for (int counter = number_of_selected_nodes; --counter >= 0;) {
      final int i = selected_node_numbers[counter];
      final Node source = (Node) this.node_manager.element.elementAt(i);
      final ListOfNearest list_of_nearest = getNearestNodes(source,
        target_node_numbers, how_many);

      //Log.log("list_of_nearest.array.length(" + counter + "):" +
      // list_of_nearest.array.length);

      createLinks(i, list_of_nearest);
    }

    FrEnd.postCleanup();
  }

  private int[] getArrayOfSelectedNodes() {
    final int number_of_nodes = this.node_manager.element.size();
    final int number_of_selected_nodes = this.node_manager
      .getNumberOfSelectedNodes();
    final int[] nodes = new int[number_of_selected_nodes];
    int idx = 0;
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) this.node_manager.element
        .elementAt(counter);
      if (candidate.type.selected) {
        nodes[idx++] = counter;
      }
    }
    return nodes;
  }

  private int[] getArrayOfNodesOfColour(int colour) {
    final int number_of_nodes = this.node_manager.element.size();
    final Vector nodes = new Vector();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) this.node_manager.element
        .elementAt(counter);
      if (candidate.clazz.colour == colour) {
        nodes.addElement(new Integer(counter));
      }
    }

    final int size = nodes.size();
    final int[] array = new int[size];

    for (int counter = size; --counter >= 0;) {
      array[counter] = ((Integer) (nodes.elementAt(counter))).intValue();
    }

    return array;
  }

  private void createLinks(int nn_1, ListOfNearest list_of_nearest) {
    //final PostModification post_modification = new PostModification(this.node_manager);
    final int number = list_of_nearest.size;
    final Node candidate = (Node) this.node_manager.element.elementAt(nn_1);
    for (int counter = number; --counter >= 0;) {
      final int nn_2 = list_of_nearest.array[counter];
      final Node destination = (Node) this.node_manager.element.elementAt(nn_2);

      
      if (!this.link_manager.isThereALinkBetween(destination, candidate)) {
        makeLinkBetweenNodes(candidate, destination);
        // make sure LOL is up to date...
        //candidate.list_of_links = post_modification.getListOfLinks(candidate);
        //destination.list_of_links = post_modification.getListOfLinks(destination);
      }
    }
  }

  private ListOfNearest getNearestNodes(Node source, int[] target_node_numbers,
    int max) {
    final ListOfNearest list_of_nearest = new ListOfNearest(max);

    final int number = target_node_numbers.length;

    for (int idx = number; --idx >= 0;) {
      final int num_of_candidate_node = target_node_numbers[idx];
      if (num_of_candidate_node < 0) {
        Log.log("num_of_candidate_node: " + num_of_candidate_node);
      }

      final Node candidate = (Node) this.node_manager.element
        .elementAt(num_of_candidate_node);
      if (source != candidate) {
        final int d = this.node_manager.distanceBetween(source, candidate);
        if (d < list_of_nearest.largest_proximity) {
          list_of_nearest.update(d, num_of_candidate_node);
        }
      }
    }
    
    if (list_of_nearest.countFound() != max) {
      Log.log("list_of_nearest.countFound:" + list_of_nearest.countFound());
    }

    return list_of_nearest;
  }

  private void makeLinkBetweenNodes(Node node_1, Node node_2) {
    final int length = this.node_manager.distanceBetween(node_1, node_2);
    final LinkType type = this.link_manager.link_type_factory
      .getNew(length, 60);

    final Clazz clazz = this.node_manager.clazz_factory
      .getNew(this.colour_of_link_to_nearest);
    this.link_manager.setLink(node_1, node_2, type, clazz);
  }
}