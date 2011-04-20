package com.springie.elements.nodes;

import java.util.Vector;

public class NodeTypeFactory {
  public Vector array = new Vector();

  public NodeType getNew(NodeType current) {
    final NodeType type = new NodeType(current);
    this.array.addElement(type);
    type.makeEqualTo(current);
    return type;
  }

  public  NodeType getNew() {
    final NodeType type = new NodeType();
    this.array.addElement(type);
    return type;
  }

//  public static NodeType getNew(NodeType type) {
//    // TODO Auto-generated method stub
//    return null;
//  }

//public public NodeTypeFactory(NodeType current) {
//final NodeType type = new NodeType();
//array.addElement(type);
//type.makeEqualTo(type)
//t
//return type;
//}

}
