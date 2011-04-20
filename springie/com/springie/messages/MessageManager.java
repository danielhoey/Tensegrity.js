package com.springie.messages;

import com.springie.FrEnd;
import com.springie.collisions.BinGrid;
import com.springie.elements.faces.Face;
import com.springie.elements.faces.FaceManager;
import com.springie.elements.links.Link;
import com.springie.elements.links.LinkManager;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeManager;
import com.springie.gui.GUIStrings;
import com.springie.gui.panels.controls.PanelControlsEdit;
import com.springie.modification.DomeRelatedChangeDelegator;
import com.springie.modification.LinkSubdivider;
import com.springie.modification.RemoveLinkAndFuseEnds;
import com.springie.modification.automaticradius.AutomaticLinkRadius;
import com.springie.modification.automaticradius.AutomaticNodeRadius;
import com.springie.modification.delete.DeleteLinks;
import com.springie.modification.delete.DeletePolygons;
import com.springie.modification.faces.FaceMaker;
import com.springie.modification.geodesic.GeodesicMaker;
import com.springie.modification.hexagons.HexagonMaker;
import com.springie.modification.polyhedra.MakeLinksToNearestNode;
import com.springie.modification.projection.PrismaticProjection;
import com.springie.modification.stellation.CentralHubCreator;
import com.springie.modification.stellation.StellationMaker;
import com.springie.modification.velocity.MotionlessMaker;
import com.springie.presets.ProceduralObject;
import com.springie.utilities.log.Log;

public class MessageManager {
  public int current_message; // make private...

  MessageObj[] msgq;

  static final int MSGQ_SIZE = 32;

  public MessageManager() {
    this.msgq = new MessageObj[MSGQ_SIZE];

    for (int c = 0; c < MSGQ_SIZE; c++) {
      this.msgq[c] = new MessageObj(0, 0, 0);
    }
  }

  public final void process() {
    int msgn = 0;
    FrEnd.generation++;

    final int number_of_messages = this.current_message;

    for (msgn = 0; msgn < number_of_messages; msgn++) {
      processMessageAndSwallowErrors(msgn);
    }

    shuffleUpFutureMessages(number_of_messages);

    if (FrEnd.mouse_pressed) {
      newmessage(Message.MOUSE_CLICK, FrEnd.last_mousex, FrEnd.last_mousey);
    }
  }

  private void processMessageAndSwallowErrors(int msgn) {
    try {
      processMessage(msgn);
    } catch (RuntimeException e) {
      Log.log("Error processing message (number:" + this.msgq[msgn] + "):" + e);
      e.printStackTrace();
    }
  }

  private void processMessage(int msgn) {
    HexagonMaker hexagon_maker;
    final MessageObj message = this.msgq[msgn];
    final NodeManager node_manager = FrEnd.node_manager;
    final FaceManager face_manager = node_manager.face_manager;
    final LinkManager link_manager = node_manager.link_manager;
    switch (this.msgq[msgn].type) {
      case Message.MOUSE_DRAG:
        FrEnd.springieMouseDragged(message.data1, message.data2);

        break;

      case Message.MOUSE_CLICK:
        FrEnd.springieMouseClicked(message.data1, message.data2);

        break;

      case Message.MOUSE_PRESSED:
        FrEnd.springieMousePressed(message.data1, message.data2);

        break;

      case Message.MOUSE_RELEASE:
        FrEnd.springieMouseReleased(message.data1, message.data2);

        break;

      case Message.MOUSE_ENTRY:
        FrEnd.springieMouseEntered(message.data1, message.data2);

        break;

      case Message.MOUSE_EXIT:
        FrEnd.springieMouseExited(message.data1, message.data2);

        break;

      case Message.MSG_DELAYED_RESTART:
        newmessage(Message.MSG_RESTART, 0, 0);

        break;

      case Message.MSG_RESTART:
        FrEnd.data_input.loadFile("" + FrEnd.next_file_path);
        FrEnd.reflectStatusInGUI();
        //Log.log("MSG_RESTART.generation" + FrEnd.generation);

        break;

      case Message.MSG_CLEAR:
        FrEnd.node_manager.initialSetUp();

        FrEnd.last_file_path = null;

        FrEnd.reflectStatusInGUI();

        break;

      case Message.MSG_PRESET_CHOSEN:
        FrEnd.data_input.resetWorkspaces();
        BinGrid.clearAndThenAddInitialObjects();
        FrEnd.reflectStatusInGUI();

        break;

      case Message.MSG_ADDITION:
        BinGrid.addBoids();
        FrEnd.reflectStatusInGUI();

        break;

      case Message.MSG_GENERATE_TUBE:
        allNewProceduralObject(new ArgumentList() {
          public Object getArguments(int i) {
            switch (i) {
              case 0:
                return ProceduralObject.tube;
              case 1:
                return new Integer(
                  FrEnd.panel_edit_generate.textfield_generate_tube_circumference
                    .getText());
              case 2:
                return new Integer(
                  FrEnd.panel_edit_generate.textfield_generate_tube_length
                    .getText());
              default:
                return null;
            }
          }
        });

        break;

      case Message.MSG_GENERATE_FREE_NODES:
        allNewProceduralObject(new ArgumentList() {
          public Object getArguments(int i) {
            switch (i) {
              case 0:
                return ProceduralObject.free_nodes;
              case 1:
                return new Integer(
                  FrEnd.panel_edit_generate.textfield_generate_free_nodes
                    .getText());
              default:
                return null;
            }
          }
        });

        break;

      case Message.MSG_GENERATE_SPHERE_PACK:
        allNewProceduralObject(new ArgumentList() {
          public Object getArguments(int i) {
            switch (i) {
              case 0:
                return ProceduralObject.sphere_pack;
              case 1:
                return new Integer(
                  FrEnd.panel_edit_generate.textfield_generate_sphere_pack
                    .getText());
              default:
                return null;
            }
          }
        });

        break;

      case Message.MSG_GENERATE_MATRIX:
        allNewProceduralObject(new ArgumentList() {
          public Object getArguments(int i) {
            switch (i) {
              case 0:
                return ProceduralObject.matrix;
              case 1:
                return new Integer(
                  FrEnd.panel_edit_generate.textfield_generate_matrix_x
                    .getText());
              case 2:
                return new Integer(
                  FrEnd.panel_edit_generate.textfield_generate_matrix_y
                    .getText());
              case 3:
                return new Integer(
                  FrEnd.panel_edit_generate.textfield_generate_matrix_z
                    .getText());
              default:
                return null;
            }
          }
        });

        break;

      case Message.MSG_RESIZE:
        FrEnd.resolutionx = this.msgq[msgn].data1;
        FrEnd.resolutiony = FrEnd.resolutionx;
        FrEnd.performResize();

        break;

      case Message.MSG_SETNEWSIZE:
        FrEnd.performResize();

        break;

      case Message.MSG_STEP:
        FrEnd.paused = false;

        try {
          FrEnd.stepping = Integer
            .parseInt(FrEnd.textfield_step_size.getText());
          if (!BinGrid.double_buffering) {
            FrEnd.stepping++;
          }
        } catch (RuntimeException e) {
          FrEnd.stepping = 1;
        }

        FrEnd.grey_selected_three();
        FrEnd.button_step.setLabel(GUIStrings.CANCEL);

        break;

      case Message.MSG_DOME_CONNECT_NODES_IN_FIRST_LAYER:
        final GeodesicMaker geodesic_maker = new GeodesicMaker(node_manager);
        geodesic_maker.connectNodesInFirstLayer();
        FrEnd.postCleanup();

        break;

      case Message.MSG_ADD_STELLATIONS:
        final StellationMaker stellation_maker = new StellationMaker(
          node_manager);
        final float sf = Float
          .parseFloat(FrEnd.panel_edit_misc.textfield_add_stellations.getText());

        stellation_maker.addStellations(1F + sf, 0xFFFFFF60);
        FrEnd.postCleanup();

        break;

      case Message.MSG_PRISMATIC_PROJECTION:
        final PrismaticProjection prismatic_projection = new PrismaticProjection(
          node_manager);
        final float sf3 = Float
          .parseFloat(FrEnd.panel_edit_misc.textfield_prismatic_projection
            .getText());
        prismatic_projection.project(sf3);
        FrEnd.postCleanup();

        break;

      case Message.MSG_EDIT_ADD_CENTRAL_HUB:
        final CentralHubCreator central_hub_creator = new CentralHubCreator(
          node_manager);
        central_hub_creator.create();
        FrEnd.postCleanup();

        break;

      case Message.MSG_ADD_NODES_TRIAXIAL:
        hexagon_maker = new HexagonMaker(node_manager);
        final float sf2 = Float
          .parseFloat(FrEnd.panel_edit_misc.textfield_add_triaxial_nodes
            .getText());
        hexagon_maker.createNodesInTriaxialOuterLayer(sf2);
        FrEnd.postCleanup();

        break;

      case Message.MSG_ADD_NODES_INNER_EDEN:
        hexagon_maker = new HexagonMaker(node_manager);
        final float sf4 = Float
          .parseFloat(FrEnd.panel_edit_misc.textfield_add_nodes_inner_eden
            .getText());
        hexagon_maker.createNodesInTriaxialOuterLayer(sf4);
        //createNodesInInnerEdenLayer(sf4);
        //hexagon_maker.createNodesInTriaxialOuterLayer(sf4);
        FrEnd.postCleanup();

        break;

      case Message.MSG_CONNECT_NODES_TO_NEAREST_NODES:
        final MakeLinksToNearestNode mltnn = new MakeLinksToNearestNode(
          node_manager);
        final int n_nearest = Integer
          .parseInt(FrEnd.panel_edit_misc.textfield_link_nearest_n_nodes
            .getText());
        mltnn.connectNodesToNearestNodes(n_nearest);
        FrEnd.postCleanup();

        break;

      //        case Message.MSG_DOME_CONNECT_NODES_IN_TRIAXIAL_LAYER:
      //          hexagon_maker = new HexagonMaker(node_manager);
      //          hexagon_maker.connectNodesToNearestNodes(4);
      //          FrEnd.postCleanup();
      //
      //          break;

      //        case Message.MSG_DOME_CONNECT_NODES_IN_TRIAXIAL_LAYER:
      //          hexagon_maker = new HexagonMaker(node_manager);
      //          hexagon_maker.connectNodesInTriaxialOuterLayer();
      //          FrEnd.postCleanup();
      //
      //          break;

      case Message.MSG_EDIT_AUTOMATIC_NODE_RADIUS:
        final AutomaticNodeRadius anr = new AutomaticNodeRadius(node_manager);
        final String anr_s = FrEnd.panel_edit_misc.textfield_automatic_node_radius
          .getText();
        final double anr_d = Double.parseDouble(anr_s);
        anr.set(anr_d, false);
        FrEnd.postCleanup();

        break;

      case Message.MSG_EDIT_AUTOMATIC_LINK_RADIUS:
        final AutomaticLinkRadius alr = new AutomaticLinkRadius(node_manager);
        final String alr_s = FrEnd.panel_edit_misc.textfield_automatic_link_radius
          .getText();
        final double alr_d = Double.parseDouble(alr_s);
        alr.set(alr_d);
        FrEnd.postCleanup();

        break;

      case Message.MSG_EDIT_SPLIT_LINKS:
        final LinkSubdivider ls = new LinkSubdivider(node_manager);
        final String ls_s = FrEnd.panel_edit_misc.textfield_split_links
          .getText();
        final int ls_i = Integer.parseInt(ls_s);
        final boolean b = FrEnd.panel_edit_misc.checkbox_split_links_remove_old
          .getState();

        ls.divide(ls_i, b);

        FrEnd.postCleanup();

        break;

      case Message.MSG_EDIT_ADD_POLYGONS:
        final FaceMaker polygon_maker = new FaceMaker(node_manager);
        //final int c = FrEnd.colour_picker.getColour();
        final int i = Integer
          .parseInt(FrEnd.panel_edit_misc.textfield_add_polygons.getText());
        polygon_maker.addPolygons(i);

        FrEnd.postCleanup();

        break;

      case Message.MSG_EDIT_REMOVE_LINKS:
        final DeleteLinks deleter_l = new DeleteLinks(node_manager);
        deleter_l.delete();

        FrEnd.postCleanup();

        break;

      case Message.MSG_EDIT_REMOVE_POLYGONS:
        final DeletePolygons deleter_p = new DeletePolygons(node_manager);
        deleter_p.delete();

        FrEnd.postCleanup();

        break;

      case Message.MSG_DOME_NODES_CONTRACT:
        DomeRelatedChangeDelegator.contract();
        FrEnd.panel_edit_edit.reflectRadius();

        break;

      case Message.MSG_DOME_NODES_EXPAND:
        DomeRelatedChangeDelegator.expand();
        FrEnd.panel_edit_edit.reflectRadius();

        break;

      case Message.MSG_EDIT_CHARGE_UP:
        DomeRelatedChangeDelegator.chargeUp();
        FrEnd.panel_edit_edit.reflectCharge();

        break;

      case Message.MSG_EDIT_CHARGE_DOWN:
        DomeRelatedChangeDelegator.chargeDown();
        FrEnd.panel_edit_edit.reflectCharge();

        break;

      case Message.MSG_CHANGE_ELASTICITY_UP:
        DomeRelatedChangeDelegator.elasticityUp();
        FrEnd.panel_edit_edit.reflectElasticity();

        break;

      case Message.MSG_CHANGE_ELASTICITY_DOWN:
        DomeRelatedChangeDelegator.elasticityDown();
        FrEnd.panel_edit_edit.reflectElasticity();

        break;

      case Message.MSG_SCALE_LINKS_DOWN:
        DomeRelatedChangeDelegator.shortenLinks();
        FrEnd.panel_edit_edit.reflectLength();

        break;

      case Message.MSG_SCALE_LINKS_UP:
        DomeRelatedChangeDelegator.lengthenLinks();
        FrEnd.panel_edit_edit.reflectLength();

        break;

      case Message.MSG_CHANGE_STIFFNESS_DOWN:
        DomeRelatedChangeDelegator.stiffnessDown();
        FrEnd.panel_edit_edit.reflectStiffness();

        break;

      case Message.MSG_CHANGE_STIFFNESS_UP:
        DomeRelatedChangeDelegator.stiffnessUp();
        FrEnd.panel_edit_edit.reflectStiffness();

        break;

      case Message.MSG_EDIT_HIDE_FLAG:
        DomeRelatedChangeDelegator.hide();

        break;

      case Message.MSG_EDIT_FIXED_FLAG:
        DomeRelatedChangeDelegator.fix();

        break;

      case Message.MSG_EDIT_ROPE_FLAG:
        DomeRelatedChangeDelegator.rope();

        break;

      case Message.MSG_EDIT_DISABLED_FLAG:
        DomeRelatedChangeDelegator.disable();

        break;

      case Message.MSG_DOME_LINKS_EQUALISE_LINK_LENGTHS:
        DomeRelatedChangeDelegator.equaliseLinkLengths();
        break;

      case Message.MSG_DOME_LINKS_RESET_LENGTHS:
        DomeRelatedChangeDelegator.resetLinkLengths();
        break;

      //        case Message.MSG_ADD_LINKS_TO_NEAREST:
      //          final MakeLinksToNearestNode mltnn = new MakeLinksToNearestNode(
      //            node_manager);
      //          mltnn.connectNodesToNearestNodes();

      //FrEnd.postCleanup();
      //break;

      case Message.MSG_DOME_MAKE_MOTIONLESS:
        new MotionlessMaker().freeze(node_manager);
        break;

      //        case Message.MSG_CONTROL:
      //          FrEnd.controls_visible = !FrEnd.controls_visible;
      //          if (FrEnd.controls_visible) {
      //            FrEnd.applet.add("East", FrEnd.panel_main.panel);
      //          } else {
      //            FrEnd.applet.remove(FrEnd.panel_main.panel);
      //
      //          }
      //
      //          break;

      case Message.MSG_CHANGE_AGENT_NUMBER:
        FrEnd.label_number.setText("" + this.msgq[msgn].data1);

        break;

      case Message.MSG_DB:
        BinGrid.double_buffering = !BinGrid.double_buffering;
        FrEnd.postCleanup();

        FrEnd.main_canvas.getImageHandle();
        FrEnd.main_canvas.forceResize();

        break;

      case Message.MSG_PAUSE:
        FrEnd.paused = FrEnd.panel_fundamental.checkbox_pause.getState();
        FrEnd.grey_selected_two();

        break;

      case Message.MSG_NODE_GROWTH:
        FrEnd.node_growth = !FrEnd.node_growth;

        break;

      case Message.MSG_CONTINUOUSLY_CENTRE:
        FrEnd.continuously_centre = !FrEnd.continuously_centre;

        break;

      case Message.MSG_CANCEL:
        FrEnd.endStepping();

        break;

      case Message.MSG_DELETE:
        node_manager.deleteSelected();
        link_manager.deleteSelected();
        face_manager.deleteSelected();
        FrEnd.postCleanup();

        break;

      case Message.MSG_LINKLENGTH:
        FrEnd.prepareToModifyLinkTypes();
        Link.link_display_length = this.msgq[msgn].data1;
        FrEnd.postCleanup();

        break;

      case Message.MSG_SELECT_ALL_TYPE:
        FrEnd.prepareToModifyAllTypes();
        selectAllOfType();

        break;

      case Message.MSG_DESELECT_ALL_TYPE:
        FrEnd.prepareToModifyAllTypes();
        deselectAllOfType();

        break;

      case Message.MSG_SELECT_CLAZZ:
        if (node_manager.isSelection()) {
          final Node n_clazz = node_manager.getSelectedNode();
          node_manager.selectAll(n_clazz.clazz.colour);
        }

        if (link_manager.isSelection()) {
          final Link l = link_manager.getFirstSelectedLink();
          link_manager.selectAll(l.clazz.colour);
        }

        if (face_manager.isSelection()) {
          final Face p = face_manager.getFirstSelectedPolygon();
          face_manager.selectAll(p.clazz.colour);
        }

        break;

      case Message.MSG_SELECT_TYPE:
        if (node_manager.isSelection()) {
          node_manager.selectAll();
        }

        if (link_manager.isSelection()) {
          link_manager.selectAll();
        }

        if (face_manager.isSelection()) {
          face_manager.selectAll();
        }

        break;

      case Message.MSG_SELECT_ALL_NODES:
        FrEnd.prepareToModifyNodeTypes();
        node_manager.selectAll();
        FrEnd.postCleanup();

        break;

      case Message.MSG_SELECT_ALL_LINKS:
        FrEnd.prepareToModifyLinkTypes();
        link_manager.selectAll();
        FrEnd.postCleanup();

        break;

      case Message.MSG_SELECT_ALL_FACES:
        FrEnd.prepareToModifyFaceTypes();
        face_manager.selectAll();
        FrEnd.postCleanup();

        break;

      case Message.MSG_SELECT_ALL_FACES_WITH_N_SIDES:
        FrEnd.prepareToModifyFaceTypes();
        final int n_sides = Integer
          .parseInt(FrEnd.panel_edit_misc.textfield_select_faces_with_n_sides
            .getText());
        face_manager.selectAllWithNSides(n_sides);
        FrEnd.postCleanup();

        break;

      case Message.MSG_SELECT_ALL:
        FrEnd.prepareToModifyAllTypes();
        node_manager.selectAll();
        link_manager.selectAll();
        face_manager.selectAll();
        FrEnd.postCleanup();

        break;

      case Message.MSG_DESELECT_ALL:
        FrEnd.prepareToModifyAllTypes();
        node_manager.deselectAll();
        link_manager.deselectAll();
        face_manager.deselectAll();
        FrEnd.postCleanup();

        break;

      case Message.MSG_ALTERSIZE:
        FrEnd.prepareToModifyAllTypes();
        node_manager.setSizeOfSelected((byte) this.msgq[msgn].data1);
        link_manager.setSizeOfSelected(this.msgq[msgn].data1);
        FrEnd.postCleanup();

        break;

      case Message.MSG_ALTER_ELASTICITY:
        FrEnd.prepareToModifyLinkTypes();
        final int elasticity = this.msgq[msgn].data1;
        link_manager.setElasticityOfSelected(elasticity);
        FrEnd.panel_edit_edit.reflectElasticity();
        FrEnd.postCleanup();

        break;

      case Message.MSG_ALTER_STIFFNESS:
        FrEnd.prepareToModifyLinkTypes();
        final int stiffness = this.msgq[msgn].data1;
        link_manager.setStiffnessOfSelected(stiffness);
        FrEnd.panel_edit_edit.reflectStiffness();
        FrEnd.postCleanup();

        break;

      case Message.MSG_ALTER_LENGTH:
        FrEnd.prepareToModifyLinkTypes();
        final int length = this.msgq[msgn].data1;
        link_manager.setLengthOfSelected(length);
        FrEnd.panel_edit_edit.reflectLength();
        FrEnd.postCleanup();

        break;

      case Message.MSG_ALTER_RADIUS:
        FrEnd.prepareToModifyAllTypes();
        final int radius = this.msgq[msgn].data1;
        link_manager.setRadiusOfSelected(radius);
        node_manager.setRadiusOfSelected(radius);
        FrEnd.panel_edit_edit.reflectRadius();
        FrEnd.postCleanup();

        break;

      case Message.MSG_ALTER_CHARGE:
        FrEnd.prepareToModifyNodeTypes();
        final int charge = this.msgq[msgn].data1;
        node_manager.setChargeOfSelected(charge);
        FrEnd.panel_edit_edit.reflectCharge();
        FrEnd.postCleanup();

        break;

      case Message.MSG_EDIT_REMOVE_LINK_AND_FUSE_ENDS:
        FrEnd.prepareToModifyLinkTypes();
        final RemoveLinkAndFuseEnds end_fuse = new RemoveLinkAndFuseEnds(
          node_manager);
        end_fuse.action();
        FrEnd.postCleanup();

        break;

      default:
        throw new RuntimeException("Message Error");

    }
  }

  private void shuffleUpFutureMessages(final int number_of_messages) {
    int num_left = this.current_message - number_of_messages;

    if (num_left > 0) {
      for (int i = 0; i < num_left; i++) {
        this.msgq[i] = this.msgq[number_of_messages + i];
      }
    }

    this.current_message = num_left;
  }

  private void deselectAllOfType() {
    final PanelControlsEdit panel = FrEnd.panel_edit_edit;
    if (panel.checkbox_select_nodes.getState()) {
      FrEnd.node_manager.deselectAll();
    }

    if (panel.checkbox_select_links.getState()) {
      FrEnd.node_manager.link_manager.deselectAll();
    }

    if (panel.checkbox_select_faces.getState()) {
      FrEnd.node_manager.face_manager.deselectAll();
    }
  }

  private void selectAllOfType() {
    final PanelControlsEdit panel = FrEnd.panel_edit_edit;
    if (panel.checkbox_select_nodes.getState()) {
      FrEnd.node_manager.selectAll();
    }

    if (panel.checkbox_select_links.getState()) {
      FrEnd.node_manager.link_manager.selectAll();
    }

    if (panel.checkbox_select_faces.getState()) {
      FrEnd.node_manager.face_manager.selectAll();
    }
  }

  public void allNewProceduralObject(ArgumentList al) {
    FrEnd.data_input.resetWorkspaces();
    BinGrid.clearAndThenAddProceduralObjects(al);
    FrEnd.reflectStatusInGUI();
  }

  public final void newmessage(int t, int d1, int d2) {
    if (this.current_message < MSGQ_SIZE) {
      this.msgq[this.current_message].type = t;
      this.msgq[this.current_message].data1 = d1;
      this.msgq[this.current_message].data2 = d2;

      this.current_message++;
    }
  }
}