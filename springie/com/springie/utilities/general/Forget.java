package com.springie.utilities.general;

public class Forget {
  public static void about(Object o) {
    if (o == null) {
      o = null;
    }
  }

  public static void about(int i) {
    i++;
  }

  public static void about(boolean i) {
    i = !i;
  }
}
