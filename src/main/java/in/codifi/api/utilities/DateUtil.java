package in.codifi.api.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static final DateFormat DDMMYYHHMMSS = new SimpleDateFormat("ddMMyyHHmmss");
	

	public static Date getNewDateWithCurrentTime() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getTime();
	}
}
