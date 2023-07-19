package in.codifi.api.utilities;

import java.util.List;
import java.util.ArrayList;

import in.codifi.api.request.model.ApiStatusModel;

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

	public static boolean isEqual(String str1, String str2) {
		boolean isEqual = false;
		if (str1 != null && str2 != null && str1.equalsIgnoreCase(str2)) {
			isEqual = true;
		}
		return isEqual;
	}
	
	public static boolean isNotNullOrEmptyA(ApiStatusModel apiStatusModel) {
		if (apiStatusModel == null) {
			return false;
		}
		// Check each field for null or empty values
		if (isNullOrEmpty(apiStatusModel.getStage()) || apiStatusModel.getApplicationId() == null
				|| apiStatusModel.getStatus() == null || isNullOrEmpty(apiStatusModel.getApprovedBy())
				|| isNullOrEmpty(apiStatusModel.getReason())) {
			return false;
		}

		return true;
	}

	@SuppressWarnings("rawtypes")

	public static boolean isListNullOrEmpty(List list) {
		boolean isNullOrEmpty = false;
		if (list == null || list.isEmpty()) {
			isNullOrEmpty = true;
		}
		return isNullOrEmpty;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isListNotNullOrEmpty(List list) {
		return !isListNullOrEmpty(list);
	}
}
