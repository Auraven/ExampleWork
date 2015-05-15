package lcms.base;

import java.util.Calendar;
public class Log {
	
	public static void log(String msg, String username){	
		String time = Calendar.getInstance().getTime().toString();
		time = time.substring(0, time.lastIndexOf(" ")).substring(0, time.lastIndexOf(" "));
		C.insert("logs", new String[]{msg, username, time});
	}
}
