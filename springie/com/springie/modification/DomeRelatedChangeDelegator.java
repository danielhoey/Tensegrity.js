package com.springie.modification;

import com.springie.FrEnd;
import com.springie.collisions.BinGrid;
import com.springie.elements.links.LinkManager;
import com.springie.elements.nodes.NodeManager;
import com.springie.modification.flags.FlagControllerDisabled;
import com.springie.modification.flags.FlagControllerFixed;
import com.springie.modification.flags.FlagControllerHidden;
import com.springie.modification.flags.FlagControllerRope;
import com.springie.modification.resize.ChargeChanger;
import com.springie.modification.resize.LinkElasticityChanger;
import com.springie.modification.resize.LinkLengthChanger;
import com.springie.modification.resize.LinkLengthEqualisation;
import com.springie.modification.resize.LinkRadiusChanger;
import com.springie.modification.resize.LinkResetter;
import com.springie.modification.resize.LinkStiffnessChanger;
import com.springie.modification.resize.NodeSizeChanger;

public class DomeRelatedChangeDelegator {
  NodeManager node_manager;

  LinkManager link_manager;

  public static void equaliseLinkLengths() {
    LinkLengthEqualisation tool;
    tool = new LinkLengthEqualisation(FrEnd.node_manager);
    tool.equalise();
  }

  private static float getScalingScaleFactor() {
    final int value = FrEnd.panel_edit_edit.scroll_bar_scale_by.getValue();
    return value / 100F;
  }

  public static void shortenLinks() {
    final float scale_factor = 1F - getScalingScaleFactor();

    final LinkLengthChanger tool = new LinkLengthChanger(FrEnd.node_manager);
    tool.resize(scale_factor);
  }

  public static void lengthenLinks() {
    final float scale_factor = 1F + getScalingScaleFactor();

    final LinkLengthChanger tool = new LinkLengthChanger(FrEnd.node_manager);
    tool.resize(scale_factor);
  }

  public static void expand() {
    final float scale_factor = 1F + getScalingScaleFactor();

    if (FrEnd.node_manager.isSelection()) {
      new NodeSizeChanger(FrEnd.node_manager).resize(scale_factor);
      BinGrid.repaint_all_objects = true;
    }

    if (FrEnd.node_manager.link_manager.isSelection()) {
      new LinkRadiusChanger(FrEnd.node_manager).resize(scale_factor);
      BinGrid.repaint_all_objects = true;
    }
  }

  public static void contract() {
    final float scale_factor = 1F - getScalingScaleFactor();
    if (FrEnd.node_manager.isSelection()) {
      new NodeSizeChanger(FrEnd.node_manager).resize(scale_factor);
      BinGrid.repaint_all_objects = true;
    }

    if (FrEnd.node_manager.link_manager.isSelection()) {
      new LinkRadiusChanger(FrEnd.node_manager).resize(scale_factor);
      BinGrid.repaint_all_objects = true;
    }
  }

  public static void chargeDown() {
    final float scale_factor = 1F - getScalingScaleFactor();
    new ChargeChanger(FrEnd.node_manager).scaleBy(scale_factor);
    FrEnd.postCleanup();
  }

  public static void chargeUp() {
    final float scale_factor = 1F + getScalingScaleFactor();
    new ChargeChanger(FrEnd.node_manager).scaleBy(scale_factor);
    FrEnd.postCleanup();
  }

  public static void elasticityUp() {
    final float scale_factor = 1F + getScalingScaleFactor();
    final LinkElasticityChanger tool = new LinkElasticityChanger(
      FrEnd.node_manager);
    tool.resize(scale_factor);
  }

  public static void elasticityDown() {
    final float scale_factor = 1F - getScalingScaleFactor();
    final LinkElasticityChanger tool = new LinkElasticityChanger(
      FrEnd.node_manager);
    tool.resize(scale_factor);
  }

  public static void stiffnessUp() {
    final float scale_factor = 1F + getScalingScaleFactor();
    final LinkElasticityChanger tool = new LinkElasticityChanger(
      FrEnd.node_manager);
    tool.resize(scale_factor);
  }

  public static void stiffnessDown() {
    final float scale_factor = 1F - getScalingScaleFactor();
    final LinkStiffnessChanger tool = new LinkStiffnessChanger(
      FrEnd.node_manager);
    tool.resize(scale_factor);
  }

  public static void resetLinkLengths() {
    new LinkResetter(FrEnd.node_manager).reset();
  }

  public static void hide() {
    new FlagControllerHidden(FrEnd.node_manager)
    .hide(FrEnd.panel_edit_edit.checkbox_hidden.getState());
    FrEnd.postCleanup();
  }

  public static void fix() {
    new FlagControllerFixed(FrEnd.node_manager)
      .fix(FrEnd.panel_edit_edit.checkbox_fixed.getState());
  }

  public static void rope() {
    new FlagControllerRope(FrEnd.node_manager)
      .rope(FrEnd.panel_edit_edit.checkbox_cable.getState());
    FrEnd.postCleanup();
  }

  public static void disable() {
    new FlagControllerDisabled(FrEnd.node_manager)
      .disable(FrEnd.panel_edit_edit.checkbox_disabled.getState());
    FrEnd.postCleanup();
  }
}