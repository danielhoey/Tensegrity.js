// This program has been placed into the public domain by its author.

package com.springie.gui.panels.controls;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.springie.FrEnd;
import com.springie.messages.MessageManager;
import com.springie.utilities.general.Forget;

public class PanelControls {
  public Panel panel = FrEnd.setUpPanelForFrame2();
  public Panel panel_centre = FrEnd.setUpPanelForFrame2();

  MessageManager message_manager;

  public PanelControls(MessageManager message_manager) {
    this.message_manager = message_manager;
    makePanel();
  }

  void makePanel() {
    final Panel panel_tabs = getPanelTabs();

    this.panel.setLayout(new BorderLayout());
    
    this.panel.add(panel_tabs, "North");
    
    usePanel(FrEnd.panel_edit_edit.panel);    
    this.panel.add(this.panel_centre, "Center");
    
    this.panel.validate();
  }

  private Panel getPanelTabs() {
    final Panel panel = new Panel();
    panel.setLayout(new GridLayout(1, 0, 0, 0));

    final Button button_edit = new Button("Edit");
    button_edit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        usePanel(FrEnd.panel_edit_edit.panel);
      }
    });

    final Button button_universe = new Button("Universe");
    button_universe.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        usePanel(FrEnd.panel_edit_universe.panel);
      }
    });

    final Button button_generate = new Button("Generate");
    button_generate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        usePanel(FrEnd.panel_edit_generate.panel);
      }
    });

    final Button button_misc = new Button("Misc");
    button_misc.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        usePanel(FrEnd.panel_edit_misc.panel);
      }
    });

    panel.add(button_edit);
    panel.add(button_universe);
    panel.add(button_generate);    
    panel.add(button_misc);

    return panel;
  }

  protected Panel getPanel() {
    return this.panel;
  }

  private void usePanel(Panel p) {
    this.panel_centre.removeAll();
    this.panel_centre.add(p, "Center");
    this.panel_centre.validate();
  }

  public MessageManager getMessageManager() {
    return this.message_manager;
  }
}