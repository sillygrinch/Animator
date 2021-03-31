package cs3500.animator.view;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cs3500.animator.model.KeyMoment;
import cs3500.animator.model.Oval;
import cs3500.animator.model.ReadOnlyAnimationModel;
import cs3500.animator.model.Rectangle;
import cs3500.animator.model.Shape;

/**
 * This is an implementation of the ITextualView interface that produces the textual description of
 * the animation that is compliant with the SVG file format. Allows us to create a visual
 * representation of our animation using existing animation frameworks, such as Flash, etc.
 */
public class SVGView implements ITextualView {

  private ReadOnlyAnimationModel model;
  String fileOutput;
  int tempo;

  /**
   * Constructs an SVGView.
   *
   * @param model      the read only model
   * @param fileOutput name of the file to output to
   * @param tempo      speed of the animation the user has chosen
   */
  public SVGView(ReadOnlyAnimationModel model, String fileOutput, int tempo) {
    this.model = model;
    this.fileOutput = fileOutput;
    this.tempo = tempo;
  }

  @Override
  public void makeVisible() {
    if (fileOutput == null) {
      System.out.print(getView());
    } else {
      try {
        File file = new File(fileOutput);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(getView());
        fileWriter.close();
      } catch (IOException ioe) {
        System.out.print("An error occurred when writing the file.");
      }
    }
  }


  @Override
  public String getView() {
    if (this == null) {
      throw new IllegalArgumentException("View cannot be null");
    }
    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append("<svg ");

    stringBuilder.append(printCanvas());

    stringBuilder.append("xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">\n");

    stringBuilder.append(printAllShapesAndTransformations());

    stringBuilder.append("</svg>");

    return stringBuilder.toString();
  }

  /**
   * Prints all the shapes and transformation compliant with the SVG file format.
   *
   * @return the textual representation compliant with the SVG file format of all shapes and its
   *         animations.
   */
  private String printAllShapesAndTransformations() {
    StringBuilder sb = new StringBuilder();
    List<Shape> a = new ArrayList<Shape>(model.getShapesInModel());
    for (Shape s : a) {
      if (!s.getKeyMoments().isEmpty()) {
        sb.append(printInitialShapeAttributes(s));
        if (s.getKeyMoments().size() > 1) {
          sb.append(printTransformations(s));
        }
        sb.append(printEnding(s));
      }
    }
    return sb.toString();
  }

  /**
   * Prints the closing tag of the shape depending on the type of shape.
   *
   * @param s the shape used to determine the closing tag
   * @return the closing tag depending on the type of shape.
   */
  private String printEnding(Shape s) {
    if (s instanceof Rectangle) {
      return "</rect>\n";
    } else if (s instanceof Oval) {
      return "</ellipse>\n";
    } else {
      throw new IllegalArgumentException("");
    }
  }

  /**
   * Prints all the transformation that the shape experiences. It only prints the transformation of
   * attributes that has changed from the previous key moment.
   *
   * @param s the shape we are printing the transformation from
   * @return textual representation of all the transformation
   */
  private String printTransformations(Shape s) {
    StringBuilder sb = new StringBuilder();
    List<KeyMoment> km = s.getKeyMoments();
    for (int i = 0; i < km.size() - 1; i++) {
      KeyMoment initial = km.get(i);
      KeyMoment next = km.get(i + 1);
      String initialColor = "rgb(" + initial.getColor().getR() + "," + initial.getColor().getG()
              + "," + initial.getColor().getB() + ")";
      String nextColor = "rgb(" + next.getColor().getR() + "," + next.getColor().getG() + ","
              + next.getColor().getB() + ")";
      double beginTick = (float) (initial.getTick() / this.tempo);
      double duration = (float) next.getTick() / this.tempo - beginTick;
      sb.append(attributeChanges(s, Attribute.X, initial.getPos().getX(), next.getPos().getX(),
              beginTick, duration));
      sb.append(attributeChanges(s, Attribute.Y, initial.getPos().getY(), next.getPos().getY(),
              beginTick, duration));
      sb.append(attributeChanges(s, Attribute.Width, initial.getDimensions().getWidth(),
              next.getDimensions().getWidth(), beginTick, duration));
      sb.append(attributeChanges(s, Attribute.Height, initial.getDimensions().getHeight(),
              next.getDimensions().getHeight(), beginTick, duration));
      sb.append(attributeColorChanges(initialColor, nextColor, beginTick, duration));
    }
    return sb.toString();
  }

  /**
   * Prints the textual representation compliant with the SVG file format of individual color
   * changes.
   *
   * @param initialColor the initial color
   * @param nextColor    the next color
   * @param beginTick    the starting tick
   * @param duration     the duration before the transition
   * @return textual representation of each color change transformation
   */
  private String attributeColorChanges(String initialColor, String nextColor, double beginTick,
                                       double duration) {
    if (!initialColor.equals(nextColor)) {
      return "<animate attributeName=\"fill\" attributeType=\"CSS\" "
              + "from=\"" + initialColor + "\" "
              + "to=\"" + nextColor + "\""
              + "  begin=\"" + beginTick + "\" "
              + "dur=\"" + duration + "\" "
              + "fill=\"freeze\" />\n";
    } else {
      return "";
    }
  }

  /**
   * Prints the textual representation compliant with the SVG file format of individual attribute
   * changes.
   *
   * @param s         the shape we are printing the transformation from
   * @param attType   the type of attribute that is being changed
   * @param att       the initial attribute
   * @param att1      the next attribute
   * @param beginTick the starting tick
   * @param duration  the duration before the transition
   * @return textual representation of each attribute change transformation
   */
  private String attributeChanges(Shape s, Attribute attType, int att, int att1, double beginTick,
                                  double duration) {
    if (att != att1) {
      return "<animate attributeName=\"" + determineAttribute(attType, s)
              + "\" attributeType=\"XML\" "
              + "begin=\"" + beginTick + "\" "
              + "dur=\"" + duration + "\" "
              + "fill=\"freeze\" "
              + "from=\"" + att + "\" "
              + "to=\"" + att1 + "\" />\n";
    } else {
      return "";
    }
  }

  /**
   * Determines the textual representation of the attribute type being changed depending on the
   * shape.
   *
   * @param attType the type of attribute that is being changed
   * @param s       the shape we are printing the transformation from
   * @return textual representation of the attribute type being changed
   */
  private String determineAttribute(Attribute attType, Shape s) {
    if (s instanceof Rectangle) {
      switch (attType) {
        case X:
          return "x";
        case Y:
          return "y";
        case Width:
          return "width";
        case Height:
          return "height";
        default:
          throw new IllegalArgumentException("no such attribute");
      }
    } else if (s instanceof Oval) {
      switch (attType) {
        case X:
          return "cx";
        case Y:
          return "cy";
        case Width:
          return "rx";
        case Height:
          return "ry";
        default:
          throw new IllegalArgumentException("no such attribute");
      }
    } else {
      throw new IllegalArgumentException("much be either oval or rectangle");
    }
  }

  /**
   * Determines the textual representation of shape and its initial components (position, color and
   * dimensions).
   *
   * @param s the shape we are printing the transformation from
   * @return textual representation of shape and its initial components
   */
  private String printInitialShapeAttributes(Shape s) {
    StringBuilder sb = new StringBuilder();
    KeyMoment initialKeyMoment = s.getKeyMoments().get(0);
    int initialX = initialKeyMoment.getPos().getX();
    int initialY = initialKeyMoment.getPos().getY();
    int initialWidth = initialKeyMoment.getDimensions().getWidth();
    int initialHeight = initialKeyMoment.getDimensions().getHeight();
    int initialR = initialKeyMoment.getColor().getR();
    int initialG = initialKeyMoment.getColor().getG();
    int initialB = initialKeyMoment.getColor().getB();
    if (s instanceof Rectangle) {
      sb.append("<rect id=\"" + s.getName()
              + "\" x=\"" + initialX
              + "\" y=\"" + initialY
              + "\" width=\"" + initialWidth
              + "\" height=\"" + initialHeight
              + "\" fill=\"rgb(" + initialR + "," + initialG + "," + initialB + ")"
              + "\" visibility=\"visible\" " + ">\n");
    } else if (s instanceof Oval) {
      sb.append("<ellipse id=\"" + s.getName()
              + "\" cx=\"" + initialX
              + "\" cy=\"" + initialY
              + "\" rx=\"" + initialWidth
              + "\" ry=\"" + initialHeight
              + "\" fill=\"rgb(" + initialR + "," + initialG + "," + initialB + ")"
              + "\" visibility=\"visible\" " + ">\n");
    } else {
      throw new IllegalArgumentException("No such shape");
    }
    return sb.toString();
  }

  /**
   * Prints the canvas' dimensions.
   *
   * @return textual representation of canvas
   */
  private String printCanvas() {
    int canvasLeft = model.getCanvasDimensions()[0];
    int canvasTop = model.getCanvasDimensions()[1];
    int canvasWidth = model.getCanvasDimensions()[2];
    int canvasHeight = model.getCanvasDimensions()[3];
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("width=\"");
    stringBuilder.append(canvasWidth);
    stringBuilder.append("\" height=\"");
    stringBuilder.append(canvasHeight + "\"");
    stringBuilder.append(" viewBox=\"");
    stringBuilder.append(canvasLeft + " ");
    stringBuilder.append(canvasTop + " ");
    stringBuilder.append(canvasWidth + " ");
    stringBuilder.append(canvasHeight + "\" ");
    return stringBuilder.toString();
  }

  /**
   * Represents the type of attributes being changed in the SVG format.
   */
  public enum Attribute {
    X, Y, Width, Height;
  }

}

