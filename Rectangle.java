package cs3500.animator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;

/**
 * Represents a Rectangle shape that can be used in an animation.
 */
public class Rectangle extends AbstractShape {

  /**
   * Constructs an Rectangle, represented by its name and its key moments of an animation.
   *
   * @param name       name of the Rectangle shape
   * @param keyMoments its key moments during an animation
   */
  public Rectangle(String name, List<KeyMoment> keyMoments) {
    super(name, keyMoments);
  }

  /**
   * Constructs an Rectangle, represented by its name with an empty list of key moments.
   *
   * @param name name of the Rectangle shape
   */
  public Rectangle(String name) {
    super(name);
  }

  /**
   * Copy constructor for a Rectangle.
   *
   * @param s the rectangle to copy
   */
  public Rectangle(AbstractShape s) {
    this(s.name, s.keyMoments);
  }

  @Override
  public Shape createCopyShape() {
    return new Rectangle(this);
  }

  @Override
  public Shape getKeyMomentsAtTick(int tick) {
    if (this.keyMoments.size() > 0) {
      if ((this.keyMoments.get(0).getTick() > tick)
              || (this.keyMoments.get(this.keyMoments.size() - 1).getTick() < tick)) {
        return null;
      } else {
        NavigableMap<Integer, KeyMoment> map = makeMap();
        List<KeyMoment> keyM = new ArrayList<>();
        if (map.floorEntry(tick).getValue().equals(map.ceilingEntry(tick).getValue())) {
          keyM.add(map.floorEntry(tick).getValue());
        } else {
          keyM.add(map.floorEntry(tick).getValue());
          keyM.add(map.ceilingEntry(tick).getValue());
        }
        return new Rectangle(this.name, keyM);
      }
    } else {
      return null;
    }
  }
}
