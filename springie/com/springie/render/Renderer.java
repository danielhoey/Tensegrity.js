// This code has been placed into the public domain by its author
package com.springie.render;

public class Renderer {
  public RendererNode renderer_node = new RendererNode();
  public RendererLink renderer_link = new RendererLink();
  public RendererFace renderer_face = new RendererFace();

  public int mask = 0xFF00FFFF;
  
  public void setMask(int mask) {
    this.mask = mask;
  }
  
  public void clear() {
    this.renderer_node.clear();
    this.renderer_link.clear();
    this.renderer_face.clear();
  }
}