package org.oscarehr.util;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

public final class EncryptionUtils
{
	private static final Logger logger = MiscUtils.getLogger();
	private static final MessageDigest messageDigest = initMessageDigest();
	private static final QueueCache<String, byte[]> sha1Cache=new QueueCache<String,byte[]>(4, 2048);
	private static final int MAX_SHA_KEY_CACHE_SIZE=2048;

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
		byte[] b=sha1Cache.get(s);

		if (b==null)
		{
			b=getSha1NoCache(s);
			if (s.length()<MAX_SHA_KEY_CACHE_SIZE) sha1Cache.put(s, b);
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
				return(messageDigest.digest(s.getBytes()));
			}
		}
		catch (Exception e)
		{
			logger.error("Unexpected error.", e);
			return(null);
		}
	}

	public static byte[] generateEncryptionKey() throws NoSuchAlgorithmException
	{
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(128);

		SecretKey secretKey = keyGenerator.generateKey();
		return(secretKey.getEncoded());
	}

	public static byte[] encrypt(SecretKeySpec secretKeySpec, byte[] plainData) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException
	{
		if (secretKeySpec == null) return(plainData);

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		byte[] results = cipher.doFinal(plainData);
		return(results);
	}

	public static byte[] decrypt(SecretKeySpec secretKeySpec, byte[] encryptedData) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
	{
		if (secretKeySpec == null) return(encryptedData);

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
		byte[] results = cipher.doFinal(encryptedData);
		return(results);
	}

	public static void main(String... argv)
	{
		String temp = "one two three four";
		logger.info(new String(Hex.encodeHex(getSha1(temp))));
	}
}
