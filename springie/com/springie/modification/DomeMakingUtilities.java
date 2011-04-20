package com.springie.modification;

import com.springie.elements.links.Link;
import com.springie.elements.links.LinkManager;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.render.Coords;
import com.springie.utilities.random.JUR;

public class DomeMakingUtilities {
  JUR rnd = new JUR();

  public boolean isThereANodeLinkedTo(NodeManager node_manager, Node node_1,
    Node node_2, Node node_3) {
    final LinkManager link_manager = node_manager.link_manager;
    final int number_of_nodes = node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) node_manager.element.elementAt(counter);
      if (!candidate.type.hidden) {
        if (link_manager.isThereALinkBetween(node_1, candidate)) {
          if (link_manager.isThereALinkBetween(node_2, candidate)) {
            if (link_manager.isThereALinkBetween(node_3, candidate)) {
              return true;
            }
          }
        }
      }
    }

    return false;
  }

  public boolean isThereANodeLinkedTo(NodeManager node_manager, Node node_1,
    Node node_2, Node node_3, int colour) {
    final LinkManager link_manager = node_manager.link_manager;
    final int number_of_nodes = node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) node_manager.element.elementAt(counter);
      if (!candidate.type.hidden) {
        if (link_manager.isThereALinkBetween(node_1, candidate)) {
          if (link_manager.isThereALinkBetween(node_2, candidate)) {
            if (link_manager.isThereALinkBetween(node_3, candidate)) {
              if (candidate.clazz.colour == colour) {
                return true;
              }
            }
          }
        }
      }
    }

    return false;
  }

  public boolean isThereANodeLinkedTo(NodeManager node_manager, Node node_1,
    Node node_2, int colour) {
    final LinkManager link_manager = node_manager.link_manager;
    final int number_of_nodes = node_manager.element.size();
    for (int counter = number_of_nodes; --counter >= 0;) {
      final Node candidate = (Node) node_manager.element.elementAt(counter);
      if (!candidate.type.hidden) {
        if (link_manager.isThereALinkBetween(node_1, candidate)) {
          if (link_manager.isThereALinkBetween(node_2, candidate)) {
            if (candidate.clazz.colour == colour) {
              return true;
            }
          }
        }
      }
    }

    return false;
  }

  //NOT!!! nodes must be same colour...
  // can produce nulls :-(

  public Node getRandomCellLinkedTo(NodeManager node_manager, Node node_1) {
    final LinkManager link_manager = node_manager.link_manager;
    final int link_count = node_1.list_of_links.size();
    if (link_count == 0) {
      return null;
    }

    //for (int tries = 1000; --tries >= 0;) {
    final int x = this.rnd.nextInt(link_count);
    //final int counter = x;

    final int n = node_1.list_of_links.retreive(x);
    final Link link = (Link) link_manager.element.elementAt(n);
    final Node other = link.theOtherEnd(node_1);
    return other;
  }

  public Node getRandomSelectedCellLinkedTo(NodeManager node_manager,
    Node node_1) {
    final LinkManager link_manager = node_manager.link_manager;
    final int link_count = node_1.list_of_links.size();
    if (link_count == 0) {
      return null;
    }

    //for (int tries = 1000; --tries >= 0;) {
    final int x = this.rnd.nextInt(link_count);
    int counter = x;

    do {
      final int n = node_1.list_of_links.retreive(counter);
      final Link link = (Link) link_manager.element.elementAt(n);
      final Node other = link.theOtherEnd(node_1);
      if (other.type.selected) {
        return other;
      }

      counter = (counter + 1) % link_count;
    } while (counter != x);

    return null;
  }

  //        // for (int counter = number_of_nodes; --counter >= 0;) { // final int
  // // a2 = this.rnd.nextInt(number_of_nodes);
  //        final Node node_2 = (Node) node_manager.element.elementAt(a2);
  //        if (node_1 != node_2) {
  //          //if (node_1.type.colour == node_2.type.colour) {
  //          if (link_manager.isThereALinkBetween(node_1, node_2)) {
  //            return node_2;
  //          }
  //          //}
  //        }
  //      }
  //    }
  //
  //    return null;
  //  }

  public boolean isNodeTouchingNode(Node node_1, Node node_2) {
    final int sum_of_radii = node_1.type.radius + node_2.type.radius;
    final int max_distance = (sum_of_radii * 9) >> 3;
    final int max_distance_sh = max_distance >> Coords.shift;
    final int max_distance_sq = max_distance_sh * max_distance_sh;
    final int d_x = (node_1.pos.x - node_2.pos.x) >> Coords.shift;
    final int d_y = (node_1.pos.y - node_2.pos.y) >> Coords.shift;
    final int d_z = (node_1.pos.z - node_2.pos.z) >> Coords.shift;
    final int actual_distance_sq = (d_x * d_x) + (d_y * d_y) + (d_z * d_z);
    return actual_distance_sq < max_distance_sq;
  }
}