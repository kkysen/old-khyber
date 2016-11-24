package sen.khyber.apcs;

// HW 03

import java.util.Random;

public class HomeworkTest {
    
    private static final Random random = new Random();
    
    public static double celsiusToFahrenheit(double celsius) {
        return celsius * 9 / 5 + 32;
    }
    
    public static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5 / 9;
    }
    
    public static void main(String[] args) {
        int numRepeats = 10;
        double maxTemp = 1000;
        double epsilon = 0.01;
        int argsLength = (args.length > 3) ? 3 : args.length;
        switch (argsLength) {
            case 3:
                epsilon = Double.parseDouble(args[2]);
            case 2:
                maxTemp = Double.parseDouble(args[1]);
            case 1:
                numRepeats = Integer.parseInt(args[0]);
        }
        
        /*int numRepeats = 10;
        if (args.length >= 1) {
            numRepeats = Integer.parseInt(args[0]);
        }
        double maxTemp = 1000;
        if (args.length >= 2) {
            maxTemp = Double.parseDouble(args[1]);
        }
        double epsilon = 0.01;
        if (args.length >= 3) {
            epsilon = 0.01
        }*/
        for (int i = 0; i < numRepeats; i++) {
            double originalCelsius = (double) random.nextInt((int) maxTemp);
            double calculatedFahrenheit = celsiusToFahrenheit(originalCelsius);
            double calculatedCelsius = fahrenheitToCelsius(calculatedFahrenheit);
            if (Math.abs(originalCelsius - calculatedCelsius) < epsilon) {
                System.out.println(calculatedCelsius + " C = " + calculatedFahrenheit + " F");
            } else {
                throw new AssertionError(originalCelsius + " C does not equal " + calculatedCelsius + " C");
            }
        }
        //System.out.println(celsiusToFahrenheit(100.0)); // 212.0
        //System.out.println(fahrenheitToCelsius(-40.0)); // -40.0
    }

}
