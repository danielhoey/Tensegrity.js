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
import com.springie.messages.MessageManager;
import com.springie.render.Coords;
import com.springie.utilities.general.Forget;

public class PanelPreferencesViewpoint {
  public Panel panel = FrEnd.setUpPanelForFrame2();

  private Scrollbar scroll_bar_translate_x;

  private Scrollbar scroll_bar_translate_y;

  private Scrollbar scroll_bar_translate_z;

  private Label label_translate_x;

  private Label label_translate_y;

  private Label label_translate_z;

  MessageManager message_manager;

  public PanelPreferencesViewpoint(MessageManager message_manager) {
    this.message_manager = message_manager;
    makePanel();
  }

  void makePanel() {
    final Panel panel_merge = new Panel();
    final Checkbox checkbox_merge = new Checkbox("Merge structures");
    checkbox_merge.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        FrEnd.merge = ((Checkbox) e.getSource()).getState();
      }
    });
    panel_merge.add(checkbox_merge);

    final Panel panel_translate_view_x = getTranslateViewXPanel();

    final Panel panel_translate_view_y = getTranslateViewYPanel();

    final Panel panel_translate_view_z = getTranslateViewZPanel();

    this.panel.add(panel_translate_view_x);
    this.panel.add(panel_translate_view_y);
    this.panel.add(panel_translate_view_z);
  }

  private Panel getTranslateViewXPanel() {
    final Panel panel = new Panel();
    panel.setLayout(new BorderLayout(0, 8));
    panel.add("West", new Label("Translate X:", Label.RIGHT));
    this.scroll_bar_translate_x = new Scrollbar(Scrollbar.HORIZONTAL, 0, 10,
      -110, 110);
    this.scroll_bar_translate_x.addAdjustmentListener(new AdjustmentListener() {
      public void adjustmentValueChanged(AdjustmentEvent e) {
        Coords.shift_constant_x = e.getValue() << 11;
        reflectTranslateX();
      }
    });
    panel.add("Center", this.scroll_bar_translate_x);

    this.label_translate_x = new Label("0", Label.LEFT);
    panel.add("East", this.label_translate_x);
    reflectTranslateX();

    return panel;
  }

  private Panel getTranslateViewYPanel() {
    final Panel panel = new Panel();
    panel.setLayout(new BorderLayout(0, 8));
    panel.add("West", new Label("Translate Y:", Label.RIGHT));
    this.scroll_bar_translate_y = new Scrollbar(Scrollbar.HORIZONTAL, 0, 10,
      -110, 110);
    this.scroll_bar_translate_y.addAdjustmentListener(new AdjustmentListener() {
      public void adjustmentValueChanged(AdjustmentEvent e) {
        Coords.shift_constant_y = e.getValue() << 11;
        reflectTranslateY();
      }
    });
    panel.add("Center", this.scroll_bar_translate_y);

    this.label_translate_y = new Label("0", Label.LEFT);
    panel.add("East", this.label_translate_y);
    reflectTranslateY();

    return panel;
  }

  private Panel getTranslateViewZPanel() {
    final Panel panel = new Panel();
    panel.setLayout(new BorderLayout(0, 8));
    panel.add("West", new Label("Translate Z:", Label.RIGHT));
    this.scroll_bar_translate_z = new Scrollbar(Scrollbar.HORIZONTAL, 10, 10,
      0, 110);
    this.scroll_bar_translate_z.addAdjustmentListener(new AdjustmentListener() {
      public void adjustmentValueChanged(AdjustmentEvent e) {
        Coords.shift_constant_z = e.getValue() << 3;
        reflectTranslateZ();
      }
    });
    panel.add("Center", this.scroll_bar_translate_z);

    this.label_translate_z = new Label("0", Label.LEFT);
    panel.add("East", this.label_translate_z);
    reflectTranslateZ();

    return panel;
  }

  void reflectTranslateX() {
    final int tmp = Coords.shift_constant_x >> 11;

    this.scroll_bar_translate_x.setValue(tmp);

    this.label_translate_x.setText("" + tmp);
  }

  void reflectTranslateY() {
    final int tmp = Coords.shift_constant_y >> 11;

    this.scroll_bar_translate_y.setValue(tmp);

    this.label_translate_y.setText("" + tmp);
  }

  void reflectTranslateZ() {
    final int tmp = Coords.shift_constant_z >> 3;

    this.scroll_bar_translate_z.setValue(tmp);

    this.label_translate_z.setText("" + tmp);
  }

  public MessageManager getMessageManager() {
    return this.message_manager;
  }
}
