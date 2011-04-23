package com.tifsoft.xml.driver;

public class ParserStateUpdater {
  static void endOfIdentifier(ParserState state) {
    if (!SomeStringUtilities.isWhiteSpace(state.name)) {
      if (state.element_stage == StateElement.WAITING_FOR_ELEMENT) {
        if ("".equals(state.name_element)) {
          state.name_element = state.name;
        }
        state.element_stage = StateElement.WAITING_FOR_ATTRIBUTE;
      } else if (state.element_stage == StateElement.WAITING_FOR_ATTRIBUTE) {
        if ("".equals(state.name_attribute)) {
          state.name_attribute = state.name;
        }
        state.element_stage = StateElement.WAITING_FOR_VALUE_QUOTE;
      }
    }

    state.name = "";
  }
}
