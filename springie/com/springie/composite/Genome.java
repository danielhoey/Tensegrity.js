package com.springie.composite;
import com.springie.elements.nodes.Node;
import com.springie.utilities.random.Hortensius32Fast;

public class Genome {
  int[] genome;

  int genome_size;
  int max_genome_size;
  int genome_index;
  boolean executing;

  public Node initial_read_node;
  public Node initial_write_node;

  // not used...
  // Link initial_read_link;
  // Link initial_write_link;

  // static variables...
  static int[] new_genome;
  static int temp;
  static Hortensius32Fast rnd = new Hortensius32Fast();

  public static final int COPY = 0;
  public static final int FOLLOW = 1; // any active points go along current link...
  public static final int READ_FOLLOW = 2; // any active read points go along current link...
  public static final int WRITE_FOLLOW = 3; // any active write points go along current link...
  public static final int READ_NEXT_LINK = 4; // link number incremented (cyclicly)...
  public static final int WRITE_NEXT_LINK = 5; // link number incremented (cyclicly)...
  public static final int DELETE = 6; // deletes link...

  static final int WAIT = 997;
  static final int NULL = 998;
  static final int FINISH = 999; // creature done...

  public Genome() {
    this.max_genome_size = 1;
    this.genome = new int[this.max_genome_size];

    reset();
  }

  public final void reset() {
    this.genome_size = 0;
    this.genome_index = 0;
    this.executing = false;

    this.initial_read_node = null;
    this.initial_write_node = null;
    //initial_read_link = null;
    //initial_write_link = null;
  }

  public final void add(int data) {
    if (this.genome_size >= this.max_genome_size) {
      makeMoreGenome();
    }

    this.genome[this.genome_size++] = data;
  }

  final void copy(Genome g) {
    reset();

    //initial_read_node = g.initial_read_node;
    //initial_write_node = g.initial_write_node;

    for (temp = 0; temp < g.genome_size; temp++) {
      add(g.genome[temp]);
    }
  }

  // again, this is wasteful :-|
  final void makeMoreGenome() {
    this.max_genome_size++;

    new_genome = new int[this.max_genome_size];

    for (temp = 0; temp < (this.max_genome_size - 1); temp++) {
      new_genome[temp] = this.genome[temp];
    }

    this.genome = new_genome;
  }

  public final void finishExecution() {
    this.executing = false;
    this.genome_index = 0;
  }

  /*
    // returns number within this creature...
     final int getNodeNumber(Node e) {
        for (temp = 0; temp < number_of_entities; temp++) {
           if (genome[temp] == e) {
              return temp;
           }
        }
     
        // debug("Node is not part of this creature"); 
     
        return -1;
     }
  */

  final void debug(String o) {

    System.out.println(o);
  }
}
