// * Read in information from files...

package com.springie.io.in.readers.spr;

import com.springie.elements.nodes.NodeManager;
import com.springie.modification.resize.ScaleToFitScreen;
import com.springie.modification.translation.CentreOnScreen;
import com.tifsoft.utilities.execute.Executor;

public class ReaderSPRExecutor implements Executor {
  public Object execute(Object o) {
    final NodeManager node_manager = (NodeManager) o;

    ScaleToFitScreen.scale(node_manager);
    CentreOnScreen.centre(node_manager);

    return null;
  }
}