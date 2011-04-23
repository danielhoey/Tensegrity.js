//This program has been placed into the public domain by its author.

package com.springie.gui.colourpicker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

public class ColourPickerVT extends ColourPickerBase {
  public void update(Graphics g) {
    paintHelper();

    for (int j = this.margin; j < this.range; j++) {
      float b = (j + (j & 15)) / (float) this.range;
      if (b > 1f) {
        b = 1f;
      }
      
      final int col = Color.HSBtoRGB(0, 0, b);
      g.setColor(new java.awt.Color(col));
      g.fillRect(j, this.margin, 1, this.height - this.margin * 2);
    }

    paintMarkers(g, this.colour_picker.oValue);
  }

  public void mouseClicked(MouseEvent e) {
    clickHelper(e);

    this.colour_picker.oValue = this.x / (float) this.range;
    this.colour_picker.repaintChildren();
  }
}
