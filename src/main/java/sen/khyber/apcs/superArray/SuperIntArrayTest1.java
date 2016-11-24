package sen.khyber.apcs.superArray;

/**
 * 
 * 
 * @author Adam Abbas
 */
public class SuperIntArrayTest1 {
    
    public static boolean testAdd1(final SuperIntArray supAr, final int n) {
        final int oldSize = supAr.size();
        supAr.add(n);
        final int[] added = supAr.toIntArray();
        if (added[oldSize] == n && oldSize + 1 == supAr.size()) {
            return true;
        } else if (added[oldSize] != n) {
            System.out.println("Add didn't append the object to the end of the array!");
            return false;
        } else if (oldSize + 1 == supAr.size()) {
            System.out.println("You didn't add 1 to size!");
            return false;
        }
        return false;
    }
    
    public static boolean testGet() {
        final SuperIntArray test2 = new SuperIntArray();
        test2.add(1);
        test2.add(3);
        test2.add(5);
        return test2.get(0) == 1 && test2.get(1) == 3 && test2.get(2) == 5;
    }
    
    public static boolean testClear(final SuperIntArray supAr) {
        supAr.clear();
        if (supAr.size() == 0) {
            return supAr.size() == 0;
        } else {
            System.out.println("Your clear() didn't set the size to 0!");
            return false;
        }
    }
    
    public static boolean testSet() {
        final SuperIntArray test = new SuperIntArray();
        test.add(5);
        test.add(3);
        test.add(2);
        test.set(0, 1);
        test.set(1, 2);
        test.set(2, 3);
        return test.get(0) == 1 && test.get(1) == 2 & test.get(2) == 3;
    }
    
    public static boolean testAdd2() {
        final SuperIntArray test = new SuperIntArray();
        test.add(1);
        test.add(2);
        test.add(3);
        test.add(1, 5);
        return test.get(1) == 5;
        
    }
    
    public static boolean testIndexOf() {
        final SuperIntArray test = new SuperIntArray();
        test.add(5);
        test.add(3);
        test.add(2);
        test.add(3);
        test.add(4);
        test.add(3);
        return test.indexOf(3) == 1;
    }
    
    public static boolean testLastIndexOf() {
        final SuperIntArray test = new SuperIntArray();
        test.add(5);
        test.add(3);
        test.add(2);
        test.add(3);
        test.add(4);
        test.add(3);
        return test.lastIndexOf(3) == 5;
    }
    
    public static void main(final String[] args) {
        final SuperIntArray test1 = new SuperIntArray();
        
        boolean testBool = true;
        
        //Testing add(int element)//
        
        testBool = testBool && testAdd1(test1, 5);
        testBool = testBool && testAdd1(test1, 6);
        testBool = testBool && testAdd1(test1, 0);
        testBool = testBool && testAdd1(test1, -3);
        
        //Testing get, this assumes your add is working!!//
        testBool = testGet();
        
        /*Not gonna bother testing size...
        And each String method is different so uncomment the next line if you wanna print the previous SuperIntArray that should be [5, 6, 0, -3]*/
        //System.out.println(test1);
        
        //Testing clear()!//
        testBool = testBool && testClear(test1);
        
        //Testing isEmpty() [assuming clear() works]//
        testBool = testBool && test1.isEmpty();
        
        //Testing set! [assumes get() works]//
        testBool = testBool && testSet();
        
        //Testing the other add//
        testBool = testBool && testAdd2();
        
        //Testing indexOf()//
        testBool = testBool && testIndexOf();
        
        //testing lastIndexOf()//
        testBool = testBool && testLastIndexOf();
        
        System.out.println(testBool);
    }
    
}