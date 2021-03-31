package cs3500.animator.view;

/**
 * A view for animations of shapes: display shapes on the panel and provide visual interface for
 * users.
 */
public interface IVisualView extends AnimationView {

  /**
   * Refresh the view to reflect any changes in the game state.
   */
  void refresh();

}
