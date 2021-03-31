package cs3500.animator.view;

/**
 * A view for animations of shapes: provides textual representations of the animation.
 */
public interface ITextualView extends AnimationView {

  /**
   * Gets the associated representation of the model. Textual view produces the textual
   * representation and SVG view produces the svg/xml presentation of the animation.
   */
  String getView();

}
