//This program has been placed into the public domain by its author.

package com.springie.gui.colourpicker;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;

import com.springie.FrEnd;
import com.springie.io.out.writers.wrl.WriterWRL;

public class ColourPickerValueDisplay extends Panel {

  int cx;

  int cy;

  int cw;

  int ch;

  int xsep;

  int ysep;

  int px;

  int py;

  int sy;

  int satx;

  int saty;

  int satw;

  int sath;

  int mainx;

  int mainy;

  int mainw;

  int mainh;

  Label label;

  ColourPicker colour_picker;

  public ColourPickerValueDisplay() {
    setLayout(new GridLayout(1, 0, 0, 0));
    this.label = new Label("", Label.CENTER);
    add(this.label);
    this.label.setFont(new Font("sansserif", Font.PLAIN, 10));
  }

  public void init() {
    this.cx = 10;
    this.cy = 0;

    this.cw = 4;
    this.ch = 8;

    this.xsep = 64;
    this.ysep = 16;

    this.satx = this.xsep * this.cw + this.cx * 2;
    this.satw = this.cx * 2;
    this.saty = this.cy;
    this.sath = this.ysep * this.ch;
    this.sy = this.saty + this.sath;
    this.mainx = this.cx;
    this.mainy = 0;
    this.px = this.cx;
    this.py = this.cy;
    this.mainw = this.xsep * this.cw;
    this.mainh = 0;
  }

  public void inform(ColourPicker colour_picker) {
    this.colour_picker = colour_picker;
  }

  void refresh() {

    //}
    //public void paint(Graphics g) {
    final float h = this.colour_picker.hValue;
    final float s = this.colour_picker.sValue;
    final float b = this.colour_picker.bValue;
    final float t = this.colour_picker.oValue;

    String v = null;

    final WriterWRL writer_wrl = new WriterWRL(FrEnd.node_manager);

    if (!this.colour_picker.isColourModelRGB()) {
      final String str_hue = writer_wrl.emit(h, 4, true);
      final String str_sat = writer_wrl.emit(s, 4, true);
      final String str_bri = writer_wrl.emit(b, 4, true);

      v = "Hue:" + str_hue + " Saturation:" + str_sat + " Brightness:"
        + str_bri;
    } else {
      final int colour = this.colour_picker.getARGBColourUsingColourModel(h, s,
        b, t);

      final String str_red = "" + ((colour >> 16) & 0xFF);

      final String str_green = "" + ((colour >> 8) & 0xFF);

      final String str_blue = "" + (colour & 0xFF);

      v = "Red:" + str_red + " Green:" + str_green + " Blue:" + str_blue;
    }
    
    final String str_opaque = writer_wrl.emit(t, 4, true);
    v += " Opaque:" + str_opaque;

    this.label.setText(v);
  }
}