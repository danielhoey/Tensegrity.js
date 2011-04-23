package com.tifsoft.xml.driver;

interface StateComment {
  StateCommentInstance OUTSIDE = new StateCommentInstance();
  StateCommentInstance EXCLAMATION = new StateCommentInstance();
  StateCommentInstance MINUS_1 = new StateCommentInstance();
  StateCommentInstance MINUS_2 = new StateCommentInstance();
  StateCommentInstance MINUS_3 = new StateCommentInstance();
}
