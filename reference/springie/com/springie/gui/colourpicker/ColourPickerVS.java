// This program has been placed into the public domain by its author.

package com.springie.gui.colourpicker;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

public class ColourPickerVS extends ColourPickerBase {
  public void update(Graphics g) {
    paintHelper();

    for (int j = this.margin; j < this.range; j++) {
      final float h = this.colour_picker.hValue;
      final float s = j / (float) this.range;
      final float b = this.colour_picker.bValue;
      final float o = this.colour_picker.oValue;
      final int col = this.colour_picker.getARGBColourUsingColourModel(h, s, b,
        o);
      g.setColor(new java.awt.Color(col));
      g.fillRect(j, this.margin, 1, this.height - this.margin * 2);
    }

    paintMarkers(g, this.colour_picker.sValue);
  }

  public void mouseClicked(MouseEvent e) {
    clickHelper(e);

    this.colour_picker.sValue = this.x / (float) this.range;
    this.colour_picker.repaintChildren();
  }
}