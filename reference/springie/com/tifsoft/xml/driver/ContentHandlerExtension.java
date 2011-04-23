package com.tifsoft.xml.driver;

import java.io.Writer;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

public interface ContentHandlerExtension extends org.xml.sax.ContentHandler {
  Writer startDocument(final Writer writer) throws SAXException;
  Writer startElement(final String namespace_uri, final String local_name, final String name_q, final Attributes atts, final Writer writer) throws SAXException;
}
