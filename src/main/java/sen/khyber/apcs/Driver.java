package sen.khyber.apcs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * HW 05
 * 
 * @author Khyber Sen
 */
public final class Driver {
    
    public static void main(final String[] args) {
        
        HomeworkTest.main(new String[0]);
        System.out.println();
        
        Student.main(new String[0]);
        System.out.println();
        
        final List<Integer> list = new ArrayList<>();
        list.add(4);
        list.add(3);
        list.add(null);
        Collections.sort(list);
        System.out.println(list);
    }
    
}
