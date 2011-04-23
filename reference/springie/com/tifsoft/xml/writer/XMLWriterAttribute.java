package com.tifsoft.xml.writer;

public class XMLWriterAttribute {
  String name;
  String value;

  public XMLWriterAttribute(String name, String value) {
    this.name = name;
    this.value = value;
  }
  
  public String toString() {
    final StringBuffer sb = new StringBuffer();

    sb.append(this.name);
    sb.append("=\"");
    sb.append(this.value);
    sb.append("\"");

    return sb.toString();
  }
}
