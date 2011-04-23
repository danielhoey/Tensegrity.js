// This program has been placed into the public domain by its author.

package com.springie.io.in.readers.eig;

import java.io.IOException;
import java.io.Reader;
import java.util.StringTokenizer;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.springie.io.in.ResourceLoader;
import com.springie.presets.ColourFactory;
import com.springie.utilities.general.Forget;
import com.tifsoft.xml.driver.SomeStringUtilities;

public class ReaderEIG extends DefaultHandler {
  final int scale_factor = 8000;

  String value;

  String last_element;

  String last_group_type;

  int length;

  boolean disabled;

  int x_min;

  int y_min;

  int z_min;

  int x_max = 640 << 20;

  int y_max = 640 << 20;

  int z_max = 640 << 20;

  private int node_group_count;

  private int link_group_count;

  public String translate(String source) throws IOException, SAXException {
    final XMLReader xr = new com.tifsoft.xml.driver.Driver();

    final ReaderEIG handler = new ReaderEIG();
    xr.setContentHandler(handler);
    xr.setErrorHandler(handler);

    handler.value = "CR ";

    final Reader reader = new ResourceLoader().getReader(source);
    xr.parse(new InputSource(reader));
    reader.close();

    //Log.log(handler.value);

    return handler.value;
  }

  public void startDocument() {
    //Log.log("Start document");
  }

  public void endDocument() {
    //Log.log("End document");
  }

  public void startElement(String uri, String name, String qName,
    Attributes atts) {
    Forget.about(name);
    final int[] colours = new ColourFactory(17854).getColourArray(64);
    boolean node_group = false;
    boolean link_group = false;
    boolean polygon_group = false;
    boolean group_eig = false;

    if ("".equals(uri)) {
      //Log.log("Start element: " + qName);
      this.last_element = qName;
      link_group = "SPRINGSET".equals(this.last_element);
      node_group = "JOINTSET".equals(this.last_element);
      polygon_group = "SKIN".equals(this.last_element);
      group_eig = "EIG".equals(this.last_element);

      if (link_group) {
        this.value += "LG ";
        this.disabled = false;
      } else if (node_group) {
        final int idx = this.node_group_count & 7;
        final int colour = (colours[idx] + this.node_group_count) | 0x808080;

        this.value += "NG R:0 C:0xFF" + Integer.toString(colour, 16) + " ";
        this.node_group_count++;
      } else if (polygon_group) {
        this.value += "PG ";
      }
    }

    int colour = 0;

    final int n = atts.getLength();
    if (n > 0) {
      for (int i = 0; i < n; i++) {
        final String nam = atts.getLocalName(i);
        final String val = atts.getValue(i);
        //Log.log("Attribute-qname: " + atts.getQName(i));
        //Log.log("Attribute-type: " + atts.getType(i));
        //Log.log("Attribute-name: " + nam);
        //Log.log("Attribute-value: " + val);

        if (link_group) {
          if ("span".equals(nam)) {
            final double span = Double.valueOf(val).doubleValue();
            this.length = (int) (this.scale_factor * span);
            this.value += "L:" + this.length + " ";
          } else if ("slack".equals(nam)) {
            this.disabled = "true".equals(val);
          } else if ("hidden".equals(nam)) {
            if ("true".equals(val)) {
              this.value += "H:1 ";
            }
          }
        } else if (node_group) {
          if ("hidden".equals(nam)) {
            if ("true".equals(val)) {
              this.value += "H:1 ";
            }
          }
        } else if (polygon_group) {
          if ("hidden".equals(nam)) {
            if ("true".equals(val)) {
              this.value += "H:1 ";
            }
          } else if ("red".equals(nam)) {
            colour = colour
              | ((int) (Double.valueOf(val).doubleValue() * 255)) << 16;
          } else if ("green".equals(nam)) {
            colour = colour
              | ((int) (Double.valueOf(val).doubleValue() * 255)) << 8;
          } else if ("blue".equals(nam)) {
            colour = colour | (int) (Double.valueOf(val).doubleValue() * 255);
          }
        } else if (group_eig) {
          this.value += "G ";
          if ("GravityAccel".equals(nam)) {
            final double sp = Double.valueOf(val).doubleValue();
            final int spi = (int) (sp * -40000);
            this.value += "GV:" + spi + " ";
          } else if ("Gravity".equals(nam)) {
            this.value += "GO:" + ("true".equals(val) ? "1" : "0") + " ";
          }
        }
      }
    }

    if (link_group) {
      final int idx = this.link_group_count & 7;
      colour = (colours[idx] + this.link_group_count) | 0x808080;
      this.link_group_count++;

      if (this.disabled) {
        this.value += "D:1 ";
      }
      //colour = colour | 0xC0C0C0;
      //this.value += "R:500 E:40 ";
      //} else {
      this.value += "R:1000 E:40 ";
      //}
      this.value += "C:0xFF" + Integer.toString(colour, 16) + " ";
    } else if (polygon_group) {
      this.value += "C:0xFF" + Integer.toString(colour, 16) + " ";
    }
  }

  public void endElement(String uri, String name, String qName) {
    Forget.about(name);
    Forget.about(qName);

    //if ("SPRINGSET".equals(qName)) {
    //link_group = false;
    //}
    //if ("JOINTSET".equals(qName)) {
    //node_group = false;
    //}

    if ("".equals(uri)) {
      //Log.log("End element: " + qName);
    } else {
      //Log.log("End element: {" + uri + "}" + name);
    }
  }

  public void ignorableWhitespace(char[] ch, int start, int length) {
    //Log.put("ignorableWhitespace:");
    characters(ch, start, length);
  }

  public void skippedEntity(String name) {
    Forget.about(name);
    //Log.put("skippedEntity:" + name);
  }

  public void characters(char ch[], int start, int length) {
    final String chars = new String(ch, start, length);
    if ("JOINT".equals(this.last_element)) {
      addNode(chars);
    } else if ("SPRING".equals(this.last_element)) {
      addLink(chars);
    } else if ("TRIANGLE".equals(this.last_element)) {
      addPolygon(chars);
    }
  }

  private void addLink(String chars) {
    if (SomeStringUtilities.isWhiteSpace(chars)) {
      return;
    }

    final StringTokenizer st = new StringTokenizer(chars, " ");

    final String s_a = st.nextToken();
    final String s_z = st.nextToken();

    final int a = Integer.parseInt(s_a);
    final int z = Integer.parseInt(s_z);

    final String type = "LK";

    this.value += type + " V:" + a + " V:" + z + " ";
  }

  private void addPolygon(String chars) {
    if (SomeStringUtilities.isWhiteSpace(chars)) {
      return;
    }

    final StringTokenizer st = new StringTokenizer(chars, " ");

    final String n1 = st.nextToken();
    final String n2 = st.nextToken();
    final String n3 = st.nextToken();

    final int v1 = Integer.parseInt(n1);
    final int v2 = Integer.parseInt(n2);
    final int v3 = Integer.parseInt(n3);

    final String type = "P ";

    this.value += type + " V:" + v1 + " V:" + v2 + " V:" + v3 + " ";
  }

  private void addNode(String chars) {
    final StringTokenizer st = new StringTokenizer(chars, " ");

    st.nextToken();
    final String s_x = st.nextToken();
    final String s_y = st.nextToken();
    final String s_z = st.nextToken();

    //Log.log(s_x + " - " + s_y);

    final int x = (int) (Double.valueOf(s_x).doubleValue() * this.scale_factor);
    final int y = (int) (Double.valueOf(s_y).doubleValue() * -this.scale_factor);
    final int z = (int) (Double.valueOf(s_z).doubleValue() * this.scale_factor);

    //Log.log("X: " + x + " Y:" + y + " Z:" + z);

    this.value += "N X:" + x + " Y:" + y + " Z:" + z;
    //Log.log("N X:" + x + " Y:" + y + " Z:" + z);
    this.value += " DX:0 DY:0 DZ:0 ";
  }
}