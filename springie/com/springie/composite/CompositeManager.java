package com.springie.composite;

import com.springie.elements.links.Link;
import com.springie.elements.nodes.Node;
import com.springie.utilities.log.Log;
import com.springie.utilities.random.Hortensius32Fast;
import com.springie.world.World;

public class CompositeManager {
  public Composite[] creature;
  Composite[] new_array_of_creatures;

  int max_number_of_creatures = 1;
  public int number_of_creatures;

  // static stuff...
  //static int temp, temp2, temp3, temp4, temp5, temp6;
  static int agent_counter;

  //static int temp_x, temp_y, temp_radius;

  static Composite temp_creature;
  static Composite current_creature;

  static Node temp_node;
  static Node temp_node2;

  static Link temp_link;
  static Link temp_link2;

  static Hortensius32Fast rnd = new Hortensius32Fast();

  public CompositeManager() {
    initial();
  }

  public final void initial() {
    this.max_number_of_creatures = 1;
    this.reset();
    this.creature = new Composite[this.max_number_of_creatures];
  }

  public final void reset() {
    this.number_of_creatures = 0;
  }

  // maximum value check....
  public final Composite add() { // Creature e1, Creature e2, int l, int e) {
    if (this.number_of_creatures >= this.max_number_of_creatures) {
      makeMoreCreatures();
    }

    if (this.creature[this.number_of_creatures] == null) {
      temp_creature = new Composite();
      this.creature[this.number_of_creatures++] = temp_creature;
    } else {
      temp_creature = this.creature[this.number_of_creatures++];
      temp_creature.reset();
    }

    return temp_creature;
  }

  // maximum value check....
  final void setCreature() {
    if (this.number_of_creatures >= this.max_number_of_creatures) {
      makeMoreCreatures();
    }

    if (this.creature[this.number_of_creatures] == null) {
      this.creature[this.number_of_creatures++] = new Composite();
    } else {
      this.creature[this.number_of_creatures++].reset();
    }
  }

  final void makeMoreCreatures() {
    this.max_number_of_creatures++;

    this.new_array_of_creatures = new Composite[this.max_number_of_creatures];

    for (int temp = this.number_of_creatures; --temp >= 0;) {
      this.new_array_of_creatures[temp] = this.creature[temp];
    }

    this.creature = this.new_array_of_creatures;
  }

  //TODO
//  public final Composite duplicate(Composite c, World pw) {
//    current_creature = null;
//
//    if (c != null) {
//      if ((c.status & Composite.DEAD) == 0) {
//        current_creature = add(); // new creature...
//
//        current_creature.private_world = pw; // c.private_world;
//
//        for (int temp = 0; temp < c.number_of_entities; temp++) {
//          temp_node = c.node_list[temp];
//
//          temp_node2 = pw.copy(temp_node);
//
//          temp_node2.type.setSize(temp_node.type.radius);
//
//          current_creature.add(temp_node2);
//
//          if (temp_node == c.read_node) {
//            current_creature.read_node = temp_node2;
//          }
//
//          if (temp_node == c.write_node) {
//            current_creature.write_node = temp_node2;
//          }
//        }
//
//        // debug("NOLK:" + pw.link_manager.number_of_links); // zero OK...
//        for (int temp = 0; temp < c.number_of_entities; temp++) {
//          temp_node = c.node_list[temp];
//          final int temp3 = temp_node.list_of_links.size();
//
//          for (int temp2 = temp3; --temp2 >= 0;) {
//            temp_link = (Link) temp_node.list_of_links.elementAt(temp2);
//            final int temp4 = c.getNodeNumber(temp_link.node1);
//            final int temp5 = c.getNodeNumber(temp_link.node2);
//
//            // if (temp4 < 0) debug("temp4 < 0 - egg?"); // NOT OK - this should be alive still...
//            // if (temp5 < 0) debug("temp5 < 0 -- egg?"); // unless it's an attached egg...
//
//            if (temp4 >= 0) { // part of the problem...
//              if (temp5 >= 0) { // part of the problem...
//
//                if (!pw.link_manager.isThereALinkFromTo(current_creature.node_list[temp4], current_creature.node_list[temp5])) {
//                  temp_link2 = pw.link_manager.setLink(current_creature.node_list[temp4], current_creature.node_list[temp5], temp_link);
//                  //if (FrEnd.big_babies) {
//                  temp_link2.type.setLength(temp_link.type.radius);
//                  //} else {
//                   // temp_link2.setLength(temp_link.target_length);
//                  //}
//
//                }
//              }
//            }
//          }
//        }
//      }
//
//      current_creature.init(c);
//      //current_creature.mutate();
//    }
//
//    return current_creature;
//  }

  final int getNumberOfCreature(Composite c) {
    for (int temp = this.number_of_creatures; --temp >= 0;) {
      if (this.creature[temp] == c) {
        return temp;
      }
    }

    return -1;
  }

  final void killNumberedCreature(int n) {
    if ((n >= 0) && (n < this.number_of_creatures)) {
      temp_creature = this.creature[n];
      if ((temp_creature.status & Composite.DEAD) == 0) {
        // temp_creature.status |= Creature.DEAD; // you need to make sure you don't try to kill a creature twice...
        // need to manually go through and destroy all references to this creature...
        temp_creature.markAsKilled();

        this.creature[n] = this.creature[this.number_of_creatures - 1];
        this.creature[--this.number_of_creatures] = temp_creature;

        // debug("CREATURE NUMBER(" + n + ") KILLED - Now NOC = " + this.number_of_creatures);
      } else {
        // never fires...
        // debug("ATTEMPT TO KILL DEAD CREATURE(" + n + ") ERROR - NOC:" + this.number_of_creatures);
      }
    }
    //else
    //{
    // now fires...
    // debug("CREATURE NUMBER(" + n + ") OUT OF BOUNDS - NOC:" + this.number_of_creatures);
    //}
  }

  public final void killSpecifiedCreature(Composite l) {
    if (l != null) {
      if ((l.status & Composite.DEAD) == 0) {
        final int temp = getNumberOfCreature(l);
        killNumberedCreature(temp);
      } else {
        // never fires...
        Log.log("ATTEMPT TO KILL DEAD CREATURE ... ERROR - NOC:" + this.number_of_creatures);
      }
    }
  }

  final void killAllCreatures() { // Creature e) {
    for (int temp = this.number_of_creatures; --temp >= 0;) {
      temp_creature = this.creature[temp];

      killCreature(temp_creature.write_node);
    }
  }

  public final void killCreature(Node e) {
    temp_creature = e.creature;
    if (temp_creature != null) {
      killSpecifiedCreature(temp_creature);
      e.simplyKill();
    }
  }

  static final void moveCreaturesBetweenWorlds(World pw_from, World pw_to) {
    for (int temp = pw_from.creature_manager.number_of_creatures; --temp >= 0;) {
      temp_creature = pw_to.creature_manager.add();

      temp_creature.private_world = pw_from;
      pw_from.creature_manager.creature[temp].private_world = pw_to;

      pw_to.creature_manager.creature[pw_to.creature_manager.number_of_creatures - 1] = pw_from.creature_manager.creature[temp];
      pw_from.creature_manager.creature[temp] = temp_creature;
    }
  }

  /*
     final void moveCreaturesBetweenWorlds(World pw_from, World pw_to, int ien) {
        for (temp = this.number_of_creatures; --temp >= 0; ) {
           temp_creature = creature[temp];
        
           if (temp_creature.private_world == pw_from) {
              temp_creature.private_world = pw_to;
           
           	// move *all* the entities across (sigh)...
           	// *must* go forwards...
              for (temp2 = 0; temp2 < temp_creature.number_of_entities; temp2++) {
                 temp_node = temp_creature.node_list[temp2];
                 temp4 = pw_from.getNodeNumber(temp_node);
                 temp_creature.node_list[temp2] = pw_to.agent[temp4 + ien];
                 if (temp_creature.node_list[temp2] == null) {
                    debug("problems already"); // doesn't fire...
                 }
              }	
           
              temp_creature.read_node = temp_creature.node_list[0]; // phew!?! :-(
              temp_creature.write_node = temp_creature.node_list[0]; // phew!?! :-(
           }
        } 
     }
  */

  public void update() {
    for (int temp = this.number_of_creatures; --temp >= 0;) {

      //TODO fix this ...
      //creature[temp].oscillator.update();
    }
  }
}
