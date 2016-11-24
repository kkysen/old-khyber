package sen.khyber.misc;


public class VoltageToSpeed {
    
    private static final double LIGHT_SPEED = 299792458;
    private static final double ELEMENTAL_CHARGE = 1.6e-19;
    private static final double ELECTRON_CHARGE = - ELEMENTAL_CHARGE;
    private static final double ELECTRON_MASS = 9.11e-31;
    
    public static double calcSpeedFromKineticEnergy(double mass, double kineticEnergy) {
        double c2 = LIGHT_SPEED * LIGHT_SPEED;
        double mc2 = mass * c2;
        double fraction = mc2 / (kineticEnergy + mc2);
        return Math.sqrt(c2 * (1 - fraction * fraction));
    }
    
    public static double calcElectronSpeedFromKineticEnergy(double kineticEnergy) {
        return calcSpeedFromKineticEnergy(ELECTRON_MASS, kineticEnergy);
    }
    
    public static double calcKineticEnergy(double charge, double voltage) {
        return - charge * voltage;
    }
    
    public static double calcElectronKineticEnergy(double voltage) {
        return calcKineticEnergy(ELECTRON_CHARGE, voltage);
    }
    
    public static double calcSpeedFromVoltage(double mass, double charge, double voltage) {
        return calcSpeedFromKineticEnergy(mass, calcKineticEnergy(charge, voltage));
    }
    
    public static double calcElectronSpeedFromVoltage(double voltage) {
        return calcSpeedFromVoltage(ELECTRON_MASS, ELECTRON_CHARGE, voltage);
    }
    
    public static double calcElectronSpeedFromVoltageClassically(double voltage) {
        double KE = calcElectronKineticEnergy(voltage);
        return Math.sqrt(2 * KE / ELECTRON_MASS);
    }
    
    public static double percentError(double accepted, double experimental) {
        return 100 * (accepted - experimental) / accepted;
    }
    
    public static double classicVsRelativisticError(double voltage) {
        double speed1 = calcElectronSpeedFromVoltage(voltage);
        double speed2 = calcElectronSpeedFromVoltageClassically(voltage);
        return percentError(speed1, speed2);
    }
    
    public static void printClassicalError(double voltage) {
        System.out.println("voltage: " + voltage);
        double speed = calcElectronSpeedFromVoltage(voltage);
        System.out.println("speed: " + speed);
        System.out.println("fraction of speed of light: " + speed / LIGHT_SPEED);
        System.out.println("percent error: " + classicVsRelativisticError(voltage) + "%");
        System.out.println();
    }
    
    public static void main(String[] args) {
        for (double voltage = 0; voltage <= 1e6; voltage += 1e4) {
            printClassicalError(voltage);
        }
    }
    
}
