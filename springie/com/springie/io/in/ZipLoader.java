package com.springie.io.in;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.springie.utilities.log.Log;

public class ZipLoader {
  private final String zip_file_name = "index.spr";
  
  public String getZIPURLAsString(String location) {
    final InputStream in = getResourceFromURL(location);
    if (in == null) {
      throwFileNotFoundException(location);
    }

    final byte[] zip_contents = getByteArrayFromInputStream(in);

    return getZipContents(zip_contents, this.zip_file_name);
  }

  public String getZIPFileAsString(String location) {
    final InputStream in = getInputStreamFromFilename(location);
    if (in == null) {
      throwFileNotFoundException(location);
    }

    final byte[] zip_contents = getByteArrayFromInputStream(in);

    return getZipContents(zip_contents, this.zip_file_name);
  }

  private void throwFileNotFoundException(String location) {
    throw new RuntimeException("File not found: <" + location + ">");
  }

  InputStream getInputStreamFromFilename(String location) {
    try {
      return new FileInputStream(location);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return null;
  }

  private InputStream getResourceFromURL(String location) {
    Log.log("ZipLoader:getResourceFromURL:" + location);

    URL url = null;
    try {
      url = new URL(location);
    } catch (MalformedURLException e) {
      Log.log("getResourceFromURL:" + location);
      e.printStackTrace();
    }
    try {
      return url.openStream();
    } catch (IOException e1) {
      Log.log("getResourceFromURL:" + location);
      e1.printStackTrace();
    }

    return null;
  }

  private byte[] getByteArrayFromInputStream(final InputStream in) {
    byte[] output = null;

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

      output = bytes.toByteArray();

      in.close();
    } catch (IOException e) {
      e.printStackTrace();
      Log.error("" + e);
    }
    return output;
  }

  String getZipContents(byte[] zip_contents, String name) {
    byte[] _array2;
    int _array2_size;

    ByteArrayOutputStream bytes2;

    ZipEntry entry;

    ByteArrayInputStream bais;
    ZipInputStream zis;

    try {
      bais = new ByteArrayInputStream(zip_contents);
      zis = new ZipInputStream(bais);
      do {
        entry = zis.getNextEntry();
      } while (!entry.getName().equals(name));

      bytes2 = new ByteArrayOutputStream();
      _array2_size = 1024; // choose a size...
      _array2 = new byte[_array2_size];

      int rb;

      while ((rb = zis.read(_array2, 0, _array2_size)) > -1) {
        bytes2.write(_array2, 0, rb);
      }

      bytes2.close();

    } catch (IOException e) {
      Log.log(e.getMessage());
      return null;
    }

    return bytes2.toString();
  }
}