package sen.khyber.util.linkedMatrix;

import sen.khyber.tuples.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Supplier;
import java.util.stream.Stream;

import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <E> element type
 */
public abstract class LinkedMatrix<E> implements Cloneable {
    
    public class Node implements Cloneable {
        
        private @Getter E element;
        
        private final @Getter int i;
        private final @Getter int j;
        
        private @Getter Set<Node> neighbors = new HashSet<>();
        
        public Node(final E element, final int i, final int j) {
            this.element = element;
            this.i = i;
            this.j = j;
        }
        
        @Override
        public Node clone() {
            final Node copy = new Node(element, i, j);
            copy.neighbors = new HashSet<>(neighbors);
            return copy;
        }
        
        private E setElement(final E e) {
            final E removed = element;
            element = e;
            return removed;
        }
        
        private void fastSetElement(final E e) {
            element = e;
        }
        
        /*package*/ void link(final int i, final int j) {
            neighbors.add(matrix.get(i).get(j));
        }
        
        /*package*/ void linkUp() {
            link(i - 1, j);
        }
        
        /*package*/ void linkDown() {
            link(i + 1, j);
        }
        
        /*package*/ void linkLeft() {
            link(i, j - 1);
        }
        
        /*package*/ void linkRight() {
            link(i, j + 1);
        }
        
        /*package*/ void linkUpLeft() {
            link(i - 1, j - 1);
        }
        
        /*package*/ void linkUpRight() {
            link(i - 1, j + 1);
        }
        
        /*package*/ void linkDownLeft() {
            link(i + 1, j - 1);
        }
        
        /*package*/ void linkDownRight() {
            link(i + 1, j + 1);
        }
        
        public int distanceTo(final Node other) {
            return Math.abs(i - other.i) + Math.abs(j - other.j);
        }
        
        @Override
        public int hashCode() {
            return element.hashCode();
        }
        
        @Override
        public boolean equals(final Object obj) {
            return element.equals(obj);
        }
        
        @Override
        public String toString() {
            return "Node [element=" + element + ", i=" + i + ", j=" + j + "]";
        }
        
    }
    
    protected @Getter int height;
    protected @Getter int width;
    protected @Getter int size;
    
    protected List<List<Node>> matrix;
    protected List<Node> list;
    
    private LinkedMatrix(final int height, final int width) {
        this.height = height;
        this.width = width;
        size = height * width;
        matrix = new ArrayList<>(height);
        list = new ArrayList<>(size);
        for (int i = 0; i < height; i++) {
            final List<Node> row = new ArrayList<>(width);
            matrix.add(row);
            for (int j = 0; j < width; j++) {
                final Node node = new Node(null, i, j);
                row.add(node);
                list.add(node);
            }
        }
    }
    
    public void setElements(final List<List<E>> elementMatrix) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                get(i, j).fastSetElement(elementMatrix.get(i).get(j));
            }
        }
    }
    
    public void randomizeElements(final Supplier<E> supplier) {
        for (int i = 0; i < size; i++) {
            list.get(i).fastSetElement(supplier.get());
        }
    }
    
    public LinkedMatrix(final List<List<E>> elementMatrix, final int height, final int width) {
        this(height, width);
        setElements(elementMatrix);
        linkNeighbors();
    }
    
    public LinkedMatrix(final Supplier<E> supplier, final int height, final int width) {
        this(height, width);
        randomizeElements(supplier);
        linkNeighbors();
    }
    
    protected LinkedMatrix() {} // for cloning
    
    protected Pair<List<List<Node>>, List<Node>> cloneMatrixAndList() {
        final List<List<Node>> matrixCopy = new ArrayList<>(height);
        final List<Node> listCopy = new ArrayList<>(size);
        for (int i = 0; i < height; i++) {
            final List<Node> rowCopy = new ArrayList<>(width);
            matrixCopy.add(rowCopy);
            for (int j = 0; j < width; j++) {
                final Node nodeCopy = get(i, j).clone();
                rowCopy.add(nodeCopy);
                listCopy.add(nodeCopy);
            }
        }
        return new Pair<List<List<Node>>, List<Node>>(matrixCopy, listCopy);
    }
    
    @Override
    public LinkedMatrix<E> clone() {
        final LinkedMatrix<E> copy = emptyClone();
        copy.height = height;
        copy.width = width;
        copy.size = size;
        final Pair<List<List<Node>>, List<Node>> matrixAndListCopy = cloneMatrixAndList();
        copy.matrix = matrixAndListCopy.getFirst();
        copy.list = matrixAndListCopy.getSecond();
        return copy;
    }
    
    public abstract LinkedMatrix<E> emptyClone();
    
    protected abstract void linkNeighbors();
    
    public Node get(final int i, final int j) {
        return matrix.get(i).get(j);
    }
    
    // sets the element of the Node to e, returning the removed element
    public E set(final int i, final int j, final E e) {
        return matrix.get(i).get(j).setElement(e);
    }
    
    public Node matchOf(final E e) {
        final int listIndex = list.indexOf(e); // TODO write Node#equals method
        if (listIndex == -1) {
            return null;
        }
        return matrix.get(listIndex / height).get(listIndex % height);
    }
    
    public Node lastMatchOf(final E e) {
        final int listIndex = list.lastIndexOf(e);
        if (listIndex == -1) {
            return null;
        }
        return matrix.get(listIndex / height).get(listIndex % height);
    }
    
    public List<Node> matchesOf(final E e) {
        final List<Node> matches = new ArrayList<>();
        for (final Node node : list) {
            if (node.getElement().equals(e)) {
                matches.add(node);
            }
        }
        return matches;
    }
    
    public boolean contains(final E e) {
        return matchOf(e) == null;
    }
    
    public List<Node> toList() {
        return list;
    }
    
    public Stream<Node> parallelStream() {
        return list.parallelStream();
    }
    
    @Override
    public String toString() {
        final StringJoiner sjOuter = new StringJoiner("\n");
        for (final List<Node> row : matrix) {
            final StringJoiner sjInner = new StringJoiner(", ", "[", "]");
            for (final Node node : row) {
                sjInner.add(node.element.toString());
            }
            sjOuter.add(sjInner.toString());
        }
        return sjOuter.toString();
    }
    
}