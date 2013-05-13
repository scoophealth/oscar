/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.research.eaaps;

import static org.junit.Assert.assertTrue;

import java.security.PrivateKey;
import java.security.PublicKey;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.SendingUtils;

public class EaapsIntegrationTest extends DaoTestFixtures {

	private static Logger logger = Logger.getLogger(EaapsIntegrationTest.class);
	
	/**
	 * Public OSCAR key
	 */
	private static final String OSCAR_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC3twPfm6XhWdcfjA9e0cSwNda2DtQpdNwQ8XBk" + 
			"/s4tSLbcZuNIjuC7AXxqI5SiuYL8AJDx8bqA3Y+egSV2QQJDUkRXDr/XqT5zl9TPQE49gPaLPZv2" + 
			"m7cBhTy8l5p1wKuvJdhIahx7/Ca3Inj6q2vMUObcOyqhQKOms143StRBFwIDAQAB";
	
	/**
	 * Public client key
	 */
	private static final String CLIENT_KEY = 
			"MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJYYlZ79cV1ijlPohPfvVF14zvTA" + 
			"TU9v+Z4HUTVlWrb5yboiPBXd2MhYI0iYznvAzlkRBDRueXcRGPUbApN9KYZYF0uBQT4ptXiOD9uj" + 
			"Ptq26EyrzDyE5km88Ekt/vyZvBQ7O50hXuIr9IU8vhWiZyXAU/v7e3vb8OZreKSm5J4dAgMBAAEC" + 
			"gYBwEi81pXuOBNbM5CLUlYjiuh+dNDEFjVBOpJwISINxeBUdvA4tLZZ+EQFXZXFXieEJM+F13L8p" + 
			"HkUKTn6f7aagmeGzFpyCppp2ht/bIZx8LpjT2cdsIn9fEEzfRKOiXf9MUeF0gFEtZXoKmaE7JURj" + 
			"ag3Fdh2zbk0S3YCp+1LXwQJBAM7p9auMCOxawTSGYoYh5PARV+1cq6MZfh7drB+f23q6z5X6/64u" + 
			"RcYe8rkLUwHM9LzChMqD/QD6+MNjnPj1S8kCQQC5tAglFTM25z1lSPfdvYRZ8fgm1GYmuQsb7u/K" + 
			"S0vtbsM2DVKz9T61QH56uMscPIXAhm35UW2R/78/9MO7eEG1AkAt062AoBQ93N/btUPO92TQMtcp" + 
			"kBPHnNbNGUWM/4fJx+RAEIZeWotDlQknKLXquS0fPWnRvKfldrBv/fj/PrzZAkEAqRZU6DcCd/Zb" + 
			"f9LN5jg+v4tD8U8qaA3LILcR3Xdr/hgBZUECUdt3KqA7ydBjGCW/f4qnrgDHrM1aPYjHg/Y+lQJA" + 
			"YZmSJwDD8dYciDJXJm0ktRDiGGlFkOG1wJ7WnZrqHq7R7VWDMPBVwJxvCE+iiU8M+Y/llTlhKgkw" + 
			"DKwMGCzL4A==";
		
	private static final String PDF = "JVBERi0xLjQKJeLjz9MKMyAwIG9iago8PC9MZW5ndGggNjE5L0ZpbHRlci9GbGF0ZURlY29kZT4+c3RyZWFtCnichZVfb9MwFMXf8yn8CA8U23H957WITUKq0KAC7dFNXBgLDcs6+vVJ44s05" + 
			"ea4mqZV+p3Tc5xd3zxVm11VW+GlFbu2kuKdctOnj7vqrnqafrX4NILbSq7W4lx5v6qFC1IoaVZWKCuGVH0lqIJeBUh1cONfRI3UKwVpjnWmFAsoxQJKsYDm2LUvxQJKsYBSLKA51uhSLKAUCy" + 
			"jFAppjtS3FAkqxgFIsoDlWFUcKUIoFlGIBnWJtKI0UojkW0RyLaI51pZFClGIBpVhAc6wtjRSiFAsoxQKaY01ppBClWEApFtAcW5dGClGKBZRiGb27LFIl5PijRJDTjtRi97t6f6OEGj8dqjf" + 
			"bvk3d292vy3J9pVbBTEtzLr9PceBqHcK0RNmXx+PLITanlyEtuMxYVi24PvRdvyDPB7i8CS5q/V9tnE5tjA6egTlUCAaegam/pGN86U6wPjPcDikdUX0rZ+rWee38fg3rM4cKbuGwVJ+pb9Iw" + 
			"xOEB1meG+9R1/Rn1N2Ym36eUtDEB9mcOLaWG/Zn6Wn9m+P7z4ZRQfe1naqt8U0cVYX3mKD5+pr5Wnxm2ceh7OD5Kzx9m601jGg/7M8fYf2HYqD9Tf+u7vz1sz+TF4bFhflUabday9TVqzx0qW" + 
			"Inac/XY/vH5HH8s3cd8BO7ZDP0ZPX/r57fF2b1pmr2CJ2CO8QQWnoCpr8wPN2y62Dyi+nZ+WWrbauMkHB/uGMcH/wOY+vOfpZcLdWfq4qM385sSDk4mHRvYnTnGzQNHn6uL3Zm6PPn1/KLUNo" + 
			"bQarh3uGN8a8G9w9XbNDSpTc/wANzxevH8AwFY0nsKZW5kc3RyZWFtCmVuZG9iago1IDAgb2JqCjw8L1BhcmVudCA0IDAgUi9Db250ZW50cyAzIDAgUi9UeXBlL1BhZ2UvUmVzb3VyY2VzPDw" + 
			"vUHJvY1NldCBbL1BERiAvVGV4dCAvSW1hZ2VCIC9JbWFnZUMgL0ltYWdlSV0vRm9udDw8L0YxIDEgMCBSL0YyIDIgMCBSPj4+Pi9NZWRpYUJveFswIDAgNTk1IDg0Ml0+PgplbmRvYmoKMSAw" + 
			"IG9iago8PC9MYXN0Q2hhciAxMTcvQmFzZUZvbnQvVGltZXMtQm9sZC9UeXBlL0ZvbnQvRW5jb2Rpbmc8PC9UeXBlL0VuY29kaW5nL0RpZmZlcmVuY2VzWzY3L0MgNzcvTSA4OS9ZIDk3L2EgO" + 
			"TkvYy9kL2UvZiAxMDgvbCAxMTAvbi9vIDExNC9yIDExNi90L3VdPj4vU3VidHlwZS9UeXBlMS9XaWR0aHNbNzIyIDAgMCAwIDAgMCAwIDAgMCAwIDk0NCAwIDAgMCAwIDAgMCAwIDAgMCAwID" + 
			"AgNzIyIDAgMCAwIDAgMCAwIDAgNTAwIDAgNDQ0IDU1NiA0NDQgMzMzIDAgMCAwIDAgMCAyNzggMCA1NTYgNTAwIDAgMCA0NDQgMCAzMzMgNTU2XS9GaXJzdENoYXIgNjc+PgplbmRvYmoKMiA" + 
			"wIG9iago8PC9MYXN0Q2hhciAxMTkvQmFzZUZvbnQvVGltZXMtUm9tYW4vVHlwZS9Gb250L0VuY29kaW5nPDwvVHlwZS9FbmNvZGluZy9EaWZmZXJlbmNlc1s0OC96ZXJvL29uZS90d28vdGhy" + 
			"ZWUvZm91ci9maXZlL3NpeC9zZXZlbi9laWdodC9uaW5lIDY2L0IgNzAvRi9HIDc3L00gNzkvTyA4Mi9SIDg2L1YvVyA4OS9ZIDk3L2EvYi9jL2QvZS9mL2cvaC9pIDEwNy9rL2wgMTEwL24vb" + 
			"y9wIDExNC9yL3MvdC91L3Yvd10+Pi9TdWJ0eXBlL1R5cGUxL1dpZHRoc1s1MDAgNTAwIDUwMCA1MDAgNTAwIDUwMCA1MDAgNTAwIDUwMCA1MDAgMCAwIDAgMCAwIDAgMCAwIDY2NyAwIDAgMC" + 
			"A1NTYgNzIyIDAgMCAwIDAgMCA4ODkgMCA3MjIgMCAwIDY2NyAwIDAgMCA3MjIgOTQ0IDAgNzIyIDAgMCAwIDAgMCAwIDAgNDQ0IDUwMCA0NDQgNTAwIDQ0NCAzMzMgNTAwIDUwMCAyNzggMCA" + 
			"1MDAgMjc4IDAgNTAwIDUwMCA1MDAgMCAzMzMgMzg5IDI3OCA1MDAgNTAwIDcyMl0vRmlyc3RDaGFyIDQ4Pj4KZW5kb2JqCjQgMCBvYmoKPDwvSVRYVCgyLjEuNykvVHlwZS9QYWdlcy9Db3Vu" + 
			"dCAxL0tpZHNbNSAwIFJdPj4KZW5kb2JqCjYgMCBvYmoKPDwvVHlwZS9DYXRhbG9nL1BhZ2VzIDQgMCBSPj4KZW5kb2JqCjcgMCBvYmoKPDwvUHJvZHVjZXIoaVRleHQgMi4xLjcgYnkgMVQzW" + 
			"FQpL01vZERhdGUoRDoyMDEzMDQwOTE3NDY1MSswMicwMCcpL0NyZWF0aW9uRGF0ZShEOjIwMTMwNDA5MTc0NjUxKzAyJzAwJyk+PgplbmRvYmoKeHJlZgowIDgKMDAwMDAwMDAwMCA2NTUzNS" + 
			"BmIAowMDAwMDAwODY3IDAwMDAwIG4gCjAwMDAwMDExOTQgMDAwMDAgbiAKMDAwMDAwMDAxNSAwMDAwMCBuIAowMDAwMDAxNzAyIDAwMDAwIG4gCjAwMDAwMDA3MDEgMDAwMDAgbiAKMDAwMDA" + 
			"wMTc2NSAwMDAwMCBuIAowMDAwMDAxODEwIDAwMDAwIG4gCnRyYWlsZXIKPDwvUm9vdCA2IDAgUi9JRCBbPDkyMThlYWZhYmZkOWM0ZjcyNmY2YjlkNDZmZDI1NmZkPjw4ODc3YzNkN2RjZDIz" + 
			"ODAxMTU5NmMxZDJlZDQ2YjgyZj5dL0luZm8gNyAwIFIvU2l6ZSA4Pj4Kc3RhcnR4cmVmCjE5MzIKJSVFT0YK";
	
	private static final String HASH = "f5950749dc36f9db4dc10fa024c86b498df592f9472775f718c9024f084339c7";
	
	private static final String HL7 = 
			"MSH|^~\\&|SENDING APP||||20130408170018.445-0400||ORU^R01|2501|01|2.2|1\r" + 
			"PID||" + HASH + "\r" + 
			"OBR||||SERVICE ID: EAAPS\r" + 
			"NTE|1|eaaps_" + HASH + "_20130408170040.pdf|" + PDF;
	
	@BeforeClass
	public static void init() throws Exception {
		SchemaUtils.restoreAllTables();
	}
	
	@Test
	public void test() throws Exception {
		if (!TestConstants.ENABLED) {
			return;
		}
		
		String url = "http://localhost:8080/oscar/lab/newLabUpload.do";
		String publicOscarKeyString = OSCAR_KEY;
		String publicServiceKeyString = CLIENT_KEY;

		PublicKey publicOscarKey = SendingUtils.getPublicOscarKey(publicOscarKeyString); 
		PrivateKey publicServiceKey = SendingUtils.getPublicServiceKey(publicServiceKeyString);
		
		byte[] bytes = HL7.getBytes(); 
		int statusCode = SendingUtils.send(bytes, url, publicOscarKey, publicServiceKey, "eaaps");
		logger.info("Completed EAAPS call with status " + statusCode);
		assertTrue(statusCode == 200);
	}

}
