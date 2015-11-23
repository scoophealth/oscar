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


/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.    
 */
package oscar.oscarRx.util;

import java.net.URL;

import org.apache.xmlrpc.AsyncCallback;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.oscarehr.util.MiscUtils;


/**
 * <p>A callback object that can wait up to a specified amount
 * of time for the XML-RPC response. Suggested use is as follows:
 * </p>
 * <pre>
 *   // Wait for 10 seconds.
 *   TimingOutCallback callback = new TimingOutCallback(10 * 1000);
 *   XmlRpcClient client = new XmlRpcClient(url);
 *   client.executeAsync(methodName, aVector, callback);
 *   try {
 *       return callback.waitForResponse();
 *   } catch (TimeoutException e) {
 *       MiscUtils.getLogger().debug("No response from server.");
 *   } catch (Exception e) {
 *       MiscUtils.getLogger().debug("Server returned an error message.");
 *   }
 * </pre>
 */
public class TimingOutCallback implements AsyncCallback {
    /** This exception is thrown, if the request times out.
     */
    public static class TimeoutException extends XmlRpcException {
        private static final long serialVersionUID = 4875266372372105081L;

        /** Creates a new instance with the given error code and
         * error message.
         */
        public TimeoutException(int pCode, String message) {
            super(pCode, message);
        }
    }

    private final long timeout;
    public Object result;
    private Exception error;
    private boolean responseSeen;

    /** Waits the specified number of milliseconds for a response.
     */
    public TimingOutCallback(long pTimeout) {
        timeout = pTimeout;
    }

    /** Called to wait for the response.
     * @throws InterruptedException The thread was interrupted.
     * @throws TimeoutException No response was received after waiting the specified time.
     * @throws Exception An error was returned by the server.
     */
    public synchronized Object waitForResponse() throws Exception {
        if (!responseSeen) {
            wait(timeout);
            if (!responseSeen) {
                throw new TimeoutException(0, "No response after waiting for " + timeout + " milliseconds.");
            }
        }
        if (error != null) {
            throw error;
        }
        return result;
    }

    public synchronized void handleError(XmlRpcRequest pRequest, Exception pError) {
        responseSeen = true;
        error = pError;
        notify();
    }

    public synchronized void handleResult(XmlRpcRequest pRequest, Object pResult) {
        responseSeen = true;
        result = pResult;
        notify();
    }

    public synchronized void handleResult(Object arg0, URL arg1, String arg2) {
        responseSeen = true;
        MiscUtils.getLogger().debug("arg2"+arg2);
        result = arg0;
         this.notify();
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public synchronized void handleError(Exception arg0, URL arg1, String arg2) {
        responseSeen = true;
        MiscUtils.getLogger().error("Error", arg0);
        error = arg0;
        this.notify();
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
