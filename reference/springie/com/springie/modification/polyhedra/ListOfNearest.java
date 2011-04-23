package com.springie.modification.polyhedra;

public class ListOfNearest {
  int[] array;

  int[] proximity;

  public int size;

  int largest_proximity = Integer.MAX_VALUE;

  int largest_index;

  ListOfNearest(int size) {
    this.size = size;
    this.array = new int[size];
    this.proximity = new int[size];
    for (int counter = size; --counter >= 0;) {
      this.array[counter] = -1;
      this.proximity[counter] = Integer.MAX_VALUE;
    }
  }

  public void update(int d, int idx) {
    if (d < this.largest_proximity) {
      this.proximity[this.largest_index] = d;
      this.array[this.largest_index] = idx;
      updateLargestProximity();
      return;
    }
  }

  public void updateLargestProximity() {
    this.largest_proximity = Integer.MIN_VALUE;

    for (int counter = this.size; --counter >= 0;) {
      if (this.proximity[counter] > this.largest_proximity) {
        this.largest_proximity = this.proximity[counter];
        this.largest_index = counter;
      }
    }
  }

  public int countFound() {
    int cnt = 0;
    for (int counter = this.size; --counter >= 0;) {
      if (this.array[counter] >= 0) {
        cnt++;
      }
    }

    return cnt;
  }
}