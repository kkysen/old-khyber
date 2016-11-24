package sen.khyber.util.linkedMatrix;

import java.util.List;
import java.util.function.Supplier;


public class AllLinkedMatrix<E> extends LinkedMatrix<E> {
    
    public AllLinkedMatrix(final List<List<E>> elementMatrix, final int height, final int width) {
        super(elementMatrix, height, width);
    }
    
    public AllLinkedMatrix(final Supplier<E> supplier, final int height, final int width) {
        super(supplier, height, width);
    }
    
    private AllLinkedMatrix() {}
    
    @Override
    public AllLinkedMatrix<E> emptyClone() {
        return new AllLinkedMatrix<E>();
    }
    
    @Override
    protected void linkNeighbors() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                linkSingleNode(get(i, j));
            }
        }
    }
    
    private void linkSingleNode(final AllLinkedMatrix<E>.Node node) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                node.link(i, j);
            }
        }
        node.getNeighbors().remove(node);  // remove link to self
    }
    
}
