package com.springie.presets;

import com.springie.utilities.random.JUR;

public class ColourFactory {
  final JUR rnd;

  public ColourFactory(int seed) {
    this.rnd = new JUR(seed);
  }

  public int[] getColourArray(int n) {

    final int[] colours = new int[n];
    for (int i = n; --i >= 0;) {
      colours[i] = this.rnd.nextInt() & 0xFFFFFF;
    }

    return colours;
  }
}