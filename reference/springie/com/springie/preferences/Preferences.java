// This program has been placed into the public domain by its author.

package com.springie.preferences;

import java.util.Hashtable;

import com.springie.FrEnd;

public class Preferences {
  public String key_output_pov_ground = "output.pov.ground";

  public String key_output_pov_sky = "output.pov.sky";

  public String key_update_animation_when_pointer_over = "update.animation.when.pointer.over";

  public Hashtable map = new Hashtable();

  public Preferences() {
    this.map.put(this.key_output_pov_ground, "water");
    this.map.put(this.key_output_pov_sky, "cirrus");
    this.map.put(this.key_update_animation_when_pointer_over, new Boolean(FrEnd.viewer));
  }
}