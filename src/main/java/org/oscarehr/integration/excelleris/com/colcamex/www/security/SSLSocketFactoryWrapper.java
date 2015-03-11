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
package org.oscarehr.integration.excelleris.com.colcamex.www.security;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLSocket;  
import javax.net.ssl.SSLSocketFactory;
/**
 * @author Dennis Warren Colcamex Resources
 * 
 * Major Contributors: 
 *  OSCARprn
 *  NERD
 *   
 * This community edition of Expedius is for use at your own risk, without warranty, and 
 * support. 
 * 
 */
public class SSLSocketFactoryWrapper extends SSLSocketFactory {

	private final SSLSocketFactory socketFactory;
	private final String[] protocols;
	private final String[] cipherSuites;

	
	public SSLSocketFactoryWrapper(final SSLSocketFactory socketFactory, 
			final String[] protocols, final String[] cipherSuites) {
		this.socketFactory = socketFactory;
		this.protocols = protocols;
		this.cipherSuites = cipherSuites;
	}
	
	@Override
	public Socket createSocket(Socket s, String host, int port,
			boolean autoClose) throws IOException {
		final Socket socket = socketFactory.createSocket(s, host, port, autoClose);  
        return override(socket);
	}

	@Override
	public String[] getDefaultCipherSuites() {
		return this.socketFactory.getDefaultCipherSuites();
	}

	@Override
	public String[] getSupportedCipherSuites() {
		return this.socketFactory.getSupportedCipherSuites();
	}

	@Override
	public Socket createSocket(String host, int port) throws IOException,
			UnknownHostException {
		final Socket socket = socketFactory.createSocket( host, port);  
        return override(socket);
	}

	@Override
	public Socket createSocket(InetAddress host, int port) throws IOException {
		final Socket socket = socketFactory.createSocket(host, port);  
        return override(socket);
	}

	@Override
	public Socket createSocket(String host, int port, InetAddress localHost,
			int localPort) throws IOException, UnknownHostException {
		final Socket socket = socketFactory.createSocket(host, port);  
        return override(socket);
	}

	@Override
	public Socket createSocket(InetAddress address, int port,
			InetAddress localAddress, int localPort) throws IOException {
		final Socket socket = socketFactory.createSocket(address, port, localAddress, localPort);  
        return override(socket);
	}
	
	private Socket override(final Socket socket) {  
        if (socket instanceof SSLSocket) { 
        	
            if (protocols != null && protocols.length > 0) {  
                ((SSLSocket) socket).setEnabledProtocols(protocols);  
            }  
            
            if( (cipherSuites != null) && (cipherSuites.length > 0) ) {
            	((SSLSocket) socket).setEnabledCipherSuites(cipherSuites);
            }
            
        }  
        
        return socket;  
    } 

}
