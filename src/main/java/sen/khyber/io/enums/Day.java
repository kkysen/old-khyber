package sen.khyber.io.enums;

import sen.khyber.reflect.Reflect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import lombok.Getter;

/**
 * 
 * 
 * @author Khyber Sen
 */
public enum Day {
    
    MONDAY("Monday", 1, true, "Mon"),
    TUESDAY("Tuesday", 2, true, "Tue"),
    WEDNESDAY("Wednesday", 3, true, "Wed"),
    THURSDAY("Thursday", 4, true, "Thu"),
    FRIDAY("Friday", 5, true, "Fri"),
    SATURDAY("Saturday", 6, false, "Sat"),
    SUNDAY("Sunday", 0, false, "Sun");
    
    public final @Getter String day;
    public final @Getter int dayNumber;
    public final @Getter boolean weekday;
    public final @Getter String abbreviation;
    
    private Day(final String day, final int dayNumber, final boolean weekday, final String abbreviation) {
        this.day = day;
        this.dayNumber = dayNumber;
        this.weekday = weekday;
        this.abbreviation = abbreviation;
    }
    
    public static Stream<Day> toStream() {
        return Stream.of(Day.values());
    }
    
    public static Set<Day> toSet() {
        final Set<Day> set = new HashSet<>();
        for (final Day day : Day.values()) {
            set.add(day);
        }
        return set;
    }
    
    public static List<Day> toList() {
        final List<Day> list = new ArrayList<>();
        for (final Day day : Day.values()) {
            list.add(day);
        }
        return list;
    }
    
    @Override
    public String toString() {
        return Reflect.toString(this);
    }
    
    public static void main(final String[] args) throws Exception {
        for (final Day day : Day.values()) {
            System.out.println(day);
        }
        Day.toStream().forEach(System.out::println);
    }
    
}