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
 * This is an implementation of the ITextualView interface that produces the textual
 * description of the animation.
 */
public class TextualView implements ITextualView {

  ReadOnlyAnimationModel model;
  String fileOutput;

  /**
   * Constructs an TextualView.
   *
   * @param model      the read only model
   * @param fileOutput name of the file to output to
   */
  public TextualView(ReadOnlyAnimationModel model, String fileOutput) {
    this.model = model;
    this.fileOutput = fileOutput;
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
    StringBuilder sb = new StringBuilder();
    sb.append(printCanvas());
    List<Shape> a = new ArrayList<Shape>(model.getShapesInModel());
    for (Shape s : a) {
      sb.append("shape " + s.getName() + " " + getShapeType(s));
      if (!s.getKeyMoments().isEmpty()) {
        sb.append(printKeyMoments(s.getKeyMoments(), s.getName()));
      }
    }
    return sb.toString();
  }

  /**
   * Prints every key moments in the list of key moments into its textual representation.
   *
   * @param keyMoments the list of key moments of a shape
   * @param name       the name of the shape
   * @return textual representation of every key moment a shape experiences.
   */
  private String printKeyMoments(List<KeyMoment> keyMoments, String name) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < keyMoments.size() - 1; i++) {
      KeyMoment initial = keyMoments.get(i);
      int initialTick = initial.getTick();
      int initialPosX = initial.getPos().getX();
      int initialPosY = initial.getPos().getY();
      int initialWidth = initial.getDimensions().getWidth();
      int initialHeight = initial.getDimensions().getHeight();
      int initialR = initial.getColor().getR();
      int initialG = initial.getColor().getG();
      int initialB = initial.getColor().getB();
      KeyMoment next = keyMoments.get(i + 1);
      int nextTick = next.getTick();
      int nextPosX = next.getPos().getX();
      int nextPosY = next.getPos().getY();
      int nextWidth = next.getDimensions().getWidth();
      int nextHeight = next.getDimensions().getHeight();
      int nextR = next.getColor().getR();
      int nextG = next.getColor().getG();
      int nextB = next.getColor().getB();
      sb.append("motion " + name + " "
              + initialTick + " "
              + initialPosX + " "
              + initialPosY + " "
              + initialWidth + " "
              + initialHeight + " "
              + initialR + " "
              + initialG + " "
              + initialB + " "
              + nextTick + " "
              + nextPosX + " "
              + nextPosY + " "
              + nextWidth + " "
              + nextHeight + " "
              + nextR + " "
              + nextG + " "
              + nextB + "\n");
    }
    return sb.toString();
  }

  /**
   * Determines the shape type and prints it accordingly.
   *
   * @param s the shape being printed
   * @return textual representation of the given shape.
   * @throws IllegalArgumentException if there is no such shape.
   */
  private String getShapeType(Shape s) {
    if (s instanceof Rectangle) {
      return "rectangle\n";
    } else if (s instanceof Oval) {
      return "ellipse\n";
    } else {
      throw new IllegalArgumentException("No such shape");
    }
  }

  /**
   * Prints the canvas and its dimensions into its textual representation.
   *
   * @return textual representation of the canvas and its dimensions.
   */
  private String printCanvas() {
    int[] vals = model.getCanvasDimensions();
    StringBuilder sb = new StringBuilder();
    sb.append("canvas ");
    for (int i : vals) {
      sb.append(i + " ");
    }
    sb.append("\n");
    return sb.toString();
  }
}
