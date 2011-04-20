package com.springie.muscles;

import com.springie.utilities.random.Hortensius32Fast;


public class Oscillator {
  public int amplitude;
  public int frequency;
  public int phase;

  public int getAmplitude() {
    return this.amplitude;
  }
  public void setAmplitude(int amplitude) {
    this.amplitude = amplitude;
  }
  public int getPhase() {
    return this.phase;
  }
  public void setPhase(int phase) {
    this.phase = phase;
  }

  final void mutate() {
    final Hortensius32Fast rnd = new Hortensius32Fast();

    this.amplitude += (rnd.nextInt() >> 24) & 255;
    this.phase += (rnd.nextInt() >> 24) & 255;
  }
  public int getFrequency() {
    return this.frequency;
  }
  public void setFrequency(int frequency) {
    this.frequency = frequency;
  }
}
