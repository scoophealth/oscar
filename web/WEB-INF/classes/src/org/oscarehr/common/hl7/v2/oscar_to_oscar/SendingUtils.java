package org.oscarehr.common.hl7.v2.oscar_to_oscar;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public final class SendingUtils {

	private static final String SERVICE_NAME = "OSCAR_TO_OSCAR_HL7_V2";

	public static void send(byte[] dataBytes, String url, PublicKey receiverOscarKey, PrivateKey receiverServiceKey) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		byte[] signature = getSignature(dataBytes, receiverServiceKey);
		SecretKey senderSecretKey = createSecretKey();
		byte[] encryptedBytes=encryptData(dataBytes, senderSecretKey);
		byte[] encryptedSecretKey=encryptEncryptionKey(senderSecretKey, receiverOscarKey);
	}

	private static byte[] encryptEncryptionKey(SecretKey senderSecretKey, PublicKey receiverOscarKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, receiverOscarKey);
		return(cipher.doFinal(senderSecretKey.getEncoded()));
    }

	private static byte[] encryptData(byte[] dataBytes, SecretKey senderSecretKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		SecretKeySpec secretKeySpec = new SecretKeySpec(senderSecretKey.getEncoded(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		return(cipher.doFinal(dataBytes));
    }

	private static SecretKey createSecretKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(128);
		return (keyGenerator.generateKey());
	}

	private static byte[] getSignature(byte[] bytes, PrivateKey receiverServiceKey) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException {
		Signature signature = Signature.getInstance("MD5WithRSA");
		signature.initSign(receiverServiceKey);
		signature.update(bytes);
		return (signature.sign());
	}
}
