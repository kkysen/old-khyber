package sen.khyber.util.linkedMatrix;

import java.util.List;
import java.util.function.Supplier;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <E> element type
 */
public class AdjacentLinkedMatrix<E> extends LinkedMatrix<E> {
    
    public AdjacentLinkedMatrix(final List<List<E>> elementMatrix, final int height,
            final int width) {
        super(elementMatrix, height, width);
    }
    
    public AdjacentLinkedMatrix(final Supplier<E> supplier, final int height, final int width) {
        super(supplier, height, width);
    }
    
    private AdjacentLinkedMatrix() {} // for cloning
    
    @Override
    public AdjacentLinkedMatrix<E> emptyClone() {
        return new AdjacentLinkedMatrix<E>();
    }
    
    @Override
    protected void linkNeighbors() {
        linkInterior();
        linkSides();
        linkCorners();
    }
    
    private void linkInterior() {
        for (int i = 1; i < height - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                final AdjacentLinkedMatrix<E>.Node node = get(i, j);
                node.linkUp();
                node.linkDown();
                node.linkLeft();
                node.linkRight();
                node.linkUpLeft();
                node.linkUpRight();
                node.linkDownLeft();
                node.linkDownRight();
            }
        }
    }
    
    private void linkSides() {
        for (int i = 1; i < height - 1; i++) {
            final AdjacentLinkedMatrix<E>.Node leftNode = get(i, 0);
            leftNode.linkUp();
            leftNode.linkDown();
            leftNode.linkRight();
            leftNode.linkUpRight();
            leftNode.linkDownRight();
            
            final AdjacentLinkedMatrix<E>.Node rightNode = get(i, width - 1);
            rightNode.linkUp();
            rightNode.linkDown();
            rightNode.linkLeft();
            rightNode.linkUpLeft();
            rightNode.linkDownLeft();
        }
        for (int j = 1; j < width - 1; j++) {
            final AdjacentLinkedMatrix<E>.Node upNode = get(0, j);
            upNode.linkDown();
            upNode.linkLeft();
            upNode.linkRight();
            upNode.linkDownLeft();
            upNode.linkDownRight();
            
            final AdjacentLinkedMatrix<E>.Node downNode = get(height - 1, j);
            downNode.linkUp();
            downNode.linkLeft();
            downNode.linkRight();
            downNode.linkUpLeft();
            downNode.linkUpRight();
        }
    }
    
    private void linkCorners() {
        final AdjacentLinkedMatrix<E>.Node upLeftNode = get(0, 0);
        upLeftNode.linkDown();
        upLeftNode.linkRight();
        upLeftNode.linkDownRight();
        
        final AdjacentLinkedMatrix<E>.Node upRightNode = get(0, width - 1);
        upRightNode.linkDown();
        upRightNode.linkLeft();
        upRightNode.linkDownLeft();
        
        final AdjacentLinkedMatrix<E>.Node downLeftNode = get(height - 1, 0);
        downLeftNode.linkUp();
        downLeftNode.linkRight();
        downLeftNode.linkUpRight();
        
        final AdjacentLinkedMatrix<E>.Node downRightNode = get(height - 1, width - 1);
        downRightNode.linkUp();
        downRightNode.linkLeft();
        downRightNode.linkUpLeft();
    }
    
}
