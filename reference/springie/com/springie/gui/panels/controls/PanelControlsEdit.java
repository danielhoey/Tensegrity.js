// This program has been placed into the public domain by its author.

package com.springie.gui.panels.controls;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Scrollbar;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import com.springie.FrEnd;
import com.springie.gui.GUIStrings;
import com.springie.gui.colourpicker.ColourPicker;
import com.springie.gui.colourpicker.ColourPickerGetAndSet;
import com.springie.gui.colourpicker.ColourPickerPreview;
import com.springie.gui.colourpicker.ColourPickerVB;
import com.springie.gui.colourpicker.ColourPickerVH;
import com.springie.gui.colourpicker.ColourPickerVS;
import com.springie.gui.colourpicker.ColourPickerVT;
import com.springie.gui.colourpicker.ColourPickerValueDisplay;
import com.springie.gui.components.ComponentAccess;
import com.springie.messages.Message;
import com.springie.messages.MessageManager;
import com.springie.metrics.AverageChargeGetter;
import com.springie.metrics.AverageElasticityGetter;
import com.springie.metrics.AverageLengthGetter;
import com.springie.metrics.AverageRadiusGetter;
import com.springie.metrics.AverageStiffnessGetter;
import com.springie.utilities.general.Forget;

public class PanelControlsEdit {
  public Panel panel = FrEnd.setUpPanelForFrame2();

  MessageManager message_manager;

  public Scrollbar scroll_bar_elasticity;

  public Scrollbar scroll_bar_length;

  public Scrollbar scroll_bar_radius;

  public Scrollbar scroll_bar_charge;

  public Scrollbar scroll_bar_damping;

  public Scrollbar scroll_bar_scale_by;

  public Label label_elasticity;

  public Label label_length;

  public Label label_damping;

  public Label label_radius;

  public Label label_charge;

  private Label label_scale_by;

  public Button button_setsize;

  Button button_colour_fix;

  Button button_radius_set;

  Button button_radius_fix;

  Button button_charge_fix;

  Button button_damping_set;

  Button button_damping_fix;

  Button button_elasticity_set;

  Button button_elasticity_fix;

  Button button_scale_elasticity_up;

  Button button_scale_elasticity_down;

  Button button_scale_lengths_up;

  Button button_scale_lengths_down;

  Button button_scale_radius_up;

  Button button_scale_radius_down;

  Button button_scale_charge_up;

  Button button_scale_charge_down;

  Button button_scale_damping_up;

  Button button_scale_damping_down;

  Button button_select_all_type;

  Button button_deselect_all_type;

  Button button_select_clazz;

  Button button_select_type;

  Button button_delete;

  Button button_edit_make_motionless;

  public Checkbox checkbox_select_nodes;

  public Checkbox checkbox_select_links;

  public Checkbox checkbox_select_faces;

  public Checkbox checkbox_fixed;

  public Checkbox checkbox_hidden;

  public Checkbox checkbox_cable;

  public Checkbox checkbox_disabled;

  public ColourPicker colour_picker;

  public TextField textfield_colour_w;

  public TextField textfield_colour_r;

  public TextField textfield_size_r;

  public TextField textfield_size_w;

  public TextField textfield_e_r;

  public TextField textfield_e_w;

  public TextField textfield_radius_r;

  public TextField textfield_radius_w;

  public TextField textfield_charge_r;

  public TextField textfield_charge_w;

  public TextField textfield_change_elasticity_by;

  public TextField textfield_change_damping_by;

  public ColourPickerGetAndSet cp_gas;

  ColourPickerPreview cp_fc;

  public PanelControlsEdit(MessageManager message_manager) {
    this.message_manager = message_manager;
    makeEditPanel();
  }

  void makeEditPanel() {
    this.colour_picker = new ColourPicker();

    final ColourPickerVH cph = new ColourPickerVH();
    cph.init();

    final ColourPickerVS cps = new ColourPickerVS();
    cps.init();

    final ColourPickerVB cpb = new ColourPickerVB();
    cpb.init();

    final ColourPickerVT cpt = new ColourPickerVT();
    cpt.init();

    this.cp_fc = new ColourPickerPreview();

    final ColourPickerValueDisplay cpfcv = new ColourPickerValueDisplay();
    cpfcv.init();

    this.cp_gas = new ColourPickerGetAndSet();

    this.colour_picker.inform(this.cp_fc);
    this.cp_fc.inform(this.colour_picker);

    this.colour_picker.inform(cph);
    cph.inform(this.colour_picker);

    this.colour_picker.inform(cps);
    cps.inform(this.colour_picker);

    this.colour_picker.inform(cpb);
    cpb.inform(this.colour_picker);

    this.colour_picker.inform(cpt);
    cpt.inform(this.colour_picker);

    this.colour_picker.inform(cpfcv);
    cpfcv.inform(this.colour_picker);

    this.cp_gas.inform(this.colour_picker);
    this.colour_picker.inform(this.cp_gas);

    final Label label_select_select_label = new Label("Select all of:", Label.RIGHT);
    final Panel panel_select_all = new Panel();

    panel_select_all.add(label_select_select_label);

    this.button_select_clazz = new Button("class");
    this.button_select_clazz.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_SELECT_CLAZZ, 0, 0);
      }
    });
    panel_select_all.add(this.button_select_clazz);

    this.button_select_type = new Button("type");
    this.button_select_type.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_SELECT_TYPE, 0, 0);
      }
    });
    panel_select_all.add(this.button_select_type);

    final Label label_deselect_deselect_label = new Label("Type", Label.RIGHT);
    final Panel panel_type_select_deselect = new Panel();

    panel_type_select_deselect.add(label_deselect_deselect_label);

    this.button_select_all_type = new Button("Select");
    this.button_select_all_type.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_SELECT_ALL_TYPE, 0, 0);
      }
    });
    panel_type_select_deselect.add(this.button_select_all_type);

    this.button_deselect_all_type = new Button("Deselect");
    this.button_deselect_all_type.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_DESELECT_ALL_TYPE, 0, 0);
      }
    });

    panel_type_select_deselect.add(this.button_deselect_all_type);

    final Panel panel_delete = new Panel();
    this.button_delete = new Button("Delete");
    this.button_delete.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_DELETE, 0, 0);
      }
    });
    panel_delete.add(this.button_delete);

    //MAKE_MOTIONLESS
    this.button_edit_make_motionless = new Button(
      GUIStrings.DOME_MAKE_MOTIONLESS);
    this.button_edit_make_motionless.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_DOME_MAKE_MOTIONLESS, 0, 0);
      }
    });
    //final Panel panel_edit_make_motionless = new Panel();
    panel_delete.add(this.button_edit_make_motionless);

    final Panel panel_edit_checkboxes_1 = new Panel();
    this.checkbox_hidden = new Checkbox("Hidden");
    this.checkbox_hidden.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_EDIT_HIDE_FLAG, 0, 0);
      }
    });
    panel_edit_checkboxes_1.add(this.checkbox_hidden);

    this.checkbox_fixed = new Checkbox("Fixed");
    this.checkbox_fixed.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_EDIT_FIXED_FLAG, 0, 0);
      }
    });
    panel_edit_checkboxes_1.add(this.checkbox_fixed);

    final Panel panel_edit_checkboxes_2 = new Panel();

    this.checkbox_cable = new Checkbox("Cable");
    this.checkbox_cable.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_EDIT_ROPE_FLAG, 0, 0);
      }
    });
    panel_edit_checkboxes_2.add(this.checkbox_cable);

    this.checkbox_disabled = new Checkbox("Disabled");
    this.checkbox_disabled.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_EDIT_DISABLED_FLAG, 0, 0);
      }
    });
    panel_edit_checkboxes_2.add(this.checkbox_disabled);

    final Panel panel_type_selector = new Panel();
    this.checkbox_select_nodes = new Checkbox(GUIStrings.NODES, true);
    this.checkbox_select_nodes.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        greySelectButtonsDependingOnSelection();
      }
    });
    panel_type_selector.add(this.checkbox_select_nodes);
    
    this.checkbox_select_links = new Checkbox(GUIStrings.LINKS, true);
    this.checkbox_select_links.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        greySelectButtonsDependingOnSelection();
      }
    });
    panel_type_selector.add(this.checkbox_select_links);
    
    this.checkbox_select_faces = new Checkbox(GUIStrings.FACES, true);
    this.checkbox_select_faces.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        greySelectButtonsDependingOnSelection();
      }
    });
    panel_type_selector.add(this.checkbox_select_faces);

    //final Panel panel_scale_radius = getPanelScaleRadius();

    //final Panel panel_scale_elasticity = getPanelScaleElasticity();

    //final Panel panel_scale_length = getPanelScaleLength();

    //final Panel panel_scale_damping = getPanelScaleDamping();

    //final Panel panel_scale_charge = getPanelScaleCharge();


    final Panel panel_length = setUpLengthSlider();

    final Panel panel_radius = setUpRadiusSlider();

    final Panel panel_elasticity = setUpElasticitySlider();

    final Panel panel_damping = setUpSliderDamping();

    final Panel panel_charge = setUpChargeSlider();
    
    final Panel panel_scale_by = getPanelScaleByQuantity();

//    final Panel panel_controls_misc = new Panel();
//    final Button button_controls_misc = new Button(GUIStrings.CONTROLS_MISC);
//    button_controls_misc.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        Forget.about(e);
//        FrEnd.frame_extra_controls.setVisible(true);
//      }
//    });
//    panel_controls_misc.add(button_controls_misc);
//
//    //final Panel panel_controls_generate = new Panel();
//    final Button button_controls_generate = new Button(
//      GUIStrings.CONTROLS_GENERATE);
//    button_controls_generate.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        Forget.about(e);
//        FrEnd.frame_generate.setVisible(true);
//      }
//    });
//    panel_controls_misc.add(button_controls_generate);

    //edit...
    this.panel.add(this.colour_picker);
    this.panel.add(cph);
    this.panel.add(cps);
    this.panel.add(cpb);
    this.panel.add(cpt);
    this.panel.add(this.cp_fc);
    this.panel.add(this.cp_gas);
    this.panel.add(cpfcv);

    this.panel.add(panel_type_selector);
    this.panel.add(panel_type_select_deselect);
    this.panel.add(panel_select_all);
    this.panel.add(panel_delete);

    this.panel.add(panel_scale_by);
    this.panel.add(panel_radius);
    this.panel.add(panel_length);
    this.panel.add(panel_elasticity);
    this.panel.add(panel_damping);
    this.panel.add(panel_charge);

    //this.panel.add(panel_scale_radius);
    //this.panel.add(panel_scale_length);
    //this.panel.add(panel_scale_elasticity);
    //this.panel.add(panel_scale_damping);
    //this.panel.add(panel_scale_charge);

    this.panel.add(panel_edit_checkboxes_1);
    this.panel.add(panel_edit_checkboxes_2);

    //this.panel.add(panel_edit_make_motionless);

    //this.panel.add(panel_left_button_config);
    //this.panel.add(panel_right_button_config);
    //this.panel.add(panel_controls_misc);
  }

  private Panel getPanelScaleByQuantity() {
    final Panel panel = new Panel();
    final Panel panel_east = new Panel();
    final Panel panel_west = new Panel();
    final Panel panel_center = new Panel();
    panel.setLayout(new BorderLayout(0, 8));
    panel_center.setLayout(new GridLayout(1, 1, 0, 0));
    final Label label = new Label("Scale by:", Label.RIGHT);
    panel_west.add(label);

    this.scroll_bar_scale_by = new Scrollbar(Scrollbar.HORIZONTAL, 10, 10,
      0, 110);
    this.scroll_bar_scale_by
      .addAdjustmentListener(new AdjustmentListener() {
        public void adjustmentValueChanged(AdjustmentEvent e) {
          final int value = e.getValue();
          getLabelScaleBy().setText("" + value + "%");
        }
      });
    panel_center.add(this.scroll_bar_scale_by);
    this.label_scale_by = new Label("10%", Label.LEFT);
    panel_east.add(this.label_scale_by);

    panel.add("East", panel_east);
    panel.add("West", panel_west);
    panel.add("Center", panel_center);

    return panel;
  }

  private Panel setUpLengthSlider() {
    final Panel panel = new Panel();
    panel.setLayout(new BorderLayout(0, 8));
    panel.add("West", new Label("Length:", Label.RIGHT));

    this.scroll_bar_length = new Scrollbar(Scrollbar.HORIZONTAL, 0, 100, 0,
      99899);
    this.scroll_bar_length.addAdjustmentListener(new AdjustmentListener() {
      public void adjustmentValueChanged(AdjustmentEvent e) {
        final int length = e.getValue();
        getMessageManager().newmessage(Message.MSG_ALTER_LENGTH, length, 0);
        reflectLength();
      }
    });

    panel.add("Center", this.scroll_bar_length);

    this.label_length = new Label("X", Label.LEFT);
    panel.add("East", this.label_length);

    this.button_scale_lengths_up = new Button(GUIStrings.EDIT_CHANGE_UP);
    this.button_scale_lengths_up.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_SCALE_LINKS_UP, 0, 0);
      }
    });

    this.button_scale_lengths_down = new Button(GUIStrings.EDIT_CHANGE_DOWN);
    this.button_scale_lengths_down.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_SCALE_LINKS_DOWN, 0, 0);
      }
    });

    final Panel panel_east = new Panel();
    panel_east.add(this.label_length);
    panel_east.add(this.button_scale_lengths_down);
    panel_east.add(this.button_scale_lengths_up);

    panel.add("East", panel_east);

    return panel;
  }

  private Panel setUpRadiusSlider() {
    final Panel panel = new Panel();
    panel.setLayout(new BorderLayout(0, 8));
    panel.add("West", new Label("Radius:", Label.RIGHT));

    this.scroll_bar_radius = new Scrollbar(Scrollbar.HORIZONTAL, 0, 100, 0,
      39899);
    this.scroll_bar_radius.addAdjustmentListener(new AdjustmentListener() {
      public void adjustmentValueChanged(AdjustmentEvent e) {
        final int radius = e.getValue();
        getMessageManager().newmessage(Message.MSG_ALTER_RADIUS, radius, 0);
      }
    });

    panel.add("Center", this.scroll_bar_radius);

    this.label_radius = new Label("X", Label.LEFT);

    this.button_scale_radius_up = new Button(GUIStrings.EDIT_CHANGE_UP);
    this.button_scale_radius_up.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_DOME_NODES_EXPAND, 0, 0);
      }
    });

    this.button_scale_radius_down = new Button(GUIStrings.EDIT_CHANGE_DOWN);
    this.button_scale_radius_down.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_DOME_NODES_CONTRACT, 0, 0);
      }
    });

    final Panel panel_east = new Panel();
    panel_east.add(this.label_radius);
    panel_east.add(this.button_scale_radius_down);
    panel_east.add(this.button_scale_radius_up);

    panel.add("East", panel_east);

    return panel;
  }

  private Panel setUpChargeSlider() {
    final Panel panel = new Panel();
    panel.setLayout(new BorderLayout(0, 8));
    panel.add("West", new Label("Charge:", Label.RIGHT));

    this.scroll_bar_charge = new Scrollbar(Scrollbar.HORIZONTAL, -100, 10,
      -100, 110);
    this.scroll_bar_charge.addAdjustmentListener(new AdjustmentListener() {
      public void adjustmentValueChanged(AdjustmentEvent e) {
        final int charge = e.getValue();
        getMessageManager().newmessage(Message.MSG_ALTER_CHARGE, charge, 0);
      }
    });

    panel.add("Center", this.scroll_bar_charge);

    this.label_charge = new Label("X", Label.LEFT);

    this.button_scale_charge_up = new Button(GUIStrings.EDIT_CHANGE_DOWN);
    this.button_scale_charge_up.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_EDIT_CHARGE_UP, 0, 0);
      }
    });

    this.button_scale_charge_down = new Button(GUIStrings.EDIT_CHANGE_UP);
    this.button_scale_charge_down.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_EDIT_CHARGE_DOWN, 0, 0);
      }
    });

    final Panel panel_east = new Panel();
    panel_east.add(this.label_charge);
    panel_east.add(this.button_scale_charge_down);
    panel_east.add(this.button_scale_charge_up);

    panel.add("East", panel_east);

    return panel;
  }

  private Panel setUpElasticitySlider() {
    final Panel panel = new Panel();
    panel.setLayout(new BorderLayout(0, 8));
    panel.add("West", new Label("Elasticity:", Label.RIGHT));

    this.scroll_bar_elasticity = new Scrollbar(Scrollbar.HORIZONTAL, 0, 10, 0,
      110);
    this.scroll_bar_elasticity.addAdjustmentListener(new AdjustmentListener() {
      public void adjustmentValueChanged(AdjustmentEvent e) {
        final int elasticity = e.getValue();
        getMessageManager().newmessage(Message.MSG_ALTER_ELASTICITY,
          elasticity, 0);
        reflectElasticity();
      }
    });
    panel.add("Center", this.scroll_bar_elasticity);
    this.label_elasticity = new Label("X", Label.LEFT);

    this.button_scale_elasticity_up = new Button(GUIStrings.EDIT_CHANGE_UP);
    this.button_scale_elasticity_up.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_CHANGE_ELASTICITY_UP, 0, 0);
      }
    });

    this.button_scale_elasticity_down = new Button(GUIStrings.EDIT_CHANGE_DOWN);
    this.button_scale_elasticity_down.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager()
          .newmessage(Message.MSG_CHANGE_ELASTICITY_DOWN, 0, 0);
      }
    });

    final Panel panel_east = new Panel();
    panel_east.add(this.label_elasticity);
    panel_east.add(this.button_scale_elasticity_down);
    panel_east.add(this.button_scale_elasticity_up);

    panel.add("East", panel_east);

    return panel;
  }

  private Panel setUpSliderDamping() {
    final Panel panel = new Panel();
    panel.setLayout(new BorderLayout(0, 8));
    panel.add("West", new Label("Damping:", Label.RIGHT));

    this.scroll_bar_damping = new Scrollbar(Scrollbar.HORIZONTAL, 0, 10, 0, 110);
    this.scroll_bar_damping.addAdjustmentListener(new AdjustmentListener() {
      public void adjustmentValueChanged(AdjustmentEvent e) {
        final int damping = e.getValue();
        getMessageManager().newmessage(Message.MSG_ALTER_STIFFNESS, damping, 0);
        reflectStiffness();
      }
    });
    panel.add("Center", this.scroll_bar_damping);
    this.label_damping = new Label("X", Label.LEFT);

    this.button_scale_damping_up = new Button(GUIStrings.EDIT_CHANGE_UP);
    this.button_scale_damping_up.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_CHANGE_STIFFNESS_UP, 0, 0);
      }
    });

    this.button_scale_damping_down = new Button(GUIStrings.EDIT_CHANGE_DOWN);
    this.button_scale_damping_down.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_CHANGE_STIFFNESS_DOWN, 0, 0);
      }
    });

    final Panel panel_east = new Panel();
    panel_east.add(this.label_damping);
    panel_east.add(this.button_scale_damping_down);
    panel_east.add(this.button_scale_damping_up);

    panel.add("East", panel_east);

    return panel;
  }

  public void updateGUIWhenSelectionIsChanging() {
    reflectRadius();
    reflectElasticity();
    reflectStiffness();
    reflectLength();
    reflectCharge();
  }

  public void reflectElasticity() {
    final int v = new AverageElasticityGetter(FrEnd.node_manager).getAverage();
    this.scroll_bar_elasticity.setValue(v);
    this.label_elasticity.setText("" + v);
  }

  public void reflectStiffness() {
    final int v = new AverageStiffnessGetter(FrEnd.node_manager).getAverage();
    this.scroll_bar_damping.setValue(v);
    this.label_damping.setText("" + v);
  }

  public void reflectLength() {
    final int v = new AverageLengthGetter(FrEnd.node_manager).getAverage();
    this.scroll_bar_length.setValue(v);
    this.label_length.setText("" + v);
  }

  public void reflectRadius() {
    final int v = new AverageRadiusGetter(FrEnd.node_manager).getAverage();
    this.scroll_bar_radius.setValue(v);
    this.label_radius.setText("" + v);
  }

  public void reflectCharge() {
    final int v = new AverageChargeGetter(FrEnd.node_manager).getAverage();
    this.scroll_bar_charge.setValue(v);
    this.label_charge.setText("" + v);
  }

  public void greySelectButtonsDependingOnSelection() {
    final boolean nodes = this.checkbox_select_nodes.getState();
    final boolean links = this.checkbox_select_links.getState();
    final boolean faces = this.checkbox_select_faces.getState();
    final boolean any = links || nodes || faces;

    ComponentAccess.setAccess(this.button_select_all_type, any);
    ComponentAccess.setAccess(this.button_deselect_all_type, any);
  }

  public void updateGUIGreyItemsDependingOnSelection() {
    final boolean nodes = FrEnd.node_manager.isSelection();
    final boolean links = FrEnd.node_manager.link_manager.isSelection();
    final boolean faces = FrEnd.node_manager.face_manager.isSelection();
    final boolean links_and_nodes = links || nodes;
    final boolean any = links || nodes || faces;

    ComponentAccess.setAccess(this.button_edit_make_motionless, nodes);
    ComponentAccess.setAccess(this.checkbox_fixed, nodes);
    ComponentAccess.setAccess(this.button_edit_make_motionless, nodes);

    ComponentAccess.setAccess(this.checkbox_cable, links);
    ComponentAccess.setAccess(this.checkbox_disabled, links);

    ComponentAccess.setAccess(this.scroll_bar_radius, links_and_nodes);
    ComponentAccess.setAccess(this.scroll_bar_length, links);
    ComponentAccess.setAccess(this.scroll_bar_elasticity, links);
    ComponentAccess.setAccess(this.scroll_bar_damping, links);
    ComponentAccess.setAccess(this.scroll_bar_charge, nodes);

    ComponentAccess.setAccess(this.button_scale_radius_up, links_and_nodes);
    ComponentAccess.setAccess(this.button_scale_radius_down, links_and_nodes);
    ComponentAccess.setAccess(this.button_scale_lengths_up, links);
    ComponentAccess.setAccess(this.button_scale_lengths_down, links);
    ComponentAccess.setAccess(this.button_scale_elasticity_up, links);
    ComponentAccess.setAccess(this.button_scale_elasticity_down, links);
    ComponentAccess.setAccess(this.button_scale_damping_up, links);
    ComponentAccess.setAccess(this.button_scale_damping_down, links);
    ComponentAccess.setAccess(this.button_scale_charge_up, nodes);
    ComponentAccess.setAccess(this.button_scale_charge_down, nodes);

    ComponentAccess.setAccess(this.checkbox_hidden, any);
    ComponentAccess.setAccess(this.button_delete, any);
    ComponentAccess.setAccess(this.button_select_clazz, any);
    ComponentAccess.setAccess(this.button_select_type, any);
    ComponentAccess.setAccess(this.cp_gas.button_set, any);

    ComponentAccess.setAccess(FrEnd.panel_edit_misc.button_automatic_node_radius, nodes);
    ComponentAccess.setAccess(FrEnd.panel_edit_misc.button_automatic_link_radius, links);

    ComponentAccess.setAccess(FrEnd.panel_edit_misc.button_select_clazz, any);
    ComponentAccess.setAccess(FrEnd.panel_edit_misc.button_add_faces, nodes);
    ComponentAccess.setAccess(FrEnd.panel_edit_misc.button_connect_nodes_to_nearest_nodes, nodes);
    ComponentAccess.setAccess(FrEnd.panel_edit_misc.button_add_central_hub,
      nodes);
    ComponentAccess.setAccess(FrEnd.panel_edit_misc.button_add_stellations,
      faces);
    ComponentAccess.setAccess(FrEnd.panel_edit_misc.button_edit_remove_links,
      nodes);
    ComponentAccess.setAccess(
      FrEnd.panel_edit_misc.button_edit_remove_polygons, nodes);

    ComponentAccess.setAccess(
      FrEnd.panel_edit_misc.button_edit_remove_link_and_fuse_ends, links);
    ComponentAccess.setAccess(FrEnd.panel_edit_misc.button_split_links, links);
    ComponentAccess.setAccess(
      FrEnd.panel_edit_misc.button_edit_reset_link_lengths, links);
    ComponentAccess.setAccess(
      FrEnd.panel_edit_misc.button_edit_equalise_link_lengths, links);

    this.colour_picker.greyGetAndSetColourButtons();
  }

  public void setElasticityLabel(int e) {
    this.label_elasticity.setText("" + e);
  }

  public void setLengthLabel(int l) {
    this.label_length.setText("" + l);
  }

  public void setRadiusLabel(int r) {
    this.label_radius.setText("" + r);
  }

  public MessageManager getMessageManager() {
    return this.message_manager;
  }

  public Label getLabelScaleBy() {
    return this.label_scale_by;
  }

  public TextField getTextfieldEW() {
    return this.textfield_e_w;
  }

  public TextField getTextfieldER() {
    return this.textfield_e_r;
  }

  public TextField getTextfieldRadiusW() {
    return this.textfield_radius_w;
  }

  public TextField getTextfieldRadiusR() {
    return this.textfield_radius_r;
  }

  public TextField getTextfieldSizeW() {
    return this.textfield_size_w;
  }

  public TextField getTextfieldSizeR() {
    return this.textfield_size_r;
  }

  public Label getLabelElasticity() {
    return this.label_elasticity;
  }

  public Label getLabelLength() {
    return this.label_length;
  }
}
