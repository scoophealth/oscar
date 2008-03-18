// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class UploadImage extends GenericServlet
{

    static final int BUFFER = 2048;

    public UploadImage()
    {
    }

    public void service(ServletRequest servletrequest, ServletResponse servletresponse)
        throws IOException, ServletException
    {
        byte abyte0[] = new byte[2048];
        byte abyte1[] = new byte[1024];
        byte abyte2[] = new byte[1024];
        byte abyte3[] = new byte[2];
        int i = servletrequest.getContentType().indexOf(61);
        String s = servletrequest.getContentType().substring(i + 1);
// this is the root folder
        String s1 = "../webapps/oscar_sfhc/e_form/images/";
        String s2 = "";
        ServletInputStream servletinputstream = servletrequest.getInputStream();
        BufferedOutputStream bufferedoutputstream = null;
        Object obj = null;
      //  System.out.println("temp :" + s);
        boolean flag = false;
        boolean flag1 = true;
        boolean flag2 = false;
        boolean flag3 = false;
        byte abyte4[] = s.getBytes();
        while(flag3 || (i = servletinputstream.readLine(abyte0, 0, 2048)) != -1) 
        {
            flag3 = false;
            boolean flag4 = false;
           // System.out.println("S=: " + new String(abyte0, 0, i) + flag4);
            if(i == 2 && abyte0[0] == 13 && abyte0[1] == 10)
            {
                abyte3[0] = 13;
                abyte3[1] = 10;
               // System.out.println("Space=: " + new String(abyte0, 0, i));
                for(int j = 0; j < 2048; j++)
                    abyte0[j] = 0;

                i = servletinputstream.readLine(abyte0, 0, 2048);
                if(i == 2 && abyte0[0] == 13 && abyte0[1] == 10)
                {
                    bufferedoutputstream.write(abyte3, 0, 2);
                    flag3 = true;
                    continue;
                }
                flag4 = true;
               // System.out.println("Sin=: " + new String(abyte0, 0, i) + flag4);
            }
            String s3 = new String(abyte0, 2, s.length());
            if(s.equals(s3))
            {
               // System.out.println("boundery" + i + " :: " + new String(abyte0, 0, i) + flag4);
                if(flag4)
                    break;
                int k;
                if((k = servletinputstream.readLine(abyte1, 0, 2048)) != -1)
                {
                    String s4 = new String(abyte1);
                    if(s4.length() > 2 && s4.indexOf("filename") != -1)
                    {
// this is the saving file name, I can use it' original name, or  I can use a temp name for all
                         s4 = s1 + s4.substring(s4.lastIndexOf(92) + 1, s4.lastIndexOf(34));
                    //s4 = s1 + "temp.jsp";
                        FileOutputStream fileoutputstream = new FileOutputStream(s4);
                        bufferedoutputstream = new BufferedOutputStream(fileoutputstream, 2048);
                    }
                    int k1 = servletinputstream.readLine(abyte2, 0, 2048);
                    if((k1 = servletinputstream.readLine(abyte2, 0, 2048)) != -1)
                        flag = flag1;
                }
                flag1 = !flag1;
                for(int i1 = 0; i1 < 2048; i1++)
                    abyte0[i1] = 0;

            }
            else
            {
                if(flag4)
                {
                    boolean flag5 = false;
                    bufferedoutputstream.write(abyte3, 0, 2);
                  //  System.out.println("write space: " + new String(abyte3, 0, 2));
                    for(int j1 = 0; j1 < 2; j1++)
                        abyte3[j1] = 0;

                }
                if(flag)
                {
                    bufferedoutputstream.write(abyte0, 0, i);
                   // System.out.println("write second: " + new String(abyte0, 0, i));
                    for(int l = 0; l < 2048; l++)
                        abyte0[l] = 0;

                }
            }
        }

        bufferedoutputstream.close();
        servletinputstream.close();

// send back message to user
	   PrintWriter out = servletresponse.getWriter();
	   out.println("<head>");
	   out.println("<script language=\"JavaScript\">");
	   out.println("setTimeout(\"top.location.href = '../e_form/UploadImages.jsp'\",1000);");
	   out.println("</script>");
	   out.println("</head>");
	   out.println("<body>");
	   out.println("File upload successfully.<br> Please wait for 1 seconds to go to \"modify\" page"); 
	   out.println("</body>");
    }
}
