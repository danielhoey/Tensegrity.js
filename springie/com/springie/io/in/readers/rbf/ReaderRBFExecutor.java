// * Read in information from files...

package com.springie.io.in.readers.rbf;

import com.springie.elements.nodes.NodeManager;
import com.springie.io.in.AutomaticNodeColourer;
import com.springie.modification.post.PostModification;
import com.springie.modification.resize.ScaleToFitScreen;
import com.springie.modification.translation.CentreOnScreen;
import com.tifsoft.utilities.execute.Executor;

public class ReaderRBFExecutor implements Executor {
  public Object execute(Object o) {
    final NodeManager node_manager = (NodeManager) o;

    ScaleToFitScreen.scale(node_manager);
    CentreOnScreen.centre(node_manager);

    new PostModification(node_manager).cleanup();

    new AutomaticNodeColourer(node_manager).execute();

    return null;
  }
}