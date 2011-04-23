package com.tifsoft.xml.driver;

import java.awt.Point;

public class ParserState {
  boolean in_element;
  boolean in_comment;
  boolean closing_element;
  boolean in_quotes;
  boolean element_singular;
  
  StateCommentInstance comment_stage = StateComment.OUTSIDE;
  StateElementInstance element_stage = StateElement.WAITING_FOR_ELEMENT;
  
  Point position = new Point(0, 1);
  CharArrayOutputStream output_stream = new CharArrayOutputStream();
  int offset;

  String name = "";
  String name_element = "";
  String name_attribute;
  String name_value;
}
