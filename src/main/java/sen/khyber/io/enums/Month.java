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
public enum Month {
    
    JANUARY("January", 1, 31, "Winter"),
    FEBRUARY("February", 2, 28, "Winter"),
    MARCH("March", 3, 31, "Winter"),
    APRIL("April", 4, 30, "Spring"),
    MAY("May", 5, 31, "Sping"),
    JUNE("June", 6, 30, "Spring"),
    JULY("July", 7, 31, "Summer"),
    AUGUST("August", 8, 31, "Summer"),
    SEPTEMBER("September", 9, 30, "Summer"),
    OCTOBER("October", 10, 31, "Fall"),
    NOVEMBER("November", 11, 30, "Fall"),
    DECEMBER("December", 12, 31, "Fall");
    
    public final @Getter String month;
    public final @Getter int monthNumber;
    public final @Getter int numDays;
    public final @Getter String season;
    
    private Month(final String month, final int monthNumber, final int numDays,
            final String season) {
        this.month = month;
        this.monthNumber = monthNumber;
        this.numDays = numDays;
        this.season = season;
    }
    
    public static Stream<Month> toStream() {
        return Stream.of(Month.values());
    }
    
    public static Set<Month> toSet() {
        final Set<Month> set = new HashSet<>();
        for (final Month month : Month.values()) {
            set.add(month);
        }
        return set;
    }
    
    public static List<Month> toList() {
        final List<Month> list = new ArrayList<>();
        for (final Month month : Month.values()) {
            list.add(month);
        }
        return list;
    }
    
    @Override
    public String toString() {
        return Reflect.toString(Month.class, this);
    }
    
    public static void main(final String[] args) throws Exception {
        for (final Month month : Month.values()) {
            System.out.println(month);
        }
        Month.toStream().forEach(System.out::println);
    }
    
}