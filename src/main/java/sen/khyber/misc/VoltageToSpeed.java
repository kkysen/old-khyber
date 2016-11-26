package sen.khyber.misc;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class VoltageToSpeed {
    
    private static final double LIGHT_SPEED = 299792458;
    private static final double ELEMENTAL_CHARGE = 1.6e-19;
    private static final double ELECTRON_CHARGE = -ELEMENTAL_CHARGE;
    private static final double ELECTRON_MASS = 9.11e-31;
    
    public static double calcSpeedFromKineticEnergy(final double mass, final double kineticEnergy) {
        final double c2 = LIGHT_SPEED * LIGHT_SPEED;
        final double mc2 = mass * c2;
        final double fraction = mc2 / (kineticEnergy + mc2);
        return Math.sqrt(c2 * (1 - fraction * fraction));
    }
    
    public static double calcElectronSpeedFromKineticEnergy(final double kineticEnergy) {
        return calcSpeedFromKineticEnergy(ELECTRON_MASS, kineticEnergy);
    }
    
    public static double calcKineticEnergy(final double charge, final double voltage) {
        return -charge * voltage;
    }
    
    public static double calcElectronKineticEnergy(final double voltage) {
        return calcKineticEnergy(ELECTRON_CHARGE, voltage);
    }
    
    public static double calcSpeedFromVoltage(final double mass, final double charge,
            final double voltage) {
        return calcSpeedFromKineticEnergy(mass, calcKineticEnergy(charge, voltage));
    }
    
    public static double calcElectronSpeedFromVoltage(final double voltage) {
        return calcSpeedFromVoltage(ELECTRON_MASS, ELECTRON_CHARGE, voltage);
    }
    
    public static double calcElectronSpeedFromVoltageClassically(final double voltage) {
        final double KE = calcElectronKineticEnergy(voltage);
        return Math.sqrt(2 * KE / ELECTRON_MASS);
    }
    
    public static double percentError(final double accepted, final double experimental) {
        return 100 * (accepted - experimental) / accepted;
    }
    
    public static double classicVsRelativisticError(final double voltage) {
        final double speed1 = calcElectronSpeedFromVoltage(voltage);
        final double speed2 = calcElectronSpeedFromVoltageClassically(voltage);
        return percentError(speed1, speed2);
    }
    
    public static void printClassicalError(final double voltage) {
        System.out.println("voltage: " + voltage);
        final double speed = calcElectronSpeedFromVoltage(voltage);
        System.out.println("speed: " + speed);
        System.out.println("fraction of speed of light: " + speed / LIGHT_SPEED);
        System.out.println("percent error: " + classicVsRelativisticError(voltage) + "%");
        System.out.println();
    }
    
    public static void main(final String[] args) {
        for (double voltage = 0; voltage <= 1e6; voltage += 1e4) {
            printClassicalError(voltage);
        }
    }
    
}
