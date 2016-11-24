package sen.khyber.io.enums;

import sen.khyber.reflect.Reflect;

public enum Day {
    
    MONDAY ("Monday", 1, true, "Mon"),
    TUESDAY ("Tuesday", 2, true, "Tue"),
    WEDNESDAY ("Wednesday", 3, true, "Wed"),
    THURSDAY ("Thursday", 4, true, "Thu"),
    FRIDAY ("Friday", 5, true, "Fri"),
    SATURDAY ("Saturday", 6, false, "Sat"),
    SUNDAY ("Sunday", 0, false, "Sun");
    
    public String day;
    public int dayNumber;
    public boolean weekday;
    public String abbreviation;
    
    private Day(String day, int dayNumber, boolean weekday, String abbreviation) {
        this.day = day;
        this.dayNumber = dayNumber;
        this.weekday = weekday;
        this.abbreviation = abbreviation;
    }
    
    @Override
    public String toString() {
        return Reflect.toString(Day.class, this);
    }
    
    public static void main(String[] args) throws Exception {
        for (Day day : Day.values()) {
            System.out.println(day);
        }
    }
    
}