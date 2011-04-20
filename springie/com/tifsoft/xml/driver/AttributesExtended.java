package com.tifsoft.xml.driver;

import org.xml.sax.Attributes;

import com.springie.utilities.general.Forget;

public class AttributesExtended implements Attributes {
  static String CDATA = "CDATA";
  private AttributesBasic aw;

  AttributesExtended(AttributesBasic aw) {
    this.aw = aw;
  }

  public int getLength() {
    return this.aw.attribute_values.size();
  }

  public String getURI(final int i) {
    return (String) this.aw.attribute_uris.elementAt(i);
  }

  public String getLocalName(final int i) {
    return (String) this.aw.attribute_local_names.elementAt(i);
  }

  public String getQName(final int i) {
    return (String) this.aw.attribute_qnames.elementAt(i);
  }

  public String getType(final int i) {
    Forget.about(i);
    return AttributesExtended.CDATA;
  }

  public String getValue(final int i) {
    return (String) this.aw.attribute_values.elementAt(i);
  }

  public int getIndex(final String uri, final String local_part) {
    int i = -1;

    while (true) {
      i = this.aw.attribute_local_names.indexOf(local_part, i + 1);

      if (i == -1 || uri.equals(this.aw.attribute_uris.elementAt(i))) {
        return i;
      }
    }
  }

  public int getIndex(final String q_name) {
    return this.aw.attribute_qnames.indexOf(q_name);
  }

  public String getType(final String uri, final String local_name) {
    Forget.about(uri);
    Forget.about(local_name);
    return AttributesExtended.CDATA;
  }

  public String getType(final String q_name) {
    Forget.about(q_name);
    return AttributesExtended.CDATA;
  }

  public String getValue(final String uri, final String local_name) {
    final int index = this.getIndex(uri, local_name);

    return (index == -1) ? null : (String) this.aw.attribute_values.elementAt(index);
  }

  public String getValue(final String q_name) {
    final int index = this.aw.attribute_qnames.indexOf(q_name);

    return (index == -1) ? null : (String) this.aw.attribute_values.elementAt(index);
  }

  public void setAw(AttributesBasic aw) {
    this.aw = aw;
  }

  public AttributesBasic getAw() {
    return this.aw;
  }
}