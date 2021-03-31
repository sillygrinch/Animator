package cs3500.animator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JRadioButton;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.ListSelectionModel;
import javax.swing.AbstractButton;

import java.util.Enumeration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import java.util.Scanner;

import javax.swing.event.ListSelectionListener;

import cs3500.animator.model.KeyMoment;
import cs3500.animator.model.ReadOnlyAnimationModel;
import cs3500.animator.model.Shape;

/**
 * This is an implementation of the InteractiveGraphicsView interface that uses Java Swing to draw
 * the results of the animation on the panel. It draws all the shapes found in the model and is the
 * visual representation of the model. It provides the user to edit the animation in the interface.
 * The interface has choices for the user to pick and choose which shape and its key moments it
 * would like to create, edit and remove.
 */
public class EditView implements InteractiveGraphicsView {

  private AnimationGraphicsView view;
  private ReadOnlyAnimationModel m;
  private int ticksPerSec;

  // start, pause, resume animation
  private JButton startPauseRestartButton;
  private JButton restartButton;
  private JButton loopingButton;
  public DefaultListModel<String> dataForListOfKeyFrames;
  public DefaultListModel<String> dataForListOfShapes;

  JPanel removeShapePanel;

  // for adding/removing shapes
  private JButton removeShapeButton;
  private JLabel removeShapeDisplay;
  private JTextField createInput;
  private JButton createShapeButton;
  private JLabel createShapeDisplay;
  public ButtonGroup createShapeGroup;
  public ButtonGroup removeShapeGroup;

  // some are public for testing purposes
  // for adding/removing/edit keyframes
  private JList listOfRemovableShapes;
  private JList listOfRemovableKeyFrames;
  private JButton removeKeyframeButton;
  private JLabel removeKeyframeDisplay;
  private JButton createKeyframeButton;
  public JTextField shapeInput;
  public JTextField tickInput;
  public JTextField colorInput;
  public JTextField dimensionInput;
  public JTextField positionInput;
  private JLabel createKeyframeDisplay;
  private JButton speedDownButton;
  private JButton speedUpButton;
  // for saving the animation to svg format
  private JButton exportButton;
  private JTextField exportFile;
  private JLabel exportDisplay;
  JSlider slider;

  /**
   * Constructs an TextualView.
   *
   * @param m           the read only model
   * @param ticksPerSec the speed the user would like to initialize the animation with
   */
  public EditView(ReadOnlyAnimationModel m, int ticksPerSec) {

    view = new AnimationGraphicsView(m, ticksPerSec);
    this.m = m;
    this.ticksPerSec = ticksPerSec;
    //button panel
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());
    view.add(buttonPanel, BorderLayout.SOUTH);
    //

    // start, pause and resume button
    this.startPauseRestartButton = new JButton("Press to start");
    this.startPauseRestartButton.setActionCommand("Start");

    buttonPanel.add(this.startPauseRestartButton);

    // restart button
    restartButton = new JButton("Press to restart");
    restartButton.setActionCommand("Restart");

    buttonPanel.add(restartButton, BorderLayout.NORTH);

    // speed up button
    speedUpButton = new JButton("Press to decrease speed");
    speedUpButton.setActionCommand("Speed down");

    buttonPanel.add(speedUpButton);

    // speed down button
    speedDownButton = new JButton("Press to increase speed");
    speedDownButton.setActionCommand("Speed up");
    buttonPanel.add(speedDownButton);

    // looping and unloop button
    this.loopingButton = new JButton("Press to loop");
    this.loopingButton.setActionCommand("Loop");
    loopingButton.addActionListener((ActionEvent e) -> {
      if (e.getActionCommand().equals("Loop")) {
        view.animationPanel.enableLooping();
        loopingButton.setActionCommand("Unloop");
        loopingButton.setText("Press to disable looping");
      } else {
        view.animationPanel.disableLooping();
        loopingButton.setActionCommand("Loop");
        loopingButton.setText("Press to loop");
      }
    });
    buttonPanel.add(this.loopingButton);

    // interactive command panel
    JPanel commandPanel = new JPanel();
    commandPanel.setLayout(new BoxLayout(commandPanel, BoxLayout.PAGE_AXIS));
    view.add(commandPanel, BorderLayout.EAST);

    JScrollPane commandScrollPane = new JScrollPane(commandPanel);
    commandScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    commandScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    commandScrollPane.setPreferredSize(new Dimension(500, 500));
    view.add(commandScrollPane, BorderLayout.EAST);
    // add new shape
    // choose type of shape radio button
    JPanel createShapePanel = new JPanel();
    createShapePanel.setBorder(BorderFactory.createTitledBorder("Create a shape"));
    createShapePanel.setLayout(new BoxLayout(createShapePanel, BoxLayout.PAGE_AXIS));
    JRadioButton[] createRadioButtons = new JRadioButton[2];
    JPanel createShapeButtonPanel = new JPanel();
    createShapeButtonPanel.setLayout(new BoxLayout(createShapeButtonPanel, BoxLayout.PAGE_AXIS));
    createShapeGroup = new ButtonGroup();
    createRadioButtons[0] = new JRadioButton("Rectangle");
    createRadioButtons[0].setSelected(false);
    createShapeButtonPanel.add(createRadioButtons[0]);
    createRadioButtons[1] = new JRadioButton("Circle");
    createRadioButtons[1].setSelected(false);
    createShapeGroup.add(createRadioButtons[0]);
    createShapeGroup.add(createRadioButtons[1]);
    createShapeButtonPanel.add(createRadioButtons[1]);
    // input text field to name the shape
    createInput = new JTextField("Name your shape! :)", 15);
    JPanel createShapeButtonAndTextPanel = new JPanel();
    createShapeButtonAndTextPanel.setLayout(new FlowLayout());
    createShapeButtonAndTextPanel.add(createShapeButtonPanel);
    createShapeButtonAndTextPanel.add(createInput);
    createShapePanel.add(createShapeButtonAndTextPanel);
    // button to execute the creation
    JPanel createShapeButtonAndDisplay = new JPanel();
    createShapeButtonAndDisplay.setLayout(new FlowLayout());
    createShapeButton = new JButton("Create");
    createShapeButton.setActionCommand("create shape");
    createShapeButtonAndDisplay.add(createShapeButton);
    createShapeDisplay = new JLabel("What shapes did you create?");
    createShapeButtonAndDisplay.add(createShapeDisplay);
    createShapePanel.add(createShapeButtonAndDisplay);
    commandPanel.add(createShapePanel);

    // radio buttons to remove shapes
    List<Shape> shapes = m.getShapesInModel();
    removeShapeGroup = new ButtonGroup();
    removeShapePanel = new JPanel();
    removeShapePanel.setBorder(BorderFactory.createTitledBorder("Remove shapes"));
    removeShapePanel.setLayout(new BoxLayout(removeShapePanel, BoxLayout.PAGE_AXIS));
    JRadioButton[] removeRadioButtons = new JRadioButton[shapes.size()];
    for (int i = 0; i < removeRadioButtons.length; i++) {
      removeRadioButtons[i] = new JRadioButton(shapes.get(i).getName());
      removeRadioButtons[i].setSelected(false);
      removeShapeGroup.add(removeRadioButtons[i]);
      removeShapePanel.add(removeRadioButtons[i]);
    }

    // button to execute the removal
    JPanel removeShapeButtonAndDisplay = new JPanel();
    removeShapeButtonAndDisplay.setLayout(new FlowLayout());
    removeShapeButton = new JButton("Remove");
    removeShapeButton.setActionCommand("remove shape");
    removeShapeButtonAndDisplay.add(removeShapeButton);
    removeShapeDisplay = new JLabel("What shapes did you remove?");
    removeShapeButtonAndDisplay.add(removeShapeDisplay);
    removeShapePanel.add(removeShapeButtonAndDisplay);
    commandPanel.add(removeShapePanel);

    // create or edit keyframes using buttons and jtextfield
    JPanel createKeyframePanel = new JPanel();
    createKeyframePanel.setBorder(BorderFactory.createTitledBorder("Create or edit keyframes"));
    createKeyframePanel.setLayout(new BoxLayout(createKeyframePanel, BoxLayout.PAGE_AXIS));
    JLabel instructions1 = new JLabel("You must input a tick.");
    JLabel instructions2 = new JLabel("For existing motions,");
    JLabel instructions3 = new JLabel("please include at least one field.");
    JLabel instructions4 = new JLabel("For new motions,");
    JLabel instructions5 = new JLabel("please include all fields.");
    JLabel instructions6 = new JLabel("You can find the existing motions below");
    JLabel instructions7 = new JLabel("in the removal selection list.");
    createKeyframePanel.add(instructions1);
    createKeyframePanel.add(instructions2);
    createKeyframePanel.add(instructions3);
    createKeyframePanel.add(instructions4);
    createKeyframePanel.add(instructions5);
    createKeyframePanel.add(instructions6);
    createKeyframePanel.add(instructions7);
    JPanel shapeNamePanel = new JPanel();
    shapeNamePanel.setLayout(new FlowLayout());
    JPanel tickPanel = new JPanel();
    tickPanel.setLayout(new FlowLayout());
    JPanel posPanel = new JPanel();
    posPanel.setLayout(new FlowLayout());
    JPanel colorPanel = new JPanel();
    colorPanel.setLayout(new FlowLayout());
    JPanel dimPanel = new JPanel();
    dimPanel.setLayout(new FlowLayout());
    shapeInput = new JTextField("Input shape name e.g. \"Barb\"", 15);
    tickInput = new JTextField("Input tick e.g. \"1\"", 15);
    colorInput = new JTextField("Input R G B e.g. \"0 255 255\"", 15);
    positionInput = new JTextField("Input X Y e.g. \"150 20\"", 15);
    dimensionInput = new JTextField("Input width height e.g. \"25 60\"", 15);
    JLabel shapeNameLabel = new JLabel("Shape Name:");
    JLabel tickLabel = new JLabel("Tick:");
    JLabel colorLabel = new JLabel("Color:");
    JLabel positionLabel = new JLabel("Position:");
    JLabel dimensionLabel = new JLabel("Dimension:");
    shapeNamePanel.add(shapeNameLabel);
    shapeNamePanel.add(shapeInput);
    createKeyframePanel.add(shapeNamePanel);
    tickPanel.add(tickLabel);
    tickPanel.add(tickInput);
    createKeyframePanel.add(tickPanel);
    colorPanel.add(colorLabel);
    colorPanel.add(colorInput);
    createKeyframePanel.add(colorPanel);
    posPanel.add(positionLabel);
    posPanel.add(positionInput);
    createKeyframePanel.add(posPanel);
    dimPanel.add(dimensionLabel);
    dimPanel.add(dimensionInput);
    createKeyframePanel.add(dimPanel);
    // button to execute the creation
    JPanel createKeyframeButtonAndDisplay = new JPanel();
    createKeyframeButtonAndDisplay.setLayout(new FlowLayout());
    createKeyframeButton = new JButton("Create");
    createKeyframeButton.setActionCommand("create keyframe");
    createKeyframeButtonAndDisplay.add(createKeyframeButton);
    createKeyframeDisplay = new JLabel("What keyframes did you create?");
    createKeyframeButtonAndDisplay.add(createKeyframeDisplay);
    createKeyframePanel.add(createKeyframeButtonAndDisplay);
    commandPanel.add(createKeyframePanel);

    // Selection lists to remove keyframes
    JPanel removeSelectionListPanel = new JPanel();
    removeSelectionListPanel.setBorder(BorderFactory.createTitledBorder("Remove keyframes"));
    removeSelectionListPanel
        .setLayout(new BoxLayout(removeSelectionListPanel, BoxLayout.PAGE_AXIS));
    // shape selection list
    JPanel twoSelectionListPanel = new JPanel();
    twoSelectionListPanel.setLayout(new FlowLayout());
    dataForListOfShapes = new DefaultListModel<>();
    for (Shape s : shapes) {
      dataForListOfShapes.addElement(s.getName());
    }

    listOfRemovableShapes = new JList<>(dataForListOfShapes);
    listOfRemovableShapes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    listOfRemovableShapes.setFixedCellWidth(100);
    twoSelectionListPanel.add(listOfRemovableShapes);
    // keyframe selection list
    dataForListOfKeyFrames = new DefaultListModel<>();
    if (listOfRemovableShapes.getSelectedIndex() >= 0) {
      Shape shapeToDisplay = shapes.get(listOfRemovableShapes.getSelectedIndex());
      for (KeyMoment k : shapeToDisplay.getKeyMoments()) {
        dataForListOfKeyFrames.addElement("Tick: " + k.getTick() + " "
            + ", Color (R,G,B): (" + k.getColor().getR() + "," + k.getColor().getG()
            + "," + k.getColor().getB() + "), Position (x,y): (" + k.getPos().getX() + ","
            + k.getPos().getY() + ")");
      }
    }
    listOfRemovableKeyFrames = new JList<>(dataForListOfKeyFrames);
    listOfRemovableKeyFrames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    twoSelectionListPanel.add(new JScrollPane(listOfRemovableKeyFrames));
    removeSelectionListPanel.add(twoSelectionListPanel);
    // button to execute the removal
    JPanel removeKeyframeButtonAndDisplay = new JPanel();
    removeKeyframeButtonAndDisplay.setLayout(new FlowLayout());
    removeKeyframeButton = new JButton("Remove");
    removeKeyframeButtonAndDisplay.add(removeKeyframeButton);
    removeKeyframeButton.setActionCommand("remove keyframe");
    removeKeyframeDisplay = new JLabel("What keyframes did you remove?");
    removeKeyframeButtonAndDisplay.add(removeKeyframeDisplay);
    removeSelectionListPanel.add(removeKeyframeButtonAndDisplay);
    commandPanel.add(removeSelectionListPanel);

    exportButton = new JButton("Save Animation");
    exportButton.setActionCommand("export SVG");
    exportFile = new JTextField("Input file path", 15);
    exportDisplay = new JLabel("You can save your beautiful animation here!");
    JPanel exportPanel = new JPanel();
    exportPanel.setBorder(BorderFactory.createTitledBorder("Save your animation"));
    exportPanel.setLayout(new FlowLayout());
    exportPanel.add(exportFile);
    exportPanel.add(exportButton);
    exportPanel.add(exportDisplay);
    commandPanel.add(exportPanel);

    this.slider = new JSlider(0, 500, 5);
    slider.setAutoscrolls(true);
    int largestTick = -1;
    for (Shape s : m.getShapesInModel()) {
      List<KeyMoment> km = s.getKeyMoments();
      if (km.size() > 0) {

        int thisTick = km.get(km.size() - 1).getTick();
        if (thisTick > largestTick) {
          largestTick = thisTick;
        }
      }
    }
    slider.setMaximum(largestTick);
    slider.addChangeListener(e -> {
      if (slider.getValueIsAdjusting()) { //if true event was generated by user.
        this.changeFrame(slider.getValue());
      }
    });

    JPanel panelCenter = new JPanel();
    panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.X_AXIS));
    this.slider.setPreferredSize(new Dimension(500, 30));
    this.slider.setPaintTicks(true);
    this.slider.setPaintTrack(true);
    panelCenter.setMinimumSize(new Dimension(500, 30));
    panelCenter.add(this.slider);
    buttonPanel.add(panelCenter, BorderLayout.CENTER);
    slider.setMinorTickSpacing(ticksPerSec / 2);
    slider.setMajorTickSpacing(ticksPerSec);
  }

  private void changeFrame(int value) {
    int largestTick = -1;
    for (Shape s : m.getShapesInModel()) {
      List<KeyMoment> km = s.getKeyMoments();
      if (km.size() > 0) {

        int thisTick = km.get(km.size() - 1).getTick();
        if (thisTick > largestTick) {
          largestTick = thisTick;
        }
      }
    }
    if (value >= 0 && value <= largestTick) {
      view.setTick(value);
    }

  }


  @Override
  public void setActionListener(ActionListener listener) {
    createShapeButton.addActionListener(listener);
    startPauseRestartButton.addActionListener(listener);
    removeShapeButton.addActionListener(listener);
    createKeyframeButton.addActionListener(listener);
    removeKeyframeButton.addActionListener(listener);
    exportButton.addActionListener(listener);
    restartButton.addActionListener(listener);
    speedUpButton.addActionListener(listener);
    speedDownButton.addActionListener(listener);
  }

  @Override
  public void setListSelectionListener(ListSelectionListener listener) {
    listOfRemovableShapes.addListSelectionListener(listener);
  }

  @Override
  public void refresh() {
    refreshRemoveButtons();
    refreshRemoveKeyFrameButtons();
    refreshSelectionList();
    view.refresh();
    slider.setValue(view.getCurrentTick());
  }

  /**
   * Refreshes the panel with buttons that represents the current list of shapes found in the
   * model.
   */
  private void refreshRemoveButtons() {
    removeShapePanel.removeAll();
    List<Shape> shapes = m.getShapesInModel();
    JRadioButton[] removeRadioButtons = new JRadioButton[shapes.size()];
    for (int i = 0; i < removeRadioButtons.length; i++) {
      removeRadioButtons[i] = new JRadioButton(shapes.get(i).getName());
      removeRadioButtons[i].setSelected(false);
      removeShapeGroup.add(removeRadioButtons[i]);
      removeShapePanel.add(removeRadioButtons[i]);
    }
    // button to execute the removal
    JPanel removeShapeButtonAndDisplay = new JPanel();
    removeShapeButtonAndDisplay.setLayout(new FlowLayout());
    removeShapeButtonAndDisplay.add(removeShapeButton);
    removeShapeButtonAndDisplay.add(removeShapeDisplay);
    removeShapePanel.add(removeShapeButtonAndDisplay);
  }

  @Override
  public void refreshRemoveKeyFrameButtons() {
    dataForListOfKeyFrames.clear();
    if (listOfRemovableShapes.getSelectedIndex() >= 0) {
      Shape shapeToDisplay = m.getShapesInModel().get(listOfRemovableShapes.getSelectedIndex());
      for (KeyMoment k : shapeToDisplay.getKeyMoments()) {
        dataForListOfKeyFrames.addElement("Tick: " + k.getTick() + " "
            + ", Color (R,G,B): (" + k.getColor().getR() + "," + k.getColor().getG()
            + "," + k.getColor().getB() + "), Position (x,y): (" + k.getPos().getX() + ","
            + k.getPos().getY() + ")");
      }
    }
  }

  /**
   * Refreshes the panel with the current list of shapes found in the model.
   */
  private void refreshSelectionList() {
    dataForListOfShapes.clear();
    for (Shape s : m.getShapesInModel()) {
      dataForListOfShapes.addElement(s.getName());
    }
  }


  @Override
  public void makeVisible() {
    view.setVisible(true);
  }

  @Override
  public String getShapeText() {
    return createInput.getText();
  }

  @Override
  public String getShapeNameText() {
    return shapeInput.getText();
  }

  @Override
  public String getShapePosXYText() {
    return positionInput.getText();
  }

  @Override
  public String getShapeColRGBText() {
    return colorInput.getText();
  }

  @Override
  public String getShapeDimText() {
    return dimensionInput.getText();
  }

  @Override
  public String getShapeTickText() {
    return tickInput.getText();
  }

  @Override
  public String getShapeType() {
    return getSelectedButtonText(createShapeGroup);
  }

  @Override
  public String getShapeSelected() {
    return getSelectedButtonText(removeShapeGroup);
  }

  @Override
  public String getShapeToRemove() {
    return (String) listOfRemovableShapes.getSelectedValue();
  }

  @Override
  public int getShapeToRemoveTick() {
    Scanner sc = new Scanner((String) listOfRemovableKeyFrames.getSelectedValue());
    sc.next();
    String i = sc.next();
    int j;
    try {
      j = Integer.parseInt(i);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("");
    }
    return j;
  }

  @Override
  public void updateCreateShapeDisplay(String name, String type) {
    createShapeDisplay.setText("You have created a " + type + " named " + name);
  }

  @Override
  public void updateRemoveShapeDisplay(String removeThis) {
    removeShapeDisplay.setText("You have removed the shape named " + removeThis);
  }

  @Override
  public void updateCreateKeyframeDisplay(String nameShape, String tickText, String posn,
      String dim, String color) {
    createKeyframeDisplay.setText("You have added or modified the keyframe of " + nameShape
        + " at time " + tickText + " with position: " + posn + " dimensions: " + " color: "
        + color);
  }

  @Override
  public void updateRemoveKeyframeDisplay(String shapeName, int tickToRemove) {
    removeKeyframeDisplay.setText("You have removed the keyframe of " + shapeName + " at time "
        + tickToRemove);
  }

  /**
   * Returns the textual representation of the selected button in the button group.
   *
   * @param buttonGroup   the given button group
   * @return  the textual representation of the button selected or null if none selected in the
   *          button group.
   */
  private String getSelectedButtonText(ButtonGroup buttonGroup) {
    for (Enumeration<AbstractButton> buttons = buttonGroup.getElements();
        buttons.hasMoreElements(); ) {
      AbstractButton button = buttons.nextElement();
      if (button.isSelected()) {
        return button.getText();
      }
    }
    return null;
  }

  @Override
  public void saveSVGFile() {
    new SVGView(m, exportFile.getText(), ticksPerSec).makeVisible();
    updateSaveDisplay();
  }

  /**
   * Updates the display to notify the user that they have successfully saved the animation.
   */
  private void updateSaveDisplay() {
    exportDisplay.setText("You have successfully saved your animation to " + exportFile.getText());
  }

  public int getTicksPerSec() {
    return this.ticksPerSec;
  }

  public void setStartResume() {
    startPauseRestartButton.setText("Press to resume");
    startPauseRestartButton.setActionCommand("Resume");
  }

  public void setPause() {
    startPauseRestartButton.setText("Press to pause");
    startPauseRestartButton.setActionCommand("Pause");
  }

  public void setRestart() {
    view.animationPanel.setTickToZero();
    slider.setValue(0);
  }

}


