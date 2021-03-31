package cs3500.animator.controller;

/**
 * Represents an interface for controllers used to determine the actions of users in interactive
 * views: handle user edits of the animation by executing them using the model; convey edit outcomes
 * to the user in some form, both textually and visually.
 */
public interface IController {

  /**
   * Executes the animation by setting the listeners and making the view visible.
   */
  void playAnimation();
}

