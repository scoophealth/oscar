/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster Unviersity
 * Hamilton
 * Ontario, Canada
 */
/*
 *
 *
 */
// javac -classpath .;..\lib\itext-1.01.jar -d . FrmPDFServlet.java
// form/createpdf?__title=British+Columbia+Antenatal+Record+Part+1&__cfgfile=bcar1PrintCfgPg1&__cfgfile=bcar1PrintCfgPg2&__template=bcar1
package oscar.form.pdfservlet;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.log4j.Logger;

import oscar.OscarProperties;
import oscar.form.graphic.FrmGraphicFactory;
import oscar.form.graphic.FrmPdfGraphic;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

/**
 *
 *
 */
public class FrmPDFServlet extends HttpServlet {
    
    public static final String HSFO_RX_DATA_KEY = "hsfo.rx.data";
    Logger log = Logger.getLogger(FrmPDFServlet.class);
    /**
     *
     *
     */
    public FrmPDFServlet() {
        super();
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws javax.servlet.ServletException,
            java.io.IOException {
        doPost(req, res);
    }
    
    /**
     * @param req HTTP request object
     * @param resp HTTP response object
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws javax.servlet.ServletException,
            java.io.IOException {
        DocumentException ex = null;
        
        ByteArrayOutputStream baosPDF = null;
        
        try {
            baosPDF = generatePDFDocumentBytes(req, this.getServletContext());
            
            StringBuffer sbFilename = new StringBuffer();
            sbFilename.append("filename_");
            //sbFilename.append(System.currentTimeMillis());
            sbFilename.append(".pdf");
            
            // set the Cache-Control header
            res.setHeader("Cache-Control", "max-age=0");
            //res.setHeader("Cache-Control","no-cache"); //HTTP 1.1
            res.setDateHeader("Expires", 0);
            
            res.setContentType("application/pdf");
            
            // The Content-disposition value will be inline
            
            StringBuffer sbContentDispValue = new StringBuffer();
            sbContentDispValue.append("inline; filename="); //inline - display
            // the pdf file
            // directly rather
            // than open/save
            // selection
            //sbContentDispValue.append("; filename=");
            sbContentDispValue.append(sbFilename);
            
            res.setHeader("Content-disposition", sbContentDispValue.toString());
            
            res.setContentLength(baosPDF.size());
            
            ServletOutputStream sos;
            
            sos = res.getOutputStream();
            
            baosPDF.writeTo(sos);
            
            sos.flush();
        } catch (DocumentException dex) {
            res.setContentType("text/html");
            PrintWriter writer = res.getWriter();
            writer.println("Exception from: " + this.getClass().getName() + " " + dex.getClass().getName() + "<br>");
            writer.println("<pre>");
            dex.printStackTrace(writer);
            writer.println("</pre>");
        } finally {
            if (baosPDF != null) {
                baosPDF.reset();
                //baosPDF.close();
            }
        }
        
    }
    
    // added by vic, hsfo
    private ByteArrayOutputStream generateHsfoRxPDF(HttpServletRequest req){
        HsfoRxDataHolder rx = (HsfoRxDataHolder) req.getSession()
        .getAttribute(HSFO_RX_DATA_KEY);
        
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(rx.getOutlines());
        InputStream is = Thread.currentThread().getContextClassLoader()
        .getResourceAsStream("/oscar/form/prop/Hsfo_Rx.jasper");
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            JasperRunManager.runReportToPdfStream(is, baos, rx.getParams(), ds);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
        return baos;
    }
    
    /**
     * the form txt file has lines in the form:
     *
     * For Checkboxes:
     * ie.  ohip : left, 76, 193, 0, BaseFont.ZAPFDINGBATS, 8, \u2713
     * requestParamName : alignment, Xcoord, Ycoord, 0, font, fontSize, textToPrint[if empty, prints the value of the request param]
     * NOTE: the Xcoord and Ycoord refer to the bottom-left corner of the element
     *
     * For single-line text:
     * ie. patientCity  : left, 242, 261, 0, BaseFont.HELVETICA, 12
     * See checkbox explanation
     *
     * For multi-line text (textarea)
     * ie.  aci : left, 20, 308, 0, BaseFont.HELVETICA, 8, _, 238, 222, 10
     * requestParamName : alignment, bottomLeftXcoord, bottomLeftYcoord, 0, font, fontSize, _, topRightXcoord, topRightYcoord, spacingBtwnLines
     *
     *NOTE: When working on these forms in linux, it helps to load the PDF file into gimp, switch to pt. coordinate system and use the mouse to find the coordinates.
     *Prepare to be bored!
     */
    protected ByteArrayOutputStream generatePDFDocumentBytes(final HttpServletRequest req, final ServletContext ctx)
    throws DocumentException, java.io.IOException {
        // added by vic, hsfo
        if (HSFO_RX_DATA_KEY.equals(req.getParameter("__title")))
            return generateHsfoRxPDF(req);
        
        final String PAGESIZE = "printPageSize";
        Document document = new Document();
        //document = new Document(psize, 50, 50, 50, 50);
        
        ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
        PdfWriter writer = null;
        
        try {
            writer = PdfWriter.getInstance(document, baosPDF);
            
            String title = req.getParameter("__title") != null ? req.getParameter("__title") : "Unknown";
            String[] cfgFile = req.getParameterValues("__cfgfile");
            String template = req.getParameter("__template") != null ? req.getParameter("__template") + ".pdf" : "";
            
            int cfgFileNo = 0;
            Properties[] printCfg = null;
            if (cfgFile != null) {
                cfgFileNo = cfgFile.length;
                printCfg = new Properties[cfgFileNo];
                for (int i = 0; i < cfgFileNo; i++) {
                    cfgFile[i] += ".txt";
                    if (cfgFile[i].indexOf("/") > 0)
                        cfgFile[i] = "";
                    printCfg[i] = getCfgProp(cfgFile[i]);
                }
            }
            
            //specify the page of the picture using __graphicPage, it may be used multiple times to specify multiple pages
            //however the same graphic will be applied to all pages
            //ie. __graphicPage=2&__graphicPage=3
            String[] cfgGraphicFile = req.getParameterValues("__cfgGraphicFile");
            int cfgGraphicFileNo = cfgGraphicFile == null ? 0 : cfgGraphicFile.length;

            String[] graphicPage = req.getParameterValues("__graphicPage");
            ArrayList graphicPageArray = new ArrayList();
            if (graphicPage != null){
                graphicPageArray = new ArrayList(Arrays.asList(graphicPage));
            }    
            
            Properties[] graphicCfg = null;
            if (cfgGraphicFileNo > 0) {
                graphicCfg = new Properties[cfgGraphicFileNo];
                for (int i = 0; i < cfgGraphicFileNo; i++) {
                    cfgGraphicFile[i] += ".txt";
                    graphicCfg[i] = getCfgProp(cfgGraphicFile[i]);
                }
            }
            
            String[] cfgVal = null;
            StringBuffer tempName = null;
            String tempValue = null;
            
            // get the print prop values
            Properties props = new Properties();
            StringBuffer temp = new StringBuffer("");
            for (Enumeration e = req.getParameterNames(); e.hasMoreElements();) {
                temp = new StringBuffer(e.nextElement().toString());
                props.setProperty(temp.toString(), req.getParameter(temp.toString()));
            }
            
            for (Enumeration e = req.getAttributeNames(); e.hasMoreElements(); ) {
                temp = new StringBuffer(e.nextElement().toString());
                props.setProperty(temp.toString(), req.getAttribute(temp.toString()).toString());
            }
            
            
            document.addTitle(title);
            document.addSubject("");
            document.addKeywords("pdf, itext");
            document.addCreator("OSCAR");
            document.addAuthor("");
            document.addHeader("Expires", "0");
            
            // A0-A10, LEGAL, LETTER, HALFLETTER, _11x17, LEDGER, NOTE, B0-B5, ARCH_A-ARCH_E, FLSA
            // and FLSE
            // the following shows a temp way to get a print page size
            Rectangle pageSize = PageSize.LETTER;
            if ("PageSize.HALFLETTER".equals(props.getProperty(PAGESIZE)))
                pageSize = PageSize.HALFLETTER;
            if ("PageSize.A6".equals(props.getProperty(PAGESIZE)))
                pageSize = PageSize.A6;
            document.setPageSize(pageSize);
            document.open();
            
            // create a reader for a certain document
            //String propFilename = "../../OscarDocument/" + getProjectName() + "/form/" + template;
            String propFilename = oscar.OscarProperties.getInstance().getProperty("pdfFORMDIR", "") + "/" + template;
            PdfReader reader = null;
            try {
                reader = new PdfReader(propFilename);
                log.info("Found template at " + propFilename);
            } catch (Exception dex) {
                log.debug("change path to inside oscar from :" + propFilename);
                reader = new PdfReader("/oscar/form/prop/" + template);
                log.debug("Found template at /oscar/form/prop/" + template);
            }
            
            // retrieve the total number of pages
            int n = reader.getNumberOfPages();
            // retrieve the size of the first page
            Rectangle pSize = reader.getPageSize(1);
            float width = pSize.width();
            float height = pSize.height();
            
            PdfContentByte cb = writer.getDirectContent();
            ColumnText ct = new ColumnText(cb);
            int i = 0;
            int p = 0;
            int fontFlags = 0;
            
            while (i < n) {
                document.newPage();
                p++;
                i++;
                PdfImportedPage page1 = writer.getImportedPage(reader, i);
                cb.addTemplate(page1, 1, 0, 0, 1, 0, 0);
                //System.err.println(cfgFileNo + "processed page " + i);
                
                BaseFont bf; // = normFont;
                String encoding;
                
                cb.setRGBColorStroke(0, 0, 255);
                //cb.setFontAndSize(bf, 8);
                // LEFT/CENTER/RIGHT, X, Y,
                //cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cathy
                // Pacific", 126, height-50, 0);
                
                if (i > (cfgFileNo))
                    continue;
                
                String[] fontType;
                for (Enumeration e = printCfg[i - 1].propertyNames(); e.hasMoreElements();) {
                    tempName = new StringBuffer(e.nextElement().toString());
                    cfgVal = printCfg[i - 1].getProperty(tempName.toString()).split(" *, *");
                    
                    if( cfgVal[4].indexOf(";") > -1 ) {
                        fontType = cfgVal[4].split(";");
                        if( fontType[1].trim().equals("italic") )
                            fontFlags = Font.ITALIC;
                        else if( fontType[1].trim().equals("bold") )
                            fontFlags = Font.BOLD;
                        else if( fontType[1].trim().equals("bolditalic") )
                            fontFlags = Font.BOLDITALIC;
                        else
                            fontFlags = Font.NORMAL;
                    } else {
                        fontFlags = Font.NORMAL;
                        fontType = new String[] { cfgVal[4].trim() };
                    }
                    
                    if(fontType[0].trim().equals("BaseFont.HELVETICA")) {
                        fontType[0] = BaseFont.HELVETICA;
                        encoding = BaseFont.CP1252;  //latin1 encoding
                    } else if(fontType[0].trim().equals("BaseFont.HELVETICA_OBLIQUE")) {
                        fontType[0] = BaseFont.HELVETICA_OBLIQUE;
                        encoding = BaseFont.CP1252;
                    } else if(fontType[0].trim().equals("BaseFont.ZAPFDINGBATS")) {
                        fontType[0] = BaseFont.ZAPFDINGBATS;
                        encoding = BaseFont.ZAPFDINGBATS;
                    } else {
                        fontType[0] = BaseFont.COURIER;
                        encoding = BaseFont.CP1252;
                    }
                    
                    
                    bf = BaseFont.createFont(fontType[0],encoding,BaseFont.NOT_EMBEDDED);
                    
                    // write in a rectangle area
                    if (cfgVal.length >= 9) {
                        Font font = new Font(bf, Integer.parseInt(cfgVal[5].trim()), fontFlags);
                        //ct.setSimpleColumn(60, 300, 200, 500, 10,
                        // Element.ALIGN_LEFT);
                        //ct.addText(new Phrase(15, "xxxx xxxxx xxxxx xxxxx xxx
                        // xxxxx xxxxx xxxx xxxxx xxxxxx xxxx xxxxxxx xxxxx
                        // xxxx", font));
                        ct.setSimpleColumn(Integer.parseInt(cfgVal[1].trim()), (height - Integer.parseInt(cfgVal[2]
                                .trim())), Integer.parseInt(cfgVal[7].trim()), (height - Integer.parseInt(cfgVal[8]
                                .trim())), Integer.parseInt(cfgVal[9].trim()), (cfgVal[0].trim().equals("left") ?
                                    Element.ALIGN_LEFT: (cfgVal[0].trim().equals("right") ? Element.ALIGN_RIGHT :
                                        Element.ALIGN_CENTER)));
                        
                        //ct.addText(new Phrase(12, props.getProperty(tempName.toString(), ""), font)); // page
                        ct.setText(new Phrase(12, props.getProperty(tempName.toString(), ""), font));
                        // size
                        // leading
                        // space between two
                        // lines
                        ct.go();
                        continue;
                    }
                    
                    // draw line directly
                    if (tempName.toString().startsWith("__$line")) {
                        cb.setRGBColorStrokeF(0f, 0f, 0f);
                        cb.setLineWidth(Float.parseFloat(cfgVal[4].trim()));
                        cb.moveTo(Float.parseFloat(cfgVal[0].trim()), Float.parseFloat(cfgVal[1].trim()));
                        cb.lineTo(Float.parseFloat(cfgVal[2].trim()), Float.parseFloat(cfgVal[3].trim()));
                        // stroke the lines
                        cb.stroke();
                        // write text directly
                        
                    } else if (tempName.toString().startsWith("__")) {
                        cb.beginText();
                        cb.setFontAndSize(bf, Integer.parseInt(cfgVal[5].trim()));
                        cb
                                .showTextAligned((cfgVal[0].trim().equals("left") ? PdfContentByte.ALIGN_LEFT
                                : (cfgVal[0].trim().equals("right") ? PdfContentByte.ALIGN_RIGHT
                                : PdfContentByte.ALIGN_CENTER)), (cfgVal.length >= 7 ? (cfgVal[6]
                                .trim()) : props.getProperty(tempName.toString(), "")), Integer
                                .parseInt(cfgVal[1].trim()), (height - Integer.parseInt(cfgVal[2].trim())), 0);
                        
                        cb.endText();
                    } else if (tempName.toString().equals("forms_promotext")){
                        if ( OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT") != null ){
                            log.info("adding user placed forms_promotext");
                            cb.beginText();
                            cb.setFontAndSize(bf, Integer.parseInt(cfgVal[5].trim()));
                            cb.showTextAligned((cfgVal[0].trim().equals("left") ? PdfContentByte.ALIGN_LEFT : (cfgVal[0].trim().equals("right") ? PdfContentByte.ALIGN_RIGHT : PdfContentByte.ALIGN_CENTER)),
                                    OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT"),
                                    Integer.parseInt(cfgVal[1].trim()),
                                    (height - Integer.parseInt(cfgVal[2].trim())),
                                    0);
                            
                            cb.endText();
                        }
                    } else { // write prop text
                        
                        cb.beginText();
                        cb.setFontAndSize(bf, Integer.parseInt(cfgVal[5].trim()));
                        cb
                                .showTextAligned((cfgVal[0].trim().equals("left") ? PdfContentByte.ALIGN_LEFT
                                : (cfgVal[0].trim().equals("right") ? PdfContentByte.ALIGN_RIGHT
                                : PdfContentByte.ALIGN_CENTER)), (cfgVal.length >= 7 ? ((props
                                .getProperty(tempName.toString(), "").equals("") ? "" : cfgVal[6].trim()))
                                : props.getProperty(tempName.toString(), "")), Integer.parseInt(cfgVal[1]
                                .trim()), (height - Integer.parseInt(cfgVal[2].trim())), 0);
                        
                        cb.endText();
                    }
                    
                }
                
                //----------
                if ( OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT") != null && printCfg[i-1].getProperty("forms_promotext") == null){
                    log.info("adding forms_promotext");
                    
                    // remove elements of the PDF file
                    Rectangle rec = new Rectangle(160, 12, 465, 21);
                    rec.setBackgroundColor(java.awt.Color.WHITE);
                    cb.rectangle(rec);
                    
                    cb.beginText();
                    cb.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA,BaseFont.CP1252,BaseFont.NOT_EMBEDDED), 6);
                    cb.showTextAligned(PdfContentByte.ALIGN_CENTER, OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT"), width/2, 16, 0);
                    cb.endText();
                }
                //----------
                
                /* used to remove elements of the PDF file
                log.debug("Writing rectangle")
                Rectangle rec = new Rectangle(243, 791-140, 289, 791-129);
                rec.setBackgroundColor(java.awt.Color.WHITE);
                cb.rectangle(rec);*/
                
                
                //graphic
                if ((graphicPageArray.contains(Integer.toString(i)) || i == 1 && graphicPageArray.size() == 0 ) && cfgGraphicFileNo > 0) {
                    boolean bFormAR = false;
                    int origX = 0;
                    int origY = 0;
                    String nMaxPixX = "0";
                    String nMaxPixY = "0";
                    String fStartX = "0f";
                    String fEndX = "0f";
                    String fStartY = "0f";
                    String fEndY = "0f";
                    
                    String dateFormat = null;
                    String fEDB = null;
                    String className = null;
                    
                    for (int k = 0; k < cfgGraphicFileNo; k++) {
                        Vector xDate = new Vector();
                        Vector yHeight = new Vector();
                        Properties args = new Properties();
                        
                        for (Enumeration e = graphicCfg[k].propertyNames(); e.hasMoreElements();) {
                            tempName = new StringBuffer(e.nextElement().toString());
                            tempValue = graphicCfg[k].getProperty(tempName.toString()).trim();
                            if (tempName.toString().equals("__finalEDB"))
                                args.setProperty(tempName.toString(), props.getProperty(tempValue));
                            else if (tempName.toString().equals("__dateFormat"))
                                args.setProperty(tempName.toString(),tempValue);
                            else if (tempName.toString().equals("__nMaxPixX"))
                                args.setProperty(tempName.toString(),tempValue);
                            else if (tempName.toString().equals("__nMaxPixY"))
                                args.setProperty(tempName.toString(),tempValue);
                            else if (tempName.toString().equals("__fStartX"))
                                args.setProperty(tempName.toString(),tempValue);
                            else if (tempName.toString().equals("__fEndX"))
                                args.setProperty(tempName.toString(),tempValue);
                            else if (tempName.toString().equals("__fStartY"))
                                args.setProperty(tempName.toString(),tempValue);
                            else if (tempName.toString().equals("__fEndY"))
                                args.setProperty(tempName.toString(),tempValue);
                            else if (tempName.toString().equals("__origX"))
                                origX = Integer.parseInt(tempValue);
                            else if (tempName.toString().equals("__origY"))
                                origY = Integer.parseInt(tempValue);
                            else if (tempName.toString().equals("__className"))
                                className = tempValue;
                            else {
                                xDate.add(props.getProperty(tempName.toString()));
                                yHeight.add(props.getProperty(tempValue));
                            }
                        } // end for read in
                        
                        FrmPdfGraphic pdfGraph = FrmGraphicFactory.create(className);
                        pdfGraph.init(args);
                        
                        Properties gProp = pdfGraph.getGraphicXYProp(xDate, yHeight);
                        
                        //draw the pic
                        cb.setLineWidth(1.5f);
                        
                        if (k % 2 == 0) {
                            cb.setRGBColorStrokeF(0f, 0f, 255f);
                        } else {
                            cb.setRGBColorStrokeF(255f, 0f, 0f);
                        }
                        
                        for (Enumeration e = gProp.propertyNames(); e.hasMoreElements();) {
                            tempName = new StringBuffer(e.nextElement().toString());
                            tempValue = gProp.getProperty(tempName.toString(), "");
                            if (tempValue.equals(""))
                                continue;
                            
                            cb.circle((origX + Float.parseFloat(tempName.toString())), (height - origY + Float
                                    .parseFloat(tempValue)), 1.5f);
                            cb.stroke();
                        }
                        /*
                        if (fEDB != null && fEDB.length() >= 8) {
                            //make the graphic class
                            FrmPdfGraphicAR myClass = new FrmPdfGraphicAR();
                            myClass.init(nMaxPixX, nMaxPixY, fStartX, fEndX, fStartY, fEndY, dateFormat, fEDB);
                            Properties gProp = myClass.getGraphicXYProp(xDate, yHeight);
                         
                            //draw the pic
                            cb.setLineWidth(1.5f);
                            //cb.setRGBColorStrokeF(0f, 255f, 0f); //cb.circle(52f,
                            // height - 751f, 1f);//cb.circle(52f, height - 609f,
                            // 1f);
                            for (Enumeration e = gProp.propertyNames(); e.hasMoreElements();) {
                                tempName = new StringBuffer(e.nextElement().toString());
                                tempValue = gProp.getProperty(tempName.toString(), "");
                                if (tempValue.equals(""))
                                    continue;
                         
                                cb.circle((origX + Float.parseFloat(tempName.toString())), (height - origY + Float
                                        .parseFloat(tempValue)), 1.5f);
                                cb.stroke();
                            }
                        }
                         
                        // general chart
                        if (!bFormAR) {
                            //make the graphic class
                            FrmPdfGraphicGrowthChart myClass = new FrmPdfGraphicGrowthChart();
                            myClass.init(nMaxPixX, nMaxPixY, fStartX, fEndX, fStartY, fEndY);
                            Properties gProp = myClass.getGraphicXYProp(xDate, yHeight);
                         
                            //draw the pic
                            cb.setLineWidth(1.5f);
                            if (k % 2 == 0) {
                                cb.setRGBColorStrokeF(0f, 0f, 255f);
                            } else {
                                cb.setRGBColorStrokeF(255f, 0f, 0f);
                            }
                            for (Enumeration e = gProp.propertyNames(); e.hasMoreElements();) {
                                tempName = new StringBuffer(e.nextElement().toString());
                                tempValue = gProp.getProperty(tempName.toString(), "");
                                if (tempValue.equals(""))
                                    continue;
                         
                                cb.circle((origX + Float.parseFloat(tempName.toString())), (height - origY + Float
                                        .parseFloat(tempValue)), 1.5f);
                                cb.stroke();
                            }
                        } // end of first pic */
                    } // end of for loop
                }
                
            }
            
        } catch (DocumentException dex) {
            baosPDF.reset();
            throw dex;
        } finally {
            if (document != null)
                document.close();
            if (writer != null)
                writer.close();
        }
        
        return baosPDF;
    }
    
    protected Properties getCfgProp(String cfgFilename) {
        Properties ret = new Properties();
        //String propFilename = "../../OscarDocument/" + getProjectName() + "/form/" + cfgFilename;
        String propFilename = oscar.OscarProperties.getInstance().getProperty("pdfFORMDIR", "") + "/" + cfgFilename;
        
        try {
            log.debug("1Looking for the prop file! " + propFilename);
            InputStream is = new FileInputStream(propFilename); //getServletContext().getResourceAsStream(propFilename);
            if (is != null) {
                log.debug("2Found the prop file! " + cfgFilename);
                ret.load(is);
                is.close();
            } else {
                log.warn("3Can't open the prop file! " + cfgFilename);
            }
        } catch (Exception e) {
            try {
                String propPath = "/WEB-INF/classes/oscar/form/prop/";
                InputStream is = getServletContext().getResourceAsStream(propPath + cfgFilename);
                if (is != null) {
                    log.debug("found prop file " + propPath + cfgFilename);
                    ret.load(is);
                    is.close();
                }
            } catch (Exception ee) {
                log.warn("Can't find the prop file! " + cfgFilename);
            }
        }
        return ret;
    }
    
    private String getProjectName() {
        String propPath = "" + this.getClass().getClassLoader().getResource("/");
        propPath = propPath.substring(0, propPath.lastIndexOf("/WEB-INF"));
        String propFilename = propPath.substring(propPath.lastIndexOf("/") + 1);
        return propFilename;
    }
    
}
