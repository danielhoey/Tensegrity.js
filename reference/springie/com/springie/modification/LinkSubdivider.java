package com.springie.modification;

import com.springie.elements.clazz.Clazz;
import com.springie.elements.links.Link;
import com.springie.elements.links.LinkManager;
import com.springie.elements.links.LinkType;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.elements.nodes.NodeType;
import com.springie.geometry.Point3D;
import com.springie.geometry.Vector3D;
import com.springie.modification.pre.PrepareToModifyLinkTypes;
import com.springie.utilities.random.JUR;

public class LinkSubdivider {
  JUR rnd = new JUR();

  NodeManager node_manager;

  LinkManager link_manager;

  public LinkSubdivider(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;
    prepare();
  }

  public void divide(int n, boolean remove_old) {
    if (n < 2) {
      return;
    }

    final int xn = this.rnd.nextInt() & 0x1F1F1F;
    final int xc = this.rnd.nextInt() & 0x1F1F1F;
    
    final int n_o_l = this.link_manager.element.size();
    for (int temp = n_o_l; --temp >= 0;) {
      final Link l = (Link) this.link_manager.element.elementAt(temp);
      if (l.type.selected) {
        subdivide(l, n, xn, xc, remove_old);
      }
    }
  }

  public void subdivide(Link l, int n, int xn, int xc, boolean remove_old) {
    final Node n1 = l.node1;
    final Node n2 = l.node2;

    int length = l.type.length;
    final int new_colour_link = l.clazz.colour ^ xc;
    final int elasticity = l.type.elasticity;
    final Point3D e1 = (Point3D) n1.pos.clone();
    final Point3D e2 = (Point3D) n2.pos.clone();

    final Vector3D v = new Vector3D(e2);
    v.subtractTuple3D(e1);
    final Vector3D v_small = (Vector3D) v.clone();
    v_small.divideBy(n);
    length = (int) ((length * 0.8)/ n);
    Node first = n1;
    
    final int new_colour_node = n1.clazz.colour ^ xn;
    final int new_radius = (n1.type.radius + n2.type.radius) >> 2;
    
    for (int i = 0; i < n; i++) {
      Node last;
      if (i < n - 1) {
        final Point3D e3 = (Point3D) n1.pos.clone();
        final Vector3D sum = (Vector3D) v_small.clone();
        sum.multiplyBy(i + 1);
        e3.addTuple3D(sum);
        // make node...
        last = makeNode(e3, new_radius, new_colour_node);
      } else {
        last = n2;
      }
      makeLink(first, last, length, elasticity, new_colour_link);
      first = last;
    }
    
    //Link lk = this.link_manager.linkBetween(n1,n2);
    if (remove_old) {
      this.link_manager.killSpecifiedLink(l);
    }
  }

  private void makeLink(Node node_1, Node node_2, int l, int e, int c) {
    final LinkType type = this.link_manager.link_type_factory.getNew(l, e);
    final Clazz clazz = this.node_manager.clazz_factory.getNew(c);
    this.link_manager.setLink(node_1, node_2, type, clazz);
  }

  private Node makeNode(Point3D pos, int r, int c) {
    final NodeType type = this.node_manager.node_type_factory.getNew();
    type.radius = r;
    type.log_mass = 4;

    final Clazz clazz = this.node_manager.clazz_factory.getNew(c);

    final Node added = this.node_manager.addNewAgent(pos, clazz, type);
    
    return added;
  }

  void prepare() {
    new PrepareToModifyLinkTypes(this.node_manager.link_manager).prepare();
  }
}