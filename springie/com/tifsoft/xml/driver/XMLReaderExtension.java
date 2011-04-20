package com.tifsoft.xml.driver;

public interface XMLReaderExtension extends org.xml.sax.XMLReader {
  String getNamespace(String tag);
  String getDefaultNamespace();
  void setContentHandler(ContentHandlerExtension handler);
}