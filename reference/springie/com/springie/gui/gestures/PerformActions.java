// This program has been placed into the public domain by its author.

package com.springie.gui.gestures;

import com.springie.FrEnd;
import com.springie.constants.Actions;
import com.springie.constants.ToolTypes;
import com.springie.elements.clazz.Clazz;
import com.springie.elements.links.LinkType;
import com.springie.elements.nodes.Node;
import com.springie.elements.nodes.NodeType;
import com.springie.geometry.Point3D;
import com.springie.modification.delete.DeleteLinks;
import com.springie.modification.pre.PrepareToModifyNodeTypes;
import com.springie.render.Coords;
import com.springie.utilities.math.SquareRoot;
import com.springie.utilities.random.Hortensius32Fast;

public class PerformActions {
  Hortensius32Fast rnd = new Hortensius32Fast();

  public Node pointer_node;

  Node end_node;

  public void actionSwitch(int x, int y, int type) {
    switch (type) {
      case Actions.VISIBLE:
        doSetHidden(x, y, false);

        break;

      case Actions.INVISIBLE:
        doSetHidden(x, y, true);

        break;

      case Actions.LINK:
        doLink(x, y);

        break;

      case Actions.UNLINK:
        doUnlink(x, y);

        break;

      case Actions.UNLINKALL:
        doUnlinkAll(x, y);

        break;

      case Actions.KILL:
        doKill(x, y);

        break;

      case Actions.SELECT:
        FrEnd.perform_selection.performSelection(x, y, true);

        break;

      case Actions.SELECT_NO_DRAG:
        FrEnd.perform_selection.performSelection(x, y, false);

        break;

      case Actions.CLONE:
        doClone(x, y);

        break;

      case Actions.ROTATE:
        doRotation(x, y);
        break;

      case Actions.TRANSLATE:
        doTranslation(x, y);
        break;

      case Actions.SCALE:
        doScale(x, y);
        break;

      case Actions.DIET:
        doDiet(x, y);
        break;

      default:
        break;
    }
  }

  void doRotation(int x, int y) {
    FrEnd.rotation_manager.initialise(x, y);
  }

  void doTranslation(int x, int y) {
    FrEnd.translation_manager.initialise(x, y);
  }

  void doScale(int x, int y) {
    FrEnd.scale_manager.initialise(FrEnd.node_manager, x, y);
  }
  
  void doDiet(int x, int y) {
    FrEnd.diet_manager.initialise(x, y);
  }
  
  void doFreeze(int x, int y) {
    final Node dragged_node = FrEnd.node_manager.isThereOne(x, y);
    if (dragged_node != null) {
      dragged_node.type.fixed = true;
    }
  }

  void doMelt(int x, int y) {
    final Node dragged_node = FrEnd.node_manager.isThereOne(x, y);
    if (dragged_node != null) {
      dragged_node.type.fixed = false;
    }
  }

  void doSetHidden(int x, int y, boolean hidden) {
    final Node dragged_node = FrEnd.node_manager.isThereOne(x, y);
    if (dragged_node != null) {
      NodeType new_type = FrEnd.node_manager.node_type_factory.getNew();
      new_type.makeEqualTo(dragged_node.type);
      new_type.hidden = hidden;

      dragged_node.type = new_type;
    }
  }

  void doLink(int x, int y) {
    if (FrEnd.button_virginity) {
      final Node dragged_node = FrEnd.node_manager.isThereOne(x, y);
      if (dragged_node != null) {

        new DeleteLinks(FrEnd.node_manager).prepare();
        FrEnd.perform_selection.doSelectNodes(x, y, false); //? TODO: check this...
        this.pointer_node = new Node(new Point3D(x, y, 0), 999,
          FrEnd.node_manager.node_type_factory);
        this.pointer_node.type.hidden = false;

        final LinkType link_type = FrEnd.node_manager.link_manager.link_type_factory
          .getNew(60, 30);
        link_type.radius = 5000;
        link_type.hidden = false;
        link_type.cable = false;
        link_type.disabled = false;
        final Clazz dragged_clazz = FrEnd.node_manager.clazz_factory.getNew(0xFFFF69FF);
        FrEnd.node_manager.link_manager.setLink(dragged_node,
          this.pointer_node, link_type, dragged_clazz);

        FrEnd.postCleanup();
      }
    }

    dragNewLink(x, y);
  }

  void doUnlink(int x, int y) {
    Node dragged_node = FrEnd.node_manager.isThereOne(x, y);
    dragged_node = FrEnd.node_manager.isThereOne(x, y);
    if (dragged_node != null) {
      FrEnd.killLastLink(dragged_node);
    }

    FrEnd.postCleanup();
  }

  void doUnlinkAll(int x, int y) {
    Node dragged_node = FrEnd.node_manager.isThereOne(x, y);
    dragged_node = FrEnd.node_manager.isThereOne(x, y);
    if (dragged_node != null) {
      FrEnd.killAllLinks(dragged_node);
    }

    FrEnd.postCleanup();
  }

  void doClone(int x, int y) {
    Node selected_node;
    if (FrEnd.button_virginity) {
      selected_node = FrEnd.node_manager.getSelectedNode();

      if (selected_node != null) {
        final NodeType type = FrEnd.node_manager.node_type_factory.getNew();
        type.makeEqualTo(selected_node.type);
        type.selected = false;

        final Point3D pos = new Point3D(x - selected_node.type.radius, y
          - selected_node.type.radius, 0);

        FrEnd.node_manager.addNewAgent(pos, selected_node.clazz, type);
      }
    }

    FrEnd.postCleanup();
  }

  public void doKill(int x, int y) {
    FrEnd.killtype = Actions.KILL;

    if (FrEnd.button_virginity) {
      single_killing(x, y);
    } else {
      kill_a_line(x, y, FrEnd.last_mousex, FrEnd.last_mousey);
    }
  }

  void single_killing(int x, int y) {
    switch (FrEnd.weapon_type) {
      case ToolTypes._PENCIL:
        kill_a_cell(x, y);
        break;

      case ToolTypes._BRUSH:
        circle_fill(x, y, 0x2000, 0);
        break;

      case ToolTypes._MACHINE:
        octagon_fill(x, y, 0x5000, 127);
        break;

      case ToolTypes._SPRAY:
        octagon_fill(x, y, 0x3000, 31);
        break;

      case ToolTypes._POTATO:
        circle_fill(x, y, 0x4000, 0);
        break;

      default:
        break;
    }
  }

  void octagon_fill(int x, int y, int r, int f) {
    for (int cx = -r; cx < r; cx = cx + 0x800) {
      for (int cy = -r; cy < r; cy = cy + 0x800) {
        if ((this.rnd.nextInt() & f) == 0) {
          if (((cx + cy) > (r * -1.5)) && ((cx + cy) < (r * 1.5))
            && ((cx - cy) > (r * -1.5)) && ((cx - cy) < (r * 1.5))) {
            kill_a_cell(x + cx, y + cy);
          }
        }
      }
    }
  }

  void circle_fill(int x, int y, int r, int f) {
    for (int cx = -r; cx < r; cx = cx + 0x800) {
      for (int cy = -r; cy < r; cy = cy + 0x800) {
        if ((this.rnd.nextInt() & f) == 0) {
          if ((cx * cx + cy * cy) < r * r) {
            kill_a_cell(x + cx, y + cy);
          }
        }
      }
    }
  }

  private void kill_a_cell(int x, int y) {
    final Node temp_node = FrEnd.node_manager.isThereOne(x, y);
    if (temp_node != null) {
      switch (FrEnd.killtype) {
        case Actions.KILL:
          final PrepareToModifyNodeTypes prepare = new PrepareToModifyNodeTypes(
            FrEnd.node_manager);
          prepare.prepare();

          temp_node.simplyKill();

          FrEnd.postCleanup();

          break;

        default:
          break;
      }
    }
  }

  private void kill_a_line(int x, int y, int ox, int oy) {
    float _x = x;
    float _y = y;
    float dx;
    float dy;
    float dis;
    float num;
    int temp;

    switch (FrEnd.weapon_type) {
      case ToolTypes._MACHINE:
        temp = 12;
        break;

      case ToolTypes._BRUSH:
        temp = 3;
        break;

      case ToolTypes._SPRAY:
        temp = 4;
        break;

      case ToolTypes._POTATO:
        temp = 6;
        break;

      default:
        temp = 1;
    }

    final int dis_sq = ((x - ox) * (x - ox)) + ((y - oy) * (y - oy));
    if (dis_sq > 0) {
      dis = SquareRoot.fastSqrt(1 + dis_sq) >> 8;

      if (dis <= temp) {
        num = dis + 2;
      } else {
        num = dis / temp;
      }

      if (num < 2) {
        num = 2;
      }

      dx = (ox - x) / num;
      dy = (oy - y) / num;

      for (int i = 0; i < (int) num; i++) {
        single_killing((int) _x, (int) _y);
        _x += dx;
        _y += dy;
      }
    }
  }

  public void terminateLink(int x, int y) {
    if (this.pointer_node != null) {
      final Node dragged_node = FrEnd.node_manager.getSelectedNode();
      if (dragged_node != null) {
        this.end_node = FrEnd.node_manager.isThereOne(x, y);
        if (this.end_node != null) {
          final int temp_distance = FrEnd.node_manager.distanceBetween(
            dragged_node, this.end_node); // in
          // pixels...
          if (temp_distance > 0) {
            FrEnd.node_manager.link_manager.deleteAllLinksBetween(dragged_node,
              this.end_node);
            final LinkType link_type = FrEnd.node_manager.link_manager.link_type_factory
              .getNew(temp_distance, 10);
            link_type.radius = (dragged_node.type.radius + this.end_node.type.radius) >> 2;
            link_type.hidden = false;

            final Clazz dragged_clazz = FrEnd.node_manager.clazz_factory.getNew(0xFFF0FFF0);
            FrEnd.node_manager.link_manager.setLink(dragged_node,
              this.end_node, link_type, dragged_clazz);
          }
        }

        FrEnd.killAllLinks(this.pointer_node);
      }
      
      FrEnd.postCleanup();
    }
  }

  void dragNewLink(int x, int y) {
    if (this.pointer_node != null) {
      this.pointer_node.pos.x = Coords.inverseXCoords(x, 0);
      this.pointer_node.pos.y = Coords.inverseYCoords(y, 0);
    }

    FrEnd.postCleanup();
  }
}

//  static void doShrink(int x, int y) {
//    if (button_virginity) {
//      final Node dragged_node = node_manager.isThereOne(x, y);
//      if (dragged_node != null) {
//        dragged_node.scrub();
//        dragged_node.type.setSize(dragged_node.type.radius - 256);
//        BinGrid.RepaintAll = true;
//      }
//    }
//  }
//
//  static void doGrow(int x, int y) {
//    if (button_virginity) {
//      final Node dragged_node = node_manager.isThereOne(x, y);
//      if (dragged_node != null) {
//        dragged_node.scrub();
//        dragged_node.type.setSize(dragged_node.type.radius + 256);
//        BinGrid.RepaintAll = true;
//      }
//    }
//  }
