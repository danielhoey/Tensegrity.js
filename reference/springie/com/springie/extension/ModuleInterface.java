package com.springie.extension;
/**
 * This class describes the interface for modules that use the program's external API
 */
public interface ModuleInterface {
  /**
   * performs any one-time set-up operations
   */
  void oneOffInitialisation();

  /**
   * called when the user hits reset
   */
  void calledOnReset();

  /**
   * called once during every frame
   */
  void update();
}
