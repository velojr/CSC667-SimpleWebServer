package server.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Time {
    public static String getTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        timeFormat.setTimeZone(TimeZone.getTimeZone("PST"));
        return timeFormat.format(new Date());
    }
}
