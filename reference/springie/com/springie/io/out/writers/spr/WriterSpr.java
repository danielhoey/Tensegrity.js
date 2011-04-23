package com.springie.io.out.writers.spr;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Vector;

import com.springie.FrEnd;
import com.springie.elements.clazz.Clazz;
import com.springie.elements.electrostatics.ElectrostaticRepulsion;
import com.springie.elements.faces.Face;
import com.springie.elements.faces.FaceManager;
import com.springie.elements.faces.FaceType;
import com.springie.elements.links.Link;
import com.springie.elements.links.LinkManager;
import com.springie.elements.links.LinkType;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.elements.nodes.NodeType;
import com.springie.io.out.DuplicateLinkRemover;
import com.springie.io.out.GarbageCollection;
import com.springie.io.out.writers.wrl.WriterWRL;
import com.springie.modification.post.PostModification;
import com.springie.modification.redundancy.RedundancyRemover;
import com.springie.render.Coords;
import com.springie.utilities.log.Log;
import com.springie.world.World;
import com.tifsoft.xml.writer.XMLWriterAttribute;
import com.tifsoft.xml.writer.XMLWriterSinglet;
import com.tifsoft.xml.writer.XMLWriterTagPair;

public class WriterSpr {
  // scale factor - causes problems if not equal to 1.
  //
  // Plan to make this work...
  // ...and then to make it more widely available -
  // e.g.: in the GUI...
  static final int scale_factor = 1;

  Writer out;

  Vector nodes;

  NodeManager node_manager;
  LinkManager link_manager;

  public WriterSpr(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;
  }

  public void write(String filename) {
    final String s = generateString();

    try {
      this.out = new FileWriter(filename);
      this.out.write("<?xml version=\"1.0\"?>\n");
      this.out.write(s);
      this.out.flush();
      this.out.close();
    } catch (IOException e) {
      Log.log("Error in WriterXMLSpr: " + e);
    }
  }

  public String generateString() {
    new DuplicateLinkRemover(this.node_manager).removeDuplicateLinks();
    new GarbageCollection(this.node_manager).cleanUp();
    new RedundancyRemover(this.node_manager).removeRedundancy();
    new PostModification(this.node_manager).thoroughCleanup();

    this.nodes = new Vector();

    final XMLWriterTagPair uni = new XMLWriterTagPair("universe");

    final String g_v = "" + World.gravity_strength;
    uni.add(new XMLWriterAttribute("gravity_strength", g_v));
    if (World.gravity_active) {
      final String g_b = "" + World.gravity_active;
      uni.add(new XMLWriterAttribute("gravity_active", g_b));
    }

    //2D
    if (!FrEnd.three_d) {
      uni.add(new XMLWriterAttribute("dimensions", "2"));
    }

    final ElectrostaticRepulsion electrostatic = FrEnd.node_manager.electrostatic;
    if (electrostatic.charge_active) {
      final String c_b = "" + electrostatic.charge_active;
      uni.add(new XMLWriterAttribute("charge_active", c_b));
    }

    recursivelyOutputAllNodes(uni);
    recursivelyOutputAllLinks(uni);
    recursivelyOutputAllFaces(uni);

    final XMLWriterTagPair ten = new XMLWriterTagPair("tensegrity");
    ten.add(new XMLWriterAttribute("version", "1.0"));
    ten.add(uni);
    return ten.makeString();
  }

  private void recursivelyOutputAllLinks(final XMLWriterTagPair uni) {
    final XMLWriterTagPair links = new XMLWriterTagPair("links");
    final boolean any = outputLinkClasses(links);

    if (any) {
      uni.add(links);
    }
  }

  private void recursivelyOutputAllFaces(final XMLWriterTagPair uni) {
    final XMLWriterTagPair polygons = new XMLWriterTagPair("faces");
    final boolean any = outputFaceClasses(polygons);
    if (any) {
      uni.add(polygons);
    }
  }

  private void recursivelyOutputAllNodes(final XMLWriterTagPair uni) {
    final XMLWriterTagPair nodes = new XMLWriterTagPair("nodes");
    final boolean any = outputNodeClasses(nodes);
    if (any) {
      uni.add(nodes);
    }
  }

  private boolean outputNodeClasses(final XMLWriterTagPair uni) {
    //Log.log("outputNodeClasses");
    final int clazz_number = this.node_manager.clazz_factory.array.size();
    boolean some = false;
    for (int c = 0; c < clazz_number; c++) {
      final Clazz clazz = (Clazz) this.node_manager.clazz_factory.array.elementAt(c);
      final XMLWriterTagPair tag_type = new XMLWriterTagPair("class");
      outputColour(clazz, tag_type);
      final boolean any = outputNodeTypes(tag_type, clazz);
      if (any) {
        uni.add(tag_type);
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
      final XMLWriterTagPair tag_type = new XMLWriterTagPair("type");
      tag_type.add(new XMLWriterAttribute("radius", ""
        + ((node_type.radius / scale_factor))));

      if (node_type.charge != 0) {
        tag_type.add(new XMLWriterAttribute("charge", "" + node_type.charge));
      }

      if (node_type.hidden) {
        tag_type.add(new XMLWriterAttribute("hidden", "true"));
      }

      if (node_type.fixed) {
        tag_type.add(new XMLWriterAttribute("fixed", "true"));
      }

      final boolean any = outputNodes(FrEnd.node_manager, clazz, node_type,
        tag_type);

      if (any) {
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
      final XMLWriterTagPair tag_type = new XMLWriterTagPair("class");
      outputColour(clazz, tag_type);
      final boolean any = outputLinkTypes(tag_type, clazz);
      if (any) {
        uni.add(tag_type);
        some = true;
      }
    }
    return some;
  }

  private boolean outputLinkTypes(final XMLWriterTagPair uni, Clazz clazz) {
    final LinkManager link_manager = FrEnd.node_manager.link_manager;
    final int link_type_number = link_manager.link_type_factory.array.size();
    boolean some = false;

    for (int lt = 0; lt < link_type_number; lt++) {
      final LinkType link_type = (LinkType) link_manager.link_type_factory.array
        .elementAt(lt);
      final XMLWriterTagPair tag_type = new XMLWriterTagPair("type");
      tag_type.add(new XMLWriterAttribute("length", ""
        + scale(link_type.length)));
      if (link_type.radius != 0) {
        tag_type.add(new XMLWriterAttribute("radius", ""
          + scale(link_type.radius)));
      }
      if (link_type.elasticity != LinkType.default_elasticity) {
        tag_type.add(new XMLWriterAttribute("elasticity", ""
          + link_type.elasticity));
      }
      if (link_type.damping != LinkType.default_damping) {
        tag_type.add(new XMLWriterAttribute("damping", "" + link_type.damping));
      }
      if (link_type.hidden) {
        tag_type.add(new XMLWriterAttribute("hidden", "true"));
      }
      if (link_type.cable) {
        tag_type.add(new XMLWriterAttribute("cable", "true"));
      }
      if (link_type.disabled) {
        tag_type.add(new XMLWriterAttribute("disabled", "true"));
      }

      final boolean any = outputLinks(link_manager, clazz, link_type, tag_type);
      if (any) {
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
      final XMLWriterTagPair tag_type = new XMLWriterTagPair("class");
      outputColour(clazz, tag_type);
      final boolean any = outputFaceTypes(tag_type, clazz);
      if (any) {
        uni.add(tag_type);
        some = true;
      }
    }
    return some;
  }

  private boolean outputFaceTypes(final XMLWriterTagPair uni, Clazz clazz) {
    boolean some = false;
    final FaceManager face_manager = FrEnd.node_manager.face_manager;
    final int type_number = face_manager.face_type_factory.array.size();
    
    for (int t = 0; t < type_number; t++) {
      final FaceType polygon_type = (FaceType) face_manager.face_type_factory.array
        .elementAt(t);
      final XMLWriterTagPair tag = new XMLWriterTagPair("type");

      if (polygon_type.hidden) {
        tag.add(new XMLWriterAttribute("hidden", "true"));
      }

      final boolean any = outputFace(face_manager, clazz, polygon_type,
        tag);
      if (any) {
        uni.add(tag);
        some = true;
      }
    }
    return some;
  }

  private boolean outputFace(FaceManager face_manager, Clazz clazz,
    FaceType face_type, XMLWriterTagPair tag_type) {
    boolean some = false;
    final int n_o_l = face_manager.element.size();
    for (int temp = n_o_l; --temp >= 0;) {
      final Face face = (Face) face_manager.element.elementAt(temp);
      if (face.clazz == clazz) {
        if (face.type == face_type) {
          final XMLWriterSinglet tag_face = outputFace(face);
          tag_type.add(tag_face);
          some = true;
        }
      }
    }
    return some;
  }

  private XMLWriterSinglet outputFace(Face face) {
    final XMLWriterSinglet tag_face = new XMLWriterSinglet("face");

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

    tag_face.add(new XMLWriterAttribute("nodes", "" + nodelist));
    return tag_face;
  }

  //  private XMLWriterTagPair outputFace(Face face) {
  //    final XMLWriterTagPair tag = new XMLWriterTagPair("face");
  //
  //    final int number = face.node.size();
  //
  //    for (int i = 0; i < number; i++) {
  //      final Node node = (Node) face.node.elementAt(i);
  //
  //      final int n1 = this.nodes.indexOf(node);
  //      final XMLWriterSinglet tag_node = new XMLWriterSinglet("node");
  //      
  //      tag_node.add(new XMLWriterAttribute("n", "" + n1));
  //      tag.add(tag_node);
  //    }
  //
  //    return tag;
  //  }

  private boolean outputLinks(LinkManager link_manager, Clazz clazz,
    LinkType link_type, XMLWriterTagPair tag_type) {
    boolean some = false;
    final int n_o_l = link_manager.element.size();
    for (int temp = n_o_l; --temp >= 0;) {
      final Link link = (Link) link_manager.element.elementAt(temp);
      if (link.clazz == clazz) {
        if (link.type == link_type) {
          final XMLWriterSinglet tag_node = outputLink(link);
          tag_type.add(tag_node);
          some = true;
        }
      }
    }

    return some;
  }

  private XMLWriterSinglet outputLink(Link link) {
    final XMLWriterSinglet tag_link = new XMLWriterSinglet("link");

    final int n1 = this.nodes.indexOf(link.node1);
    final int n2 = this.nodes.indexOf(link.node2);

    tag_link.add(new XMLWriterAttribute("nodes", "" + n1 + " " + n2));

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
        }
      }
    }
    return some;
  }

  private XMLWriterTagPair outputNode(final Node node) {
    final XMLWriterTagPair tag_node = new XMLWriterTagPair("node");
    tag_node.add(getNodePosition(node));
    tag_node.add(getVelocityPosition(node));

    return tag_node;
  }

  private XMLWriterSinglet getNodePosition(final Node node) {
    final XMLWriterSinglet singlet = new XMLWriterSinglet("position");

    final int y = Coords.getInternalFromPixelCoords(Coords.z_pixels)
      - node.pos.z;

    singlet.add(new XMLWriterAttribute("x", "" + scale(node.pos.x)));
    singlet.add(new XMLWriterAttribute("y", "" + scale(y)));
    singlet.add(new XMLWriterAttribute("z", "" + scale(node.pos.y)));
    return singlet;
  }

  private XMLWriterSinglet getVelocityPosition(final Node node) {
    final XMLWriterSinglet singlet = new XMLWriterSinglet("velocity");
    if (node.velocity.x != 0) {
      singlet.add(new XMLWriterAttribute("x", "" + scale(node.velocity.x)));
    }
    if (node.velocity.y != 0) {
      singlet.add(new XMLWriterAttribute("y", "" + -scale(node.velocity.z)));
    }
    if (node.velocity.z != 0) {
      singlet.add(new XMLWriterAttribute("z", "" + scale(node.velocity.y)));
    }
    return singlet;
  }

  //private XMLWriterTagPair outputLink(Link link) {
  //    final XMLWriterTagPair tag_link = new XMLWriterTagPair("link");
  //
  //    final int n1 = this.nodes.indexOf(link.node1);
  //    final int n2 = this.nodes.indexOf(link.node2);
  //
  //    final XMLWriterSinglet node1 = new XMLWriterSinglet("node");
  //    node1.add(new XMLWriterAttribute("n", "" + n1));
  //    tag_link.add(node1);
  //
  //    final XMLWriterSinglet node2 = new XMLWriterSinglet("node");
  //    node2.add(new XMLWriterAttribute("n", "" + n2));
  //    tag_link.add(node2);
  // 
  //    return tag_link;
  //  }

  //  private void outputNodePosition(final Node node,
  //    final XMLWriterSinglet tag_node) {
  //    final int y = Coords.getInternalFromPixelCoords(Coords.z_pixels)
  //      - node.pos.z;
  //
  //    tag_node.add(new XMLWriterAttribute("x", "" + scale(node.pos.x)));
  //    tag_node.add(new XMLWriterAttribute("y", "" + scale(y)));
  //    tag_node.add(new XMLWriterAttribute("z", "" + scale(node.pos.y)));
  //  }

  int scale(int v) {
    if (v >= 0) {
      return (v + (scale_factor >> 1)) / scale_factor;
    }

    return (v - (scale_factor >> 1)) / scale_factor;
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