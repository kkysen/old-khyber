package sen.khyber.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class SuperMap<K, V> extends HashMap<K, V> {
    
    /**
     * randomly generated by eclipse
     */
    private static final long serialVersionUID = -3342756662367407767L;
    
    protected Collection<Collection<? super V>> superColls;
    
    public SuperMap(Collection<Collection<? super V>> superColls) {
        this.superColls = superColls;
    }
    
    @SafeVarargs
    public SuperMap(Collection<? super V>... superColls) {
        this.superColls = new ArrayList<>(Arrays.asList(superColls));
    }
    
    @Override
    public V put(K key, V value) {
        for (Collection<? super V> superList : superColls) {
            superList.add(value);
        }
        return super.put(key, value);
    }
    
    @Override
    public V remove(Object obj) {
        for (Collection<? super V> superList : superColls) {
            superList.remove(obj);
        }
        return super.remove(obj);
    }
    
    public void linkCollection(Collection<? super V> superColl) {
        superColls.add(superColl);
    }
    
}