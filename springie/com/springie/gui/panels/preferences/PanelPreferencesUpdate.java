// This program has been placed into the public domain by its author.

package com.springie.gui.panels.preferences;

import java.awt.Checkbox;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import com.springie.FrEnd;
import com.springie.constants.Delay;
import com.springie.gui.components.TTChoice;
import com.springie.messages.MessageManager;

public class PanelPreferencesUpdate {
  public Panel panel = FrEnd.setUpPanelForFrame2();

  MessageManager message_manager;

  TTChoice choose_left_action;

  TTChoice choose_right_action;

  private Checkbox checkbox_animate_pointer_over;
  
  public Checkbox checkbox_iconise_bottom_toolbar;
  
  public PanelPreferencesUpdate(MessageManager message_manager) {
    this.message_manager = message_manager;
    makePanel();
  }

  void makePanel() {
    final TTChoice choose_delay = new TTChoice(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        final String string = (String) e.getItem();
        FrEnd.delay = FrEnd.choose_delay.str_to_num(string);
      }
    });

    FrEnd.choose_delay = choose_delay;

    final Panel panel_delay = new Panel();
    panel_delay.add(new Label("Delay:", Label.RIGHT));
    choose_delay.add("0", Delay._DELAY_0);
    choose_delay.add("1", Delay._DELAY_1);
    choose_delay.add("2", Delay._DELAY_2);
    choose_delay.add("5", Delay._DELAY_5);
    choose_delay.add("10", Delay._DELAY_10);
    choose_delay.add("20", Delay._DELAY_20);
    choose_delay.add("50", Delay._DELAY_50);
    choose_delay.add("100", Delay._DELAY_100);
    choose_delay.add("200", Delay._DELAY_200);
    choose_delay.add("512", Delay._DELAY_512);
    choose_delay.choice.select(choose_delay.num_to_str(FrEnd.delay));
    panel_delay.add(choose_delay.choice);

    final Panel panel_step_size = new Panel();
    
    FrEnd.label_step_size = new Label("Step size:", Label.RIGHT);
    panel_step_size.add(FrEnd.label_step_size);

    FrEnd.textfield_step_size = new TextField("1", 0);
    panel_step_size.add(FrEnd.textfield_step_size);

    this.panel.add(getAnimatePointerOverPanel());
    this.panel.add(getHideBottomToolbarPanel());
    this.panel.add(panel_delay);
    this.panel.add(panel_step_size);
  }

  private Panel getAnimatePointerOverPanel() {
    final boolean b = ((Boolean)FrEnd.preferences.map.get(FrEnd.preferences.key_update_animation_when_pointer_over)).booleanValue();
    
    this.checkbox_animate_pointer_over = new Checkbox("Stop animating when pointer exits", b);
    this.checkbox_animate_pointer_over.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        FrEnd.preferences.map.put(FrEnd.preferences.key_update_animation_when_pointer_over, new Boolean(((Checkbox) e.getSource()).getState()));
      }
    });

    final Panel panel = new Panel();

    panel.add(this.checkbox_animate_pointer_over);
    
    return panel;
  }

  private Panel getHideBottomToolbarPanel() {
    final boolean b = FrEnd.viewer;
    
    this.checkbox_iconise_bottom_toolbar = new Checkbox("Iconise bottom toolbar", b);
    this.checkbox_iconise_bottom_toolbar.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (((Checkbox) e.getSource()).getState()) {
          FrEnd.main_canvas.getInfoButton().hide();
        } else {
          FrEnd.main_canvas.getInfoButton().show();
        }
      }
    });

    final Panel panel = new Panel();

    panel.add(this.checkbox_iconise_bottom_toolbar);
    
    return panel;
  }

  public MessageManager getMessageManager() {
    return this.message_manager;
  }
}