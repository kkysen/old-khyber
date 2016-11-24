package sen.khyber.io.enums;

import sen.khyber.reflect.Reflect;

public enum Month {
    
    JANUARY ("January", 1, 31, "Winter"),
    FEBRUARY ("February", 2, 28, "Winter"),
    MARCH ("March", 3, 31, "Winter"),
    APRIL ("April", 4, 30, "Spring"),
    MAY ("May", 5, 31, "Sping"),
    JUNE ("June", 6, 30, "Spring"),
    JULY ("July", 7, 31, "Summer"),
    AUGUST ("August", 8, 31, "Summer"),
    SEPTEMBER ("September", 9, 30, "Summer"),
    OCTOBER ("October", 10, 31, "Fall"),
    NOVEMBER ("November", 11, 30, "Fall"),
    DECEMBER ("December", 12, 31, "Fall");
    
    public String month;
    public int monthNumber;
    public int numDays;
    public String season;
    
    private Month(String month, int monthNumber, int numDays, String season) {
        this.month = month;
        this.monthNumber = monthNumber;
        this.numDays = numDays;
        this.season = season;
    }
    
    @Override
    public String toString() {
        return Reflect.toString(Month.class, this);
    }
    
    public static void main(String[] args) throws Exception {
        for (Month month : Month.values()) {
            System.out.println(month);
        }
    }
    
}