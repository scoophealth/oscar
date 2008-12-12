<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ page
	import="java.util.*,
java.io.*,
oscar.oscarLab.ca.all.util.KeyPairGen"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<% 
String name = request.getParameter("name");
String type = request.getParameter("type");
if (type != null && type.equals("OTHER"))
    type = request.getParameter("otherType");

String message = "Key pair created successfully";
String error = "false";

if (name.equals("oscar")){
    KeyPairGen k = new KeyPairGen();
    if(k.checkName(name)){
        message = "Failed: The oscar key pair has already been created";
        error = "true";
    }else if(k.createKeys(name, null).equals("success")){
        message = "Oscar key pair created successfully";
    }else{
        message = "Failed: Could not create the oscar key pair";
        error = "true";
    }
}else{
    
    KeyPairGen k = new KeyPairGen();
    if(k.checkName(name)){
        message = "Failed: Key pair has already been created for the service '"+name+"'";
        error = "true";
    }else{
        String clientKey = k.createKeys(name, type);
        String oscarKey = k.getPublic();
        
        if (clientKey == null){
            message = "Failed: Could not create key pair";
            error = "true";
        }else if(oscarKey == null){
            message = "Failed: Could not retrieve public key from oscar";
            error = "true";
        }else{
            
            String keyPairOut = "-------- Service Name --------\n"+name+"\n------------------------------\n"+
                    "----- Client Private Key -----\n"+clientKey+"\n------------------------------\n"+
                    "------ Oscar Public Key ------\n"+oscarKey+"\n------------------------------";
            response.setContentType("text");
            response.setContentLength(keyPairOut.length());
            response.setHeader("Content-Disposition","attachment; filename=keyPair.key");
            ServletOutputStream output = null;
            
            try{
                output = response.getOutputStream();
                output.print(keyPairOut);
                output.flush();
            }catch(IOException e){
                message = "Failed: Could not save key pair";
                error = "true";
            }finally{
                if (output != null){
                    try{
                        output.close();
                    }catch(IOException e){
                        message = "Failed: Could not close output stream";
                        error = "true";
                    }
                }
            }
        }
    }
}

request.setAttribute("message", message);
request.setAttribute("error", error);

//response.sendRedirect("keyGen.jsp?message="+message+"&error="+error);

RequestDispatcher rd = request.getRequestDispatcher("keyGen.jsp");
rd.forward(request, response);

%>
