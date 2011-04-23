// * Read in information from files...

package com.springie.io.in.readers.rbf;

import java.util.StringTokenizer;
import java.util.Vector;

import com.springie.presets.ColourFactory;
import com.springie.utilities.log.Log;

public class ReaderRBF {
  public static String translate(String in) {
    final StringTokenizer st = new StringTokenizer(in);

    final StringBuffer out = parseTheFile(st);

    return out.toString();
  }

  private static StringBuffer parseTheFile(final StringTokenizer st) {
    final int sf = 32000;
    final int[] colours = new ColourFactory(65387).getColourArray(64);
    int last_link_group = -1;
    boolean waiting_for_hidden = false;
    final Vector v = new Vector();
    final Vector h = new Vector();
    final StringBuffer out = new StringBuffer();
    String tok;
    out.append("CR NG R:0 C:0x0 ");
    do {
      tok = st.nextToken();
      switch (tok.charAt(0)) {
        case 'J':
          if (tok.length() < 6) {
            final String st_x = st.nextToken();
            if (isANumber(st_x)) {
              final String st_y = st.nextToken();
              if (isANumber(st_y)) {
                final String st_z = st.nextToken();
                if (isANumber(st_z)) {
                  final int x = (int) (Double.valueOf(st_x).doubleValue() * sf);
                  final int y = (int) (Double.valueOf(st_y).doubleValue() * sf);
                  final int z = (int) (Double.valueOf(st_z).doubleValue() * sf);

                  out.append("N X:" + x + " Y:" + y + " Z:" + z + " ");
                  //out.append("DX:1 DY:2 DZ:3 ");
                  waiting_for_hidden = false;
                  Log.log("N X:" + x + " Y:" + y + " Z:" + z + " ");
                }
              }
            }
          }
          break;

        case 'S':
          if (tok.length() > 1) {
            final String st_num = tok.substring(1);
            if (isANumber(st_num)) {
              final int num = Integer.parseInt(st_num);
              if (num != last_link_group) {
                last_link_group = num;
                final int length = getLength(v, num);
                out.append("LG L:" + length + " ");
                out.append("E:62 C:0xFF");
                final int colour = (colours[num % 7] + num) | 0x808080;
                out.append("" + Integer.toString(colour, 16) + " ");
                final String hidden = getHidden(h, num);
                if ("1".equals(hidden)) {
                  out.append("H:0 ");
                }
              }

              final String st_n1 = st.nextToken();
              if (isANumber(st_n1)) {
                final int n1 = Integer.parseInt(st_n1);

                final String st_n2 = st.nextToken();
                if (isANumber(st_n2)) {
                  final int n2 = Integer.parseInt(st_n2);

                  out.append("LK V:" + n1 + " V:" + n2 + " ");
                  waiting_for_hidden = false;
                }
              }
            }
          }
          break;
        case 'G':
          if (tok.length() < 4) {
            final String st_l = st.nextToken();
            if (isANumber(st_l)) {
              final int l = (int) ((Double.valueOf(st_l).doubleValue() * sf));
              v.addElement(new Integer(l));
              h.addElement("0");
              waiting_for_hidden = true;
            }
          }
          break;

        case 'H':
          if (waiting_for_hidden) {
            final int cv = h.size();
            h.setElementAt("1", cv - 1);
            waiting_for_hidden = false;
          }

          break;

        default:
          // do nothing..
          break;
      }
    } while (!tok.equals("RBF_End"));
    
    return out;
  }

  private static String getHidden(final Vector h, final int num) {
    if (num >= h.size()) {
      return "0";
    }

    final String hidden = (String) h.elementAt(num);
    return hidden;
  }

  private static int getLength(final Vector v, final int num) {
    if (num >= v.size()) {
      return 10;
    }

    final int length = ((Integer) v.elementAt(num)).intValue();
    return length;
  }

  static boolean isANumber(String s) {
    for (int i = 0; i < s.length(); i++) {
      final char c = s.charAt(i);
      if (!couldBeInANumber(c)) {
        return false;
      }
    }
    return true;
  }

  static boolean couldBeInANumber(char c) {
    if (c == '-') {
      return true;
    }
    if (c == '.') {
      return true;
    }
    if (c > '9') {
      return false;
    }
    if (c < '0') {
      return false;
    }
    return true;
  }
}