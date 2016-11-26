package sen.khyber.optimization.sa;

/**
 * 
 * 
 * @author Khyber Sen
 */
public interface State extends Cloneable {
    
    public void step();
    
    public void undo();
    
    public double energy();
    
    public State clone();
    
    @Override
    public String toString();
    
}
