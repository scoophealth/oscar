/*
   FOLLOW THE COMMENTS CAREFULY AND DO CHANGES WHERE EVER NECESSARY

This servlet class first checks the integrity of the Form Content transmitted over network
   It also converts the Image strings submitted by client and stores them as files
   The server side integrisign API's is used to convert the raw Signature srtring to different image formats
   refer to API Documentation of  SaveImage class
*/
package org.oscarehr.PMmodule.consent;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class ProcessIntegrisign extends HttpServlet {
   public void init (ServletConfig sc) throws ServletException{
      //initialization
      super.init(sc);
   }
   public void doPost(HttpServletRequest hreq, HttpServletResponse hres)throws IOException{
/*
    //Get the parameters submited by HTML
    String userid,name,signstr,bmpstr,gifstr,jpegstr,hashstr,crdno;
    int bmplen,giflen,jpeglen,pnglen,siglen;
    signstr=null;
    bmpstr=null;
    gifstr=null;
    jpegstr=null;
    hashstr=null;
    crdno=null;
    userid = hreq.getParameter("uid");
    name = hreq.getParameter("uname");
    crdno = hreq.getParameter("crdno");
    signstr = hreq.getParameter("signstr");
    bmpstr = hreq.getParameter("bmpstr");
    gifstr=hreq.getParameter("gifstr");
    jpegstr=hreq.getParameter("jpegstr");
    hres.setContentType("text/html");
    PrintWriter out=hres.getWriter();

    //Initialize the hashstr with the same form field values which are used in the original hash
    hashstr=userid+name+crdno;
    //First check the Integrity of the Form Data Submitted
    //Create the Instance of VerifyIntegrity class
    VerifyIntegrity vi = new VerifyIntegrity();
    System.out.println(hashstr);
    int retstatus =vi.verifyDocument(signstr,hashstr);
	siglen = signstr.length();

    //convert the bmp String into byte[] using base64format
    Base64Format b64f= new Base64Format();
    byte[] bmparray = b64f.decode64(bmpstr);
    bmplen=bmparray.length;
    //convert the gif String into byte[] using base64format
    byte[] gifarray = b64f.decode64(gifstr);
    giflen=gifarray.length;
    //convert the jpeg string into byte[] sing Base64Format class
    byte[] jpegarray = b64f.decode64(jpegstr);
    jpeglen=jpegarray.length;

    try {

        // The following file path should be according to your webserver directory structure
        FileOutputStream fos1 = new FileOutputStream("c:/"+userid+".bmp");
        fos1.write(bmparray);
        fos1.close();
    } catch(Exception exec) {
        exec.printStackTrace();
    }

    try {
        // The following file path should be according to your webserver directory structure
        FileOutputStream fos = new FileOutputStream(new File("public_html/"+userid+".gif"));
        fos.write(gifarray);
        fos.close();
    } catch(Exception ex) {
        ex.printStackTrace();
    }

    try {
        // The following file path should be according to your webserver directory structure
        FileOutputStream fos2 = new FileOutputStream(new File("public_html/"+userid+".jpeg"));
        fos2.write(jpegarray);
        fos2.close();
    } catch(Exception ex) {
        ex.printStackTrace();
    }

	  //create the Images from the raw signature data on the server end using integrisign libraries.
	  SaveImage si = new SaveImage();
	  byte[] servgifbytes=si.getGifImageBytes(signstr,125, 65);
	  byte[] servjpegbytes=si.getJpegImageBytes(signstr, 125, 65,100);
	try {

	// The following file path should ve according to your webserver directory structure
	FileOutputStream fos3 = new FileOutputStream(new File("public_html/"+userid+".gif"));
	fos3.write(servgifbytes);
	fos3.close();
	} catch(Exception exec) {
		exec.printStackTrace();
	}

	try {
		// The following file path should ve according to your webserver directory structure
	   FileOutputStream fos4 = new FileOutputStream(new File("public_html/"+userid+".jpeg"));
	   fos4.write(servjpegbytes);
	   fos4.close();
	} catch(Exception ex) {
		ex.printStackTrace();
	}

        String res="";
        switch(retstatus) {
            case 0:
                res = "Contents have not been modified.";
            break;
            case 1:
                res = "Contents have  been modified.";
            break;
            case 2:
                res = "Hash generated with different version";
            break;
            case 3:
                res = "Photographing information is not stored";
            break;
            case 4:
                res = "No Signature String available";
        }

        //CHANGE THE URL'S IN THE FOLLOWING HTML RESPONSE WHERE EVER NECESSARY OR COMMENTS INDICATES A CHANGE

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Interlink Electronics Authentication Result</title>");
        out.println("<script language=javascript>");
        out.println("function displayImage(x) {");
        out.println("window.open(x,'integrisign','width=200,height=100,status=no,toolbar=no,scroll=no,top=200,left=300')");
        out.println("}");
        out.println("</script>");
        out.println("</head>");
        out.println("<body bgcolor= #FFFFFF  topmargin=0 leftmargin=0 >");
        out.println("<table border=0  width= 100%  bgcolor= #336699  height= 3% >");
        out.println("<tr>");
        out.println("<td width= 39% >");
        out.println("<b><font color= #FFFFFF  face= Century Gothic  size= 4 >Interlink Electronics.</font></b></td>");
        out.println("<td width= 61%  align= left  valign= top >");
        out.println("<b><font color= #FFFFFF  size= 2  face= Arial >Content Integrity</font></b></td>");
        out.println("</tr>");
        out.println("</table>");
        out.println("<br>");
        out.println("<table border= 0  width= 80%  cellspacing= 0  cellpadding= 0 >");
        out.println("<tr>");
        out.println("<td width= 60% ><b><font color= #336699 >Content integrity check result is:");
        out.println("&nbsp;</font></b></td>");
        out.println("<td width= 40% ><b><font color= green >"+res+"</font></b></td>");
        out.println("</tr>");
        out.println("</table>");
        out.println("<br>");
        out.println("<table border= 0  width= 100%  bgcolor= #336699 >");
        out.println("<tr>");
        out.println("<td width= 100% ><font color= #FFFFFF >Biometric record Information</font></td>");
        out.println("</tr>");
        out.println("</table>");
        out.println("<br>");
        out.println("<table border= 0  width= 100%  cellspacing= 0  cellpadding= 0 >");
        out.println("<tr>");
        out.println("<td width= 20% ><font color= #FF0000 ><b>Biometric Record Size:</b></font></td>");
        out.println("<td width= 40% ><font color= #FF0000 ><b>Biometric Record</b></font></td>");
        out.println("<td width= 40% ><font color= #FF0000 ><b>Remarks</b></font></td>");
        out.println("</tr>");
        out.println("<tr>");

        out.println("<td width= 23% valign=top align=center><font color= #336699 >"+siglen+" bytes</font></td>");
        out.println("<td width= 50% valign=top><font color= #336699>");
        out.println("<form>");
        out.println("<textarea rows=10 name=S1 cols=38>"+signstr);
        out.println("</textarea>");
        out.println("</form>");
        out.println("</font></td>");
        out.println("<td width= 27% valign=top><font color= #336699 >This encrypted raw signature information  in the form");
        out.println("Base64String can be stored in any database or file system. This base64 data  is easily transportable");
        out.println("through HTTP/HTTPS protocols. Standard image file (BMP,GIF,JPEG) files can be generated from the raw data");
        out.println("at the client as well as at server. ");
        out.println("</font></td>");
        out.println("</tr>");
        out.println("</table>");
        out.println("<br>");
        out.println("<table border= 0  width= 100%  bgcolor= #336699 >");
        out.println("<tr>");
        out.println("<td width= 100% ><font color= #FFFFFF >Image Information</font></td>");
        out.println("</tr>");
        out.println("</table>");
        out.println("<br>");
        out.println("<table border= 0  width= 75% >");
        out.println("<tr>");
        out.println("<td width= 33% ><b><font color= #FF0000 >Image Format</font></b></td>");
        out.println("<td width= 33% ><font color= #FF0000 ><b>Image Size (bytes)</b></font></td>");
        out.println("<td width= 34% ><font color= #FF0000 ><b>Image</b></font></td>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td width= 33% ><font color= #336699 >BMP</font></td>");
        out.println("<td width= 33% ><font color= #336699 >"+bmplen+" bytes</font></td>");


        //THE FOLLOWING PATH SHOULD POINT WHERE THE BMP IMAGE FILES IS STORED


        out.println("<td width= 34% ><a href=JavaScript:onClick=displayImage('http://localhost:8080/"+userid+".bmp')>Click Here to view BMP. </a></td><br>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td width= 33% ><font color= #336699 >GIF</font></td>");
        out.println("<td width= 33% ><font color= #336699 >"+giflen+" bytes</font></td>");


        //THE FOLLOWING PATH SHOULD POINT WHERE THE GIF IMAGE FILES IS STORED



        out.println("<td width= 34% ><a href=JavaScript:onClick=displayImage('http://localhost:8080/"+userid+".gif')>Click Here to view GIF</a></td><br>");
        out.println("</tr>");
        out.println("<tr>");
        out.println("<td width= 33% ><font color= #336699 >JPEG</font></td>");


        //THE FOLLOWING PATH SHOULD POINT WHERE THE JPEG IMAGE FILES IS STORED



        out.println("<td width= 33% ><font color= #336699 >"+jpeglen+" bytes</font></td>");

        out.println("<td width= 34% ><a href=JavaScript:onClick=displayImage('http://localhost:8080/"+userid+".jpeg')>Click Here to view JPEG</a></td><br>");

        out.println("</tr>");


        out.println("</table>");
        out.println("<br>");
        out.println("<br>");
        out.println("<table border= 0  width= 100%  bgcolor= #336699  cellspacing= 0  cellpadding= 0  height= 4 >");
        out.println("<tr>");
        out.println("<td width= 100% >");
        out.println("<p align= center ><font color= #FFFFFF ><b>Interlink Electronics. Copyright�</b></font></td>");
        out.println("</tr>");
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
*/
   }

}