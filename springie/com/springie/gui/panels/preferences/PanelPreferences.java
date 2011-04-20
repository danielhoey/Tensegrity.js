// This program has been placed into the public domain by its author.

package com.springie.gui.panels.preferences;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.springie.FrEnd;
import com.springie.messages.MessageManager;
import com.springie.utilities.general.Forget;

public class PanelPreferences {
  public Panel panel = FrEnd.setUpPanelForFrame2();
  public Panel panel_centre = FrEnd.setUpPanelForFrame2();

  MessageManager message_manager;

  public PanelPreferences(MessageManager message_manager) {
    this.message_manager = message_manager;
    makePanel();
  }
  
  void makePanel() {
    final Panel panel_tabs = getPanelTabs();

    this.panel.setLayout(new BorderLayout());
    
    this.panel.add(panel_tabs, "North");
    
    usePanel(FrEnd.panel_preferences_display.panel);    
    this.panel.add(this.panel_centre, "Center");
    
    //this.panel_centre.validate();
    this.panel.validate();
  }

  private Panel getPanelTabs() {
    final Panel panel = new Panel();
    panel.setLayout(new GridLayout(1, 0, 0, 0));

    final Button button_display = new Button("Display");
    button_display.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        usePanel(FrEnd.panel_preferences_display.panel);
      }
    });

    final Button button_viewpoint = new Button("Viewpoint");
    button_viewpoint.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        usePanel(FrEnd.panel_preferences_viewpoint.panel);
      }
    });

    final Button button_stereo3d = new Button("Stereo3D");
    button_stereo3d.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        usePanel(FrEnd.panel_preferences_stereo3d.panel);
      }
    });

    final Button button_editing = new Button("Editing");
    button_editing.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        usePanel(FrEnd.panel_preferences_edit.panel);
      }
    });

    final Button button_update = new Button("Update");
    button_update.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        usePanel(FrEnd.panel_preferences_update.panel);
      }
    });

    final Button button_io = new Button("I/O");
    button_io.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        usePanel(FrEnd.panel_preferences_io.panel);
      }
    });

    panel.add(button_display);
    panel.add(button_viewpoint);
    panel.add(button_editing);
    panel.add(button_stereo3d);
    panel.add(button_update);
    panel.add(button_io);

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