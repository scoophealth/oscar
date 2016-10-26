<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>
<%-- 
    Document   : labCheck
    Created on : 15-Sep-2016, 9:03:20 PM
    Author     : rjonasz
--%>

<%@page import="java.io.InputStream"%>
<%@page import="oscar.oscarLab.ca.all.upload.MessageUploader"%>
<%@page import="org.oscarehr.common.model.FileUploadCheck"%>
<%@page import="java.security.MessageDigest"%>
<%@page import="org.oscarehr.common.dao.FileUploadCheckDao"%>
<%@page import="oscar.oscarLab.ca.all.upload.RouteReportResults"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="oscar.oscarLab.ca.all.upload.HandlerClassFactory"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.nio.file.attribute.BasicFileAttributes"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.ParseException"%>
<%@page import="java.nio.file.attribute.FileAttribute"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.zip.ZipEntry"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.util.zip.ZipOutputStream"%>
<%@page import="java.nio.file.attribute.FileTime"%>
<%@page import="com.lowagie.text.html.simpleparser.HTMLWorker"%>
<%@page import="java.io.StringReader"%>
<%@page import="com.lowagie.text.Element"%>
<%@page import="java.awt.Color"%>
<%@page import="com.lowagie.text.Chunk"%>
<%@page import="com.lowagie.text.Phrase"%>
<%@page import="com.lowagie.text.pdf.PdfPTable"%>
<%@page import="com.lowagie.text.pdf.PdfPCell"%>
<%@page import="com.lowagie.text.Font"%>
<%@page import="com.lowagie.text.DocumentException"%>
<%@page import="com.lowagie.text.PageSize"%>
<%@page import="com.lowagie.text.ExceptionConverter"%>
<%@page import="com.lowagie.text.pdf.BaseFont"%>
<%@page import="com.lowagie.text.pdf.PdfContentByte"%>
<%@page import="com.lowagie.text.Rectangle"%>
<%@page import="com.lowagie.text.pdf.PdfPageEventHelper"%>
<%@page import="java.io.FileNotFoundException"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.OutputStream"%>
<%@page import="org.oscarehr.common.printing.FontSettings"%>
<%@page import="org.oscarehr.common.printing.PdfWriterFactory"%>
<%@page import="com.lowagie.text.pdf.PdfWriter"%>
<%@page import="com.lowagie.text.Document"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.FileReader"%>
<%@page import="java.io.FileWriter"%>
<%@page import="oscar.oscarLab.ca.all.util.Utilities"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.nio.charset.MalformedInputException"%>
<%@page import="java.nio.file.DirectoryIteratorException"%>
<%@page import="java.nio.file.FileSystems"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.Hl7TextInfoDao"%>
<%@page import="org.oscarehr.common.model.Hl7TextInfo"%>
<%@page import="oscar.oscarLab.ca.all.parsers.Factory"%>
<%@page import="oscar.oscarLab.ca.all.parsers.MessageHandler"%>
<%@page import="java.io.IOException"%>
<%@page import="oscar.util.StringUtils"%>
<%@page import="java.util.List"%>
<%@page import="java.nio.charset.Charset"%>
<%@page import="java.nio.file.Files"%>
<%@page import="java.nio.file.DirectoryStream"%>
<%@page import="oscar.OscarProperties"%>
<%@page import="java.nio.file.Paths"%>
<%@page import="java.nio.file.Path"%>
<%@page contentType="text/html"%>
<%
long start = System.currentTimeMillis();
String docDir = OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
String cacheDir = docDir.substring(0, docDir.lastIndexOf("document")) + "document_cache/";

Path dir = FileSystems.getDefault().getPath( docDir );
Hl7TextInfoDao hl7TextInfoDao = SpringUtils.getBean(Hl7TextInfoDao.class);
int idx = 0;
int success, errors, missing, finalstatus;
StringBuilder accessionNums = new StringBuilder();
String filenameTemplate = docDir + "/labCheckReport.";
List<Path>pdfList = new ArrayList<Path>();
ArrayList<Path>labList = new ArrayList<Path>();
String action = request.getParameter("run");

if( "true".equalsIgnoreCase(action) ) {
    String file =  filenameTemplate + start + ".html";
    FileWriter fout = null;
    Calendar limCalendar = Calendar.getInstance();    
    limCalendar.set(2016,8,28);
    Date limitDate = limCalendar.getTime();
    LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
    FileUploadCheckDao fileUploadCheckDao = SpringUtils.getBean(FileUploadCheckDao.class);
    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
    try {
       fout = new FileWriter(file); 
        
       fout.write("<html><head><title>LabReport</title><body>");
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.{HL7,hl7}.*"))  {
            success = 0;
            errors = 0;
            missing = 0;
            finalstatus = 0;
            fout.write("<h2>Beginning to process</h2>");
            for (Path entry : stream) {                
                //if( entry.toFile().isFile() ) {
                
                if( entry.getFileName().toString().toLowerCase().indexOf("olis") > -1 ) {
                    continue;
                }
                
                BasicFileAttributes basicFileAttributes = Files.readAttributes(entry, BasicFileAttributes.class);
                Date filecreatedate = new Date (basicFileAttributes.creationTime().toMillis());
                
                /*if( filecreatedate.before(limitDate) ) {
                    continue;
                }*/
                
                labList.add(entry);
            }
            
            Comparator<Path>comparator = new Comparator<Path>() {
             public int compare(Path o1, Path o2) {
                try {
                    return Files.getLastModifiedTime(o2).compareTo(Files.getLastModifiedTime(o1));
                } catch (IOException e) {
                    return -1;
                }
             }
            };
            
            Collections.sort(labList, comparator);
            String firstLine;
            String[] temp;
            for( Path entry : labList) {
                    ++idx;
                    fout.write(idx + " ");
                    MiscUtils.getLogger().info("Processing file number " + idx);
                    ArrayList<String> messages;
                    String fileContents;
                    String md5sum;
                    int fileId = 0;
                    try {                        
                        messages = Utilities.separateMessages(entry.toString());                    
                        
                    }catch( Exception e ) {
                        fout.write("<br>error processing " + entry.getFileName() + "<br>");                        
                        ++errors;
                        continue;
                    }
                    
                    
                    String dateLabReceived = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM).format(new Date(Files.getLastModifiedTime(entry).toMillis()));
                    
                    
                    for( String hl7 : messages ) {
                        firstLine = "";
                         temp = hl7.split("\\r?\\n");
                         for( String line : temp) {
                             if( org.apache.commons.lang.StringUtils.trimToNull(line) != null ) {
                                 firstLine = line;
                                 break;
                             }
                         }
                         
                        if( firstLine.indexOf("MDS") > -1 ) {
                            MessageHandler handler = Factory.getHandler("MDS", hl7);
                           
                            if( handler != null ) {
                                if( "F".equalsIgnoreCase(handler.getOrderStatus())) {
                                    ++finalstatus;                                    
                                }
                                    
                                List<Hl7TextInfo> dupResults = hl7TextInfoDao.searchByAccessionNumber(handler.getAccessionNum());
                                if( dupResults.size() == 0 ) {
                                    fout.write("<br>"+entry.getFileName() + " NOT FOUND ");                                
                                    accessionNums.append(entry.getFileName() + " accession: " + handler.getAccessionNum()+" Date: " + dateLabReceived + "<br>");
                                    ++missing;
                                    
                                    pdfList = writePdf(handler, entry.getFileName().toString(), cacheDir, dateLabReceived, pdfList);
                                    MiscUtils.getLogger().info("List size " + pdfList.size());
                                }  
                                else {
                                    fout.write("OK ");
                                    ++success;
                                }
                            }
                            else {
                                fout.write("<br>" + entry.getFileName() + " result is corrupted in file<br>");                                
                                ++errors;
                            }
                            
                            
                        }
                       else if( firstLine.indexOf("GDML") > -1 ) {
                            MessageHandler handler = Factory.getHandler("GDML", hl7);
                            if( handler != null ) {
                                if( "F".equalsIgnoreCase(handler.getOrderStatus())) {
                                    ++finalstatus;                                                                
                                }
                                List<Hl7TextInfo> dupResults = hl7TextInfoDao.searchByAccessionNumber(handler.getAccessionNum());
                                if( dupResults.size() == 0 ) {
                                    fout.write("<br>"+entry.getFileName() + " NOT FOUND ");                                
                                    accessionNums.append(entry.getFileName() + " accession: " + handler.getAccessionNum()+" Date: " + dateLabReceived + "<br>");
                                    ++missing;
                                    
                                    pdfList = writePdf(handler, entry.getFileName().toString(), cacheDir, dateLabReceived, pdfList);
                                    MiscUtils.getLogger().info("List size " + pdfList.size());
                                }     
                                else {
                                    fout.write("OK ");
                                    ++success;
                                }
                            }
                            else {
                                fout.write("<br>" + entry.getFileName() + " result is corrupted in file<br>");
                                ++errors;
                            }
                           
                        }
                        else if( firstLine.indexOf("CML") > -1 ) {
                            MessageHandler handler = Factory.getHandler("CML", hl7);
                            if( handler != null ) {
                                if( "F".equalsIgnoreCase(handler.getOrderStatus())) {
                                    ++finalstatus;                                    
                                }
                                List<Hl7TextInfo> dupResults = hl7TextInfoDao.searchByAccessionNumber(handler.getAccessionNum());
                                if( dupResults.size() == 0 ) {
                                    fout.write("<br>"+entry.getFileName() + " NOT FOUND ");                                
                                    accessionNums.append(entry.getFileName() + " accession: " + handler.getAccessionNum()+" Date: " + dateLabReceived + "<br>");
                                    ++missing;
                                    
                                    pdfList = writePdf(handler, entry.getFileName().toString(), cacheDir, dateLabReceived, pdfList);
                                    MiscUtils.getLogger().info("List size " + pdfList.size());
                                }     
                                else {
                                    fout.write("OK ");
                                    ++success;
                                }
                            }
                            else {
                                fout.write("<br>" + entry.getFileName() + " result is corrupted in file<br>");
                                ++errors;
                            }
                            
                        }
                    }
                //}
            }
        } catch( DirectoryIteratorException ex) {
            fout.write(ex.getMessage() +" "+ ex.getCause());
            throw ex;
        } catch (IOException e) {
            fout.write(e.getMessage() + " " + e.getCause());
            throw e;
        }
        
        zipPdfs(pdfList, docDir);
        String completeTime = String.valueOf((System.currentTimeMillis()-start)/1000L/60L);
        fout.write("<h2>Finished! Total files processed " + idx + " in " + completeTime +  " minutes</h2><table border='1' cellpadding='10'><tr><th>Errors processing labs</th><th>Labs without errors</th><th>Final Labs</th><th>TOTAL MISSING LABS</th><th>Missing Accession Numbers</th></tr><tr><td style='vertical-align:top'>" + 
                errors + "</td><td style='vertical-align:top'>" +
        success + "</td><td style='vertical-align:top'>" + finalstatus + "</td><td>"+ missing + "<td>" + accessionNums.toString() + "</td></tr></table>");
        fout.write("</body></html>");
        
        LisReports(out, request, dir);
    }
    finally {
        if( fout != null ) {
            fout.close();
        }
    }
    
}
else if( request.getParameter("file") != null ) {
    
    
    try {
        byte[] freader = Files.readAllBytes(Paths.get(docDir + "/" + request.getParameter("file")));
        
        if( request.getParameter("file").toLowerCase().endsWith("html")) {
            response.setContentType("text/html");
            out.print(new String(freader));
        }
        else if( request.getParameter("file").toLowerCase().endsWith("zip")) {
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment;filename=".concat(request.getParameter("file")));
            OutputStream o = response.getOutputStream();
            o.write(freader, 0, freader.length);
            o.close();
        }
        
    }
    catch( IOException e ) {
        ;
    }
    
}
else {
    LisReports(out, request, dir);
}
%>
<%!
    public void LisReports(JspWriter out, HttpServletRequest request, Path dir) throws ParseException, IOException {        
        List<Path>pathList = new ArrayList<Path>();
        
        Comparator<Path>comparator = new Comparator<Path>() {
             public int compare(Path o1, Path o2) {
                try {
                    return Files.getLastModifiedTime(o1).compareTo(Files.getLastModifiedTime(o2));
                } catch (IOException e) {
                    return -1;
                }
             }
            };

        out.print("<html><head><title>Missing Labs</title></head><body>");

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "labCheckReport.*.html"))  {
            out.println("<h2>Previously Run Reports</h2>");
            for( Path entry : stream ) {
                pathList.add(entry);
            }

            Collections.sort(pathList,comparator );
            
            for( Path entry : pathList ) {
                out.println("<a href='" + request.getContextPath() + "/system/labCheck.jsp?file=" + entry.getFileName()+"'>"+entry.getFileName()+"</a> Date: " +  DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM).format(new Date(Files.getLastModifiedTime(entry).toMillis())) + "<br>");
            }

            out.flush();
        }
        catch( IOException e) {
            ;
        }

       try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "missingLabs.*.zip"))  {
            pathList.clear();
            out.println("<h2>Missing Lab Pdfs</h2>");

            for( Path entry : stream ) {
                pathList.add(entry);
            }

            Collections.sort(pathList, comparator);

            for( Path entry : pathList ) {
                    out.println("<a href='" + request.getContextPath() + "/system/labCheck.jsp?file=" + entry.getFileName()+"'>"+entry.getFileName()+"</a> Date: " +  DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM).format(new Date(Files.getLastModifiedTime(entry).toMillis())) + " Size: " + (entry.toFile().length()/1024) + "KB<br>");
            }

            out.println("<h2><a href='" + request.getContextPath() + "/system/labCheck.jsp?run=true'>Run The Report Again</a></h2>");
            out.flush();
        }
        catch( IOException e) {
            ;
        }

        out.print("</body></html>");
    }

    public void zipPdfs(List<Path>pdfList, String docDir) throws IOException {
        byte[] buffer;        
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        String zipFile = docDir + "/missingLabs." + System.currentTimeMillis() + ".zip";
        try {
        
          fos = new FileOutputStream(zipFile);
          zos = new ZipOutputStream(fos);
          
          MiscUtils.getLogger().info("Preparing to add " + pdfList.size() + " files to zip");
          
          Path file;
          for (int i = 0; i < pdfList.size(); ++i) {
            file = pdfList.get(i);
             MiscUtils.getLogger().info("Adding " + file.getFileName() + " to zip file");
             ZipEntry ze = new ZipEntry(file.getFileName().toString());
             zos.putNextEntry(ze);
          
             
             buffer = Files.readAllBytes(file);
                
             zos.write(buffer, 0, buffer.length);
                
          
            zos.closeEntry();
            Files.delete(file);
          }
          

       }
       catch (Exception ex) {
        MiscUtils.getLogger().error("Error creating zip",ex);
          
       }
       finally {
       
          try {
          
             zos.close();
          }
          catch (IOException e) {
          
           ;
          }
       }
        
    }

    private Color getTextColor(MessageHandler handler, String abn){
        Color ret = Color.BLACK;
        if ( abn != null && ( abn.equals("A") || abn.startsWith("H")) ){
            ret = Color.RED;
        }else if ( abn != null && abn.startsWith("L")){
            ret = Color.BLUE;
        }
        
        if("CLS".equals(handler.getMsgType()) && abn.equals("C"))  {
        	//critical
        	ret = Color.RED;
        }
        return ret;
    }

    private Document addLabCategory(String header, MessageHandler handler, Font boldFont, Font font, BaseFont bf, Document document) throws DocumentException {
		
		
		float[] mainTableWidths;
		
		
		
		mainTableWidths = new float[] {5f, 3f, 1f, 3f, 2f, 4f, 2f };
		
		
		PdfPTable table = new PdfPTable(mainTableWidths);
		
		table.setHeaderRows(3);
		table.setWidthPercentage(100);

		PdfPCell cell = new PdfPCell();
		// category name
		
		cell.setPadding(3);
		cell.setPhrase(new Phrase("  "));
		cell.setBorder(0);
		cell.setColspan(7);
		table.addCell(cell);
		cell.setBorder(15);
		cell.setPadding(3);
		cell.setColspan(2);
		cell.setPhrase(new Phrase(header.replaceAll("<br\\s*/*>", "\n"),
				new Font(bf, 12, Font.BOLD)));
		table.addCell(cell);
		cell.setPhrase(new Phrase("  "));
		cell.setBorder(0);
		cell.setColspan(5);
		table.addCell(cell);

		// table headers
		cell.setColspan(1);
		cell.setBorder(15);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBackgroundColor(new Color(210, 212, 255));
		cell.setPhrase(new Phrase("Test Name(s)", boldFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Result", boldFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Abn", boldFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Reference Range", boldFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Units", boldFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Date/Time Completed", boldFont));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Status", boldFont));
		table.addCell(cell); 


		// add test results
		int obrCount = handler.getOBRCount();
		int linenum = 0;
		cell.setBorder(12);
		cell.setBorderColor(Color.BLACK); // cell.setBorderColor(Color.WHITE);
		cell.setBackgroundColor(new Color(255, 255, 255));

		if (handler.getMsgType().equals("MEDVUE")) {

			//cell.setBackgroundColor(getHighlightColor(linenum));
			linenum++;
			cell.setPhrase(new Phrase(handler.getRadiologistInfo(), boldFont));
			cell.setColspan(7);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			table.addCell(cell);
			cell.setPaddingLeft(100);
			cell.setColspan(7);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			cell.setPhrase(new Phrase(handler.getOBXComment(1, 1, 1)
					.replaceAll("<br\\s*/*>", "\n"), font));
			table.addCell(cell);

		} else {
			for (int j = 0; j < obrCount; j++) {
				boolean obrFlag = false;
				int obxCount = handler.getOBXCount(j);
				for (int k = 0; k < obxCount; k++) {
					String obxName = handler.getOBXName(j, k);
					
					boolean isAllowedDuplicate = false;
					if(handler.getMsgType().equals("PATHL7")){
						//if the obxidentifier and result name are any of the following, they must be displayed (they are the Excepetion to Excelleris TX/FT duplicate result name display rules)
						if((handler.getOBXName(j, k).equals("Culture") && handler.getOBXIdentifier(j, k).equals("6463-4")) || 
								(handler.getOBXName(j, k).equals("Organism") && (handler.getOBXIdentifier(j, k).equals("X433") || handler.getOBXIdentifier(j, k).equals("X30011")))){
		   					isAllowedDuplicate = true;
		   				}
					}
					if (!handler.getOBXResultStatus(j, k).equals("TDIS")) {

						// ensure that the result is a real result
						if ((!handler.getOBXResultStatus(j, k).equals("DNS")
								&& !obxName.equals("")
								&& header.equals(handler.getObservationHeader(j, k))) || 
								(handler.getMsgType().equals("EPSILON") && header.equals(handler.getOBXIdentifier(j,k)) && !obxName.equals("")) 
								|| (handler.getMsgType().equals("PFHT") && !obxName.equals("") && header.equals(handler.getObservationHeader(j,k)))) { // <<-- DNS only needed for
													// MDS messages
							String obrName = handler.getOBRName(j);
							// add the obrname if necessary
							if (!obrFlag
									&& !obrName.equals("")
									&& (!(obxName.contains(obrName) && obxCount < 2 ))) {
								// cell.setBackgroundColor(getHighlightColor(linenum));
								linenum++;
								cell.setPhrase(new Phrase(obrName, boldFont));
								cell.setColspan(7);
								cell.setHorizontalAlignment(Element.ALIGN_LEFT);
								table.addCell(cell);
								cell.setColspan(1);
								obrFlag = true;
							}

							// add the obx results and info
							Font lineFont = new Font(bf, 8, Font.NORMAL,
									getTextColor(handler,handler.getOBXAbnormalFlag(j,
											k)));
							// cell.setBackgroundColor(getHighlightColor(linenum));
							linenum++;
							
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							if(!isAllowedDuplicate && (obxCount>1) && k > 0 && handler.getOBXIdentifier(j, k).equals(handler.getOBXIdentifier(j, k-1)) && (handler.getOBXValueType(j, k).equals("TX") || handler.getOBXValueType(j, k).equals("FT"))){
								cell.setPhrase(new Phrase("", lineFont));
								table.addCell(cell);
							}
							else{
							cell.setPhrase(new Phrase((obrFlag ? "   " : "")
									+ obxName, lineFont));
							table.addCell(cell);}
							boolean isLongText =false;
							if(handler.getMsgType().equals("PATHL7")){
								cell.setPhrase(new Phrase(handler.getOBXResult(j, k).replaceAll("<br\\s*/*>", "\n").replace("\t","\u00a0\u00a0\u00a0\u00a0"), lineFont));
								//if this PATHL7 result is from CDC/SG and is greater than 100 characters
								if((handler.getOBXResult(j, k).length() > 100) && (handler.getPatientLocation().equals("SG") || handler.getPatientLocation().equals("CDC"))){
									cell.setHorizontalAlignment(Element.ALIGN_LEFT);
									//if the Abn, Reference Range and Units are empty or equal to null, give the long result the use of those columns
									if(( handler.getOBXAbnormalFlag(j, k) == null ||handler.getOBXAbnormalFlag(j, k).isEmpty()) &&
									( handler.getOBXReferenceRange(j, k) == null || handler.getOBXReferenceRange(j, k).isEmpty()) &&
									(handler.getOBXUnits(j, k) == null || handler.getOBXUnits(j, k).isEmpty())){
										isLongText = true;
										cell.setColspan(4);
										table.addCell(cell);
									}else{//else use the 6 remaining columns, and add a new empty cell that takes the first two columns(Test & Results). 
										//This will allow the corresponding Abn, RR and Units to be printed beneath the long result in the appropriate columns
										cell.setColspan(6);
										table.addCell(cell);
										cell.setPhrase(new Phrase("", lineFont));
										cell.setColspan(2);
										table.addCell(cell);
									}
								}else{
									cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
									table.addCell(cell);}
							}else{
							cell.setPhrase(new Phrase(handler
									.getOBXResult(j, k).replaceAll(
											"<br\\s*/*>", "\n"), lineFont));
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							table.addCell(cell);}
							cell.setColspan(1);
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							
							String abnFlag = handler.getOBXAbnormalFlag(j, k);
							if(!isLongText){//if the Abn, RR and Unit columns have not been occupied above
								if(handler.getMsgType().equals("PATHL7")){
									cell.setPhrase(new Phrase(abnFlag, lineFont));
								}else{
								if (abnFlag == null || abnFlag.trim().equals(""))
									abnFlag = "N";
								cell.setPhrase(new Phrase(
										abnFlag,
										lineFont));}
								table.addCell(cell);
								cell.setHorizontalAlignment(Element.ALIGN_LEFT);
								cell.setPhrase(new Phrase(handler
										.getOBXReferenceRange(j, k), lineFont));
								table.addCell(cell);
								cell.setPhrase(new Phrase(
										handler.getOBXUnits(j, k), lineFont));
								table.addCell(cell);}// end of isLongText
							cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							cell.setPhrase(new Phrase(handler
									.getTimeStamp(j, k), lineFont));
							table.addCell(cell);
							cell.setPhrase(new Phrase(handler
									.getOBXResultStatus(j, k), lineFont));
							table.addCell(cell);
							
						if(!handler.getMsgType().equals("PFHT")) {
							// add obx comments
							if (handler.getOBXCommentCount(j, k) > 0) {
								// cell.setBackgroundColor(getHighlightColor(linenum));
								linenum++;
								cell.setPaddingLeft(100);
								cell.setColspan(7);
								cell.setHorizontalAlignment(Element.ALIGN_LEFT);
								for (int l = 0; l < handler.getOBXCommentCount(
										j, k); l++) {

									cell.setPhrase(new Phrase(handler
											.getOBXComment(j, k, l).replaceAll(
													"<br\\s*/*>", "\n"), font));
									table.addCell(cell);

								}
								cell.setPadding(3);
								cell.setColspan(1);
							}
						}
						// if (DNS)
						} else if ((handler.getMsgType().equals("EPSILON") && header.equals(handler.getOBXIdentifier(j,k)) && obxName.equals("")) || (handler.getMsgType().equals("PFHT") && obxName.equals("")&& header.equals(handler.getObservationHeader(j,k)))){
							// cell.setBackgroundColor(getHighlightColor(linenum));
							linenum++;
							cell.setPaddingLeft(100);
							cell.setColspan(7);
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell.setPhrase(new Phrase(handler
									.getOBXResult(j, k).replaceAll(
											"<br\\s*/*>", "\n"), font));
							table.addCell(cell);
							cell.setPadding(3);
							cell.setColspan(1);
						
						}
						if (handler.getMsgType().equals("PFHT") && !handler.getNteForOBX(j,k).equals("") && handler.getNteForOBX(j,k)!=null) {
							// cell.setBackgroundColor(getHighlightColor(linenum));
							linenum++;
							cell.setPaddingLeft(100);
							cell.setColspan(7);
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell.setPhrase(new Phrase(handler.getNteForOBX(j, k).replaceAll("<br\\s*/*>", "\n"),font));
							table.addCell(cell);
							cell.setPadding(3);
							cell.setColspan(1);
							
							if (handler.getOBXCommentCount(j, k) > 0) {
								// cell.setBackgroundColor(getHighlightColor(linenum));
								linenum++;
								cell.setPaddingLeft(100);
								cell.setColspan(7);
								cell.setHorizontalAlignment(Element.ALIGN_LEFT);
								for (int l = 0; l < handler.getOBXCommentCount(
										j, k); l++) {

									cell.setPhrase(new Phrase(handler
											.getOBXComment(j, k, l).replaceAll(
													"<br\\s*/*>", "\n"), font));
									table.addCell(cell);

								}
								cell.setPadding(3);
								cell.setColspan(1);
							}
						}
					}else {
						if (handler.getOBXCommentCount(j, k) > 0) {
							// cell.setBackgroundColor(getHighlightColor(linenum));
							linenum++;
							cell.setPaddingLeft(100);
							cell.setColspan(7);
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							for (int l = 0; l < handler
									.getOBXCommentCount(j, k); l++) {

								cell.setPhrase(new Phrase(handler
										.getOBXComment(j, k, l).replaceAll(
												"<br\\s*/*>", "\n"), font));
								table.addCell(cell);

							}
							cell.setPadding(3);
							cell.setColspan(1);
						}
					} // if (!handler.getOBXResultStatus(j, k).equals("TDIS"))
				}
				
			if (!handler.getMsgType().equals("PFHT")) {
				// add obr comments
                                    String hdr;                                
                                try {
                                    hdr = handler.getObservationHeader(j, 0);
                                }
                                catch(Exception e ) {
                                    hdr = "";
                                }

				if (hdr.equals(header)) {
					cell.setColspan(7);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					for (int k = 0; k < handler.getOBRCommentCount(j); k++) {
						// the obrName should only be set if it has not been
						// set already which will only have occured if the
						// obx name is "" or if it is the same as the obr name
						if (!obrFlag && handler.getOBXName(j, 0).equals("")) {
							// cell.setBackgroundColor(getHighlightColor(linenum));
							linenum++;

							cell.setPhrase(new Phrase(handler.getOBRName(j),
									boldFont));
							table.addCell(cell);
							obrFlag = true;
						}

						// cell.setBackgroundColor(getHighlightColor(linenum));
						linenum++;
						//cell.setPaddingLeft(100);
						if (handler.getMsgType().equals("TRUENORTH")) {
							try {
								Phrase phrase= new Phrase();
								StringReader strReader = new StringReader(handler.getOBRComment(j, k));
								@SuppressWarnings("rawtypes")
                                ArrayList p = HTMLWorker.parseToList(strReader, null);
								strReader.close();
								for (int h=0; h<p.size();h++) {
									phrase.add(p.get(h));
									phrase.add("\n");
								}
								cell.setPhrase(phrase);
							} catch (Exception e) {
					            throw new ExceptionConverter(e);
					        }
							
						} else {
							cell.setPhrase(new Phrase(handler.getOBRComment(j, k)
									.replaceAll("<br\\s*/*>", "\n"), font));
						}
						table.addCell(cell);
						cell.setPadding(3);
					}
					cell.setColspan(1);
				}
			}
			} // for (j)

		}// if (isMEDVUE)

		document.add(table);
                return document;
	}


    private PdfPTable addTableToTable(PdfPTable main, PdfPTable add, int colspan){
        PdfPCell cell = new PdfPCell(add);
        cell.setPadding(3);
        cell.setColspan(colspan);
        main.addCell(cell);
        return main;
    }

    private Document createInfoTable(Document document, MessageHandler handler, Font boldFont, Font font, String dateLabReceived) throws DocumentException{

        //Create patient info table
        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        float[] pInfoWidths = {2f, 4f, 3f, 2f};
        PdfPTable pInfoTable = new PdfPTable(pInfoWidths);
        cell.setPhrase(new Phrase("Patient Name: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getPatientName(), font));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase("Home Phone: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getHomePhone(), font));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase("Date of Birth: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getDOB(), font));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase("Work Phone: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getWorkPhone(), font));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase("Age: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getAge(), font));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase("Sex: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getSex(), font));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase("Health #: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getHealthNum(), font));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase("Patient Location: ", boldFont));
        pInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getPatientLocation(), font));
        pInfoTable.addCell(cell);

        //Create results info table
        PdfPTable rInfoTable = new PdfPTable(2);
        cell.setPhrase(new Phrase("Date of Service: ", boldFont));
        rInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getServiceDate(), font));
        rInfoTable.addCell(cell);
        cell.setPhrase(new Phrase("Date Received: ", boldFont));
        rInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(dateLabReceived, font));
        rInfoTable.addCell(cell);
        cell.setPhrase(new Phrase("Report Status: ", boldFont));
        rInfoTable.addCell(cell);
        if(handler.getMsgType().equals("PATHL7")){
        	cell.setPhrase(new Phrase((handler.getOrderStatus().equals("F") ? "Final" : (handler.getOrderStatus().equals("C") ? "Corrected" : "Preliminary")), font));
        	rInfoTable.addCell(cell);
        }else{
        cell.setPhrase(new Phrase((handler.getOrderStatus().equals("F") ? "Final" : (handler.getOrderStatus().equals("C") ? "Corrected" : "Partial")), font));
        rInfoTable.addCell(cell);}
        cell.setPhrase(new Phrase("Client Ref. #: ", boldFont));
        rInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getClientRef(), font));
        rInfoTable.addCell(cell);
        cell.setPhrase(new Phrase("Accession #: ", boldFont));
        rInfoTable.addCell(cell);
        cell.setPhrase(new Phrase(handler.getAccessionNum(), font));
        rInfoTable.addCell(cell);

        //Create client table
        float[] clientWidths = {2f, 3f};
        Phrase clientPhrase = new Phrase();
        PdfPTable clientTable = new PdfPTable(clientWidths);
        clientPhrase.add(new Chunk("Requesting Client:  ", boldFont));
        clientPhrase.add(new Chunk(handler.getDocName(), font));
        cell.setPhrase(clientPhrase);
        clientTable.addCell(cell);

        clientPhrase = new Phrase();
        clientPhrase.add(new Chunk("cc: Client:  ", boldFont));
        clientPhrase.add(new Chunk(handler.getCCDocs(), font));
        cell.setPhrase(clientPhrase);
        clientTable.addCell(cell);

        //Create header info table
        float[] tableWidths = {2f, 1f};
        PdfPTable table = new PdfPTable(tableWidths);
        
        cell = new PdfPCell(new Phrase("Detail Results: Patient Info", boldFont));
        cell.setBackgroundColor(new Color(210, 212, 255));
        cell.setPadding(3);
        table.addCell(cell);
        cell.setPhrase(new Phrase("Results Info", boldFont));
        table.addCell(cell);

        // add the created tables to the document
        table = addTableToTable(table, pInfoTable, 1);
        table = addTableToTable(table, rInfoTable, 1);
        table = addTableToTable(table, clientTable, 2);

        table.setWidthPercentage(100);

        document.add(table);

        return document;
    }

    public List<Path> writePdf(final MessageHandler handler, String labfilename, String dir, String dateLabReceived, List<Path>pdfList) throws FileNotFoundException, DocumentException, IOException {
        String pdfFilename = dir + "missingLab." + System.currentTimeMillis() + "-" + handler.getMsgType() + "-" +  handler.getFirstName() + "-" + handler.getLastName() +".pdf";
        Path path = FileSystems.getDefault().getPath(pdfFilename);
        
        FileOutputStream os = new FileOutputStream(pdfFilename);
        

        Document document = new Document();
        //PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
        // PdfWriter writer = PdfWriter.getInstance(document, os);
        PdfWriter writer = PdfWriterFactory.newInstance(document, os, FontSettings.HELVETICA_10PT);

        //Set page event, function onEndPage will execute each time a page is finished being created
        PdfPageEventHelper pdfeh = new PdfPageEventHelper() {
            
            public void onEndPage(PdfWriter writer, Document document){
        try {

            Rectangle page = document.getPageSize();
            PdfContentByte cb = writer.getDirectContent();
            BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            int pageNum = document.getPageNumber();
            float width = page.getWidth();
            float height = page.getHeight();

            //add patient name header for every page but the first.
            if (pageNum > 1){
                cb.beginText();
                cb.setFontAndSize(bf, 8);
                cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, handler.getPatientName(), 575, height - 30, 0);
                cb.endText();

            }

            //add footer for every page
            cb.beginText();
            cb.setFontAndSize(bf, 8);
            cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "-"+pageNum+"-", width/2, 30, 0);
            cb.endText();


            // add promotext as footer if it is enabled
            if ( OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT") != null){
                cb.beginText();
                cb.setFontAndSize(BaseFont.createFont(BaseFont.HELVETICA,BaseFont.CP1252,BaseFont.NOT_EMBEDDED), 6);
                cb.showTextAligned(PdfContentByte.ALIGN_CENTER, OscarProperties.getInstance().getProperty("FORMS_PROMOTEXT"), width/2, 19, 0);
                cb.endText();
            }

        // throw any exceptions
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
        
    }
        

};
       
        writer.setPageEvent(pdfeh);
        
        document.setPageSize(PageSize.LETTER);
        document.addTitle("Title of the Document");
        document.addCreator("OSCAR");
        document.open();

        //Create the fonts that we are going to use
        BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        Font font = new Font(bf, 9, Font.NORMAL);
        Font boldFont = new Font(bf, 10, Font.BOLD);
      //  redFont = new Font(bf, 9, Font.NORMAL, Color.RED);

        // add the header table containing the patient and lab info to the document
        document = createInfoTable(document, handler, boldFont, font, dateLabReceived);

        // add the tests and test info for each header
        ArrayList<String> headers = handler.getHeaders();
        for (int i=0; i < headers.size(); i++)
            document = addLabCategory(headers.get(i), handler, boldFont, font, bf, document);

        
        // add end of report table
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        cell.setPhrase(new Phrase("  "));
        table.addCell(cell);
        cell.setBorder(15);
        cell.setBackgroundColor(new Color(210, 212, 255));
        cell.setPhrase(new Phrase("END OF REPORT", boldFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
        document.add(table);

        document.close();

        os.flush();

        os.close();    
        MiscUtils.getLogger().info("Adding " + path.getFileName() + " to list");
        pdfList.add(path);
        return pdfList;
}

    
%>