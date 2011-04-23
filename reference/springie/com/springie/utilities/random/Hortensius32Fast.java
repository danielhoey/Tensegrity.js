package com.springie.utilities.random;

/**
 * Hortensius32FastFast:
 * <P>
 * <b>Hortensius32Fast</b> is a drop-in subclass replacement
 * for java.util.Random.  It is *NOT* properly synchronised and
 * cannot be used in a multithreaded environment without care.
 *
 * <p><b>Hortensius32FastFast</b> is not a subclass of java.util.Random.  It has
 * the same methods as Random does, however, and it is
 * algorithmically identical to Hortensius32Fast.  Hortensius32FastFast
 * has hard-code inlined all of its methods directly, and made all of them
 * final (well, the ones of consequence anyway).  Further, these
 * methods are <i>not</i> synchronized, so the same Hortensius32FastFast
 * instance cannot be shared by multiple threads.  But all this helps
 * Hortensius32FastFast achieve over twice the speed of Hortensius32Fast.
 *
 * next(int bits) >>>'s by (32-bits) to get a value ranging
 * between 0 and 2^bits-1 long inclusive; hope that's correct.
 * setSeed(seed) set initial values to the working area
 * of 624 words. For setSeed(seed), seed is any 32-bit integer
 * <b>except for 0</b>.
 *
 * This library is free software; you can redistribute it and or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later
 * version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Library General License for more details.
 * You should have received a copy of the GNU Library General
 * License along with this library; if not, write to the
 * Free Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307  USA
 *
 * <p><b>Important Note on Seeds. </b> Just like java.util.Random, this
 * generator accepts a long seed but doesn't use all of it.  java.util.Random
 * uses 48 bits.  The Mersenne Twister instead uses 32 bits (int size).
 *
 * <p><b>Different Java Versions.</b>  Hortensius32Fast can be used reliably
 * on JDK version 1.1.5 or above.  Earlier problems have serious bugs in
 * random number generation.
 *
 * <i>Important note:</i>  Do not Hortensius32Fast.java or java.util.Random on a Java version this early!
 * Random number generation in versions less than 1.1.5 has serious bugs.
 * </dl>
 *
 */

public class Hortensius32Fast extends JUR { // implements Serializable {
  // Tempering parameters

  int integer_seed;

  // a good initial seed (of int size, though stored in a long)
  private static final long GOOD_SEED = 4357;

  /**
   * Constructor using the default seed.
   */
  public Hortensius32Fast() {
    super(GOOD_SEED);
    setSeed(GOOD_SEED);
  }

  /**
   * Constructor using a given seed.  Though you pass this seed in
   * as a long, it's best to make sure it's actually an integer.
   *
   * @param seed generator starting number, often the time of day.
   */
  Hortensius32Fast(final long seed) {
    if (seed == 0) {
      setSeed(GOOD_SEED);
    } else {
      setSeed(seed);
    }
    // super(seed);    /* just in case */

  }

  Hortensius32Fast(final int seed) {

    if (seed == 0) {
      setSeed(GOOD_SEED);
    } else {
      setSeed(seed);
    }
    // super(seed);    /* just in case */

  }

  /**
   * Initalize the pseudo random number generator.
   * The Hortensius automata only use an integer for a seed;

   * Note that for very old versions of jdk (like 1.0.2),
   * setSeed will not properly reset the gaussian mechanism,
   * so nextGaussian() may return <i>one</i> more extra
   * gaussian drawn from the old seed rather than the new one.
   *
   * @param seed from constructor
   *
   */

  public void setSeed(final long seed) {
    // this lets java.util.Random clear its nextNextGaussian field
    // Note this is broken in older jdks like 1.0.2. -- nextNextGaussian
    // will not be cleared so the very next gaussian you get *may* be drawn
    // from the old seed's generation.

    super.setSeed(seed);

    this.integer_seed = (int) seed;
  }

  /**
   * Returns an integer with <i>bits</i> bits filled with a random number.
   */
  protected int next(final int bits) {
    final int MASK = (int) Long.parseLong("11010010100101010101001010101010", 2); // wrong
    this.integer_seed = (this.integer_seed >>> 1) ^ (this.integer_seed & MASK) ^ (this.integer_seed << 1);

    return this.integer_seed >>> (32 - bits); // hope that's right!
  }

  /* If you've got a truly old version of Java, you can omit these
     two next methods. */

//  private void writeObject(ObjectOutputStream out) throws IOException {
    // just so we're .
//    out.defaultWriteObject();
//  }

//  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    // just so we're .
//    in.defaultReadObject();
//  }

  /** This method is missing from jdk 1.0.x and below.  JDK 1.1
  includes this for us, but what the heck.*/
  boolean nextBoolean() {
    return next(1) != 0;
  }

  /** This generates a coin flip with a probability <tt>probability</tt>
  of returning true, else returning false. <tt>probability</tt> must
  be between 0.0 and 1.0, inclusive. */

  boolean nextBoolean(final float probability) {
    if (probability < 0 || probability > 1)
      throw new IllegalArgumentException("probability must be between 0.0 and 1.0 inclusive.");
    return nextFloat() < probability;
  }

  /** This method is missing from JDK 1.1 and below.  JDK 1.2
  includes this for us, but what the heck. */

  public int nextInt(final int n) {
    if (n <= 0)
      throw new IllegalArgumentException("n must be positive");

    if ((n & -n) == n) // i.e., n is a power of 2
      return (int) ((n * (long) next(31)) >> 31);

    int val;
    int bits;
    do {
      bits = next(31);
      val = bits % n;
    } while (bits - val + (n - 1) < 0);
    return val;
  }

  /** A bug fix for versions of JDK 1.1 and below.  JDK 1.2 fixes
  this for us, but what the heck. */
  double nextDouble() {
    return (((long) next(26) << 27) + next(27)) / (double) (1L << 53);
  }

  /** A bug fix for versions of JDK 1.1 and below.  JDK 1.2 fixes
  this for us, but what the heck. */

  float nextFloat() {
    return next(24) / ((float) (1 << 24));
  }

  /** A bug fix for all versions of the JDK.  The JDK appears to
  use all four bytes in an integer as independent byte values!
  Totally wrong. I've submitted a bug report. */

  void nextBytes(final byte[] bytes) {
    for (int x = 0; x < bytes.length; x++)
      bytes[x] = (byte) next(8);
  }

  /** For completeness' sake, though it's not in java.util.Random.  */

  char nextChar() {
    // chars are 16-bit UniCode values
    return (char) (next(16));
  }

  /** For completeness' sake, though it's not in java.util.Random. */

  short nextShort() {
    return (short) (next(16));
  }

  /** For completeness' sake, though it's not in java.util.Random.  */

  byte nextByte() {
    return (byte) (next(8));
  }

  String returnName() {
    return "Hortensius";
  }

  /**
   * Tests the code.
   */

  /*
   static void main(String args[]) {

  }
  */

}
