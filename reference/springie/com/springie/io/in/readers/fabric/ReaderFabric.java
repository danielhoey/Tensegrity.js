//* Read in information from files...

package com.springie.io.in.readers.fabric;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.springie.io.in.ResourceLoader;
import com.springie.utilities.general.Forget;

public class ReaderFabric extends DefaultHandler {
  static final int scale_factor = 18000;
  static int node_count;
  static HashMap map;

  static String value;
  static String last_element;
  static String last_group_type;

  static int length;
  static boolean slack;

  static int x_min;
  static int y_min;
  static int z_min;
  static int x_max = 640 << 20;
  static int y_max = 640 << 20;
  static int z_max = 640 << 20;

  public static String translate(String source) throws IOException, SAXException {
    final XMLReader xr = new com.tifsoft.xml.driver.Driver();

    final ReaderFabric handler = new ReaderFabric();
    xr.setContentHandler(handler);
    xr.setErrorHandler(handler);

    value = "CR F:2048 NG R:0 C:0x0 ";
    map = new HashMap();
    node_count = 0;

    final Reader reader = new ResourceLoader().getReader(source);
    xr.parse(new InputSource(reader));
    reader.close();

    return ReaderFabric.value;
  }

  ////////////////////////////////////////////////////////////////////
  // Event handlers.
  ////////////////////////////////////////////////////////////////////

  public void startDocument() {
    //Log.log("Start document");
  }

  public void endDocument() {
    //Log.log("End document");
  }

  public void startElement(String uri, String name, String desc,
    Attributes atts) {
    Forget.about(name);
    boolean is_joint = false;
    boolean is_locus = false;
    boolean is_interval = false;
    boolean is_physics = false;
    int n1i = -1;
    int n2i = -1;

    if ("".equals(uri)) {
      last_element = desc;
      is_joint = "joint".equals(last_element);
      is_locus = "locus".equals(last_element);
      is_interval = "interval".equals(last_element);
      is_physics = "physics".equals(last_element);

      if (is_joint) {
        value += "N ";
      } else if (is_interval) {
        value += "LG E:50 C:0xFFFFFF80 ";
      }
    }

    final int n = atts.getLength();
    if (n > 0) {
      for (int i = 0; i < n; i++) {
        final String nam = atts.getLocalName(i);
        final String val = atts.getValue(i);
        //Log.log("Attribute-name: " + nam);
        //Log.log("Attribute-value: " + val);

        if (is_joint) {
          if ("id".equals(nam)) {
            map.put(val, "" + node_count++);
          }
        } else if (is_locus) {
          if ("x".equals(nam)) {
            final double x = Double.valueOf(val).doubleValue();
            final int xi = (int) (scale_factor * x);
            value += "X:" + xi + " ";
          } else if ("y".equals(nam)) {
            final double y = Double.valueOf(val).doubleValue();
            final int yi = (int) (scale_factor * y);
            value += "Z:" + yi + " ";
          } else if ("z".equals(nam)) {
            final double z = Double.valueOf(val).doubleValue();
            final int zi = (int) (scale_factor * -z);
            value += "Y:" + zi + " ";
          }
        } else if (is_interval) {
          if ("span".equals(nam)) {
            final double sp = Double.valueOf(val).doubleValue();
            final int spi = (int) (scale_factor * sp);
            value += "L:" + spi + " LK ";
          } else if ("alphaRef".equals(nam)) {
            final String n1 = (String) map.get(val);
            n1i = Integer.parseInt(n1);
          } else if ("omegaRef".equals(nam)) {
            final String n2 = (String) map.get(val);
            n2i = Integer.parseInt(n2);
          }
        } else if (is_physics) {
          value += "G ";
          if ("gravity".equals(nam)) {
            final double sp = Double.valueOf(val).doubleValue();
            final int spi = (int) (sp * 3);
            value += "GV:" + spi + " ";
          } else if ("gravityOn".equals(nam)) {
            value += "GO:" + ("true".equals(val) ? "1" : "0") + " ";
          }
        }
      }
    }

    if (is_interval) {
      value += "V:" + n1i + " ";
      value += "V:" + n2i + " ";
    }
  }

  public void endElement(String uri, String name, String desc) {
    Forget.about(name);
    Forget.about(desc);
    Forget.about(uri);
  }

  public void ignorableWhitespace(char[] ch, int start, int length) {
    characters(ch, start, length);
  }

  public void skippedEntity(String name) {
    Forget.about(name);
  }

  public void characters(char ch[], int start, int length) {
    Forget.about(ch);
    Forget.about(start);
    Forget.about(length);
  }
}
