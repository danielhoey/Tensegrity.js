//This program has been placed into the public domain by its author.

package com.springie.gui.components;

import java.awt.Panel;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.swing.JOptionPane;

import com.springie.FrEnd;
import com.springie.utilities.FilePath;
import com.springie.utilities.general.Forget;
import com.springie.utilities.log.Log;

public class DropablePanel extends Panel {
  private DropTargetListener dtListener;

  private int acceptableActions = DnDConstants.ACTION_COPY;

  public DropablePanel() {
    int a = DnDConstants.ACTION_COPY_OR_MOVE;
    if (a != DnDConstants.ACTION_NONE && a != DnDConstants.ACTION_COPY
      && a != DnDConstants.ACTION_MOVE && a != DnDConstants.ACTION_COPY_OR_MOVE
      && a != DnDConstants.ACTION_LINK) {
      throw new IllegalArgumentException("action" + a);
    }

    this.acceptableActions = a;
    this.dtListener = new DTListener();

    new DropTarget(this, this.acceptableActions, this.dtListener, true);
  }

  class DTListener implements DropTargetListener {
    private boolean isDragFlavorSupported(DropTargetDragEvent e) {
      Forget.about(e);
      return true;
    }

    private DataFlavor chooseDropFlavor(DropTargetDropEvent e) {
      Forget.about(e);

      DataFlavor chosen = null;

      //if (e.isDataFlavorSupported(DataFlavor.plainTextFlavor)) {
      chosen = DataFlavor.javaFileListFlavor;
      //}

      return chosen;
    }

    private boolean isDragOk(DropTargetDragEvent e) {
      if (!isDragFlavorSupported(e)) {
        Log.log("isDragOk:no flavors chosen");
        return false;
      }

      final int da = e.getDropAction();
      //Log.put("dt drop action " + da);
      //Log.log(" my acceptable actions " + DropablePanel.this.acceptableActions);

      if ((da & DropablePanel.this.acceptableActions) == 0) {
        return false;
      }
      return true;
    }

    public void dragEnter(DropTargetDragEvent e) {
      //Log.log("Drag: dragEnter");
      if (!isDragOk(e)) {
        Log.log("Drag: enter not ok");
        e.rejectDrag();
        return;
      }
//    Log.log("dt enter: accepting " + e.getDropAction());
      e.acceptDrag(e.getDropAction());
    }

    public void dragOver(DropTargetDragEvent e) {
      if (!isDragOk(e)) {
        System.out.println("dtlistener dragOver not ok");
        e.rejectDrag();
        return;
      }
//    Log.log("dt over: accepting");
      e.acceptDrag(e.getDropAction());
    }

    public void dropActionChanged(DropTargetDragEvent e) {
      if (!isDragOk(e)) {
        Log.log("dtlistener changed not ok");
        e.rejectDrag();
        return;
      }
      Log.log("dt changed: accepting" + e.getDropAction());
      e.acceptDrag(e.getDropAction());
    }

    public void dragExit(DropTargetEvent e) {
      Forget.about(e);
      //Log.log("dtlistener dragExit");
    }

    public void drop(DropTargetDropEvent e) {
      //Log.log("dtlistener drop");

      final DataFlavor chosen = chooseDropFlavor(e);
      if (chosen == null) {
        Log.log("No flavor match found");
        e.rejectDrop();
        return;
      }

      //Log.log("Chosen data flavor is " + chosen.getMimeType());

      //Log.log("chosen:" + chosen);
      //Log.log("DropTargetDropEvent:" + e);
      //Log.log("getSource:" + e.getSource());

      final Transferable tr = e.getTransferable();
      //Log.log("Transferable:" + tr.toString());

      //final int da = e.getDropAction();
      final int sa = e.getSourceActions();
      //Log.log("drop: sourceActions: " + sa);
      //Log.log("drop: dropAction: " + da);

      if ((sa & DropablePanel.this.acceptableActions) == 0) {
        Log.log("No action match found");
        e.rejectDrop();
        return;
      }

      Object data = null;
      try {
        e.acceptDrop(DropablePanel.this.acceptableActions);
        // e.acceptDrop(DnDConstants.ACTION_MOVE);
        //e.acceptDrop(DnDConstants.ACTION_COPY);
        //e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);

        try {
          data = tr.getTransferData(chosen);
        } catch (UnsupportedFlavorException e1) {
          reportException(e, e1);
          return;
        } catch (IOException e1) {
          reportException(e, e1);
          return;
        }
        if (data == null) {
          throw new NullPointerException();
        }
      } catch (NullPointerException t) {
        reportException(e, t);
        return;
      }
      //System.out.println("Got data: " + data.getClass().getName());

      //if (data instanceof String) {
      //final String s = (String) data;
      //} else
      if (data instanceof java.util.List) {
        final java.util.List julist = (java.util.List) data;
        //Log.log("File:" + julist);

        final File file = (File) julist.get(0);
        //final String path = file.getAbsolutePath();
        final String path = file.getParent()
          + System.getProperty("file.separator");
        final String filename = file.getName();
        final FilePath filepath = new FilePath(path, filename);
        //Log.log("File:" + filepath);
        FrEnd.loadFile(filepath);
      } else if (data instanceof InputStream) {
        final InputStream input = (InputStream) data;
        InputStreamReader isr = null;
        try {
          isr = new InputStreamReader(input, "Unicode");
        } catch (UnsupportedEncodingException uee) {
          isr = new InputStreamReader(input);
        }

        final StringBuffer str = new StringBuffer();
        int in = -1;
        try {
          while ((in = isr.read()) >= 0) {
            if (in != 0) {
              str.append((char) in);
            }
          }
        } catch (IOException ioe) {
          Log.log("cannot read" + ioe);
          e.dropComplete(false);
          final String message = "Bad drop\n" + ioe.getMessage();
          JOptionPane.showMessageDialog(DropablePanel.this, message, "Error",
            JOptionPane.ERROR_MESSAGE);
          return;
        }

      } else {
        Log.log("drop: rejecting");
        e.dropComplete(false);
        return;
      }

      e.dropComplete(true);
    }

    private void reportException(DropTargetDropEvent e, Exception t) {
      Log.log("Couldn't get transfer data: " + t.getMessage());
      t.printStackTrace();
      e.dropComplete(false);
    }
  }
}