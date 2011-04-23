// This program has been placed into the public domain by its author.

package com.springie.gui.panels.controls;

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
import com.springie.elements.links.Link;
import com.springie.elements.nodes.Node;
import com.springie.gui.GUIStrings;
import com.springie.messages.Message;
import com.springie.messages.MessageManager;
import com.springie.utilities.general.Forget;
import com.springie.world.World;


public class PanelControlsUniverse {
  public Panel panel = FrEnd.setUpPanelForFrame2();

  MessageManager message_manager;

  public Checkbox checkbox_3D;

  public Checkbox checkbox_gravity_switch;

  public Checkbox checkbox_charge_switch;

  public Checkbox checkbox_node_growth;

  public Checkbox checkbox_continuously_centre;

  public Checkbox checkbox_collision_check;

  public Checkbox checkbox_collide_self_only;

  Label label_gravity;
  
  Label label_charge;
  
  Label label_viscocity;

  Label label_number;

  Label label_bias;

  Label label_noc;

  Label label_impact;

  Label label_limit;

  Label label_friction;

  Scrollbar scroll_bar_noc;

  Scrollbar scroll_bar_n;

  Scrollbar scroll_bar_gravity;
  
  Scrollbar scroll_bar_stiffness;

  Scrollbar scroll_bar_elasticity;

  Scrollbar scroll_bar_viscocity;

  Scrollbar scroll_bar_bias;

  Scrollbar scroll_bar_limit;

  Scrollbar scroll_bar_impact;

  Scrollbar scroll_bar_friction;

  private Checkbox checkbox_links_disabled;

  public PanelControlsUniverse(MessageManager message_manager) {
    this.message_manager = message_manager;
    makeEditMiscPanel();
  }

  void makeEditMiscPanel() {
    final Panel panel3D = new Panel();
    this.checkbox_3D = new Checkbox("3D");
    this.checkbox_3D.setState(FrEnd.three_d);

    this.checkbox_3D.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        FrEnd.three_d = getCheckbox3D().getState();

        BinGrid.repaint_all_objects = true;
      }
    });

    panel3D.add(this.checkbox_3D);

    final Panel panel_node_growth = new Panel();
    this.checkbox_node_growth = new Checkbox(GUIStrings.NODE_GROWTH);
    this.checkbox_node_growth.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_NODE_GROWTH, 0, 0);
      }
    });
    panel_node_growth.add(this.checkbox_node_growth);

    final Panel panel_gravity = new Panel();
    panel_gravity.setLayout(new BorderLayout(0, 8));
    panel_gravity.add("West", new Label("Gravity:", Label.RIGHT));

    this.scroll_bar_gravity = new Scrollbar(Scrollbar.HORIZONTAL, 10, 80, 0,
      580);
    this.scroll_bar_gravity.addAdjustmentListener(new AdjustmentListener() {
      public void adjustmentValueChanged(AdjustmentEvent e) {
        final int temp = e.getValue();
        World.gravity_strength = temp;
        getLabelGravity().setText("" + temp);
      }
    });

    panel_gravity.add("Center", this.scroll_bar_gravity);

    this.label_gravity = new Label("" + World.gravity_strength, Label.LEFT);
    panel_gravity.add("East", this.label_gravity);

    final Panel panel_gravity_switch = new Panel();
    this.checkbox_gravity_switch = new Checkbox(GUIStrings.GRAVITY);
    this.checkbox_gravity_switch.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        World.gravity_active = ((Checkbox)e.getSource()).getState();
      }
    });
    panel_gravity_switch.add(this.checkbox_gravity_switch);

    final Panel panel_charge_switch = new Panel();
    this.checkbox_charge_switch = new Checkbox(GUIStrings.CHARGE, true);
    this.checkbox_charge_switch.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        FrEnd.node_manager.electrostatic.charge_active = ((Checkbox)e.getSource()).getState();
      }
    });
    panel_charge_switch.add(this.checkbox_charge_switch);

    final Panel panel_continuously_centre = new Panel();
    this.checkbox_continuously_centre = new Checkbox(
      GUIStrings.CONTINUOUSLY_CENTRE);
    this.checkbox_continuously_centre.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_CONTINUOUSLY_CENTRE, 0, 0);
      }
    });
    panel_continuously_centre.add(this.checkbox_continuously_centre);

    // elasticity..
    final Panel panel_viscocity = new Panel();
    panel_viscocity.setLayout(new BorderLayout(0, 8));
    panel_viscocity.add("West", new Label("Viscosity:", Label.RIGHT));

    this.scroll_bar_viscocity = new Scrollbar(Scrollbar.HORIZONTAL, 0, 10, 0,
      110);
    this.scroll_bar_viscocity.addAdjustmentListener(new AdjustmentListener() {
      public void adjustmentValueChanged(AdjustmentEvent e) {
        Node.viscocity = e.getValue();
        reflectViscocity();
      }
    });

    panel_viscocity.add("Center", this.scroll_bar_viscocity);

    this.label_viscocity = new Label("XXX", Label.LEFT);
    panel_viscocity.add("East", this.label_viscocity);

    final Panel panel_collision_check = new Panel();
    this.checkbox_collision_check = new Checkbox(GUIStrings.collision_check);
    this.checkbox_collision_check.setState(true);
    this.checkbox_collision_check.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        FrEnd.check_collisions = !FrEnd.check_collisions;
      }
    });
    panel_collision_check.add(this.checkbox_collision_check);

    final Panel panel_links_disabled = new Panel();
    this.checkbox_links_disabled = new Checkbox(GUIStrings.LINKS_DISABLED);
    this.checkbox_links_disabled.setState(true);
    this.checkbox_links_disabled.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        FrEnd.links_disabled = !FrEnd.links_disabled;
      }
    });
    panel_links_disabled.add(this.checkbox_links_disabled);

    
    
    final Panel panel_collide_self_only = new Panel();
    this.checkbox_collide_self_only = new Checkbox(GUIStrings.CSO);
    this.checkbox_collide_self_only.setState(FrEnd.collide_self_only);
    this.checkbox_collide_self_only.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_LINKLENGTH, Link.SHORT, 0);
      }
    });
    panel_collide_self_only.add(this.checkbox_collide_self_only);

    final Panel panel_bias = new Panel();
    panel_bias.setLayout(new BorderLayout(0, 8));
    panel_bias.add("West", new Label("Bias:", Label.RIGHT));

    this.scroll_bar_bias = new Scrollbar(Scrollbar.HORIZONTAL, 0, 90, -180, 270);
    this.scroll_bar_bias.addAdjustmentListener(new AdjustmentListener() {
      public void adjustmentValueChanged(AdjustmentEvent e) {
        final int temp = e.getValue();
        //Node.direction_bias = (byte) ((temp * 16) / 180);
        getLabelBias().setText("" + temp);
      }
    });

    panel_bias.add("Center", this.scroll_bar_bias);

    this.label_bias = new Label("  0", Label.LEFT);
    panel_bias.add("East", this.label_bias);

    final Panel panel_speed = new Panel();
    panel_speed.setLayout(new BorderLayout(0, 8));
    panel_speed.add("West", new Label("Speed:", Label.RIGHT));

    this.scroll_bar_limit = new Scrollbar(Scrollbar.HORIZONTAL,
      Node.max_speed >> 5, 50, 0, 450);
    this.scroll_bar_limit.addAdjustmentListener(new AdjustmentListener() {
      public void adjustmentValueChanged(AdjustmentEvent e) {
        Node.max_speed = e.getValue() << 5;
        reflectMaxSpeed();
      }
    });
    panel_speed.add("Center", this.scroll_bar_limit);

    this.label_limit = new Label("" + (Node.max_speed >> 5), Label.LEFT);
    panel_speed.add("East", this.label_limit);

    // rest energy
    final Panel panel_excite = new Panel();
    panel_excite.setLayout(new BorderLayout(0, 8));
    panel_excite.add("West", new Label("Excite:", Label.RIGHT));

    this.scroll_bar_impact = new Scrollbar(Scrollbar.HORIZONTAL,
      World.minimum_magnitude >> 6, 10, 0, 109);
    this.scroll_bar_impact.addAdjustmentListener(new AdjustmentListener() {
      public void adjustmentValueChanged(AdjustmentEvent e) {
        final int temp = e.getValue();
        World.minimum_magnitude = temp << 6;
        getLabelImpact().setText("" + temp);
      }
    });

    panel_excite.add("Center", this.scroll_bar_impact);

    this.label_impact = new Label("" + (World.minimum_magnitude >> 6),
      Label.LEFT);
    panel_excite.add("East", this.label_impact);

    final Panel panel_friction = new Panel();
    panel_friction.setLayout(new BorderLayout(0, 8));
    panel_friction.add("West", new Label("Friction:", Label.RIGHT));

    this.scroll_bar_friction = new Scrollbar(Scrollbar.HORIZONTAL, 0, 100, 0,
      356);
    this.scroll_bar_friction.addAdjustmentListener(new AdjustmentListener() {
      public void adjustmentValueChanged(AdjustmentEvent e) {
        Node.friction_strength = e.getValue();
        reflectFriction();
      }
    });
    panel_friction.add("Center", this.scroll_bar_friction);

    this.label_friction = new Label("12", Label.LEFT);
    panel_friction.add("East", this.label_friction);
    reflectFriction();

    //universe...
    this.panel.add(panel3D);
    this.panel.add(panel_viscocity);
    this.panel.add(panel_gravity);
    this.panel.add(panel_gravity_switch);


    this.panel.add(panel_continuously_centre);
    this.panel.add(panel_node_growth);

    this.panel.add(panel_collision_check);
    this.panel.add(panel_links_disabled);
    this.panel.add(panel_charge_switch);

    if (FrEnd.development_version) {
      this.panel.add(panel_bias); // bias...

      this.panel.add(panel_speed);
      this.panel.add(panel_friction);

      this.panel.add(panel_excite);
    }
  }

  public void reflectMaxSpeed() {
    this.scroll_bar_limit.setValue(Node.max_speed >> 5);

    this.label_limit.setText("" + (Node.max_speed >> 5));
  }

  public void reflectViscocity() {
    this.scroll_bar_viscocity.setValue(Node.viscocity);

    this.label_viscocity.setText("" + Node.viscocity);
  }

  public void reflectGravity() {
    this.scroll_bar_gravity.setValue(World.gravity_strength);

    this.label_gravity.setText("" + World.gravity_strength);
    this.checkbox_gravity_switch.setState(World.gravity_active);
  }

  public void reflect3D() {
    this.checkbox_3D.setState(FrEnd.three_d);
  }

  public void reflectImpact() {
    this.scroll_bar_impact.setValue(World.minimum_magnitude >> 6);

    this.label_impact.setText("" + (World.minimum_magnitude >> 6));
  }

  public void reflectFriction() {
    this.scroll_bar_friction.setValue(Node.friction_strength);

    this.label_friction.setText("" + (Node.friction_strength));
  }

  public Label getLabelBias() {
    return this.label_bias;
  }

  public Label getLabelFriction() {
    return this.label_friction;
  }

  public Label getLabelGravity() {
    return this.label_gravity;
  }

  public Label getLabelImpact() {
    return this.label_impact;
  }

  public Label getLabelLimit() {
    return this.label_limit;
  }

  public Label getLabelNOC() {
    return this.label_noc;
  }

  public Label getLabelNumber() {
    return this.label_number;
  }

  public Label getLabelCharge() {
    return this.label_charge;
  }

  public Label getLabelViscocity() {
    return this.label_viscocity;
  }

  public MessageManager getMessageManager() {
    return this.message_manager;
  }

  public Checkbox getCheckbox3D() {
    return this.checkbox_3D;
  }

  public Checkbox getCheckboxCollideSelfOnly() {
    return this.checkbox_collide_self_only;
  }

  public Checkbox getCheckboxCollisionCheck() {
    return this.checkbox_collision_check;
  }

  public Checkbox getCheckboxContinuouslyCentre() {
    return this.checkbox_continuously_centre;
  }

  public Checkbox getCheckboxGravitySwitch() {
    return this.checkbox_gravity_switch;
  }

  public Checkbox getCheckboxNodeGrowth() {
    return this.checkbox_node_growth;
  }
}