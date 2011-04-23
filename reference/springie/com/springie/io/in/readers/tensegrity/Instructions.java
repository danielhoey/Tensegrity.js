//* Read in information from files...

package com.springie.io.in.readers.tensegrity;

public class Instructions {

  static final String[] INS_NAMES = {
    "N",  // 0 // Node
    "X",  // 1
    "Y",  // 2
    "Z",  // 3
    "DX", // 4
    "DY", // 5
    "DZ", // 6
    "R",  // 7 // radius
    "M",  // 8 // mass
    "LK", // 9 // link
    "E",  // 10 // Elasticity
    "DA", // 11 // Damping
    "S",  // 12 // Status
    "L",  // 13 // Length
    "CA", // 14 // Cable
    "CR", // 15 // Creature
    "D",  // 16 // Disabled
    "FX", // 17 // Fixed
    "F",  // 18 // ??
    "NG", // 19 // Node group
    "LG", // 20 // Link group
    "GO", // 21 // Gravity active
    "GS", // 22 // Gravity strength
    "PG", // 23 // Face group
    "P",  // 24 // Face
    "C",  // 25 // Colour
    "V",  // 26 // Value
    "H",  // 27 // Hidden
    "DIM",// 28 // Dimensions
    "CO", // 29 // Charge active
    "CH", // 30 // Charge
    "G",  // 31 // Gravity
//    "A",  // 37 // Amplitude...
    //"PH", // 38 // Phase?
//  "CS", // 29 // Charge strength
  };

  static final int N   = 0;
  static final int X   = 1;
  static final int Y   = 2;
  static final int Z   = 3;
  static final int DX  = 4;
  static final int DY  = 5;
  static final int DZ  = 6;
  static final int R   = 7;
  static final int M   = 8;
  static final int LK  = 9;
  static final int E   = 10;
  static final int DA  = 11;
  static final int S   = 12;
  static final int L   = 13;
  static final int CA  = 14;
  static final int CR  = 15;
  static final int D   = 16;
  static final int FX  = 17;
  static final int F   = 18;
  static final int NG  = 19;
  static final int LG  = 20;
  static final int GO  = 21;
  static final int GS  = 22;
  static final int PG  = 23;
  static final int P   = 24;
  static final int C   = 25;
  static final int V   = 26;
  static final int H   = 27;
  static final int DIM = 28;
  static final int CO  = 29;
  static final int CH  = 30;
  static final int G   = 31;
  //static final int CS  = 28;
  //static final int A   = 37;
  //static final int PH  = 38;
}                        