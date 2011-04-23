package com.springie.io.in;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;

import com.springie.FrEnd;
import com.springie.utilities.log.Log;

public class ResourceLoader {
  public String getResourceAsString(Class base, String name) {
    String output;

    //Log.log("Starting to load " + name + ".");
    final InputStream in = getInputStream(base, name);
    if (in == null) {
      Log.log("Base class: <" + base + ">");
      throw new RuntimeException("File not found: <" + name + ">");
    }

    output = getStringFromInputStream(in);

    return output;
  }

  private String getStringFromInputStream(final InputStream in) {
    String output = null;
    ByteArrayOutputStream bytes;

    bytes = new ByteArrayOutputStream();
    final int array_size = 1024; // choose a size...
    final byte[] array = new byte[array_size];

    int rb;

    try {
      while ((rb = in.read(array, 0, array_size)) > -1) {
        bytes.write(array, 0, rb);
      }

      bytes.close();

      output = bytes.toString();

      in.close();
    } catch (IOException e) {
      e.printStackTrace();
      Log.error("" + e);
    }
    return output;
  }

  String getStringFromReader(Reader r) {
    final StringBuffer output = new StringBuffer();

    final int array_size = 1024; // choose a size...
    final char[] array = new char[array_size];

    int rb;

    try {
      while ((rb = r.read(array, 0, array_size)) > -1) {
        output.append(array, 0, rb);
      }

      r.close();
    } catch (IOException e) {
      e.printStackTrace();
      Log.error("" + e);
    }

    return output.toString();
  }

  private InputStream getInputStream(Class base, String location) {
    if (isURL(location)) {
      return getResourceFromURL(location);
    }

    return base.getResourceAsStream(location);
  }

  private InputStream getResourceFromURL(String location) {
    URL url = null;
    try {
      url = new URL(location);
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    try {
      return url.openStream();
    } catch (IOException e1) {
      e1.printStackTrace();
    }

    return null;
  }

  private String getResourceAsStringHelper(Class base, String location) {
    if (isResource(location)) {
      return getResourceAsString(base, location.substring(11));
    }

    if (isArchive(location)) {
      return new ZipLoader().getZIPURLAsString(location);
    }

    return getResourceAsString(base, location);
  }

  public Reader getReader(String location) {
    if (isFile(location)) {
      if (isArchive(location)) {
        final String s = new ZipLoader().getZIPFileAsString(location.substring(7));

        return new StringReader(s);
      }
      try {
        return new FileReader(location.substring(7));
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }

    if (isResource(location)) {
      final String s = getResourceAsStringHelper(FrEnd.class, location);

      return new StringReader(s);
    }

    if (isURL(location)) {
      if (isArchive(location)) {
        final String s = new ZipLoader().getZIPURLAsString(location);

        return new StringReader(s);
      }
      final String s = getURLAsString(location);

      return new StringReader(s);
    }

    return null;
  }

  private String getURLAsString(String url) {
    final InputStream is = getResourceFromURL(url);
    return getStringFromInputStream(is);
  }

  boolean isURL(String location) {
    return location.startsWith("http://");
  }

  boolean isArchive(String location) {
    return location.endsWith(".zip");
  }

  boolean isFile(String location) {
    return location.startsWith("file://");
  }

  boolean isResource(String location) {
    return location.startsWith("resource://");
  }
}