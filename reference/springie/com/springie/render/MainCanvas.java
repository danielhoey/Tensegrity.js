// This code has been placed into the public domain by its author

package com.springie.render;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.ImageObserver;

import com.springie.FrEnd;
import com.springie.collisions.BinGrid;
import com.springie.gui.components.DropablePanel;
import com.springie.messages.Message;

public class MainCanvas {
  public Panel panel = setUpPanelForFrame2();

  InfoButton info_button = new InfoButton();

  Image image_offscreen;

  public Graphics graphics_handle;

  ImageObserver observer;

  FrEnd parent;

  public int modifiers;

  int xoff = 4;

  int yoff = 4;

  int img_x;

  int img_y;

  Dimension dim;

  int old_gridsize = -99;

  int new_gridsize;

  int current_mouse_x;

  int current_mouse_y;

  // constructor
  public MainCanvas(FrEnd mother) {
    this.parent = mother;

    this.panel.addMouseMotionListener(new MouseMotionListener() {
      public void mouseMoved(MouseEvent e) {
        FrEnd.mouse_pressed = false;

        final MainCanvas canvas = getCanvas();

        canvas.current_mouse_x = e.getX() << Coords.shift;
        canvas.current_mouse_y = e.getY() << Coords.shift;
      }

      public void mouseDragged(MouseEvent e) {
        final MainCanvas canvas = getCanvas();
        canvas.modifiers = e.getModifiers();
        canvas.current_mouse_x = e.getX() << Coords.shift;
        canvas.current_mouse_y = e.getY() << Coords.shift;

        FrEnd.message_manager.newmessage(Message.MOUSE_DRAG,
          e.getX() << Coords.shift, e.getY() << Coords.shift);
      }
    });

    this.panel.addMouseListener(new MouseListener() {
      public void mouseReleased(MouseEvent e) {
        FrEnd.mouse_pressed = false;
        FrEnd.message_manager.newmessage(Message.MOUSE_RELEASE,
          e.getX() << Coords.shift, e.getY() << Coords.shift);
      }

      public void mousePressed(MouseEvent e) {
        final MainCanvas canvas = getCanvas();

        canvas.modifiers = e.getModifiers();
        FrEnd.mouse_pressed = true;

        FrEnd.message_manager.newmessage(Message.MOUSE_PRESSED,
          e.getX() << Coords.shift, e.getY() << Coords.shift);
      }

      public void mouseClicked(MouseEvent e) {
        final InfoButton info_button = getInfoButton();
        info_button.mouseClicked(e);
        
        final MainCanvas canvas = getCanvas();
        canvas.modifiers = e.getModifiers();

        FrEnd.message_manager.newmessage(Message.MOUSE_CLICK,
          e.getX() << Coords.shift, e.getY() << Coords.shift);
      }

      public void mouseEntered(MouseEvent e) {
        FrEnd.message_manager.newmessage(Message.MOUSE_ENTRY,
          e.getX() << Coords.shift, e.getY() << Coords.shift);
      }

      public void mouseExited(MouseEvent e) {
        FrEnd.message_manager.newmessage(Message.MOUSE_EXIT,
          e.getX() << Coords.shift, e.getY() << Coords.shift);
        //Forget.about(e);
      }
    });
  }

  // methods
  public final void start_up() {
    if (Coords.x_pixels < 1) {
      Coords.x_pixels = 511;
    }

    if (Coords.y_pixels < 1) {
      Coords.y_pixels = 511;
    }

    this.img_x = Coords.x_pixels;
    this.img_y = Coords.y_pixels;

    this.image_offscreen = this.panel.createImage(this.img_x, this.img_y);

    getImageHandle();

    FrEnd.node_manager.resetNodeGrid(); // due to resize...

    //StarManager.reset(); // stars need resizing...

    BinGrid.repaint_all_objects = true;
  }

  public final void getImageHandle() {
    if (this.image_offscreen != null) {
      this.graphics_handle = this.image_offscreen.getGraphics();
    }
  }

  public final void forceResize() {
    Coords.x_pixels_old = -1;
  }

  // draw
  public final void update(Graphics g) {
    this.observer = this.panel;

    if (!BinGrid.double_buffering) {
      if (FrEnd.message_manager.current_message != 0) {
        FrEnd.message_manager.process();
      }
    }

    if (FrEnd.isAnimationInactive()) {
      if (!BinGrid.repaint_all_objects) {
        if (!BinGrid.repaint_some_objects) {
          return;
        }
      }
    }

    BinGrid.repaint_some_objects = false;
    //BinGrid.repaint_all_objects = false;

    if (!BinGrid.double_buffering) {
      this.graphics_handle = g;
      BinGrid.redraw_changed(g);
    }

    setUpCoordsSize();

    if ((Coords.x_pixels != Coords.x_pixels_old)
      || (Coords.y_pixels != Coords.y_pixels_old)) {
      this.old_gridsize = this.new_gridsize;

      Coords.x_pixels_old = Coords.x_pixels;
      Coords.y_pixels_old = Coords.y_pixels;

      Coords.x_pixelso2 = Coords.x_pixels >> 1;
      Coords.y_pixelso2 = Coords.y_pixels >> 1;

      start_up();
    }

    if (BinGrid.double_buffering) {
      if ((((BinGrid.generation) & FrEnd.frame_frequency) == 0)
        || FrEnd.isAnimationInactive()) {
        if (this.image_offscreen != null) {
          g.drawImage(this.image_offscreen, this.xoff, this.yoff, this.panel);
        }
      }
    }

    this.info_button.drawInfoButton(g);
  }

  public void setUpCoordsSize() {
    this.dim = this.panel.getSize();

    Coords.x_pixels = this.dim.width;
    Coords.y_pixels = this.dim.height;
  }

  public final void paint(Graphics g) {
    BinGrid.repaint_all_objects = true;

    if (!BinGrid.double_buffering) {
      BinGrid.virgin_applet = 1; // not known why this hack is necessary :-(
    }

    update(g);
  }

  private Panel setUpPanelForFrame() {
    final Panel panel = new Panel() {
      public final void paint(Graphics g) {
        FrEnd.main_canvas.paint(g);
      }

      public final void update(Graphics g) {
        FrEnd.main_canvas.update(g);
      }
    };
    return panel;
  }

  public Panel setUpPanelForFrame2() {
    if (FrEnd.application && FrEnd.usingJava120()) {
      final DropablePanel panel = new DropablePanel() {
        public final void paint(Graphics g) {
          FrEnd.main_canvas.paint(g);
        }

        public final void update(Graphics g) {
          FrEnd.main_canvas.update(g);
        }
      };
      return panel;
    }

    return setUpPanelForFrame();
  }

  MainCanvas getCanvas() {
    return this;
  }
  
  public InfoButton getInfoButton() {
    return this.info_button;
  }

}