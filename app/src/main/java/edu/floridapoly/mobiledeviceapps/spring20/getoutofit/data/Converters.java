package edu.floridapoly.mobiledeviceapps.spring20.getoutofit.data;

import androidx.room.TypeConverter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Converters {
    public static final Date DEFAULT_DATE = new GregorianCalendar(2020, Calendar.MARCH, 27).getTime();

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? DEFAULT_DATE : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? DEFAULT_DATE.getTime() : date.getTime();
    }

}
