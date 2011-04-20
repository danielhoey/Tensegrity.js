// This program has been placed into the public domain by its author.

package com.springie.gui.gestures;

import com.springie.FrEnd;
import com.springie.elements.faces.Face;
import com.springie.elements.faces.FaceType;
import com.springie.elements.links.LinkType;
import com.springie.elements.nodes.Node;

public class PerformSelection {
  public void performSelection(int x, int y, boolean drag_is_possible) {
    if (FrEnd.panel_edit_edit.checkbox_select_nodes.getState()) {
      doSelectNodes(x, y, drag_is_possible);
    }

    if (FrEnd.node_manager.isSelection()) {
      return;
    }

    if (FrEnd.panel_edit_edit.checkbox_select_links.getState()) {
      doSelectLinks(x, y);
    }

    if (FrEnd.node_manager.link_manager.isSelection()) {
      return;
    }

    if (FrEnd.panel_edit_edit.checkbox_select_faces.getState()) {
      doSelectPolygons(x, y);
    }
  }

  public void doSelectNodes(int x, int y, boolean drag_is_possible) {
    final Node selected_node = FrEnd.node_manager.isThereOne(x, y);

    if (!FrEnd.currently_dragging) { //FrEnd.dragged_node == null) {
      if (selected_node == null || !selected_node.type.selected) {
        deselectAllNodesInitially();
        deselectAllLinksInitially();
        deselectAllPolygonsInitially();
      }

      // check for selected node...
      if (selected_node != null) {
        FrEnd.selectNewNodeIfAppropriate(selected_node);

        FrEnd.panel_edit_edit.checkbox_fixed.setState(selected_node.type.fixed);
        FrEnd.panel_edit_edit.checkbox_hidden
          .setState(selected_node.type.hidden);
        FrEnd.panel_edit_edit.colour_picker
          .setColour(selected_node.clazz.colour);

        FrEnd.panel_edit_edit.scroll_bar_radius
          .setValue(selected_node.type.radius);
        FrEnd.panel_edit_edit.setRadiusLabel(selected_node.type.radius);
      }
    }

    if (drag_is_possible) {
      dealWithDrag(x, y, selected_node);
    }
  }

  private void dealWithDrag(int x, int y, final Node selected_node) {
    if (FrEnd.button_virginity) {
      if (selected_node != null) {
        FrEnd.dragged_node = selected_node;
        FrEnd.dragged_x_offset = x - FrEnd.dragged_node.pos.x;
        FrEnd.dragged_y_offset = y - FrEnd.dragged_node.pos.y;
        FrEnd.currently_dragging = true;
      }
    } else {
      if (FrEnd.currently_dragging) {
        FrEnd.dragCurrentObject(x, y);
      }
    }
  }

  public void doSelectLinks(int x, int y) {
    deselectAllNodesInitially();
    deselectAllLinksInitially();
    //deselectAllPInitially();

    attemptToSelectLink(x, y);
  }

  private void attemptToSelectLink(int x, int y) {
    FrEnd.selected_link = FrEnd.node_manager.link_manager.isThereOne(x, y);
    if (FrEnd.selected_link != null) {
      final LinkType type = FrEnd.selected_link.type;
      if ((FrEnd.main_canvas.modifiers & 2) != 0) {
        if (FrEnd.button_virginity) {
          FrEnd.prepareToModifyLinkTypes();
          type.selected = !type.selected;
        }
      } else {
        FrEnd.prepareToModifyLinkTypes();
        type.selected = true;
      }

      FrEnd.panel_edit_edit.checkbox_hidden.setState(type.hidden);
      FrEnd.panel_edit_edit.checkbox_disabled.setState(type.disabled);
      FrEnd.panel_edit_edit.checkbox_cable.setState(type.cable);
      FrEnd.panel_edit_edit.colour_picker.setColour(FrEnd.selected_link.clazz.colour);
      FrEnd.panel_edit_edit.scroll_bar_elasticity.setValue(type.elasticity);
      FrEnd.panel_edit_edit.setElasticityLabel(type.elasticity);
      FrEnd.panel_edit_edit.scroll_bar_length.setValue(type.length);
      FrEnd.panel_edit_edit.setLengthLabel(type.length);
      FrEnd.panel_edit_edit.scroll_bar_radius.setValue(type.radius);
      FrEnd.panel_edit_edit.setRadiusLabel(type.radius);
    }

    FrEnd.updateGUIToReflectSelectionChange();
  }

  public void doSelectPolygons(int x, int y) {
    deselectAllNodesInitially();
    deselectAllLinksInitially();
    deselectAllPolygonsInitially();

    attemptToSelectPolygon(x, y);
  }

  private void attemptToSelectPolygon(int x, int y) {
    final Face selected_polygon = FrEnd.node_manager.face_manager.isThereOne(x, y);
    if (selected_polygon != null) {
      final FaceType type = selected_polygon.type;
      if ((FrEnd.main_canvas.modifiers & 2) != 0) {
        if (FrEnd.button_virginity) {
          FrEnd.prepareToModifyFaceTypes();
          type.selected = !type.selected;
        }
      } else {
        FrEnd.prepareToModifyFaceTypes();
        type.selected = true;
      }

      FrEnd.panel_edit_edit.checkbox_hidden.setState(type.hidden);
      FrEnd.panel_edit_edit.colour_picker
        .setColour(selected_polygon.clazz.colour);
    }

    FrEnd.updateGUIToReflectSelectionChange();
  }

  public void selectAll() {
    selectAllNodes();
    selectAllLinks();
  }

  public void deselectAll() {
    deselectAllNodes();
    deselectAllLinks();
  }

  private void deselectAllNodes() {
    FrEnd.node_manager.deselectAll();
  }

  private void selectAllNodes() {
    FrEnd.node_manager.selectAll();
  }

  private void selectAllLinks() {
    FrEnd.node_manager.link_manager.selectAll();
  }

  private void deselectAllLinks() {
    FrEnd.node_manager.link_manager.deselectAll();
    FrEnd.selected_link = null;
  }

  private void deselectAllPolygons() {
    FrEnd.node_manager.face_manager.deselectAll();
  }

  private void deselectAllNodesInitially() {
    if ((FrEnd.main_canvas.modifiers & 3) == 0) {
      deselectAllNodes();
    }
  }

  private void deselectAllLinksInitially() {
    if ((FrEnd.main_canvas.modifiers & 3) == 0) {
      deselectAllLinks();
    }
  }

  private void deselectAllPolygonsInitially() {
    if ((FrEnd.main_canvas.modifiers & 3) == 0) {
      deselectAllPolygons();
    }
  }
}