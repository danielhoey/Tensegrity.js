package com.springie.io.out.writers.wrl;

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
import com.springie.io.out.AreThereAny;
import com.springie.io.out.GarbageCollection;
import com.springie.metrics.BoundingBox;
import com.springie.modification.redundancy.RedundancyRemover;
import com.springie.utilities.general.Forget;
import com.springie.utilities.log.Log;

public class WriterWRL {
  NodeManager node_manager;

  LinkManager link_manager;

  float scale_factor;

  BoundingBox bb;

  Point3D middle = new Point3D(0, 0, 0);

  Writer out;

  Vector nodes;

  public WriterWRL(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;
  }

  public void write(String filename) {
    new GarbageCollection(FrEnd.node_manager).cleanUp();
    new RedundancyRemover(FrEnd.node_manager).removeRedundancy();

    this.bb = new BoundingBox();
    this.bb.find(FrEnd.node_manager);

    final float sfx = 2f / (this.bb.max.x - this.bb.min.x);
    final float sfy = 2f / (this.bb.max.y - this.bb.min.y);
    final float sfz = 2f / (this.bb.max.z - this.bb.min.z);
    this.scale_factor = Math.min(sfx, Math.min(sfy, sfz));

    this.middle.x = (this.bb.max.x + this.bb.min.x) >> 1;
    this.middle.y = (this.bb.max.y + this.bb.min.y) >> 1;
    this.middle.z = (this.bb.max.z + this.bb.min.z) >> 1;

    this.nodes = new Vector();

    try {
      this.out = new FileWriter(filename);
      writeHeader();

      writeLine("# vertices");
      outputNodeClasses();
      writeLine("# edges");
      outputLinkClasses();
      writeLine("# faces");
      outputPolygonClasses();

      this.out.flush();
    } catch (IOException e) {
      Log.log("Error in write: " + e);
    }
  }

  private void writeHeader() {
    writeLine("#VRML V2.0 utf8");
    writeLine("");
    writeLine("DEF Flight Transform {");
    writeLine(" children [");
    writeLine("  Viewpoint {");
    writeLine("   description \"Distant (rotating)\"");
    writeLine("   position 0 0 2.8");
    writeLine("   orientation 0 0 1 0");
    writeLine("  }");
    writeLine("");
    writeLine("  Viewpoint {");
    writeLine("   description \"Close (rotating)\"");
    writeLine("   position 0 0 2");
    writeLine("   orientation 0 0 1 0");
    writeLine("  }");
    writeLine("");
    writeLine("  Viewpoint {");
    writeLine("   description \"Inside (rotating)\"");
    writeLine("   position  0 0 0.5");
    writeLine("   orientation 0 0 1 0");
    writeLine("  }");
    writeLine(" ]");
    writeLine("}");
    writeLine("");
    writeLine("DEF Clock TimeSensor {");
    writeLine(" enabled TRUE");
    writeLine(" cycleInterval 100");
    writeLine(" loop TRUE");
    writeLine("}");
    writeLine("");
    writeLine("DEF Path OrientationInterpolator {");
    writeLine(" key [ 0.0 0.5 1.0 ]");
    writeLine(" keyValue [");
    writeLine("  0.0 1.0 0.0 0.0,");
    writeLine("  0.0 1.0 0.0 3.14,");
    writeLine("  0.0 1.0 0.0 6.28");
    writeLine(" ]");
    writeLine("}");
    writeLine("");
    writeLine("ROUTE Clock.fraction_changed TO Path.set_fraction");
    writeLine("ROUTE Path.value_changed TO Flight.set_rotation");
    writeLine("");
    writeLine("Viewpoint {");
    writeLine(" description \"Distant\"");
    writeLine(" position 0 0 2.7");
    writeLine(" orientation 0 0 1 0");
    writeLine(" jump FALSE");
    writeLine("}");
    writeLine("");
    writeLine("DEF V1 Viewpoint {");
    writeLine(" description \"Close\"");
    writeLine(" position 0 0 2.1");
    writeLine(" orientation 0 0 1 0");
    writeLine(" jump FALSE");
    writeLine("}");
    writeLine("");
    writeLine("Viewpoint {");
    writeLine(" description \"Very close\"");
    writeLine(" position 0 0 1.3");
    writeLine(" orientation 0 0 1 0");
    writeLine(" jump FALSE");
    writeLine("}");
    writeLine("");
    writeLine("Viewpoint {");
    writeLine(" description \"Inside\"");
    writeLine(" position 0 0 0.5");
    writeLine(" orientation 0 0 1 0");
    writeLine(" jump FALSE");
    writeLine("}");
    writeLine("");
    writeLine("Viewpoint {");
    writeLine(" description \"Central\"");
    writeLine(" position 0 0 0.3");
    writeLine(" orientation 0 0 1 0");
    writeLine(" jump FALSE");
    writeLine("}");
    writeLine("");
    writeLine("Viewpoint {");
    writeLine(" description \"Too far\"");
    writeLine(" position 0 0 -0.3");
    writeLine(" orientation 0 0 1 0");
    writeLine(" jump FALSE");
    writeLine("}");
    writeLine("");
    writeLine("NavigationInfo {");
    writeLine(" type [\"EXAMINE\",\"ANY\"]");
    writeLine(" headlight FALSE");
    writeLine("}");
    writeLine("");
    writeLine("DirectionalLight {");
    writeLine(" color 0.7 0.7 0.7");
    writeLine(" direction 1 0 0");
    writeLine(" intensity 1");
    writeLine("}");
    writeLine("");
    writeLine("DirectionalLight {");
    writeLine(" color 0.7 0.7 0.75");
    writeLine(" direction -0.2 -0.9 0");
    writeLine(" intensity 1");
    writeLine("}");
    writeLine("");
    writeLine("DirectionalLight {");
    writeLine(" color 0.75 0.7 0.7");
    writeLine(" direction -0.2 0.2 0.7");
    writeLine(" intensity 1");
    writeLine("}");
    writeLine("");
    writeLine("DirectionalLight {");
    writeLine(" color 0.7 0.75 0.7");
    writeLine(" direction -0.2 0.2 -0.7");
    writeLine(" intensity 1");
    writeLine("}");
    writeLine("");
  }

  private void outputNodeClasses() {
    final int clazz_number = this.node_manager.clazz_factory.array.size();
    for (int c = 0; c < clazz_number; c++) {
      final Clazz clazz = (Clazz) this.node_manager.clazz_factory.array
        .elementAt(c);
      outputNodeTypes(clazz);
    }
  }

  private void outputLinkClasses() {
    final int clazz_number = this.node_manager.clazz_factory.array.size();
    for (int c = 0; c < clazz_number; c++) {
      final Clazz clazz = (Clazz) this.node_manager.clazz_factory.array
        .elementAt(c);
      outputLinkTypes(clazz);
    }
  }

  private void outputPolygonClasses() {
    final int clazz_number = this.node_manager.clazz_factory.array.size();
    for (int c = 0; c < clazz_number; c++) {
      final Clazz clazz = (Clazz) this.node_manager.clazz_factory.array
        .elementAt(c);
      outputFacesTypes(clazz);
    }
  }

  private void outputNodeTypes(Clazz clazz) {
    final int node_type_number = FrEnd.node_manager.node_type_factory.array
      .size();

    if (!new AreThereAny().nodes(clazz)) {
      return;
    }

    for (int number = 0; number < node_type_number; number++) {
      outputNodeType(clazz, number);
      outputNodes(FrEnd.node_manager, clazz, number);
      writeLine("");
    }
  }

  private void outputLinkTypes(Clazz clazz) {
    final LinkManager link_manager = FrEnd.node_manager.link_manager;
    final int link_type_number = link_manager.link_type_factory.array.size();

    if (!new AreThereAny().links(clazz)) {
      return;
    }

    for (int number = 0; number < link_type_number; number++) {
      final LinkType type = (LinkType) link_manager.link_type_factory.array
        .elementAt(number);

      if (new AreThereAny().links(clazz, type)) {
        outputLinks(clazz, link_manager, number, type);
      }
    }
  }

  private void outputLinks(Clazz clazz, LinkManager link_manager, int number,
    LinkType link_type) {
    final float r = (((clazz.colour >> 16) & 0xFF)) / 255f;
    final float g = (((clazz.colour >> 8) & 0xFF)) / 255f;
    final float b = (((clazz.colour) & 0xFF)) / 255f;
    //final float t = 0F; //(((clazz.colour >> 24) & 0xFF)) / 255f;
    final float radius = toVRMLCoords(link_type.radius);
    final float length = toVRMLCoords(link_type.length);

    writeLine("PROTO E" + number + " [");
    writeLine(" field SFVec3f C 0 -1 0");
    writeLine(" field SFRotation R  0 1 0 0");
    writeLine(" field SFVec3f S 0 -1 0");
    writeLine("] {");
    writeLine(" Transform {");
    writeLine("  translation IS C");
    writeLine("  rotation IS R");
    writeLine("  scale IS S");
    writeLine("  children [");
    writeLine("   Shape {");
    writeLine("    appearance Appearance {");
    writeLine("     material Material {");
    writeLine("      diffuseColor " + emit(r) + " " + emit(g) + " " + emit(b));
    writeLine("      specularColor 1 1 1");
    writeLine("      shininess 1");
    //writeLine(" transparency " + emit(t));
    writeLine("     }");
    writeLine("    }");
    writeLine("    geometry Cylinder {");
    writeLine("     height " + emit(length));
    writeLine("     radius " + emit(radius));
    writeLine("    }");
    writeLine("   }");
    writeLine("  ]");
    writeLine(" }");
    writeLine("}");
    writeLine("");

    outputLinks(link_manager, clazz, number);
    writeLine("");
  }

  private void outputFacesTypes(Clazz clazz) {
    final FaceManager polygon_manager = FrEnd.node_manager.face_manager;
    final int type_number = polygon_manager.face_type_factory.array.size();
    if (!new AreThereAny().polygons(clazz)) {
      return;
    }

    for (int number = 0; number < type_number; number++) {
      final FaceType type = (FaceType) polygon_manager.face_type_factory.array
        .elementAt(number);
      if (new AreThereAny().polygons(clazz, type)) {
        outputFaceType(clazz, number);
        outputFaces(polygon_manager, clazz, number);
      }
    }
  }

  private void outputFaceType(Clazz clazz, int number) {
    Forget.about(number);

    final float r = (((clazz.colour >> 16) & 0xFF)) / 255f;
    final float g = (((clazz.colour >> 8) & 0xFF)) / 255f;
    final float b = (((clazz.colour) & 0xFF)) / 255f;
    final float t = (((clazz.colour >> 24) & 0xFF)) / 255f;

    writeLine("Shape {");
    writeLine(" appearance Appearance {");
    writeLine("  material Material {");
    writeLine("   diffuseColor " + emit(r) + " " + emit(g) + " " + emit(b));
    writeLine("   specularColor 1 1 1");
    writeLine("   shininess 0.8");
    writeLine("   transparency " + emit(t));
    writeLine("  }");
    writeLine(" }");
    writeLine(" ");

    this.nodes.removeAllElements();
  }

  private void outputFaces(FaceManager face_manager, Clazz clazz, int number) {
    final FaceType type = (FaceType) face_manager.face_type_factory.array
      .elementAt(number);

    writeLine(" geometry IndexedFaceSet {");
    writeLine("  coord Coordinate {");
    writeLine("   point [");
    final int number_of_faces = face_manager.element.size();

    int cnt = 0;
    for (int n = 0; n < number_of_faces; n++) {
      final Face polygon = (Face) face_manager.element.elementAt(n);
      if (polygon.type == type) {
        if (polygon.clazz == clazz) {
          final int npolygon = polygon.node.size();

          for (int i = npolygon; --i >= 0;) {
            final Node node = (Node) polygon.node.elementAt(i);

            if (processNode(node, cnt)) {
              cnt++;
            }
          }
        }
      }
    }

    writeLine("   ]");
    writeLine("  }");
    writeLine("");
    writeLine("  coordIndex [");

    for (int n = 0; n < number_of_faces; n++) {
      final Face face = (Face) face_manager.element.elementAt(n);
      if (face.type.equals(type)) {
        if (!face.type.hidden || FrEnd.render_hidden_faces) {
          writeFaceClockwise(face);
          writeFaceAntiClockwise(face);
        }
      }
    }

    writeLine("  ]");
    writeLine(" }");
    writeLine("}");
    writeLine("");
  }

  private void writeFaceClockwise(final Face polygon) {
    final int npolygon = polygon.node.size();
    String pline = "   ";

    for (int i = npolygon; --i >= 0;) {
      final Node node = (Node) polygon.node.elementAt(i);

      final int n1 = this.nodes.indexOf(node);

      pline += n1 + " ";
    }
    writeLine(pline + "-1");
  }

  private void writeFaceAntiClockwise(final Face polygon) {
    final int npolygon = polygon.node.size();
    String pline = "   ";

    for (int i = 0; i < npolygon; i++) {
      final Node node = (Node) polygon.node.elementAt(i);

      final int n1 = this.nodes.indexOf(node);

      pline += n1 + " ";
    }
    writeLine(pline + "-1");
  }

  private boolean processNode(final Node node, int cnt) {
    if (this.nodes.indexOf(node) >= 0) {
      return false;
    }

    outputFaceVertex(node, cnt);
    this.nodes.addElement(node);
    return true;
  }

  private void outputFaceVertex(final Node node, int n) {
    final float x = toVRMLCoords(node.pos.x - this.middle.x);
    final float y = toVRMLCoords(node.pos.y - this.middle.y);
    final float z = toVRMLCoords(node.pos.z - this.middle.z);
    writeLine("    " + emit(x) + " " + emit(y) + " " + emit(z) + " #" + n);
  }

  private void outputNodeType(Clazz clazz, int number) {
    final NodeType type = (NodeType) FrEnd.node_manager.node_type_factory.array
      .elementAt(number);

    if (!new AreThereAny().nodes(clazz, type)) {
      return;
    }

    final float r = (((clazz.colour >> 16) & 0xFF)) / 255f;
    final float g = (((clazz.colour >> 8) & 0xFF)) / 255f;
    final float b = (((clazz.colour) & 0xFF)) / 255f;
    //float t = (((clazz.colour >> 24) & 0xFF)) / 255f;
    final float radius = toVRMLCoords(type.radius);
    //t = 1F;

    writeLine("PROTO V" + number + " [");
    writeLine(" field SFVec3f C 0 0 0");
    writeLine("] {");
    writeLine(" Transform {");
    writeLine("  translation IS C");
    writeLine("  children [");
    writeLine("   Shape {");
    writeLine("    appearance Appearance {");
    writeLine("     material Material {");
    writeLine("      diffuseColor " + emit(r) + " " + emit(g) + " " + emit(b));
    writeLine("      specularColor 1 1 1");
    writeLine("      shininess 1");
    //writeLine(" transparency " + emit(t));
    writeLine("     }");
    writeLine("    }");
    writeLine("    geometry Sphere {");
    writeLine("     radius " + emit(radius));
    writeLine("    }");
    writeLine("   }");
    writeLine("  ]");
    writeLine(" }");
    writeLine("}");
    writeLine("");
  }

  private void outputNodes(final NodeManager node_manager, Clazz clazz,
    int number) {
    final NodeType node_type = (NodeType) FrEnd.node_manager.node_type_factory.array
      .elementAt(number);
    if (!node_type.hidden || FrEnd.render_hidden_nodes) {

      final int number_of_nodes = node_manager.element.size();
      for (int n = 0; n < number_of_nodes; n++) {
        final Node node = (Node) node_manager.element.elementAt(n);
        if (node.type == node_type) {
          if (node.clazz == clazz) {
            outputNode(number, node);
          }
        }
      }
    }
  }

  private void outputNode(int type_idx, final Node node) {
    final float x = toVRMLCoords(node.pos.x - this.middle.x);
    final float y = toVRMLCoords(node.pos.y - this.middle.y);
    final float z = toVRMLCoords(node.pos.z - this.middle.z);

    writeLine("V" + type_idx + " { C " + emit(x) + " " + emit(y) + " "
      + emit(z) + " }");
  }

  private void outputLinks(LinkManager link_manager, Clazz clazz, int number) {
    final LinkType type = (LinkType) link_manager.link_type_factory.array
      .elementAt(number);

    final int n_o_l = link_manager.element.size();
    for (int temp = n_o_l; --temp >= 0;) {
      final Link link = (Link) link_manager.element.elementAt(temp);
      if (link.type == type) {
        if (link.clazz == clazz) {
          if (!link.type.hidden || FrEnd.render_hidden_links) {
            outputLink(link, number);
          }
        }
      }
    }
  }

  private void outputLink(Link link, int number) {
    final Point3D pos_1 = link.node1.pos;
    final Point3D pos_2 = link.node2.pos;

    final float x = toVRMLCoords(((pos_1.x + pos_2.x) >> 1) - this.middle.x);
    final float y = toVRMLCoords(((pos_1.y + pos_2.y) >> 1) - this.middle.y);
    final float z = toVRMLCoords(((pos_1.z + pos_2.z) >> 1) - this.middle.z);

    final float rx = toVRMLCoords(pos_1.z - pos_2.z);
    float ry = toVRMLCoords(pos_1.y - pos_2.y);
    final float rz = toVRMLCoords(pos_2.x - pos_1.x);

    if (ry == 0f) {
      ry = 1f;
    }

    final double amp = Math.sqrt(0.001 + (rx * rx) + (rz * rz));
    final double theta = Math.atan(amp / ry);
    final int actual = link.getActualLength();
    final float sf = actual / (1F + link.type.length);

    writeLine("E" + number + " {");
    writeLine(" C " + emit(x) + " " + emit(y) + " " + emit(z));
    writeLine(" R " + emit(rx) + " 0 " + emit(rz) + " " + emit((float) theta));
    writeLine(" S 1 " + emit(sf) + " 1");
    writeLine("}");
    writeLine("");
  }

  float toVRMLCoords(int v) {
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

  void writeLine(String s) {
    try {
      this.out.write(s + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}