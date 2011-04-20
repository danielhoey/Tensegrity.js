// * Read in information from files...

package com.springie.io.in.readers.spr.types;

import com.springie.elements.links.LinkType;

public class TypeLink extends TypeBase {
  public int length;            
  public int elasticity = LinkType.default_elasticity;            
  public int damping = LinkType.default_damping;                
  public boolean cable;
  public boolean disabled;
}
