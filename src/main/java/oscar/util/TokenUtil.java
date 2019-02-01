package oscar.util;
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
import java.util.Base64;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;
import org.oscarehr.util.MiscUtils;


public class TokenUtil {
	private static Logger logger = MiscUtils.getLogger();
	
	public static String createJWTString(String config,String subject,String audience) throws JSONException {
		org.codehaus.jettison.json.JSONObject configObject = new org.codehaus.jettison.json.JSONObject(config);
		byte[] key = Base64.getDecoder().decode(configObject.getString("key").getBytes());
		logger.debug("decoded key"+ new String(key)+ " key in database "+configObject.getString("key") );
		return createJWTString(key,subject,configObject.getString("issuer"),audience);
	}
	
	public static String createJWTString(byte[] keyString,String subject,String issuer,String audience){
		String returnString = null;
		try{
			JwtClaims claims = new JwtClaims();
		    claims.setIssuer(issuer);  // who creates the token and signs it
		    claims.setAudience(audience); // to whom the token is intended to be sent
		    claims.setExpirationTimeMinutesInTheFuture(10); // time when the token will expire (10 minutes from now)
		    claims.setGeneratedJwtId(); // a unique identifier for the token
		    claims.setIssuedAtToNow();  // when the token was issued/created (now)
		    claims.setNotBeforeMinutesInThePast(2); // time before which the token is not yet valid (2 minutes ago)
		    claims.setSubject(subject); // the subject/principal is whom the token is about
		    //claims.setClaim("email","mail@example.com");
		    
		    
		    String payload = claims.toJson();
		    JsonWebSignature jws = new JsonWebSignature();
			
			HmacKey key = new HmacKey(keyString);
			jws.setPayload(payload);
			jws.setKey(key);
			jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
			returnString =jws.getCompactSerialization(); 
		}catch(Exception ex){
			logger.error("ERROR with token: "+ex);
		}
		return returnString;
	    
	}
}
