package cs3500.animator;

import cs3500.animator.controller.IController;
import cs3500.animator.controller.MVCController;
import cs3500.animator.view.InteractiveGraphicsView;

import java.io.FileNotFoundException;
import java.io.FileReader;

import cs3500.animator.model.AnimationModel;
import cs3500.animator.model.SimpleAnimation;
import cs3500.animator.util.AnimationReader;
import cs3500.animator.view.AnimationView;
import cs3500.animator.view.ViewCreator;

/**
 * Class representing the entry point to the program.
 */
public final class Excellence {

  /**
   * Runs the animation, depending on the inputs as command-line arguments.
   *
   * @throws IllegalArgumentException if a command that we do not recognize is found.
   * @args the command-line arguments
   */
  public static void main(String[] args) {
    String inputFile = null;
    String outputFile = null;
    String viewType = null;

    int speed = 1;
    for (int i = 0; i < args.length; i = i + 2) {
      String cmd = args[i];
      String detail = args[i + 1];
      if (cmd.equals("-in")) {
        inputFile = detail;
      } else if (cmd.equals("-out")) {
        outputFile = detail;
      } else if (cmd.equals("-view")) {
        viewType = detail;
      } else if (cmd.equals("-speed")) {
        speed = Integer.parseInt(detail);
      } else {
        throw new IllegalArgumentException("no such command");
      }
    }
    try {
      AnimationModel model = AnimationReader.parseFile(new FileReader(inputFile),
              new SimpleAnimation.Builder());
      AnimationView view = new ViewCreator().create(viewType, model, outputFile, speed);
      if (viewType.equals("edit")) {
        IController c = new MVCController(model, (InteractiveGraphicsView) view);
        c.playAnimation();
      }
      view.makeVisible();
    } catch (FileNotFoundException e) {
      throw new IllegalStateException("File cannot be found");
    }
  }
}