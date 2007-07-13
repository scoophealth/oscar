import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import java.util.logging.*;
import javax.xml.parsers.*;
import org.apache.commons.httpclient.protocol.*;
import org.w3c.dom.*;

import java.security.*;
import java.security.spec.*;
import javax.crypto.*; 
import javax.crypto.spec.*;
 
public class testClient {
   
   static int Success = 0;
   static int ValidationFailed = 1;
   static int DecryptFailed = 2;
   static int MessageFailed = 3;
   static int SKeyFailed = 4;
   static int KeyStringFailed = 5;
   static int SigStringFailed = 6;
	static int ConnectionRefused = 7;
	static int NameFailed = 6;
	static int SaveFailed = 7;
	static int TypeFailed = 8;
   
   static Logger logger;

	static char[] hexChar = {
	        '0' , '1' , '2' , '3' ,
	        '4' , '5' , '6' , '7' ,
	        '8' , '9' , 'A' , 'B' ,
	        'C' , 'D' , 'E' , 'F' 
   };
   
   /** Creates a new instance of testClient */
   public testClient() {
   }
   
  
   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      String url = "http://localhost:8084/oscar/newUploader";   
      String filename = "";
		String type = "";
      
      initLogger();

      try{
      	if ( args[0] == null ){ throw new Exception("Usage Exception"); }
      	   logger.info("Loading Properties file "+args[0]);
      	   filename = args[0];
			if ( args.length < 2 ){
				System.out.println("No message type specified, sending message anyways\nUsage: testClient <input file> <message type>");
	  	 	}else{
		 		type = args[1];
		 	}
           
      }catch(Exception loadingEx){
         logger.severe("Error loading properties file"+loadingEx);
         System.out.println("Usage: testClient <input file> <message type>");
         System.exit(2);
      }
            
      try{
         System.out.println("Files to parse  "+filename);

         logger.info("About to parse file "+filename);
         testClient client = new testClient();
            
			// Sign the message       
		   String signature = signMessage(filename, getClientPrivate());
         
			// encrypt the message and the key used to encrypt it
			SecretKey sKey = createSecretKey();
			String encodedFile = encodeFile(filename, sKey);
			String encodedKey = encodeKey(sKey);

			// send the encrypted message along with the encrypted key used to encrypt the message and the signature
         int returnCode = client.sendFileToServer(url, encodedFile, filename, encodedKey, signature, type);
           
			// verify that the operation completed successfully
		   if (returnCode == Success){ 
				System.out.println("Success\n");                                                   
         }else if (returnCode == ValidationFailed){                        
            logger.severe("file: "+filename+" could not be validated\n");
         }else if (returnCode == DecryptFailed){                           
            logger.severe("file: "+filename+" could not be decrypted\n");                            
         }else if(returnCode == MessageFailed){
            logger.severe("file: "+filename+" was not transfered\n");                                                        
         }else if (returnCode == SKeyFailed){                           
            logger.severe("No secret key was transfered\n");                                               
         }else if (returnCode == SigStringFailed){                           
         	logger.severe("No signature was transfered\n");
         }else if (returnCode == KeyStringFailed){                           
         	logger.severe("No public key was transfered\n");
         }else if (returnCode == ConnectionRefused){                           
         	logger.severe("The connection was refused\n");
         }else if (returnCode == NameFailed){                           
         	logger.severe("No filename was transfered\n");
         }else if (returnCode == SaveFailed){                           
         	logger.severe("Could not save the uploaded file\n");
         }else if (returnCode == TypeFailed){                           
         	logger.severe("Type not transfered with file\n");
         }

       }catch (Exception e1){
           logger.severe("Major Mishap: "+e1.getMessage());
           e1.printStackTrace();
       }
      
   }
		
	/*
	 *	Retrieve the clients private key from a file
	 */
	static PrivateKey getClientPrivate(){
		
	   PrivateKey Key = null;

      try{
			FileInputStream fstream = new FileInputStream("clientPrivate.key");
			String privateKey = fileInputStreamAsString(fstream);
		
	      PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(toBinArray(privateKey));

      	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			Key = keyFactory.generatePrivate(privKeySpec);
      }catch(Exception e){
         System.out.println("Could not retrieve private key: "+e);
		}
      return(Key);
	}

	/*
	 *	Creates teh secret key used to encrypt the message
	 */
	static SecretKey createSecretKey() throws NoSuchAlgorithmException{
		// Create key for ecryption
	 	KeyGenerator kgen = KeyGenerator.getInstance("AES");
      kgen.init(128);
      return(kgen.generateKey());
	}

	/*
	 *	Encrypts the secret key so it can be transfered along with the message
	 */
	static String encodeKey(SecretKey skey){

		String publicKey = "";

		// Get servers public key from stored file			
		try{		
			FileInputStream fstream = new FileInputStream("RSAPublic.key");
			publicKey = fileInputStreamAsString(fstream);
		}catch(FileNotFoundException e){
			System.out.println("File 'RSAPublic.key' does not exist: "+e);
		}catch(IOException e){
			System.out.println("Could not create string from file: "+e);
		}	

		PublicKey pubKey = getPublicKey(publicKey);

		//Encode key string
		try{
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			byte[] cipherText = cipher.doFinal(skey.getEncoded());
			return(toHexString(cipherText));
		}catch(Exception e){
			System.out.println("Encoding error: "+e);
			return("failed");
		}


	}

	/*
	 *	Encrypts the message so it can be transfered securely
	 */
	static String encodeFile(String fileName, SecretKey skey){

		String fileString = "";
		
		//Get file as string
		try{		
			FileInputStream fs = new FileInputStream(fileName);
			fileString = fileInputStreamAsString(fs);
		}catch(FileNotFoundException e){
			System.out.println("File '"+fileName+"' does not exist: "+e);
			return("failed");
		}catch(IOException e){
			System.out.println("Could not create string from file: "+e);
			return("failed");
		}	

		//Encode file string
		try{
      		SecretKeySpec skeySpec = new SecretKeySpec(skey.getEncoded(), "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] cipherText = cipher.doFinal(fileString.getBytes());
			return(toHexString(cipherText));
		}catch(Exception e){
			System.out.println("Encoding error: "+e);
			return("failed");
		}				
	}

	/*
    *  Converts a key string to a public key
    */
	static PublicKey getPublicKey(String keyString){
      PublicKey publicKey = null;
      X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(toBinArray(keyString));
      try{
      	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
         publicKey = keyFactory.generatePublic(pubKeySpec);
      }catch(NoSuchAlgorithmException e){
         System.out.println("Algorithm could not be found: "+e);
		}catch(InvalidKeySpecException e){
         System.out.println("Key Spec is invalid: "+e);
		}  
      return(publicKey);
   }

	/*
	 *	Signs the message with the given private key and returns
	 * the signature
	 */
   static String signMessage(String filename, PrivateKey key){
		byte[] signature = null;		
		try{		

			FileInputStream fs = new FileInputStream(filename);
			String fileString = fileInputStreamAsString(fs);

			try{    

				Signature sig = Signature.getInstance("MD5WithRSA");
	      		sig.initSign(key);
				sig.update(fileString.getBytes());	

      		signature = sig.sign();
			}catch(NoSuchAlgorithmException e){
				logger.severe("Algorithm could not be found: "+e);
			}catch(InvalidKeyException e){
				logger.severe("Private key is invalid: "+e);
			}catch(SignatureException e){
				logger.severe("Could not create signature: "+e);
			}
		}catch(IOException e){
			logger.severe("Could not read file");
		}		
	   return(toHexString(signature));	
   }
	
	/*
	 *	Send the message to the server
	 */
   int sendFileToServer(String URL, String encodedFile, String filename, String encodedKey, String sig, String type){      
      int returnCode = this.ConnectionRefused;
      try{
         
   	Protocol easyhttps = new Protocol("https", new SSLProtocolSocketFactory(), 443);
      Protocol.registerProtocol("https", easyhttps);
         
      HttpClient client = new HttpClient();
         
      
      MultipartPostMethod mPost = new MultipartPostMethod(URL);
		client.setConnectionTimeout(8000);

		// Add the encoded secret key, the encoded message and the signature to the message being sent   
		mPost.addParameter("encodedKey",encodedKey);      
		mPost.addParameter("encodedFile",encodedFile);
		mPost.addParameter("fileName",filename);
		mPost.addParameter("signature",sig);
		mPost.addParameter("type",type);
      
		// Send the message
      int statuscode = client.executeMethod(mPost);
      System.out.println("StatusLine>>>"+ mPost.getStatusLine());

		// Get the message response
      String outcome  = mPost.getResponseBodyAsString();
      outcome = outcome.trim();

      System.out.println("\noutcome: "+outcome+"\n");
      
		// Determine the return code corresponding to the message response
      if (outcome == null){
         returnCode = this.ConnectionRefused;
		}else if (outcome.equals("uploaded")){
         returnCode = this.Success;  
		}else if (outcome.equals("validationFailed")){
         returnCode = this.ValidationFailed;  
      }else if (outcome.equals("decryptFailed")){
         returnCode = this.DecryptFailed;         
      }else if (outcome.equals("messageFailed")){
         returnCode = this.MessageFailed;    
      }else if (outcome.equals("sKeyFailed")){
         returnCode = this.SKeyFailed;   
      }else if (outcome.equals("sigStringFailed")){
         returnCode = this.SigStringFailed;
      }else if (outcome.equals("keyStringFailed")){
         returnCode = this.KeyStringFailed;
      }else if (outcome.equals("nameFailed")){
         returnCode = this.NameFailed;
      }else if (outcome.equals("saveFailed")){
         returnCode = this.SaveFailed;
      }else if (outcome.equals("typeFailed")){
         returnCode = this.TypeFailed;
      }
     
      mPost.releaseConnection();
      
      }catch(ConnectException connEx){
         returnCode = ConnectionRefused;   
      }catch(Exception e){
         e.printStackTrace();
      }
      return returnCode;
   }
   
   /*
	 *	Initiate the logger
	 */
   static void initLogger(){
      logger = Logger.getLogger("CMLClient");
        try {
            // Create an appending file handler
            boolean append = true;
            FileHandler handler = new FileHandler("clientlogs.log", append);
            handler.setFormatter(new SimpleFormatter());
            logger.setLevel(Level.ALL);             
            // Add to the desired logger          
            logger.addHandler(handler);

        } catch (IOException e) {}         
        
   }

	/*
	 *	Convert a byte array to a hexadecimal encoded string
	 */
	public static String toHexString ( byte[] b ) {
		StringBuffer sb = new StringBuffer( b.length * 2 );
	   for ( int i=0; i<b.length; i++ ) {
	      // look up high nibble char
	      sb.append( hexChar [( b[i] & 0xf0 ) >>> 4] ); // fill left with zero bits
 
	      // look up low nibble char
	      sb.append( hexChar [b[i] & 0x0f] );
	   }
	   return sb.toString();
   }

	/*
	 *	Convert a hexadecimal encoded string to a byte array
	 */
	public static byte[] toBinArray( String hexStr ){
		byte bArray[] = new byte[hexStr.length()/2];  
		for(int i=0; i<(hexStr.length()/2); i++){
      	byte firstNibble  = Byte.parseByte(hexStr.substring(2*i,2*i+1),16); // [x,y)
	    	byte secondNibble = Byte.parseByte(hexStr.substring(2*i+1,2*i+2),16);
	    	int finalByte = (secondNibble) | (firstNibble << 4 ); // bit-operations only with numbers, not bytes.
	    	bArray[i] = (byte) finalByte;
		}
      return bArray;
	}

	/*
	 *	Return a string corresponding to the given data in a FileInputStream
	 */
	static String fileInputStreamAsString(FileInputStream stream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }

        br.close();
        return sb.toString();
    }

}

