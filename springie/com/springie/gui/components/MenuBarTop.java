// This program has been placed into the public domain by its author.

package com.springie.gui.components;

import java.awt.FileDialog;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;

import com.springie.FrEnd;
import com.springie.io.out.writers.eig.WriterEIG;
import com.springie.io.out.writers.off.WriterOFF;
import com.springie.io.out.writers.pov.WriterPOV;
import com.springie.io.out.writers.spr.WriterSpr;
import com.springie.io.out.writers.wrl.WriterWRL;
import com.springie.utilities.FilePath;
import com.springie.utilities.general.Forget;

public class MenuBarTop extends MenuBar {
  static final String QUIT = "Quit";

  private static final String CONTROLS = "Controls...";

  private static final String PREFERENCES = "Preferences...";

  static final String HELP = "Help...";

  static final String ABOUT = "About...";

  static final String LOAD_DATA = "Load objects...";

  static final String SAVE_AS_SPR = "Save .SPR file as...";

  static final String SAVE_AS_EIG = "Save .EIG file as...";

  static final String SAVE_AS_OFF = "Save .OFF file as...";

  static final String SAVE_AS_POV = "Save .POV file as...";

  static final String SAVE_AS_WRL = "Save .WRL file as...";

  AppletFrame frame;

  public MenuBarTop(AppletFrame frame) {
    this.frame = frame;

    this.add(makeLoadMenu());

    this.add(makeSaveMenu());

    this.add(makeWindowMenu());

    this.add(makeHelpMenu());

    this.add(makeQuitMenu());
  }

  private Menu makeLoadMenu() {
    final Menu file = new Menu("Load", true);

    file.add(MenuBarTop.LOAD_DATA);

    file.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        final String arg = e.getActionCommand();

        if (LOAD_DATA.equals(arg)) {
          chooseLoad();
        } else if (QUIT.equals(arg)) {
          chooseQuit();
        }
      }
    });
    return file;
  }

  private Menu makeWindowMenu() {
    final Menu file = new Menu("Window", true);

    file.add(MenuBarTop.CONTROLS);
    file.add(MenuBarTop.PREFERENCES);

    file.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        final String arg = e.getActionCommand();

        if (MenuBarTop.CONTROLS.equals(arg)) {
          FrEnd.frame_controls.setVisible(true);
        } else if (MenuBarTop.PREFERENCES.equals(arg)) {
          FrEnd.frame_preferences.setVisible(true);
        }
      }
    });
    return file;
  }

  private Menu makeSaveMenu() {
    final Menu export = new Menu("Save", true);
    export.add(MenuBarTop.SAVE_AS_SPR);
    export.add(MenuBarTop.SAVE_AS_OFF);
    export.add(MenuBarTop.SAVE_AS_EIG);
    export.add(MenuBarTop.SAVE_AS_WRL);
    export.add(MenuBarTop.SAVE_AS_POV);

    export.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        final String arg = e.getActionCommand();

        if (SAVE_AS_SPR.equals(arg)) {
          chooseSaveAsSpr();
        } else if (SAVE_AS_OFF.equals(arg)) {
          chooseSaveAsOff();
        } else if (SAVE_AS_EIG.equals(arg)) {
          chooseSaveAsEig();
        } else if (SAVE_AS_WRL.equals(arg)) {
          chooseSaveAsWrl();
        } else if (SAVE_AS_POV.equals(arg)) {
          chooseSaveAsPov();
        }
      }
    });
    return export;
  }

  
  private Menu makeHelpMenu() {
    final Menu menu = new Menu("Help", true);
    menu.add(HELP);
    menu.add(ABOUT);
    menu.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        final String arg = e.getActionCommand();
        if (HELP.equals(arg)) {
          FrEnd.frame_panel_help.setVisible(true);
        } else if (ABOUT.equals(arg)) {
          FrEnd.frame_panel_about.setVisible(true);
        }
      }
    });
    return menu;
  }

  private Menu makeQuitMenu() {
    final Menu menu = new Menu("Quit", true);
    menu.add(MenuBarTop.QUIT);
    menu.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        final String arg = e.getActionCommand();
        if (QUIT.equals(arg)) {
          chooseQuit();
        }
      }
    });
    return menu;
  }

  protected AppletFrame getFrame() {
    return this.frame;
  }

  private void chooseLoad() {
    final FileDialog fd = new FileDialog(this.frame.getAppletFrame(),
      "Load objects", FileDialog.LOAD);
    fd.setVisible(true);
    final String returnedstring = fd.getFile();
    fd.setVisible(false);

    if (isAcceptableFileName(returnedstring)) {
      final FilePath fp = new FilePath(fd.getDirectory(), returnedstring);
      FrEnd.loadFile(fp);
    }
  }

  private void chooseSaveAsWrl() {
    final FileDialog fd = new FileDialog(this.frame.getAppletFrame(),
      "Save .WRL file as:", FileDialog.SAVE);

    final String no_ext = getLeaf(FrEnd.last_file_path);
    fd.setFile(ensureExtension(no_ext, "wrl"));

    fd.setFilenameFilter(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        Forget.about(dir);
        return !(name.endsWith(".wrl"));
      }
    });
    fd.setVisible(true);
    final String path = fd.getDirectory();
    final String leaf = fd.getFile();
    if (isAcceptableFileName(leaf)) {
      new WriterWRL(FrEnd.node_manager).write(path + leaf);
    }
  }

  private void chooseSaveAsSpr() {
    final FileDialog fd = new FileDialog(this.frame.getAppletFrame(),
      "Save .SPR file as:", FileDialog.SAVE);

    final String leaf = getLeaf(FrEnd.last_file_path);
    fd.setFile(ensureExtension(leaf, "spr"));

    fd.setFilenameFilter(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        Forget.about(dir);
        return !(name.endsWith(".spr"));
      }
    });

    fd.setVisible(true);
    final String path = fd.getDirectory();
    final String leaf_name = fd.getFile();
    if (isAcceptableFileName(leaf_name)) {
      final String file_path = path + leaf_name;
      new WriterSpr(FrEnd.node_manager).write(file_path);
      FrEnd.setFilePath("file://" + file_path);
    }
  }

  private void chooseSaveAsOff() {
    final FileDialog fd = new FileDialog(this.frame.getAppletFrame(),
      "Save .OFF file as:", FileDialog.SAVE);

    final String leaf = getLeaf(FrEnd.last_file_path);
    fd.setFile(ensureExtension(leaf, "off"));

    fd.setFilenameFilter(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        Forget.about(dir);
        return !(name.endsWith("off"));
      }
    });

    fd.setVisible(true);
    final String path = fd.getDirectory();
    final String leaf_name = fd.getFile();
    if (isAcceptableFileName(leaf_name)) {
      final String file_path = path + leaf_name;
      new WriterOFF(FrEnd.node_manager).write(file_path);
      FrEnd.setFilePath("file://" + file_path);
    }
  }

  private void chooseSaveAsEig() {
    final FileDialog fd = new FileDialog(this.frame.getAppletFrame(),
      "Save .EIG file as:", FileDialog.SAVE);

    final String leaf = getLeaf(FrEnd.last_file_path);
    fd.setFile(ensureExtension(leaf, "eig"));

    fd.setFilenameFilter(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        Forget.about(dir);
        return !(name.endsWith("eig"));
      }
    });

    fd.setVisible(true);
    final String path = fd.getDirectory();
    final String leaf_name = fd.getFile();
    if (isAcceptableFileName(leaf_name)) {
      final String file_path = path + leaf_name;
      new WriterEIG(FrEnd.node_manager).write(file_path);
      FrEnd.setFilePath("file://" + file_path);
    }
  }

  private void chooseSaveAsPov() {
    final FileDialog fd = new FileDialog(this.frame.getAppletFrame(),
      "Save .POV file as:", FileDialog.SAVE);

    final String leaf = getLeaf(FrEnd.last_file_path);
    fd.setFile(ensureExtension(leaf, "pov"));

    fd.setFilenameFilter(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        Forget.about(dir);
        return !(name.endsWith("pov"));
      }
    });

    fd.setVisible(true);
    final String path = fd.getDirectory();
    final String leaf_name = fd.getFile();
    if (isAcceptableFileName(leaf_name)) {
      final String file_path = path + leaf_name;
      new WriterPOV(FrEnd.node_manager).write(file_path);
    }
  }

  private String ensureExtension(String leaf, String extension) {
    final int len = leaf.length();
    final int idx = leaf.lastIndexOf(".");
    if (idx > len - 6) {
      return leaf.substring(0, idx) + "." + extension;
    }
    
    return leaf + "." + extension;
  }

  private boolean isAcceptableFileName(String name) {
    if (name == null) {
      return false;
    }
    if ("".equals(name)) {
      return false;
    }

    return true;
  }

  private void chooseQuit() {
    this.frame.getApplet().stop();
    System.exit(0);
  }

  private String getLeaf(String path) {
    final int i = path.lastIndexOf("/") + 1;
    return path.substring(i);
  }
}