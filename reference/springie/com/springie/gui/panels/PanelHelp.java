// This code has been placed into the public domain by its author

package com.springie.gui.panels;

import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextArea;

import com.springie.FrEnd;
import com.springie.io.in.ResourceLoader;

public class PanelHelp {
  public Panel panel = FrEnd.setUpPanelForFrame2();

  public PanelHelp() {
    final TextArea helptext = new TextArea("");
    final ResourceLoader rl = new ResourceLoader();
    final String text = rl
      .getResourceAsString(FrEnd.class, "help/helptext.txt");

    helptext.append(text);
    final Panel panel_help = new Panel();
    panel_help.setLayout(new GridLayout(0, 1, 0, 0));
    panel_help.add(helptext);

    this.panel.add(panel_help);
  }
}