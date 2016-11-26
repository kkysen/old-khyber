package sen.khyber.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * 
 * 
 * @author Khyber Sen
 * @param <E> element type
 */
public class SuperList<E> extends ArrayList<E> {
    
    /**
     * randomly generated by eclipse
     */
    private static final long serialVersionUID = 2732888534285675100L;
    
    protected Collection<Collection<? super E>> superColls;
    
    public SuperList(final Collection<Collection<? super E>> superColls) {
        this.superColls = superColls;
    }
    
    @SafeVarargs
    public SuperList(final Collection<? super E>... superColls) {
        this.superColls = new ArrayList<>(Arrays.asList(superColls));
    }
    
    @Override
    public boolean add(final E e) {
        super.add(e);
        for (final Collection<? super E> superList : superColls) {
            superList.add(e);
        }
        return true;
    }
    
    @Override
    public boolean remove(final Object obj) {
        for (final Collection<? super E> superList : superColls) {
            superList.remove(obj);
        }
        return super.remove(obj);
    }
    
    public void linkCollection(final Collection<? super E> superColl) {
        superColls.add(superColl);
    }
    
}