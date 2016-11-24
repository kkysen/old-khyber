package sen.khyber.apcs.abstractClasses;

public class Square extends Rectangle {
    
    public Square(final double length) {
        super(length, length);
    }
    
    public Square() {
        super();
    }
    
    @Override
    public void setLength(final double length) {
        super.setLength(length);
        super.setWidth(length);
    }
    
    @Override
    public void setWidth(final double width) {
        super.setWidth(width);
        super.setLength(width);
    }
    
    @Override
    public String toString() {
        return "A " + Square.class.getSimpleName() + " with side=" + getLength()
                + ", which is a subclass of " + super.toString();
    }
    
}
