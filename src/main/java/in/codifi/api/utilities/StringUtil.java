package in.codifi.api.utilities;

public class StringUtil {

	public static boolean isNotNullOrEmpty(String str) {
		return !isNullOrEmpty(str);
	}
	
	public static boolean isNullOrEmpty(String str) {
		if (str != null && str.length() > 0) {
			return false;
		} else {
			return true;
		}
	}
}
