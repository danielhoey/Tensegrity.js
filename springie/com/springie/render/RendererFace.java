// This code has been placed into the public domain by its author
package com.springie.render;

public class RendererFace {
  public CachedFace[] array;

  public void ensureCapacity(int size) {
    if (this.array == null || (this.array.length != size)) {
      this.array = new CachedFace[size];
      for (int i = size; --i >= 0;) {
        this.array[i] = new CachedFace();
      }
    }
  }
  
  public void clear() {
    this.array = null;
  }
}