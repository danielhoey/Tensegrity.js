package com.springie.utilities.log;
import java.io.PrintStream;

/**
 * Log - Tim Tyler 2001 - 2004<P>
 *
 * A simple logging class.<P>
 *
 * This code has been placed in the public domain.<P>
 * You can do what you like with it.<P>
 * Note that this code comes with no warranty.<P>
 *
 */
public class Log {
  public static final boolean debug = true;

  public static final boolean error = debug & true;
  public static final boolean warn = debug & true;
  public static final boolean info = debug & true;
  public static final boolean log = debug & true;

  public static PrintStream print_stream = System.out;

  public static PrintStream getPrintStream() {
    return print_stream;
  }

  /**
   * Log an error
   */
  public static void error(String message) {
    if (error) {
      print(print_stream, "Error: " + message);
    }
  }

  /**
   * Log a warning
   */
  public static void warn(String message) {
    if (warn) {
      print(print_stream, "Warning: " + message);
    }
  }

  /**
   * Log an informational message
   */
  public static void info(String message) {
    if (info) {
      print(print_stream, "Information: " + message);
    }
  }

  /**
   * Log a plain message
   */
  public static void log(String message) {
    if (log) {
      print(print_stream, message);
    }
  }

  /**
   * Put out a partial line
   */
  public static void put(String message) {
    if (log) {
      put(print_stream, message);
    }
  }

  public static void print(PrintStream out, String message) {
    out.println(message);
  }

  public static void put(PrintStream out, String message) {
    out.print(message);
  }
}
