package xyz.keksdose.keksscreen;


import java.awt.AWTException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class KeyBoardListener implements NativeKeyListener {
  public static final int VC_PAUSE = ConfigReader.getKey();

  public void nativeKeyReleased(NativeKeyEvent e) {
    // not needed
  }

  public void nativeKeyTyped(NativeKeyEvent e) {
    // not needed
  }

  @Override
  public void nativeKeyPressed(NativeKeyEvent e) {
    if (e.getKeyCode() == VC_PAUSE) {
      try {
        new ScreenCapture().takeScreenShot();
      } catch (AWTException e1) {
        e1.printStackTrace();
      }
    } ;
  }
}

