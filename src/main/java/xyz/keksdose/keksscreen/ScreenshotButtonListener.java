package xyz.keksdose.keksscreen;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import xyz.keksdose.keksscreen.ConfigReader;

public class ScreenshotButtonListener extends KeyAdapter {
  private BufferedImage screenShot;
  private Rectangle selection;
  private boolean stop = false;

  public ScreenshotButtonListener(BufferedImage screenShot, Rectangle selection) {
    this.screenShot = screenShot;
    this.selection = selection;
  }

  @Override
  public void keyTyped(KeyEvent e) {
    if (e.getKeyChar() == '\n') {
      File f = new File(Math.abs(UUID.randomUUID().hashCode()) + "." + ConfigReader.getFormat());
      try {
        // cut subimage from screenshot and safe it.
        BufferedImage image = screenShot.getSubimage((int) selection.getX(), (int) selection.getY(),
            (int) selection.getWidth(), (int) selection.getHeight());
        ImageIO.write(image, ConfigReader.getFormat(), f);

        // do scp command
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(ConfigReader.getSCPCommand(f.getPath(), f.getName())).inheritIO().start()
            .waitFor();

        // and copy link to clipboard
        StringSelection stringSelection =
            new StringSelection(ConfigReader.getDomain() + f.getName());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, stringSelection);

      } catch (IOException | InterruptedException e1) {
        e1.printStackTrace();
      } finally {
        // cleanup File
        f.delete();
        stop = true;
      }
    } else {
      stop = true;
    }
  }

  public void await() {
    while (!stop) {
      try {
        TimeUnit.MILLISECONDS.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
