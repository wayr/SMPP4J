package org.wayr.smpp.utils;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import org.wayr.smpp.exceptions.InvalidDateFormatException;

/**
 *
 * @author paul
 */
public class SMPPDate {

    protected int year;
    protected int month;
    protected int day;
    protected int hour;
    protected int minute;
    protected int second;
    protected int tenth;
    protected char sign = '+';
    protected TimeZone savedTimeZone;
    protected int hashCode;
    private static final String FORMAT =
            "{0,number,00}{1,number,00}{2,number,00}{3,number,00}{4,number,00}"
            + "{5,number,00}{6,number,0}{7,number,00}{8}";
    private static final String SHORT_FORMAT =
            "{0,number,00}{1,number,00}{2,number,00}{3,number,00}{4,number,00}"
            + "{5,number,00}";

    public SMPPDate() {
        hashCode = toString().hashCode();
        savedTimeZone = TimeZone.getDefault();
    }

    public SMPPDate(Date d) {
        if (d == null) {
            throw new NullPointerException("Cannot use a null Date");
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        setFields(cal);
    }

    public SMPPDate(Calendar cal) {
        if (cal == null) {
            throw new NullPointerException("Cannot use a null Calendar");
        }

        setFields(cal);
    }

    public SMPPDate(int years, int months, int days, int hours, int minutes,
            int seconds) {
        this.year = years;
        this.month = months;
        this.day = days;
        this.hour = hours;
        this.minute = minutes;
        this.second = seconds;
        this.tenth = 0;
        this.sign = 'R';
        this.savedTimeZone = null;

        this.hashCode = toString().hashCode();
    }

    protected void setFields(Calendar calendar) {
        year = calendar.get(Calendar.YEAR) - 2000;
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        second = calendar.get(Calendar.SECOND);
        tenth = calendar.get(Calendar.MILLISECOND) / 100;
        savedTimeZone = calendar.getTimeZone();

        // Time zone calculation
        sign = '+';
        int off = savedTimeZone.getOffset(System.currentTimeMillis());
        if (off < 0) {
            sign = '-';
        }

        // Cache the hashCode
        hashCode = toString().hashCode();
    }

    protected void initCalendar(Calendar calendar) {
        if (savedTimeZone != null) {
            calendar.setTimeZone(savedTimeZone);
        }
        calendar.set(Calendar.YEAR, year + 2000);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, tenth * 100);
    }

    public Calendar getCalendar() {
        Calendar cal = Calendar.getInstance();
        initCalendar(cal);
        return cal;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public int getTenth() {
        return tenth;
    }

    public int getUtcOffset() {
        int offset = 0;
        if (savedTimeZone != null) {
            offset = savedTimeZone.getOffset(System.currentTimeMillis());
        }
        // Calculate the difference in quarter hours.
        return Math.abs(offset) / 900000;

    }

    public TimeZone getTimeZone() {
        return savedTimeZone;
    }

    public char getSign() {
        return sign;
    }

    public boolean isRelative() {
        return sign == 'R';
    }

    public boolean hasTimezone() {
        return sign == '+' || sign == '-';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SMPPDate) {
            SMPPDate d = (SMPPDate) obj;
            int diff = (year - d.year)
                    + (month - d.month)
                    + (day - d.day)
                    + (hour - d.hour)
                    + (minute - d.minute)
                    + (second - d.second)
                    + (tenth - d.tenth)
                    + ((int) sign - (int) d.sign);
            boolean sameTz = savedTimeZone == null
                    ? d.savedTimeZone == null : savedTimeZone.equals(d.savedTimeZone);
            return diff == 0 && sameTz && sign == d.sign;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        int utcOffset = getUtcOffset();
        Object[] args = {new Integer(year), new Integer(month),
            new Integer(day), new Integer(hour), new Integer(minute),
            new Integer(second), new Integer(tenth),
            new Integer(utcOffset), new Character(sign),};
        String format = FORMAT;
        if (sign == (char) 0) {
            format = SHORT_FORMAT;
        }
        return MessageFormat.format(format, args);
    }

    public static SMPPDate parseSMPPDate(String s) throws InvalidDateFormatException {
        SMPPDate d = new SMPPDate();

        if (s == null || s.length() == 0) {
            return d;
        }
        if (s.length() != 16 && s.length() != 12) {
            throw new InvalidDateFormatException("Date string is incorrect length", s);
        }
        boolean longForm = s.length() == 16;
        try {
            d.year = Integer.parseInt(s.substring(0, 2));
            d.month = Integer.parseInt(s.substring(2, 4));
            d.day = Integer.parseInt(s.substring(4, 6));
            d.hour = Integer.parseInt(s.substring(6, 8));
            d.minute = Integer.parseInt(s.substring(8, 10));
            d.second = Integer.parseInt(s.substring(10, 12));
            if (longForm) {
                d.sign = s.charAt(15);
                if (d.sign != 'R') {
                    d.tenth = Integer.parseInt(s.substring(12, 13));
                    int utcOffset = Integer.parseInt(s.substring(13, 15));
                    int rawOffset = utcOffset * 900000;
                    if (d.sign == '-') {
                        rawOffset = -rawOffset;
                    }
                    int hours = utcOffset / 4;
                    int minutes = (utcOffset - (hours * 4)) * 15;
                    Object[] args = new Object[]{
                        new Character(d.sign),
                        Integer.valueOf(hours),
                        Integer.valueOf(minutes),};
                    String id = MessageFormat.format("UTC{0}{1,number,00}:{1,number,00}", args);
                    d.savedTimeZone = new SimpleTimeZone(rawOffset, id);
                } else {
                    d.savedTimeZone = null;
                }
            } else {
                d.sign = (char) 0;
                d.savedTimeZone = null;
            }
            d.hashCode = d.toString().hashCode();
        } catch (NumberFormatException x) {
            throw new InvalidDateFormatException("Invalid SMPP date string", s);
        }
        return d;
    }
}
