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
package org.oscarehr.research.eaaps;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.log4j.Logger;

import oscar.OscarProperties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * Handler class for uploading files to the server via scp protocol.
 *
 */
public class SshHandler {

	private static Logger logger = Logger.getLogger(SshHandler.class);

	private JSch jsch = new JSch();
	private String userName;
	private String host;
	private int port;
	private String password;
	private Session session;
	private int timeOut;

	public SshHandler(String userName, String password, String connectionIP, String knownHostsFileName, int port, int timeout) throws Exception {
		OscarProperties props = OscarProperties.getInstance();
		if (userName == null) {
			userName = props.getProperty("eaaps.user");
		}
		if (password == null) {
			password = props.getProperty("eaaps.pass");
		}
		if (connectionIP == null) {
			connectionIP = props.getProperty("eaaps.host");
		}
		if (knownHostsFileName == null) {
			knownHostsFileName = props.getProperty("eaaps.knownHosts");
		}
		try {
			jsch.setKnownHosts(knownHostsFileName);
		} catch (JSchException e) {
			throw new Exception("Unable to set known hosts", e);
		}

		setUserName(userName);
		setPassword(password);
		setHost(connectionIP);
		setPort(port);
		setTimeOut(timeout);
	}

	public SshHandler() throws Exception {
		this(null, null, null);
	}
	
	public SshHandler(String userName, String password, String connectionIP) throws Exception {
		this(userName, password, connectionIP, "", 22, 60000);
	}

	public void connect() throws Exception {
		try {
			Session session = jsch.getSession(getUserName(), getHost(), getPort());
			session.setPassword(getPassword());
			// UNCOMMENT THIS FOR TESTING PURPOSES, BUT DO NOT USE IN PRODUCTION
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect(getTimeOut());

			setSession(session);
		} catch (JSchException e) {
			throw new Exception(e);
		}
	}

	protected ChannelSftp openChannel() throws Exception {
		Channel channel = getSession().openChannel("sftp");
		channel.connect();
		return (ChannelSftp) channel;
	}

	public void put(String content, String fileName) throws Exception {
		ChannelSftp channel = null;
		try {
			channel = openChannel();
			channel.put(new ByteArrayInputStream(content.getBytes()), fileName);
		} finally {
			if (channel != null) {
				channel.exit();
			}
		}
	}
	
	public void put(List<File> files) throws Exception {
		ChannelSftp channel = null;
		try {
			channel = openChannel();
			for(File file : files) {
				put(channel, file);
			}
		} finally {
			if (channel != null) {
				channel.exit();
			}
		}
	}

	private void put(ChannelSftp channel, File file) throws SftpException, FileNotFoundException {
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(file));
			channel.put(is, file.getName());
		} finally {
			if (is != null) {
				try {
	                is.close();
                } catch (IOException e) {
                	logger.error("Unable to close input stream for " + file, e);
                }
			}
		}
		
    }

	public void close() {
		if (getSession() != null) {
			getSession().disconnect();
		}
	}

	public JSch getJsch() {
		return jsch;
	}

	public void setJsch(JSch jsch) {
		this.jsch = jsch;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session connection) {
		this.session = connection;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

}