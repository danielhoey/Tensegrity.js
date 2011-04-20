// This program has been placed into the public domain by its author.

package com.springie.gui.panels.preferences;

import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import com.springie.FrEnd;
import com.springie.constants.Actions;
import com.springie.gui.components.TTChoice;
import com.springie.messages.MessageManager;

public class PanelPreferencesEdit {
  public Panel panel = FrEnd.setUpPanelForFrame2();

  MessageManager message_manager;

  TTChoice choose_left_action;

  TTChoice choose_middle_action;

  TTChoice choose_right_action;

  public PanelPreferencesEdit(MessageManager message_manager) {
    this.message_manager = message_manager;
    makePanel();
  }

  void makePanel() {
    this.panel.add(getLeftActionPanel());
    this.panel.add(getMiddleActionPanel());
    this.panel.add(getRightActionPanel());
  }

  private Panel getRightActionPanel() {
    // how to use...
    final Panel panel_right_button_config = new Panel();
    //panel_right_button_config.setBackground(colour_grey1);
    panel_right_button_config.add(new Label("Right-click to", Label.RIGHT));
    this.choose_right_action = new TTChoice(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        FrEnd.action_right_type = getChooseRightAction().str_to_num(
          (String) (e.getItem()));
      }
    });
    addActionTypes(this.choose_right_action);

    this.choose_right_action.choice.select(this.choose_right_action
      .num_to_str(FrEnd.action_right_type));
    panel_right_button_config.add(this.choose_right_action.choice);
    return panel_right_button_config;
  }

  private Panel getMiddleActionPanel() {
    final Panel panel = new Panel();

    panel.add(new Label("Middle-click to", Label.RIGHT));
    this.choose_middle_action = new TTChoice(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        FrEnd.action_middle_type = getChooseMiddleAction().str_to_num(
          (String) (e.getItem()));
      }
    });
    addActionTypes(this.choose_middle_action);

    this.choose_middle_action.choice.select(this.choose_middle_action
      .num_to_str(FrEnd.action_middle_type));
    panel.add(this.choose_middle_action.choice);
    return panel;
  }

  private Panel getLeftActionPanel() {
    final Panel panel = new Panel();
    //panel_left_button_config.setBackground(colour_grey1);

    panel.add(new Label("Left-click to", Label.RIGHT));
    this.choose_left_action = new TTChoice(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        FrEnd.action_left_type = getChooseLeftAction().str_to_num(
          (String) (e.getItem()));
      }
    });
    addActionTypes(this.choose_left_action);

    this.choose_left_action.choice.select(this.choose_left_action
      .num_to_str(FrEnd.action_left_type));
    panel.add(this.choose_left_action.choice);
    return panel;
  }

  private void addActionTypes(TTChoice action_choice) {
    //action_choice.add("infect", _ACT_KILLALL);
    
    action_choice.add("select", Actions.SELECT);
    action_choice.add("rotate", Actions.ROTATE);
    action_choice.add("scale", Actions.SCALE);
    action_choice.add("diet", Actions.DIET);
    action_choice.add("translate", Actions.TRANSLATE);
    action_choice.add("delete", Actions.KILL);
    action_choice.add("link", Actions.LINK);
    action_choice.add("unlink", Actions.UNLINK);
    action_choice.add("clone", Actions.CLONE);
    action_choice.add("unlink all", Actions.UNLINKALL);
    action_choice.add("select no drag", Actions.SELECT_NO_DRAG);

    //action_choice.add("select links", _ACT_SELECT_LINKS);
    //    action_choice.add("select faces", _ACT_SELECT_POLYGONS);
    //action_choice.add("delete", _ACT_DELETE);
    //action_choice.add("grow", _ACT_GROW);
    //action_choice.add("shrink", _ACT_SHRINK);
    //action_choice.add("ignore", _ACT_IGNORE);
    //action_choice.add("drag", _ACT_DRAG);
    //action_choice.add("don't drag", _ACT_DONTDRAG);
    //action_choice.add("active feet", _ACT_ACTIVATE_FEET);
    //action_choice.add("inactive", _ACT_DEACTIVATE);
    //action_choice.add("read colour", _ACT_READ_COLOUR);
    //action_choice.add("set colour", _ACT_SET_COLOUR);
    //action_choice.add("make visibile", _ACT_VISIBLE);
    //action_choice.add("make invisibile", _ACT_INVISIBLE);
    //action_choice.add("freeze", _ACT_FREEZE);
    //action_choice.add("melt", _ACT_MELT);
  }
  
  public TTChoice getChooseMiddleAction() {
    return this.choose_middle_action;
  }

  public TTChoice getChooseLeftAction() {
    return this.choose_left_action;
  }

  public TTChoice getChooseRightAction() {
    return this.choose_right_action;
  }

  public MessageManager getMessageManager() {
    return this.message_manager;
  }
}