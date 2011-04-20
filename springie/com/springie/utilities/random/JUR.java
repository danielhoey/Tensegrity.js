package com.springie.utilities.random;

public class JUR {
  static final long serialVersionUID = 3905348978240129619L;

  private long seed;

  private static final long multiplier = 0x5DEECE66DL;

  private static final long mask = (1L << 48) - 1;

  public JUR() {
    this(System.currentTimeMillis());
  }

  public JUR(long seed) {
    setSeed(seed);
  }

  void setSeed(long seed) {
    this.seed = (seed ^ multiplier) & mask;
    this.haveNextNextGaussian = false;
  }

  protected int next(int bits) {
    final long addend = 0xBL;
    long nextseed = (this.seed * multiplier + addend) & mask;
    this.seed = nextseed;
    return (int) (nextseed >>> (48 - bits));
  }

  void nextBytes(byte[] bytes) {
    final int BITS_PER_BYTE = 8;
    final int BYTES_PER_INT = 4;
    final int numRequested = bytes.length;

    int numGot = 0;
    int rnd = 0;

    while (true) {
      for (int i = 0; i < BYTES_PER_INT; i++) {
        if (numGot == numRequested)
          return;

        rnd = i == 0 ? next(BITS_PER_BYTE * BYTES_PER_INT)
          : rnd >> BITS_PER_BYTE;
        bytes[numGot++] = (byte) rnd;
      }
    }
  }

  public int nextInt() {
    return next(32);
  }

  public int nextInt(int n) {
    if (n <= 0)
      throw new IllegalArgumentException("n must be positive");

    if ((n & -n) == n)
      return (int) ((n * (long) next(31)) >> 31);

    int bits;
    int val;
    do {
      bits = next(31);
      val = bits % n;
    } while (bits - val + (n - 1) < 0);
    return val;
  }

  long nextLong() {

    return ((long) (next(32)) << 32) + next(32);
  }

  boolean nextBoolean() {
    return next(1) != 0;
  }

  float nextFloat() {
    final int i = next(24);
    return i / ((float) (1 << 24));
  }

  double nextDouble() {
    final long l = ((long) (next(26)) << 27) + next(27);
    return l / (double) (1L << 53);
  }

  char nextChar() {

    return (char) (next(16));
  }

  short nextShort() {
    return (short) (next(16));
  }

  byte nextByte() {
    return (byte) (next(8));
  }

  private double nextNextGaussian;

  private boolean haveNextNextGaussian;

  double nextGaussian() {

    if (this.haveNextNextGaussian) {
      this.haveNextNextGaussian = false;
      return this.nextNextGaussian;
    }
    double v1;
    double v2;
    double s;
    do {
      v1 = 2 * nextDouble() - 1; // between -1 and 1
      v2 = 2 * nextDouble() - 1; // between -1 and 1
      s = v1 * v1 + v2 * v2;
    } while (s >= 1);
    final double multiplier = Math.sqrt(-2 * Math.log(s) / s);
    this.nextNextGaussian = v2 * multiplier;
    this.haveNextNextGaussian = true;
    return v1 * multiplier;
  }

  String returnName() {
    return "java.util.Random";
  }
}