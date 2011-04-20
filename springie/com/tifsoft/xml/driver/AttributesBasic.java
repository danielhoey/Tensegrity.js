package com.tifsoft.xml.driver;

import java.util.Vector;

public class AttributesBasic {
  final Vector attribute_uris = new Vector();
  final Vector attribute_local_names = new Vector();
  public final Vector attribute_qnames = new Vector();
  final Vector attribute_values = new Vector();

  
  void removeAllElements() {
    this.attribute_uris.removeAllElements();
    this.attribute_local_names.removeAllElements();
    this.attribute_qnames.removeAllElements();
    this.attribute_values.removeAllElements();
  }
}
