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
<%@page import="com.indivica.olis.parameters.*,com.indivica.olis.*,com.indivica.olis.queries.*,org.apache.commons.lang.time.DateUtils"%>
<%@page import="oscar.OscarProperties,java.net.InetAddress,java.io.*,java.util.List,java.util.*,javax.net.ssl.*,java.security.*,java.security.cert.*"%>
<%@page import="org.oscarehr.util.DbConnectionFilter,java.sql.*,org.oscarehr.util.SpringUtils" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>
<%

OscarProperties oscarProperties = OscarProperties.getInstance();

List<String> errors = new ArrayList<String>();

checkProperties(oscarProperties,errors);

int testPatientLookup = 2;

if(oscarProperties.getProperty("olis_request_url") != null &&  oscarProperties.getProperty("olis_request_url").equals("https://cst.olis.ssha.ca/ssha.olis.webservices.ER7/OLIS.asmx") ){
	testPatientLookup = 1;
}

%>
<html>
	<body>

	<h1>Olis Configuration Checker</h1>

	<h3>Checking Properties</h3>
	<ul>
		<%for(String errorString:errors){ %>
			<li><%=errorString%></li>
		<%}%>
	</ul>
	<h3>Providers Configured</h3>
	<table border=1>
	
		<tr>
			<th>provider #</th>
			<th>Lastname</th>
			<th>Firstname</th>
			<th>CSPO</th>
			<th>Official Last Name</th>
			<th>Official First Name</th>
			<th>Official Second Name</th>
			<th>Received</th>
			<th>Sent</th>
		</tr>
	 <%
	 	String error = null;
        Connection c = DbConnectionFilter.getThreadLocalDbConnection(); //select only
        try {

            PreparedStatement ps_findConfiguredProviders = c.prepareStatement("select * from provider where practitionerNo != ''");
            PreparedStatement ps_prop = c.prepareStatement("select * from property where provider_no = ? and name = ?");
            ResultSet rs = ps_findConfiguredProviders.executeQuery();
            
            while (rs.next()) {
            	String providerNo = rs.getString("provider_no");
            	String lastName   = rs.getString("last_name");
            	String firstName  = rs.getString("first_name");
            	String cpso = rs.getString("practitionerNo");
            	String officialFirstName = findProp(ps_prop, providerNo, "official_first_name");
            	String officialLastName = findProp(ps_prop, providerNo, "official_last_name");
            	String officialSecondName = findProp(ps_prop, providerNo, "official_second_name");

            	
            	%>  
            	<tr>
					<td><%=providerNo%></td>
					<td><%=lastName%></td>
					<td><%=firstName%></td>
					<td><%=cpso%></td>
					<td><%=officialLastName%></td>
					<td><%=officialFirstName%></td>
					<td><%=officialSecondName%></td>
					<%if(cpso != null  ){ %>
					<td><textarea cols=100 rows=10><%=tryZ01Query(testPatientLookup, cpso, officialLastName, officialFirstName, officialSecondName,request)%></textarea></td>
					<td><textarea cols=100 rows=10><%=request.getAttribute("msgInXML")%></textarea></td>
					<%
					request.setAttribute("msgInXML","");
					}%>
				</tr>
            	
            	
            	<%
            }
        }catch(Exception e){
        	error = org.apache.commons.lang.exception.ExceptionUtils.getStackTrace(e);      
        }
	%>
	</table>
	<%if(error !=null){%>
		<pre><%=error%></pre>
	<% }%>
	</body>
</html>

<%!


public String tryZ01Query(int patientNum,String cpso,String lastName,String firstName, String secondName,HttpServletRequest request)throws Exception{
	
		if(lastName == null){
			return "Provider Record:  Official Last Name Required";
		}else if(firstName == null){
			return "Provider Record:  Official First Name Required";
		}else if(secondName == null){
			return "Provider Record:  Official Second Name Required";
		}
		
	
		Query query = new Z01Query();

		String[] dateFormat = new String[] {
				"yyyy-MM-dd"
		};

		
		String startTimePeriod = "2001-01-01";
		String endTimePeriod = "2014-01-01";
		
		java.util.Date startTime = DateUtils.parseDate(startTimePeriod, dateFormat);	
		java.util.Date endTime = changeToEndOfDay(DateUtils.parseDate(endTimePeriod, dateFormat));

		List<java.util.Date> dateList = new LinkedList<java.util.Date>();
		dateList.add(startTime);
		dateList.add(endTime);

		OBR22 obr22 = new OBR22();
		obr22.setValue(dateList);

		((Z01Query) query).setStartEndTimestamp(obr22);
			
		PID3 pid3 = null;	
		if(patientNum == 1){
				
			pid3 = new PID3("2000010674", null, null, "JHN", "ON", "HL70347", "F", null);
			pid3.setValue(7, DateUtils.parseDate("1990-12-12", dateFormat));

		}else if(patientNum ==2){
			pid3 = new PID3("9999999999", null, null, "JHN", "ON", "HL70347", "M", null);
			pid3.setValue(7, DateUtils.parseDate("1950-01-01", dateFormat));

		}

		
		((Z01Query) query).setPatientIdentifier(pid3);
			
	
				
		ZRP1 zrp1 = new ZRP1(cpso, "MDL", "ON", "HL70347", lastName,firstName,secondName);

		((Z01Query) query).setRequestingHic(zrp1);
	
				
		com.indivica.olis.Driver.submitOLISQuery(request, query);
		String msgInXML = (String) request.getAttribute("msgInXML");
		String signedRequest = (String) request.getAttribute("signedRequest");
		String signedData = (String) request.getAttribute("signedData");
		String unsignedData = (String) request.getAttribute("unsignedResponse" );
			
		return unsignedData;
		
}


private java.util.Date changeToEndOfDay(java.util.Date d) throws Exception{
	Calendar c = Calendar.getInstance();
	c.setTime(d);
	c.set(Calendar.HOUR_OF_DAY, 23);
	c.set(Calendar.MINUTE, 59);
	c.set(Calendar.SECOND,59);
	return c.getTime();
}

public String findProp(PreparedStatement ps,String providerNo, String name) throws Exception{
	 String retval = null;
	 ps.setString(1,providerNo);
	 ps.setString(2,name);
	 
	 ResultSet rs = ps.executeQuery();
	 
     while (rs.next()) {
    	 retval = rs.getString("value");
     }
     return retval;
}

public String prob(String s){
	return "<div style='color:red;'>"+s+"</div>";
}

public String good(String s){
    return s;
}

public void checkProperties(OscarProperties p,List<String> errors){
	checkSendingApplicationFormat("OLIS_SENDING_APPLICATION",p,errors);
	checkOlisKeystore("olis_keystore","changeit",p,errors);
	checkOlisSSLKeystore("olis_ssl_keystore","olis_ssl_keystore_password",p,errors);
	checkIP(errors);
	checkTrustStore("olis_truststore","olis_truststore_password",p,errors);
	checkTrustStoreAndKeyStore("olis_truststore","olis_truststore_password","olis_ssl_keystore","olis_ssl_keystore_password",p,errors);
	checkReturnedCert("olis_returned_cert",p,errors);
	
}

public boolean checkSendingApplicationFormat(String s,OscarProperties p,List<String> errors){
	boolean correct = true;
	String property = p.getProperty(s);
	if(property  == null){
        errors.add(prob(s+" can't be null"));
        return false;
    }
    if(!property.startsWith("^2.16.840.1.113883.3.239.14:")){
    	errors.add(prob(s+" Needs to be in the format ^2.16.840.1.113883.3.239.14:EMR9999^ISO   "+property));
    	return false;
    }
    if(!property.endsWith("^ISO")){
    	errors.add(prob(s+" Needs to be in the format ^2.16.840.1.113883.3.239.14:EMR9999^ISO   "+property));
    	return false;
    }    
    return correct;
}

public boolean checkOlisKeystore(String key,String password,OscarProperties p,List<String> errors){
	boolean correct = true;
	if(checkFileCanBeRead(key,p, errors)){
		String filepath = p.getProperty(key); 
		PrivateKey priv = null;
		KeyStore keystore = null;
		try {
			keystore = KeyStore.getInstance("JKS");
			// Load the keystore
			keystore.load(new FileInputStream(filepath), password.toCharArray());
			if(keystore.size() == 0){
				errors.add(prob(key+" keystore is empty"));
				return false;
			}
			//Enumeration e = keystore.aliases();
			String name = "olis";

			// Get the private key and the certificate
			priv = (PrivateKey) keystore.getKey(name, password.toCharArray());
			if(priv == null){
				errors.add(prob(key+"private key was not loaded"));
				return false;
			}
			
		}catch(Exception e){
			errors.add(prob("Olis keystore error :"+key+" -- "+e.getMessage()));
			return false;
		}
	}
	
	return correct;
}

public boolean checkOlisSSLKeystore(String key,String password,OscarProperties p,List<String> errors){
	boolean correct = true;
	if(checkFileCanBeRead(key,p, errors)){
		PrivateKey priv = null;
		KeyStore keystore = null;
		String filepath = p.getProperty(key);
		String pwd = p.getProperty(password);
		try {
			 
			keystore = KeyStore.getInstance("JKS");
			// Load the keystore
			keystore.load(new FileInputStream(filepath), pwd.toCharArray());
			if(keystore.size() == 0){
				errors.add(prob(key+" keystore is empty"));
				return false;
			}
		}catch(Exception e){
			errors.add(prob("SSL KeyStore Error: File="+filepath+" Password ="+pwd+ "Error message: "+e.getMessage()));
			return false;
		}
	}
	return correct;
}

public boolean checkIP(List<String> errors){
	boolean correct = true;
	try{
		InetAddress address = InetAddress.getByName("olis.ssha.ca");
		String ipAddr =  address.getHostAddress();
		if(!"76.75.164.17".equals(ipAddr)){
			errors.add(prob("Add line in /etc/hosts file:<br><br> 76.75.164.17   olis.ssha.ca"));
			correct = false;
		}
	}catch(Exception e){
		errors.add(prob("Error checking IP"+e.getMessage()));
		return false;
	}
	return correct;
}

public boolean checkTrustStoreAndKeyStore(String trustStoreKey,String trustStorePassword,String keyStoreKey, String keyStorePassword, OscarProperties p,List<String> errors){
	boolean correct = true;
	PrivateKey priv = null;
	KeyStore keystore = null;
	String trustStoreFilepath = p.getProperty(trustStoreKey);
	String trustStorePwd = p.getProperty(trustStorePassword);
	String keyStoreFilepath = p.getProperty(keyStoreKey);
	String keyStorePwd = p.getProperty(keyStorePassword);
	
	try {
		
		keystore = KeyStore.getInstance("JKS");
		// Load the keystore
		keystore.load(new FileInputStream(trustStoreFilepath), trustStorePwd.toCharArray());
		if(keystore.size() == 0){
			errors.add(prob(trustStoreFilepath+" Truststore is empty"));
			return false;
		}
		
		KeyStore keystoreolis = KeyStore.getInstance("JKS");
		keystoreolis.load(new FileInputStream(keyStoreFilepath), keyStorePwd.toCharArray());
		
		
		KeyManagerFactory kmf=KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		
		kmf.init(keystoreolis,keyStorePwd.toCharArray());
		KeyManager[] keystoreManagers=kmf.getKeyManagers();
		
		
		
		SSLContext context = SSLContext.getInstance("TLS");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keystore);
        X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
        //SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
        //context.init(null, new TrustManager[]{tm}, null);
        context.init(keystoreManagers, new TrustManager[]{defaultTrustManager}, null);
        
        SSLSocketFactory factory = context.getSocketFactory();

        SSLSocket socket = (SSLSocket) factory.createSocket(getDomain(), 443);
        socket.setNeedClientAuth(true);
        socket.setSoTimeout(10000);
        try {
            //Starting SSL handshake...");
            socket.startHandshake();
            socket.close();
            //No errors, certificate is already trusted");
        } catch (SSLException e) {
        	errors.add(prob("SSLException Error: "+e.getMessage()));
            return false;
        }
		
		
	}catch(Exception e){
		errors.add(prob("Connection Error: File="+trustStoreFilepath+","+keyStoreFilepath+" Password ="+trustStorePwd+ "Error message: "+e.getMessage()));
		return false;
	}
	return correct;
}


public boolean checkTrustStore(String s,String password,OscarProperties p,List<String> errors){
	boolean correct = true;
	PrivateKey priv = null;
	KeyStore keystore = null;
	String filepath = p.getProperty(s);
	String pwd = p.getProperty(password);
	try {
		
		keystore = KeyStore.getInstance("JKS");
		// Load the keystore
		keystore.load(new FileInputStream(filepath), pwd.toCharArray());
		if(keystore.size() == 0){
			errors.add(prob(s+" Truststore is empty"));
			return false;
		}
		
		
		SSLContext context = SSLContext.getInstance("TLS");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keystore);
        X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
        //SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
        //context.init(null, new TrustManager[]{tm}, null);
        context.init(null, new TrustManager[]{defaultTrustManager}, null);
        
        SSLSocketFactory factory = context.getSocketFactory();

        SSLSocket socket = (SSLSocket) factory.createSocket(getDomain(), 443);
        socket.setNeedClientAuth(true);
        socket.setSoTimeout(10000);
        try {
            //Starting SSL handshake...");
            socket.startHandshake();
            socket.close();
            //No errors, certificate is already trusted");
        } catch (SSLException e) {
        	if(!"Received fatal alert: certificate_unknown".equals(e.getMessage())){
        		errors.add(prob("SSLException Truststore Error: "+e.getMessage()));
            	return false;
        	}
        }
		
		
	}catch(Exception e){
		errors.add(prob("Truststore Error: File="+filepath+" Password ="+pwd+ "Error message: "+e.getMessage()));
		return false;
	}
	return correct;
}

public String getDomain(){
	OscarProperties oscarProperties =  OscarProperties.getInstance();
	if(oscarProperties.getProperty("olis_request_url") != null &&  oscarProperties.getProperty("olis_request_url").equals("https://cst.olis.ssha.ca/ssha.olis.webservices.ER7/OLIS.asmx") ){
		return "cst.olis.ssha.ca";
	}
	return "olis.ssha.ca";
}


public boolean checkReturnedCert(String s,OscarProperties p,List<String> errors){
	boolean correct = true;
	
	try{
		FileInputStream is = new FileInputStream(p.getProperty(s));
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		X509Certificate cert = (X509Certificate) cf.generateCertificate(is);
		if (cert == null){
			errors.add(prob("Error loading Cert:"+s));
			return false;
		}
	}catch(Exception e){
		errors.add(prob("Error loading Returned Cert"+s+"----"+e.getMessage()));
		return false;	
	}
    return correct;
}



public String checkProperty(String s){
        if(OscarProperties.getInstance().getProperty(s)  == null){
           return prob(s+" can't be null");
        }
        return s;
}

public boolean checkFileCanBeRead(String s,OscarProperties p,List<String> errors){
	boolean canRead = true;
	String property = p.getProperty(s);
	if(property  == null){
        errors.add(prob(s+" can't be null"));
        return false;
    }
	File file = new File(property);
	
	if(!file.exists()){
		errors.add(prob(property+ "doesn't exist"));
		return false;
	}
	if(!file.canRead()){
		errors.add(prob(property+ " tomcat can't read this file"));
		return false;
	}
	return canRead;
}


%>