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
package org.oscarehr;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SSLSocketFactoryWrapper extends SSLSocketFactory {

    private final SSLSocketFactory wrappedFactory;
    private final SSLParameters sslParameters;

    public SSLSocketFactoryWrapper(SSLSocketFactory factory, SSLParameters sslParameters) {
        this.wrappedFactory = factory;
        this.sslParameters = sslParameters;
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        SSLSocket socket = (SSLSocket) wrappedFactory.createSocket(host, port);
        setParameters(socket);
        return socket;
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
            throws IOException, UnknownHostException {
        SSLSocket socket = (SSLSocket) wrappedFactory.createSocket(host, port, localHost, localPort);
        setParameters(socket);
        return socket;
    }


    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        SSLSocket socket = (SSLSocket) wrappedFactory.createSocket(host, port);
        setParameters(socket);
        return socket;
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        SSLSocket socket = (SSLSocket) wrappedFactory.createSocket(address, port, localAddress, localPort);
        setParameters(socket);
        return socket;

    }

    @Override
    public Socket createSocket() throws IOException {
        SSLSocket socket = (SSLSocket) wrappedFactory.createSocket();
        setParameters(socket);
        return socket;
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return wrappedFactory.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return wrappedFactory.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        SSLSocket socket = (SSLSocket) wrappedFactory.createSocket(s, host, port, autoClose);
        setParameters(socket);
        return socket;
    }

    private void setParameters(SSLSocket socket) {
        socket.setSSLParameters(sslParameters);
    }

}