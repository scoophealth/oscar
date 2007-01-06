/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.PMmodule.integrator.xfire;
import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.handler.AbstractHandler;
import org.jdom.Element;
import org.jdom.Namespace;


public class OutgoingAuthenticationHandler extends AbstractHandler {

	private String username = null;
	private String password = null;
	
	public OutgoingAuthenticationHandler() {
	}
	
	public OutgoingAuthenticationHandler(String username,String password) {
		this.username = username;
		this.password = password;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void invoke(MessageContext context) throws Exception {
		Element el = context.getOutMessage().getHeader();
		final Namespace ns = Namespace.getNamespace("caisi","http://ws.integrator.caisi.org");  
        el.addNamespaceDeclaration(ns);
        
        Element auth = new Element("AuthenticationToken", ns);
        Element username_el = new Element("Username",ns);
        username_el.addContent(username);
        Element password_el = new Element("Password",ns);
        password_el.addContent(password);
        auth.addContent(username_el);
        auth.addContent(password_el);
        el.addContent(auth);
	}

}

