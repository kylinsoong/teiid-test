package sample.java8.ch12;

import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeExamples {
    
    private static final ThreadLocal<DateFormat> formatters = new ThreadLocal<DateFormat>() {
        protected DateFormat initialValue() {
            return new SimpleDateFormat("dd-MMM-yyyy");
        }
    };

    public static void main(String[] args) {

//        useOldDate();
        
//        useLocalDate();
        
        manipulate();
        
//        parseFormat();
        
//        timeZone();
    }
    
    static void timeZone() {
        
        ZoneId shanghaiZone = ZoneId.of("Asia/Shanghai");
        ZoneId zoneId = TimeZone.getDefault().toZoneId();
        
        System.out.println(shanghaiZone);
        System.out.println(zoneId);
        
        LocalDate date = LocalDate.of(2016, 9, 18);
        LocalDateTime dateTime = LocalDateTime.of(2016, 9, 18, 13, 45);
        Instant instant = Instant.now();
        ZonedDateTime zdt1 = date.atStartOfDay(shanghaiZone);
        ZonedDateTime zdt2 = dateTime.atZone(shanghaiZone);
        ZonedDateTime zdt3 = instant.atZone(shanghaiZone);
        
        System.out.println(zdt1);
        System.out.println(zdt2);
        System.out.println(zdt3);
        
        ZoneOffset beijingOffset = ZoneOffset.of("+08:00");
        OffsetDateTime dateTimeInBeiJing = OffsetDateTime.of(dateTime, beijingOffset);
        
        System.out.println(beijingOffset);
        System.out.println(dateTimeInBeiJing);
        
        Chronology cnaChronology = Chronology.ofLocale(Locale.CHINA);
        ChronoLocalDate now = cnaChronology.dateNow();
        
        System.out.println(cnaChronology);
        System.out.println(now);
    }

    static void parseFormat() {

        LocalDate date = LocalDate.of(2016, 9, 18);
        String s1 = date.format(DateTimeFormatter.BASIC_ISO_DATE);
        String s2 = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        
        LocalDate date1 = LocalDate.parse("20160918", DateTimeFormatter.BASIC_ISO_DATE);
        LocalDate date2 = LocalDate.parse("2016-09-18", DateTimeFormatter.ISO_LOCAL_DATE);
        
        System.out.println(s1);
        System.out.println(s2);
        System.out.println(date1);
        System.out.println(date2);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = date.format(formatter);
        LocalDate date3 = LocalDate.parse(formattedDate, formatter);
        
        System.out.println(formattedDate);
        System.out.println(date3);
        
        DateTimeFormatter cnaFormatter = DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.CHINA);       
        String formattedDate1 = date.format(cnaFormatter);
        LocalDate date4 = LocalDate.parse(formattedDate1, cnaFormatter);
        
        System.out.println(formattedDate1);
        System.out.println(date4);
        
        DateTimeFormatter cnaFormatter1 = new DateTimeFormatterBuilder()
                .appendText(ChronoField.DAY_OF_MONTH)
                .appendLiteral(". ")
                .appendText(ChronoField.MONTH_OF_YEAR)
                .appendLiteral(" ")
                .appendText(ChronoField.YEAR)
                .parseCaseInsensitive()
                .toFormatter(Locale.CHINA);  
        String formattedDate2 = date.format(cnaFormatter1);
        LocalDate date5 = LocalDate.parse(formattedDate2, cnaFormatter1);
        
        System.out.println(formattedDate2);
        System.out.println(date5);
        
    }
    
    static class NextWorkingDay implements TemporalAdjuster {
        @Override
        public Temporal adjustInto(Temporal temporal) {
            DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
            int dayToAdd = 1;
            if (dow == DayOfWeek.FRIDAY) dayToAdd = 3;
            if (dow == DayOfWeek.SATURDAY) dayToAdd = 2;
            return temporal.plus(dayToAdd, ChronoUnit.DAYS);
        }
    }

    static void manipulate() {

        LocalDate date1 = LocalDate.of(2016, 9, 18);
        LocalDate date2 = date1.withYear(2015);
        LocalDate date3 = date2.withDayOfMonth(25); 
        LocalDate date4 = date3.with(ChronoField.MONTH_OF_YEAR, 10);
        LocalDate date5 = date4.plusWeeks(1);
        LocalDate date6 = date5.minusYears(3);
        LocalDate date7 = date6.plus(36, ChronoUnit.MONTHS);
         
        System.out.println(date1);
        System.out.println(date2);
        System.out.println(date3);
        System.out.println(date4);
        System.out.println(date5);
        System.out.println(date6);
        System.out.println(date7);
        
        LocalDate date = LocalDate.of(2016, 9, 18);
        LocalDate date8 = date.with(nextOrSame(DayOfWeek.SUNDAY));
        LocalDate date9 = date.with(lastDayOfMonth());
        LocalDate date10 = date.with(new NextWorkingDay());
        LocalDate date11 = date.with(nextOrSame(DayOfWeek.MONDAY));
        LocalDate date12 = date.with(temporal -> {
            DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
            int dayToAdd = 1;
            if (dow == DayOfWeek.FRIDAY) dayToAdd = 3;
            if (dow == DayOfWeek.SATURDAY) dayToAdd = 2;
            return temporal.plus(dayToAdd, ChronoUnit.DAYS);
            });
        
        System.out.println(date8);
        System.out.println(date9);
        System.out.println(date10);
        System.out.println(date11);
        System.out.println(date12);
    }

    @SuppressWarnings("unused")
    static void useLocalDate() {
        
        LocalDate date = LocalDate.of(2016, 9, 18);
        int year = date.getYear(); 
        Month month = date.getMonth(); 
        int day = date.getDayOfMonth(); 
        DayOfWeek dow = date.getDayOfWeek(); 
        int len = date.lengthOfMonth(); 
        boolean leap = date.isLeapYear(); 
        System.out.println(date);
        
        LocalDate today = LocalDate.now();
        System.out.println(today);
        
        int y = date.get(ChronoField.YEAR);
        int m = date.get(ChronoField.MONTH_OF_YEAR);
        int d = date.get(ChronoField.DAY_OF_MONTH);
        System.out.println(y + "-" + m + "-" + d);
        
        LocalTime time = LocalTime.of(13, 25, 40);
        int hour = time.getHour(); 
        int minute = time.getMinute(); 
        int second = time.getSecond();
        System.out.println(time);
        
//        LocalDate date = LocalDate.parse("2016-09-18");
//        LocalTime time = LocalTime.parse("12:17:40");
        
        LocalDateTime dt1 = LocalDateTime.of(2016, Month.SEPTEMBER, 18, 13, 25, 40);
        LocalDateTime dt2 = LocalDateTime.of(date, time);
        LocalDateTime dt3 = date.atTime(13, 25, 40);
        LocalDateTime dt4 = date.atTime(time);
        LocalDateTime dt5 = time.atDate(date);
        
        System.out.println(dt1);
        System.out.println(dt2);
        System.out.println(dt3);
        System.out.println(dt4);
        System.out.println(dt5);
        
        LocalDate date1 = dt1.toLocalDate();
        LocalTime time1 = dt1.toLocalTime();
        
        System.out.println(date1);
        System.out.println(time1);

        Instant instant1 = Instant.ofEpochSecond(3);
        Instant instant2 = Instant.ofEpochSecond(3, 0);
        Instant instant3 = Instant.ofEpochSecond(2, 1000000000);
        Instant instant4 = Instant.ofEpochSecond(4, -1000000000);
        Instant now = Instant.ofEpochSecond(new Date().getTime());
        
        System.out.println(instant1);
        System.out.println(instant2);
        System.out.println(instant3);
        System.out.println(instant4);
        System.out.println(now);
        
        Duration threeMinutes1 = Duration.ofMinutes(3);
        Duration threeMinutes2 = Duration.of(3, ChronoUnit.MINUTES);
        Duration threeDays1 = Duration.ofDays(3);
        Duration threeDays2 = Duration.of(3, ChronoUnit.DAYS);
        
        System.out.println(threeMinutes1);
        System.out.println(threeMinutes2);
        System.out.println(threeDays1);
        System.out.println(threeDays2);
        
        Period tenDays = Period.ofDays(10);
        Period threeWeeks = Period.ofWeeks(3);
        Period twoYearsSixMonthsOneDay = Period.of(2, 6, 1);
        Period fiveDays = Period.between(LocalDate.of(2016, 9, 13), LocalDate.of(2016, 9, 18));
        
        System.out.println(tenDays);
        System.out.println(fiveDays);
        System.out.println(threeWeeks);
        System.out.println(twoYearsSixMonthsOneDay);
        
        JapaneseDate japaneseDate = JapaneseDate.from(date);
        System.out.println(japaneseDate); 
    }

    static void useOldDate() {
        Date date = new Date(114, 2, 18);
        System.out.println(date);

        System.out.println(formatters.get().format(date));

        Calendar calendar = Calendar.getInstance();
        calendar.set(2014, Calendar.FEBRUARY, 18);
        System.out.println(calendar);
    }

}
