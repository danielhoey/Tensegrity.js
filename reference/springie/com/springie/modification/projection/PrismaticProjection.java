// This code has been placed into the public domain by its author

package com.springie.modification.projection;

import java.io.IOException;

import org.xml.sax.SAXException;

import com.springie.FrEnd;
import com.springie.elements.links.LinkManager;
import com.springie.elements.nodes.NodeManager;
import com.springie.io.in.readers.spr.ReaderSPR;
import com.springie.io.out.Serialiser;
import com.tifsoft.utilities.execute.Executor;

public class PrismaticProjection {
  NodeManager node_manager;

  LinkManager link_manager;

  float sf;

  public PrismaticProjection(NodeManager node_manager) {
    this.node_manager = node_manager;
    this.link_manager = node_manager.link_manager;
  }

  public void project(float sf) {
    this.sf = sf;

    final Serialiser serialiser = new Serialiser(this.node_manager);
    final String serialised = serialiser.toString();

    //Log.log(serialised);

    String tokenised = null;
    try {
      tokenised = new ReaderSPR().translateString(serialised);
    } catch (IOException e1) {
      e1.printStackTrace();
    } catch (SAXException e1) {
      e1.printStackTrace();
    }

    final Executor e = new Executor() {
      public Object execute(Object o) {
        final NodeManager node_manager = (NodeManager) o;

        final RescaleManager rescale_manager = new RescaleManager(node_manager);

        rescale_manager.setup();
        rescale_manager.performRescale(getScaleFactor());

        new MakeSlightAdjustmentsToClazzes(node_manager).adjust();

        return null;
      }
    };

    //Log.log(tokenised);

    FrEnd.data_input.processBuffer(tokenised.toCharArray(), e);
  }

  float getScaleFactor() {
    return 1F + this.sf;
  }
}