// This program has been placed into the public domain by its author.

package com.springie.gui.components;

import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.springie.utilities.general.Forget;

public class AppletFrame extends Frame {
  Applet applet;

  static final String QUIT = "Quit";

  static final String LOAD_DATA = "Load objects...";

  static final String SAVE_AS_SPR = "Save .SPR file as...";

  static final String SAVE_AS_OFF = "Save .OFF file as...";

  static final String SAVE_AS_WRL = "Save .WRL file as...";

  //static final String ADDDATA = "Add objects...";

  //static final String SAVEDATAAS = "Save objects as...";

  //static final String SAVECONDENSED = "Save squeezed objects as...";

  static final String HELP = "Help...";

  static final String ABOUT = "About...";

  public AppletFrame(String title, Applet app, int width, int height) {
    super(title);
    this.applet = app;

    final MenuBarTop menubar = new MenuBarTop(this);
    setMenuBar(menubar);

    //    final Menu file = new Menu("File", true);
    //
    //    file.add(AppletFrame.LOAD_DATA);
    //    file.add(AppletFrame.SAVE_AS_SPR);
    //    file.add(AppletFrame.SAVE_AS_WRL);
    //    file.add(AppletFrame.SAVE_AS_OFF);
    //    file.add(AppletFrame.QUIT);
    //    
    //    file.addActionListener(new ActionListener() {
    //      public void actionPerformed(ActionEvent e) {
    //        final String arg = e.getActionCommand();
    //
    //        if (QUIT.equals(arg)) {
    //          chooseQuit();
    //        }
    //
    //        if (SAVE_AS_SPR.equals(arg)) {
    //          chooseSaveAsSpr();
    //        }
    //
    //        if (SAVE_AS_OFF.equals(arg)) {
    //          chooseSaveAsOff();
    //        }
    //        
    //        if (SAVE_AS_WRL.equals(arg)) {
    //          chooseSaveAsWrl();
    //        }
    //
    //        if (LOAD_DATA.equals(arg)) {
    //          chooseLoad();
    //        }
    //      }
    //    });
    //    menubar.add(file);
    //
    //    final Menu help = new Menu("Help", true);
    //    help.add(HELP);
    //    help.add(ABOUT);
    //    help.addActionListener(new ActionListener() {
    //      public void actionPerformed(ActionEvent e) {
    //        final String arg = e.getActionCommand();
    //
    //        if (equals(arg)) {
    //          getApplet().stop();
    //          System.exit(0);
    //        }
    //
    //        if (HELP.equals(arg)) {
    //          FrEnd.frame_panel_help.setVisible(true);
    //        } else if (ABOUT.equals(arg)) {
    //          FrEnd.frame_panel_about.setVisible(true);
    //        }
    //      }
    //    });
    //
    //    menubar.add(help);

    add("Center", this.applet);
    setSize(new Dimension(width, height));

    setVisible(true);

    this.applet.init();
    this.applet.start();

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        Forget.about(e);
        System.exit(0);
      }

      public void windowDeiconified(WindowEvent e) {
        Forget.about(e);
      }

      public void windowIconified(WindowEvent e) {
        Forget.about(e);
      }
    });
  }

  //        if (SAVEDATAAS.equals(arg)) {
  //          FrEnd.output_linefeeds = true;
  //          // System.out.println("DONE DataDump!");
  //          final FileDialog fd = new FileDialog(getAppletFrame(),
  //            "Save objects as", FileDialog.SAVE);
  //          // fd.setFile("tempfile.dat");
  //          fd.setFilenameFilter(new FilenameFilter() {
  //            public boolean accept(File dir, String name) {
  //              Forget.about(dir);
  //              // return false;
  //              System.out.println("Done DataDump!" + name);
  //              return !(name.endsWith(".dat"));
  //            }
  //          });
  //          fd.setVisible(true);
  //          final String returnedstring = fd.getFile();
  //          if (!returnedstring.equals("")) {
  //            FrEnd.data_output.dataDump(returnedstring);
  //          }
  //        }

  //        if (SAVECONDENSED.equals(arg)) {
  //          FrEnd.output_linefeeds = false;
  //          final FileDialog fd = new FileDialog(getAppletFrame(),
  //            "Save objects as", FileDialog.SAVE);
  //          fd.setFilenameFilter(new FilenameFilter() {
  //            public boolean accept(File dir, String name) {
  //              Forget.about(dir);
  //              // return false;
  //              System.out.println("Done DataDump!" + name);
  //              return !(name.endsWith(".dat"));
  //            }
  //          });
  //          fd.setVisible(true);
  //          final String returnedstring = fd.getFile();
  //          if (!returnedstring.equals("")) {
  //            FrEnd.data_output.dataDump(returnedstring);
  //          }
  //        }

  //        if (ADDDATA.equals(arg)) {
  //          final FileDialog fd = new FileDialog(getAppletFrame(),
  //            "Load objects", FileDialog.LOAD);
  //          fd.setFilenameFilter(new FilenameFilter() {
  //            public boolean accept(File dir, String name) {
  //              Forget.about(dir);
  //              return !(name.endsWith(".dat"));
  //            }
  //          });
  //          fd.setVisible(true);
  //
  //          final String returnedstring = fd.getFile();
  //          if (!returnedstring.equals("")) {
  //            FrEnd.data_input.addFile(returnedstring);
  //          }
  //        }

  //  private void chooseLoad() {
  //    final FileDialog fd = new FileDialog(getAppletFrame(), "Load objects",
  //      FileDialog.LOAD);
  //    // fd.setFile("tempfile.dat");
  //    fd.setFilenameFilter(new FilenameFilter() {
  //      public boolean accept(File dir, String name) {
  //        Forget.about(dir);
  //        return !(name.endsWith(".dat"));
  //      }
  //    });
  //    fd.setVisible(true);
  //    final String returnedstring = fd.getFile();
  //    fd.setVisible(false);
  //
  //    if (returnedstring != null) {
  //      if (!returnedstring.equals("")) {
  //        final FilePath fp = new FilePath(fd.getDirectory(), returnedstring);
  //        FrEnd.loadFile(fp);
  //      }
  //    }
  //  }
  //
  //  private void chooseSaveAsWrl() {
  //    final FileDialog fd = new FileDialog(getAppletFrame(),
  //      "Save .WRL file as:", FileDialog.SAVE);
  //
  //    String no_ext = getLeaf(FrEnd.last_file_path);
  //    final int index_of_dot = no_ext.indexOf(".");
  //    if (index_of_dot >= 0) {
  //      no_ext = no_ext.substring(0, index_of_dot);
  //    }
  //
  //    fd.setFile(no_ext + ".wrl");
  //    fd.setFilenameFilter(new FilenameFilter() {
  //      public boolean accept(File dir, String name) {
  //        Forget.about(dir);
  //        return !(name.endsWith(".wrl"));
  //      }
  //    });
  //    fd.setVisible(true);
  //    final String path = fd.getDirectory();
  //    final String leaf = fd.getFile();
  //    if (isAcceptableFileName(leaf)) {
  //      new WriterVRML().write(path + leaf);
  //    }
  //  }
  //
  //  private void chooseSaveAsSpr() {
  //    final FileDialog fd = new FileDialog(getAppletFrame(),
  //      "Save .SPR file as:", FileDialog.SAVE);
  //
  //    final String leaf = getLeaf(FrEnd.last_file_path);
  //    fd.setFile(leaf);
  //
  //    fd.setFilenameFilter(new FilenameFilter() {
  //      public boolean accept(File dir, String name) {
  //        Forget.about(dir);
  //        return !(name.endsWith(".spr"));
  //      }
  //    });
  //
  //    fd.setVisible(true);
  //    final String path = fd.getDirectory();
  //    final String leaf_name = fd.getFile();
  //    if (isAcceptableFileName(leaf_name)) {
  //      final String file_path = path + leaf_name;
  //      new WriterXMLSpr().write(file_path);
  //      FrEnd.setFilePath("file://" + file_path);
  //    }
  //  }
  //
  //  private void chooseSaveAsOff() {
  //    final FileDialog fd = new FileDialog(getAppletFrame(),
  //      "Save .OFF file as:", FileDialog.SAVE);
  //
  //    final String leaf = getLeaf(FrEnd.last_file_path);
  //    fd.setFile(leaf);
  //
  //    fd.setFilenameFilter(new FilenameFilter() {
  //      public boolean accept(File dir, String name) {
  //        Forget.about(dir);
  //        return !(name.endsWith("off"));
  //      }
  //    });
  //
  //    fd.setVisible(true);
  //    final String path = fd.getDirectory();
  //    final String leaf_name = fd.getFile();
  //    if (isAcceptableFileName(leaf_name)) {
  //      final String file_path = path + leaf_name;
  //      new WriterOFF(FrEnd.node_manager).write(file_path);
  //      FrEnd.setFilePath("file://" + file_path);
  //    }
  //  }
  //
  //  private boolean isAcceptableFileName(String name) {
  //    if (name == null) {
  //      return false;
  //    }
  //    if ("".equals(name)) {
  //      return false;
  //    }
  //
  //    return true;
  //  }
  //
  //  private void chooseQuit() {
  //    getApplet().stop();
  //    System.exit(0);
  //  }
  //
  //  private String getLeaf(String path) {
  //    final int i = path.lastIndexOf("/") + 1;
  //    return path.substring(i);
  //  }

  public Applet getApplet() {
    return this.applet;
  }

  public AppletFrame getAppletFrame() {
    return this;
  }
}