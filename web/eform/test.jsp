<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
%>

<%@ page import="javax.servlet.*, java.io.*" errorPage="../appointment/errorpage.jsp" contentType="image/jpg" %>
<%
  ServletContext sc = getServletContext();
  InputStream is = sc.getResourceAsStream("../images/stonechurch.jpg"); //../images/stonechurch.jpg");

  response.setContentType("image/jpg");
  ServletOutputStream sos = response.getOutputStream();
//  out.print("*****************");
//  out.clear();

  byte[] bytes = new byte[1024]; 
  int c;
//  while((c=is.read()) != -1) {
//  c=is.read(bytes);
//	sos.write(bytes);
//  }
  sos.println("99");
  sos.flush();
  is.close();
  sos.close();

/*
LOAD_FILE(file_name) 
Reads the file and returns the file contents as a string. The file must be on the server, you must specify the full pathname to the file,
and you must have the FILE privilege. The file must be readable by all and be smaller than max_allowed_packet. If the file doesn't exist 
or can't be read due to one of the above reasons, the function returns NULL: 
mysql> UPDATE tbl_name
           SET blob_column=LOAD_FILE("/tmp/picture")
           WHERE id=1;

while(result.next()) 
{ 

byte[] bytes = result.getBytes(1); 

String jd = String.valueOf(bytes); 
System.out.println(jd); 

//get:[B@153 [B@154 
//or [B@155 [B@156 for the 2.result 

String name = result.getString(2); 
System.out.println(name); 

//get the correct result 

FileOutputStream file = null; 
file = new FileOutputStream ("c:\\"+name+".jpg"); 
file.write(bytes); 

//it works fine !!! 

ImageIcon icon = new ImageIcon(bytes); //it is correct ? 

String ls = String.valueOf(icon.getImageLoadStatus()); 
String h = String.valueOf(icon.getIconHeight()); 
String w = String.valueOf(icon.getIconWidth()); 

System.out.println(h+" : "+w); 
//get: -1 : -1 

System.out.println(ls); 
//get: 4 
*/

%>