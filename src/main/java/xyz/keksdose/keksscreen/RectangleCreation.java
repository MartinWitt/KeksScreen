package xyz.keksdose.keksscreen;

import java.awt.Frame;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RectangleCreation {

  private Point start;
  private Point end;

  public RectangleCreation() {
    start = MouseInfo.getPointerInfo().getLocation();
  }

  public void register(Frame frame) {
    frame.addMouseListener(new ClickHandler());
  }


  public Rectangle createRectangle() {
    Rectangle rectangle = new Rectangle(start);
    while (Objects.isNull(end)) {
      try {
        TimeUnit.MILLISECONDS.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    rectangle.add(end);

    return rectangle;
  }



  class ClickHandler extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
      end = e.getPoint();
    }
  }
}
