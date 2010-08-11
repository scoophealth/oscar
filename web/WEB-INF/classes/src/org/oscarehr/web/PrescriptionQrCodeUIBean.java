package org.oscarehr.web;

import org.oscarehr.util.MiscUtils;

public final class PrescriptionQrCodeUIBean {	
	private PrescriptionQrCodeUIBean() {
		// not meant to be instantiated, just a utility class
	}
	
	public static byte[] getPrescriptionHl7QrCodeImage(int prescriptionId)
	{
// stubbed
MiscUtils.getLogger().error("########### HERE "+prescriptionId);
		return(null);
	}
}
