// This program has been placed into the public domain by its author.

package com.springie.gui.panels.controls;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import com.springie.FrEnd;
import com.springie.constants.ToolTypes;
import com.springie.gui.GUIStrings;
import com.springie.gui.components.TTChoice;
import com.springie.messages.Message;
import com.springie.messages.MessageManager;
import com.springie.utilities.general.Forget;

public class PanelControlsMisc {
  public Panel panel = FrEnd.setUpPanelForFrame2();

  MessageManager message_manager;

  public TextField textfield_add_polygons;

  public TextField textfield_select_faces_with_n_sides;

  public TextField textfield_add_stellations;

  public TextField textfield_add_stellation_vertex_count;

  public TextField textfield_add_inner_hex_nodes;

  public TextField textfield_add_nodes_inner_eden;

  public TextField textfield_add_triaxial_nodes;

  public TextField textfield_split_links;

  public TextField textfield_automatic_node_radius;

  public TextField textfield_automatic_link_radius;

  public TextField textfield_link_nearest_n_nodes;

  public Button button_edit_remove_link_and_fuse_ends;

  public Button button_add_faces;

  public Button button_edit_remove_links;

  public Button button_edit_remove_polygons;

  public Button button_add_stellations;

  public Button button_split_links;

  public Button button_edit_equalise_link_lengths;

  public Button button_edit_reset_link_lengths;
  
  public Button button_automatic_node_radius;
  
  public Button button_automatic_link_radius;
  
  public Button button_hex_triaxial;

  public Checkbox checkbox_split_links_remove_old;

  Button button_select_clazz;

  private Button button_select_nodes;

  private Button button_select_faces;

  private Button button_select_links;

  private Button button_prismatic_projection;

  public Button button_add_central_hub;

  public TextField textfield_prismatic_projection;

  public Button button_connect_nodes_to_nearest_nodes;

  public PanelControlsMisc(MessageManager message_manager) {
    this.message_manager = message_manager;
    makeEditMiscPanel();
  }

  void makeEditMiscPanel() {
//    final Button button_hex_1 = new Button(GUIStrings.DOME_1);
//    button_hex_1.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        Forget.about(e);
//        getMessageManager().newmessage(
//          Message.MSG_DOME_CONNECT_NODES_IN_FIRST_LAYER, 0, 0);
//      }
//    });
//    final Panel panel_polyform = new Panel();
//    panel_polyform.add(button_hex_1);

//    final Button button_add_links_to_nearest = new Button(
//      GUIStrings.EDIT_ADD_LINKS_TO_NEAREST);
//    button_add_links_to_nearest.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        Forget.about(e);
//        getMessageManager().newmessage(Message.MSG_ADD_LINKS_TO_NEAREST, 0, 0);
//      }
//    });
//
//    final Panel panel_add_links_to_nearest = new Panel();
//    panel_add_links_to_nearest.add(button_add_links_to_nearest);

    final Button button_add_nodes_inner_eden = new Button(
      GUIStrings.EDIT_ADD_NODES_INNER_EDEN);
    button_add_nodes_inner_eden.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_ADD_NODES_INNER_EDEN, 0, 0);
      }
    });
    final Panel panel_add_nodes_inner_eden = new Panel();
    panel_add_nodes_inner_eden.add(button_add_nodes_inner_eden);
    this.textfield_add_nodes_inner_eden = new TextField("-0.18");
    panel_add_nodes_inner_eden.add(this.textfield_add_nodes_inner_eden);

    this.button_split_links = new Button(GUIStrings.EDIT_SPLIT_LINKS);
    this.button_split_links.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_EDIT_SPLIT_LINKS, 0, 0);
      }
    });
    final Panel panel_split_links = new Panel();
    panel_split_links.add(this.button_split_links);
    this.textfield_split_links = new TextField("2");
    panel_split_links.add(this.textfield_split_links);
    this.checkbox_split_links_remove_old = new Checkbox("kill");
    panel_split_links.add(this.checkbox_split_links_remove_old);

    this.button_add_stellations = new Button(GUIStrings.EDIT_ADD_STELLATIONS);
    this.button_add_stellations.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_ADD_STELLATIONS, 0, 0);
      }
    });

    final Panel panel_add_stellations = new Panel();
    panel_add_stellations.add(this.button_add_stellations);
    this.textfield_add_stellations = new TextField("0.2");
    panel_add_stellations.add(this.textfield_add_stellations);

    this.button_prismatic_projection = new Button(GUIStrings.EDIT_PRISMATIC_PROGECTION);
    this.button_prismatic_projection.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_PRISMATIC_PROJECTION, 0, 0);
      }
    });

    final Panel panel_prismatic_projection = new Panel();
    panel_prismatic_projection.add(this.button_prismatic_projection);
    this.textfield_prismatic_projection = new TextField("-0.2");
    panel_prismatic_projection.add(this.textfield_prismatic_projection);

    //    final Button button_add_nodes_inner_hex = new Button(
    //      GUIStrings.EDIT_ADD_NODES_INNER_HEX);
    //    button_add_nodes_inner_hex.addActionListener(new ActionListener() {
    //      public void actionPerformed(ActionEvent e) {
    //        Forget.about(e);
    //        getMessageManager().newmessage(Message.MSG_ADD_NODES_INNER_HEX, 0, 0);
    //      }
    //    });
    //    final Panel panel_add_nodes_inner_hex = new Panel();
    //    panel_add_nodes_inner_hex.add(button_add_nodes_inner_hex);
    //    this.textfield_add_inner_hex_nodes = new TextField("-0.28");
    //    panel_add_nodes_inner_hex.add(this.textfield_add_inner_hex_nodes);

    this.button_hex_triaxial = new Button(
      GUIStrings.EDIT_ADD_NODES_TRIAXIAL);
    this.button_hex_triaxial.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_ADD_NODES_TRIAXIAL, 0, 0);
      }
    });
    final Panel panel_add_nodes_triaxial = new Panel();
    panel_add_nodes_triaxial.add(this.button_hex_triaxial);
    this.textfield_add_triaxial_nodes = new TextField("-0.17");
    panel_add_nodes_triaxial.add(this.textfield_add_triaxial_nodes);

    //    final Button button_add_links_inner_hex = new Button(
    //      GUIStrings.EDIT_ADD_LINKS_INNER_HEX);
    //    button_add_links_inner_hex.addActionListener(new ActionListener() {
    //      public void actionPerformed(ActionEvent e) {
    //        Forget.about(e);
    //        getMessageManager().newmessage(Message.MSG_ADD_LINKS_INNER_HEX, 0, 0);
    //      }
    //    });
    //    final Panel panel_add_links_inner_hex = new Panel();
    //    panel_add_links_inner_hex.add(button_add_links_inner_hex);

    this.button_connect_nodes_to_nearest_nodes = new Button(
      GUIStrings.EDIT_ADD_LINKS_TO_NEAREST);
    this.button_connect_nodes_to_nearest_nodes.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_CONNECT_NODES_TO_NEAREST_NODES, 0, 0);
      }
    });
    final Panel panel_add_links_outer_hex = new Panel();
    panel_add_links_outer_hex.add(this.button_connect_nodes_to_nearest_nodes);
    this.textfield_link_nearest_n_nodes = new TextField("3");
    panel_add_links_outer_hex.add(this.textfield_link_nearest_n_nodes);

    //    final Button button_add_links_triaxial = new Button(
    //      GUIStrings.EDIT_ADD_LINKS_TRIAXIAL);
    //    button_add_links_triaxial.addActionListener(new ActionListener() {
    //      public void actionPerformed(ActionEvent e) {
    //        Forget.about(e);
    //        getMessageManager().newmessage(
    //          Message.MSG_DOME_CONNECT_NODES_IN_TRIAXIAL_LAYER, 0, 0);
    //      }
    //    });
    //    final Panel panel_add_links_triaxial = new Panel();
    //    panel_add_links_triaxial.add(button_add_links_triaxial);

    this.textfield_add_polygons = new TextField("3");

    this.button_add_faces = new Button(GUIStrings.EDIT_ADD_POLYGONS);
    this.button_add_faces.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_EDIT_ADD_POLYGONS, 0, 0);
      }
    });
    final Panel panel_add_polygons = new Panel();
    panel_add_polygons.add(this.button_add_faces);
    panel_add_polygons.add(this.textfield_add_polygons);
    
    //...

    this.button_edit_remove_link_and_fuse_ends = new Button(
      GUIStrings.EDIT_REMOVE_LINK_AND_FUSE_ENDS);
    this.button_edit_remove_link_and_fuse_ends
      .addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          Forget.about(e);
          getMessageManager().newmessage(
            Message.MSG_EDIT_REMOVE_LINK_AND_FUSE_ENDS, 0, 0);
        }
      });
    final Panel panel_edit_remove_link_and_fuse_ends = new Panel();
    panel_edit_remove_link_and_fuse_ends
      .add(this.button_edit_remove_link_and_fuse_ends);

    final Panel panel_tool = new Panel();
    panel_tool.add(new Label("Use", Label.RIGHT));

    FrEnd.choose_tool = new TTChoice(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e == null) {
          return;
        }

        final String stateChangedString = (String) (e.getItem());

        if (FrEnd.choose_tool.str_to_num(stateChangedString) != -99) {
          FrEnd.weapon_type = FrEnd.choose_tool.str_to_num(stateChangedString);
        }
      }
    });

    FrEnd.choose_tool.add("a pencil", ToolTypes._PENCIL);
    FrEnd.choose_tool.add("a brush", ToolTypes._BRUSH);
    FrEnd.choose_tool.add("a toothbrush", ToolTypes._MACHINE);
    FrEnd.choose_tool.add("an aerosol", ToolTypes._SPRAY);
    FrEnd.choose_tool.add("potato printing", ToolTypes._POTATO);
    FrEnd.choose_tool.choice.select(FrEnd.choose_tool
      .num_to_str(FrEnd.weapon_type));
    panel_tool.add(FrEnd.choose_tool.choice);

    this.panel.add(getSelectAllPanel());
    this.panel.add(getEqualiseLinkLengthsPanel());
    this.panel.add(getResetLinkLengthsPanel());

    this.panel.add(getRemoveLinksPanel());
    this.panel.add(getRemoveFacesPanel());

    this.panel.add(panel_edit_remove_link_and_fuse_ends);

    this.panel.add(panel_add_stellations);
    this.panel.add(panel_add_polygons);

    this.panel.add(getAddCentralHubPanel());

    //this.panel.add(panel_polyform);
    this.panel.add(panel_prismatic_projection);
    
    //this.panel.add(panel_add_links_to_nearest);
    this.panel.add(panel_split_links);

    this.panel.add(panel_add_nodes_triaxial);
    //this.panel.add(button_hex_triaxial);
    //panel_add_nodes_inner_eden);

    this.panel.add(panel_add_links_outer_hex);

    this.panel.add(getAutomaticLinkRadiusPanel());
    this.panel.add(getAutomaticNodeRadiusPanel());

    this.panel.add(getSelectAllFacesWithNSidesPanel());
    
    this.panel.add(getClearPanel());

    this.panel.add(panel_tool); // low interest
  }

  
  
  private Panel getResetLinkLengthsPanel() {
    this.button_edit_reset_link_lengths = new Button(
      GUIStrings.DOME_LINKS_RESET_LENGTHS);
    this.button_edit_reset_link_lengths.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_DOME_LINKS_RESET_LENGTHS, 0,
          0);
      }
    });
    final Panel panel_edit_reset_link_lengths = new Panel();
    panel_edit_reset_link_lengths.add(this.button_edit_reset_link_lengths);
    return panel_edit_reset_link_lengths;
  }

  private Panel getEqualiseLinkLengthsPanel() {
    this.button_edit_equalise_link_lengths = new Button(
      GUIStrings.DOME_LINKS_EQUALISE_LINK_LENGTHS);
    this.button_edit_equalise_link_lengths
      .addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          Forget.about(e);
          getMessageManager().newmessage(
            Message.MSG_DOME_LINKS_EQUALISE_LINK_LENGTHS, 0, 0);
        }
      });

    final Panel panel_edit_equalise_link_lengths = new Panel();
    panel_edit_equalise_link_lengths
      .add(this.button_edit_equalise_link_lengths);
    return panel_edit_equalise_link_lengths;
  }

  private Panel getAutomaticLinkRadiusPanel() {
    this.button_automatic_link_radius = new Button(
      GUIStrings.EDIT_AUTOMATIC_LINK_RADIUS);
    this.button_automatic_link_radius.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_EDIT_AUTOMATIC_LINK_RADIUS,
          0, 0);
      }
    });

    final Panel panel_automatic_link_radius = new Panel();
    panel_automatic_link_radius.add(this.button_automatic_link_radius);
    this.textfield_automatic_link_radius = new TextField("0.05");
    panel_automatic_link_radius.add(this.textfield_automatic_link_radius);

    return panel_automatic_link_radius;
  }

  private Panel getAutomaticNodeRadiusPanel() {
    this.button_automatic_node_radius = new Button(
      GUIStrings.EDIT_AUTOMATIC_NODE_RADIUS);
    this.button_automatic_node_radius.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_EDIT_AUTOMATIC_NODE_RADIUS,
          0, 0);
      }
    });

    final Panel panel_automatic_node_radius = new Panel();
    panel_automatic_node_radius.add(this.button_automatic_node_radius);
    this.textfield_automatic_node_radius = new TextField("1.5");
    panel_automatic_node_radius.add(this.textfield_automatic_node_radius);

    return panel_automatic_node_radius;
  }

  private Panel getAddCentralHubPanel() {
    this.button_add_central_hub = new Button(
      GUIStrings.EDIT_ADD_CENTRAL_HUB);
    this.button_add_central_hub.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_EDIT_ADD_CENTRAL_HUB, 0, 0);
      }
    });
    final Panel panel = new Panel();
    panel.add(this.button_add_central_hub);
    return panel;
  }

  private Panel getRemoveFacesPanel() {
    this.button_edit_remove_polygons = new Button(
      GUIStrings.DOME_REMOVE_POLYGONS);
    this.button_edit_remove_polygons.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_EDIT_REMOVE_POLYGONS, 0, 0);
      }
    });
    final Panel panel = new Panel();
    panel.add(this.button_edit_remove_polygons);
    return panel;
  }

  private Panel getRemoveLinksPanel() {
    this.button_edit_remove_links = new Button(GUIStrings.DOME_REMOVE_LINKS);
    this.button_edit_remove_links.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_EDIT_REMOVE_LINKS, 0, 0);
      }
    });
    final Panel panel = new Panel();
    panel.add(this.button_edit_remove_links);
    return panel;
  }

  private Panel getClearPanel() {
    final Button button_clear = new Button(GUIStrings.BUTTON_CLEAR);
    button_clear.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_CLEAR, 0, 0);
      }
    });
    final Panel panel = new Panel();
    panel.add(button_clear);
    return panel;
  }

  private Panel getSelectAllPanel() {
    final Label label_select_select_label = new Label("Select:", Label.RIGHT);
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

    this.button_select_nodes = new Button("nodes");
    this.button_select_nodes.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_SELECT_ALL_NODES, 0, 0);
      }
    });
    panel_select_all.add(this.button_select_nodes);

    this.button_select_links = new Button("links");
    this.button_select_links.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_SELECT_ALL_LINKS, 0, 0);
      }
    });
    panel_select_all.add(this.button_select_links);

    this.button_select_faces = new Button("faces");
    this.button_select_faces.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_SELECT_ALL_FACES, 0, 0);
      }
    });
    panel_select_all.add(this.button_select_faces);

    return panel_select_all;
  }

  private Panel getSelectAllFacesWithNSidesPanel() {
    final Panel panel = new Panel();

    this.button_select_faces = new Button("Select faces with");
    this.button_select_faces.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(
          Message.MSG_SELECT_ALL_FACES_WITH_N_SIDES, 0, 0);
      }
    });
    panel.add(this.button_select_faces);

    this.textfield_select_faces_with_n_sides = new TextField("5");
    panel.add(this.textfield_select_faces_with_n_sides);

    final Label label_select = new Label("sides", Label.LEFT);
    panel.add(label_select);

    return panel;
  }

  public MessageManager getMessageManager() {
    return this.message_manager;
  }
}