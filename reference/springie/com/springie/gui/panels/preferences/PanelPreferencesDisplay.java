// This program has been placed into the public domain by its author.

package com.springie.gui.panels.preferences;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Scrollbar;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import com.springie.FrEnd;
import com.springie.collisions.BinGrid;
import com.springie.constants.Quality;
import com.springie.elements.DeepObjectColourCalculator;
import com.springie.elements.faces.Face;
import com.springie.elements.faces.FaceRenderTypes;
import com.springie.elements.links.Link;
import com.springie.elements.links.LinkRenderType;
import com.springie.elements.nodes.Node;
import com.springie.gui.GUIStrings;
import com.springie.gui.components.TTChoice;
import com.springie.messages.Message;
import com.springie.messages.MessageManager;
import com.springie.utilities.general.Forget;

public class PanelPreferencesDisplay {
  public Panel panel = FrEnd.setUpPanelForFrame2();

  MessageManager message_manager;

  public Checkbox checkbox_db;

  public Checkbox checkbox_longlinks;

  public Checkbox checkbox_shortlinks;

  public Checkbox checkbox_explosions;
  
  public Checkbox checkbox_redraw_deepest_first;
  

  public Checkbox checkbox_render_hidden_nodes;

  public Checkbox checkbox_render_hidden_links;

  public Checkbox checkbox_render_hidden_polygons;

  public Checkbox checkbox_render_nodes;

  public Checkbox checkbox_render_links;

  public Checkbox checkbox_render_polygons;

  private Checkbox checkbox_render_charges;

  TTChoice choose_polygon_render_type;

  private Scrollbar scroll_bar_face_render_number;

  private Scrollbar scroll_bar_link_render_cables;

  private Scrollbar scroll_bar_render_nodes;

  private Scrollbar scroll_bar_fog;

  private Label label_face_render_number;

  private Label label_cable_render_number;

  private Label label_strut_render_number;

  private Label label_node_render_number;

  private Label label_fog;

  public PanelPreferencesDisplay(MessageManager message_manager) {
    this.message_manager = message_manager;
    makeEditMiscPanel();
  }

  void makeEditMiscPanel() {
    final Panel panel_render_normal = new Panel();
    this.checkbox_render_nodes = new Checkbox("Nodes",
      FrEnd.render_nodes);
    this.checkbox_render_nodes.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        FrEnd.render_nodes = ((Checkbox) e.getSource()).getState();
        BinGrid.repaint_all_objects = true;
      }
    });
    panel_render_normal.add(new Label("Show:"));
    panel_render_normal.add(this.checkbox_render_nodes);

    this.checkbox_render_charges = new Checkbox("Charges",
      FrEnd.render_charges);
    this.checkbox_render_charges.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        FrEnd.render_charges = ((Checkbox) e.getSource()).getState();
        BinGrid.repaint_all_objects = true;
      }
    });
    panel_render_normal.add(this.checkbox_render_charges);

    this.checkbox_render_polygons = new Checkbox("Faces",
      FrEnd.render_polygons);
    this.checkbox_render_polygons.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        FrEnd.render_polygons = getCheckboxRenderPolygons().getState();
        BinGrid.repaint_all_objects = true;
      }
    });
    panel_render_normal.add(this.checkbox_render_polygons);

    this.checkbox_render_links = new Checkbox("Links",
      FrEnd.render_links);
    this.checkbox_render_links.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        FrEnd.render_links = ((Checkbox) e.getSource()).getState();
        BinGrid.repaint_all_objects = true;
      }
    });
    panel_render_normal.add(this.checkbox_render_links);

    final Panel panel_render_hidden = new Panel();
    this.checkbox_render_hidden_nodes = new Checkbox("Nodes",
      FrEnd.render_hidden_nodes);
    this.checkbox_render_hidden_nodes.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        FrEnd.render_hidden_nodes = getCheckboxRenderHiddenNodes().getState();
        BinGrid.repaint_all_objects = true;
      }
    });
    panel_render_hidden.add(new Label("Show hidden:"));
    panel_render_hidden.add(this.checkbox_render_hidden_nodes);

    this.checkbox_render_hidden_links = new Checkbox("Links",
      FrEnd.render_hidden_links);
    this.checkbox_render_hidden_links.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        FrEnd.render_hidden_links = getCheckboxRenderHiddenLinks().getState();
        BinGrid.repaint_all_objects = true;
      }
    });
    panel_render_hidden.add(this.checkbox_render_hidden_links);

    //final Panel panel_render_hidden_polygons = new Panel();
    this.checkbox_render_hidden_polygons = new Checkbox(
      "Faces", FrEnd.render_hidden_faces);
    this.checkbox_render_hidden_polygons.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        FrEnd.render_hidden_faces = getCheckboxRenderHiddenPolygons()
          .getState();
        BinGrid.repaint_all_objects = true;
      }
    });
    panel_render_hidden.add(this.checkbox_render_hidden_polygons);

    final Panel panel_node_render_type = new Panel();
    //panel_quality.setBackground(colour_grey1);
    panel_node_render_type.add(new Label("Nodes are:", Label.RIGHT));

    FrEnd.choose_quality = new TTChoice(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        final String scs = (String) e.getItem();
        FrEnd.quality = FrEnd.choose_quality.str_to_num(scs);
        BinGrid.repaint_all_objects = true;
      }
    });
    final TTChoice choose_quality = FrEnd.choose_quality;

    choose_quality.add("thick outline", Quality.THICK_OUTLINE);
    //choose_quality.add("thin outline", Quality.THIN_OUTLINE);
    choose_quality.add("solid", Quality.SOLID);
    choose_quality.add("multiple", Quality.MULTIPLE);
    //choose_quality.add("< medium", Quality._QUALITY_2A);
    choose_quality.add("poor", Quality.SOLID);
    choose_quality.add("< poor", Quality._QUALITY_1A);
    choose_quality.add("terrible", Quality._QUALITY_3);
    choose_quality.add("< terrible", Quality._QUALITY_3A);
    choose_quality.add("abysmal", Quality._QUALITY_4);
    choose_quality.add("< abysmal", Quality._QUALITY_4A);
    choose_quality.add("dots only", Quality._QUALITY_5);
    choose_quality.add("gone", Quality._QUALITY_6);
    choose_quality.choice.select(choose_quality.num_to_str(FrEnd.quality));
    panel_node_render_type.add(choose_quality.choice);

    final TTChoice choose_display_struts = new TTChoice(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        final String scs = (String) e.getItem();
        Link.link_display_struts_type = FrEnd.choose_display_struts
          .str_to_num(scs);
        BinGrid.repaint_all_objects = true;
      }
    });

    FrEnd.choose_display_struts = choose_display_struts;

    final Panel panel_link_display_struts = new Panel();
    panel_link_display_struts.add(new Label("Struts are", Label.RIGHT));
    addLinkDisplayTypes(choose_display_struts);
    panel_link_display_struts.add(choose_display_struts.choice);

    final TTChoice choose_display_cables = new TTChoice(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        final String scs = (String) e.getItem();
        Link.link_display_cables_type = FrEnd.choose_display_cables
          .str_to_num(scs);
        BinGrid.repaint_all_objects = true;
      }
    });

    FrEnd.choose_display_cables = choose_display_cables;

    final Panel panel_link_display_cable = new Panel();
    panel_link_display_cable.add(new Label("Cables are", Label.RIGHT));
    addLinkDisplayTypes(choose_display_cables);
    panel_link_display_cable.add(choose_display_cables.choice);

    final Panel panel_face_render_type = chooseFaceRenderType();
    final Panel panel_face_render_number = getFaceRenderNumber();
    final Panel panel_strut_render_number = getStrutRenderNumber();
    final Panel panel_cable_render_number = getCableRenderNumber();
    final Panel panel_node_render_number = getNodeRenderNumber();
    final Panel panel_fog = getFogPanel();

    final CheckboxGroup checkbox_linklength = new CheckboxGroup();

    final Panel panel_link_length = new Panel();
    panel_link_length.add(new Label("Struts and cables are:", Label.RIGHT));
    this.checkbox_shortlinks = new Checkbox("short", checkbox_linklength, true);
    this.checkbox_shortlinks.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_LINKLENGTH, Link.SHORT, 0);
      }
    });

    this.checkbox_longlinks = new Checkbox("long", checkbox_linklength, false);
    this.checkbox_longlinks.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_LINKLENGTH, Link.LONG, 0);
      }
    });

    panel_link_length.add(this.checkbox_shortlinks);
    panel_link_length.add(this.checkbox_longlinks);

    final Panel panel_visible_explosions = new Panel();
    this.checkbox_explosions = new Checkbox(GUIStrings.EXPLOSIONS);
    this.checkbox_explosions.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        FrEnd.explosions = ((Checkbox)e.getSource()).getState();
      }
    });
    this.checkbox_explosions.setState(FrEnd.explosions);
    panel_visible_explosions.add(this.checkbox_explosions);

    final Panel panel_redraw_deepest_first = new Panel();
    this.checkbox_redraw_deepest_first = new Checkbox("Render deepest objects first");
    this.checkbox_redraw_deepest_first.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        FrEnd.redraw_deepest_first = ((Checkbox)e.getSource()).getState();
      }
    });
    this.checkbox_redraw_deepest_first.setState(FrEnd.redraw_deepest_first);
    panel_redraw_deepest_first.add(this.checkbox_redraw_deepest_first);
    
    final Panel panel_double_buffering = new Panel();
    this.checkbox_db = new Checkbox(GUIStrings.DB); // TODO: default?

    this.checkbox_db.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        getMessageManager().newmessage(Message.MSG_DB, 0, 0);
      }
    });

    panel_double_buffering.add(this.checkbox_db);

    final Panel panel_xor = new Panel();
    final Checkbox checkbox_xor = new Checkbox(GUIStrings.XOR, FrEnd.xor);
    checkbox_xor.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        Forget.about(e);
        FrEnd.xor = ((Checkbox) e.getSource()).getState();
        BinGrid.repaint_all_objects = true;
      }
    });
    panel_xor.add(checkbox_xor);

    this.panel.add(panel_render_normal);

    this.panel.add(panel_render_hidden);

    this.panel.add(panel_node_render_type);
    this.panel.add(panel_node_render_number);

    this.panel.add(panel_link_display_struts);
    this.panel.add(panel_strut_render_number);

    this.panel.add(panel_link_display_cable);
    this.panel.add(panel_cable_render_number);

    this.panel.add(panel_link_length);

    this.panel.add(panel_face_render_type);
    this.panel.add(panel_face_render_number);

    this.panel.add(panel_fog);
    
    this.panel.add(panel_xor);

    this.panel.add(panel_double_buffering);

    this.panel.add(panel_redraw_deepest_first);

    this.panel.add(panel_visible_explosions);
  }

  private void addLinkDisplayTypes(final TTChoice choose_display_cables) {
    choose_display_cables.add("filled", LinkRenderType.SOLID);
    choose_display_cables.add("multiple", LinkRenderType.MULTIPLE);
    choose_display_cables.add("dotted", LinkRenderType.DOTTED);
    choose_display_cables.add("circle (thin)", LinkRenderType.CIRCLE_THIN);
    choose_display_cables.add("circle (filled)", LinkRenderType.CIRCLE_THICK);
    choose_display_cables.add("point", LinkRenderType.POINT);
    choose_display_cables.add("invisible", Link.INVISIBLE);
    choose_display_cables.choice.select(choose_display_cables
      .num_to_str(Link.link_display_cables_type));
  }

  private Panel chooseFaceRenderType() {
    final Panel panel_polygon_render_type = new Panel();
    //panel_quality.setBackground(colour_grey1);
    panel_polygon_render_type.add(new Label("Polygon fill:", Label.RIGHT));

    this.choose_polygon_render_type = new TTChoice(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        final String scs = (String) e.getItem();
        Face.face_display_type = getChoosePolygonRenderType().str_to_num(scs);
        BinGrid.repaint_all_objects = true;
      }
    });

    this.choose_polygon_render_type.add("concentric",
      FaceRenderTypes.CONCENTRIC);
    this.choose_polygon_render_type.add("segmented", FaceRenderTypes.SEGMENTED);
    this.choose_polygon_render_type.add("clockwise", FaceRenderTypes.CLOCKWISE);
    this.choose_polygon_render_type.add("anti-clockwise",
      FaceRenderTypes.ANTI_CLOCKWISE);
    this.choose_polygon_render_type.choice
      .select(this.choose_polygon_render_type
        .num_to_str(Face.face_display_type));
    panel_polygon_render_type.add(this.choose_polygon_render_type.choice);
    return panel_polygon_render_type;
  }

  private Panel getFaceRenderNumber() {
    final Panel panel = new Panel();
    panel.setLayout(new BorderLayout(0, 8));
    panel.add("West", new Label("Face lines:", Label.RIGHT));

    this.scroll_bar_face_render_number = new Scrollbar(Scrollbar.HORIZONTAL,
      Face.number_of_render_divisions, 4, 0, 28);
    this.scroll_bar_face_render_number
      .addAdjustmentListener(new AdjustmentListener() {
        public void adjustmentValueChanged(AdjustmentEvent e) {
          final int temp = e.getValue();
          Face.number_of_render_divisions = temp;
          reflectLabelFaceRenderNumber();
          BinGrid.repaint_all_objects = true;
        }
      });

    panel.add("Center", this.scroll_bar_face_render_number);

    this.label_face_render_number = new Label("", Label.LEFT);
    panel.add("East", this.label_face_render_number);
    reflectLabelFaceRenderNumber();

    return panel;
  }

  private Panel getFogPanel() {
    final Panel panel = new Panel();
    panel.setLayout(new BorderLayout(0, 8));
    panel.add("West", new Label("Fog:", Label.RIGHT));

    this.scroll_bar_fog = new Scrollbar(Scrollbar.HORIZONTAL,
      DeepObjectColourCalculator.factor / 10, 10, 0, 110);
    this.scroll_bar_fog.addAdjustmentListener(new AdjustmentListener() {
      public void adjustmentValueChanged(AdjustmentEvent e) {
        final int temp = e.getValue();
        DeepObjectColourCalculator.factor = temp * 10;
        reflectLabelFog();
      }
    });

    panel.add("Center", this.scroll_bar_fog);

    this.label_fog = new Label("", Label.LEFT);
    panel.add("East", this.label_fog);
    reflectLabelFog();

    return panel;
  }

  private Panel getStrutRenderNumber() {
    final Panel panel = new Panel();
    panel.setLayout(new BorderLayout(0, 8));
    panel.add("West", new Label("Strut lines:", Label.RIGHT));

    this.scroll_bar_link_render_cables = new Scrollbar(Scrollbar.HORIZONTAL,
      Link.number_of_strut_render_divisions, 4, 0, 22);
    this.scroll_bar_link_render_cables
      .addAdjustmentListener(new AdjustmentListener() {
        public void adjustmentValueChanged(AdjustmentEvent e) {
          final int temp = e.getValue();
          Link.number_of_strut_render_divisions = temp;
          reflectLabelStrutRenderNumber();
          BinGrid.repaint_all_objects = true;
        }
      });

    panel.add("Center", this.scroll_bar_link_render_cables);

    this.label_strut_render_number = new Label("", Label.LEFT);
    panel.add("East", this.label_strut_render_number);
    reflectLabelStrutRenderNumber();

    return panel;
  }

  private Panel getCableRenderNumber() {
    final Panel panel = new Panel();
    panel.setLayout(new BorderLayout(0, 8));
    panel.add("West", new Label("Cable lines:", Label.RIGHT));

    this.scroll_bar_link_render_cables = new Scrollbar(Scrollbar.HORIZONTAL,
      Link.number_of_cable_render_divisions, 4, 0, 22);
    this.scroll_bar_link_render_cables
      .addAdjustmentListener(new AdjustmentListener() {
        public void adjustmentValueChanged(AdjustmentEvent e) {
          final int temp = e.getValue();
          Link.number_of_cable_render_divisions = temp;
          reflectLabelCableRenderNumber();
          BinGrid.repaint_all_objects = true;
        }
      });

    panel.add("Center", this.scroll_bar_link_render_cables);

    this.label_cable_render_number = new Label("", Label.LEFT);
    panel.add("East", this.label_cable_render_number);
    reflectLabelCableRenderNumber();

    return panel;
  }

  private Panel getNodeRenderNumber() {
    final Panel panel = new Panel();
    panel.setLayout(new BorderLayout(0, 8));
    panel.add("West", new Label("Node lines:", Label.RIGHT));

    this.scroll_bar_render_nodes = new Scrollbar(Scrollbar.HORIZONTAL,
      Node.number_of_render_divisions, 4, 0, 12);
    this.scroll_bar_render_nodes
      .addAdjustmentListener(new AdjustmentListener() {
        public void adjustmentValueChanged(AdjustmentEvent e) {
          final int temp = e.getValue();
          Node.number_of_render_divisions = temp;
          reflectLabelNodeRenderNumber();
          BinGrid.repaint_all_objects = true;
        }
      });

    panel.add("Center", this.scroll_bar_render_nodes);

    this.label_node_render_number = new Label("", Label.LEFT);
    panel.add("East", this.label_node_render_number);
    reflectLabelNodeRenderNumber();

    return panel;
  }

  private void reflectLabelFaceRenderNumber() {
    getLabelFaceRenderNumber().setText("" + Face.number_of_render_divisions);
  }

  private void reflectLabelFog() {
    getLabelFog().setText("" + (DeepObjectColourCalculator.factor / 10));
  }
  
  private void reflectLabelCableRenderNumber() {
    getLabelCableRenderNumber().setText(
      "" + Link.number_of_cable_render_divisions);
  }

  private void reflectLabelStrutRenderNumber() {
    getLabelStrutRenderNumber().setText(
      "" + Link.number_of_strut_render_divisions);
  }

  private void reflectLabelNodeRenderNumber() {
    getLabelNodeRenderNumber().setText("" + Node.number_of_render_divisions);
  }

  public Label getLabelFog() {
    return this.label_fog;
  }

  public Label getLabelFaceRenderNumber() {
    return this.label_face_render_number;
  }

  public Label getLabelCableRenderNumber() {
    return this.label_cable_render_number;
  }

  public Label getLabelNodeRenderNumber() {
    return this.label_node_render_number;
  }

  public Label getLabelStrutRenderNumber() {
    return this.label_strut_render_number;
  }

  public Checkbox getCheckboxRenderLinks() {
    return this.checkbox_render_links;
  }

  public Checkbox getCheckboxRenderNodes() {
    return this.checkbox_render_nodes;
  }

  public Checkbox getCheckboxRenderPolygons() {
    return this.checkbox_render_polygons;
  }

  public Checkbox getCheckboxRenderCharges() {
    return this.checkbox_render_charges;
  }

  public Checkbox getCheckboxRenderHiddenLinks() {
    return this.checkbox_render_hidden_links;
  }

  public Checkbox getCheckboxRenderHiddenNodes() {
    return this.checkbox_render_hidden_nodes;
  }

  public Checkbox getCheckboxRenderHiddenPolygons() {
    return this.checkbox_render_hidden_polygons;
  }

  public TTChoice getChoosePolygonRenderType() {
    return this.choose_polygon_render_type;
  }

  public MessageManager getMessageManager() {
    return this.message_manager;
  }
}