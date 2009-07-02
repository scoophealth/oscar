import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.MultipartPostMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.io.FileUtils;

public class TestHL7Upload {

	private static String URL = "http://localhost:8080/oscarnew/MeasurementHL7Uploader.do?method=upload";
	// private static String URL =
	// "https://oscarservice.com:8443/oscar_rogers/MeasurementHL7Uploader.do?method=upload";
	private static String keyLocation = "c:\\temp\\keyPair.key";
	private static String fileLocation = "c:\\temp\\hl7.txt";

	static {
		Protocol.registerProtocol("https", new Protocol("https",
				new EasySSLProtocolSocketFactory(), 443));

	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public static void main(String[] args) throws Exception {
		if (args.length > 0)
			URL = args[0];
		if (args.length > 1)
			keyLocation = args[1];
		if (args.length > 2)
			fileLocation = args[2];

		//testSecure(URL);
		testLoose(URL);
	}

	private static void testLoose(String url) throws IllegalArgumentException,
			IOException {
		sendEncryptedFileToServer(url, new File(fileLocation), "", "", "");
	}

	private static void testSecure(String url) throws IOException, Exception {
		File file = new File(fileLocation);
		upload(url, keyLocation, file.getName(), FileUtils
				.readFileToByteArray(file));
	}

	private static int upload(String URL, String keyLocation, String fileName,
			byte[] fileBytes) throws Exception {
		InputStream is = new BufferedInputStream(new ByteArrayInputStream(
				fileBytes));
		is.mark(is.available() + 1);

		// location of keyPair.key
		// String keyLocation =
		// "/home/wrighd/sandbox/oscar_mcmaster/FileManagement/keys/keyPair.key";
		ArrayList keyPairInfo = parseKeyFile(keyLocation);
		String serviceName = (String) keyPairInfo.get(0);
		PrivateKey clientPrivKey = (PrivateKey) keyPairInfo.get(1);
		PublicKey serverPubKey = (PublicKey) keyPairInfo.get(2);

		// Sign the message
		String signature = signMessage(is, clientPrivKey);
		is.reset();

		// Encrypt the message and the key used to encrypt it
		SecretKey sKey = createSecretKey();
		File encryptedFile = encryptFile(is, sKey, fileName);
		is.close();

		String encryptedKey = encryptKey(sKey, serverPubKey);
		int status = sendEncryptedFileToServer(URL, encryptedFile,
				encryptedKey, signature, serviceName);
		encryptedFile.delete();

		return status;
	}

	private static int sendEncryptedFileToServer(String URL, File file,
			String sKey, String sig, String serviceName) {
		try {
			HttpClient client = new HttpClient();
			MultipartPostMethod mPost = new MultipartPostMethod(URL);
			client.setConnectionTimeout(8000);

			mPost.addParameter("importFile", file.getName(), file);
			mPost.addParameter("key", sKey);
			mPost.addParameter("signature", sig);
			mPost.addParameter("service", serviceName);

			int statuscode = client.executeMethod(mPost);
			System.out.println("\nStatusLine>>>" + mPost.getStatusLine());

			mPost.releaseConnection();

			return statuscode;
		} catch (Exception e) {
			throw new RuntimeException("Failed to upload", e);
		}

	}

	/*
	 * Encrypts the message so it can be transfered securely
	 */
	private static File encryptFile(InputStream is, SecretKey skey,
			String fileName) throws Exception {

		fileName = fileName + ".enc";
		// Encode file
		try {
			OutputStream fos = new FileOutputStream(fileName);
			byte[] buf = new byte[1024];

			SecretKeySpec skeySpec = new SecretKeySpec(skey.getEncoded(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			fos = new CipherOutputStream(fos, cipher);

			// Read in the cleartext bytes and write to out to encrypt
			int numRead = 0;
			while ((numRead = is.read(buf)) >= 0) {
				fos.write(buf, 0, numRead);
			}
			fos.close();

		} catch (Exception e) {
			throw e;
		}

		return (new File(fileName));

	}

	/*
	 * Encrypts the secret key so it can be transfered along with the message
	 */
	static String encryptKey(SecretKey skey, PublicKey pubKey) throws Exception {

		Base64 base64 = new Base64();

		// Encode key string
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			byte[] cipherText = cipher.doFinal(skey.getEncoded());
			return (new String(base64.encode(cipherText), "ASCII"));
		} catch (Exception e) {
			// return("failed");
			throw e;
		}
	}

	/*
	 * Creates the secret key used to encrypt the message
	 */
	static SecretKey createSecretKey() throws Exception {
		// Create key for ecryption
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128);
			return (kgen.generateKey());
		} catch (Exception e) {
			// return(null);
			throw e;
		}
	}

	/*
	 * Signs the message with the given private key and returns the signature
	 */
	static String signMessage(InputStream is, PrivateKey key) throws Exception {
		byte[] signature = null;
		byte[] buf = new byte[1024];
		Base64 base64 = new Base64();

		try {
			Signature sig = Signature.getInstance("MD5WithRSA");
			sig.initSign(key);

			// Read in the message bytes and update the signature
			int numRead = 0;
			while ((numRead = is.read(buf)) >= 0) {
				sig.update(buf, 0, numRead);
			}

			signature = sig.sign();
			return (new String(base64.encode(signature), "ASCII"));
		} catch (Exception e) {
			// return("");
			throw e;
		}
	}

	static ArrayList parseKeyFile(String fileName) throws Exception {
		String serviceName = null;
		String privateKey = null;
		String publicKey = null;
		PrivateKey privKey = null;
		PublicKey pubKey = null;

		Base64 base64 = new Base64();
		ArrayList keyInfo = new ArrayList();

		try {
			InputStream input = new FileInputStream(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(input));

			String line = null;
			int lineCount = 0;
			while ((line = br.readLine()) != null) {
				if (lineCount == 1)
					serviceName = line;
				else if (lineCount == 4)
					privateKey = line;
				else if (lineCount == 7)
					publicKey = line;
				lineCount++;
			}

			// create private key from string
			PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(base64
					.decode(privateKey.getBytes("ASCII")));
			KeyFactory privKeyFactory = KeyFactory.getInstance("RSA");
			privKey = privKeyFactory.generatePrivate(privKeySpec);

			// create public key from string
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(base64
					.decode(publicKey.getBytes("ASCII")));
			KeyFactory pubKeyFactory = KeyFactory.getInstance("RSA");
			pubKey = pubKeyFactory.generatePublic(pubKeySpec);

			keyInfo.add(serviceName);
			keyInfo.add(privKey);
			keyInfo.add(pubKey);
		} catch (Exception e) {
			System.out.println("Could not get key info from file '" + fileName
					+ "': " + e);
			throw e;
		}

		return (keyInfo);

	}

}
