package com.springie.elements.faces;
import com.springie.elements.base.BaseType;

public class FaceType extends BaseType {
  protected FaceType() {
    // nothing yet...
  }
  
  public boolean equals(Object o) {
    final FaceType t = (FaceType) o;
   
    if (this.hidden != t.hidden) {
      return false;
    }
    
    if (this.selected != t.selected) {
      return false;
    }
    
    return true;
  }
  
  public int hashCode() {
    return 0;
  }
  
  public void makeEqualTo(FaceType t) {
    this.hidden = t.hidden;
    this.selected = t.selected;
  }
}
