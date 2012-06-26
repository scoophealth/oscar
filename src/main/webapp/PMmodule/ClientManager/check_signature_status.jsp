<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="org.oscarehr.util.DigitalSignatureUtils"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.common.model.Provider" %>
<%@page import="java.io.File" %>

<%

Provider provider = (Provider) session.getAttribute(SessionConstants.LOGGED_IN_PROVIDER);
if (provider == null) {
%>NOT_AUTHORIZED<%
}
%>


<%

String signatureRequestId = request.getParameter(DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY);
String tempFilePath = DigitalSignatureUtils.getTempFilePath(signatureRequestId);
File f = new File(tempFilePath);
if(f.exists()) {
%>FOUND<%
} else {
%>NOT_FOUND<%
}
%>
