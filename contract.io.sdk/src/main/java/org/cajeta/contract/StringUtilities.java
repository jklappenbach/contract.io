/**
 * 
 */
package org.cajeta.contract;

/**
 * @author julian
 *
 */
public class StringUtilities {
	public static final long KILO = 1024;
	public static final long MEGA = 1024 * 1024;
	public static final long GIGA = 1024 * 1024 * 1024;
	public static Long toLong(String str) {
		Long l = null;
		long multiplier = 1;
		String value = str.toLowerCase();
		if (value.contains("k")) {
			multiplier = KILO;
			value = value.substring(0, value.indexOf("k"));
		} else if (value.contains("m")) {
			multiplier = MEGA;
			value = value.substring(0, value.indexOf("m"));
		} else if (value.contains("g")) {
			multiplier = GIGA;
			value = value.substring(0, value.indexOf("g"));
		}
		l = Long.parseLong(value) * multiplier;
		return l;
	}
	public static String camelCase(boolean firstUpper, String[] strings) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < strings.length; i++) {
			if (i == 0) {
				result.append(firstUpper ? strings[0].substring(0, 1).toUpperCase() : strings[0].substring(0, 1).toLowerCase());
				result.append(strings[0].substring(1).toLowerCase());
			} else {
				result.append(strings[i].substring(0, 1).toUpperCase() + strings[i].substring(1).toLowerCase());
			}
			
		}
		return result.toString();
	}
}
