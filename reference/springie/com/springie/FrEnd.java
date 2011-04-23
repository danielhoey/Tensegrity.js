// This program has been placed into the public domain by its author.

package com.springie;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Scrollbar;
import java.awt.TextField;
import java.awt.event.InputEvent;

import com.springie.collisions.BinGrid;
import com.springie.constants.Actions;
import com.springie.constants.Delay;
import com.springie.constants.Enumerations;
import com.springie.constants.FrameFrequency;
import com.springie.constants.Quality;
import com.springie.constants.ToolTypes;
import com.springie.elements.links.Link;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.extension.Module;
import com.springie.gui.GUIStrings;
import com.springie.gui.components.AppletFrame;
import com.springie.gui.components.ChoiceWithDescription;
import com.springie.gui.components.DropablePanel;
import com.springie.gui.components.TTChoice;
import com.springie.gui.frames.FrameMaker;
import com.springie.gui.gestures.DietManager;
import com.springie.gui.gestures.PerformActions;
import com.springie.gui.gestures.PerformSelection;
import com.springie.gui.gestures.RotationManager;
import com.springie.gui.gestures.ScaleManager;
import com.springie.gui.gestures.TranslationManager;
import com.springie.gui.panels.PanelAbout;
import com.springie.gui.panels.PanelFundamental;
import com.springie.gui.panels.PanelHelp;
import com.springie.gui.panels.controls.PanelControls;
import com.springie.gui.panels.controls.PanelControlsEdit;
import com.springie.gui.panels.controls.PanelControlsGenerate;
import com.springie.gui.panels.controls.PanelControlsMisc;
import com.springie.gui.panels.controls.PanelControlsUniverse;
import com.springie.gui.panels.preferences.PanelPreferences;
import com.springie.gui.panels.preferences.PanelPreferencesDisplay;
import com.springie.gui.panels.preferences.PanelPreferencesEdit;
import com.springie.gui.panels.preferences.PanelPreferencesIO;
import com.springie.gui.panels.preferences.PanelPreferencesStereo3D;
import com.springie.gui.panels.preferences.PanelPreferencesUpdate;
import com.springie.gui.panels.preferences.PanelPreferencesViewpoint;
import com.springie.io.in.DataInput;
import com.springie.messages.Message;
import com.springie.messages.MessageManager;
import com.springie.modification.post.PostModification;
import com.springie.modification.pre.PrepareToModifyFaceClazzes;
import com.springie.modification.pre.PrepareToModifyFaceTypes;
import com.springie.modification.pre.PrepareToModifyLinkClazzes;
import com.springie.modification.pre.PrepareToModifyLinkTypes;
import com.springie.modification.pre.PrepareToModifyNodeClazzes;
import com.springie.modification.pre.PrepareToModifyNodeTypes;
import com.springie.preferences.Preferences;
import com.springie.render.Coords;
import com.springie.render.MainCanvas;
import com.springie.utilities.FilePath;
import com.springie.utilities.general.Forget;
import com.springie.utilities.random.Hortensius32Fast;

public class FrEnd extends java.applet.Applet implements Enumerations, Runnable {
  /**
   * true if development version of the app
   */
  public static final boolean development_version = false;

  /**
   * true if this is the cut-down viewer version of Springie
   */
  public static final boolean viewer = false;

  public static final String application_name = viewer ? "SprView" : "Springie";

  /**
   * true if 3rd party module is in use <BR>
   * <STRONG>MAKE SURE THIS BIT IS SET </STRONG> if developing or using a 3rd
   * party module...
   */
  public static final boolean module = false;

  /**
   * true if running in 3D space
   */
  public static boolean three_d = true;

  /**
   * true if confining everything to a hemisphere
   */
  static final boolean testing = false;

  static final boolean chained = true;

  public static String index_subdirectory = "index";

  public static String model_index = "resource://" + index_subdirectory
    + "/index.xml";

  public static boolean node_growth;

  public static boolean continuously_centre;

  public static boolean render_nodes = true;

  public static boolean render_links = true;

  public static boolean render_anaglyph;

  public static boolean render_polygons = true;

  public static boolean render_charges = true;

  public static boolean render_hidden_nodes;

  public static boolean render_hidden_links;

  public static boolean render_hidden_faces;

  public static boolean output_linefeeds = true;

  public static boolean check_collisions = true;

  public static boolean boundaries = true;

  public static boolean oscd = true;

  public static boolean controls_visible = true;

  public static boolean collide_self_only;

  public static int node_initial_size = 32;

  public static boolean explosions = true;

  public static boolean merge;

  static boolean draw_guide = true;

  public static boolean depth_cues;

  public static int weapon_type = ToolTypes._PENCIL;
  
  public static String initial_type;
  
  public static String initial_initial_type = "Moscow";

  public static int frame_frequency = FrameFrequency._FRAME_1;

  public static int delay = Delay._DELAY_5; // 2 is too much for IE JVM!

  public static int action_left_type = Actions.SELECT;

  public static int action_middle_type = Actions.TRANSLATE;

  public static int action_right_type = Actions.ROTATE;

  public static boolean currently_dragging;

  public static Module extension;

  static int sample_rate = 64;

  static int use_native;

  static long very_old_time = -1;

  static long old_time = -1;

  static long last_fps_time = -1;

  static long current_time;

  static long one_frame_takes;

  static long lastime;

  static long newtime;

  static TTChoice choose_resolution;

  static TTChoice choose_display;

  static TTChoice choose_kill;

  static TTChoice choose_frequency;

  public static TTChoice choose_delay;

  static TTChoice choose_sample_rate;

  static TTChoice choose_sample_number;

  static TTChoice choose_density;

  public static TTChoice choose_quality;

  public static TTChoice choose_tool;

  public static TTChoice choose_display_cables;

  public static TTChoice choose_display_struts;

  public static ChoiceWithDescription choose_initial;

  public static ChoiceWithDescription choose_preset_index;

  public static boolean links_disabled;

  public static boolean thread_terminated;

  static int finding_period;

  public static int killtype;

  public static boolean mouse_pressed;

  public static boolean show_exhaust;

  public static int generation;

  public static boolean paused;

  public static int stepping;

  static int start_type;

  static final boolean crippled_version = true;

  static final Color colour_grey1 = new Color(0xB0B0B0);

  public static boolean button_virginity = true;

  static boolean statusShown = true;

  private static volatile Thread runner;

  static final int xoff = 4;

  static final int yoff = 4;

  public static Font bold_font = new Font("Dialog", Font.BOLD, 12);

  static Hortensius32Fast rnd = new Hortensius32Fast();

  public static MessageManager message_manager = new MessageManager();

  public static int last_mousex;

  public static int last_mousey;

  public static boolean application;

  public static int dragged_x_offset;

  public static int dragged_y_offset;

  static Node temp_node;

  static Link temp_link;

  public static Link selected_link;

  public static NodeManager node_manager;

  public static boolean xor;

  public static int quality;

  public static Frame frame_main;

  public static final String window_title_prefix = application_name;

  public static String last_file_path = "test.spr";

  public static String next_file_path = last_file_path;

  static Panel panel_resolution_selector;

  public static Panel panel_with_controls_at_bottom;

  public static Frame frame_controls;

  public static Frame frame_preferences;

  public static Frame frame_panel_about;

  public static Frame frame_panel_help;

  static Scrollbar scroll_bar_zoom;

  public static MainCanvas main_canvas;

  public static Button button_restart;

  static Button button_randomise;

  static Button button_set;

  public static Button button_step;

  static Button button_deselall;

  static Button button_drag;

  //public static Checkbox checkbox_pause;

  static Checkbox checkbox_control;

  static Checkbox checkbox_control1;

  static Checkbox checkbox_control_dome;

  static Checkbox checkbox_simplesize;

  static Checkbox checkbox_complexsize;

  static Checkbox checkbox_exhaust;

  static Checkbox checkbox_boundaries;

  // labels
  static Label label_show_time;

  static Label label_show_gen;

  static Label label_show_cnum;

  static Label label_show_cells;

  static Label label_cell_count;

  static Label label_show_set;

  static Label label_resolution;

  static Label label_size_x;

  static Label label_size_y;

  public static Label label_step_size;

  static Label label_history_size;

  public static Label label_number;

  static Label label_bias;

  static Label label_friction;

  static Label label_zoom;

  static Label label_limit;

  static Label label_size;

  static Label label_gravity;

  static Label label_impact;

  static Label label_noc;

  static Label label_stiffness;

  static Label label_elasticity;

  static Label label_viscocity;

  public static TextField textfield_step_size;

  static PerformActions perform_actions;

  public static PerformSelection perform_selection;

  public static RotationManager rotation_manager;

  public static ScaleManager scale_manager;

  public static DietManager diet_manager;

  public static TranslationManager translation_manager;

  public static boolean redraw_deepest_first = true;

  public static Preferences preferences = new Preferences();

  public static PanelControlsMisc panel_edit_misc = new PanelControlsMisc(
    message_manager);

  public static PanelControlsGenerate panel_edit_generate = new PanelControlsGenerate(
    message_manager);

  public static PanelControlsEdit panel_edit_edit = new PanelControlsEdit(
    message_manager);

  public static PanelControlsUniverse panel_edit_universe = new PanelControlsUniverse(
    message_manager);

  public static PanelFundamental panel_fundamental = new PanelFundamental(
    message_manager);

  public static PanelAbout panel_about = new PanelAbout();

  public static PanelHelp panel_help = new PanelHelp();

  public static PanelControls panel_controls = new PanelControls(
    message_manager);

  public static PanelPreferencesDisplay panel_preferences_display = new PanelPreferencesDisplay(
    message_manager);

  public static PanelPreferencesViewpoint panel_preferences_viewpoint = new PanelPreferencesViewpoint(
    message_manager);

  public static PanelPreferencesStereo3D panel_preferences_stereo3d = new PanelPreferencesStereo3D(
    message_manager);

  public static PanelPreferencesEdit panel_preferences_edit = new PanelPreferencesEdit(
    message_manager);

  public static PanelPreferencesUpdate panel_preferences_update = new PanelPreferencesUpdate(
    message_manager);

  public static PanelPreferencesIO panel_preferences_io = new PanelPreferencesIO(
    message_manager);

  public static PanelPreferences panel_preferences = new PanelPreferences(
    message_manager);

  public static int resolutionx;

  public static int resolutiony;

  public static Applet applet;

  public static DataInput data_input = new DataInput(null);

  public static Node dragged_node;

  public static boolean forces_disabled_during_gesture;

  public static boolean animation_inactive = true;

  public static String archive;

  public void start() {
    applet = this;
    resolutionx = 800;
    resolutiony = 600;

    if (module) {
      extension = new Module();
      extension.oneOffInitialisation();
    }

    BinGrid.initialise(resolutionx, resolutiony);

    setUpGestureManagers();

    main_canvas = new MainCanvas(this);

    paused = false;

    show_exhaust = true;
    quality = Quality.MULTIPLE;

    panel_with_controls_at_bottom = setUpPanelForFrame2();

    this.setBackground(Color.black);

    final Panel panelFPS = new Panel();
    panelFPS.add(new Label("Frames per second:", Label.RIGHT));
    label_show_time = new Label("0000", Label.LEFT);
    panelFPS.add(label_show_time);

    final Panel panel_cnum = new Panel();
    panel_cnum.add(new Label("Number of creatures:", Label.RIGHT));
    label_show_cnum = new Label("0000", Label.LEFT);
    panel_cnum.add(label_show_cnum);

    final Panel panel_generation = new Panel();
    panel_generation.add(new Label("Generation:", Label.RIGHT));
    label_show_gen = new Label("0       ", Label.LEFT);
    panel_generation.add(label_show_gen);
    
    FrEnd.panel_edit_universe.reflectMaxSpeed();

    setLayout(new BorderLayout(0, 0));
    
    if (!viewer) {
      panel_with_controls_at_bottom.add(panel_fundamental.panel);
      //panel_fundamental.panel.setVisible(false);
      //this.validate();
    }

    add("South", panel_with_controls_at_bottom);

    setUpFrames();

    add("Center", main_canvas.panel);

    grey_selected_two();
    
    loadInitialModel();

    FrEnd.node_manager.initialSetUp();
    
    reflectStatusInGUI();

    main_canvas.start_up();

    startThread();

    if (application) {
      resize(resolutionx, resolutiony);
    } else if (last_known_good_x > 0) {
      resize(last_known_good_x, last_known_good_y);
    }
    
    message_manager.newmessage(Message.MSG_DELAYED_RESTART, 0, 0);
    
    validate();
    repaint();
  }

  // Note: sequence is important here...
  private void loadInitialModel() {
    FrEnd.archive = null;
    initial_type = initial_initial_type;

    BinGrid.clearAndThenAddInitialObjects(); // TODO - get rid of this legacy code

    if (!application) {
      FrEnd.archive = getParameter("url");
      final boolean focus = "true".equals(getParameter("focus"));
      preferences.map.put(preferences.key_update_animation_when_pointer_over, new Boolean(focus));
    }
  }

  private void setUpGestureManagers() {
    translation_manager = new TranslationManager();

    perform_actions = new PerformActions();

    perform_selection = new PerformSelection();

    rotation_manager = new RotationManager();

    scale_manager = new ScaleManager();

    diet_manager = new DietManager();
  }

  private static Panel setUpPanelForFrame() {
    final Panel p = new Panel();
    p.setLayout(new GridLayout(0, 1, 0, 0));
    p.setForeground(Color.black);
    p.setBackground(Color.lightGray);
    return p;
  }

  public static Panel setUpPanelForFrame2() {
    if (application && usingJava120()) {
      final DropablePanel p = new DropablePanel();
      p.setLayout(new GridLayout(0, 1, 0, 0));
      p.setForeground(Color.black);
      p.setBackground(Color.lightGray);
      return p;
    }

    return setUpPanelForFrame();
  }

  public static boolean usingJava120() {
    final String version = System.getProperty("java.version");
    return "1.1.9".compareTo(version) < 0;
  }

  private void setUpFrames() {
    final FrameMaker frame_maker = new FrameMaker();

    frame_panel_about = frame_maker.setUpFrameAbout();
    frame_panel_help = frame_maker.setUpFrameHelp();
    frame_preferences = frame_maker.setUpFramePreferences();
    frame_controls = frame_maker.setUpFrameControls();
  }

  static void setUpResolutionSelector2() {
    panel_resolution_selector.removeAll();
    panel_resolution_selector.setBackground(colour_grey1);
    panel_resolution_selector.add(label_size_x);
    panel_resolution_selector.add(label_size_y);
    panel_resolution_selector.add(panel_edit_edit.button_setsize);
  }

  public static void reflectStatusInGUI() {
    panel_edit_edit.reflectRadius();
    panel_edit_edit.reflectCharge();
    panel_edit_edit.reflectLength();
    panel_edit_edit.reflectElasticity();
    panel_edit_edit.reflectStiffness();

    FrEnd.panel_edit_universe.reflectMaxSpeed();
    FrEnd.panel_edit_universe.reflectFriction();
    FrEnd.panel_edit_universe.reflectImpact();
    FrEnd.panel_edit_universe.reflectViscocity();
    FrEnd.panel_edit_universe.reflectGravity();
    FrEnd.panel_edit_universe.reflect3D();

    BinGrid.repaint_all_objects = true;
  }

  // .....................Run Method......................
  public final void run() {
    thread_terminated = false;

    while (!thread_terminated) {
      BinGrid.calculate_grid();
      if (!paused) {
        if (stepping > 0) {
          if (--stepping == 0) {
            endStepping();
          }
        }
      }

      if (BinGrid.double_buffering) {
        if (message_manager.current_message != 0) {
          message_manager.process();
        }

        BinGrid.redraw_changed(main_canvas.graphics_handle);
      }

      //if (((generation & frame_frequency) == 0) || paused) {
      main_canvas.panel.repaint();

      if (delay > 0) {
        takeFortyWinks();
      }

      Thread.yield();
      //}
    }

    thread_terminated = true;

    Thread.yield();
  }

  private void takeFortyWinks() {
    try {
      Thread.sleep(delay);
    } catch (InterruptedException e) {
      Forget.about(e);
    }
  }

  public static final void endStepping() {
    stepping = 0;
    paused = true;
    grey_selected_three();
    button_step.setLabel(GUIStrings.STEP);
  }

  public void startThread() {
    main_canvas.forceResize();
    thread_terminated = false;

    runner = new Thread(this);
    runner.start();

    validate();
    repaint();
  }

  static int last_known_good_x;
  static int last_known_good_y;
  
  public void stop() {
    //Log.log("Call to 'stop' method");

    last_known_good_x = Coords.x_pixels;
    last_known_good_y = Coords.y_pixels;
    thread_terminated = true;
    runner = null;

    removeAll();
  }

  public static final void performResize() {
    main_canvas.forceResize();
    BinGrid.set_size(resolutionx, resolutiony);
    reflectStatusInGUI();
  }

  public static void grey_selected_two() {
    button_step.setEnabled(paused);
  }

  public static void grey_selected_three() {
    panel_fundamental.checkbox_pause.setEnabled(paused);
    button_restart.setEnabled(paused);
  }

  public static void postCleanup() {
    new PostModification(node_manager).cleanup();
    BinGrid.repaint_all_objects = true;
  }

  public static void processMouseClick(int x, int y) {
    if ((main_canvas.modifiers & InputEvent.BUTTON1_MASK) != 0) {
      perform_actions.actionSwitch(x, y, action_left_type);
    }

    if ((main_canvas.modifiers & InputEvent.BUTTON2_MASK) != 0) {
      perform_actions.actionSwitch(x, y, action_middle_type);
    }

    if ((main_canvas.modifiers & InputEvent.BUTTON3_MASK) != 0) {
      perform_actions.actionSwitch(x, y, action_right_type);
    }

    last_mousex = x;
    last_mousey = y;
  }

  public static void springieMouseClicked(int x, int y) {
    if (!button_virginity) {
      processMouseClick(x, y);
      button_virginity = false;
    }
  }

  public static void springieMousePressed(int x, int y) {
    processMouseClick(x, y);
    button_virginity = false;
  }

  public static void springieMouseDragged(int x, int y) {
    processMouseClick(x, y);
    button_virginity = false;
  }

  public static void springieMouseReleased(int x, int y) {
    perform_actions.terminateLink(x, y);
    rotation_manager.terminate(x, y);
    translation_manager.terminate(x, y);
    scale_manager.terminate(x, y);
    diet_manager.terminate(x, y);

    perform_actions.pointer_node = null;
    currently_dragging = false;
    button_virginity = true;
    dragged_node = null;
  }

  public static void springieMouseEntered(int x, int y) {
    last_mousex = x;
    last_mousey = y;

    animation_inactive = false;
    if (((Boolean)FrEnd.preferences.map.get(FrEnd.preferences.key_update_animation_when_pointer_over)).booleanValue()) {
      BinGrid.repaint_all_objects = true;
    }
  }

  public static void springieMouseExited(int x, int y) {
    last_mousex = x;
    last_mousey = y;

    animation_inactive = true;
  }

  public static void selectNewNodeIfAppropriate(Node selected_node) {
    if ((main_canvas.modifiers & 2) != 0) {
      if (button_virginity) {
        prepareToModifyNodeTypes();
        selected_node.type.selected = !selected_node.type.selected;
      }
    } else {
      prepareToModifyNodeTypes();
      selected_node.type.selected = true;
    }

    updateGUIToReflectSelectionChange();
  }

  public static void updateGUIToReflectSelectionChange() {
    BinGrid.repaint_some_objects = true;

    FrEnd.panel_edit_edit.updateGUIGreyItemsDependingOnSelection();
    FrEnd.panel_edit_edit.updateGUIWhenSelectionIsChanging();
  }

  public static void prepareToModifyAllTypes() {
    FrEnd.prepareToModifyNodeTypes();
    FrEnd.prepareToModifyLinkTypes();
    FrEnd.prepareToModifyFaceTypes();
  }

  public static void prepareToModifyNodeTypes() {
    final PrepareToModifyNodeTypes prepare = new PrepareToModifyNodeTypes(
      node_manager);
    prepare.prepare();
    BinGrid.repaint_some_objects = true;
  }

  public static void prepareToModifyLinkTypes() {
    final PrepareToModifyLinkTypes prepare = new PrepareToModifyLinkTypes(
      node_manager.link_manager);
    prepare.prepare();
    BinGrid.repaint_some_objects = true;
  }

  public static void prepareToModifyFaceTypes() {
    final PrepareToModifyFaceTypes prepare = new PrepareToModifyFaceTypes(
      node_manager.face_manager);
    prepare.prepare();
    BinGrid.repaint_some_objects = true;
  }

  static void prepareToModifyNodeClazzes() {
    final PrepareToModifyNodeClazzes prepare = new PrepareToModifyNodeClazzes(
      node_manager);
    prepare.prepare();
    BinGrid.repaint_some_objects = true;
  }

  static void prepareToModifyLinkClazzes() {
    final PrepareToModifyLinkClazzes prepare = new PrepareToModifyLinkClazzes(
      node_manager);
    prepare.prepare();
    BinGrid.repaint_some_objects = true;
  }

  static void prepareToModifyPolygonClazzes() {
    final PrepareToModifyFaceClazzes prepare = new PrepareToModifyFaceClazzes(
      node_manager);
    prepare.prepare();
    BinGrid.repaint_some_objects = true;
  }

  public static void killAllLinks(Node e) {
    node_manager.link_manager.killAllLinks(e);

    postCleanup();
  }

  public static void killLastLink(Node e) {
    node_manager.link_manager.killLastLink(e);

    postCleanup();
  }

  public static void dragCurrentObject(int x, int y) {
    if (FrEnd.dragged_node != null) {
      if (FrEnd.dragged_node.isSelected()) {
        final int d_x = x - dragged_x_offset - FrEnd.dragged_node.pos.x;
        final int d_y = y - dragged_y_offset - FrEnd.dragged_node.pos.y;

        node_manager.moveSelection(d_x, d_y);
        BinGrid.repaint_some_objects = true;
      }
    }
  }

  public static void loadFile(FilePath filepath) {
    final String path = "file://" + filepath;
    FrEnd.setFilePath(path);
    FrEnd.data_input.loadFile(path);
  }

  public static void main(String args[]) {
    Forget.about(args);
    final FrEnd applet = new FrEnd();

    FrEnd.application = true;

    frame_main = new AppletFrame(window_title_prefix, applet, 800, 768);
    frame_main.setVisible(true);
  }
  
  
  public String getAppletInfo() {
    return application_name + " - tensegrity simulator.";
  }

  public static boolean isAnimationInactive() {
    if (FrEnd.paused) {
      return true;
    }

    final Boolean edit_animation_with_pointer = (Boolean) FrEnd.preferences.map
      .get(FrEnd.preferences.key_update_animation_when_pointer_over);

    return FrEnd.animation_inactive
      && edit_animation_with_pointer.booleanValue();
  }

  //public static NodeManager getNodeManager() {
  //return node_manager;
  //}

  public static void setFilePath(String path) {
    FrEnd.last_file_path = path;
    FrEnd.next_file_path = path;

    if (FrEnd.frame_main == null) {
      return;
    }

    final String s = FrEnd.window_title_prefix + " - " + FrEnd.last_file_path;

    FrEnd.frame_main.setTitle(s);
  }
}