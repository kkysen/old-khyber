package sen.khyber.apcs;

import java.util.Random;

import lombok.Getter;

import sen.khyber.reflect.Reflect;

public class Student {
    
    private static final Random random = new Random();
    private static final double STARTING_GPA = 100.0;
    
    private final @Getter String firstName;
    private final @Getter String lastName;
    private final @Getter int osis;
    private final @Getter int age;
    private double gpa = STARTING_GPA;
    
    public Student(String firstName, String lastName, int osis, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        if (String.valueOf(osis).length() == 9) {
            this.osis = osis;
        } else {
            throw new IllegalArgumentException("the OSIS must be a 9-digit number");
        }
        if (age < 0) {
            throw new IllegalArgumentException("you cannot have a negative age");
        } else {
            this.age = age;
        }
    }
    
    public Student(String firstName, String lastName, int osis) {
        this(firstName, lastName, osis, random.nextInt(4) + 12);
    }
    
    public Student(String firstName, String lastName, String osis, int age) {
        this(firstName, lastName, Integer.parseInt(osis), age);
    }
    
    public Student(String firstName, String lastName, String osis) {
        this(firstName, lastName, Integer.parseInt(osis));
    }
    
    /*@Override
    public String toString() {
        Field[] fields = Student.class.getDeclaredFields();
        StringJoiner sj = new StringJoiner(", ", "{", "}");
        for (Field field : fields) {
            if (! Modifier.isStatic(field.getModifiers())) {
                try {
                    sj.add(field.getName() + ": " + field.get(this));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return sj.toString();
    }*/
    
    @Override
    public String toString() {
        return Reflect.toString(Student.class, this);
    }
    
    public void printStudent(String message) {
        System.out.println(message + ": " + this);
    }
    
    public void printStudent() {
        printStudent("");
    }
    
    public void raiseGPA(double d) {
        if (d < 0) {
            throw new IllegalArgumentException("cannot be raised by a negative");
        } else if (gpa + d < 100) {
            throw new IllegalArgumentException("gpa cannot be greater than 100");
        }
        gpa += d;
    }
    
    public void lowerGPA(double d) {
        if (d < 0) {
            throw new IllegalArgumentException("cannot be lowered by a negative");
        } else if (gpa - d < 0) {
            throw new IllegalArgumentException("gpa cannot be less than 0");
        }
        gpa -= d;
    }
    
    public void raiseGPA() {
        gpa += random.nextDouble() * (100 - gpa);
    }
    
    public void lowerGPA() {
        gpa -= random.nextDouble() * gpa;
    }
    
    public static void main(String[] args) {
        Student me = new Student("Khyber", "Sen", 205312234, 16);
        int numIters = (args.length == 1) ? Integer.parseInt(args[0]) : 10;
        for (int i = 0; i < numIters; i++) {
            me.lowerGPA();
            me.printStudent("lowered");
            me.raiseGPA();
            me.printStudent("raised ");
        }
        me.printStudent("final  ");
    }
    
}
