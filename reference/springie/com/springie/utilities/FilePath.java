package com.springie.utilities;

public class FilePath {
  public String path;

  public String filename;

  public FilePath(String path, String filename) {
    this.path = path;
    this.filename = filename;
  }

  public String toString() {
    return this.path + this.filename;
  }
}