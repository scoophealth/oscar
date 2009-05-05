
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.InputStream"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%><%
System.err.println("UPLOAD SIGNATURE");
System.err.println("LOGGED IN USER "+LoggedInInfo.loggedInInfo.get().loggedInProvider);
InputStream is=request.getInputStream();
FileOutputStream fos=new FileOutputStream("/tmp/sigtestuploaded.jpg");
int i=0;
while ((i=is.read())!=-1)
{
	fos.write(i);
}
fos.flush();
fos.close();
%>