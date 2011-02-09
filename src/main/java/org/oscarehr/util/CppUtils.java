package org.oscarehr.util;

import java.util.Arrays;

public class CppUtils {
	public static String cppCodes[] = {"OMeds", "SocHistory", "MedHistory", "Concerns", "FamHistory", "Reminders", "RiskFactors"};
	
	public static void addCppCode(String code) {
		String[] result = Arrays.copyOf(cppCodes, cppCodes.length+1);
		result[result.length-1] = code;
		cppCodes = result;
	}
}
