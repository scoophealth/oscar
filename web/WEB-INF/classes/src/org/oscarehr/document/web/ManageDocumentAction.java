/*
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. 
 *
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.document.web;

import java.nio.ByteBuffer;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.document.dao.DocumentDAO;
import org.oscarehr.document.model.CtlDocument;
import org.oscarehr.document.model.Document;
import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.util.UtilDateUtilities;
import org.oscarehr.common.dao.ProviderInboxRoutingDao;

/**
 *
 * @author jaygallagher
 */
public class ManageDocumentAction extends DispatchAction {
    
    private static Log log = LogFactory.getLog(ManageDocumentAction.class);

    private DocumentDAO documentDAO = null;
    private ProviderInboxRoutingDao  providerInboxRoutingDAO = null;
    

    public void setDocumentDAO(DocumentDAO documentDAO) {
        this.documentDAO = documentDAO;
    }
    
    public void setProviderInboxRoutingDAO(ProviderInboxRoutingDao providerInboxRoutingDAO){
        this.providerInboxRoutingDAO = providerInboxRoutingDAO;
    }

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        //System.out.println("AddEditDocumentAction - unspecified");
        return null;//execute2(mapping, form, request, response);
    }

    //public ActionForward multifast(
    //public ActionForward documentUpdate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    public ActionForward documentUpdate(org.apache.struts.action.ActionMapping mapping, org.apache.struts.action.ActionForm form, javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) {
        //System.out.println("In here DocumentUpdate");
        String ret = "";


        String observationDate = request.getParameter("observationDate");// :2008-08-22<
        String documentDescription = request.getParameter("documentDescription");//:test2<
        String documentId = request.getParameter("documentId");//:29<
        String docType = request.getParameter("docType");//:consult<
        
        LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ADD, LogConst.CON_DOCUMENT, documentId, request.getRemoteAddr());
        
        String demog = request.getParameter("demog");// :22<
        String demographicKeyword = request.getParameter("demographicKeyword");// :ABLE, ALEX<
        String[] flagproviders = request.getParameterValues("flagproviders");//:999998<
        System.out.println("DOCUMNET " + documentDAO);
        System.out.println("link to prov "+request.getParameter("demoLink")+" demo "+demog);
        //DONT COPY THIS !!!
         if (flagproviders !=null && flagproviders.length > 0){ //TODO: THIS NEEDS TO RUN THRU THE  lab forwarding rules!
             
            for(String proNo:flagproviders){ 
               providerInboxRoutingDAO.addToProviderInbox(proNo, documentId, "DOC"); 
               //try{ 
               //String docLab = "insert into providerLabRouting (provider_no,lab_no,status,timestamp,lab_type)  values ('"+proNo+"','"+documentId+"','N',now(),\"DOC\")";
               //DBHandler dbh = new DBHandler(DBHandler.OSCAR_DATA);
               //dbh.RunSQL(docLab);
               //}catch(Exception ee ){ee.printStackTrace();}
            }
        }
        //// END COPY  
        

        Document d = documentDAO.getDocument(documentId);

        //System.out.println("aaa " + d);

        d.setDocdesc(documentDescription);
        d.setDoctype(docType);
        Date obDate = UtilDateUtilities.StringToDate(observationDate);
        //System.out.println("Date util " + obDate);
        if (obDate != null) {
            d.setObservationdate(obDate);
        }

        
        


        //System.out.println("bbb " + d);
        documentDAO.save(d);
        //System.out.println("Document " + d.getDocfilename() + " desc " + d.getDocdesc());

        try {
            //System.out.println("parse Int " + Integer.parseInt(demog));


            CtlDocument ctlDocument = documentDAO.getCtrlDocument(Integer.parseInt(documentId));
            //System.out.println("CtlDocument1 " + ctlDocument.getModuleId());
            ctlDocument.setModuleId(Integer.parseInt(demog));

            //System.out.println("CtlDocument1 " + ctlDocument.getModuleId());

            //System.out.println("1" + ctlDocument.toString());
            documentDAO.saveCtlDocument(ctlDocument);
            //System.out.println("2" + ctlDocument.toString());
        //ret = "{\"success\": \"yes\", \"docId\" : \""+documentId+"\"}";
        } catch (Exception e) {
            e.printStackTrace();
        }





        if (flagproviders != null) {
            for (String str : flagproviders) {
                //System.out.println("str " + str);
            }
        }
        if (ret != null && !ret.equals("")) {
            //response.getOutputStream().print(ret);
        }
        return null;//execute2(mapping, form, request, response);
    }
    private File getDocumentCacheDir(String docdownload){
        //File cacheDir = new File(docdownload+"_cache");
        File docDir = new File(docdownload);
        String documentDirName = docDir.getName();
        File parentDir = docDir.getParentFile();
        
        File cacheDir = new File(parentDir,documentDirName+"_cache");
        
        if(!cacheDir.exists()){
            cacheDir.mkdir();
        }
        return cacheDir;
    }
    
    private File hasCacheVersion(Document d){
        File documentCacheDir = getDocumentCacheDir(oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR"));
        File outfile = new File(documentCacheDir,d.getDocfilename()+".png");
        if (!outfile.exists()){
            outfile = null;
        }
        return outfile;
    }
    
    public File createCacheVersion(Document d) throws Exception{
        
        String docdownload = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
        File documentDir = new File(docdownload);
        File documentCacheDir = getDocumentCacheDir(docdownload);
        log.debug("Document Dir is a dir"+documentDir.isDirectory());
        
        File file = new File(documentDir,d.getDocfilename());

        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        PDFFile pdffile = new PDFFile(buf);
        //long readfile = System.currentTimeMillis() - start;
        // draw the first page to an image
        PDFPage ppage = pdffile.getPage(0);

        log.debug("WIDTH " + (int) ppage.getBBox().getWidth() + " height " + (int) ppage.getBBox().getHeight());

        //get the width and height for the doc at the default zoom 
        Rectangle rect = new Rectangle(0, 0,
                (int) ppage.getBBox().getWidth(),
                (int) ppage.getBBox().getHeight());

        log.debug("generate the image");
        Image img = ppage.getImage(
                rect.width, rect.height, //width & height
                rect, // clip rect
                null, // null for the ImageObserver
                true, // fill background with white
                true // block until drawing is done
                );
       
        log.debug("about to Print to stream");
        File outfile = new File(documentCacheDir,d.getDocfilename()+".png");
        OutputStream outs = new FileOutputStream(outfile);
  
        RenderedImage rendImage = (RenderedImage) img;
        ImageIO.write(rendImage, "png", outs);
        outs.flush();
        outs.close(); 
        return outfile;
        
    }
    //PNG version
    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
        String doc_no = request.getParameter("doc_no");
           log.debug("Document No :"+doc_no);
                   
        LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.READ, LogConst.CON_DOCUMENT, doc_no, request.getRemoteAddr());
  
        Document d = documentDAO.getDocument(doc_no);
        log.debug("Document Name :"+d.getDocfilename());
        
        File outfile = hasCacheVersion(d);
        if (outfile == null){
            System.out.println("No Cache Version");
           outfile = createCacheVersion( d);
           //System.out.println("outfile after create cache version "+outfile);
        }else{
            System.out.println("THERE WAS A CACHE Version "+outfile);
        }
        
        response.setContentType("image/png");
        //response.setHeader("Content-Disposition", "attachment;filename=\"" + filename+ "\"");
        //read the file name.
        

        log.debug("about to Print to stream");
        ServletOutputStream outs = response.getOutputStream();
 
        
        response.setHeader("Content-Disposition", "attachment;filename=" + d.getDocfilename());
        BufferedInputStream bfis = new BufferedInputStream(new FileInputStream(outfile));
        int data;
        while ((data = bfis.read()) != -1) {
            outs.write(data);
            outs.flush();
        }
		
        bfis.close();
        outs.flush();
        outs.close();     
        return null;
    }

    public ActionForward view2(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
        //TODO: NEED TO CHECK FOR ACCESS
        String doc_no = request.getParameter("doc_no");
           log.debug("Document No :"+doc_no);
                   
        LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.READ, LogConst.CON_DOCUMENT, doc_no, request.getRemoteAddr());
  
        String docdownload = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
        File documentDir = new File(docdownload);
        log.debug("Document Dir is a dir"+documentDir.isDirectory());
        
        Document d = documentDAO.getDocument(doc_no);
        log.debug("Document Name :"+d.getDocfilename());
        
        //TODO: Right now this assumes it's a pdf which it shouldn't
        
        response.setContentType("image/png");
        //response.setHeader("Content-Disposition", "attachment;filename=\"" + filename+ "\"");
        //read the file name.
        File file = new File(documentDir,d.getDocfilename());

        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        PDFFile pdffile = new PDFFile(buf);
        //long readfile = System.currentTimeMillis() - start;
        // draw the first page to an image
        PDFPage ppage = pdffile.getPage(0);

        log.debug("WIDTH " + (int) ppage.getBBox().getWidth() + " height " + (int) ppage.getBBox().getHeight());

        //get the width and height for the doc at the default zoom 
        Rectangle rect = new Rectangle(0, 0,
                (int) ppage.getBBox().getWidth(),
                (int) ppage.getBBox().getHeight());

        log.debug("generate the image");
        Image img = ppage.getImage(
                rect.width, rect.height, //width & height
                rect, // clip rect
                null, // null for the ImageObserver
                true, // fill background with white
                true // block until drawing is done
                );
       
        log.debug("about to Print to stream");
        ServletOutputStream outs = response.getOutputStream();
  
        RenderedImage rendImage = (RenderedImage) img;
        ImageIO.write(rendImage, "png", outs);
        outs.flush();
        outs.close();     
        return null;
    }


    
        public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
        
        String doc_no = request.getParameter("doc_no");
           log.debug("Document No :"+doc_no);
           
        CtlDocument ctld = documentDAO.getCtrlDocument(Integer.parseInt(doc_no));
        if(ctld.isDemographicDocument()){
            LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.READ, LogConst.CON_DOCUMENT, doc_no, request.getRemoteAddr(),""+ctld.getModuleId());
        }else{           
            LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.READ, LogConst.CON_DOCUMENT, doc_no, request.getRemoteAddr());
        }
        
        String docdownload = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
        File documentDir = new File(docdownload);
        log.debug("Document Dir is a dir"+documentDir.isDirectory());
        
        Document d = documentDAO.getDocument(doc_no); 
        log.debug("Document Name :"+d.getDocfilename());
        String docxml = d.getDocxml();
        
        String contentType = d.getContenttype();
        if (docxml != null && !docxml.trim().equals("")){
            ServletOutputStream outs = response.getOutputStream();
            outs.write(docxml.getBytes());
            outs.flush();
            outs.close();
            return null;
        }
        
        //TODO: Right now this assumes it's a pdf which it shouldn't
        if (contentType == null){
            contentType = "application/pdf";
        }
        //System.out.println("Content type was set tooo "+contentType);
        File file = new File(documentDir,d.getDocfilename());
        response.setContentType(contentType);
        response.setContentLength((int)file.length());
        //response.setHeader("Content-Disposition", "attachment;filename=\"" + filename+ "\"");
        //read the file name.
        //InputFileStream is =
        //response.setHeader("Content-disposition","inline; filename=" + d.getDocfilename());
        response.setHeader("Content-Disposition", "inline; filename=" + d.getDocfilename());
        log.debug("about to Print to stream");
        ServletOutputStream outs = response.getOutputStream();
 
      
        BufferedInputStream bfis = new BufferedInputStream(new FileInputStream(file));

        byte[] buffer = new byte[1024];
        int bytesRead = 0;

        while ((bytesRead = bfis.read(buffer)) != -1) {
            outs.write(buffer, 0, bytesRead);
            //outs.flush();
        }
	
        bfis.close();
        
        outs.flush();
        outs.close();
        return null;
    }



}
