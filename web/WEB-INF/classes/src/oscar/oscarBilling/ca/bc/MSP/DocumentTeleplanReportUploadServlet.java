package oscar.oscarBilling.ca.bc.MSP;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.zip.*;
import oscar.DocumentBean;

public class DocumentTeleplanReportUploadServlet extends HttpServlet{
    final static int BUFFER = 2048;
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {
        int c;
        int count;
        byte data[] = new byte[BUFFER];
        byte data1[] = new byte[BUFFER/2];
        byte data2[] = new byte[BUFFER/2];
        byte enddata[] = new byte[2];
        
        
        HttpSession session = request.getSession(true);
        String backupfilepath = ((String) session.getAttribute("homepath"))!=null?((String) session.getAttribute("homepath")):"null" ;
        
        count=request.getContentType().indexOf('=');
        String temp = request.getContentType().substring(count+1);
        String filename = "test.txt", fileoldname="", foldername="", fileheader="", forwardTo="", function="", function_id="", filedesc="", creator="", doctype="", docxml="";
        String home_dir="", doc_forward="";
        String userHomePath = System.getProperty("user.home", "user.dir");
        System.out.println(userHomePath);
        
        File pFile = new File(userHomePath, backupfilepath+".properties");
        
        
        //   File pFile = new File(userHomePath, "oscar_sfhc.properties");
        FileInputStream pStream = new FileInputStream(pFile.getPath());
        
        Properties ap = new Properties();
        ap.load(pStream);
        
        forwardTo  = ap.getProperty("TA_FORWARD");
        foldername = ap.getProperty("DOCUMENT_DIR");
        pStream.close();
        
        
        // function = request.getParameter("function");
        // function_id = request.getParameter("functionid");
        // filedesc = request.getParameter("filedesc");
        // creator = request.getParameter("creator");
        
        ServletInputStream sis = request.getInputStream();
        BufferedOutputStream dest = null;
        FileOutputStream fos = null;
        boolean bwri = false;
        boolean bfbo = true;
        boolean benddata = false;
        boolean bf = false;
        byte boundary[] = temp.getBytes();
        
        while (bf?true:((count = sis.readLine(data, 0, BUFFER)) != -1)) {
            bf = false;
            benddata = false;
            if(count==2 && data[0]==13 && data[1]==10) {
                enddata[0] = 13;
                enddata[1] = 10;
                for(int i=0;i<BUFFER;i++) data[i]=0;
                
                count = sis.readLine(data, 0, BUFFER);
                if(count==2 && data[0]==13 && data[1]==10) {
                    dest.write(enddata, 0, 2);
                    bf = true;
                    continue;
                } else {
                    benddata = true;
                }
            }
            String s = new String(data,2,temp.length());
            if(temp.equals(s)) {
                if(benddata) break;
                if((c =sis.readLine(data1, 0, BUFFER)) != -1) {
                    filename = new String(data1);
                    if(filename.length()>2 && filename.indexOf("filename")!=-1) {
                        filename = filename.substring(filename.lastIndexOf('\\')+1,filename.lastIndexOf('\"'));
                        //System.out.println("filename: "+filename);
                        fileheader = filename;
                        fos = new FileOutputStream(foldername+ filename);
                        dest = new BufferedOutputStream(fos, BUFFER);
                    }
                    c =sis.readLine(data2, 0, BUFFER);
                    if((c =sis.readLine(data2, 0, BUFFER)) != -1) {
                        bwri = bfbo?true:false;
                    }
                }
                bfbo = bfbo?false:true;
                for(int i=0;i<BUFFER;i++) data[i]=0;
                continue;
            } //end period
            
            if(benddata) {
                benddata = false;
                dest.write(enddata, 0, 2);
                for(int i=0;i<2;i++) enddata[i]=0;
            }
            if(bwri) {
                dest.write(data, 0, count);
                for(int i=0;i<BUFFER;i++) data[i]=0;
            }
        } //end while
        //dest.flush();
        dest.close();
        sis.close();
        
        
        DocumentBean documentBean = new DocumentBean();
        
        request.setAttribute("documentBean", documentBean);
        
        
        
        documentBean.setFilename(fileheader);
        
        //  documentBean.setFileDesc(filedesc);
        
        //  documentBean.setFoldername(foldername);
        
        //  documentBean.setFunction(function);
        
        //  documentBean.setFunctionID(function_id);
        
        //  documentBean.setCreateDate(fileheader);
        
        //  documentBean.setDocCreator(creator);
        
        
        
        
        
        // Call the output page.
        
        RequestDispatcher dispatch = getServletContext().getRequestDispatcher(forwardTo);
        dispatch.forward(request, response);
    }
    
}
