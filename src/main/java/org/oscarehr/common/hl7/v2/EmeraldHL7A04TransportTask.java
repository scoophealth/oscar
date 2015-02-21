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
package org.oscarehr.common.hl7.v2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;

/**
 * Class HL7A04TransportTask
 */ 
public class EmeraldHL7A04TransportTask extends TimerTask {

	private static final Logger logger = MiscUtils.getLogger();
	private OscarProperties oscarProperties = OscarProperties.getInstance();
	
	private String dirName;
	private String sentDir;
	private String failDir;

	/**
	 * 
	 */
	public class HL7A04FileFilter implements FilenameFilter {
		/**
		 * accept
		 * 
		 * @param dir
		 * @param name
		 */
		public boolean accept(File dir, String name) {
			return name.toLowerCase().endsWith(".txt");
		}
	}
	
	/**
	 * Constructor.
	 */
	public EmeraldHL7A04TransportTask() {
		this.dirName = oscarProperties.getHL7A04BuildDirectory();
		this.sentDir = oscarProperties.getHL7A04SentDirectory();
		this.failDir = oscarProperties.getHL7A04FailDirectory();
	}

	/**
	 * run
	 */
	@Override
	public void run() {
		try {        	
        	File directory = new File(this.dirName);
        	if (!directory.exists()) {
        		logger.info("HL7 A04 directory does not exist!  Location: " + this.dirName);
        		return;
			}
			
			// get files in directory
			File[] listOfFiles = directory.listFiles(new HL7A04FileFilter());
			
			if (listOfFiles == null || listOfFiles.length == 0) {
        		logger.info("No HL7 A04 files to send.");
        		return;
			}
			
			String sendAddr = oscarProperties.getEmeraldHL7A04TransportAddr();
			int sendPort = oscarProperties.getEmeraldHL7A04TransportPort();
			
			Socket client = new Socket(sendAddr, sendPort);
			//getting the o/p stream of that connection
			PrintStream out = new PrintStream(client.getOutputStream());
			//reading the response using input stream
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			logger.info("Sending file(s) to '" + sendAddr + ":" + sendPort + "'");
			
			int numErrors = 0;
			
			for (int i = 0; i < listOfFiles.length; i++)  {				
				String fName = listOfFiles[i].getName();
				
				// Read message
				String message = this.readA04File(this.dirName + fName);
				
				if (message.equals(""))
					continue;
				
				logger.info("Sending HL7 A04 file '" + fName + "' with message: " + message);
				
				// Send message
				this.sendMessage(out, message);
				
				// Get response from Emerald
				String messageReceived = this.getResponse(in);
				
				// Process Emeralds response (returns false if error found in received message)
				boolean emeraldError = (this.processResponse(listOfFiles[i], messageReceived) == false);
				if (emeraldError)
					numErrors++;
				
				// move file to 'sent' directory
				File dir = new File( (!emeraldError? this.sentDir : this.failDir) );				
				boolean moveSuccess = listOfFiles[i].renameTo(new File(dir, fName));
				
				if (!moveSuccess) {
					// File was not successfully moved
					logger.info("HL7 A04 not successfully moved to 'sent' directory.");
				}
			}
			
			in.close();
			out.close();
			
			if (!client.isClosed())
				client.shutdownOutput();
			if (!client.isClosed())
				client.close();
			
			if (numErrors == 0)
				logger.info("Successfully sent all HL7 A04 file(s)!");
			else {
				logger.info("Finished sending all HL7 A04 file(s).");
				logger.info("Warning: " + numErrors + " error(s) occured during transmission.");
			}
		} catch (Exception e) {
			logger.error("ERROR while sending HL7 A04 file: " + e.toString(),e);
		}
	}
	
	/**
	 * processResponse
	 * 
	 * @param file
	 * @param messageReceived
	 * 
	 * @return true if no errors found in received message, false otherwise
	 */
	private boolean processResponse(File file, String messageReceived) {
		boolean success = checkResponse(messageReceived);
		
		if (!success) {
			logger.info("Received error from Emerald for A04 file '" + file.getName() + "'.");
			logger.info("Received message: " + messageReceived);
			createErrorFile(file, messageReceived);
		}
		
		return success;
	}
	
	/**
	 * checkResponse
	 * 
	 * @param messageReceived
	 * 
	 * @return true if no errors found in received message, false otherwise
	 */
	private boolean checkResponse(String messageReceived) {
		boolean success = true;
		
		if (messageReceived.indexOf("java.lang.IllegalArgumentException") != -1)
			success = false;
		
		return success;
	}
	
	/**
	 * sendMessage
	 * Helper method for sending an Hl7 A04 message to Emerald.
	 * 
	 * NOTES:
	 * 1. '0x0b' is a character that needs to be sent before an HL7 message
	 * 2. '0x0d0x1c0x0d' is the character sequence that needs to be sent after an HL7 message
	 * 3. HL7 specification requires \r to be used instead of \n
	 * 
	 * @param out
	 * @param message
	 */
	private void sendMessage(PrintStream out, String message) {
		out.print((char)0x0b);
		out.print(message.replaceAll("\n", "\r"));
		out.print((char)0x0d);
		out.print((char)0x1c);
		out.print((char)0x0d);
	}
	
	/**
	 * getResponse
	 * Helper method to get the response message from Emerald.
	 * 
	 * NOTES:
	 * 1. '0x1c' is the last character in the message from Emerald (i.e. EOF character).
	 * 
	 * @param in
	 * 
	 * @return The Emerald String response
	 */
	private String getResponse(BufferedReader in) throws java.io.IOException {
		String messageReceived = "";
		String messageReceivedPart = "";
		
		while (true) {
			messageReceivedPart = in.readLine();
			if (messageReceivedPart == null)
				break;
			messageReceived += messageReceivedPart;
			if (messageReceivedPart.charAt(0) == 28) // (char)0x1c
				break;
		}
		
		return messageReceived;
	}
	
	/**
	 * readA04File
	 * 
	 * @param theFile
	 * 
	 * @return The contents of the A04 file
	 */
	private String readA04File(String theFile) throws java.io.FileNotFoundException, java.io.IOException {
		FileInputStream fin = new FileInputStream(theFile);
		DataInputStream dis = new DataInputStream(fin);
		
		// Read message
		String message = "";
		String messagePart = "";
		while ((messagePart = dis.readLine()) != null)
			message += messagePart + "\r";
			
		fin.close();
		dis.close();
		
		return message;
	}
	
	/**
	 * createErrorFile
	 * 
	 * @param sentFile
	 * @param messageReceived
	 */
	private void createErrorFile(File sentFile, String messageReceived) {
		File f = new File(this.failDir + sentFile.getName().replace(".txt", "") + "_ERROR.txt");
		if (!f.exists()) {
			try {
				f.createNewFile();
				FileWriter fstream = new FileWriter(f);
				BufferedWriter out = new BufferedWriter(fstream);
				out.write(messageReceived);
				//Close the output stream
				out.close();
			} catch (Exception e) { //Catch exception if any
				logger.error("Error occured while creating error file: " + e.getMessage());
			}
		}
	}
}
