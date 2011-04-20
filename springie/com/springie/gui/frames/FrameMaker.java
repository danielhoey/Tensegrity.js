// This program has been placed into the public domain by its author.

package com.springie.gui.frames;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.springie.FrEnd;
import com.springie.utilities.general.Forget;

public class FrameMaker {
  //static final String program_name = FrEnd.application_name;

  public Frame setUpFrameControls() {
    final Frame frame = new Frame();
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        Forget.about(e);
        FrEnd.frame_controls.setVisible(false);
      }
    });

    frame.add("Center", FrEnd.panel_controls.panel);
    frame.pack();
    frame.setSize(320, 600);
    frame.setTitle(FrEnd.application_name + " - Controls");
    return frame;
  }

  public Frame setUpFramePreferences() {
    final Frame frame = new Frame();
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        Forget.about(e);
        FrEnd.frame_preferences.setVisible(false);
      }
    });

    frame.add("Center", FrEnd.panel_preferences.panel);
    frame.pack();
    frame.setSize(400, 480);
    frame.setTitle(FrEnd.application_name + " - Preferences");
    return frame;
  }

  public Frame setUpFrameAbout() {
    final Frame frame = new Frame();
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        Forget.about(e);
        FrEnd.frame_panel_about.setVisible(false);
      }
    });

    frame.add("Center", FrEnd.panel_about.panel);
    frame.pack();
    frame.setSize(300, 200);
    frame.setTitle(FrEnd.application_name + " - About box");
    return frame;
  }

  public Frame setUpFrameHelp() {
    final Frame frame = new Frame();
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        Forget.about(e);
        FrEnd.frame_panel_help.setVisible(false);
      }
    });

    frame.add("Center", FrEnd.panel_help.panel);
    frame.pack();
    frame.setSize(300, 200);
    frame.setTitle(FrEnd.application_name + " - Help");

    return frame;
  }
}


//public Frame setUpFrameUniverse() {
//final Frame frame = new Frame();
//frame.addWindowListener(new WindowAdapter() {
//public void windowClosing(WindowEvent e) {
//  Forget.about(e);
//  FrEnd.frame_universe.setVisible(false);
//}
//});
//
//frame.add("Center", FrEnd.panel_edit_universe.panel);
//frame.pack();
//frame.setSize(250, 450);
//frame.setTitle(program_name + " - Universe Controls");
//return frame;
//}

//public Frame setUpFrameDisplay() {
//final Frame frame = new Frame();
//frame.addWindowListener(new WindowAdapter() {
//public void windowClosing(WindowEvent e) {
//  Forget.about(e);
//  FrEnd.frame_display.setVisible(false);
//}
//});
//
//frame.add("Center", FrEnd.panel_edit_display.panel);
//frame.pack();
//frame.setSize(250, 640);
//frame.setTitle(program_name + " - Display Controls");
//return frame;
//}

//public Frame setUpFrameEdit() {
//final Frame frame = new Frame();
//frame.addWindowListener(new WindowAdapter() {
//public void windowClosing(WindowEvent e) {
//  Forget.about(e);
//  FrEnd.frame_panel_edit.setVisible(false);
//}
//});
//
//frame.add("Center", FrEnd.panel_edit_edit.panel);
//frame.pack();
//frame.setSize(272, 640);
//frame.setTitle(program_name + " - Edit Controls");
//return frame;
//}

//public Frame setUpFrameMisc() {
//final Frame frame = new Frame();
//frame.addWindowListener(new WindowAdapter() {
//public void windowClosing(WindowEvent e) {
//  Forget.about(e);
//  FrEnd.frame_extra_controls.setVisible(false);
//}
//});
//
//frame.add("Center", FrEnd.panel_edit_misc.panel);
//frame.pack();
//frame.setSize(250, 600);
//frame.setTitle(program_name + " - Misc Controls");
//return frame;
//}

//public Frame setUpFrameGenerate() {
//final Frame frame = new Frame();
//frame.addWindowListener(new WindowAdapter() {
//public void windowClosing(WindowEvent e) {
//  Forget.about(e);
//  FrEnd.frame_generate.setVisible(false);
//}
//});
//
//frame.add("Center", FrEnd.panel_edit_generate.panel);
//frame.pack();
//frame.setSize(250, 400);
//frame.setTitle(program_name + " - Generate Controls");
//return frame;
//}
