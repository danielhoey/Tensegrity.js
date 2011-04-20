// * Read in information from files...

package com.springie.io.in;

import java.io.IOException;
import java.io.Reader;

import org.xml.sax.SAXException;

import com.springie.FrEnd;
import com.springie.collisions.BinGrid;
import com.springie.elements.faces.FaceManager;
import com.springie.elements.links.LinkManager;
import com.springie.elements.nodes.NodeManager;
import com.springie.io.in.readers.eig.ReaderEIG;
import com.springie.io.in.readers.fabric.ReaderFabric;
import com.springie.io.in.readers.m.ReaderM;
import com.springie.io.in.readers.off.ReaderOFF;
import com.springie.io.in.readers.off.ReaderOFFExecutor;
import com.springie.io.in.readers.rbf.ReaderRBF;
import com.springie.io.in.readers.rbf.ReaderRBFExecutor;
import com.springie.io.in.readers.spr.ReaderSPR;
import com.springie.io.in.readers.spr.ReaderSPRExecutor;
import com.springie.io.in.readers.tensegrity.ReaderTens;
import com.springie.modification.automaticradius.AutomaticLinkRadius;
import com.springie.modification.automaticradius.AutomaticNodeRadius;
import com.springie.modification.post.PostModification;
import com.springie.utilities.log.Log;
import com.tifsoft.utilities.execute.Executor;

public class DataInput {
  Reader in;

  public NodeManager manager_destination;

  static final boolean debug_parser = false;

  public DataInput(NodeManager manager) {
    this.manager_destination = manager;
  }

  public void loadFile(String filename) {
    resetIfNeeded();

    addFile(filename);
  }

  public void resetWorkspaces() {
    final LinkManager link_manager = this.manager_destination.link_manager;
    final FaceManager face_manager = this.manager_destination.face_manager;

    this.manager_destination.initial_reset();
    link_manager.reset();
    face_manager.reset();

    this.manager_destination.node_type_factory.array.removeAllElements();
    link_manager.link_type_factory.array.removeAllElements();
    face_manager.face_type_factory.array.removeAllElements();
    this.manager_destination.clazz_factory.array.removeAllElements();
  }

  public void addFromString(String s, NodeManager manager) {
    this.manager_destination = manager;

    final char[] temp_str = s.toCharArray();

    final Executor execute = new ReaderSPRExecutor();

    processBuffer(temp_str, execute);
  }

  public void loadFromString(String s, NodeManager m) {
    resetIfNeeded();

    addFromString(s, m);
  }

  private void resetIfNeeded() {
    if (!FrEnd.merge) {
      resetWorkspaces();
    }
  }

  void addFile(String filename) {
    final String lower = filename.toLowerCase();

    if (filename.startsWith("internal://")) {
      FrEnd.node_manager.addCreatureFromLocation(filename);
    } else if (lower.endsWith(".spr")) {
      readInSprFile(filename);
    } else if (lower.endsWith(".zip")) {
      readInSprFile(filename);
    } else if (lower.endsWith(".fabric")) {
      readInFabricFile(filename);
    } else if (lower.endsWith(".eig")) {
      readInEIGFile(filename);
    } else if (lower.endsWith(".rbf")) {
      readInRBFFile(filename);
    } else if (lower.endsWith(".off")) {
      readInOFFFile(filename);
    } else if (lower.endsWith(".m")) {
      readInMFile(filename);
    } else {
      throw new RuntimeException("Unrecognised file type");
      //readInTensegrityFile(filename);
    }
  }

  private void readInSprFile(String filename) {
    String input = null;
    try {
      input = new ReaderSPR().translate(filename);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    }

    if (FrEnd.development_version) {
      Log.log("DataInput->readInSprFile output:" + input);
    }

    if (input == null) {
      Log.log("DataInput->readInSprFile problems with:" + filename);
    }

    final Executor execute = new ReaderSPRExecutor();

    processBuffer(input.toCharArray(), execute);
  }

  private void readInEIGFile(String filename) {
    String out = null;
    try {
      out = new ReaderEIG().translate(filename);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    }
    final Executor execute = new ReaderRBFExecutor();

    processBuffer(out.toCharArray(), execute);

    resizeRadii();
  }

  private void readInFabricFile(String filename) {
    String out = null;
    try {
      out = ReaderFabric.translate(filename);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    }
    final Executor execute = new ReaderRBFExecutor();

    processBuffer(out.toCharArray(), execute);

    resizeRadii();
  }

  private void readInRBFFile(String filename) {
    final String str = getContentsOfFileAsString(filename);

    final String out = ReaderRBF.translate(str);

    final Executor execute = new ReaderRBFExecutor();

    processBuffer(out.toCharArray(), execute);
    resizeRadii();
  }

  private void readInOFFFile(String filename) {
    final String str = getContentsOfFileAsString(filename);

    final String out = ReaderOFF.translate(str);

    final Executor execute = new ReaderOFFExecutor();

    processBuffer(out.toCharArray(), execute);

    resizeRadii();
  }

  private void readInMFile(String filename) {
    final String str = getContentsOfFileAsString(filename);

    final String out = new ReaderM().translate(str);

    final Executor execute = new ReaderOFFExecutor();

    processBuffer(out.toCharArray(), execute);

    resizeRadii();
  }

//  private void readInTensegrityFile(String filename) {
//    final String str = getContentsOfFileAsString(filename);
//
//    loadFromString(str, FrEnd.node_manager);
//  }

  private String getContentsOfFileAsString(String filepath) {
    final ResourceLoader rl = new ResourceLoader();

    final Reader r = rl.getReader(filepath);

    final String s = rl.getStringFromReader(r);

    return s;
  }

  public void processBuffer(char[] buf, Executor execute) {
    final NodeManager temp_manager = new NodeManager();

    ReaderTens.interpretBuffer(temp_manager, buf, 0, 0, 0, 256);

    if (execute != null) {
      execute.execute(temp_manager);
    }

    this.manager_destination.merge(temp_manager);

    this.manager_destination.makeSureNoClazzesOrTypesAreFlagged();
    
    FrEnd.node_manager.nodes_have_been_deleted = true;

    new PostModification(this.manager_destination).thoroughCleanup();

    BinGrid.repaint_all_objects = true;

    FrEnd.reflectStatusInGUI();
  }

  void resizeRadii() {
    final AutomaticLinkRadius alr = new AutomaticLinkRadius(FrEnd.node_manager);
    alr.setInitially();
    final AutomaticNodeRadius anr = new AutomaticNodeRadius(FrEnd.node_manager);
    anr.setInitially();
  }
}