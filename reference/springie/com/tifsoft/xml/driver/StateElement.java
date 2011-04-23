package com.tifsoft.xml.driver;

interface StateElement {
  StateElementInstance WAITING_FOR_ELEMENT = new StateElementInstance();

  StateElementInstance WAITING_FOR_ATTRIBUTE = new StateElementInstance();

  StateElementInstance WAITING_FOR_VALUE_QUOTE = new StateElementInstance();

  StateElementInstance WAITING_FOR_VALUE = new StateElementInstance();
}