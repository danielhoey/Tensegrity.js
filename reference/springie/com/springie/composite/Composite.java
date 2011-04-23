package com.springie.composite;

import com.springie.composite.pair.PairManager;
import com.springie.elements.links.Link;
import com.springie.elements.nodes.Node;
import com.springie.muscles.Oscillator;
import com.springie.utilities.general.Forget;
import com.springie.utilities.random.Hortensius32Fast;
import com.springie.world.World;

public class Composite {
  public Node[] node_list;

  int max_number_of_entities;

  int number_of_entities;

  int status;

  public char age_of_creature;

  char age_of_puberty;

  public char period_of_gestation;

  public char age_when_next_fertile;

  public char maximum_age;

  //public boolean egg;
  //public boolean egg_attached;
  //public boolean fission;

  public byte number_of_kids_so_far;

  public Node read_node;

  public Node write_node;

  public Link read_link;

  public Link write_link;

  public World private_world; // needed?

  public Genome genome;

  public Oscillator oscillator;

  Composite next_creature; // used for reproduction...

  PairManager pair_manager; // links between the corresponding cells of this
                            // creature and the next...

  int stored_colour;

  int stored_elasticity;

  Link stored_link;

  Link old_write_link;

  // static variables...
  static int temp;

  static int temp2;

  static int temp3;

  static int temp4;

  static int instruction;

  static Node[] new_node_list;

  static Node temp_node;

  static Node next_node;

  static Node corresponding_node;

  static Node other_temp_node;

  static Node other_corresponding_node;

  static Composite temp_creature;

  static CompositeManager temp_creature_manager;

  static Link temp_link;

  static Hortensius32Fast rnd = new Hortensius32Fast();

  static final int DEAD = 1 << 31;

  Composite() {
    this.max_number_of_entities = 1;
    this.node_list = new Node[this.max_number_of_entities];
    this.genome = new Genome();

    reset();
  }

  final void reset() {
    this.number_of_entities = 0;
    this.status = 0;

    this.number_of_kids_so_far = 0;
    this.age_of_creature = 0;
    this.age_of_puberty = (char) (64 + rnd.nextInt(32));
    this.period_of_gestation = (char) 64;
    this.age_when_next_fertile = this.age_of_puberty;
    this.maximum_age = (char) (128 + rnd.nextInt(128));

    this.genome.reset();

    this.oscillator = new Oscillator();
  }

  final void init(Composite c) {
    Forget.about(c);
  }

  public final void add(Node e) {
    if (this.number_of_entities >= this.max_number_of_entities) {
      makeMoreEntities();
    }

    this.node_list[this.number_of_entities++] = e;

    e.creature = this;
  }

  // again, this is wasteful :-|
  final void makeMoreEntities() {
    this.max_number_of_entities++;

    new_node_list = new Node[this.max_number_of_entities];

    for (temp = 0; temp < (this.max_number_of_entities - 1); temp++) {
      new_node_list[temp] = this.node_list[temp];
    }

    this.node_list = new_node_list;
  }

  // returns number within this creature...
  final int getNodeNumber(Node e) {
    for (temp = 0; temp < this.number_of_entities; temp++) {
      if (this.node_list[temp] == e) {
        return temp;
      }
    }

    // debug("Node is not part of this creature");

    return -1;
  }

  // returns number within this creature...
  final boolean isAssociatedWithThisCreature(Node e) {
    for (temp = 0; temp < this.number_of_entities; temp++) {
      if (this.node_list[temp] == e) {
        return true;
      }
    }

    return false;
  }

  final void markAsKilled() {
    this.status |= Composite.DEAD;
    // need to manually go through and destroy all references to this
    // creature...
    for (temp = 0; temp < this.number_of_entities; temp++) {
      this.node_list[temp].creature = null; // OK after all...
    }
  }

  public final void beginToReproduce() {
    if (this.next_creature == null) {
      this.next_creature = new Composite();
    } else {
      this.next_creature.reset();
    }

    this.next_creature.mostlyCopy(this);

    if (this.pair_manager == null) {
      this.pair_manager = new PairManager(); // links between the corresponding
                                             // cells of this creature and the
                                             // next...
    } else {
      this.pair_manager.reset();
    }

    this.genome.executing = true;
  }

  public final void executeInstruction() {
    if (this.genome.executing) {
      instruction = this.genome.genome[this.genome.genome_index++];
      if (this.genome.genome_index >= this.genome.genome_size) {
        this.genome.finishExecution();
      }

      //bigSwitch();//TODO
    }
  }

  //  final void bigSwitch() {
  //    switch (instruction) {
  //      case Genome.COPY :
  //        temp_node = this.private_world.addNewAgent(this.read_node);
  //        temp_node.type.setSize(this.read_node.type.radius);
  //        temp_node.creature = this;
  //
  //        if (this.write_node != null) {
  //          next_node = this.write_link.theOtherEnd(this.write_node);
  //          if (next_node != null) {
  //            this.old_write_link = this.write_link;
  //
  //            temp_node.pos.x = (next_node.pos.x + this.write_node.pos.x) >> 1; // NPE??
  //            temp_node.pos.y = (next_node.pos.y + this.write_node.pos.y) >> 1;
  //            temp_node.pos.z = (next_node.pos.z + this.write_node.pos.z) >> 1;
  //
  //            temp_link = this.private_world.link_manager.setLink(this.write_node,
  // temp_node, this.write_link);
  //            temp_link.type.setLength(this.write_link.type.radius);
  //            
  //            final LinkType type = LinkTypeFactory.getNew(256, 20);
  //            final Clazz clazz = new Clazz(0xFFFFC0FF);
  //            this.private_world.link_manager.setLink(this.read_node, temp_node, type,
  // clazz);
  //
  //            this.write_link = temp_link; // ?
  //
  //            temp_link = this.private_world.link_manager.setLink(temp_node, next_node,
  // this.read_link);
  //            temp_link.type.setLength(this.write_link.type.radius); // read_l
  //
  //            this.write_link = temp_link; // ?
  //
  //            if (this.genome.genome_index > 1) {
  //              this.private_world.link_manager.deleteAllLinksBetween(this.write_node,
  // next_node);
  //            }
  //            //else
  //            //{
  //            // stored_link = old_write_link; //
  // private_world.link_manager.linkBetween(write_node, next_node);
  //            //stored_elasticity = stored_link.elasticity;
  //            //stored_colour = stored_link.colour;
  //            // stored_link.elasticity = 0;
  //            //stored_link.colour = 0;
  //            //}
  //
  //            // add node to creature...? (no...)..?!?!?
  //            // Update "List of Links"...
  //            // add node to new creature...
  //            this.next_creature.add(temp_node);
  //
  //            // old, new
  //            this.pair_manager.add(this.read_node, temp_node);
  //          }
  //        }
  //
  //        break;
  //
  //      case Genome.FOLLOW :
  //        this.read_node = this.read_link.theOtherEnd(this.read_node);
  //        this.read_link = (Link) this.read_node.list_of_links.elementAt(0);
  //
  //        break;
  //
  //      case Genome.FINISH :
  //        //if (stored_link != null) {
  //        //stored_link.elasticity = stored_elasticity;
  //        //stored_link.colour = stored_colour;
  //        //}
  //
  //        // break all links between the creatures...
  //        for (temp3 = 0; temp3 < this.next_creature.number_of_entities; temp3++) {
  //          temp_node = this.next_creature.node_list[temp3];
  //          this.private_world.link_manager.killAllLinks(temp_node);
  //        }
  //
  //        // reconstruct new links, using the old ones as a template...
  //        for (temp3 = 0; temp3 < this.number_of_entities; temp3++) {
  //          temp_node = this.node_list[temp3];
  //          corresponding_node = this.pair_manager.findCorrespondingNode(temp_node);
  //          corresponding_node.creature = this.next_creature;
  //          temp4 = temp_node.list_of_links.size();
  //          for (temp2 = 0; temp2 < temp4; temp2++) {
  //            temp_link = (Link) temp_node.list_of_links.elementAt(temp2);
  //            other_temp_node = temp_link.theOtherEnd(temp_node);
  //            if (isAssociatedWithThisCreature(other_temp_node)) {
  //              other_corresponding_node =
  // this.pair_manager.findCorrespondingNode(other_temp_node);
  //              if (other_corresponding_node != null) { // needed?
  //                if (!this.private_world.link_manager.isNodeLinkedToNode(corresponding_node,
  // other_corresponding_node)) {
  //                  temp_link = this.private_world.link_manager.setLink(corresponding_node,
  // other_corresponding_node, temp_link);
  //                  temp = this.private_world.distanceBetween(corresponding_node,
  // other_corresponding_node);
  //                  temp_link.type.setLength(temp_link.type.radius);
  //                }
  //              }
  //            }
  //          }
  //        }
  //
  //        // Make sure the order of the links in the new node's list of links is
  //        // the same as it was for the old node...
  //        for (temp3 = 0; temp3 < this.number_of_entities; temp3++) {
  //          temp_node = this.node_list[temp3];
  //          corresponding_node = this.pair_manager.findCorrespondingNode(temp_node);
  //          temp_link = (Link) temp_node.list_of_links.elementAt(0);
  //          other_temp_node = temp_link.theOtherEnd(temp_node);
  //
  //          other_corresponding_node =
  // this.pair_manager.findCorrespondingNode(other_temp_node);
  //          temp_link = (Link) corresponding_node.list_of_links.elementAt(0);
  //          if (other_corresponding_node != temp_link.theOtherEnd(corresponding_node))
  // {
  //            temp4 = corresponding_node.list_of_links.size();
  //            for (temp2 = 1; temp2 < temp4; temp2++) {
  //              temp_link = (Link) corresponding_node.list_of_links.elementAt(temp2);
  //              if (other_corresponding_node == temp_link.theOtherEnd(corresponding_node))
  // {
  //                corresponding_node.list_of_links.setElementAt(corresponding_node.list_of_links.elementAt(0),
  // temp2);
  //                corresponding_node.list_of_links.setElementAt(temp_link, 0);
  //
  //                break;
  //              }
  //            }
  //          }
  //        }
  //
  //        this.read_node = this.genome.initial_read_node;
  //        this.write_node = this.genome.initial_write_node;
  //        setDefaultRWLinks();
  //
  //        this.next_creature.read_node =
  // this.pair_manager.findCorrespondingNode(this.read_node);
  //        this.next_creature.write_node =
  // this.pair_manager.findCorrespondingNode(this.write_node);
  //
  //        this.next_creature.genome.initial_read_node = this.next_creature.read_node;
  //        this.next_creature.genome.initial_write_node =
  // this.next_creature.write_node;
  //        this.next_creature.setDefaultRWLinks();
  //
  //        temp_creature_manager = this.private_world.creature_manager;
  //        temp_creature_manager.add();
  //        temp_creature =
  // temp_creature_manager.creature[temp_creature_manager.number_of_creatures -
  // 1];
  //        temp_creature_manager.creature[temp_creature_manager.number_of_creatures -
  // 1] = this.next_creature;
  //        this.next_creature = temp_creature;
  //
  //        this.genome.finishExecution();
  //
  //        break;
  //        
  //      default:
  //        throw new RuntimeException("");
  //    }
  //  }

  //  final void setDefaultRWLinks() {
  //    this.read_link = (Link) this.read_node.list_of_links.elementAt(0);
  //    this.write_link = (Link) this.write_node.list_of_links.elementAt(0);
  //  }

  final void mostlyCopy(Composite c) {
    this.genome.copy(c.genome);
    this.private_world = c.private_world;
  }
}