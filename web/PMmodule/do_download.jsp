<%@ page contentType="text/html; charset=iso-8859-1"%> 
<%@ page import="java.io.*" %>

<% 
    String root = getServletContext().getRealPath(File.separator) + File.separator;
    String name = "IntakeCReport1.csv";

    response.setContentType("unknown");
    response.addHeader("Content-Disposition", "filename=\"" + name + "\"");

    try {
        OutputStream os = response.getOutputStream();
        FileInputStream fis = new FileInputStream(root + name);

        byte[] b = new byte[1024];
        int i = 0;

        while ((i = fis.read(b)) > 0)  {
        	os.write(b, 0, i);
        }

        fis.close();
        os.flush();
        os.close();
    }
    catch ( Exception e ) {
        System.out.println(e);
    }
%>



