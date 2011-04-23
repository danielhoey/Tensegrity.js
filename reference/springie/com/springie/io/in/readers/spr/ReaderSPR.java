// * Read in information from files...

package com.springie.io.in.readers.spr;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.util.StringTokenizer;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.springie.io.in.ResourceLoader;
import com.springie.io.in.readers.spr.types.TypeBase;
import com.springie.io.in.readers.spr.types.TypeFace;
import com.springie.io.in.readers.spr.types.TypeLink;
import com.springie.io.in.readers.spr.types.TypeNode;
import com.springie.render.Coords;
import com.springie.utilities.general.Forget;

public class ReaderSPR extends DefaultHandler {
  final int scale_factor = 1;

  int node_count;

  StringBuffer out = new StringBuffer();

  String last_element;

  String last_group_type;

  boolean in_nodes_section;

  boolean in_links_section;

  boolean in_faces_section;

  boolean in_node;

  boolean in_link;

  boolean in_face;

  int x_min;

  int y_min;

  int z_min;

  int x_max = 640 << 20;

  int y_max = 640 << 20;

  int z_max = 640 << 20;

  int number;

  TypeBase type;

  int clazz_colour;

  TypeNode type_node;

  TypeLink type_link;

  TypeFace type_face;
  
  public String translate(String filename) throws IOException, SAXException {
    final XMLReader xr = new com.tifsoft.xml.driver.Driver();

    final ReaderSPR handler = new ReaderSPR();
    xr.setContentHandler(handler);
    xr.setErrorHandler(handler);

    handler.out.append("CR NG D:8000 C:0xff20ff20 ");
    handler.type = new TypeBase();
    handler.node_count = 0;

    final Reader reader = new ResourceLoader().getReader(filename);
    xr.parse(new InputSource(reader));
    reader.close();

    return handler.out.toString();
  }

  public String translateString(String source_data) throws IOException,
    SAXException {
    final XMLReader xr = new com.tifsoft.xml.driver.Driver();

    final ReaderSPR handler = new ReaderSPR();
    xr.setContentHandler(handler);
    xr.setErrorHandler(handler);

    handler.out.append("CR NG D:8000 C:0xff20ff20 ");
    handler.type = new TypeBase();
    handler.node_count = 0;

    final char[] ca = source_data.toCharArray();
    final Reader reader = new CharArrayReader(ca);
    xr.parse(new InputSource(reader));
    reader.close();

    return handler.out.toString();
  }

  public void startDocument() {
    //...
  }

  public void endDocument() {
    //...
  }

  public void startElement(String uri, String name, String qName,
    Attributes atts) {
    Forget.about(name);
    boolean is_nodes = false;
    boolean is_links = false;
    boolean is_faces = false;
    boolean is_type = false;
    boolean is_node = false;
    boolean is_class = false;
    boolean is_link = false;
    boolean is_face = false;
    boolean is_universe = false;
    boolean is_position = false;
    boolean is_velocity = false;

    if ("".equals(uri)) {
      this.last_element = qName;
      is_nodes = "nodes".equals(this.last_element);
      is_links = "links".equals(this.last_element);
      is_faces = "faces".equals(this.last_element);
      is_node = "node".equals(this.last_element);
      is_link = "link".equals(this.last_element);
      is_face = "face".equals(this.last_element);
      is_class = "class".equals(this.last_element);
      is_type = "type".equals(this.last_element);
      is_position = "position".equals(this.last_element);
      is_velocity = "velocity".equals(this.last_element);
      is_universe = "universe".equals(this.last_element);

      if (is_nodes) {
        this.in_nodes_section = true;
      }

      if (is_links) {
        this.in_links_section = true;
      }

      if (is_faces) {
        this.in_faces_section = true;
      }

      if (is_node) {
        this.in_node = true;
      }

      if (is_link) {
        this.in_link = true;
        this.number = 1;
      }

      if (is_face) {
        this.in_face = true;
        this.number = 1;
      }

      if (is_node) {
        if (this.in_nodes_section) {
          this.out.append("N ");
        }
      } else if (is_link) {
        this.out.append("LK ");
      } else if (is_face) {
        this.out.append("P ");
      } else if (is_universe) {
        this.out.append("G ");
      }
    }

    if (is_type) {
      if (this.in_nodes_section) {
        this.type_node = new TypeNode();
        this.type = this.type_node;
        this.out.append("NG ");
      } else if (this.in_links_section) {
        this.type_link = new TypeLink();
        this.type = this.type_link;
        this.out.append("LG ");
      } else if (this.in_faces_section) {
        this.type_face = new TypeFace();
        this.type = this.type_face;
        this.out.append("PG ");
      }
    } else if (is_class) {
      this.clazz_colour = -1;
    }

    final int n = atts.getLength();
    if (n > 0) {
      for (int i = 0; i < n; i++) {
        final String nam = atts.getLocalName(i);
        final String val = atts.getValue(i);
        if (is_position) {
          if ("x".equals(nam)) {
            final double x = Double.valueOf(val).doubleValue();
            final int xi = (int) (this.scale_factor * x);
            this.out.append("X:" + xi + " ");
          } else if ("y".equals(nam)) {
            final double z = Double.valueOf(val).doubleValue();
            final int zi = Coords.getInternalFromPixelCoords(Coords.z_pixels)
              - (int) (this.scale_factor * z);
            this.out.append("Z:" + zi + " ");
          } else if ("z".equals(nam)) {
            final double y = Double.valueOf(val).doubleValue();
            final int yi = (int) (this.scale_factor * y);
            this.out.append("Y:" + yi + " ");
          }
        } else if (is_velocity) {
          if ("x".equals(nam)) {
            final double x = Double.valueOf(val).doubleValue();
            final int xi = (int) (this.scale_factor * x);
            this.out.append("DX:" + xi + " ");
          } else if ("y".equals(nam)) {
            final double z = Double.valueOf(val).doubleValue();
            final int zi = (int) (this.scale_factor * -z);
            this.out.append("DZ:" + zi + " ");
          } else if ("z".equals(nam)) {
            final double y = Double.valueOf(val).doubleValue();
            final int yi = (int) (this.scale_factor * y);
            this.out.append("DY:" + yi + " ");
          }
        } else if (is_node) {
          if ((this.in_link) || (this.in_face)) {
            outputNodeNumber(Integer.parseInt(val));
          }
        } else if (is_link || is_face) {
          if ("nodes".equals(nam)) {
            final StringTokenizer st = new StringTokenizer(val);
            while (st.hasMoreTokens()) {
              final String tok = st.nextToken();

              final int node = Integer.parseInt(tok);
              outputNodeNumber(node);
            }
          }
        } else if (is_type) {
          if ("radius".equals(nam)) {
            final double sp = Double.valueOf(val).doubleValue();
            this.type.radius = (int) (this.scale_factor * sp);
          } else if ("hidden".equals(nam)) {
            this.type.hidden = "true".equals(val);
          } else if ("fixed".equals(nam)) {
            this.type_node.fixed = "true".equals(val);
          } else if ("charge".equals(nam)) {
            this.type_node.charge = (int) Long.parseLong(val);
          } else if ("disabled".equals(nam)) {
            this.type_link.disabled = "true".equals(val);
          } else if ("elasticity".equals(nam)) {
            this.type_link.elasticity = (int) Long.parseLong(val);
          } else if ("damping".equals(nam)) {
            this.type_link.damping = (int) Long.parseLong(val);
          } else if ("length".equals(nam)) {
            final double sp = Double.valueOf(val).doubleValue();
            this.type_link.length = (int) (this.scale_factor * sp);
          } else if ("radius".equals(nam)) {
            final double sp = Double.valueOf(val).doubleValue();
            this.type_link.radius = (int) (this.scale_factor * sp);
          } else if ("cable".equals(nam)) {
            this.type_link.cable = true;
          } else if ("color".equals(nam)) {
            this.clazz_colour = (int) Long.parseLong(val.substring(1), 16);
          } else if ("elasticity".equals(nam)) {
            this.type_link.elasticity = (int) Long.parseLong(val);
          }
        } else if (is_class) {
          if ("opacity".equals(nam)) {
            final float t = getFloat(val);
            this.clazz_colour = (this.clazz_colour & 0xFFFFFF)
              | ((int) (t * 255) << 24);
          } else if ("red".equals(nam)) {
            final float r = getFloat(val);
            this.clazz_colour = (this.clazz_colour & 0xFF00FFFF)
              | ((int) (r * 255) << 16);
          } else if ("green".equals(nam)) {
            final float g = getFloat(val);
            this.clazz_colour = (this.clazz_colour & 0xFFFF00FF)
              | ((int) (g * 255) << 8);
          } else if ("blue".equals(nam)) {
            final float b = getFloat(val);
            this.clazz_colour = (this.clazz_colour & 0xFFFFFF00)
              | (int) (b * 255);
          }
        } else if (is_universe) {
          if ("gravity_strength".equals(nam)) {
            final double sp = Double.valueOf(val).doubleValue();
            final int spi = (int) sp;
            this.out.append("GS:" + spi + " ");
          } else if ("gravity_active".equals(nam)) {
            this.out.append("GA:" + ("true".equals(val) ? "1" : "0") + " ");
          } else if ("charge_strength".equals(nam)) {
            final double sp = Double.valueOf(val).doubleValue();
            final int spi = (int) sp;
            this.out.append("CS:" + spi + " ");
          } else if ("dimensions".equals(nam)) {
            final int dimensions = Integer.parseInt(val);
            this.out.append("DIM:" + dimensions + " ");
          } else if ("charge_active".equals(nam)) {
            this.out.append("CA:" + ("true".equals(val) ? "1" : "0") + " ");
          }
        }
      }
    }

    if (is_type) {
      if (this.in_nodes_section) {
        this.out.append("NG R:" + this.type.radius + " ");

        if (this.type_node.fixed) {
          this.out.append("FX:1 ");
        }
        if (this.type_node.charge != 0) {
          this.out.append("CH:" + this.type_node.charge + " ");
        }
      } else if (this.in_links_section) {
        this.out.append("LG L:" + this.type_link.length + " ");

        this.out.append("E:" + this.type_link.elasticity + " ");
        this.out.append("DA:" + this.type_link.damping + " ");

        if (this.type_link.radius != 0) {
          this.out.append("R:" + this.type_link.radius + " ");
        }

        if (this.type_link.cable) {
          this.out.append("CA:1 ");
        }
        if (this.type_link.disabled) {
          this.out.append("D:1 ");
        }
      }

      final long colour = this.clazz_colour & 0xFFFFFFFFL;
      this.out.append("C:0x" + Long.toString(colour, 16) + " ");
      if (this.type.hidden) {
        this.out.append("H:1 ");
      }
    }
  }

  private void outputNodeNumber(final int node1) {
    this.out.append("V:"); // + this.number + ":");
    this.out.append(node1 + " ");
    this.number++;
  }

  private float getFloat(final String val) {
    final float r = Float.valueOf(val).floatValue();
    return r;
  }

  public void endElement(String uri, String name, String tag) {
    Forget.about(name);
    if ("".equals(uri)) {
      if ("nodes".equals(tag)) {
        this.in_nodes_section = false;
      } else if ("links".equals(tag)) {
        this.in_links_section = false;
      } else if ("faces".equals(tag)) {
        this.in_faces_section = false;
      } else if ("node".equals(tag)) {
        this.in_node = false;
      } else if ("link".equals(tag)) {
        this.in_link = false;
      } else if ("face".equals(tag)) {
        this.in_face = false;
      }
    }
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