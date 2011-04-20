package com.tifsoft.xml.writer;

public class XMLWriterUtilities {
  public static void indent(StringBuffer sb, int indent) {
    sb.append(repeat(' ', indent));
  }

  public static String repeat(char character, int count) {
    final StringBuffer stringBuffer = new StringBuffer(count);
    for (int i = 0; i < count; i++) {
      stringBuffer.append(character);
    }
    return stringBuffer.toString();
  }

}