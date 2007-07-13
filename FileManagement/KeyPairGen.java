import java.util.*;
import java.io.*;
import java.security.*;
import org.apache.commons.codec.binary.Base64;

public class KeyPairGen{
	
	public static void main(String[] args) {
		//String pubKey = "";
		//String privKey = "";	
		byte[] pubKey;
		byte[] privKey;	
		Base64 base64 = new Base64();	

		// Generate an RSA key
      System.out.println( "\nStart generating RSA key" );
		try{
      	KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(1024);
 	      KeyPair key = keyGen.generateKeyPair();   

			pubKey = base64.encode(key.getPublic().getEncoded());	
			privKey = base64.encode(key.getPrivate().getEncoded());
		
			System.out.println( "Finished generating RSA key" );
		}catch(NoSuchAlgorithmException e){
			pubKey = "error".getBytes();
			privKey = "error".getBytes();
			System.out.println("Could not create RSA key: "+e);
		}      
		
		// Save to file
		try{
			FileOutputStream fos = new FileOutputStream("serverPublic.key");
			fos.write(pubKey);
			fos.close();			
		}catch(IOException e){
			System.out.println("Could not create public key file: "+e);
		}
		
		try{
			FileOutputStream fos = new FileOutputStream("serverPrivate.key");
			fos.write(privKey);
			fos.close();			
		}catch(IOException e){
			System.out.println("Could not create private key file: "+e);
		}

	}

}
