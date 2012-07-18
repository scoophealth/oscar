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


package org.oscarehr.util;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public final class EncryptionUtils
{
	private static final Logger logger = MiscUtils.getLogger();
	private static final MessageDigest messageDigest = initMessageDigest();
	private static final QueueCache<String, byte[]> sha1Cache = new QueueCache<String, byte[]>(4, 2048);
	private static final int MAX_SHA_KEY_CACHE_SIZE = 2048;
	private static final String MANGLED_SECRET_KEY_SESSION_KEY=EncryptionUtils.class.getName()+".MANGLED_SECRET_KEY";

	private static MessageDigest initMessageDigest()
	{
		try
		{
			return(MessageDigest.getInstance("SHA-1"));
		}
		catch (NoSuchAlgorithmException e)
		{
			logger.error("Error", e);
			return(null);
		}
	}

	/**
	 * Previously we benchmarked caching SHA1 against the JCS cache and found almost no 
	 * performance benefit. Testing this against the queue cache finds a much more significant 
	 * difference. Tested against 2000 calls of 200, 32 character keys, we find at least 2x performance and an average of 5x performance increase.
	 * It's now worth caching.
	 * 
	 * Having said that, this is relatively insignificant as sha1 takes about 0.010ms where as cached calls take about 0.002ms.
	 */
	public static byte[] getSha1(String s)
	{
		byte[] b = sha1Cache.get(s);

		if (b == null)
		{
			b = getSha1NoCache(s);
			if (s.length() < MAX_SHA_KEY_CACHE_SIZE) sha1Cache.put(s, b);
		}

		return(b);
	}

	/**
	 * We're only really using this to encrypt passwords so it's not too often so synchronisation on 1 instance shouldn't be too bad.
	 */
	protected static byte[] getSha1NoCache(String s)
	{
		if (s == null) return(null);

		try
		{
			synchronized (messageDigest)
			{
				return(messageDigest.digest(s.getBytes("UTF-8")));
			}
		}
		catch (Exception e)
		{
			logger.error("Unexpected error.", e);
			return(null);
		}
	}

	/**
	 * Assumes AES 128
	 */
	public static SecretKey generateEncryptionKey() throws NoSuchAlgorithmException
	{
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(128);

		SecretKey secretKey = keyGenerator.generateKey();
		return(secretKey);
	}

	/**
	 * Assumes AES 128
	 * A deterministic key generator based on the seed.
	 */
	public static SecretKeySpec generateEncryptionKey(String seed)
	{
		byte[] sha1 = getSha1(seed);

		SecretKeySpec secretKey = new SecretKeySpec(sha1, 0, 16, "AES");
		return(secretKey);
	}

	/**
	 * Assumes AES 128
	 */
	public static byte[] encrypt(SecretKey secretKey, byte[] plainData) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException
	{
		if (secretKey == null) return(plainData);

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] results = cipher.doFinal(plainData);
		return(results);
	}

	/**
	 * Assumes AES 128
	 */
	public static byte[] decrypt(SecretKey secretKey, byte[] encryptedData) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
	{
		if (secretKey == null) return(encryptedData);

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] results = cipher.doFinal(encryptedData);
		return(results);
	}
	
	/**
	 * deterministically mangle the byte input, doesn't have to be complex, just something different.
	 * Reason being we don't want to just sha1 the oscar_password as the encryption key because
	 * that's already stored in the db as the password record.
	 */
	public static String deterministicallyMangle(String s)
	{
		Random random=new Random(s.length());

		StringBuilder sb=new StringBuilder();
		
		for (int i=0; i< s.length(); i++)
		{
			sb.append(random.nextInt(s.charAt(i)));
			
		}
				
		return(sb.toString());
	}
	
	public static SecretKeySpec getSecretKeyFromDeterministicallyMangledPassword(String unmangledPassword)
	{
		String mangledPassword=deterministicallyMangle(unmangledPassword);
		SecretKeySpec secretKeySpec=EncryptionUtils.generateEncryptionKey(mangledPassword);
		return(secretKeySpec);
	}
	
	public static void setDeterministicallyMangledPasswordSecretKeyIntoSession(HttpSession session, String unmangledPassword)
	{
		SecretKeySpec secretKeySpec=getSecretKeyFromDeterministicallyMangledPassword(unmangledPassword);
		session.setAttribute(MANGLED_SECRET_KEY_SESSION_KEY, secretKeySpec);
	}
	
	public static SecretKeySpec getDeterministicallyMangledPasswordSecretKeyFromSession(HttpSession session)
	{
		return (SecretKeySpec) (session.getAttribute(MANGLED_SECRET_KEY_SESSION_KEY));
	}
}
