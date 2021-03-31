package cs3500.animator.view;


import java.awt.Dimension;


import javax.swing.JFrame;

import javax.swing.JScrollPane;

import cs3500.animator.model.ReadOnlyAnimationModel;

/**
 * This is an implementation of the IVisualView interface that uses Java Swing to draw the results
 * of the animation on the panel. It draws all the shapes found in the model and is the visual
 * representation of the model.
 */
public class AnimationGraphicsView extends JFrame implements IVisualView {


  protected AnimationPanel animationPanel;


  /**
   * Constructs an AnimationGraphicsView.
   *
   * @param m           the read only model
   * @param ticksPerSec the tempo for the animation which the user indicates
   */
  public AnimationGraphicsView(ReadOnlyAnimationModel m, int ticksPerSec) {
    super();
    animationPanel = new AnimationPanel(m, 0);

    this.setTitle("Simple Animation");
    this.setSize(1000, 1000);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    animationPanel.setPreferredSize(new Dimension(1000, 1000));
    JScrollPane scroll = new JScrollPane(animationPanel);
    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scroll.setBounds(0, 0, 0, 0);
    scroll.setPreferredSize(new Dimension(1000, 1000));
    this.add(scroll);
    this.pack();

  }

  @Override
  public void refresh() {
    animationPanel.setEndTick();
    animationPanel.repaint();
    this.repaint();
  }

  @Override
  public void makeVisible() {
    this.setVisible(true);
  }


  public void setTick(int value) {
    animationPanel.setTick(value);

  }

  public int getCurrentTick() {
    return animationPanel.getCurrentTick();
  }


}
