package com.tifsoft.xml.driver;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.xml.sax.SAXException;

public class BufferForXML extends Writer {
  static final int default_initial_buffer_size = 256;

  static final int default_buffer_increment = 256;

  static final int buffer_size = 32;

  private Driver sd;

  private int next_in;

  private int last_in;

  char[] chars;

  private final Reader in;

  private int count;

  private Writer writer = this;

  private boolean written;

  public BufferForXML(final Reader in, Driver sd) {
    this.in = in;
    this.sd = sd;
    this.chars = new char[BufferForXML.buffer_size];
  }

  public void close() throws IOException {
    flush();
  }

  public void flush() throws IOException {
    flushBuffer();
    if (this.writer != this) {
      this.writer.flush();
    }
  }

  public void write(final int c) throws IOException {
    this.written = true;
    this.chars[this.count++] = (char) c;
  }

  public void write(final char[] cbuf, final int off, final int len)
    throws IOException {
    this.written = true;
    System.arraycopy(cbuf, off, this.chars, this.count, len);
    this.count += len;
  }

  public void saveChar(final char c) {
    this.written = false;
    this.chars[this.count++] = c;
  }

  public String getString() {
    final String result = new String(this.chars, 0, this.count);

    this.count = 0;
    return result;
  }

  public void reset() {
    this.count = 0;
  }

  public int read() throws IOException {
    if (this.next_in == this.last_in) {
      if (this.count != 0) {
        if (this.written) {
          flushBuffer();
        } else {
          if (this.count >= (this.chars.length - BufferForXML.default_buffer_increment)) {
            final char[] new_chars = new char[this.chars.length
              + BufferForXML.default_buffer_increment];

            System.arraycopy(this.chars, 0, new_chars, 0, this.count);
            this.chars = new_chars;
          }
        }
      }

      final int number_read = this.in.read(this.chars, this.count,
        this.chars.length - this.count);

      if (number_read == -1) {
        return -1;
      }

      this.next_in = this.count;
      this.last_in = this.count + number_read;
    }

    return this.chars[this.next_in++];
  }

  private void flushBuffer() throws IOException {
    if (this.count != 0) {
      try {
        if (this.writer == this) {
          try {
            this.sd.getContentHandler().characters(this.chars, 0, this.count);
          } catch (final SAXException e) {
            throw new IOException(e.toString());
          }
        } else {
          this.writer.write(this.chars, 0, this.count);
        }
      } finally {
        this.count = 0;
      }
    }
  }

  public void setChars(char[] chars) {
    this.chars = chars;
  }

  public char[] getChars() {
    return this.chars;
  }

  public void setLastIn(int last_in) {
    this.last_in = last_in;
  }

  public int getLastIn() {
    return this.last_in;
  }

  public void setNextIn(int next_in) {
    this.next_in = next_in;
  }

  public int getNextIn() {
    return this.next_in;
  }

  public void setSd(Driver sd) {
    this.sd = sd;
  }

  public Driver getSd() {
    return this.sd;
  }

  public int getNextChar() throws IOException {
    if (getNextIn() == getLastIn()) {
      return read();
    }
    setNextIn(getNextIn() + 1);
    return this.chars[getNextIn() - 1];
  }
}