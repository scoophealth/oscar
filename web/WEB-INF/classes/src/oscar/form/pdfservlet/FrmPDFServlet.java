/*
 *
 *  
 */
// javac -classpath .;..\lib\itext-1.01.jar -d . FrmPDFServlet.java
// form/createpdf?__title=British+Columbia+Antenatal+Record+Part+1&__cfgfile=bcar1PrintCfgPg1&__cfgfile=bcar1PrintCfgPg2&__template=bcar1

package oscar.form.pdfservlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.InputStream;
import oscar.form.graphic.FrmPdfGraphicAR;

// import the iText packages
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

/** 
 * 
 * 
 */
public class FrmPDFServlet extends HttpServlet {
	/** 
	* 
	* 
	*/
	public FrmPDFServlet() {
		super();
	}


	public void doGet(HttpServletRequest req, HttpServletResponse res)
		throws javax.servlet.ServletException, java.io.IOException	{
		doPost(req, res);
	}

	/**
	 *  
	 * 
	 * @param req HTTP request object 
	 * @param resp HTTP response object
	 * 
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
		throws javax.servlet.ServletException, java.io.IOException	{
		DocumentException ex = null;
		
		ByteArrayOutputStream baosPDF = null;
		
		try	{
			baosPDF = generatePDFDocumentBytes(req, this.getServletContext());
			
			StringBuffer sbFilename = new StringBuffer();
			sbFilename.append("filename_");
			//sbFilename.append(System.currentTimeMillis());
			sbFilename.append(".pdf");

			// set the Cache-Control header
			res.setHeader("Cache-Control", "max-age=0");
			//res.setHeader("Cache-Control","no-cache"); //HTTP 1.1 
			res.setDateHeader ("Expires", 0); 
			
			res.setContentType("application/pdf");
			
			// The Content-disposition value will be inline

			StringBuffer sbContentDispValue = new StringBuffer();
			sbContentDispValue.append("inline; filename="); //inline - display the pdf file directly rather than open/save selection
			//sbContentDispValue.append("; filename=");
			sbContentDispValue.append(sbFilename);
							
			res.setHeader("Content-disposition", sbContentDispValue.toString());

			res.setContentLength(baosPDF.size());

			ServletOutputStream sos;

			sos = res.getOutputStream();
			
			baosPDF.writeTo(sos);
			
			sos.flush();
		} catch (DocumentException dex)	{
			res.setContentType("text/html");
			PrintWriter writer = res.getWriter();
			writer.println("Exception from: " + this.getClass().getName() + " " + dex.getClass().getName() + "<br>");
			writer.println("<pre>");
			dex.printStackTrace(writer);
			writer.println("</pre>");
		} finally	{
			if (baosPDF != null)	{
				baosPDF.reset();
				//baosPDF.close();
			}
		}
		 
	}

	/**
	 *  
	 */
	protected ByteArrayOutputStream generatePDFDocumentBytes(
		final HttpServletRequest req,	final ServletContext ctx)
		throws DocumentException, java.io.IOException	{	
		Document document = new Document();
		//document = new Document(psize, 50, 50, 50, 50);
		
		ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
		PdfWriter writer = null;

		try	{
			writer = PdfWriter.getInstance(document, baosPDF);

			String title = req.getParameter("__title") != null? req.getParameter("__title") : "Unknown" ;
			String[] cfgFile = req.getParameterValues("__cfgfile");
			String template = req.getParameter("__template") != null? req.getParameter("__template")+".pdf" : "" ;
			//for page 1 picture only
			String cfgGraphicFile = req.getParameter("__cfgGraphicFile") != null? req.getParameter("__cfgGraphicFile")+".txt" : "" ;

			int cfgFileNo = 0;
			Properties[] printCfg = null;
			if (cfgFile != null )	{
				cfgFileNo = cfgFile.length;
				printCfg = new Properties[cfgFileNo];
				for (int i=0; i<cfgFileNo; i++)	{
					cfgFile[i] += ".txt"  ;
					if (cfgFile[i].indexOf("/") > 0) cfgFile[i] = "";
					printCfg[i] =  getCfgProp(cfgFile[i]);
				}
			} 

			Properties	graphicCfg =  cfgGraphicFile.equals("") ? null : getCfgProp(cfgGraphicFile);

			String[] cfgVal = null;
			StringBuffer tempName = null;
			String tempValue = null;


			// get the print prop values
			Properties props = new Properties();
			StringBuffer temp = new StringBuffer("");
			for (Enumeration e = req.getParameterNames() ; e.hasMoreElements() ;) {
				temp = new StringBuffer(e.nextElement().toString());
				props.setProperty(temp.toString(), req.getParameter(temp.toString()));
			}


            document.addTitle(title);
            document.addSubject("");
            document.addKeywords("pdf, itext");
            document.addCreator("OSCAR");
            document.addAuthor("");
            document.addHeader("Expires", "0");
			
			document.setPageSize(PageSize.LETTER);
			document.open();

            // create a reader for a certain document
            PdfReader reader = new PdfReader("/" + template);
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

            while (i < n) {
				document.newPage();
                p++;
                i++;
                PdfImportedPage page1 = writer.getImportedPage(reader, i);
                cb.addTemplate(page1, 1, 0, 0, 1, 0, 0);
                //System.err.println(cfgFileNo + "processed page " + i);

                BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				cb.setRGBColorStroke(0, 0, 255 );
                //cb.setFontAndSize(bf, 8);
				// LEFT/CENTER/RIGHT, X, Y, 
                //cb.showTextAligned(PdfContentByte.ALIGN_LEFT, "Cathy Pacific", 126, height-50, 0);

				if (i > (cfgFileNo)) continue;

				for (Enumeration e = printCfg[i-1].propertyNames() ; e.hasMoreElements() ;) {
					tempName = new StringBuffer(e.nextElement().toString());
					cfgVal = printCfg[i-1].getProperty(tempName.toString()).split(" *, *");

					// write in a rectangle area
					if (cfgVal.length >= 9) {
						Font font = new Font(bf, Integer.parseInt(cfgVal[5].trim()), Font.NORMAL);
						//ct.setSimpleColumn(60, 300, 200, 500, 10, Element.ALIGN_LEFT);
						//ct.addText(new Phrase(15, "xxxx xxxxx xxxxx xxxxx xxx xxxxx xxxxx xxxx xxxxx xxxxxx xxxx xxxxxxx xxxxx xxxx", font));
						ct.setSimpleColumn(Integer.parseInt(cfgVal[1].trim()), (height-Integer.parseInt(cfgVal[2].trim())), Integer.parseInt(cfgVal[7].trim()), (height-Integer.parseInt(cfgVal[8].trim())), Integer.parseInt(cfgVal[9].trim()), Element.ALIGN_LEFT);
						ct.addText(new Phrase(12, props.getProperty(tempName.toString(), "") , font)); // page size leading space between two lines
						ct.go(); 
						continue;
					}

	                cb.beginText();
					cb.setFontAndSize(bf, Integer.parseInt(cfgVal[5].trim()));
					cb.showTextAligned( (cfgVal[0].trim().equals("left")? PdfContentByte.ALIGN_LEFT : (cfgVal[0].trim().equals("right")? PdfContentByte.ALIGN_RIGHT : PdfContentByte.ALIGN_CENTER) ), (cfgVal.length >= 7? ( (props.getProperty(tempName.toString(), "").equals("")?"":cfgVal[6].trim() )) : props.getProperty(tempName.toString(), "") ), Integer.parseInt(cfgVal[1].trim()), (height-Integer.parseInt(cfgVal[2].trim())), 0);
	                cb.endText();
				}

				//graphic
				if (i == 1 && graphicCfg != null)	{
					System.err.println("processed page " + i);
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

					Vector xDate = new Vector();
					Vector yHeight = new Vector();

					for (Enumeration e = graphicCfg.propertyNames() ; e.hasMoreElements() ;) {
						tempName = new StringBuffer(e.nextElement().toString());
						tempValue = graphicCfg.getProperty(tempName.toString()).trim();

						if (tempName.toString().equals("__finalEDB")) fEDB = props.getProperty(tempValue);
						else if (tempName.toString().equals("__dateFormat")) dateFormat = tempValue;
						else if (tempName.toString().equals("__nMaxPixX")) nMaxPixX = tempValue;
						else if (tempName.toString().equals("__nMaxPixY")) nMaxPixY = tempValue;
						else if (tempName.toString().equals("__fStartX")) fStartX = tempValue;
						else if (tempName.toString().equals("__fEndX")) fEndX = tempValue;
						else if (tempName.toString().equals("__fStartY")) fStartY = tempValue;
						else if (tempName.toString().equals("__fEndY")) fEndY = tempValue;
						else if (tempName.toString().equals("__origX")) origX = Integer.parseInt(tempValue);
						else if (tempName.toString().equals("__origY")) origY = Integer.parseInt(tempValue);
						else if (tempName.toString().equals("__className")) className = tempValue;
						else {
							xDate.add(props.getProperty(tempName.toString()));
							yHeight.add(props.getProperty(tempValue));
						}
					}

					//make the graphic class
					FrmPdfGraphicAR myClass = new FrmPdfGraphicAR();
					myClass.init(nMaxPixX,nMaxPixY,fStartX,fEndX,fStartY,fEndY,dateFormat,fEDB);
					Properties gProp = myClass.getGraphicXYProp(xDate, yHeight);

					//draw the pic
					cb.setLineWidth(1.5f);  
					//cb.setRGBColorStrokeF(0f, 255f, 0f);	//cb.circle(52f, height - 751f, 1f);//cb.circle(52f, height - 609f, 1f);
					for (Enumeration e = gProp.propertyNames() ; e.hasMoreElements() ;) {
						tempName = new StringBuffer(e.nextElement().toString());
						tempValue = gProp.getProperty(tempName.toString(), "");
						if (tempValue.equals("")) continue;

						cb.circle((origX + Float.parseFloat(tempName.toString())), (height - origY + Float.parseFloat(tempValue)), 1.5f);
						cb.stroke(); 
					}
				}


            }


		} catch (DocumentException dex)		{
			baosPDF.reset();
			throw dex; 
		} finally	{
			if (document != null)	document.close();
			if (writer != null)		writer.close();
		}

		return baosPDF;
	}
	
	protected Properties getCfgProp(String cfgFilename) {
		Properties ret = new Properties();
		//ClassLoader cLoader = getClass().getClassLoader();
		//System.out.println(getServletContext().getRealPath("/WEB-INF/classes/"+ cfgFilename));

		try {
				//InputStream is = cLoader.getResourceAsStream(cfgFilename);
				InputStream is = getServletContext().getResourceAsStream("/WEB-INF/classes/" + cfgFilename);
				
				if (is != null) {
					ret.load(is);
	
					is.close();
				}
			}	catch (Exception e) {
				e.printStackTrace();
			}
	
			return ret;
		}
	 
}
