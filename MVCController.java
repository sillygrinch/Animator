package cs3500.animator.controller;

import cs3500.animator.model.Color;
import cs3500.animator.model.Dimensions;
import cs3500.animator.model.Oval;
import cs3500.animator.model.Position2D;
import cs3500.animator.model.Rectangle;
import cs3500.animator.view.InteractiveGraphicsView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import cs3500.animator.model.AnimationModel;

import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Represents a Controller for interactive view: handle user edits by executing them using the
 * model; convey edit outcomes to the user in some form, both textually and visually.
 */
public class MVCController implements IController, ActionListener, ListSelectionListener {

  private AnimationModel model;
  private InteractiveGraphicsView view;
  protected Timer timer;

  /**
   * Constructs an MVCController.
   *
   * @param model the model
   * @param view  the view
   * @throws IllegalArgumentException if the model or view is null.
   */
  public MVCController(AnimationModel model, InteractiveGraphicsView view) {
    if (model == null || view == null) {
      throw new IllegalArgumentException("View or Model cannot be NULL");
    }
    this.model = model;
    this.view = view;
    this.timer = new Timer(1000 / this.view.getTicksPerSec(), e -> {
      this.view.refresh();
    });

  }

  @Override
  public void playAnimation() {
    this.view.setActionListener(this);
    this.view.setListSelectionListener(this);
    this.view.makeVisible();
    this.view.refresh();

  }

  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {

      case "Start":
      case "Resume":
        timer.start();
        view.setPause();
        break;
      case "Pause":
        timer.stop();
        view.setStartResume();
        break;
      case "Speed down":
        timer.setDelay(timer.getDelay() + 15);
        if (timer.getDelay() - 15 > 1) {
          timer.setDelay(timer.getDelay() + 15);
        }
        break;
      case "Speed up":
        if (timer.getDelay() - 15 > 1) {
          timer.setDelay(timer.getDelay() - 15);
        }
        break;
      case "Restart":
        view.setRestart();
        break;
      case "create shape":
        String name = this.view.getShapeText();
        String type = this.view.getShapeType();
        createShape(name, type);
        view.updateCreateShapeDisplay(name, type);
        break;
      case "remove shape":
        String removeThis = this.view.getShapeSelected();
        removeShape(removeThis);
        view.updateRemoveShapeDisplay(removeThis);
        break;
      case "create keyframe":
        String tickText = view.getShapeTickText();
        String nameShape = view.getShapeNameText();
        int tick = Integer.parseInt(view.getShapeTickText());
        String posn = view.getShapePosXYText();
        String dim = view.getShapeDimText();
        String color = view.getShapeColRGBText();
        createKeyFrame(tickText, nameShape, tick, posn, dim, color);
        view.updateCreateKeyframeDisplay(nameShape, tickText, posn, dim, color);
        break;
      case "remove keyframe":
        String shapeName = view.getShapeToRemove();
        int tickToRemove = view.getShapeToRemoveTick();
        model.removeAnimation(shapeName, tickToRemove);
        view.updateRemoveKeyframeDisplay(shapeName, tickToRemove);
        break;
      case "export SVG":
        view.saveSVGFile();
        break;
      default:
        throw new IllegalStateException("Error, no such command");
    }
    view.refresh();
  }

  /**
   * Transform the textual representation of a dimension to an instance of a dimension.
   *
   * @param s the textual representation of a dimension
   * @return an instance of the dimension.
   */
  private Dimensions convertDimension(String s) {
    Scanner sc = new Scanner(s);
    int i = sc.nextInt();
    int j = sc.nextInt();
    sc.close();
    return new Dimensions(i, j);
  }

  /**
   * Transform the textual representation of a position to an instance of a position.
   *
   * @param s the textual representation of a position
   * @return an instance of the position.
   */
  private Position2D convertPosition(String s) {
    Scanner sc = new Scanner(s);
    int i = sc.nextInt();
    int j = sc.nextInt();
    sc.close();
    return new Position2D(i, j);
  }

  /**
   * Transform the textual representation of a color to an instance of a color.
   *
   * @param s the textual representation of a color
   * @return an instance of the color.
   */
  private Color convertColor(String s) {
    Scanner sc = new Scanner(s);
    int i = sc.nextInt();
    int j = sc.nextInt();
    int k = sc.nextInt();
    sc.close();
    return new Color(i, j, k);
  }

  @Override
  public void valueChanged(ListSelectionEvent e) {
    view.refreshRemoveKeyFrameButtons();
  }

  /**
   * Transform the textual representation of a type of shape to an instance of that shape.
   *
   * @param name the name of the shape the user would like to name the shape with
   * @param type the textual representation of the type of shape
   * @return an instance of the shape
   */
  private void createShape(String name, String type) {
    if (name == null || name.equals("")) {
      JOptionPane.showMessageDialog(null,
          "Need to enter a name for the new shape ",
          "ERROR", JOptionPane.INFORMATION_MESSAGE);
    } else if (type == null) {
      JOptionPane.showMessageDialog(null,
          "Need to select a type for the new shape ",
          "ERROR", JOptionPane.INFORMATION_MESSAGE);
    } else {
      try {
        if (type.equals("Rectangle")) {
          model.addShape(new Rectangle(name));
        } else {
          model.addShape(new Oval(name));
        }
      } catch (IllegalArgumentException ex) {
        JOptionPane.showMessageDialog(null,
            "Cannot make duplicate shapes ",
            "ERROR", JOptionPane.INFORMATION_MESSAGE);
      }
    }
  }

  /**
   * Tries to remove the shape with the given name.
   *
   * @param removeThis the name of the shape to remove.
   */
  private void removeShape(String removeThis) {
    if (removeThis == null) {
      JOptionPane.showMessageDialog(null,
          "Need to select a shape to remove it", "ERROR", JOptionPane.INFORMATION_MESSAGE);
    } else {
      model.removeShape(removeThis);
    }
  }


  /**
   * Creates a keyframe with the given tick, position, dimension and color to the given shape and
   * adds it to the model.
   *
   * @param tickText  the textual representation of the tick
   * @param nameShape the name of the shape
   * @param tick      the tick
   * @param posn      the position
   * @param dim       the dimension
   * @param color     the color
   */
  private void createKeyFrame(String tickText, String nameShape, int tick, String posn, String dim,
      String color) {
    if (tickText == null) {
      JOptionPane.showMessageDialog(null,
          "Need to enter tick", "ERROR", JOptionPane.INFORMATION_MESSAGE);
    } else {
      try {
        model.addKeyframe(nameShape, tick, convertPosition(posn), convertDimension(dim),
            convertColor(color));
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(null,
            "Need to enter a valid tick", "ERROR", JOptionPane.INFORMATION_MESSAGE);
      } catch (IllegalArgumentException f) {
        JOptionPane.showMessageDialog(null,
            f.getMessage(), "ERROR", JOptionPane.INFORMATION_MESSAGE);
      }
    }
  }
}
