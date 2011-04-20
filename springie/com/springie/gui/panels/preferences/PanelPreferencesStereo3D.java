// This program has been placed into the public domain by its author.

package com.springie.gui.panels.preferences;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Scrollbar;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import com.springie.FrEnd;
import com.springie.collisions.BinGrid;
import com.springie.elements.nodes.NodeManager;
import com.springie.gui.components.TTChoice;
import com.springie.messages.MessageManager;
import com.springie.utilities.general.Forget;

public class PanelPreferencesStereo3D {
  public Panel panel = FrEnd.setUpPanelForFrame2();

  MessageManager message_manager;

  Scrollbar scroll_bar_eye_distance;

  public Checkbox checkbox_anaglyph;

  private Label label_eye_distance;

  int eye_distance_scale_factor = 160;
  
  TTChoice choice_eye_left;

  TTChoice choice_eye_right;

  protected Object getEyeDistanceScaleFactor;

  public PanelPreferencesStereo3D(MessageManager message_manager) {
    this.message_manager = message_manager;
    makePanel();
  }

  void makePanel() {
    final Panel panel_anaglyph = new Panel();
    this.checkbox_anaglyph = new Checkbox("Stereo3D", FrEnd.render_anaglyph);
    this.checkbox_anaglyph.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        FrEnd.render_anaglyph = ((Checkbox) e.getSource()).getState();
        BinGrid.repaint_all_objects = true;
      }
    });
    panel_anaglyph.add(this.checkbox_anaglyph);

    final Panel panel_eye_left = getEyeLeftPanel();
    final Panel panel_eye_right = getEyeRightPanel();

    final Panel panel_eye_distance = getEyeDistancePanel();

    this.panel.add(panel_anaglyph);
    this.panel.add(panel_eye_distance);
    this.panel.add(panel_eye_left);
    this.panel.add(panel_eye_right);
  }

  private Panel getEyeLeftPanel() {
    this.choice_eye_left = new TTChoice(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        final String string = (String) e.getItem();
        NodeManager.mask_left = getChoiceEyeLeft().str_to_num(string);
      }
    });

    final Panel panel = new Panel();
    panel.add(new Label("Left eye:", Label.RIGHT));
    addColourOptions(this.choice_eye_left);
    this.choice_eye_left.choice.select(this.choice_eye_left.num_to_str(NodeManager.mask_left));
    panel.add(this.choice_eye_left.choice);
    return panel;
  }

  private Panel getEyeRightPanel() {
    this.choice_eye_right = new TTChoice(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        final String string = (String) e.getItem();
        NodeManager.mask_right = getChoiceEyeRight().str_to_num(string);
      }
    });

    final Panel panel = new Panel();
    panel.add(new Label("Right eye:", Label.RIGHT));
    addColourOptions(this.choice_eye_right);
    this.choice_eye_right.choice.select(this.choice_eye_right.num_to_str(NodeManager.mask_right));
    panel.add(this.choice_eye_right.choice);
    return panel;
  }

  private void addColourOptions(TTChoice choice) {
    choice.add("red", 0xFF0000);
    choice.add("green", 0x00FF00);
    choice.add("blue", 0x0000FF);
    choice.add("cyan", 0x00FFFF);
    choice.add("magenta", 0xFF00FF);
    choice.add("yellow", 0xFFFFFF);
  }

  private Panel getEyeDistancePanel() {
    final Panel panel = new Panel();
    panel.setLayout(new BorderLayout(0, 8));
    panel.add("West", new Label("Eye distance:", Label.RIGHT));

    this.scroll_bar_eye_distance = new Scrollbar(Scrollbar.HORIZONTAL,
      NodeManager.eye_distance / this.eye_distance_scale_factor, 10, -100, 110);
    this.scroll_bar_eye_distance
      .addAdjustmentListener(new AdjustmentListener() {
        public void adjustmentValueChanged(AdjustmentEvent e) {
          final int temp = e.getValue();
          NodeManager.eye_distance = temp * getEyeDistanceScaleFactor();
          reflectLabelEyeDistance();
          BinGrid.repaint_all_objects = true;
        }
      });

    panel.add("Center", this.scroll_bar_eye_distance);

    this.label_eye_distance = new Label("", Label.LEFT);
    panel.add("East", this.label_eye_distance);
    reflectLabelEyeDistance();

    return panel;
  }

  public void reflectLabelEyeDistance() {
    final int v = NodeManager.eye_distance / this.eye_distance_scale_factor;
    this.scroll_bar_eye_distance.setValue(v);
    this.label_eye_distance.setText("" + v);
  }

  int getEyeDistanceScaleFactor() {
    return this.eye_distance_scale_factor;
  }

  protected TTChoice getChoiceEyeLeft() {
    return this.choice_eye_left;
  }

  protected TTChoice getChoiceEyeRight() {
    return this.choice_eye_right;
  }

  public MessageManager getMessageManager() {
    return this.message_manager;
  }
}