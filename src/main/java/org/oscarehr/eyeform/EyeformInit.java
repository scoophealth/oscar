package org.oscarehr.eyeform;

import org.oscarehr.util.CppUtils;

public class EyeformInit {

	static boolean isInit = false;
	public static void init() {
		if(isInit)return;
		CppUtils.addCppCode("CurrentHistory");
		CppUtils.addCppCode("DiagnosticNotes");
		CppUtils.addCppCode("PastOcularHistory");
		isInit=true;
	}
}
