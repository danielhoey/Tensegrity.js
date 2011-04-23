// This program has been placed into the public domain by its author.

package com.springie.presets;

import java.io.IOException;
import java.io.Reader;
import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.springie.io.in.ResourceLoader;
import com.springie.utilities.general.Forget;
import com.springie.utilities.log.Log;

public class ReadXMLModelIndexFile extends DefaultHandler {

  String leaf = "";

  String path;

  Vector directories = new Vector();

  public String translate(String leaf, String source) throws IOException,
    SAXException {

    final XMLReader xr = new com.tifsoft.xml.driver.Driver();

    final ReadXMLModelIndexFile handler = new ReadXMLModelIndexFile();
    xr.setContentHandler(handler);
    xr.setErrorHandler(handler);
    handler.leaf = leaf;

    //Log.log("source:" + source);
    //Log.log("leaf:" + leaf);

    final Reader reader = new ResourceLoader().getReader(source);
    xr.parse(new InputSource(reader));

    Log.log("handler.path:" + handler.path);

    return handler.path;
  }

  public void startDocument() {
    //Log.log("Start document");
  }

  public void endDocument() {
    //Log.log("End document");
  }

  public void startElement(String uri, String name, String element_name,
    Attributes atts) {
    Forget.about(name);
    boolean node = false;
    boolean leaf = false;

    if ("".equals(uri)) {
      node = "node".equals(element_name);
      leaf = "leaf".equals(element_name);

      final int n = atts.getLength();
      for (int i = 0; i < n; i++) {
        final String nam = atts.getLocalName(i);
        final String val = atts.getValue(i);

        if (node) {
          if ("name".equals(nam)) {
            this.directories.addElement(val);
          }
        }
        if (leaf) {
          if ("name".equals(nam)) {
            if (this.leaf.equals(val)) {
              this.path = "";
              for (int j = 0; j < this.directories.size(); j++) {
                this.path += (String) this.directories.elementAt(j) + "/";
              }
              this.path += this.leaf;
            }
          }
        }
      }
    }
  }

  public void endElement(String uri, String name, String element_name) {
    Forget.about(uri);
    Forget.about(name);
    Forget.about(element_name);

    if ("node".equals(element_name)) {
      this.directories.removeElementAt(this.directories.size() - 1);
    }
  }

  public void ignorableWhitespace(char[] ch, int start, int length) {
    characters(ch, start, length);
  }

  public void skippedEntity(String name) {
    Forget.about(name);
  }

  public void characters(char ch[], int start, int length) {
    Forget.about(ch);
    Forget.about(start);
    Forget.about(length);
  }
}