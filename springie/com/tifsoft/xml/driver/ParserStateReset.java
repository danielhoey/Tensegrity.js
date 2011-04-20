package com.tifsoft.xml.driver;

public class ParserStateReset {
  static void parserStateReset(final ParserState state) {
    state.element_stage = StateElement.WAITING_FOR_ELEMENT;
    state.in_element = true;
    state.closing_element = false;
    state.element_singular = false;
    state.name_element = "";
    state.name_attribute = "";
    state.name_value = "";
    state.name = "";
  }
}
