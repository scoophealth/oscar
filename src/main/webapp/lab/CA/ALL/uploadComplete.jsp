<%@page contentType="text/xml"%><%@page import="javax.xml.parsers.*, org.w3c.dom.*, oscar.util.*"%><%
 
DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
Document doc = builder.newDocument();

Element element = doc.createElement("labUploadResult");
doc.appendChild(element);
Element outcome = doc.createElement("outcome");
Element audit = doc.createElement("audit");
element.appendChild(outcome);
element.appendChild(audit);
String s = (String) request.getAttribute("outcome");
String a = (String) request.getAttribute("audit");
if (s == null) s = "accessDenied";
if (a == null) a = "failure";
outcome.appendChild(doc.createTextNode(s));
audit.appendChild(doc.createTextNode(a));
out.write(UtilXML.toXML(doc));
%>

<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>