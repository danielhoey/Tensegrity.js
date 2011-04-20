package com.tifsoft.xml.driver;

class StateElementInstance {
  String toString(Object o) {
    if (o == StateElement.WAITING_FOR_ELEMENT) {
      return "WAITING_FOR_ELEMENT";
    }

    if (o == StateElement.WAITING_FOR_ATTRIBUTE) {
      return "WAITING_FOR_ATTRIBUTE";
    }

    if (o == StateElement.WAITING_FOR_VALUE_QUOTE) {
      return "WAITING_FOR_VALUE_QUOTE";
    }

    if (o == StateElement.WAITING_FOR_VALUE) {
      return "WAITING_FOR_VALUE";
    }

    return "UNKNOWN";
  }
}