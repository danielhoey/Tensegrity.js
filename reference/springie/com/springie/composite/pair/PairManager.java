package com.springie.composite.pair;

import com.springie.elements.nodes.Node;
import com.springie.render.Coords;

public class PairManager {
  Pair[] node_pair;
  Pair[] new_array_of_pairs;

  int max_number_of_pairs = 1;
  int number_of_pairs;

  // static stuff...
  static int agent_counter;
  static int shift = Coords.shift;

  //static int temp, temp2, temp3, temp4, temp5, temp6;
  static Pair temp_pair;

  static Node temp_node;
  static int threshold = 8;
  static int bigthreshold = threshold << Coords.shift;

  //static int temp_x, temp_y, temp_radius;

  static int PAIR_NUMBER_INCREMENT = 16;

  public PairManager() {
    this.node_pair = new Pair[this.max_number_of_pairs];

    reset();
  }

  public void reset() {
    this.number_of_pairs = 0;
  }

  public final Pair add(Node e1, Node e2) {
    if (this.number_of_pairs >= this.max_number_of_pairs) {
      makeMorePairs();
    }

    if (this.node_pair[this.number_of_pairs] == null) {
      temp_pair = new Pair(e1, e2);
      this.node_pair[this.number_of_pairs++] = temp_pair;
    } else {
      temp_pair = this.node_pair[this.number_of_pairs++];
      temp_pair.set(e1, e2);
    }

    return temp_pair;
  }

  // DANGER
  final void add(Pair l) {
    if (this.number_of_pairs >= this.max_number_of_pairs) {
      makeMorePairs();
    }

    // node_pair[number_of_pairs++] = l; /// eeekkkk!
    this.node_pair[this.number_of_pairs++].set(l); /// eeekkkk!
  }

  final void makeMorePairs() {
    this.new_array_of_pairs = new Pair[this.max_number_of_pairs + PAIR_NUMBER_INCREMENT];

    for (int temp = this.max_number_of_pairs; --temp >= 0;) {
      this.new_array_of_pairs[temp] = this.node_pair[temp];
    }

    this.node_pair = this.new_array_of_pairs;

    this.max_number_of_pairs += PAIR_NUMBER_INCREMENT;
  }

  final boolean aPairLikeThisExists(Node e1, Node e2) {
    for (int temp = this.number_of_pairs; --temp >= 0;) {
      if ((this.node_pair[temp].node1 == e1) && (this.node_pair[temp].node2 == e2)) {
        return true;
      }

      if ((this.node_pair[temp].node1 == e2) && (this.node_pair[temp].node2 == e1)) {
        return true;
      }
    }

    return false;
  }

  // need to re-write this so that it uses local list of Pairs stored with node...
  // doesn't quite work properly...
  final void deleteAllPairsBetween(Node e1, Node e2) {
    for (int temp = this.number_of_pairs; --temp >= 0;) {
      if ((this.node_pair[temp].node1 == e1) && (this.node_pair[temp].node2 == e2)) {
        killNumberedPair(temp);
      }

      if ((this.node_pair[temp].node1 == e2) && (this.node_pair[temp].node2 == e1)) {
        killNumberedPair(temp);
      }
    }
  }

  // ?.
  final boolean thereIsAPair(Node e1, Node e2) {
    for (int temp = this.number_of_pairs; --temp >= 0;) {
      if ((this.node_pair[temp].node1 == e1) && (this.node_pair[temp].node2 == e2)) {
        return true;
      }
    }

    return false;
  }

  public final Node findCorrespondingNode(Node e) {
    for (int temp = this.number_of_pairs; --temp >= 0;) {
      if (this.node_pair[temp].node1 == e) {
        return this.node_pair[temp].node2;
      }
    }

    return null;
  }

  // hmm...
  final void killAllPairs(Node e) {
    for (int temp = this.number_of_pairs; --temp >= 0;) {
      if ((this.node_pair[temp].node1 == e) || (this.node_pair[temp].node2 == e)) {
        killNumberedPair(temp);
      }
    }
  }

  // hmm...
  final boolean killLastPair(Node e) {
    for (int temp = this.number_of_pairs; --temp >= 0;) {
      if ((this.node_pair[temp].node1 == e) || (this.node_pair[temp].node2 == e)) {
        killNumberedPair(temp);
        return true;
      }
    }

    return false;
  }

  final void killNumberedPair(int n) {
    temp_pair = this.node_pair[n];

    // need to tell the entities involved...
    temp_pair.node1 = null;
    temp_pair.node2 = null;
    this.node_pair[n] = this.node_pair[this.number_of_pairs - 1];
    this.node_pair[--this.number_of_pairs] = temp_pair;
  }

  final int getNumberOfPair(Pair l) {
    for (int temp = this.number_of_pairs; --temp >= 0;) {
      if (this.node_pair[temp] == l) {
        return temp;
      }
    }

    return -1; // not found...
  }

  final void killSpecifiedPair(Pair l) {
    final int temp = getNumberOfPair(l);
    killNumberedPair(temp);
  }

  static final void debug(String o) {
    System.out.println(o);
  }

}
