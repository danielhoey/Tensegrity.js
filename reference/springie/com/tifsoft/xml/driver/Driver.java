package com.tifsoft.xml.driver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;

import com.springie.utilities.general.Forget;

public class Driver implements XMLReaderExtension, ContentHandlerExtension,
  Locator, ErrorHandler {

  private ContentHandlerExtension content_handler_extension = this;

  private org.xml.sax.ContentHandler content_handler = this;

  private ErrorHandler error_handler = this;

  private final Stack stack = new Stack();

  final ParserState state = new ParserState();

  AttributesBasic attributes_basic;

  Attributes attributes;

  BufferForXML buffer;

  public void parse(final Reader in) throws SAXException {
    final ParserState state = this.state;
    this.attributes_basic = new AttributesBasic();
    this.attributes = new AttributesExtended(this.attributes_basic);
    this.buffer = new BufferForXML(in, this);

    try {
      this.content_handler.startDocument();
      while (true) {
        final int c = this.buffer.getNextChar();
        if (c < 0) {
          return;
        }

        // start dealing with comments...
        try {
          if (state.in_element) {
            if (state.comment_stage != StateComment.OUTSIDE) {
              if (c == '-') {
                dealWithMinus();
              } else {
                dealWithNonMinus();
              }
            }
          }

          if (this.state.comment_stage == StateComment.OUTSIDE) {
            if (this.state.in_element) {
              if (this.state.element_stage == StateElement.WAITING_FOR_VALUE) {
                //Log.log("WAITING_FOR_VALUE");
                if (c == '"') {
                  dealWithQuotes2();
                } else {
                  //Log.log("Value:" + (char)c);
                  state.name += (char) c;
                }
              } else {
                switch (c) {
                  case '!':
                    dealWithExclamation();
                    break;
                  case '<':
                    dealWithLessThan();
                    break;
                  case '>':
                    dealWithGreaterThan();
                    break;
                  case '/':
                    ParserStateUpdater.endOfIdentifier(state);
                    dealWithSlash();
                    break;
                  case '"':
                    dealWithQuotes();
                    break;
                  default:
                    if (SomeStringUtilities.isXMLElementPart((char) c)) {
                      dealWithXMLElementCharacter(c);
                    } else {
                      ParserStateUpdater.endOfIdentifier(state);
                    }
                    break;
                }
              }
            } else {
              if (c == '<') {
                flushCharacters(state);
                ParserStateReset.parserStateReset(state);
              } else {
                state.output_stream.add((char) c);
              }
            }
          }

          // end of comment state...
          if (state.in_element) {
            if (state.comment_stage != StateComment.OUTSIDE) {
              if (c == '>') {
                dealWithGreaterThan2();
              }
            }
          }
        } finally {
          state.position.x++;

          if (c == '\n') {
            dealWithNewLine();
          }
          state.offset++;
        }
      }
    } catch (final IOException e) {
      this.error_handler.fatalError(new SAXParseException(e.toString(), null,
        null, state.position.y, state.position.x, e));
    } finally {
      flushCharacters(state);
      this.content_handler.endDocument();
      this.content_handler = this;
      this.content_handler_extension = this;
      this.error_handler = this;
      this.stack.removeAllElements();
    }
  }

  private void dealWithNewLine() {
    this.state.position.y++;
    this.state.position.x = 0;
  }

  private void dealWithNonMinus() {
    if (this.state.comment_stage == StateComment.MINUS_1) {
      dealWithExclamation();
    } else if (this.state.comment_stage == StateComment.MINUS_3) {
      this.state.comment_stage = StateComment.MINUS_2;
    }
  }

  private void dealWithGreaterThan2() {
    if (this.state.comment_stage == StateComment.EXCLAMATION) {
      this.state.comment_stage = StateComment.OUTSIDE;
      this.state.in_element = false;
    }
  }

  private void dealWithXMLElementCharacter(int c) {
    this.state.name += (char) c;
  }

  private void dealWithQuotes2() {
    this.state.element_stage = StateElement.WAITING_FOR_ATTRIBUTE;
    this.attributes_basic.attribute_local_names
      .addElement(this.state.name_attribute);
    this.attributes_basic.attribute_values.addElement(this.state.name);
    this.state.name_attribute = "";
    this.state.name_value = "";
    this.state.name = "";
  }

  private void dealWithMinus() {
    moveOnToNextCommentStage();
  }

  private void dealWithLessThan() throws SAXException {
    if (this.state.in_element) {
      reportError(ConstantsErrorMessages.LT_INSIDE_LT);
    }
  }

  private void dealWithExclamation() {
    this.state.comment_stage = StateComment.EXCLAMATION;
  }

  private void dealWithSlash() {
    if ("".equals(this.state.name_element)) {
      this.state.closing_element = true;
    } else {
      this.state.element_singular = true;
    }
  }

  private void dealWithGreaterThan() throws SAXException {
    final ParserState state = this.state;
    final String LNAM = "lnam";
    state.in_element = false;
    ParserStateUpdater.endOfIdentifier(state);
    if (state.closing_element) {
      this.content_handler.endElement("", LNAM, state.name_element);
      final String popped = (String) this.stack.pop();
      if (!popped.equals(state.name_element)) {
        String error = ConstantsErrorMessages.MISMATCHED + state.name_element;
        error += ConstantsErrorMessages.DOES_NOT_MATCH + popped;
        reportError(error);
      }
    } else {
      this.content_handler.startElement("", LNAM, state.name_element,
        this.attributes);
      if (!state.element_singular) {
        this.stack.push(state.name_element);
      }

      this.attributes_basic.removeAllElements();
    }
  }

  private void dealWithQuotes() throws SAXException {
    final ParserState state = this.state;
    if (state.element_stage == StateElement.WAITING_FOR_VALUE_QUOTE) {
      state.element_stage = StateElement.WAITING_FOR_VALUE;
    } else {
      reportError(ConstantsErrorMessages.MISPLACED_QUOTE);
    }
  }

  private void moveOnToNextCommentStage() {
    final ParserState state = this.state;
    if (state.comment_stage == StateComment.EXCLAMATION) {
      state.comment_stage = StateComment.MINUS_1;
    } else if (state.comment_stage == StateComment.MINUS_1) {
      state.comment_stage = StateComment.MINUS_2;
    } else if (state.comment_stage == StateComment.MINUS_2) {
      state.comment_stage = StateComment.MINUS_3;
    } else if (state.comment_stage == StateComment.MINUS_3) {
      state.comment_stage = StateComment.EXCLAMATION;
    }
  }

  private void flushCharacters(ParserState state) throws SAXException {
    final CharArrayOutputStream output_stream = state.output_stream;
    if (output_stream.getSize() > 0) {
      final char[] ca = state.output_stream.getRawArray();
      // check for white space...
      int n = output_stream.getSize();
      if (!SomeStringUtilities.isWhiteSpace(ca, n)) {
        int i = 0;
        while (ca[n - 1] <= ' ') {
          n--;
        }
        while (ca[i] <= ' ') {
          i++;
          n--;
        }
        this.content_handler
          .characters(state.output_stream.getRawArray(), i, n);
      }
    }

    state.output_stream.setSize(0);
  }

  public void parse(final InputSource source) throws SAXException, IOException {
    if (source.getCharacterStream() != null) {
      parse(source.getCharacterStream());
    } else if (source.getByteStream() != null) {
      parse(new InputStreamReader(source.getByteStream()));
    } else {
      parse(new InputStreamReader(new URL(source.getSystemId()).openStream()));
    }
  }

  public void parse(final String system_id) throws SAXException, IOException {
    parse(new InputSource(system_id));
  }

  public void setContentHandler(final org.xml.sax.ContentHandler handler) {
    if (this.content_handler == null) {
      throw new NullPointerException();
    }
    this.content_handler = handler;
  }

  public void setContentHandler(final ContentHandlerExtension handler) {
    this.content_handler_extension = (handler == null) ? this : handler;
    this.content_handler = this.content_handler_extension;
    this.content_handler.setDocumentLocator(this);
  }

  public org.xml.sax.ContentHandler getContentHandler() {
    return this.content_handler;
  }

  public void setErrorHandler(final ErrorHandler handler) {
    this.error_handler = (handler == null) ? this : handler;
  }

  public ErrorHandler getErrorHandler() {
    return this.error_handler;
  }

  public void setDocumentLocator(final Locator locator) {
    Forget.about(locator);
  }

  public void setDTDHandler(final DTDHandler handler) {
    Forget.about(handler);
  }

  public DTDHandler getDTDHandler() {
    return null;
  }

  public void setEntityResolver(final EntityResolver resolver) {
    Forget.about(resolver);
  }

  public EntityResolver getEntityResolver() {
    return null;
  }

  public String getDefaultNamespace() {
    return null;
  }

  public String getNamespace(final String tag) {
    Forget.about(tag);
    return null;
  }

  public void startDocument() throws SAXException {
    Forget.about(null);
  }

  public Writer startDocument(final Writer writer) throws SAXException {
    this.content_handler.startDocument();
    return writer;
  }

  public void endDocument() throws SAXException {
    //...
  }

  public void startPrefixMapping(final String prefix, final String uri)
    throws SAXException {
    Forget.about(prefix);
    Forget.about(uri);
  }

  public void endPrefixMapping(final String prefix) throws SAXException {
    //...
    Forget.about(prefix);
  }

  public void startElement(final String namespace_uri, final String name_local,
    final String name_q, final Attributes atts) throws SAXException {
    Forget.about(namespace_uri);
    Forget.about(name_local);
    Forget.about(name_q);
    Forget.about(atts);
    //...
  }

  public Writer startElement(final String namespace_uri,
    final String name_local, final String name_q, final Attributes atts,
    final Writer writer) throws SAXException {
    this.content_handler.startElement(namespace_uri, name_local, name_q, atts);
    return writer;
  }

  public void endElement(final String namespace_uri, final String name_local,
    final String name_q) throws SAXException {
    Forget.about(namespace_uri);
    Forget.about(name_local);
    Forget.about(name_q);
  }

  public void characters(final char[] ch, final int start, final int length)
    throws SAXException {
    Forget.about(length);
    Forget.about(ch);
    Forget.about(start);
  }

  public void ignorableWhitespace(final char[] ch, final int start,
    final int length) throws SAXException {
    Forget.about(length);
    Forget.about(ch);
    Forget.about(start);
  }

  public void processingInstruction(final String target, final String data)
    throws SAXException {
    Forget.about(target);
    Forget.about(data);
  }

  public void skippedEntity(final String name) throws SAXException {
    Forget.about(name);
  }

  public boolean getFeature(final String name)
    throws SAXNotRecognizedException, SAXNotSupportedException {
    throw new SAXNotRecognizedException(name);
  }

  public void setFeature(final String name, final boolean value)
    throws SAXNotRecognizedException, SAXNotSupportedException {
    Forget.about(value);
    throw new SAXNotSupportedException(name);
  }

  public Object getProperty(final String name)
    throws SAXNotRecognizedException, SAXNotSupportedException {
    throw new SAXNotRecognizedException(name);
  }

  public void setProperty(final String name, final Object value)
    throws SAXNotRecognizedException, SAXNotSupportedException {
    Forget.about(value);
    throw new SAXNotRecognizedException(name);
  }

  public void warning(final SAXParseException e) throws SAXException {
    Forget.about(e);
  }

  public void error(final SAXParseException e) throws SAXException {
    Forget.about(e);
  }

  public void fatalError(final SAXParseException e) throws SAXException {
    throw e;
  }

  private void fatalError(final String msg, final int line_number,
    final int column_number) throws SAXException {
    this.error_handler.fatalError(new SAXParseException(msg, null, null,
      line_number, column_number));
  }

  void reportError(String msg) throws SAXException {
    fatalError(msg, this.state.position.y, this.state.position.x);
  }

  public String getPublicId() {
    return "";
  }

  public String getSystemId() {
    return "";
  }

  public int getLineNumber() {
    return this.state.position.y;
  }

  public int getColumnNumber() {
    return this.state.position.x;
  }
}