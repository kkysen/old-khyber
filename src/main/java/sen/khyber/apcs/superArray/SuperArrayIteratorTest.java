package sen.khyber.apcs.superArray;

/**
 * 
 * 
 * @author Samuel Konstantinovich
 */
public class SuperArrayIteratorTest {
    
    public static void main(final String[] args) {
        final SuperArray data = new SuperArray();
        int i = 0;
        while (i < 26) {
            data.add("" + (char) ('A' + i % 26));
            i++;
        }
        
        System.out.println(data);
        System.out.println("Standard loop:");
        
        for (int n = 0; n < data.size(); n++) {
            System.out.print(data.get(n) + " ");
        }
        System.out.println();
        System.out.println("for-each loop:");
        for (final String s : data) {
            System.out.print(s + " ");
        }
    }
    
}
