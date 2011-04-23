// This code has been placed into the public domain by its author
package com.springie.render;

public class RendererLink {
  public CachedLink[] array;

  public void ensureCapacity(int size) {
    if (this.array == null || (this.array.length != size)) {
      this.array = new CachedLink[size];
      for (int i = size; --i >= 0;) {
        this.array[i] = new CachedLink();
      }
    }
  }
  
  public void clear() {
    this.array = null;
  }
}