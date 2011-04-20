package com.tifsoft.xml.writer;

import java.util.Vector;

public class XMLWriterSinglet implements XMLWriterInterface {
  String name;

  Vector attributes;

  public XMLWriterSinglet(String name, Vector arguments) {
    this.name = name;
    this.attributes = arguments;
  }

  public XMLWriterSinglet(String name) {
    this.name = name;
  }

  public String makeString() {
    return makeString(0);
  }

  public String makeString(int indent) {
    final StringBuffer sb = new StringBuffer();

    if (this.name != null) {
      XMLWriterUtilities.indent(sb, indent);
      sb.append("<");
      sb.append(this.name);

      if (this.attributes != null) {
        final int arguments_size = this.attributes.size();
        for (int i = 0; i < arguments_size; i++) {
          final XMLWriterAttribute arg = (XMLWriterAttribute) this.attributes.elementAt(i);
          sb.append(" " + arg.toString());
        }
      }

      sb.append("/>\n");
    }

    return sb.toString();
  }

  public Vector getAttributes() {
    return this.attributes;
  }

  public void setAttributes(Vector arguments) {
    this.attributes = arguments;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void add(XMLWriterAttribute attribute) {
    ensureAttributesExist();

    this.attributes.addElement(attribute);
  }

  private void ensureAttributesExist() {
    if (this.attributes == null) {
      this.attributes = new Vector();
    }
  }
}