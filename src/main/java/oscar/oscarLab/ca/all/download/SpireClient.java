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
package oscar.oscarLab.ca.all.download;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * 
 * 
 */ 
public class SpireClient extends TimerTask {
	private ChannelSftp spireSFTPChannel;
	private Session spireSession;
	
	private oscar.OscarProperties p = oscar.OscarProperties.getInstance();
	private static Logger logger = MiscUtils.getLogger();
	
	/**
	 * Constructor.
	 */ 
	public SpireClient() {			
	}
	
	/**
	 * 
	 */ 
	@Override
	public void run() {
		try {
			connect();
			
			logger.info("Copying files from Spire...");
			List files = spireSFTPChannel.ls("/");
			
			String saveDir = p.getSpireDownloadDir();
			
			if (files.size() == 0) {
				logger.info("No files found.");
			} else {
				for (int i = 0; i < files.size() && i < 25; i++) {
					com.jcraft.jsch.ChannelSftp.LsEntry file = (com.jcraft.jsch.ChannelSftp.LsEntry) files.get(i);
					
					if (file.getAttrs().isDir())
						continue;
			
					logger.info("Downloading file " + file.getFilename());

					File f = new File(saveDir + file.getFilename() + ".HL7");
					spireSFTPChannel.get(file.getFilename(), new FileOutputStream(f));
					
					//spireSFTPChannel.rename(file.getFilename(), "downloaded/" + file.getFilename());
					
					spireSFTPChannel.rm(file.getFilename());
				}
			}		
			
			logger.info("Done copying files.");
			
			
			File directory = new File(saveDir);
			
			File[] listOfFiles = directory.listFiles();
			if (listOfFiles == null || listOfFiles.length == 0) {
        		logger.info("No hl7 files to move.");
        		return;
			}
			
			for (int i = 0; i < listOfFiles.length && i < 25; i++)  {				
				String fName = listOfFiles[i].getName();

				File dir = new File( p.getSpireDownloadDir() );				
				boolean moveSuccess = listOfFiles[i].renameTo(new File(dir, fName));
				
				if (!moveSuccess) {
					// File was not successfully moved
					logger.info("HL7 not successfully moved to 'incoming' directory.");
				}
				
				Thread.sleep(2000);
			}
			
			logger.info("Done moving files.");
			
			
		} catch(Exception e){
			logger.error("Error: " + e.toString());
	    } finally {
			disconnect();
		}
	}
	
	/**
	 * 
	 */ 
	private void connect() throws JSchException {
		logger.info("Connecting to Spire...");
		if (spireSFTPChannel != null || spireSession != null)
			disconnect();
		
		JSch jsch = new JSch();
		
		spireSession = jsch.getSession( p.getSpireServerUser(), p.getSpireServerHostname() );
		
		java.util.Properties confProp = new java.util.Properties();
		confProp.put("StrictHostKeyChecking", "no");
		spireSession.setConfig(confProp);
		
		spireSession.setPassword( p.getSpireServerPassword() );
					
		spireSession.connect();
		
		Channel channel = spireSession.openChannel( "sftp" );
		channel.connect();
		
		spireSFTPChannel = (ChannelSftp) channel;
		
		logger.info("Connected to Spire.");
	}
	
	/**
	 * 
	 */ 
	private void disconnect() {
		logger.info("Disconnecting from Spire...");
		
		if (spireSFTPChannel != null)
			spireSFTPChannel.exit();
		spireSFTPChannel = null;
		
		if (spireSession != null)
			spireSession.disconnect();
		spireSession = null;
		
		logger.info("Done disconnecting from Spire.");
	}
	
}
