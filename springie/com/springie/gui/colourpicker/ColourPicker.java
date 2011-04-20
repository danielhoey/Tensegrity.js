//This program has been placed into the public domain by its author.

package com.springie.gui.colourpicker;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import com.springie.gui.components.ComponentAccess;
import com.springie.utilities.general.Forget;

public class ColourPicker extends Panel {
  float hValue = 0.5f;

  float sValue = 0.7f;

  float bValue = 0.9f;

  float oValue = 1.0f;

  ColourPickerVH cph;

  ColourPickerVS cps;

  ColourPickerVB cpb;

  ColourPickerVT cpt;

  ColourPickerPreview cp_fc;

  ColourPickerValueDisplay cpfcv;

  ColourPickerGetAndSet cp_gas;

  boolean colour_model_rgb;

  public ColourPicker() {
    final Label l = new Label("Colour model:");
    add(l);

    final CheckboxGroup colour_group = new CheckboxGroup();
    final Checkbox checkbox_rgb = new Checkbox("RGB", colour_group,
      this.colour_model_rgb);
    checkbox_rgb.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        if (checkbox_rgb.getState()) {
          if (!isColourModelRGB()) {
            convertHSBtoRGB();
          }
        }
      }
    });

    add(checkbox_rgb);
    final Checkbox checkbox_hsb = new Checkbox("HSB", colour_group,
      !this.colour_model_rgb);
    checkbox_hsb.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        if (checkbox_hsb.getState()) {
          if (isColourModelRGB()) {
            convertRGBtoHSB();
          }
        }
      }
    });
    add(checkbox_hsb);
  }

  public void setColour(int x) {
    final int o = (x >> 24) & 0xFF;
    final int r = (x >> 16) & 0xFF;
    final int g = (x >> 8) & 0xFF;
    final int b = x & 0xFF;

    final float[] fa = new float[3];

    if (this.colour_model_rgb) {
      this.hValue = r / 255f;
      this.sValue = g / 255f;
      this.bValue = b / 255f;
    } else {
      Color.RGBtoHSB(r, g, b, fa);

      this.hValue = fa[0];
      this.sValue = fa[1];
      this.bValue = fa[2];
    }

    this.oValue = o / 255f;

    repaintChildren();
  }

  public void inform(ColourPickerPreview cpfc) {
    this.cp_fc = cpfc;
  }

  public void inform(ColourPickerVB cpb) {
    this.cpb = cpb;
  }

  public void inform(ColourPickerVH cph) {
    this.cph = cph;
  }

  public void inform(ColourPickerVS cps) {
    this.cps = cps;
  }

  public void inform(ColourPickerVT cpt) {
    this.cpt = cpt;
  }

  public void inform(ColourPickerValueDisplay cpfcv) {
    this.cpfcv = cpfcv;
  }

  public void inform(ColourPickerGetAndSet cp_gas) {
    this.cp_gas = cp_gas;
  }

  public void repaintChildren() {
    this.cp_fc.refresh();
    this.cpfcv.refresh();
    repaintH();
    repaintS();
    repaintB();
    repaintT();

    greyGetAndSetColourButtons();
  }

  public void repaintH() {
    this.cph.repaint();
  }

  public void repaintS() {
    this.cps.repaint();
  }

  public void repaintB() {
    this.cpb.repaint();
  }

  public void repaintT() {
    this.cpt.repaint();
  }

  void convertRGBtoHSB() {
    this.colour_model_rgb = false;

    final float[] fa = new float[3];
    final int red = (int) (this.hValue * 0xFF);
    final int green = (int) (this.sValue * 0xFF);
    final int blue = (int) (this.bValue * 0xFF);

    Color.RGBtoHSB(red, green, blue, fa);
    this.hValue = fa[0];
    this.sValue = fa[1];
    this.bValue = fa[2];

    repaintChildren();
  }

  void convertHSBtoRGB() {
    this.colour_model_rgb = true;

    final int rgb = Color.HSBtoRGB(this.hValue, this.sValue, this.bValue);
    this.hValue = ((rgb >> 16) & 0xFF) / 255f;
    this.sValue = ((rgb >> 8) & 0xFF) / 255f;
    this.bValue = (rgb & 0xFF) / 255f;

    repaintChildren();
  }

  public boolean isColourModelRGB() {
    return this.colour_model_rgb;
  }

  public void setColourModelRGB(boolean colour_model_rgb) {
    this.colour_model_rgb = colour_model_rgb;
  }

  public int getARGBColourUsingColourModel(float h, float s, float b, float o) {
    final int opacity = (int) (o * 255) & 0xFF;
    int rgb = Color.HSBtoRGB(h, s, b) & 0xFFFFFF;

    if (this.colour_model_rgb) {
      final int red = (int) (h * 255) & 0xFF;
      final int green = (int) (s * 255) & 0xFF;
      final int blue = (int) (b * 255) & 0xFF;

      rgb = (red << 16) | (green << 8) | blue;
    }
 
    return rgb | (opacity << 24);
  }

  public void greyGetAndSetColourButtons() {
    final boolean same = this.cp_gas.getColour() == getColour();

    ComponentAccess.setAccess(this.cp_gas.button_get, !same);
    ComponentAccess.setAccess(this.cp_gas.button_restore, !same);
  }

  public int getColour() {
    return getARGBColourUsingColourModel(this.hValue, this.sValue, this.bValue, this.oValue);
  }
}