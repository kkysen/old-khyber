package sen.khyber.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * parameterized calculations for 
 * AP Phys Lab #3: Missile Motion and the Gravitational Field,
 * specifically in the parameterization of the error prone ball diameter
 * 
 * 
 * @author Khyber Sen
 * 
 */
public class PhysicsGravityLab {
    
    private static final double
        GRAVITY = -9.8,
        TUBE_LENGTH = 0.4118,
        DIST_TABLE_TO_PAPER = 0.288,
        TABLE_HEIGHT = 0.917,
        MAX_BALL_DIAMETER = 0.0254,
        RADIUS = MAX_BALL_DIAMETER / 2;
    
    private final double
        frontHeight,
        backHeight,
        distGateToTable,
        paperDist,
        gateTime;
    
    private double ballDiameter;
    
    public PhysicsGravityLab(double frontHeight, double backHeight, 
            double distGateToTable, double paperDist, double gateTime) {
        this.frontHeight = frontHeight;
        this.backHeight = backHeight;
        this.distGateToTable = distGateToTable;
        this.paperDist = paperDist;
        this.gateTime = gateTime;
    }
    
    private double tubeHeight() {
        return backHeight - frontHeight;
    }
    
    private double tubeAngle() {
        return Math.asin(tubeHeight() / TUBE_LENGTH);
    }
    
    private double horizDist() {
        return distGateToTable + DIST_TABLE_TO_PAPER + paperDist;
    }
    
    private double vertDist() {
        return - (TABLE_HEIGHT + frontHeight);
    }
    
    private double ballSpeed() {
        return ballDiameter / gateTime;
    }
    
    private double horizVelocity() {
        return ballSpeed() * Math.cos(tubeAngle());
    }
    
    private double vertVelocity() {
        return - (ballSpeed() * Math.sin(tubeAngle()));
    }
    
    private double fallTime() {
        return horizDist() / horizVelocity();
    }
    
    private double calcGravity() {
        double fallTime = fallTime();
        return 2 * (vertDist() - (vertVelocity() * fallTime)) / (fallTime * fallTime);
    }
    
    public double percentError1(double ballDiameter) {
        this.ballDiameter = ballDiameter;
        double percentDecimal = (GRAVITY - calcGravity()) / GRAVITY;
        int truncatedPercent = (int) (percentDecimal * 100000);
        return truncatedPercent / 100000.0;
    }
    
    public List<Double> enumeratePercentErrors1(double minBallDiameter, double step) {
        int numTests = (int) ((MAX_BALL_DIAMETER - minBallDiameter) / step);
        List<Double> percentErrors = new ArrayList<>(numTests);
        for (double d = MAX_BALL_DIAMETER; d > minBallDiameter; d -= step) {
            percentErrors.add(percentError1(d));
        }
        return percentErrors;
    }
    
    public List<Double> enumeratePercentErrors1() {
        return enumeratePercentErrors1(0.02, 0.0005);
    }
    
    private static double calcBallDiameter(double distFromDiameter) {
        double chord = 2 * Math.sqrt((RADIUS * RADIUS) - 
                (distFromDiameter * distFromDiameter));
        //System.out.println(chord);
        return chord;
    }
    
    public double percentError2(double distFromDiameter) {
        return percentError1(calcBallDiameter(distFromDiameter));
    }
    
    public List<Double> enumeratePercentErrors2(double maxDistFromDiameter, double step) {
        if (maxDistFromDiameter > RADIUS) {
            maxDistFromDiameter = RADIUS;
        }
        List<Double> percentErrors = new ArrayList<>();
        for (double d = 0; d < maxDistFromDiameter; d += step) {
            percentErrors.add(percentError2(d));
        }
        return percentErrors;
    }
    
    public List<Double> enumeratePercentErrors2() {
        return enumeratePercentErrors2(RADIUS / 2 + 0.0005, 0.0005);
    }
    
    public static void main(String[] args) throws Exception {
        PhysicsGravityLab trial1 = new PhysicsGravityLab(0.0565, 0.2550, 0.034,  0.1607, 0.0150);
        PhysicsGravityLab trial2 = new PhysicsGravityLab(0.0635, 0.1825, 0.000,  0.1184, 0.0216);
        PhysicsGravityLab trial3 = new PhysicsGravityLab(0.0760, 0.1145, 0.000, -0.0073, 0.0271);
        PhysicsGravityLab trial4 = new PhysicsGravityLab(0.0620, 0.3140, 0.000,  0.1992, 0.0100);
        PhysicsGravityLab[] trials = new PhysicsGravityLab[] {trial1, trial2, trial3, trial4};
        double[][] allPercentErrors = new double[trials.length][];
        for (int i = 0; i < trials.length; i++) {
            List<Double> percentErrorsList = trials[i].enumeratePercentErrors1();
            double[] percentErrors = new double[percentErrorsList.size()];
            for (int j = 0; j < percentErrors.length; j++) {
                percentErrors[j] = percentErrorsList.get(j);
            }
            allPercentErrors[i] = percentErrors;
        }
        double d = 0.0254;
        for (int j = 0; j < allPercentErrors[0].length; j++) {
            StringJoiner sj = new StringJoiner(",");
            sj.add(String.valueOf((int) (d * 10000) / 10000.0));
            d -= 0.0005;
            for (int i = 0; i < 4; i++) {
                sj.add(String.valueOf(allPercentErrors[i][j]));
            }
            System.out.println(sj);
        }
        
        System.out.println();
        
        System.out.println(trial1.percentError2(0));
        
        System.out.println();
        System.out.println();
        
        for (PhysicsGravityLab trial : trials) {
            System.out.println(trial.enumeratePercentErrors2()
                                    .parallelStream()
                                    .map(x -> String.valueOf(x))
                                    .collect(Collectors.joining(",")));
        }
        
        //int i = 5;
        //System.out.println(i++-++i);
        
    }
    
}
