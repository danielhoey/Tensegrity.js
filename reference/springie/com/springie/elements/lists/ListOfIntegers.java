package com.springie.elements.lists;

import java.util.Vector;

import com.springie.elements.base.BaseType;
import com.springie.utilities.log.Log;

public class ListOfIntegers extends BaseType {
  Vector list = new Vector();

  public final int size() {
    return this.list.size();
  }

  public final void add(int i) {
    this.list.addElement(new Integer(i));
  }

  public int retreive(int i) {
    return ((Integer) this.list.elementAt(i)).intValue();
  }

  public void remove(int n) {
    for (int i = this.list.size(); --i >= 0;) {
      final int value = retreive(i);
      if (value == n) {
        Log.log("Removing index " + n + " : from list position:" + i);
        this.list.removeElementAt(i);
        return;
      }
    }
  }
}