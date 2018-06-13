import java.io.InputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Test {

	public static String decrypt(String key, String data) {

	    try {
	      String[] parts = data.split(":");

	      IvParameterSpec iv = new IvParameterSpec(java.util.Base64.getDecoder().decode(parts[1]));
	      SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

	      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	      cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

	      byte[] decodedEncryptedData = java.util.Base64.getDecoder().decode(parts[0]);

	      byte[] original = cipher.doFinal(decodedEncryptedData);

	      return new String(original);
	    } catch (Exception ex) {
	      throw new RuntimeException(ex);
	    }
	  }


	
    protected static String decryptOneIdToken(byte[] encryptedBytes) throws Exception {
    	InputStream cipherInputStream = null;
    	
    	//String encData = "1/LX9jQ7xSH43KlAHiwkOTo6eVlxZGlaMWt2eVpNL0pnM3RJYUNYdz09";
    	//String[] parts = new String(org.apache.commons.codec.binary.Base64.decodeBase64(encData)).split("::");
    	
   // 	System.out.println(parts[0]);
   // 	System.out.println(parts[1]);
    	
    	byte[] encData = org.apache.commons.codec.binary.Base64.decodeBase64("MgbXZSiWIxAmjrhZ0edxzQ==");
    	
    	byte[] iv = org.apache.commons.codec.binary.Base64.decodeBase64("LkJXEZiEfl2LL0I+kt9r/Q==u");
    	
    	
    	
    	final Cipher cipher1 = Cipher.getInstance("AES/CBC/NoPADDING");
 	    cipher1.init(Cipher.DECRYPT_MODE, new SecretKeySpec("abcdefghijklmopq".getBytes(), "AES"), new IvParameterSpec(iv));
 	//    cipherInputStream = new CipherInputStream(new ByteArrayInputStream(parts[0].getBytes()), cipher1);
 	    byte[] result = cipher1.doFinal(encData);
 	    
 	    System.out.println(new String(result));
 	    return null;
 	    /*
    	
    	try {
    		//$key = hash('sha256', 'marc');
          //  $iv = substr(hash('sha256', '8465ef1069b311e8'), 0, 16);
    		MessageDigest digest = MessageDigest.getInstance("SHA-256");
    		//digest.digest("marc".getBytes());
    		
    	    final StringBuilder output = new StringBuilder();
    	    final byte[] secretKey = digest.digest("marc".getBytes());
    	    
    	    digest.reset();
    	    
    	    final byte[] initVector = digest.digest("8465ef1069b311e8".getBytes());
    	    
    	    final Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    	    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secretKey, "AES"), new IvParameterSpec(initVector, 0, cipher.getBlockSize()));
    	    cipherInputStream = new CipherInputStream(new ByteArrayInputStream(encryptedBytes), cipher);

    	    final String charsetName = "UTF-8";

    	    final byte[] buffer = new byte[8192];
    	    int read = cipherInputStream.read(buffer);

    	    while (read > -1) {
    	        output.append(new String(buffer, 0, read, charsetName));
    	        read = cipherInputStream.read(buffer);
    	    }

    	    System.out.println(output);
    	    return output.toString();
    	}catch(Exception e) {
    		//logger.error("Error",e);
    		e.printStackTrace();
    		return null;
    	} finally {
    	    IOUtils.closeQuietly(cipherInputStream);
    	}
    	*/
    }
    
    public static String toHexString( byte[] bytes )
    {
        StringBuffer sb = new StringBuffer( bytes.length*2 );
        for( int i = 0; i < bytes.length; i++ )
        {
            sb.append( toHex(bytes[i] >> 4) );
            sb.append( toHex(bytes[i]) );
        }

        return sb.toString();
    }
    private static char toHex(int nibble)
    {
        final char[] hexDigit =
        {
            '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
        };
        return hexDigit[nibble & 0xF];
    }

    
	public static void main(String[] args)   {
		// TODO Auto-generated method stub

		//System.out.println(decryptOneIdToken(Base64.decode("Ull0UTk1S2hRM0lnTm9RZk1nQWNHajAzbmg5NW9XSlh6OHg5RjlNSTRxMD0=".getBytes())));
	    String key = "0123456789abcdef"; // 128 bit key
	    String data = "ge6B+cOM/y7xhfiepo0ERA==:WYlTg/R3KOMNKqUpo45XIQ==";
		System.out.println(decrypt(key, data));
	}

}
