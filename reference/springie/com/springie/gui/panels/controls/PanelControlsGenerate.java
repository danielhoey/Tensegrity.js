// This program has been placed into the public domain by its author.

package com.springie.gui.panels.controls;

import java.awt.Button;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.springie.FrEnd;
import com.springie.gui.GUIStrings;
import com.springie.messages.Message;
import com.springie.messages.MessageManager;
import com.springie.utilities.general.Forget;

public class PanelControlsGenerate {
  public Panel panel = FrEnd.setUpPanelForFrame2();

  MessageManager message_manager;

  public TextField textfield_generate_tube_length;

  public TextField textfield_generate_matrix_x;
  
  public TextField textfield_generate_matrix_y;
  
  public TextField textfield_generate_matrix_z;

  public TextField textfield_generate_tube_circumference;

  public TextField textfield_generate_free_nodes;

  public TextField textfield_generate_sphere_pack;

  //public Label label_number;
  //public Scrollbar scroll_bar_n;
  public PanelControlsGenerate(MessageManager message_manager) {
    this.message_manager = message_manager;
    makePanelGenerate();
  }

  void makePanelGenerate() {
    final Panel panel_generate_tube = makeTubeGUI();

    final Panel panel_generate_free_nodes = makeFreeNodesGUI();

    final Panel panel_generate_sphere_pack = makeSpherePackGUI();

    final Panel panel_generate_matrix = makeMatrixGUI();

    this.panel.add(panel_generate_tube);
    this.panel.add(panel_generate_free_nodes);
    this.panel.add(panel_generate_sphere_pack);
    this.panel.add(panel_generate_matrix);
  }

  private Panel makeTubeGUI() {
    final Button button = new Button(GUIStrings.GENERATE_TUBE);
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_GENERATE_TUBE, 0, 0);
      }
    });

    final Panel panel = new Panel();

    panel.add(new Label("Length:", Label.RIGHT));
    this.textfield_generate_tube_length = new TextField("20");
    panel.add(this.textfield_generate_tube_length);

    panel.add(new Label("D:", Label.RIGHT));
    this.textfield_generate_tube_circumference = new TextField("3");
    panel.add(this.textfield_generate_tube_circumference);

    panel.add(button);
    return panel;
  }

  private Panel makeMatrixGUI() {
    final Button button = new Button(GUIStrings.GENERATE_MATRIX);
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_GENERATE_MATRIX, 0, 0);
      }
    });

    final Panel panel = new Panel();

    panel.add(new Label("X:", Label.RIGHT));
    this.textfield_generate_matrix_x = new TextField("6");
    panel.add(this.textfield_generate_matrix_x);

    panel.add(new Label("Y:", Label.RIGHT));
    this.textfield_generate_matrix_y = new TextField("3");
    panel.add(this.textfield_generate_matrix_y);

    panel.add(new Label("Z:", Label.RIGHT));
    this.textfield_generate_matrix_z = new TextField("6");
    panel.add(this.textfield_generate_matrix_z);

    panel.add(button);
    return panel;
  }

  private Panel makeFreeNodesGUI() {
    final Button button = new Button(
      GUIStrings.GENERATE_FREE_NODES);
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_GENERATE_FREE_NODES, 0, 0);
      }
    });

    final Panel panel = new Panel();

    panel.add(new Label("N:", Label.RIGHT));
    this.textfield_generate_free_nodes = new TextField("100");

    panel.add(this.textfield_generate_free_nodes);

    panel.add(button);
    return panel;
  }

  private Panel makeSpherePackGUI() {
    final Button button = new Button(
      GUIStrings.GENERATE_SPHERE_PACK);
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_GENERATE_SPHERE_PACK, 0, 0);
      }
    });

    final Panel panel = new Panel();

    panel.add(new Label("N:", Label.RIGHT));
    this.textfield_generate_sphere_pack = new TextField("20");

    panel.add(this.textfield_generate_sphere_pack);

    panel.add(button);
    return panel;
  }

  public MessageManager getMessageManager() {
    return this.message_manager;
  }
}