// This program has been placed into the public domain by its author.

package com.springie.gui.panels;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;

import com.springie.FrEnd;
import com.springie.gui.GUIStrings;

public class PanelAbout {
  public Panel panel = FrEnd.setUpPanelForFrame2();

  public PanelAbout() {
    final Label label_about_t = new Label(GUIStrings.ABOUT, Label.CENTER);
    final Label label_about_d = new Label(GUIStrings.ABOUT_DESC, Label.CENTER);
    final Label label_about_v = new Label(GUIStrings.ABOUT_VER, Label.CENTER);
    final Label label_about_p = new Label(GUIStrings.ABOUT_PD, Label.CENTER);
    final Panel panel_about = new Panel();
    panel_about.setLayout(new GridLayout(0, 1, 0, 0));
    panel_about.add(label_about_t);
    panel_about.add(label_about_d);
    panel_about.add(label_about_v);
    panel_about.add(label_about_p);

    this.panel.add(panel_about);
  }
}