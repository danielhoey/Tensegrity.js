//This program has been placed into the public domain by its author.

package com.springie.gui.components;

import java.awt.Choice;
import java.awt.Color;
import java.awt.event.ItemListener;
import java.util.Enumeration;
import java.util.Vector;


public class TTChoice {
  public Vector vector;

  public Choice choice;

  public TTChoice(ItemListener il) {
    this.choice = new Choice();
    this.choice.addItemListener(il);
    this.choice.setBackground(Color.white);
    this.choice.setForeground(Color.black);

    this.vector = new Vector();
  }

  public void add(String s, int n) {
    this.choice.addItem(s);

    this.vector.addElement(new TTNumStr(n, s)); //  = s;
  }

  void removeAll() {
    this.choice.removeAll();

    this.vector.removeAllElements(); //  = s;
  }

  public int str_to_num(String s) {
    // int i = 0;
    Enumeration enumeration;
    TTNumStr temp_pair;

    enumeration = this.vector.elements();

    while (enumeration.hasMoreElements()) {

      temp_pair = (TTNumStr) (enumeration.nextElement());

      if (temp_pair.string == s)
        return temp_pair.number;
    }

    return -99; // -99 = not found...
  }

  public String num_to_str(int j) {
    //int i = 0;
    Enumeration enumeration;
    TTNumStr temp_pair;

    enumeration = this.vector.elements();

    while (enumeration.hasMoreElements()) {
      temp_pair = (TTNumStr) (enumeration.nextElement());

      if (temp_pair.number == j)
        return temp_pair.string;
    }

    return ""; // null marker = not found...
  }

}