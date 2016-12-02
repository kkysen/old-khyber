package sen.khyber.apcs.superArray;

import java.util.Collection;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class SuperArray extends MyArrayList<String> {
    
    public SuperArray(final int initialCapacity) {
        super(initialCapacity);
    }
    
    public SuperArray() {}
    
    public SuperArray(final Collection<? extends String> coll) {
        super(coll);
    }
    
    public SuperArray(final MyArrayList<? extends String> list) {
        super(list);
    }
    
    public static void main(String[] args) {
    SuperArray data = new SuperArray();
    for (int i = 0; i < 26; i++) {
      data.add(""+(char)('A'+i%26));
    }

    System.out.println(data);
    System.out.println("Standard loop:");

    for(int n = 0; n < data.size(); n++){
      System.out.print(data.get(n)+" ");
    }
    System.out.println();
    System.out.println("for-each loop:");
    for(String s : data){
      System.out.print(s+" ");
    }
  }
    
}
