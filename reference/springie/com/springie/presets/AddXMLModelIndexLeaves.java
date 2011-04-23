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

import com.springie.gui.components.ChoiceWithDescription;
import com.springie.io.in.ResourceLoader;
import com.springie.utilities.general.Forget;

public class AddXMLModelIndexLeaves extends DefaultHandler {

  ChoiceWithDescription choice;

  int index;

  Vector directories = new Vector();

  private String name;
  private String desc;

  public String addLeaves(ChoiceWithDescription choice, String source)
    throws IOException, SAXException {

    final XMLReader xr = new com.tifsoft.xml.driver.Driver();

    final AddXMLModelIndexLeaves handler = new AddXMLModelIndexLeaves();
    xr.setContentHandler(handler);
    xr.setErrorHandler(handler);
    handler.choice = choice;

    final Reader reader = new ResourceLoader().getReader(source);
    xr.parse(new InputSource(reader));

    return "";
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

      this.name = "unk";
      this.desc = "unk";
      
      final int n = atts.getLength();
      for (int i = 0; i < n; i++) {
        final String nam = atts.getLocalName(i);
        final String val = atts.getValue(i);

        if (node) {
          if ("name".equals(nam)) {
            this.directories.addElement(val);
            //Log.log("ADD DIR" + val);
          }
        }
        if (leaf) {
          if ("name".equals(nam)) {
            this.name = val;
          }
          if ("desc".equals(nam)) {
            this.desc = val;
          }
        }
      }
      
      if (leaf) {
        String path = "";
        for (int j = 0; j < this.directories.size(); j++) {
          path += (String) this.directories.elementAt(j) + "/";
        }
        path += "" + this.name;
        //Log.log("Add:" + this.desc + " -> " + path);

        this.choice.add(this.desc, path);
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
    //...
  }
}