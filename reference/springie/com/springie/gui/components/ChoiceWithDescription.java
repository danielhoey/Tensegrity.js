//This program has been placed into the public domain by its author.

package com.springie.gui.components;

import java.awt.Choice;
import java.awt.Color;
import java.awt.event.ItemListener;
import java.util.Hashtable;

public class ChoiceWithDescription {
  public Hashtable hashtable;

  public Choice choice;

  public ChoiceWithDescription(ItemListener il) {
    this.choice = new Choice();
    this.choice.addItemListener(il);
    this.choice.setBackground(Color.white);
    this.choice.setForeground(Color.black);

    this.hashtable = new Hashtable();
  }

  public void add(String description, String name) {
    this.choice.addItem(description);

    this.hashtable.put(description, name);
  }

  public void removeAll() {
    this.choice.removeAll();

    this.hashtable.clear();
  }

  public String getName(String description) {
    return (String) this.hashtable.get(description);
  }
}