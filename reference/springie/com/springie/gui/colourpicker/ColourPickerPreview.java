// This program has been placed into the public domain by its author.

package com.springie.gui.colourpicker;

import java.awt.Button;
import java.awt.Color;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.springie.FrEnd;
import com.springie.modification.colour.ColourChanger;
import com.springie.utilities.general.Forget;

public class ColourPickerPreview extends Panel {
  public Button button_set;

  public ColourPickerPreview() {
    this.button_set = new Button("Set colour");
    this.button_set.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        final ColourChanger cc = new ColourChanger(FrEnd.node_manager);

        cc.setColour(getColour());
      }
    });
    add(this.button_set);
    
    this.setBackground(Color.black);
  }

  private ColourPicker colour_picker;

  int colour;

  public void inform(ColourPicker colour_picker) {
    this.colour_picker = colour_picker;
  }

  public void refresh() {
    this.colour = this.colour_picker.getColour();
    this.setBackground(new Color(this.colour));
  }

  public ColourPicker getColourPicker() {
    return this.colour_picker;
  }

  public int getColour() {
    return this.colour;
  }

  public void setColourPicker(ColourPicker colour_picker) {
    this.colour_picker = colour_picker;
  }
}