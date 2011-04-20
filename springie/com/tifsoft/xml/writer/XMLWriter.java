package com.tifsoft.xml.writer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.springie.utilities.log.Log;

public class XMLWriter {
  Writer out;

  public void test(String filename) {
    try {
      this.out = new FileWriter(filename);
      final XMLWriterTagPair uni = new XMLWriterTagPair("test_tag_1");
      uni.add(new XMLWriterCharacters("CHARS 1"));
      uni.add(new XMLWriterAttribute("gravity", "5"));
      uni.add(new XMLWriterCharacters("CHARS 2"));
      uni.add(new XMLWriterAttribute("gravity_active", "true"));
      uni.add(new XMLWriterCharacters("CHARS 3"));
      final XMLWriterTagPair ten = new XMLWriterTagPair("test_tag_2");
      ten.add(new XMLWriterCharacters("CHARS 4"));
      ten.add(uni);
      ten.add(new XMLWriterCharacters("CHARS 5"));

      this.out.write(ten.makeString());
      this.out.flush();
    } catch (IOException e) {
      Log.log("TT_Error (writeOutFile): " + e.toString());
    }
  }
}
