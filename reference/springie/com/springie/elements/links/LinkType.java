package com.springie.elements.links;
import com.springie.elements.base.BaseType;

public class LinkType extends BaseType {
  public static final int default_elasticity = 50;

  public static final int default_damping = 95;
  
  public int length;
  
  public boolean cable;
  
  public int elasticity = default_elasticity;

  public int damping = default_damping;

  protected LinkType(int l, int e) {
    setLength(l);
    this.radius = l >> 3;
    this.elasticity = e;
    //this.amplitude = 0;
    //this.phase = 0;
  }

  protected LinkType() {
    // simply do nothing...
  }

  /**
   * Creates a link between e1 and e1, with length l and elasticity e
   */
  void set(int l, int e) {
    setLength(l);
    this.elasticity = e;
  }

  /**
   * Changes the actual length of a strut...
   */
  public void setLength(int length) {
    this.length = length;
  }

  /**
   * Changes the elasticity of a link
   */
  public void setElasticity(int e) {
    this.elasticity = e;
  }

  /**
   * Changes the amplitude of any oscillations in a link
   */
  public boolean equals(Object o) {
    final LinkType lt = (LinkType) o;
    
    if (this.length != lt.length) {
      return false;
    }
    
    if (this.radius != lt.radius) {
      return false;
    }
    
    if (this.hidden != lt.hidden) {
      return false;
    }

    if (this.elasticity != lt.elasticity) {
      return false;
    }
    
    if (this.damping != lt.damping) {
      return false;
    }
    
    return true;
  }
  
  public int hashCode() {
   return this.elasticity ^ this.length;
  }

  public void makeEqualTo(LinkType lt) {
    this.length = lt.length;
    this.cable = lt.cable;
    this.hidden = lt.hidden;
    this.elasticity = lt.elasticity;
    this.damping = lt.damping;
    this.radius = lt.radius;
    this.selected = lt.selected;
  }
}
