
/*
 *
 *
 */
// javac -classpath .;..\lib\itext-1.01.jar -d . FrmPDFServlet.java
// form/createpdf?__title=British+Columbia+Antenatal+Record+Part+1&__cfgfile=bcar1PrintCfgPg1&__cfgfile=bcar1PrintCfgPg2&__template=bcar1
package oscar.form.pdfservlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.printing.FontSettings;
import org.oscarehr.common.printing.PdfWriterFactory;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.form.FrmRecord;
import oscar.form.FrmRecordFactory;
import oscar.form.graphic.FrmGraphicFactory;
import oscar.form.graphic.FrmPdfGraphic;
import oscar.log.LogAction;
import oscar.util.ConcatPDF;

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
     * @param res HTTP response object
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws javax.servlet.ServletException,
            java.io.IOException {

        ByteArrayOutputStream baosPDF = null;
        LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(req);
        
        try {        	
            File tmpFile = null;
            
            if(req.getParameter("multiple")!=null) {
            	ArrayList<Object> files = new ArrayList<Object>();
            	for(int x=0;x<Integer.parseInt(req.getParameter("multiple"));x++) {
            		baosPDF = new ByteArrayOutputStream();
                    baosPDF = generatePDFDocumentBytes(req, this.getServletContext(),baosPDF,x);
            		tmpFile = File.createTempFile("formpdf", String.valueOf((int)Math.random()*10000));
                    baosPDF.writeTo(new FileOutputStream(tmpFile));
                    files.add(tmpFile.getAbsolutePath());
            	}
            	tmpFile = File.createTempFile("formpdf", String.valueOf((int)Math.random()*10000));
            	ConcatPDF.concat(files, tmpFile.getAbsolutePath());
            } else {
            	baosPDF = new ByteArrayOutputStream();
                baosPDF = generatePDFDocumentBytes(req, this.getServletContext(),baosPDF,0);
            	tmpFile = File.createTempFile("formpdf", String.valueOf((int)Math.random()*10000));
                baosPDF.writeTo(new FileOutputStream(tmpFile));
            }
            StringBuilder sbFilename = new StringBuilder();
            sbFilename.append("filename_");
            //sbFilename.append(System.currentTimeMillis());
            sbFilename.append(".pdf");

            // set the Cache-Control header
            res.setHeader("Cache-Control", "max-age=0");
            //res.setHeader("Cache-Control","no-cache"); //HTTP 1.1
            res.setDateHeader("Expires", 0);
            res.setContentType("application/pdf");

            // The Content-disposition value will be inline

            StringBuilder sbContentDispValue = new StringBuilder();
            sbContentDispValue.append("inline; filename="); //inline - display
            // the pdf file
            // directly rather
            // than open/save
            // selection
            //sbContentDispValue.append("; filename=");
            sbContentDispValue.append(sbFilename);

            res.setHeader("Content-disposition", sbContentDispValue.toString());

            
            res.setContentLength((int)tmpFile.length());
            
            
            ServletOutputStream sout = res.getOutputStream();  
            FileInputStream fis = new FileInputStream(tmpFile);
            try {
	            byte[] buffer = new byte[64000];  
	            int bytesRead = 0;  
	                                
	            while(true)  
                {  
                       bytesRead = fis.read(buffer);  
                       if (bytesRead == -1)  
                              break;  
                                    
                       sout.write(buffer,0,bytesRead);  
                }
            }
            finally {
            	fis.close();
            }
            
            LogAction.addLogSynchronous(loggedInInfo,"FrmPDFServlet", "formID=" + req.getParameter("formId") + ",form_class=" + req.getParameter("form_class"));
            
        } catch (DocumentException dex) {
            res.setContentType("text/html");
            PrintWriter writer = res.getWriter();
            writer.println("Exception from: " + this.getClass().getName() + " " + dex.getClass().getName() + "<br>");
            writer.println("<pre>");
            writer.println(dex.getMessage());
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
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("/oscar/form/prop/Hsfo_Rx.jasper");
       
        try {
            JasperRunManager.runReportToPdfStream(is, baos, rx.getParams(), ds);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
        finally {
        	IOUtils.closeQuietly(is);
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
    protected ByteArrayOutputStream generatePDFDocumentBytes(final HttpServletRequest req, final ServletContext ctx, ByteArrayOutputStream baosPDF, int multiple)
    throws DocumentException, java.io.IOException {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(req);

    	// added by vic, hsfo
        if (HSFO_RX_DATA_KEY.equals(req.getParameter("__title")))
            return generateHsfoRxPDF(req);

        final String PAGESIZE = "printPageSize";
        Document document = new Document();
        //document = new Document(psize, 50, 50, 50, 50);

        String suffix = (multiple>0)?String.valueOf(multiple):"";
        
        PdfWriter writer = null;

        try {
            writer = PdfWriterFactory.newInstance(document, baosPDF, FontSettings.HELVETICA_6PT); 

            String title = req.getParameter("__title"+suffix) != null ? req.getParameter("__title"+suffix) : "Unknown";
            
            String template = req.getParameter("__template"+suffix) != null ? req.getParameter("__template"+suffix) + ".pdf" : "";
                        
            int numPages = 1;
            String pages = req.getParameter("__numPages"+suffix);
            if( pages != null ) {
            	numPages = Integer.parseInt(pages);
            }
                                     
            Properties[] printCfg = null;
            int cfgFileNo;
            String[] cfgFile = req.getParameterValues("__cfgfile"+suffix);
            
            cfgFileNo = cfgFile == null ? 0 : cfgFile.length;
            if( cfgFileNo > 0 ) {
            	printCfg = new Properties[cfgFileNo];
            	for (int idx2 = 0; idx2 < cfgFileNo; ++idx2) {
                    cfgFile[idx2] += ".txt";
                    if (cfgFile[idx2].indexOf("/") > 0) {
                        cfgFile[idx2] = "";
                    }
                    
                    printCfg[idx2] = getCfgProp(cfgFile[idx2]);
                }
            }
            
                    
            
            Properties[][] graphicCfg = new Properties[numPages][];
            String[] cfgGraphicFile;
            String paramName;
            int cfgGraphicFileNo;            
            for( int idx = 0; idx < numPages; ++idx ) {
            	if( idx == 0 ) {
            		cfgGraphicFile = req.getParameterValues("__cfgGraphicFile"+suffix);            		
            	}
            	else {
            		paramName = "__cfgGraphicFile" + String.valueOf(idx) + suffix;
            		cfgGraphicFile = req.getParameterValues(paramName);
            	}
            	            	
            	cfgGraphicFileNo = cfgGraphicFile == null ? 0: cfgGraphicFile.length;
            	if( cfgGraphicFileNo == 0 ) {
            		graphicCfg[idx] = null;            		
            	}
            	else {
            		graphicCfg[idx] = new Properties[cfgGraphicFileNo];
            		for( int idx2 = 0; idx2 < cfgGraphicFileNo; ++idx2 ) {
            			cfgGraphicFile[idx2] += ".txt";
            			graphicCfg[idx][idx2] = getCfgProp(cfgGraphicFile[idx2]);
            		}
            	}            	
            }
            
            String[] cfgVal = null;
            StringBuilder tempName = null;
            String tempValue = null;

            //load from DB
            int demoNo = Integer.parseInt(req.getParameter("demographic_no"));
            String strFormId =req.getParameter("formId");
            int formId = 0;
            try {
            	formId =  Integer.parseInt(strFormId);
            }catch(NumberFormatException e){/*ignore*/}

            String formClass=req.getParameter("form_class");
            FrmRecord record = (new FrmRecordFactory()).factory(formClass);
            java.util.Properties props = new Properties();
            if(record != null) {
	            try {
	            	props = record.getFormRecord(loggedInInfo, demoNo, formId);
	            }catch(SQLException e) {
	            	MiscUtils.getLogger().error("Error",e);
	            }
            }
            
            // get the print prop values
            //Properties props = new Properties();
            StringBuilder temp = new StringBuilder("");
            for (Enumeration<String> e = req.getParameterNames(); e.hasMoreElements();) {
                temp = new StringBuilder(e.nextElement().toString());
                props.setProperty(temp.toString(), req.getParameter(temp.toString()));
            }
            
            if(req.getParameter("postProcessor"+suffix)!=null) {
            	String className = "oscar.form.pdfservlet."+req.getParameter("postProcessor"+suffix);
            	try {
            		FrmPDFPostValueProcessor pp = (FrmPDFPostValueProcessor)Class.forName(className).newInstance();
            		props = pp.process(props);
            	}catch(Exception e) {
            		//ignore
            	}
            }
            
            String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
            int totalPages = 1;
            if(req.getParameter("multiple")!=null)
            	totalPages = Integer.parseInt(req.getParameter("multiple"));
            String currentUser = loggedInInfo.getLoggedInProvider().getFormattedName();
            String pg = suffix.length()==0||suffix.equals("0")?"0":suffix;                        
            String currentPage = String.valueOf(Integer.parseInt(pg)+1);
            
            props.setProperty("total_pages",String.valueOf(totalPages));
            props.setProperty("current_page",currentPage);
            props.setProperty("current_user", currentUser);
            props.setProperty("current_date", currentDate);
            
            props.setProperty("printer_info", "Printed on " + currentDate + " by " + currentUser + ": Page " + currentPage + " of " + totalPages );
            
            //initialise measurement collections = a list of pages sections measurements
            List<List<List<String>>>xMeasurementValues = new ArrayList<List<List<String>>>();
            List<List<List<String>>>yMeasurementValues = new ArrayList<List<List<String>>>();
            for( int idx = 0; idx < numPages; ++idx ) {
            	MiscUtils.getLogger().debug("Adding page " + idx);
            	xMeasurementValues.add(new ArrayList<List<String>>());
            	yMeasurementValues.add(new ArrayList<List<String>>());
            }
            
            String elementNum;
            int page;
            int section;
            int index, index2, index3;
            Properties measurements = new Properties();
            
            for (Enumeration<String> e = req.getAttributeNames(); e.hasMoreElements(); ) {
                temp = new StringBuilder(e.nextElement().toString());                
                measurements.setProperty(temp.toString(), req.getAttribute(temp.toString()).toString());
                
                //for graphing measurements of height and weight
                //since we don't know how many there could be they cannot be defined in a config file
                //save them here so they can be added to the graph vectors below    
                //naming convention of measurements is xVal_num_section_page, yVal_num_section_page
                //num uniquely identifies value so x num should = y num
                //section allows graphing of more than one measurement axis e.g. if top of page is different graph than bottom of page see rourke
                //page is the pdf page it should be plotted on
                if( temp.toString().startsWith("xVal_") ) {
                	MiscUtils.getLogger().debug("Processing " + temp.toString());
                	
                	index = temp.indexOf("_");
                	index2 = temp.indexOf("_", index+1);
                	elementNum = temp.substring(index+1,index2);
                	
                	index3 = temp.lastIndexOf("_");
                	section = Integer.parseInt(temp.substring(index2 + 1, index3));                	
                	page = Integer.parseInt(temp.substring(index3+1));
                	
                	//page is zero based, numPages is not
                	if( page >= numPages ) {
                		continue;
                	}
                	
                	//if this is the first measurement of the section init array
                	while( xMeasurementValues.get(page).size() <= section ) {
                		MiscUtils.getLogger().debug("Adding section " + section);
                		List<List<String>> list = xMeasurementValues.get(page);
                		list.add(new ArrayList<String>());
                	}
                	                	
                	while( yMeasurementValues.get(page).size() <= section ) {
                		List<List<String>> list = yMeasurementValues.get(page);
                		list.add(new ArrayList<String>());
                	}
                	
                	xMeasurementValues.get(page).get(section).add((String)req.getAttribute(temp.toString()));
                	MiscUtils.getLogger().debug("Setting xMeasurementDate to " + (String)req.getAttribute(temp.toString()));
                	                	
                	temp = new StringBuilder("yVal_");
                	temp = temp.append(elementNum);
                	temp = temp.append("_"+section);
                	temp = temp.append("_"+page);
                	MiscUtils.getLogger().debug("Key " + temp);
                	tempValue = (String)req.getAttribute(temp.toString());                	
                	yMeasurementValues.get(page).get(section).add(tempValue);
                	MiscUtils.getLogger().debug("Setting yMeasurementValue to " + tempValue);
                }
                else {
                	props.setProperty(temp.toString(), req.getAttribute(temp.toString()).toString());
                }
            }
            
            document.addTitle(title);
            document.addSubject("");
            document.addKeywords("pdf, itext");
            document.addCreator("OSCAR");
            document.addAuthor("");
            //document.addHeader("Expires", "0");

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
            float height;
            int n;
            try
            {
	            try {
	                reader = new PdfReader(propFilename);
	                log.info("Found template at " + propFilename);
	            } catch (Exception dex) {
	                log.debug("change path to inside oscar from :" + propFilename);
	                reader = new PdfReader("/oscar/form/prop/" + template);
	                log.debug("Found template at /oscar/form/prop/" + template);
	            }
	
	            // retrieve the total number of pages
	            n = reader.getNumberOfPages();
	            // retrieve the size of the first page
	            Rectangle pSize = reader.getPageSize(1);
	            height = pSize.getHeight();
            }
            finally {
            	reader.close();
            }

            PdfContentByte cb = writer.getDirectContent();
            ColumnText ct = new ColumnText(cb);
            int i = 0;
            int fontFlags = 0;
            String propValue;
            
            while (i < n) {
                document.newPage();
                
                i++;
                PdfImportedPage page1 = writer.getImportedPage(reader, i);
                cb.addTemplate(page1, 1, 0, 0, 1, 0, 0);

                BaseFont bf; // = normFont;
                String encoding;

                cb.setRGBColorStroke(0, 0, 255);
                //cb.setFontAndSize(bf, 8);
                // LEFT/CENTER/RIGHT, X, Y,
                //cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cathy
                // Pacific", 126, height-50, 0);

                if ( i <= cfgFileNo ) {
                    
	                String[] fontType;
	                for (Enumeration e = printCfg[i - 1].propertyNames(); e.hasMoreElements();) {
	                    tempName = new StringBuilder(e.nextElement().toString());
	                    cfgVal = printCfg[i - 1].getProperty(tempName.toString()).split(",");
	                    for(int x=0;x<cfgVal.length;x++) {
	                    	cfgVal[x].trim();
	                    }
	
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
	                    propValue = props.getProperty(tempName.toString());
	                    //if not in regular config then check measurements
	                    if(propValue == null ) {
	                    	propValue = measurements.getProperty(tempName.toString(),"");
	                    }
	                    
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
	                        ct.setText(new Phrase(12, propValue, font));
	                        // size
	                        // leading
	                        // space between two
	                        // lines
	                        ct.go();
	                        continue;
	                    }
	                    
	                    //adapted by DENNIS WARREN June 2012 to allow a colour rectangle
	                    // handy for covering up parts of a document
	                    if(tempName.toString().startsWith("__$rectangle")) {
	                    	
	                    	float llx = Float.parseFloat(cfgVal[0].trim());
		                	float lly = Float.parseFloat(cfgVal[1].trim());
		                	float urx = Float.parseFloat(cfgVal[2].trim());
		                	float ury = Float.parseFloat(cfgVal[3].trim());
		                	
		                    Rectangle rec = new Rectangle(llx, lly, urx, ury);
		                    rec.setBackgroundColor(java.awt.Color.WHITE);
		                    cb.rectangle(rec);
	                    	
	                    } else if (tempName.toString().startsWith("__$line")) {
	                        cb.setRGBColorStrokeF(0f, 0f, 0f);
	                        cb.setLineWidth(Float.parseFloat(cfgVal[4].trim()));
	                        cb.moveTo(Float.parseFloat(cfgVal[0].trim()), Float.parseFloat(cfgVal[1].trim()));
	                        cb.lineTo(Float.parseFloat(cfgVal[2].trim()), Float.parseFloat(cfgVal[3].trim()));
	                        cb.stroke();
	
	                    } else if (tempName.toString().startsWith("__")) {
	                        cb.beginText();
	                        cb.setFontAndSize(bf, Integer.parseInt(cfgVal[5].trim()));
	                        cb.showTextAligned((cfgVal[0].trim().equals("left") ? PdfContentByte.ALIGN_LEFT
	                                : (cfgVal[0].trim().equals("right") ? PdfContentByte.ALIGN_RIGHT
	                                : PdfContentByte.ALIGN_CENTER)), (cfgVal.length >= 7 ? (cfgVal[6]
	                                .trim()) : propValue), Integer
	                                .parseInt(cfgVal[1].trim()), (height - Integer.parseInt(cfgVal[2].trim())), 0);	
	                        cb.endText();
	                    } else if (tempName.toString().equals("forms_promotext")){
//	                        if ( OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT") != null ){
//	                            log.info("adding user placed forms_promotext");
//	                            cb.beginText();
//	                            cb.setFontAndSize(bf, Integer.parseInt(cfgVal[5].trim()));
//	                            cb.showTextAligned((cfgVal[0].trim().equals("left") ? PdfContentByte.ALIGN_LEFT : (cfgVal[0].trim().equals("right") ? PdfContentByte.ALIGN_RIGHT : PdfContentByte.ALIGN_CENTER)),
//	                                    OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT"),
//	                                    Integer.parseInt(cfgVal[1].trim()),
//	                                    (height - Integer.parseInt(cfgVal[2].trim())),
//	                                    0);
//	
//	                            cb.endText();
//	                        }
	                    } else { // write prop text
	                        cb.beginText();
	                        cb.setFontAndSize(bf, Integer.parseInt(cfgVal[5].trim()));
	                        cb.showTextAligned((cfgVal[0].trim().equals("left") ? PdfContentByte.ALIGN_LEFT
	                                : (cfgVal[0].trim().equals("right") ? PdfContentByte.ALIGN_RIGHT
	                                : PdfContentByte.ALIGN_CENTER)), (cfgVal.length >= 7 ? ((propValue.equals("") ? "" : cfgVal[6].trim()))
	                                : propValue), Integer.parseInt(cfgVal[1]
	                                .trim()), (height - Integer.parseInt(cfgVal[2].trim())), 0);
	
	                        cb.endText();
	                    }
	
	                }
	
	                //----------
	                if ( OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT") != null && printCfg[i-1].getProperty("forms_promotext") == null){
//	                    log.info("adding forms_promotext");
//	
//	                    // remove elements of the PDF file
//	                    Rectangle rec = new Rectangle(160, 12, 465, 21);
//	                    rec.setBackgroundColor(java.awt.Color.WHITE);
//	                    cb.rectangle(rec);
//	
//	                    cb.beginText();
//	                    cb.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA,BaseFont.CP1252,BaseFont.NOT_EMBEDDED), 6);
//	                    cb.showTextAligned(PdfContentByte.ALIGN_CENTER, OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT"), width/2, 16, 0);
//	                    cb.endText();
	                }
	                
	                
                	
                } //end if there are print properties

                //graphic
                //if ((graphicPageArray.contains(Integer.toString(i)) || i == 1 && graphicPageArray.size() == 0 ) && cfgGraphicFileNo > 0) {
                
                    int origX = 0;
                    int origY = 0;

                    String className = null;
                    Properties[] tempPropertiesArray;
                    if( i <= graphicCfg.length ) {
                    	tempPropertiesArray = graphicCfg[i-1];
                    	MiscUtils.getLogger().debug("Plotting page " + i);
                    }
                    else {
                    	tempPropertiesArray = null;
                    	MiscUtils.getLogger().debug("Skipped Plotting page " + i);
                    }
                    String[] tempYcoords;
                    
                    //if there are properties to plot
                    if( tempPropertiesArray != null ) {
                    	MiscUtils.getLogger().debug("TEMP PROP LENGTH " + tempPropertiesArray.length);
	                    for (int k = 0; k < tempPropertiesArray.length; k++) {
	                    	//initialise with measurement values which are mapped to config file by form get graphic function
	                    	List<String> xDate, yHeight;
	                    	if( xMeasurementValues.get(i-1).size() > k && yMeasurementValues.get(i-1).size() > k ) {
	                    		xDate = new ArrayList<String>(xMeasurementValues.get(i-1).get(k));
	                    		yHeight = new ArrayList<String>(yMeasurementValues.get(i-1).get(k));
	                    	}
	                    	else {
	                    		xDate = new ArrayList<String>();
	                    		yHeight = new ArrayList<String>();
	                    	}
	                    	
	                        Properties args = new Properties();
	
	                        
	                        for (Enumeration e = tempPropertiesArray[k].propertyNames(); e.hasMoreElements();) {
	                            tempName = new StringBuilder(e.nextElement().toString());
	                            tempValue = tempPropertiesArray[k].getProperty(tempName.toString()).trim();
	                            if (tempName.toString().equals("__finalEDB"))
	                                args.setProperty(tempName.toString(), props.getProperty(tempValue));
	                            else if (tempName.toString().equals("__xDateScale"))
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
	                            	MiscUtils.getLogger().debug("Adding xDate " + tempName.toString() + " VAL: " + props.getProperty(tempName.toString()));
	                            	MiscUtils.getLogger().debug("Adding yHeight " + tempValue + " VAL: " + props.getProperty(tempValue));
	                                xDate.add(props.getProperty(tempName.toString()));
	                                yHeight.add(props.getProperty(tempValue));
	                            }
	                        } // end for read in from config file                                                
	                        
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
	                            tempName = new StringBuilder(e.nextElement().toString());
	                            tempValue = gProp.getProperty(tempName.toString(), "");
	                            
	                            if (tempValue.equals("")) {
	                                continue;
	                            }
	                            
	                            tempYcoords = tempValue.split(",");
	                            for( int idx = 0; idx < tempYcoords.length; ++idx ) {
	                            	tempValue = tempYcoords[idx];
	                            	MiscUtils.getLogger().debug("COORDS: cfg_pg " + k + " : " + String.valueOf(origX + Float.parseFloat(tempName.toString())) + ", " + String.valueOf(height - origY + Float
	                                    .parseFloat(tempValue)));
	
	                            	cb.circle((origX + Float.parseFloat(tempName.toString())), (height - origY + Float
	                                    .parseFloat(tempValue)), 1.5f);
	                            	cb.stroke();
	                            }
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
	                                tempName = new StringBuilder(e.nextElement().toString());
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
	                                tempName = new StringBuilder(e.nextElement().toString());
	                                tempValue = gProp.getProperty(tempName.toString(), "");
	                                if (tempValue.equals(""))
	                                    continue;
	
	                                cb.circle((origX + Float.parseFloat(tempName.toString())), (height - origY + Float
	                                        .parseFloat(tempValue)), 1.5f);
	                                cb.stroke();
	                            }
	                        } // end of first pic */
	                    } // end of for loop
                    } //end if there are properties to process

            }

        } catch (DocumentException dex) {
            baosPDF.reset();
            throw dex;
        } finally {
            if (document.isOpen())
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
            try {
	            if (is != null) {
	                log.debug("2Found the prop file! " + cfgFilename);
	                ret.load(is);
	                is.close();
	            }
            }
            finally {
            	is.close();
            }
        } catch (Exception e) {
            try {
                String propPath = "/WEB-INF/classes/oscar/form/prop/";
                InputStream is = getServletContext().getResourceAsStream(propPath + cfgFilename);
                try {
	                if (is != null) {
	                    log.debug("found prop file " + propPath + cfgFilename);
	                    ret.load(is);
	                    is.close();
	                }
                }
                finally {
                	is.close();
                }
            } catch (Exception ee) {
                log.warn("Can't find the prop file! " + cfgFilename);
            }
        }
        return ret;
    }

}
