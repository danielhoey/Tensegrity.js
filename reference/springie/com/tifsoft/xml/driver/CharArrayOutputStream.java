package com.tifsoft.xml.driver;

public class CharArrayOutputStream {
  private int max_size;

  private int size;

  private char[] array;

  static final int INCREMENT = 10; // final?

  boolean check_bounds = true;

  public CharArrayOutputStream() {
    this.max_size = 0;
    this.size = 0;
    this.array = new char[this.max_size];
  }

  CharArrayOutputStream(int max_size) {
    this.max_size = max_size;
    this.size = 0;
    this.array = new char[this.max_size];
  }

  public void add(char c) {
    makeMoreIfNeeded();

    this.array[this.size++] = c;
  }

  public void overwrite(char c, int index) {
    if (this.check_bounds) {
      checkBounds(index);
    }

    this.array[index] = c;
  }

  public int getSize() {
    return this.size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public char get(int index) {
    if (this.check_bounds) {
      checkBounds(index);
    }

    return this.array[index];
  }

  public void deleteQuickly(int index) {
    if (this.check_bounds) {
      checkBounds(index);
    }

    char temp = this.array[index];
    this.array[index] = this.array[--this.size];
    this.array[this.size] = temp;
  }

  public void delete(int index) {
    if (this.check_bounds) {
      checkBounds(index);
    }

    char temp = this.array[index];
    for (int i = index; i < (this.size - 1); i++) {
      this.array[i] = this.array[i + 1];
    }

    this.array[--this.size] = temp;
  }

  public void swap(int j, int k) {
    final char temp = get(j);
    overwrite(get(k), j);
    overwrite(temp, k);
  }

  private void checkBounds(int index) {
    if (index < 0) {
      throw new ArrayIndexOutOfBoundsException("(" + index + ")");
    }

    if (index >= this.size) {
      throw new ArrayIndexOutOfBoundsException("(" + index + " / " + this.size + ")");
    }
  }

  private void makeMoreIfNeeded() {
    if (this.size >= this.max_size) {
      makeMore();
    }
  }

  private void makeMore() {
    char[] new_array = new char[this.max_size + CharArrayOutputStream.INCREMENT];

    System.arraycopy(this.array, 0, new_array, 0, this.max_size);

    this.array = new_array;

    this.max_size += CharArrayOutputStream.INCREMENT;
  }

  // Danger, Will Robinson!
  // Low-level access is permitted here for the sake of performance...
  public char[] getRawArray() {
    return this.array;
  }
}
