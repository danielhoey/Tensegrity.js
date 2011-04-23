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

public class ColourPickerGetAndSet extends Panel {
  private ColourPicker colour_picker;

  int colour;

  public Button button_set;

  public Button button_get;

  public Button button_restore;

  public void inform(ColourPicker colour_picker) {
    this.colour_picker = colour_picker;
  }

  public ColourPickerGetAndSet() {
    //this.button_get = new Label("Colour");

    this.button_get = new Button("Get");
    this.button_get.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        setColour();
      }
    });

    add(this.button_get);

    this.button_set = new Button("Set");
    this.button_set.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        final ColourChanger cc = new ColourChanger(FrEnd.node_manager);

        cc.setColour(getColour());
      }
    });

    add(this.button_set);

    this.button_restore = new Button("Restore");
    this.button_restore.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getColourPicker().setColour(getColour());
      }
    });

    add(this.button_restore);
  }

  public void setColour() {
    this.colour = this.colour_picker.getColour();

    getColourPickerGetAndSet().setBackground(new Color(this.colour));
    this.colour_picker.greyGetAndSetColourButtons();
  }

  public ColourPicker getColourPicker() {
    return this.colour_picker;
  }

  public int getColour() {
    return this.colour;
  }

  public ColourPickerGetAndSet getColourPickerGetAndSet() {
    return this;
  }
}