package cs3500.animator.view;

import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.util.List;

import cs3500.animator.model.KeyMoment;
import cs3500.animator.model.Oval;
import cs3500.animator.model.Shape;
import cs3500.animator.model.ReadOnlyAnimationModel;

/**
 * This panel represents the region where the shapes are to be drawn.
 */
public class AnimationPanel extends JPanel {

  private ReadOnlyAnimationModel model;
  private int tick;
  private boolean looping;
  private int endTick;

  /**
   * Constructs an animation panel with a read only model and starting tick.
   *
   * @param model read only animation model used for this animation
   * @param tick  starting tick
   */
  public AnimationPanel(ReadOnlyAnimationModel model, int tick) {
    super();
    this.model = model;
    this.tick = tick;
    this.looping = false;
    this.setEndTick();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    setBackground(Color.WHITE);
    AffineTransform transform = g2d.getTransform();
    drawShapes(g2d);
    g2d.setTransform(transform);
    if ((this.endTick <= this.tick) && this.looping) {
      setTickToZero();
    } else {
      tick = tick + 1;
    }
  }

  /**
   * Draws all the shapes that should be displayed at the current tick on the panel.
   *
   * @param g2d the graphics to draw the shapes into
   */
  protected void drawShapes(Graphics2D g2d) {
    for (Shape s : model.getShapesAtTick(tick)) {
      if (s != null) {
        int posX;
        int posY;
        int width;
        int height;
        int r;
        int g;
        int b;
        // tweening is needed
        if (s.getKeyMoments().size() > 1) {
          KeyMoment initial = s.getKeyMoments().get(0);
          KeyMoment next = s.getKeyMoments().get(1);
          posX = (int) tweening(initial.getTick(), initial.getPos().getX(), next.getTick(),
              next.getPos().getX()) - model.getCanvasDimensions()[0];
          posY = (int) tweening(initial.getTick(), initial.getPos().getY(), next.getTick(),
              next.getPos().getY()) - model.getCanvasDimensions()[1];
          width = (int) tweening(initial.getTick(), initial.getDimensions().getWidth(),
              next.getTick(),
              next.getDimensions().getWidth());
          height = (int) tweening(initial.getTick(), initial.getDimensions().getHeight(),
              next.getTick(), next.getDimensions().getHeight());
          r = (int) tweening(initial.getTick(), initial.getColor().getR(), next.getTick(),
              next.getColor().getR());
          g = (int) tweening(initial.getTick(), initial.getColor().getG(), next.getTick(),
              next.getColor().getG());
          b = (int) tweening(initial.getTick(), initial.getColor().getB(), next.getTick(),
              next.getColor().getB());
          // tweening is not needed
        } else {
          KeyMoment initial = s.getKeyMoments().get(0);
          posX = initial.getPos().getX() - model.getCanvasDimensions()[0];
          posY = initial.getPos().getY() - model.getCanvasDimensions()[1];
          width = initial.getDimensions().getWidth();
          height = initial.getDimensions().getHeight();
          r = initial.getColor().getR();
          g = initial.getColor().getG();
          b = initial.getColor().getB();
        }

        if (s instanceof Oval) {
          g2d.setColor(new Color(r, g, b));
          g2d.fillOval(posX, posY, width, height);
        } else {
          g2d.setColor(new Color(r, g, b));
          g2d.fillRect(posX, posY, width, height);
        }
      }
    }
  }

  /**
   * Calculates the intermediate states of the shape at the current tick if the tick is in between
   * two key moments of a shape. Using linear interpolation, we are able to find all the
   * intermediate states of all components (color, dimensions and position).
   *
   * @param tick1   initial tick,  the tick of the key moment that has the greatest tick lesser than
   *                or equals to the current tick
   * @param initial initial component
   * @param tick2   next tick, the tick of the key moment that has the least tick greater than or
   *                equals to the current tick
   * @param next    next component
   * @return the intermediate state
   */
  private double tweening(int tick1, int initial, int tick2, int next) {
    return (initial * ((float) (tick2 - tick) / (tick2 - tick1)))
        + (next * ((float) (tick - tick1) / (tick2 - tick1)));
  }

  /**
   * Sets the current tick to zero.
   */
  void setTickToZero() {
    this.tick = 0;
  }

  /**
   * Enables looping by setting looping to true.
   */
  void enableLooping() {
    this.looping = true;
  }

  /**
   * Disables looping by setting looping to false.
   */
  void disableLooping() {
    this.looping = false;
  }

  /**
   * Finds the end tick of the overall animation and sets end tick to that tick.
   */
  void setEndTick() {
    int largestTick = 0;
    for (Shape s : model.getShapesInModel()) {
      List<KeyMoment> km = s.getKeyMoments();
      if (km.size() > 0) {

        int thisTick = km.get(km.size() - 1).getTick();
        if (thisTick > largestTick) {
          largestTick = thisTick;
        }
      }
    }
    this.endTick = largestTick;
  }

  void setTick(int i) {
    this.tick = i;
  }

  public int getCurrentTick() {
    return tick;
  }
}