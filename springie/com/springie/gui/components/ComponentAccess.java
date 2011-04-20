// This program has been placed into the public domain by its author.

package com.springie.gui.components;

import java.awt.Component;

public class ComponentAccess {
  public static void setAccess(Component c, boolean v) {
    if (c.isEnabled() != v) {
      c.setEnabled(v);
    }
  }
}