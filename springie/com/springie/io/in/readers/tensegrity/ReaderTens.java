package com.springie.io.in.readers.tensegrity;

import java.io.Reader;
import java.util.Vector;

import com.springie.FrEnd;
import com.springie.composite.Composite;
import com.springie.elements.clazz.Clazz;
import com.springie.elements.faces.Face;
import com.springie.elements.faces.FaceType;
import com.springie.elements.links.Link;
import com.springie.elements.links.LinkType;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.elements.nodes.NodeType;
import com.springie.utilities.log.Log;
import com.springie.world.World;

public class ReaderTens {
  static Reader in;

  static int last_token_number = 999;

  static int node_number;

  static final boolean debug_parser = false;

  static int MAX_INS = Instructions.INS_NAMES.length;

  public static void interpretBuffer(NodeManager node_manager, char[] buf,
    int x, int y, int z, int scale_factor) {
    //FrEnd.data_input.start_agent_number = 0;
    final int index = buf.length;
    Composite current_creature = null;
    //World manager = FrEnd.data_input.manager;

    int in_token = 0;
    int in_number = 0;
    int in_instruction = 0;
    String number = "";
    String token = "";
    int current_object = 0;

    NodeType current_node_type = null;
    LinkType current_link_type = null;
    Clazz current_clazz = null;
    FaceType current_face_type = null;

    Node current_node = new Node();
    Link current_link = new Link(null, null, current_link_type, current_clazz);
    int current_link_number = -1;
    Face current_polygon = new Face(new Vector());
    int current_face_number = -1;

    setUpUniverseProperties();

    for (int i = 0; i < index; i++) {
      char c = buf[i];

      // ignore commented lines...
      if (c == '#')
        do {
          c = buf[++i];
        } while (c > 31);

      if (in_instruction == 0) {
        if ((c == '-') || (Character.isDigit(c))) { // start number...
          in_number = 1;
          in_instruction = 1;
          number += c;
        } else {
          if (Character.isLetter(c)) { // (Character.isUpperCase(c)))
            // { // start instruction...
            in_token = 1;
            in_instruction = 1;
            token += c;
          }
        }
      } else { // is in existing instruction...
        if (in_token != 0) {
          if ((c >= '0') && (c != ':')) {
            token += c;
          } else { // finished token...
            int t = 0;
            int found = 0;
            do {
              if (token.equals(Instructions.INS_NAMES[t++])) {
                found = 1;
                t--;
              }
            } while ((found == 0) && (t != MAX_INS));

            if (t == MAX_INS) {
              if (debug_parser)
                Log.log("Unknown token: " + token + " (ignored)");
            } else {
              last_token_number = t;
              switch (last_token_number) {
                case Instructions.N:
                  node_manager.addNewAgent();

                  final int n = node_manager.element.size();
                  current_node = (Node) node_manager.element.elementAt(n - 1);

                  current_object = last_token_number;
                  current_node.type = current_node_type;
                  current_node.clazz = current_clazz;

                  if (current_creature != null) {
                    current_creature.add(current_node);
                  }

                  break;

                case Instructions.LK:
                  current_link_number = node_manager.link_manager.element
                    .size();
                  current_object = last_token_number;
                  node_manager.link_manager.setLink(current_link_type,
                    current_clazz);
                  current_link = (Link) node_manager.link_manager.element
                    .lastElement();
                  node_number = 0;

                  break;

                case Instructions.P:
                  current_face_number = node_manager.face_manager.element
                    .size();
                  current_object = last_token_number;
                  node_manager.face_manager.setPolygon(current_face_type,
                    current_clazz);
                  current_polygon = (Face) node_manager.face_manager.element
                    .lastElement();
                  node_number = 0;

                  break;

                case Instructions.NG:
                  current_object = last_token_number;
                  current_node_type = node_manager.node_type_factory.getNew();
                  current_clazz = node_manager.clazz_factory.getNew(0xFFFFFFF0);

                  break;

                case Instructions.LG:
                  current_object = last_token_number;
                  current_link_type = node_manager.link_manager.link_type_factory
                    .getNew(200, 500);
                  current_clazz = node_manager.clazz_factory.getNew(0xFFFFF0FF);

                  break;

                case Instructions.PG:
                  current_object = last_token_number;
                  current_face_type = node_manager.face_manager.face_type_factory
                    .getNew();
                  //Log.log("ReaderTens: NEW PG");
                  current_clazz = node_manager.clazz_factory.getNew(0xFFF0FFFF);

                  break;

                case Instructions.CR:
                  current_creature = node_manager.creature_manager.add(); // creature_manager.creature[creature_manager.number_of_creatures
                  current_creature.private_world = node_manager;

                  current_object = last_token_number;
                  current_link.type = current_link_type;

                  break;

                case Instructions.G:
                  current_object = last_token_number;
                  break;

                case Instructions.GO:
                  //current_creature = manager.creature_manager.add(); //
                  // creature_manager.creature[creature_manager.number_of_creatures
                  //current_creature.private_world = manager;

                  //current_object = last_token_number;

                  break;

                default:
                  break;
              //throw new RuntimeException("");
              }

              //current_object = last_token_number;

              if (debug_parser) {
                Log.log("Token: " + token + " (" + t + ")");
              }
            }

            in_token = 0;
            in_instruction = 0;
            token = "";
          }
        }

        if (in_number != 0) {
          if (c >= '0') {
            number += c;
          } else { // finished number...
            int temp;
            if ((number.length() >= 3) && (number.charAt(1) == 'x')) {
              number = number.substring(2);
              if (debug_parser) {
                Log.log("Hex number: 0x" + number + " ("
                  + (int) Long.parseLong(number, 16) + ")");
              }

              temp = (int) Long.parseLong(number, 16);
            } else {
              temp = (int) Long.parseLong(number);
            }

            switch (current_object) {
              case Instructions.G: //gravity...
                switch (last_token_number) {
                  case Instructions.GO:
                    World.gravity_active = temp == 1;
                    break;

                  case Instructions.GS:
                    World.gravity_strength = temp;

                    break;

                  case Instructions.CO:
                    FrEnd.node_manager.electrostatic.charge_active = temp == 1;
                    break;

                  case Instructions.DIM:
                    FrEnd.three_d = temp == 3;
                    break;

                  //case Instructions.CS:
                  //FrEnd.node_manager.electrostatic.charge_strength = temp;
                  //break;

                  default:
                    break;
                }
                break;

              case Instructions.NG: // nodes
                switch (last_token_number) {
                  case Instructions.R:
                    current_node_type.setSize((temp * scale_factor) >> 8);
                    break;

                  case Instructions.CH:
                    current_node_type.charge = temp;
                    break;

                  case Instructions.C:
                    current_clazz.colour = temp;

                    break;

                  case Instructions.H:
                    current_node_type.hidden = temp != 0;
                    break;

                  case Instructions.FX:
                    current_node_type.fixed = temp != 0;
                    break;

                  default:
                    break;
                }
                break;

              case Instructions.LG: //links
                switch (last_token_number) {
                  case Instructions.E:
                    current_link_type.elasticity = temp;

                    break;

                  case Instructions.DA:
                    current_link_type.damping = temp;

                    break;

                  case Instructions.L:
                    current_link_type.setLength((temp * scale_factor) >> 8);

                    break;

                  case Instructions.C:
                    current_clazz.colour = temp;

                    break;

                  //                  case Instructions.PH:
                  //                    current_link_type.phase = (byte) temp;
                  //
                  //                    break;

                  //                  case Instructions.A:
                  //                    current_link_type.amplitude = (char) ((temp * scale_factor)
                  // >>> 8);
                  //
                  //                    break;

                  case Instructions.R:
                    current_link_type.radius = (temp * scale_factor) >> 8;

                    break;

                  case Instructions.CA:
                    current_link_type.cable = true;

                    break;

                  case Instructions.H:
                    current_link_type.hidden = true;
                    break;

                  case Instructions.D:
                    current_link_type.disabled = true;
                    break;

                  default:
                    break;
                }

                break;
              case Instructions.PG:
                switch (last_token_number) {
                  case Instructions.C:
                    current_clazz.colour = temp;

                    break;

                  case Instructions.H:
                    current_face_type.hidden = temp != 0;
                    break;

                  default:
                    break;
                }

                break;
              case Instructions.N:
                switch (last_token_number) {
                  case Instructions.X:
                    current_node.pos.x = ((temp * scale_factor) >> 8) + x;
                    //Log.log("X:" + current_node.pos.x);

                    break;

                  case Instructions.Y:
                    current_node.pos.y = ((temp * scale_factor) >> 8) + y;
                    //Log.log("Y:" + current_node.pos.y);

                    break;

                  case Instructions.Z:
                    current_node.pos.z = ((temp * scale_factor) >> 8) + z;
                    //Log.log("Z:" + current_node.pos.z);
                    break;

                  case Instructions.DX:
                    current_node.velocity.x = (temp * scale_factor) >> 8;

                    break;

                  case Instructions.DY:
                    current_node.velocity.y = (temp * scale_factor) >> 8;
                    break;

                  case Instructions.DZ:
                    current_node.velocity.z = (temp * scale_factor) >> 8;
                    break;

                  default:
                    break;
                //throw new RuntimeException("");
                }

                break;

              case Instructions.LK:
                // no links yet...
                switch (last_token_number) {
                  case Instructions.V:
                    if (node_number == 0) {
                      Node temp_node1 = (Node) node_manager.element
                        .elementAt(temp);
                      current_link.node1 = temp_node1;
                      temp_node1.list_of_links.add(current_link_number);
                      node_number++;
                    } else {
                      Node temp_node2 = (Node) node_manager.element
                        .elementAt(temp);
                      current_link.node2 = temp_node2;
                      temp_node2.list_of_links.add(current_link_number);
                      node_number++;
                    }

                    break;

                  default:
                    break;
                }

                break;

              case Instructions.P:
                switch (last_token_number) {
                  case Instructions.V:
                    final Node temp_node1 = (Node) node_manager.element
                      .elementAt(temp);
                    current_polygon.node.addElement(temp_node1);
                    temp_node1.list_of_polygons.add(current_face_number);
                    node_number++;

                    break;

                  default:
                    break;
                }

                break;

              case Instructions.CR:
                // creature...
                switch (last_token_number) {
                  //case 23:
                  // data...
                  //current_creature.genome.add(temp);

                  //break;
                  case Instructions.F:
                    //frequency...
                    current_creature.oscillator.setPhase((char) temp);

                    break;
                  default:
                    break;
                //throw new RuntimeException("");
                }

                break;

              default:
                Log.log("current_object:" + current_object);
                throw new RuntimeException("");
            }

            if (debug_parser) {
              Log.log("Number: " + number);
            }

            in_number = 0;
            in_instruction = 0;
            number = "";
          }
        }
      }
    }

    if (current_creature != null) {
      //if (current_creature.write_node != null) {
      //  current_creature.write_link =
      // current_creature.write_node.list_of_links.link[0];
      //}

      //if (current_creature.read_node != null) {
      //  current_creature.read_link =
      // current_creature.read_node.list_of_links.link[0];
      //}
    }

    if (node_manager == FrEnd.node_manager) {
      //NodeManager.target_number_of_agents = manager.node.size();
      //number_of_nodes;

      FrEnd.reflectStatusInGUI();
    }
  }

  private static void setUpUniverseProperties() {
    FrEnd.three_d = true;
    FrEnd.node_manager.electrostatic.charge_active = true;
    World.gravity_active = false;
    World.gravity_strength = 2;
  }
}