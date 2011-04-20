// This code has been placed into the public domain by its author

package com.springie.render;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import com.springie.FrEnd;

public class InfoButton {
  boolean gui_is_iconised = FrEnd.viewer;

  final int size = 16;

  final int margin = 2;

  void drawInfoButton(Graphics g) {
    if (this.gui_is_iconised) {
      //if ((FrEnd.generation & 31) < 3) {
      renderButton(g);
      //}
    }
  }

  private void renderButton(Graphics g) {
    final int left = getLeft();
    final int top = getTop();

    g.setColor(Color.yellow);
    g.drawRect(left, top, this.size, this.size);
    g.setColor(Color.green);
    g.drawRect(left + (this.size >> 2), top + (this.size >> 2), this.size >> 1,
      this.size >> 1);
  }

  private int getLeft() {
    return Coords.x_pixels - this.margin - this.size;
  }

  private int getTop() {
    return Coords.y_pixels - this.margin - this.size;
  }

  public void mouseClicked(MouseEvent e) {
    final int left = getLeft();
    final int top = getTop();
    final int x = e.getX();
    final int y = e.getY();

    if (x < left) {
      return;
    }

    if (y < top) {
      return;
    }

    show();
    FrEnd.panel_preferences_update.checkbox_iconise_bottom_toolbar
      .setState(false);
  }

  public void show() {
    FrEnd.panel_with_controls_at_bottom.add(FrEnd.panel_fundamental.panel);

    FrEnd.applet.validate();
    this.gui_is_iconised = false;
  }

  public void hide() {
    FrEnd.panel_with_controls_at_bottom.removeAll();
    FrEnd.applet.validate();

    this.gui_is_iconised = true;
  }
}