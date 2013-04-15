package org.oscarehr.olis;

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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.SocketFactory;


import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

import oscar.OscarProperties;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

public class OLISProtocolSocketFactory implements SecureProtocolSocketFactory{
	SSLContext context = null;
	
	public  OLISProtocolSocketFactory() throws Exception{
	
		String pKeyFile = OscarProperties.getInstance().getProperty("olis_ssl_keystore").trim();
		String pKeyPassword = OscarProperties.getInstance().getProperty("olis_ssl_keystore_password").trim();

			
		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        KeyStore keyStore = KeyStore.getInstance("JKS");
        InputStream keyInput = new FileInputStream(pKeyFile);
        keyStore.load(keyInput,  pKeyPassword.toCharArray());
        keyInput.close();
        keyManagerFactory.init(keyStore, pKeyPassword.toCharArray());

        context = SSLContext.getInstance("TLS");
        context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
	}

	public Socket createSocket(final String host, final int port, final InetAddress localAddress, final int localPort, final HttpConnectionParams params) throws IOException {
		if (params == null) {
			throw new IllegalArgumentException("Parameters may not be null");
		}
		int timeout = params.getConnectionTimeout();
		SocketFactory socketfactory = context.getSocketFactory();
		if (timeout == 0) {
			return socketfactory.createSocket(host, port, localAddress, localPort);
		} else {
			Socket socket = socketfactory.createSocket();
			SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
			SocketAddress remoteaddr = new InetSocketAddress(host, port);
			socket.bind(localaddr);
			socket.connect(remoteaddr, timeout);
			return socket;
		}
	}

	public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException {
		return context.getSocketFactory().createSocket(host, port, clientHost, clientPort);
	}

	public Socket createSocket(String host, int port) throws IOException {
		return context.getSocketFactory().createSocket(host, port);
	}

	public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
		return context.getSocketFactory().createSocket(socket, host, port, autoClose);
	}

}
