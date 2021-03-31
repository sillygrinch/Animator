package cs3500.animator.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import cs3500.animator.util.AnimationBuilder;


/**
 * This class manages and represents a model for simple animation of 2D Shapes and implements all
 * its associated operations which includes the process of starting, adding and outputting the state
 * of the animation.
 */
public final class SimpleAnimation implements AnimationModel {

  private int canvasLeft;
  private int canvasTop;
  private int canvasWidth;
  private int canvasHeight;
  private List<Shape> listOfShape;

  /**
   * Constructs a SimpleAnimation, initializing the simple animation to the default state whereby
   * the list of shape it contains is empty.
   */
  public SimpleAnimation() {
    this.listOfShape = new ArrayList<Shape>();
  }

  /**
   * Ensures the given list does not have duplicate shapes.
   *
   * @param los list of shapes
   * @throws IllegalArgumentException if the list has duplicate shapes, in terms of name.
   */
  private void ensureNoDuplicates(List<Shape> los) {
    List<String> listOfShapeNames = getListOfNames(los);
    Set<String> setOfShapeNames = new HashSet<String>(listOfShapeNames);
    if (listOfShapeNames.size() > setOfShapeNames.size()) {
      throw new IllegalArgumentException("No duplicates.");
    }
  }

  /**
   * Returns a list of string containing the names of the shapes in the given list.
   *
   * @param los list of shapes
   * @return a list of string representing the names of the shapes in the given list.
   */
  private List<String> getListOfNames(List<Shape> los) {
    List<String> names = new ArrayList<>();
    for (Shape s : los) {
      names.add(s.getName());
    }
    return names;
  }

  @Override
  public void addAnimation(String shapeToAnimate, int startTick, Position2D startPosition,
                           Dimensions startDimensions, Color startColor, int endTick,
                           Position2D endPosition, Dimensions endDimensions, Color endColor) {
    getThisShape(shapeToAnimate).addAnimationShape(startTick, startPosition, startDimensions,
            startColor, endTick, endPosition, endDimensions, endColor);
  }

  /**
   * Returns the shape of the given name found in the model.
   *
   * @param shapeToMove name of the shape
   * @return Shape of the given name.
   * @throws IllegalArgumentException if the shape is not found in the model using the given name.
   */
  private Shape getThisShape(String shapeToMove) {
    for (Shape shape : this.listOfShape) {
      if (shape.getName().equals(shapeToMove)) {
        return shape;
      }
    }
    throw new IllegalArgumentException("No such shape.");
  }

  @Override
  public void addShape(Shape shapeToAdd) {
    List<Shape> newList = new ArrayList<Shape>(this.listOfShape);
    newList.add(shapeToAdd);
    ensureNoDuplicates(newList);
    this.listOfShape.add(shapeToAdd.createCopyShape());
  }

  @Override
  public List<Shape> getShapesAtTick(int tick) {
    List<Shape> los = new ArrayList<>();
    for (Shape s : this.listOfShape) {
      los.add(s.getKeyMomentsAtTick(tick));
    }
    return los;
  }

  @Override
  public List<Shape> getShapesInModel() {
    List<Shape> newList = new ArrayList<Shape>(this.listOfShape);
    return newList;
  }

  @Override
  public int[] getCanvasDimensions() {
    int[] vals = new int[4];
    vals[0] = canvasLeft;
    vals[1] = canvasTop;
    vals[2] = canvasWidth;
    vals[3] = canvasHeight;
    return vals;
  }

  @Override
  public void setCanvas(int left, int top, int width, int height) {
    this.canvasLeft = left;
    this.canvasTop = top;
    this.canvasWidth = width;
    this.canvasHeight = height;
  }

  @Override
  public void removeShape(String shapeToRemove) {
    this.listOfShape.remove(getThisShape(shapeToRemove));
  }

  @Override
  public void removeAnimation(String shapeToRemove, int tickOfAnimationToRemove) {
    getThisShape(shapeToRemove).removeKeyMomentAt(tickOfAnimationToRemove);
  }

  @Override
  public void addKeyframe(String shapeToAdd, int tick, Position2D pos, Dimensions dim,
                          Color color) {
    getThisShape(shapeToAdd).addKeyframe(tick, pos, dim, color);
  }

  @Override
  public boolean equals(Object a) {
    if (this == a) {
      return true;
    }
    if (!(a instanceof SimpleAnimation)) {
      return false;
    }

    SimpleAnimation that = (SimpleAnimation) a;

    return (this.listOfShape.equals(that.listOfShape));
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.listOfShape);
  }

  /**
   * This class manages and assist to adapt from the model interface that the AnimationReader
   * expects to this model. It describes constructing any animation, shape-by-shape and
   * motion-by-motion.
   */
  public static final class Builder implements AnimationBuilder<AnimationModel> {

    private AnimationModel model;


    /**
     * Constructs a new Builder with a new empty AnimationModel.
     */
    public Builder() {
      model = new SimpleAnimation();
    }

    @Override
    public AnimationModel build() {
      return model;
    }

    @Override
    public AnimationBuilder<AnimationModel> setBounds(int x, int y, int width, int height) {
      model.setCanvas(x, y, width, height);
      return this;
    }

    @Override
    public AnimationBuilder<AnimationModel> declareShape(String name, String type) {
      if (type.equals("ellipse")) {
        model.addShape(new Oval(name));
      } else if (type.equals("rectangle")) {
        model.addShape(new Rectangle(name));
      } else {
        throw new IllegalArgumentException("No such shape allowed.");
      }
      return this;
    }

    @Override
    public AnimationBuilder<AnimationModel> addMotion(String name, int t1, int x1, int y1, int w1,
                                                      int h1, int r1, int g1, int b1, int t2,
                                                      int x2, int y2, int w2, int h2, int r2,
                                                      int g2, int b2) {
      Position2D initialPos = new Position2D(x1, y1);
      Position2D finalPos = new Position2D(x2, y2);
      Dimensions initialDimension = new Dimensions(w1, h1);
      Dimensions finalDimension = new Dimensions(w2, h2);
      Color initialColor = new Color(r1, g1, b1);
      Color finalColor = new Color(r2, g2, b2);
      model.addAnimation(name, t1, initialPos, initialDimension, initialColor, t2, finalPos,
              finalDimension, finalColor);
      return this;
    }

    @Override
    public AnimationBuilder<AnimationModel> addKeyframe(String name, int t, int x, int y, int w,
                                                        int h, int r, int g, int b) {
      return null;
    }

  }

}
