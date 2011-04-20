package com.springie.io.out.writers.eig;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Vector;

import com.springie.FrEnd;
import com.springie.elements.clazz.Clazz;
import com.springie.elements.faces.Face;
import com.springie.elements.faces.FaceManager;
import com.springie.elements.faces.FaceType;
import com.springie.elements.links.Link;
import com.springie.elements.links.LinkManager;
import com.springie.elements.links.LinkType;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.elements.nodes.NodeType;
import com.springie.geometry.Point3D;
import com.springie.io.out.GarbageCollection;
import com.springie.io.out.writers.wrl.WriterWRL;
import com.springie.metrics.BoundingBox;
import com.springie.modification.redundancy.RedundancyRemover;
import com.springie.render.Coords;
import com.springie.utilities.log.Log;
import com.tifsoft.xml.writer.XMLWriterAttribute;
import com.tifsoft.xml.writer.XMLWriterCharacters;
import com.tifsoft.xml.writer.XMLWriterTagPair;

public class WriterEIG {
  float scale_factor;

  BoundingBox bb;

  Point3D middle = new Point3D(0, 0, 0);

  //  static final int scale_factor = 1;

  Writer out;

  Vector nodes;

  private NodeManager node_manager;

  private LinkManager link_manager;

  private FaceManager face_manager;

  int count_joints;

  int count_springs;

  int count_skins;

  int number_jointsets;

  int number_springsets;

  int number_skinsets;

  public WriterEIG(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;
    this.face_manager = node_manager.face_manager;
  }

  public void write(String filename) {
    new GarbageCollection(this.node_manager).cleanUp();
    new RedundancyRemover(FrEnd.node_manager).removeRedundancy();

    this.bb = new BoundingBox();
    this.bb.find(this.node_manager);

    final float sfx = 2f / (this.bb.max.x - this.bb.min.x);
    final float sfy = 2f / (this.bb.max.y - this.bb.min.y);
    final float sfz = 2f / (this.bb.max.z - this.bb.min.z);
    this.scale_factor = Math.min(sfx, Math.min(sfy, sfz));
    this.scale_factor *= 2;

    this.middle.x = (this.bb.max.x + this.bb.min.x) >> 1;
    this.middle.y = (this.bb.max.y + this.bb.min.y) >> 1;
    this.middle.z = (this.bb.max.z + this.bb.min.z) >> 1;

    this.nodes = new Vector();

    final int n_nodes = this.node_manager.element.size();

    try {
      this.out = new FileWriter(filename);
      final XMLWriterTagPair eig = new XMLWriterTagPair("EIG");

      eig.add(new XMLWriterAttribute("joints", "" + n_nodes));

      recursivelyOutputAllNodes(eig);
      recursivelyOutputAllLinks(eig);
      recursivelyOutputAllPolygons(eig);
      this.number_springsets = 0;

      eig.add(new XMLWriterAttribute("jointsets", "" + this.number_jointsets));
      eig.add(new XMLWriterAttribute("springsets", "" + this.number_springsets));
      eig.add(new XMLWriterAttribute("skinsets", "" + this.number_skinsets));

      eig.add(new XMLWriterAttribute("iterationsPerTick", "8"));
      eig.add(new XMLWriterAttribute("Springconstant", "0.15"));
      eig.add(new XMLWriterAttribute("dampingPercent", "10"));
      eig.add(new XMLWriterAttribute("ViscousDrag", "0.05"));
      eig.add(new XMLWriterAttribute("Dampingconstant", "0.1"));
      eig.add(new XMLWriterAttribute("Gravity", "false"));

      //eig.add(new XMLWriterAttribute("degreesPerTick", "0"));
      //eig.add(new XMLWriterAttribute("phaseAngle", "0"));

      this.out.write("<?xml version=\"1.0\"?>\n");
      this.out.write(eig.makeString());
      this.out.flush();
      this.out.close();
    } catch (IOException e) {
      Log.log("Error: " + e);
    }
  }

  private void recursivelyOutputAllLinks(final XMLWriterTagPair uni) {
    final XMLWriterTagPair links = new XMLWriterTagPair("SPRINGSETS_IGNOR");
    final boolean any = outputLinkClasses(links);

    if (any) {
      uni.addContentsOf(links);
    }
  }

  private void recursivelyOutputAllPolygons(final XMLWriterTagPair uni) {
    final XMLWriterTagPair polygons = new XMLWriterTagPair("SKINSETS_IGNOR");
    final boolean any = outputFaceClasses(polygons);
    if (any) {
      uni.add(polygons);
    }
  }

  private void recursivelyOutputAllNodes(final XMLWriterTagPair eig) {
    final XMLWriterTagPair nodes = new XMLWriterTagPair("JOINTSETS_INVIS");
    final boolean any = outputNodeClasses(nodes);
    if (any) {
      eig.addContentsOf(nodes);
    }
  }

  private boolean outputNodeClasses(final XMLWriterTagPair uni) {
    //Log.log("outputNodeClasses");
    final int clazz_number = this.node_manager.clazz_factory.array.size();
    boolean some = false;
    for (int c = 0; c < clazz_number; c++) {
      final Clazz clazz = (Clazz) this.node_manager.clazz_factory.array.elementAt(c);
      final XMLWriterTagPair tag_type = new XMLWriterTagPair("CLASS_INVIS");
      outputColour(clazz, tag_type);
      final boolean any = outputNodeTypes(tag_type, clazz);
      if (any) {
        uni.addContentsOf(tag_type);
        some = true;
      }
    }
    return some;
  }

  private void outputColour(final Clazz clazz, final XMLWriterTagPair tag_type) {
    final WriterWRL writer = new WriterWRL(this.node_manager);

    final float t = (clazz.colour >>> 24) / 255f;
    if (t < 0.999f) {
      tag_type.add(new XMLWriterAttribute("opacity", writer.emit(t)));
    }
    final float r = ((clazz.colour >>> 16) & 0xFF) / 255f;
    if (r < 0.999f) {
      tag_type.add(new XMLWriterAttribute("red", writer.emit(r)));
    }

    final float g = ((clazz.colour >>> 8) & 0xFF) / 255f;
    if (g < 0.999f) {
      tag_type.add(new XMLWriterAttribute("green", writer.emit(g)));
    }

    final float b = (clazz.colour & 0xFF) / 255f;
    if (b < 0.999f) {
      tag_type.add(new XMLWriterAttribute("blue", writer.emit(b)));
    }
  }

  private boolean outputNodeTypes(final XMLWriterTagPair uni, Clazz clazz) {
    final int node_type_number = FrEnd.node_manager.node_type_factory.array
      .size();
    boolean some = false;
    for (int nt = 0; nt < node_type_number; nt++) {
      final NodeType node_type = (NodeType) FrEnd.node_manager.node_type_factory.array
        .elementAt(nt);

      this.count_joints = 0;
      final XMLWriterTagPair tag_type = new XMLWriterTagPair("JOINTSET");
      this.number_jointsets++;
      this.count_joints = 0;

      tag_type.add(new XMLWriterAttribute("hidden", "" + node_type.hidden));
      
      final boolean any = outputNodes(FrEnd.node_manager, clazz, node_type,
        tag_type);

      if (any) {
        tag_type.add(new XMLWriterAttribute("count", "" + this.count_joints));

        uni.add(tag_type);
        some = true;
      }
    }
    return some;
  }

  private boolean outputLinkClasses(final XMLWriterTagPair uni) {
    final int clazz_number = this.node_manager.clazz_factory.array.size();
    boolean some = false;
    for (int c = 0; c < clazz_number; c++) {
      final Clazz clazz = (Clazz) this.node_manager.clazz_factory.array.elementAt(c);
      final XMLWriterTagPair tag_type = new XMLWriterTagPair("CLASS_INVIS");
      outputColour(clazz, tag_type);
      final boolean any = outputLinkTypes(tag_type, clazz);
      if (any) {
        uni.addContentsOf(tag_type);
        some = true;
      }
    }
    return some;
  }

  private boolean outputLinkTypes(final XMLWriterTagPair uni, Clazz clazz) {
    final int link_type_number = this.link_manager.link_type_factory.array
      .size();
    boolean some = false;

    for (int lt = 0; lt < link_type_number; lt++) {
      final LinkType link_type = (LinkType) this.link_manager.link_type_factory.array
        .elementAt(lt);
      final XMLWriterTagPair tag_type = new XMLWriterTagPair("SPRINGSET");
      this.number_springsets++;
      this.count_springs = 0;

      final float span = toFloatingPointCoords(link_type.length);

      tag_type.add(new XMLWriterAttribute("span", "" + emit(span)));
      tag_type.add(new XMLWriterAttribute("Stylename", "PurpleCylinder"));

      tag_type.add(new XMLWriterAttribute("hidden", "" + link_type.hidden));

      if (link_type.disabled) {
        tag_type.add(new XMLWriterAttribute("disabled", "true"));
      }

      final boolean any = outputLinks(clazz, link_type, tag_type);
      if (any) {
        tag_type.add(new XMLWriterAttribute("count", "" + this.count_springs));
        tag_type.add(new XMLWriterAttribute("class", "springset_"
          + this.number_springsets));
        tag_type.add(new XMLWriterAttribute("name", "springset_"
          + this.number_springsets));
        uni.add(tag_type);
        some = true;
      }
    }
    return some;
  }

  private boolean outputFaceClasses(final XMLWriterTagPair uni) {
    final int clazz_number = this.node_manager.clazz_factory.array.size();
    boolean some = false;
    for (int c = 0; c < clazz_number; c++) {
      final Clazz clazz = (Clazz) this.node_manager.clazz_factory.array.elementAt(c);
      final XMLWriterTagPair tag_type = new XMLWriterTagPair("CLASS_INVIS");
      outputColour(clazz, tag_type);
      //      tag_type.add(new XMLWriterAttribute("color", "#"
      //        + Integer.toHexString(clazz.colour)));
      final boolean any = outputFaceTypes(tag_type, clazz);
      if (any) {
        uni.addContentsOf(tag_type);
        some = true;
      }
    }
    return some;
  }

  private boolean outputFaceTypes(final XMLWriterTagPair uni, Clazz clazz) {
    boolean some = false;
    final int type_number = this.face_manager.face_type_factory.array.size();

    for (int t = 0; t < type_number; t++) {
      final FaceType polygon_type = (FaceType) this.face_manager.face_type_factory.array
        .elementAt(t);
      final XMLWriterTagPair tag_type = new XMLWriterTagPair("SKINSET!");

      this.number_skinsets++;
      this.count_skins = 0;

      final boolean any = outputFace(clazz, polygon_type, tag_type);
      if (any) {
        tag_type.add(new XMLWriterAttribute("count", "" + this.count_skins));
        uni.add(tag_type);
        some = true;
      }
    }
    return some;
  }

  private boolean outputFace(Clazz clazz, FaceType face_type,
    XMLWriterTagPair tag_type) {
    boolean some = false;
    final int n_o_l = this.face_manager.element.size();
    for (int temp = n_o_l; --temp >= 0;) {
      final Face face = (Face) this.face_manager.element.elementAt(temp);
      if (face.clazz == clazz) {
        if (face.type == face_type) {
          final XMLWriterTagPair tag_face = outputFace(face);
          tag_type.add(tag_face);
          some = true;
        }
      }
    }
    return some;
  }

  private XMLWriterTagPair outputFace(Face face) {
    final XMLWriterTagPair tag_face = new XMLWriterTagPair("SKIN");

    final StringBuffer nodelist = new StringBuffer();

    final int number = face.node.size();

    for (int i = 0; i < number; i++) {
      final Node node = (Node) face.node.elementAt(i);

      final int n1 = this.nodes.indexOf(node);

      if (i > 0) {
        nodelist.append(" ");
      }
      nodelist.append("" + n1);
    }

    tag_face.add(new XMLWriterCharacters("" + nodelist));

    this.count_skins++;

    return tag_face;
  }

  private boolean outputLinks(Clazz clazz, LinkType link_type,
    XMLWriterTagPair tag_type) {
    boolean some = false;
    final int n_o_l = this.link_manager.element.size();
    for (int temp = n_o_l; --temp >= 0;) {
      final Link link = (Link) this.link_manager.element.elementAt(temp);
      if (link.clazz == clazz) {
        if (link.type == link_type) {
          final XMLWriterTagPair tag_node = outputLink(link);
          tag_type.add(tag_node);
          some = true;
        }
      }
    }

    return some;
  }

  private XMLWriterTagPair outputLink(Link link) {
    final XMLWriterTagPair tag_link = new XMLWriterTagPair("SPRING");
    tag_link.newlines = false;

    final int n1 = this.nodes.indexOf(link.node1);
    final int n2 = this.nodes.indexOf(link.node2);

    final XMLWriterCharacters chars = new XMLWriterCharacters(" " + n1 + " " + n2
      + " ");
    chars.newlines = false;
    tag_link.add(chars);

    this.count_springs++;

    return tag_link;
  }

  private boolean outputNodes(NodeManager node_manager, Clazz clazz,
    NodeType node_type, XMLWriterTagPair tag_type) {
    boolean some = false;
    final int number_of_nodes = node_manager.element.size();
    for (int n = 0; n < number_of_nodes; n++) {
      final Node node = (Node) node_manager.element.elementAt(n);
      if (node.clazz == clazz) {
        if (node.type == node_type) {
          final XMLWriterTagPair tag_node = outputNode(node);

          tag_type.add(tag_node);
          this.nodes.addElement(node);
          some = true;
          //Log.log("outputNodeTypes: yes");
        }
      }
    }
    return some;
  }

  private XMLWriterTagPair outputNode(final Node node) {
    final XMLWriterTagPair tag_node = new XMLWriterTagPair("JOINT");
    tag_node.newlines = false;
    tag_node.add(getNodePosition(node));

    return tag_node;
  }

  private XMLWriterCharacters getNodePosition(final Node node) {
    final int yy = Coords.getInternalFromPixelCoords(Coords.z_pixels)
      - node.pos.z;

    String o = "" + this.count_joints++ + " ";

    final float x = toFloatingPointCoords(node.pos.x - this.middle.x);
    final float y = toFloatingPointCoords(yy - this.middle.z);
    final float z = toFloatingPointCoords(node.pos.y - this.middle.y);

    o += emit(x) + " ";
    o += emit(y) + " ";
    o += emit(z);

    final XMLWriterCharacters chars = new XMLWriterCharacters(o);
    chars.newlines = false;

    return chars;
  }

  //  int scale(int v) {
  //    if (v >= 0) {
  //      return (v + (scale_factor >> 1)) / scale_factor;
  //    }
  //
  //    return (v - (scale_factor >> 1)) / scale_factor;
  //  }

  float toFloatingPointCoords(int v) {
    return v * this.scale_factor;
  }

  public String emit(float f) {
    return emit(f, 5, false);
  }

  public String emit(float f, int sf, boolean fixed_dp) {
    String o = "" + f;
    if (o.indexOf("E") >= 0) {
      return "0";
    }

    if (o.indexOf(".") < 0) {
      o += ".";
    }

    o += "000000";

    final int actual_sf = (o.charAt(0) == '-') ? sf + 1 : sf;
    o = o.substring(0, actual_sf);

    if (fixed_dp) {
      return o;
    }

    while (o.endsWith("0")) {
      o = o.substring(0, o.length() - 1);
    }

    while (o.endsWith(".")) {
      o = o.substring(0, o.length() - 1);
    }

    return o;
  }

  void writeOut(String s) {
    try {
      if (s.equals("")) {
        if (FrEnd.output_linefeeds) {
          this.out.write("\n");
        }
      } else {
        this.out.write(s + " ");
      }
    } catch (IOException e) {
      Log.log("Error (writeOut): " + e.toString());
    }
  }
}