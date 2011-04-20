// This program has been placed into the public domain by its author.

package com.springie.gui.panels;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Enumeration;

import org.xml.sax.SAXException;

import com.springie.FrEnd;
import com.springie.gui.GUIStrings;
import com.springie.gui.components.ChoiceWithDescription;
import com.springie.messages.Message;
import com.springie.messages.MessageManager;
import com.springie.presets.AddXMLModelIndexLeaves;
import com.springie.utilities.general.Forget;

public class PanelFundamental {
  public Panel panel = FrEnd.setUpPanelForFrame2();

  MessageManager message_manager;

  public TextField textfield_generate_tube_length;

  public TextField textfield_generate_matrix_x;

  public TextField textfield_generate_matrix_y;

  public TextField textfield_generate_matrix_z;

  public TextField textfield_generate_tube_circumference;

  public TextField textfield_generate_free_nodes;

  public TextField textfield_generate_sphere_pack;
  
  public Checkbox checkbox_pause;

  public PanelFundamental(MessageManager message_manager) {
    this.message_manager = message_manager;
    makePanelGenerate();
  }

  void makePanelGenerate() {
    initialSetup();
    this.panel.setLayout(new GridLayout(1, 0, 0, 0));

    final Panel panel_restart = makePanelRestart();
    final Panel panel_pause = makePanelPause();
    final Panel panel_preset_index = makePanelPresetIndex();
    final Panel panel_initial_configuration = makePanelInitialCvonfiguration();
    final Panel panel_controls_edit = makePanelControls();
    final Panel panel_controls_preferences = makePanelPreferences();

    this.panel.add(panel_controls_edit);
    this.panel.add(panel_controls_preferences);
    this.panel.add(panel_pause);
    this.panel.add(panel_preset_index);
    this.panel.add(panel_initial_configuration);
    this.panel.add(panel_restart);
  }

  private Panel makePanelPreferences() {
    final Panel panel_controls_preferences = new Panel();
    final Button button_controls_preferences = new Button(
      GUIStrings.CONTROLS_PREFERENCES);
    button_controls_preferences.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        FrEnd.frame_preferences.setVisible(true);
      }
    });
    panel_controls_preferences.add(button_controls_preferences);
    return panel_controls_preferences;
  }

  private Panel makePanelControls() {
    final Panel panel_controls_edit = new Panel();
    final Button button_controls_edit = new Button(GUIStrings.CONTROLS_EDIT);
    button_controls_edit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        FrEnd.frame_controls.setVisible(true);
      }
    });
    panel_controls_edit.add(button_controls_edit);
    return panel_controls_edit;
  }

  private Panel makePanelPresetIndex() {
    final Panel panel_preset_index = new Panel();
    panel_preset_index.add(FrEnd.choose_preset_index.choice);
    return panel_preset_index;
  }

  private Panel makePanelInitialCvonfiguration() {
    final Panel panel_initial_configuration = new Panel();
    panel_initial_configuration.add(FrEnd.choose_initial.choice);
    return panel_initial_configuration;
  }

  private Panel makePanelPause() {
    // pause...
    final Panel panel = new Panel();
    this.checkbox_pause = new Checkbox(GUIStrings.PAUSE);
    this.checkbox_pause.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_PAUSE, 0, 0);
      }
    });
    panel.add(this.checkbox_pause);

    // Step...
    FrEnd.button_step = new Button(GUIStrings.STEP);
    FrEnd.button_step.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_STEP, 0, 0);
      }
    });

    FrEnd.button_step.setFont(FrEnd.bold_font);
    panel.add(FrEnd.button_step);

    return panel;
  }

  private Panel makePanelRestart() {
    final Panel panel = new Panel();
    FrEnd.button_restart = new Button(GUIStrings.RESTART);
    FrEnd.button_restart.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_RESTART, 0, 0);
      }
    });

    FrEnd.button_restart.setFont(FrEnd.bold_font);
    FrEnd.button_restart.setForeground(Color.red);
    panel.setLayout(new BorderLayout(0, 0));
    panel.add(FrEnd.button_restart);
    return panel;
  }

  private void initialSetup() {
    setUpPresetIndex();

    try {
      new AddXMLModelIndexLeaves().addLeaves(FrEnd.choose_preset_index,
        FrEnd.model_index);
    } catch (IOException e1) {
      e1.printStackTrace();
    } catch (SAXException e1) {
      e1.printStackTrace();
    }

    setUpInitialChoice();
  }

  private void setUpInitialChoice() {
    FrEnd.choose_initial = new ChoiceWithDescription(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e == null) {
          return;
        }

        final String string = (String) (e.getItem());

        FrEnd.next_file_path = (String) FrEnd.choose_initial.hashtable
          .get(string);
      }
    });

    final String index_name = (String) FrEnd.choose_preset_index.hashtable
      .get("Presets");

    setUpLeafIndex(index_name);

    FrEnd.choose_initial.choice.addKeyListener(new KeyListener() {
      public void keyTyped(KeyEvent arg0) {
        Forget.about(arg0);
      }

      public void keyPressed(KeyEvent arg0) {
        if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
          getMessageManager().newmessage(Message.MSG_PRESET_CHOSEN, 0, 0);
        }
      }

      public void keyReleased(KeyEvent arg0) {
        Forget.about(arg0);
      }
    });
  }

  private void setUpPresetIndex() {
    FrEnd.choose_preset_index = new ChoiceWithDescription(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e == null) {
          return;
        }

        final String string = (String) (e.getItem());
        final String path = (String) FrEnd.choose_preset_index.hashtable
          .get(string);

        setUpLeafIndex(path);
      }
    });
  }

  private void setUpLeafIndex(final String path) {
    FrEnd.choose_initial.removeAll();
    try {
      new AddXMLModelIndexLeaves().addLeaves(FrEnd.choose_initial, path);
    } catch (IOException e1) {
      e1.printStackTrace();
    } catch (SAXException e1) {
      e1.printStackTrace();
    }
  }

  public static String getXMLIndexPath() {
    final Enumeration e = FrEnd.choose_preset_index.hashtable.keys();
    final String initial = (String) e.nextElement();
    return initial;
  }

  public MessageManager getMessageManager() {
    return this.message_manager;
  }
}