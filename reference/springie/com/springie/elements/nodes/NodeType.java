package com.springie.elements.nodes;

import com.springie.elements.base.BaseType;
import com.springie.utilities.random.Hortensius32Fast;

public class NodeType extends BaseType {
  public boolean fixed;

  public int charge;

  static Hortensius32Fast static_rnd = new Hortensius32Fast();

  protected NodeType() {
    setSize(18);
  }
  
  protected NodeType(NodeType current) {
    makeEqualTo(current);
  }
  
  public void setMass(int log_mass) {
    this.log_mass = log_mass;
  }

  public void setSize(int r) {
    this.radius = r;
  }
  
  public void makeEqualTo(NodeType t) {
    this.log_mass = t.log_mass;
    this.charge = t.charge;
    this.hidden = t.hidden;
    this.radius = t.radius;
    this.fixed = t.fixed;
    this.selected = t.selected;
  }
  
  public boolean equals(Object o) {
    final NodeType nt = (NodeType) o;
    if (this.radius != nt.radius) {
      return false;
    }
    
    if (this.hidden != nt.hidden) {
      return false;
    }
    
    if (this.log_mass != nt.log_mass) {
      return false;
    }
    
    if (this.charge != nt.charge) {
      return false;
    }
    
    if (this.fixed != nt.fixed) {
      return false;
    }
    
    return true;
  }
  
  public int hashCode() {
    return this.radius ^ this.charge;
  }
}
