package xyz.keksdose.keksscreen;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;



public class ScreenCapture {

  public void takeScreenShot() throws AWTException {

    // Create rectangle for all screens
    Rectangle screenRect = new Rectangle(0, 0, 0, 0);
    for (GraphicsDevice gd : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
      screenRect = screenRect.union(gd.getDefaultConfiguration().getBounds());
    }
    // take screenshot and convert it
    Robot robot = new Robot();
    Image screenshot = robot.createMultiResolutionScreenCapture(screenRect)
        .getResolutionVariant(screenRect.getWidth(), screenRect.getHeight());
    BufferedImage screenShot = toBufferedImage(screenshot);

    // create selection frame
    JFrame frame = new JFrame();
    frame.setTitle("Click Area");
    JLabel label = new JLabel(new ImageIcon(screenshot));
    frame.add(label);
    frame.setVisible(true);
    frame.setSize(screenRect.getSize());
    frame.setAlwaysOnTop(true);
    frame.requestFocus();

    // register mouse listener
    RectangleCreation mouse = new RectangleCreation();
    mouse.register(frame);
    Rectangle selection = mouse.createRectangle();

    // refresh frame and show selections
    label.setVisible(false);
    JLabel label2 = new JLabel(new ImageIcon(grayScale(screenShot, selection)));
    frame.add(label2);
    frame.setVisible(true);
    frame.repaint();
    createAndAwaitListener(screenShot, frame, selection);

    // stop frame
    frame.dispose();
  }



  private void createAndAwaitListener(BufferedImage screenShot, JFrame frame, Rectangle foo) {
    ScreenshotButtonListener listener = new ScreenshotButtonListener(screenShot, foo);
    frame.addKeyListener(listener);
    listener.await();
  }



  private BufferedImage grayScale(BufferedImage input, Rectangle shape) {
    BufferedImage image = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
    for (int y = 0; y < input.getHeight(); y++) {
      for (int x = 0; x < input.getWidth(); x++) {
        int p = input.getRGB(x, y);
        if (shape.contains(x, y)) {
          image.setRGB(x, y, p);
        } else {
          int a = (p >> 24) & 0xff;
          int r = (p >> 16) & 0xff;
          int g = (p >> 8) & 0xff;
          int b = p & 0xff;
          // calculate average
          int avg = (r + g + b) / 3;
          // replace RGB value with avg
          p = (a << 24) | (avg << 16) | (avg << 8) | avg;
          image.setRGB(x, y, p);
        }
      }
    }
    Graphics2D g = image.createGraphics();
    g.setPaint(Color.RED);
    float dash[] = {10.0f};
    g.setStroke(
        new BasicStroke(5, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
    g.draw(shape);
    g.create().drawImage(image, 0, 0, null);

    return image;
  }

  private BufferedImage toBufferedImage(Image img) {
    if (img instanceof BufferedImage) {
      return (BufferedImage) img;
    }
    // Create a buffered image with transparency
    BufferedImage bimage =
        new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
    // Draw the image on to the buffered image
    Graphics2D bGr = bimage.createGraphics();
    bGr.drawImage(img, 0, 0, null);
    bGr.dispose();
    // Return the buffered image
    return bimage;
  }
}
