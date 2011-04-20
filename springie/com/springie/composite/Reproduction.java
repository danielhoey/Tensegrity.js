package com.springie.composite;

import com.springie.collisions.BinGrid;
import com.springie.utilities.random.Hortensius32Fast;

public class Reproduction {
  // static variables...
  static int[] new_genome;
  static int temp;
  static Composite temp_creature;
  static Hortensius32Fast rnd = new Hortensius32Fast();

  /*
     Reproduction() {
        max_genome_size = 1;	
        genome = new int[max_genome_size];
     
        reset();
     }
  
  
     final static void reset() {
        genome_size = 0;
        genome_index = 0;
        executing = false;
     
        initial_read_link = null;
        initial_write_link = null;
        initial_read_node = null;
        initial_write_node = null;
     }
  	*/

  /*
     final static void add(int data) {
        if (genome_size >= max_genome_size) {
           makeMoreReproduction();
        }
     
        genome[genome_size++] = data;
     }
  
  
    // again, this is wasteful :-|
     final static void makeMoreReproduction() {
        max_genome_size++;
     
        new_genome = new int[max_genome_size];
     
        for (temp = 0; temp < (max_genome_size - 1); temp++) {
           new_genome[temp] = genome[temp];
        }
     
        genome = new_genome;
     }
  */

  public static final void handleReproduction(CompositeManager cm) {
    if ((BinGrid.generation & 15) == 5) { // rarely...
      for (temp = cm.number_of_creatures; --temp >= 0;) {
        temp_creature = cm.creature[temp];
        if (temp_creature.genome.executing) {
          temp_creature.executeInstruction();
          // debug("EI");
        }
      }
    }
  }

  static final void debug(String o) {
    System.out.println(o);
  }
}
